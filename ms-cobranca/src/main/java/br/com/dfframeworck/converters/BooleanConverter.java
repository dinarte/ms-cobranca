package br.com.dfframeworck.converters;

import org.springframework.stereotype.Component;

@Component
@DataConverter(types= {Boolean.class, boolean.class})
public class BooleanConverter  implements IConverter<Boolean>{

	@Override
	public Boolean parse(String value, Class<?> type) {
		return Boolean.parseBoolean(value);
	}

}
