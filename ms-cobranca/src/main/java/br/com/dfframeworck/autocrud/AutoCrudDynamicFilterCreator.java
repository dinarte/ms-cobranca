package br.com.dfframeworck.autocrud;

public class AutoCrudDynamicFilterCreator {
	
	
	AutoCrudEntity entity;
	
	
	public void add(String fieldName, Class<?>type, Object value ) {
		
		AutoCrudField field = new AutoCrudField();
		field.setEntity(entity);
		field.setFieldName(fieldName);
		field.setType(type);
		field.getValue();
		
	}

}
