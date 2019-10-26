package br.com.dfframeworck.converters;

import org.springframework.stereotype.Component;

@Component
@DataMigrationConverter(types= {String.class})
public class StringConverter  implements IConverter<String>{

	@Override
	public String parse(String value, Class<?> type) {
		return value;
	}

}