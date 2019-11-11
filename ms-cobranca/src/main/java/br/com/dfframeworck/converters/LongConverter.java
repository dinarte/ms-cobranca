package br.com.dfframeworck.converters;

import org.springframework.stereotype.Component;

@Component
@EnableDataConver(types={Long.class} , enableForForm=true, enableForMigration=true)
public class LongConverter  implements IConverter<Long>{

	@Override
	public Long parse(String value, Class<?> type) {
		return Long.parseLong(value);
	}

}
