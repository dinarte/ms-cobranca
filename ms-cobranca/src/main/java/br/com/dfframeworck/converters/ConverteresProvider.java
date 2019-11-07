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
		Map<String, Object> convertersBeans = appContext.getBeansWithAnnotation(DataConverter.class);
		
		convertersBeans.values().forEach(converter -> {
			DataConverter dataMigrationConverter = converter.getClass().getAnnotation(DataConverter.class);
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

	public void invokeForMigration(String value, Class<?> type, Method methode, Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (migrationConverters.get(type) == null)
			throw new RuntimeException("Não foi possível encontrar um converter para o tipo "+ type.getName());
		methode.invoke(obj, migrationConverters.get(type).parse(value, type));
	}

}
