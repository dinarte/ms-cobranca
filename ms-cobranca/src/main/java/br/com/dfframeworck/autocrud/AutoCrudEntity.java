package br.com.dfframeworck.autocrud;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.data.domain.Persistable;

import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;

public class AutoCrudEntity {

	private Class<?> type;
	
	private String entityName;
	
	private List<AutoCrudField> fields;
	
	private Persistable<?> obj;
	
	
	public List<AutoCrudField> getFieldsOnCrud(){
		return fields.stream().filter(AutoCrudField::isCrudEnabled).sorted(Comparator.comparingInt(AutoCrudField::getOrdinal)).collect(Collectors.toList());
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
	
	public AutoCrudField getField(String name){
		try {
		return fields.stream().filter(f->f.getFieldName().equals(name)).findFirst().get();
		}catch (NoSuchElementException e) {
			throw new RuntimeException("Não foi possível acessar o AutoCrudField: "+name);
		}
	}
	
	public boolean isOperationEnabled(String operation) {
		return Arrays.asList( getMeta().operations() ).contains(operation);
	}
	
	
	public AutoCrud getMeta() {
		return getType().getAnnotation(AutoCrud.class);		
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

	public Persistable<?> getObj() {
		return obj;
	}

	public void setObj(Persistable<?> obj) {
		this.obj = obj;
	}
}
