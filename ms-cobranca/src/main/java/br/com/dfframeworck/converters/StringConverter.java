package br.com.dfframeworck.converters;

import org.springframework.stereotype.Component;

@Component
@EnableDataConver(types= {String.class}, enableForForm=true, enableForMigration=true)
public class StringConverter  implements IConverter<String>{

	@Override
	public String parse(String value, Class<?> type) {

		if (value.equalsIgnoreCase("null"))
			return null;
		
		return value;
	}

}
