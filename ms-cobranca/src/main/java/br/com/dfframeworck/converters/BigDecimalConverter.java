package br.com.dfframeworck.converters;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
@DataMigrationConverter(types= {BigDecimal.class})
public class BigDecimalConverter implements IConverter<BigDecimal> {

	
	@Override
	public BigDecimal parse(String value, Class<?> type) {
		if (Objects.isNull(value))
			return null;
		else if(value.equals("NULL")){
			return null;
				
		}else if (value.trim().equals("")){
			return null;
		}		
		else {
			return BigDecimal.valueOf(Double.valueOf(value));
		}
	}
	
}
