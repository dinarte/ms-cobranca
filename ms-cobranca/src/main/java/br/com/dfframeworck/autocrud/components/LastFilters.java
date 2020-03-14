package br.com.dfframeworck.autocrud.components;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class LastFilters {

	private String queryString = "";
	
	private String page = "";

	
	public String getRedirectFor() {
		String redirect = "redirect:"+page+"?"+queryString;
		queryString = "";
		page = "";
		return redirect;
	}
	
	
	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getEntity() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
	
}
