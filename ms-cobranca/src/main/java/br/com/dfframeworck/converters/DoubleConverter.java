package br.com.dfframeworck.converters;

import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
@EnableDataConver(types= {Double.class}, enableForForm=true, enableForMigration=true)
public class DoubleConverter implements IConverter<Double> {

	
	@Override
	public Double parse(String value, Class<?> type) {
		if (Objects.isNull(value))
			return null;
		else if(value.equals("NULL")){
			return null;
				
		}else if (value.trim().equals("")){
			return null;
		}		
		else {
			return Double.valueOf(value);
		}
	}
	
}
