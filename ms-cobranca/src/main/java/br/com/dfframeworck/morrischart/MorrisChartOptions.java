package br.com.dfframeworck.morrischart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MorrisChartOptions {

	String element = "morris-area-chart";

	List<Map<String,Object>> data = new ArrayList<>();
    
	String xkey = "periodo";
    
	String ymin = "auto";
	
	String ymax = "auto";
    
	List<String> ykeys = new ArrayList<>();
    
	List<String> labels = new ArrayList<>();
    
	Integer pointSize = 2;
    
	String hideHover = "auto";
    
	boolean resize = true;
	
	String dateFormat;
    
     
	public void addDataItem(String key, Object value) {
		Map<String,Object> item = new HashMap<>();
		item.put(key, value);
		data.add(item);
	}
	
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	
	public List<Map<String, Object>> getData() {
		return data;
	}
	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
	public String getXkey() {
		return xkey;
	}
	public void setXkey(String xkey) {
		this.xkey = xkey;
	}
	
	public String getYmin() {
		return ymin;
	}

	public void setYmin(String ymin) {
		this.ymin = ymin;
	}

	public String getYmax() {
		return ymax;
	}

	public void setYmax(String ymax) {
		this.ymax = ymax;
	}

	public List<String> getYkeys() {
		return ykeys;
	}
	public void setYkeys(List<String> ykeys) {
		this.ykeys = ykeys;
	}
	public List<String> getLabels() {
		return labels;
	}
	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	public Integer getPointSize() {
		return pointSize;
	}
	public void setPointSize(Integer pointSize) {
		this.pointSize = pointSize;
	}
	public String getHideHover() {
		return hideHover;
	}
	public void setHideHover(String hideHover) {
		this.hideHover = hideHover;
	}
	public boolean isResize() {
		return resize;
	}
	public void setResize(boolean resize) {
		this.resize = resize;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
    
    
}
