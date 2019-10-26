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
		List<AutoCrudSelectItem> ret = new ArrayList<AutoCrudSelectItem>();
		List<Persistable<?>> list = service.findAll(field.getType().getSimpleName());
		list.forEach(i-> ret.add(new AutoCrudSelectItem(i.getId(), i.toString())));
		return ret;
	}
	
	public List<AutoCrudSelectItem> getAll(String entity) {
		List<AutoCrudSelectItem> ret = new ArrayList<AutoCrudSelectItem>();
		List<Persistable<?>> list = service.findAll(entity);
		list.forEach(i-> ret.add(new AutoCrudSelectItem(i.getId(), i.toString())));
		return ret;
	}

}
