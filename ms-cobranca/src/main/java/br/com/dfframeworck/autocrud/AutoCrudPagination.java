package br.com.dfframeworck.autocrud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoCrudPagination {
	
	private Integer page = 1;
	private Integer size = 15;
	private Integer firstRow = 0;
	private Integer totalResults = 0;
	private String path = "";
	
	public AutoCrudPagination(Integer page, Integer size) {
		
		if (page != null) this.page = page;
		if (size != null) this.size = size;
		
		if (this.page > 1)
			firstRow = size * page;
	}
	
	public AutoCrudPagination(String page, String size) {
		
		if (page != null) this.page = Integer.parseInt(page);
		if (size != null) this.size = Integer.parseInt(size);
	
		if (this.page > 1)
			firstRow = this.size * this.page;
	}
	
	public List<Map<String,Object>> getPageList(){
		List<Map<String,Object>> ret = new ArrayList<>();
		int totalPages = totalResults / size;
		totalPages = totalPages % size > 0 ? totalPages + 1 : totalPages;
		for (int i = 1; i<=totalPages ; i++) {
			Map<String, Object> item = new HashMap<>();
			item.put("index", i);
			item.put("path", path + "?page=" + i + "&size=" + size);
			ret.add(item);
		}
		return ret;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getFirstRow() {
		return firstRow;
	}

	public void setFirstRow(Integer firstRow) {
		this.firstRow = firstRow;
	}

	public Integer getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	
}
