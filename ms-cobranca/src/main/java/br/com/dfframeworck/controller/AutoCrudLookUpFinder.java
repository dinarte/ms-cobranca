package br.com.dfframeworck.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Component;

import br.com.dfframeworck.autocrud.AutoCrudField;
import br.com.dfframeworck.autocrud.AutoCrudSelectItem;
import br.com.dfframeworckservice.AutoCrudService;

@Component
public class AutoCrudLookUpFinder {
	
	@Autowired
	AutoCrudService service;
	
	
	public List<AutoCrudSelectItem> getAll(AutoCrudField field) {
		if(!field.getType().isEnum()) {
			return getLookUpEntity((field.getType().getSimpleName()));
		}else {
			return getLookUpEnum(field);
		}
	}
	
	public List<AutoCrudSelectItem> getAll(String entity) {
		return getLookUpEntity(entity);
	}

	private List<AutoCrudSelectItem> getLookUpEntity(String entity) {
		List<AutoCrudSelectItem> ret = new ArrayList<AutoCrudSelectItem>();
		List<Persistable<?>> list = service.findAll(entity);
		list.forEach(i-> ret.add(new AutoCrudSelectItem(i.getId(), i.toString())));
		return ret;
	}
	
	
	private List<AutoCrudSelectItem> getLookUpEnum(AutoCrudField field) {
		List<AutoCrudSelectItem> ret = new ArrayList<AutoCrudSelectItem>(); 
		Enum[] consts =	(Enum[]) field.getType().getEnumConstants();
		for (Enum object : consts) {
			ret.add(new AutoCrudSelectItem(object.name(), object.toString()));
		}
		return ret;
	}

}
