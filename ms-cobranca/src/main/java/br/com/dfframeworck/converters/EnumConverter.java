package br.com.dfframeworck.converters;

import java.util.Objects;

import org.springframework.stereotype.Component;

import br.com.eflux.comum.domain.EstadoCivilEnum;
import br.com.eflux.comum.domain.SexoEnum;

@Component
@DataMigrationConverter(types= {EstadoCivilEnum.class, SexoEnum.class})
public class EnumConverter implements IConverter<Enum>{

	@Override
	public Enum parse(String value, Class<?> type) {
		if (Objects.isNull(value))
			return null;
		else if(value.equals("NULL")){
			return null;
		}		
		else {
		  Enum[] consts =	(Enum[]) type.getEnumConstants();
		  for (Enum object : consts) {
			if( object.toString().equals(value) )
				return object;
		}
		}
		return null;
	}

}
