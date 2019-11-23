package br.com.dfframeworck.autocrud.components;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Component;

import br.com.dfframeworck.autocrud.AutoCrudEntity;
import br.com.dfframeworck.autocrud.AutoCrudField;
import br.com.dfframeworck.autocrud.Periodo;
import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.controller.AutoCrudControllerCustomizer;
import br.com.dfframeworck.controller.AutoCrudCusomizerProvider;
import br.com.dfframeworck.converters.ConverteresProvider;
import br.com.dfframeworck.converters.IConverter;
import br.com.dfframeworck.exception.ErroException;
import br.com.dfframeworck.util.SerializationException;
import br.com.dfframeworck.util.SerializationUtils;
import br.com.dfframeworckservice.AutoCrudService;

@Component
public class AutoCrudHelper {
	
	@Autowired
	EntityManager em;
	
	@Autowired
	ConverteresProvider converterProvider;
	
	@Autowired
	AutoCrudCusomizerProvider customizerProviger;
	
	private IConverter<?> getConverter(Class<?> type){
		return converterProvider.getForForm(type);
	}
	
	public List<String> processFilters(AutoCrudEntity crudEntity, HttpServletRequest request) {
		
		List<String> filtros = new ArrayList<String>();
		
		request.getParameterMap()
			.keySet()
			.stream()
			.filter(k -> k.contains(crudEntity.getEntityName()+"."))
			.forEach(k -> {
			
				if ( request.getParameter(k) != null && !request.getParameter(k).equals("")) {
					AutoCrudField field = crudEntity.getField(k.replace(crudEntity.getEntityName()+".", ""));
					
					IConverter<?> converter = getConverter(field.getType());					
					field.setValue(converter.parse(request.getParameter(k), field.getType()));
					
					Map<String, String> operatorDetails = AutoCrudService.getOperatorsDetails(field.getType());
									
					String value = field.getValue().toString(); 
					try {
						if (field.getType().asSubclass(Persistable.class) != null) {
							value = ((Persistable<?>)field.getValue()).getId().toString();
							field.setValue(SerializationUtils.toMap(field.getValue()));
						}
					}catch (ClassCastException e) {
						// NADA PARA FAZER
					}	
					
					String path = field.getFieldName();
					if (Strings.isNotBlank(field.getMeta().path()))
						path = field.getMeta().path();
					
					String filter = path +" "+ operatorDetails.get("operator") + " " + operatorDetails.get("quot").replace("{value}", value);
					
					
					filtros.add(filter);
				}
				
			});
		
		return filtros;
		
	}
	
	@Deprecated
	public Object processEntityObject(AutoCrudEntity crudEntity, HttpServletRequest request) throws SerializationException {
		
		Map<String, Object> data = new HashMap<>();
		
		request.getParameterMap()
			.keySet()
			.stream()
			.filter(k -> k.contains(crudEntity.getEntityName()+"."))
			.forEach(k -> {
				System.out.println(k);
				if ( request.getParameter(k) != null && !request.getParameter(k).equals("")) {
					
					AutoCrudField field = crudEntity.getField(k.replace(crudEntity.getEntityName()+".", ""));
					System.out.println(field.getFieldName());
					Object value; 
						value = getConverter(field.getType()).parse(request.getParameter(k),field.getType());
						if (field.isLookUp()) { 
							value = SerializationUtils.toMap(value);
						}	
						
					data.put(field.getFieldName(), value);
				}
				
			});
		
		return SerializationUtils.toObject(data, crudEntity.getType());
	}
	
	
	public Object processEntityObjectNew(AutoCrudEntity crudEntity, HttpServletRequest request, Object obj) throws SerializationException {
		request.getParameterMap()
			.keySet()
			.stream()
			.filter(k -> k.contains(crudEntity.getEntityName()+"."))
			.forEach(k -> {
				System.out.println(k);
				if ( request.getParameter(k) != null && !request.getParameter(k).equals("")) {
					
					AutoCrudField field = crudEntity.getField(k.replace(crudEntity.getEntityName()+".", ""));
					
					try {
						converterProvider.invokeForForm(request.getParameter(k), field.getType(), field.getFieldName(), obj);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
							| NoSuchMethodException | SecurityException e) {
						throw new SerializationException("Erro na conversão de valores", e);
					}
				}
				
			});
		
		return obj;
	}
	

	
	public AutoCrudEntity getAutoCrudEntity(String entity, EntityType<?> meta) throws InstantiationException, IllegalAccessException {
		return getAutoCrudEntity(entity, meta, meta.getJavaType().newInstance());
	}
	
	
	public AutoCrudEntity getAutoCrudEntity(String entity, EntityType<?> meta, Object obj) throws InstantiationException, IllegalAccessException {
		AutoCrudEntity crudEntity = new AutoCrudEntity();
		crudEntity.setEntityName(entity);
		crudEntity.setType(meta.getJavaType());
		crudEntity.setFields(new ArrayList<AutoCrudField>());
		crudEntity.setObj((Persistable<?>) obj);
		
		meta.getAttributes().forEach( atribute -> {
			AutoCrudField field = new AutoCrudField();
			field.setType(atribute.getJavaType());
			field.setFieldName(atribute.getName());
			field.setEntity(crudEntity);
			try {
				field.setMeta(crudEntity.getType().getDeclaredField(field.getFieldName()).getAnnotation(EnableAutoCrudField.class));
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
			if (Objects.nonNull(obj)) {
				Map<String, Object> data = new HashMap<String, Object>();
				try {
					data = SerializationUtils.toMap(obj);
				} catch (SerializationException e) {
					throw new RuntimeException(e);
				}
				field.setValue(data.get(field.getFieldName()));
			}
			
			crudEntity.getFields().add(field);
		});
		
		AutoCrudControllerCustomizer customizer = customizerProviger.getCustomizer(crudEntity.getType());  
		List<Field> fields = Arrays.asList( customizer.getClass().getDeclaredFields() );
		fields.forEach( javaField -> {
			if (javaField.isAnnotationPresent(EnableAutoCrudField.class)) {
				AutoCrudField field = new AutoCrudField();
				field.setType(javaField.getType());
				field.setFieldName(javaField.getName());
				field.setEntity(crudEntity);
				field.setMeta(javaField.getDeclaredAnnotation(EnableAutoCrudField.class));
				try {
					field.setValue(javaField.get(customizer));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				crudEntity.getFields().add(field);
			}
			
			
		});
		
		
		
		return crudEntity;
	}


	public AutoCrudEntity constructCrudEntity(String entity) throws ErroException, InstantiationException, IllegalAccessException {
		return constructCrudEntity(entity, null);
	}

	public AutoCrudEntity constructCrudEntity(String entity, Object obj) throws ErroException, InstantiationException, IllegalAccessException {
		EntityType<?> meta = validateIsAutoCrudEnabledAndReturnEntity(entity);
		if (Objects.isNull(obj))
			obj = meta.getJavaType().newInstance();
		AutoCrudEntity crudEntity = getAutoCrudEntity(entity, meta, obj);
		return crudEntity;
	}
	
	
	/**
	 * Valida se a entidade que está endo acessada existe ou possue a anotação AuntoCrud
	 * @param entity
	 * @throws ErroException
	 */
	@SuppressWarnings("deprecation")
	public EntityType<?> validateIsAutoCrudEnabledAndReturnEntity(String entity) throws ErroException {
	  Set<javax.persistence.metamodel.EntityType<?>> entities = ((Session) em.getDelegate()).getSessionFactory().getMetamodel().getEntities() ;
      EntityType<?> entityType = entities.stream().filter(e -> e.getName().equals(entity)).findFirst().orElseThrow(()->throwAutoCrudNotAllowadException(entity));
      if (!entityType.getJavaType().isAnnotationPresent(AutoCrud.class))
    	 throw throwAutoCrudNotAllowadException(entity);
      return entityType;
	}
	
	/**
	 * Cria um ErroException no sistema para o caso de a entidade ue está sendo acessada não estar acessível pelo AutoCrud
	 * @param entity
	 * @return
	 */
	private ErroException throwAutoCrudNotAllowadException(String entity) {
		return new ErroException(entity + " Não é uma entidade do sistema, ou não é acessível pelo Auto Crud");
	}

	
	
	//new ObjectToMap()

}
