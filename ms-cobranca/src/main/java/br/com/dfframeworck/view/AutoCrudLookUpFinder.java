package br.com.dfframeworck.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.dfframeworck.autocrud.AutoCrudField;
import br.com.dfframeworck.autocrud.AutoCrudSelectItem;
import br.com.dfframeworckservice.AutoCrudService;
import br.com.eflux.payments.api.PaymentApiConsumersProvaider;

@Controller
public class AutoCrudLookUpFinder {
	
	@Autowired
	AutoCrudService service;
	
	@Autowired
	PaymentApiConsumersProvaider connsumers;
	
	@RequestMapping(path="/lookup/{entity}/{field}", produces=MediaType.APPLICATION_JSON)
	@ResponseBody
	private Map<String, Object> lookUpEntityService(
			@PathVariable String entity, 
			@PathVariable String field, 	
			@RequestParam String query) {
		
		
		List<String> filters = new ArrayList<>();
		if (Strings.isNotBlank(query))
			filters.add("upper("+field+") like '%"+query.toUpperCase()+"%'");
		
		List<AutoCrudSelectItem> ret = new ArrayList<AutoCrudSelectItem>();
		List<Persistable<?>> list = service.findAll(entity, 0, 50, filters);
		list.forEach(i-> ret.add(new AutoCrudSelectItem(i.getId(), i.toString())));
		Map<String, Object> retMap = new HashMap<>();
		retMap.put("suggestions", ret);
		return retMap;
	}
	
	
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
	
	public List<AutoCrudSelectItem> getAllApiConsumers() {
		String[] itens = connsumers.getAll();
		List<AutoCrudSelectItem> ret = new ArrayList<AutoCrudSelectItem>();
		for (String name : itens) {
			ret.add(new AutoCrudSelectItem(name, connsumers.get(name).getDescription()));
		}
		return ret;
	}

}
