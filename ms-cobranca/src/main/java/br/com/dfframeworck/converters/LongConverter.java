package br.com.dfframeworck.converters;

import org.springframework.stereotype.Component;

@Component
@DataMigrationConverter(types={Long.class})
public class LongConverter  implements IConverter<Long>{

	@Override
	public Long parse(String value, Class<?> type) {
		return Long.parseLong(value);
	}

}
