package br.com.dfframeworck.security;

import java.lang.reflect.Method;

public class MenuItem {
	
	Class<?> controller;
	Method method;
	String hierarchy;
	String name;
	String path;
	String icon;
	
	
	public Class<?> getController() {
		return controller;
	}
	public void setController(Class<?> controller) {
		this.controller = controller;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public String getHierarchy() {
		return hierarchy;
	}
	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getMethodName() {
		return controller.getName().concat(".").concat(method.getName());
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
	
}
