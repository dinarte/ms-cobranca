package br.com.dfframeworck.autocrud.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.dfframeworck.autocrud.AutoCrudEntity;
import br.com.dfframeworck.autocrud.AutoCrudField;
import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.exception.ErroException;
import br.com.dfframeworck.util.ObjectToMap;
import br.com.dfframeworckservice.AutoCrudService;

@Component
public class AutoCrudHelper {
	
	@Autowired
	EntityManager em;
	
	
	public List<String> processFilters(AutoCrudEntity crudEntity, HttpServletRequest request) {
		
		List<String> filtros = new ArrayList<String>();
		
		request.getParameterMap()
			.keySet()
			.stream()
			.filter(k -> k.contains(crudEntity.getEntityName()+"."))
			.forEach(k -> {
			
				if ( request.getParameter(k) != null && !request.getParameter(k).equals("")) {
					AutoCrudField field = crudEntity.getField(k.replace(crudEntity.getEntityName()+".", ""));
					Map<String, String> operatorDetails = AutoCrudService.getOperatorsDetails(field.getType());
					System.out.println(field.getFieldName());
					String filter = field.getFieldName() +" "+ operatorDetails.get("operator") + " " + operatorDetails.get("quot").replace("{value}", request.getParameter(k));
					//System.out.println(filter);
					filtros.add(filter);
				}
				
			});
		
		return filtros;
		
	}
	
	@SuppressWarnings("unchecked")
	public Object processEntityObject(AutoCrudEntity crudEntity, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException{
		
		Map<String, Object> data = new HashMap<>();
		
		request.getParameterMap()
			.keySet()
			.stream()
			.filter(k -> k.contains(crudEntity.getEntityName()+"."))
			.forEach(k -> {
			
				if ( request.getParameter(k) != null && !request.getParameter(k).equals("")) {
					
					AutoCrudField field = crudEntity.getField(k.replace(crudEntity.getEntityName()+".", ""));
					System.out.println(field.getFieldName());
					Object value =  request.getParameter(k);
					if (field.isLookUp() && !field.getType().isEnum()) {
						value = new HashMap<String, Object>();
						((HashMap<String,Object>)value).put("id", request.getParameter(k));
					}
						
					data.put(field.getFieldName(),  value);
				}
				
			});
		
		return ObjectToMap.toObject(data, crudEntity.getType());
	}

	
	public AutoCrudEntity getAutoCrudEntity(String entity, EntityType<?> meta) {
		return getAutoCrudEntity(entity, meta, null);
	}
	
	
	public AutoCrudEntity getAutoCrudEntity(String entity, EntityType<?> meta, Object obj) {
		AutoCrudEntity crudEntity = new AutoCrudEntity();
		crudEntity.setEntityName(entity);
		crudEntity.setType(meta.getJavaType());
		crudEntity.setFields(new ArrayList<AutoCrudField>());
		
		meta.getAttributes().forEach( atribute -> {
			AutoCrudField field = new AutoCrudField();
			field.setType(atribute.getJavaType());
			field.setFieldName(atribute.getName());
			field.setEntity(crudEntity);;
			if (Objects.nonNull(obj)) {
				Map<String, Object> data = new HashMap<String, Object>();
				data = ObjectToMap.toMap(obj);
				field.setValue(data.get(field.getFieldName()));
			}
			
			crudEntity.getFields().add(field);
		});
		return crudEntity;
	}


	public AutoCrudEntity constructCrudEntity(String entity) throws ErroException {
		return constructCrudEntity(entity, null);
	}

	public AutoCrudEntity constructCrudEntity(String entity, Object obj) throws ErroException {
		EntityType<?> meta = validateIsAutoCrudEnabledAndReturnEntity(entity);
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
