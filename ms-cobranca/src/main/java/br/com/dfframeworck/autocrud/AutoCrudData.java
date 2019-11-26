package br.com.dfframeworck.autocrud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import br.com.dfframeworck.util.SerializationUtils;
import javassist.expr.Instanceof;

public class AutoCrudData {

	AutoCrudEntity entity;

	List<Map<String,Object>> data;
	
	AutoCrudPagination pagination;
	
	List<AutoCrudAction> actions = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	private List<AutoCrudAction> parseActions(String id) {
		List<AutoCrudAction> actionsOffData = new ArrayList<>();
		actionsOffData.addAll((Collection<? extends AutoCrudAction>) SerializationUtils.clone( actions ));
		actionsOffData.forEach(action-> action.setPath( action.getPath().replace("{id}", id).replace("{entityName}", getEntity().getEntityName()) ));
		return actionsOffData.stream().sorted(Comparator.comparingInt(AutoCrudAction::getOrdinal)).collect(Collectors.toList());
	}
	
	
	public void parseData(List<?> list) {
		data = new ArrayList<Map<String,Object>>();
		list.forEach(obj->{ 
			AutoCrudDataMap objMap = new AutoCrudDataMap();
			objMap.putAll( SerializationUtils.toAutoCrudMap(obj) );
			objMap.put("actions", parseActions(objMap.get("id").toString()));
			data.add(objMap);
		});
	}

	public AutoCrudEntity getEntity() {
		return entity;
	}


	public void setEntity(AutoCrudEntity entity) {
		
		if (Arrays.asList( entity.getMeta().operations() ).contains("update")) {
			AutoCrudAction editar = new AutoCrudAction("Editar registro", "/crud/{entityName}/{id}","glyphicon glyphicon-pencil","btn btn-success btn-circle", true, 99);
			actions.add(editar);
		}
		
		if (Arrays.asList( entity.getMeta().operations() ).contains("delete")) {
			AutoCrudAction remover = new AutoCrudAction("Remover registro", "/crud/{entityName}/{id}/del", "glyphicon glyphicon-trash","btn btn-danger btn-circle delete",true, 100) ;
			actions.add(remover);
		}
		
		this.entity = entity;
	}


	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	public AutoCrudPagination getPagination() {
		return pagination;
	}

	public void setPagination(AutoCrudPagination pagination) {
		this.pagination = pagination;
	}

	public List<AutoCrudAction> getActions() {
		return actions;
	}
	
}
