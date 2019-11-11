package br.com.dfframeworck.converters;

import org.springframework.stereotype.Component;

@Component
@EnableDataConver(types= {Integer.class, int.class}, enableForForm=true, enableForMigration=true)
public class IntConverter  implements IConverter<Integer>{

	@Override
	public Integer parse(String value, Class<?> type) {
		
		return Integer.parseInt(value);
	}

}
