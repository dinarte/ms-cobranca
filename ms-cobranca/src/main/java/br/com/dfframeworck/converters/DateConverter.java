package br.com.dfframeworck.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
@EnableDataConver(types= {Date.class}, enableForForm=true, enableForMigration=true)
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
			Date date = tryLong(value);
			if (Objects.isNull(date))
				date = tryUsFormat(value);
			if (Objects.isNull(date))
				date = tryBrFormat(value);
			if (Objects.isNull(date))
				date = tryBrFormatWBar(value);
			if (Objects.isNull(date))
				throw new RuntimeException("Não foi possíve converter o valor de "+value+ " pra Date");
			else
				return date;
		}
	}
	
	public Date tryLong(String value) {
		try {
			return new Date( Long.parseLong(value) );
		} catch (Exception e) {
			return null;
		}
	}
	
	public Date tryUsFormat(String value) {
		return tryFormat(value, "yyyy-MM-dd");
	}
	
	public Date tryBrFormat(String value) {
		return tryFormat(value, "dd-MM-yyyy");
	}
	
	public Date tryBrFormatWBar(String value) {
		return tryFormat(value, "dd/MM/yyyy");
	}
	
	public Date tryFormat(String value, String partner) {
		SimpleDateFormat formato = new SimpleDateFormat(partner);
		try {
			return formato.parse(value);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static void main(String[] args) throws ParseException {
		 
		System.out.println(new DateConverter().parse("1573142631708", Date.class));
		
	}

}
