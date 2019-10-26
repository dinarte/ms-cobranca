package br.com.dfframeworck.autocrud;

import java.util.List;
import java.util.stream.Collectors;

import br.com.dfframeworck.autocrud.annotations.AutoCrud;

public class AutoCrudEntity {

	private Class<?> type;
	
	private String entityName;
	
	private List<AutoCrudField> fields;
	
	
	public List<AutoCrudField> getFieldsOnCrud(){
		return fields.stream().filter(AutoCrudField::isCrudEnabled).collect(Collectors.toList());
	}
	
	public List<AutoCrudField> getFieldsOnFilter(){
		return getFieldsOnCrud().stream().filter(f->f.getMeta().enableForFilter()).collect(Collectors.toList());
	}
	
	public List<AutoCrudField> getFieldsOnList(){
		return getFieldsOnCrud().stream().filter(f->f.getMeta().enableForList()).collect(Collectors.toList());
	}
	
	public List<AutoCrudField> getFieldsOnCreate(){
		return getFieldsOnCrud().stream().filter(f->f.getMeta().enableForCreate()).collect(Collectors.toList());
	}
	
	public List<AutoCrudField> getFieldsOnUpdate(){
		return getFieldsOnCrud().stream().filter(f->f.getMeta().enableForUpdate()).collect(Collectors.toList());
	}
	

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getName() {
		return type.getAnnotation(AutoCrud.class).name();
	}
	
	public String getDescription() {
		return type.getAnnotation(AutoCrud.class).description();
	}

	public List<AutoCrudField> getFields() {
		return fields;
	}

	public void setFields(List<AutoCrudField> fields) {
		this.fields = fields;
	}

}