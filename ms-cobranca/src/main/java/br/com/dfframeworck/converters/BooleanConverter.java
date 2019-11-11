package br.com.dfframeworck.converters;

import org.springframework.stereotype.Component;

@Component
@EnableDataConver(types= {Boolean.class, boolean.class} , enableForForm=true, enableForMigration=true)
public class BooleanConverter  implements IConverter<Boolean>{

	@Override
	public Boolean parse(String value, Class<?> type) {
		
		if (value.equals("on") ) value = "true";
		if (value.equals("off") ) value = "false";
		
		return Boolean.parseBoolean(value);
	}

}
