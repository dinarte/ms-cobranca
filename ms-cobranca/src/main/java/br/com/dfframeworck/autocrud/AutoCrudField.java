package br.com.dfframeworck.autocrud;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;

import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.converters.DateConverter;

public class AutoCrudField {
	
	private Map<Class<?>, String> uiDefaultByType;
	
	private Map<String, String> uiDefaultByfieldName;

	private AutoCrudEntity entity;
	
	private Class<?> type;

	private String fieldName;
	
	private Object value;
	
	private EnableAutoCrudField meta;
	
	public AutoCrudField() {
		uiDefaultByType = new HashMap<>();
		uiDefaultByType.put(boolean.class, "simpleCheckBox");
		uiDefaultByType.put(Boolean.class, "simpleCheckBox");
		uiDefaultByType.put(Date.class, "inputCalendar");
		uiDefaultByType.put(String.class, "inputText");
		uiDefaultByType.put(Periodo.class, "inputPeriod");
		
		uiDefaultByfieldName = new HashMap<>();
		uiDefaultByfieldName.put("email", "inputEmail");
		uiDefaultByfieldName.put("cnpj", "inputCnpj");
	}
	
	public String getDefaultUi() throws InstantiationException, IllegalAccessException {
		if (getMeta().ui().equals( "default" )){
			String ui = uiDefaultByType.get(getType());
			if(Objects.isNull(ui))
				ui = "inputText";
			
			String uiName = uiDefaultByfieldName.get(fieldName);
			
			if (uiName != null)
				ui=uiName;
			
			if(!type.isPrimitive() && type.isAnnotationPresent(Entity.class))
				ui = "selectMenu";
			if(type.isEnum())
				ui = "selectEnum";
			return ui;
		}
		return getMeta().ui();
	}
	
	public String getUiFieldName() {
		return  entity.getType().getSimpleName() + "." + fieldName;
	}
	
	public String getUiFieldId() {
		return entity.getType().getSimpleName() + "-" + fieldName;
	}
	
	
	public Object getFormatedValue() {
		return getFormatedValue(this, value, type);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getFormatedValue(AutoCrudField field, Object value, Class<?> type) {
	
		if (Objects.isNull(value))
			return "";
		
		if (isType(type, "BigDecimal","Float", "float", "Double", "double"))
			//return new DecimalFormat("0.00,##").format(Double.valueOf(value.toString()));
			return NumberFormat.getInstance().format(Double.parseDouble(value.toString()));
		
		if (isType(type, "Boolean","boolean"))
			return (Boolean) value ? "SIM" : "NÃO";
		
		if (isType(type,"Date")) {
			Date date = (Date) new DateConverter().parse(value.toString(), Date.class);
			SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
			return formater.format(date);
		}
		
		if (type.isEnum())
			return value;
		
		if (field.isLookUp()) {
			System.out.println(field.getFieldName() + " - " + field.getMeta().lookUpFieldName());
			return  AutoCrudDataMap.getH((Map)value, field.getMeta().lookUpFieldName()); 
		}
		
		return value;
	}
	
	
	
	private static boolean isType(Class<?> type, String... tName) {
		for (String item : tName) {
			if (type.getSimpleName().equals(item))
				return true;
		}
		return false;
	}

	public EnableAutoCrudField getMeta() {
		
		try {
			return entity.getType().getDeclaredField(fieldName).getAnnotation(EnableAutoCrudField.class);
		} catch (NoSuchFieldException | SecurityException e) {
			//nada a ser feito
		}
		
		return meta;
	}
	
	public void setMeta(EnableAutoCrudField meta) {
		this.meta = meta;
	}
	
	public boolean isReadOnly() {
		
		if (Objects.isNull(entity.getObj()))
			return false;
		
		return !entity.getObj().isNew() && getMeta().readOnlyForUpdate();
		
	}
	
	public int getOrdinal() {
		return getMeta().ordinal();
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
		if (getMeta()==null)
			return false;
		return ((getMeta().lookUpFieldName().length() > 0 && !getType().isEnum()) || getType().isAnnotationPresent(Entity.class));
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
