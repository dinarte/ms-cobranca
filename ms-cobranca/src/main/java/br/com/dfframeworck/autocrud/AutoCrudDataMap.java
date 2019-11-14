package br.com.dfframeworck.autocrud;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import br.com.dfframeworck.converters.DateConverter;

public class AutoCrudDataMap extends HashMap<String,Object>{

	private static final long serialVersionUID = 1L;
	
	
	public Object getFormated(AutoCrudField field){
		
		Object value = getOrDefault(field.getFieldName(), "");
		if (Objects.equals("", value))
			return value;
		
		if (field.getType().equals(Date.class)) {
			Date date = (Date) new DateConverter().parse(value.toString(), Date.class);
			
			SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
			
			return formater.format(date);
		
		} 
		
		if(field.getType().equals(BigDecimal.class)){
			if (Objects.nonNull(value))
				value = value.toString().replace(".", "").replace(",", ".");
		}
		
		return get(field.getFieldName());
	}

}
