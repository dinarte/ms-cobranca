package br.com.dfframeworck.autocrud;

import java.io.Serializable;

public class AutoCrudAction implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String description;
	
	private String path;
	
	private String icon = "arrow-circle-right";
	
	private String cssClass="btn btn-primary btn-circle";
	
	private boolean onList = true;
	
	private int ordinal;

	
	public AutoCrudAction(String description, String path) {
		super();
		this.description = description;
		this.path = path;
	}
	
	
	public AutoCrudAction(String description, String path, String icon, String cssClass, boolean onList, int ordinal) {
		super();
		this.description = description;
		this.path = path;
		this.icon = icon;
		this.onList = onList;
		this.ordinal = ordinal;
		this.cssClass = cssClass;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isOnList() {
		return onList;
	}

	public void setOnList(boolean onList) {
		this.onList = onList;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
	
}
