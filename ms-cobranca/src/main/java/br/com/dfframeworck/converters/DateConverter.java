package br.com.dfframeworck.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
@DataConverter(types= {Date.class})
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
			
			try {
				return new Date( Long.parseLong(value) );
			} catch (Exception e) {
				//NADA POR FAZER
			}
			
			
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return formato.parse(value);
			} catch (ParseException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) throws ParseException {
		 
		System.out.println(new DateConverter().parse("1573142631708", Date.class));
		
	}

}
