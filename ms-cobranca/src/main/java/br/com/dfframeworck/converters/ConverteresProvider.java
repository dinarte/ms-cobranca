package br.com.dfframeworck.converters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.spel.ast.OpInc;
import org.springframework.stereotype.Component;

import br.com.dfframeworck.repository.MigrationService;

@Component
public class ConverteresProvider {
	
	@Autowired
	ApplicationContext appContext;
	
	@Autowired
	MigrationService mService;
	
	@Autowired
	PersistableMigrationConverter persistableMigrationConverter;
	
	@Autowired
	PersistableFormConverter persistableFormConverter;
	
	private Map<Class<?>, IConverter<?>> migrationConverters = new HashMap<>();

	private Map<Class<?>, IConverter<?>> formConverters = new HashMap<>();
	
	@PostConstruct
	public void init() throws ClassNotFoundException {
		initConvertersLists();
	}


	private void initConvertersLists() throws ClassNotFoundException {
		Map<String, Object> convertersBeans = appContext.getBeansWithAnnotation(EnableDataConver.class);
		
		convertersBeans.values().forEach(converter -> {
			
			EnableDataConver dataMigrationConverter = converter.getClass().getAnnotation(EnableDataConver.class);
			
			Class<?>[] types = dataMigrationConverter.types();
		
			if (dataMigrationConverter.enableForMigration())
				for (Class<?> type : types) {
					migrationConverters.put(type, (IConverter<?>) converter);
				}
			if (dataMigrationConverter.enableForForm())
				for (Class<?> type : types) {
					formConverters.put(type, (IConverter<?>) converter);
				}
		});
		
		mService.getAllMigrables().forEach(type-> {
			migrationConverters.put(type, persistableMigrationConverter);
			formConverters.put(type, persistableFormConverter);
		});
	}
	
	public IConverter<?> getForMigration(Class<?> type){
		Optional<IConverter<?>> oConverter = Optional.ofNullable(migrationConverters.get(type));  
		return oConverter.orElse(getNNO());
	}
	
	
	public IConverter<?> getForForm(Class<?> type){
		Optional<IConverter<?>> oConverter = Optional.ofNullable(formConverters.get(type));  
		return oConverter.orElse(getNNO());
	}
	
	private IConverter<?> getNNO(){
		return new IConverter<Object>() {

			@Override
			public Object parse(String value, Class<?> type) {
				return value;
			}
		};
	}

	public void invokeForMigration(String value, Class<?> type, String field, Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (migrationConverters.get(type) == null)
			throw new RuntimeException("Não foi possível encontrar um converter para o tipo "+ type.getName());
		Method methode = obj.getClass().getDeclaredMethod("set"+firstCharUpper(field), type);
		methode.invoke(obj, migrationConverters.get(type).parse(value, type));
	}
	
	public void invokeForForm(String value, Class<?> type, String field, Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (formConverters.get(type) == null)
			throw new RuntimeException("Não foi possível encontrar um converter para o tipo "+ type.getName());
		Method methode = obj.getClass().getDeclaredMethod("set"+firstCharUpper(field), type);
		methode.invoke(obj, formConverters.get(type).parse(value, type));
	}
	
	public String firstCharUpper(String str) {
		return str.trim().replace(" ", "").substring(0, 1).toUpperCase() + str.substring(1);
	}

}
