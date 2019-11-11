package br.com.dfframeworck.converters;

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
@EnableDataConver(types= {Persistable.class}, enableForForm=true, enableForMigration=false)
public class PersistableFormConverter implements IConverter<Persistable<?>> {

	@Autowired
	MigrationService mService;

	
	@Override
	public Persistable<?> parse(String value, Class<?> type) {
			return  (Persistable<?>) mService.findOne(type, Long.parseLong(value));
	}

	 

}
