package br.com.dfframeworck.repository;

public interface Migrable<T> {

	public void setId(T id);
	
	public void setOriginalId(String id);
	
	public String getOriginalId();
}
