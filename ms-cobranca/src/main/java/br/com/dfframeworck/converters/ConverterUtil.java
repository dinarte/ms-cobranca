package br.com.dfframeworck.converters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Component;

import br.com.dfframeworck.repository.MigrationService;

@Component
public class ConverterUtil {
	
	@Autowired
	ApplicationContext appContext;
	
	@Autowired
	MigrationService mService;
	
	@Autowired
	PersistableConverter persistableConverter;
	
	private Map<Class<?>, IConverter<?>> converters = new HashMap<>();
	
	@PostConstruct
	public void init() throws ClassNotFoundException {
		
		Map<String, Object> convertersBeans = appContext.getBeansWithAnnotation(DataMigrationConverter.class);
		
		convertersBeans.values().forEach(converter -> {
			DataMigrationConverter dataMigrationConverter = converter.getClass().getAnnotation(DataMigrationConverter.class);
			Class<?>[] types = dataMigrationConverter.types();
			for (Class<?> type : types) {
				converters.put(type, (IConverter<?>) converter);
			}
		});
		
		mService.getAllMigrables().forEach(type-> converters.put(type, persistableConverter));
		
	}
	
	
	public void invoke(String value, Class<?> type, Method methode, Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		methode.invoke(obj, converters.get(type).parse(value, type));
	}

}
