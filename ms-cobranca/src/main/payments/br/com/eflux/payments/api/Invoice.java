package br.com.eflux.payments.api;

public interface Invoice {

	String STATUS_NOVO = "CRIADO";
	String STATUS_REMETIDO = "REMETIDO";
	String STATUS_REGISTRADO = "REGISTRADO";
	String STATUS_LIQUIDADO = "LIQUIDADO";
	String STATUS_CANCELADO = "CANCELADO";

	String getTokenId();

	void setTokenId(String tokenId);

	String getLocation();

	void setLocation(String location);

	String getNossoNumero();

	void setNossoNumero(String nossoNumero);

	String getStatus();

	void setStatus(String status);
	
	void setPdfFile(byte[] file);
	
	byte[] getPdfFile();

	PaymentApiConfigurationAccount getConfigConta();

	void setConfigConta(PaymentApiConfigurationAccount configConta);

}