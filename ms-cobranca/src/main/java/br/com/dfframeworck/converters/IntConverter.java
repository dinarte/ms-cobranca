package br.com.dfframeworck.converters;

import org.springframework.stereotype.Component;

@Component
@DataConverter(types= {Integer.class, int.class})
public class IntConverter  implements IConverter<Integer>{

	@Override
	public Integer parse(String value, Class<?> type) {
		
		return Integer.parseInt(value);
	}

}
