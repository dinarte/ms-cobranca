package br.com.dfframeworck.converters;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
@EnableDataConver(types= {BigDecimal.class}, enableForForm=true, enableForMigration=true)
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
			try {
				return tryUsType(value);
			}catch (Exception e) {
				try {
					return tryBrType(value);
				} catch (Exception e2) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}	
		}
		
	}
	
	private BigDecimal tryUsType(String value) {
			return BigDecimal.valueOf(Double.valueOf(value));
	}
	
	private BigDecimal tryBrType(String value) throws ParseException {
		value = value.replace(".", "").replace(",", ".");
		return tryUsType(value);
	}
	
	public static void main(String[] args) throws ParseException {
		BigDecimalConverter converter = new BigDecimalConverter();
		System.out.println(converter.parse("58.400,00", BigDecimal.class));
	}
	
}


