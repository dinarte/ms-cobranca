package br.com.dfframeworck.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
@DataMigrationConverter(types= {Date.class})
public class DateConverter implements IConverter<Date> {

	@Override
	public Date parse(String value, Class<?> type) {
		if (Objects.isNull(value))
			return null;
		else if(value.equals("NULL")){
			return null;
				
		}else if (value.trim().equals("")){
			return null;
		}		
		else {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return formato.parse(value);
			} catch (ParseException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}

}
