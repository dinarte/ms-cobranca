package br.com.dfframeworck.util;

import javax.servlet.http.HttpServletRequest;

public class NavigationUtils {
	
	
	static public String  getReferer(HttpServletRequest request, String defaultIsNull) {
		String refer = request.getHeader("referer");
		if (refer == null)
			return defaultIsNull;
		refer = refer.substring(refer.indexOf("//")+2);
		refer = refer.substring(refer.indexOf("/"));
		
		String queryString = "?";
		
		
		request.getParameterMap()
			.keySet()
			.stream()
			.filter(k -> k.contains("."))
			.forEach(k -> {
			
				if ( request.getParameter(k) != null && !request.getParameter(k).equals("")) {
					queryString.concat(k+"="+request.getParameter(k)+"&");
				}
				
			});
		
		
		return refer + queryString;
	}

}
