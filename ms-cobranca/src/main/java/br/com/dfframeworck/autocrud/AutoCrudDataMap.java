package br.com.dfframeworck.autocrud;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AutoCrudDataMap extends HashMap<String,Object>{

	private static final long serialVersionUID = 1L;
	
	
	public Object getFormated(AutoCrudField field){
		Object value = get(field.getFieldName());
		if (Objects.equals("", value))
			return value;
		return AutoCrudField.getFormatedValue(field, value, field.getType());
	}
	
	@Override
	public Object get(Object key) {
		getH( (HashMap) this, key);	
		 return super.get(key);
	}

	public static Object  getH(Map<Object,Object> map, Object key) {
		if (key.toString().contains(".")) {
			Object value=null;
			List<String> keys = Arrays.asList( key.toString().split(".") );
			for (String k : keys) {
				value = map.getOrDefault(k, new HashMap<>());
			}
			return value;
		}
		return map.getOrDefault(key, "");
	}

}
