package br.com.dfframeworck.autocrud;

public class AutoCrudSelectItem {
	
	Object id;
	
	String label;
	
	public AutoCrudSelectItem(Object id, String  label) {
		this.id = id;
		this.label = label;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	

}
