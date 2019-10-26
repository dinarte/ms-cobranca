package br.com.dfframeworck.autocrud;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.domain.Persistable;

import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;

public class AutoCrudField {
	
	private Map<Class, String> uiDefaultByType;
	
	private Map<String, String> uiDefaultByfieldName;

	private AutoCrudEntity entity;
	
	private Class<?> type;

	private String fieldName;
	
	private Object value;
	
	public AutoCrudField() {
		uiDefaultByType = new HashMap<>();
		uiDefaultByType.put(boolean.class, "simpleCheckBox");
		uiDefaultByType.put(Boolean.class, "simpleCheckBox");
		uiDefaultByType.put(int.class, "inputNumeric");
		uiDefaultByType.put(Integer.class, "inputNumeric");
		uiDefaultByType.put(Long.class, "inputNumeric");
		uiDefaultByType.put(Date.class, "inputCalendar");
		uiDefaultByType.put(String.class, "inputText");
		
		uiDefaultByfieldName = new HashMap<>();
		uiDefaultByfieldName.put("cpf", "inputCpf");
		uiDefaultByfieldName.put("cnpj", "inputCnpj");
	}
	
	public String getDefaultUi() throws InstantiationException, IllegalAccessException {
		if (getMeta().ui().equals( "default" )){
			String ui = uiDefaultByType.get(getType());
			if(Objects.isNull(ui))
				ui = uiDefaultByfieldName.get(fieldName);
			if(Objects.isNull(ui))
				ui = "inputText";
			if(!type.isPrimitive() && type.isAnnotationPresent(Entity.class))
				ui = "selectMenu";
			return ui;
		}
		return getMeta().ui();
	}

	public EnableAutoCrudField getMeta() {
		
		try {
			
			entity.getType().getFields();
			
			entity.getType().getDeclaredFields();
			
			return entity.getType().getDeclaredField(fieldName).getAnnotation(EnableAutoCrudField.class);
		} catch (NoSuchFieldException | SecurityException e) {
			//nada a ser feito
		}
		
		return null;
	}
	
	public boolean isCrudEnabled() {
		return Objects.nonNull(getMeta());
	}
	
	public Column getColumnDefination() {
		try {
			return type.getField(fieldName).getAnnotation(Column.class);
		} catch (NoSuchFieldException | SecurityException e) {
			//nada a ser feito
		}
		return null;
	}
	
	public boolean isLookUp(){
		return getMeta().lookUpFieldName().length() > 0;
	}
	
	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}


	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public AutoCrudEntity getEntity() {
		return entity;
	}

	public void setEntity(AutoCrudEntity entity) {
		this.entity = entity;
	}
	
	
	
}