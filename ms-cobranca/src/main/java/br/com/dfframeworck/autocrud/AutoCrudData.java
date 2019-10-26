package br.com.dfframeworck.autocrud;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.dfframeworck.util.ObjectToMap;

public class AutoCrudData {

	AutoCrudEntity entity;

	List<Map<String,Object>> data;
	
	AutoCrudPagination pagination;
	
	public void parseData(List<?> list) {
		data = new ArrayList<Map<String,Object>>();
		list.forEach(obj->{ data.add(ObjectToMap.toMap(obj));});
	}

	public AutoCrudEntity getEntity() {
		return entity;
	}


	public void setEntity(AutoCrudEntity entity) {
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
	
	
	
}
