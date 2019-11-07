package br.com.dfframeworck.autocrud;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

import br.com.dfframeworck.converters.DateConverter;

public class AutoCrudDataMap extends HashMap<String,Object>{

	private static final long serialVersionUID = 1L;
	
	
	public Object getFormated(AutoCrudField field){
		
		Object value = getOrDefault(field.getFieldName(), "");
		if (Objects.equals("", value))
			return value;
		
		if (field.getType().equals(Date.class)) {
			Date date = (Date) new DateConverter().parse(value.toString(), Date.class);
			
			/*
			 * Properties ps = System.getProperties(); Locale local = new
			 * Locale((String)ps.get("user.country"), (String)ps.get("user.language"));
			 * DateFormat hoje = DateFormat.getDateInstance(DateFormat.MEDIUM, local);
			 * return hoje.format(date);
			 */
			
			return date;
		
		} 
		
		if(field.getType().equals(BigDecimal.class)){
		
			value = value.toString().replace(".", "").replace(",", ".");
		}
		
		return get(field.getFieldName());
	}

}
