package br.com.eflux.payments.api;

public interface BatchFile {
	
	public String getTokenId();
	
	public void setTokenId(String headerString);
	
	public String getName();
	
	public void setName(String name);
	
	public String getLocation();
	
	public void setLocation(String location);
	
	public byte[] getFile();
	
	public void setFile(byte[] file);

}
