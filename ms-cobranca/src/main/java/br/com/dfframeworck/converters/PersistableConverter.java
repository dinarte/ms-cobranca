package br.com.dfframeworck.converters;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Component;

import br.com.dfframeworck.repository.MigrationService;

@Component
/**
 * Conversor padrão para entidades
 * @author dinarte
 *
 */
public class PersistableConverter implements IConverter<Persistable<Long>> {

	@Autowired
	MigrationService mService;
	
	@Override
	@SuppressWarnings("unchecked")
	public Persistable<Long> parse(String value, Class<?> type) {
		Persistable<Long> obj = null;
		 try {
			obj = (Persistable<Long>) type.newInstance();
			Long newValue = mService.getIdByOriginal(type, value);
			type.getMethod("setId", Long.class).invoke(obj, newValue);
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Erros ao converter Entidade: " + e.getMessage());
		}
		
		return obj;
	}

	 

}
