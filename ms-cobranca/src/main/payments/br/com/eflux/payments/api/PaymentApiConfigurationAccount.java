package br.com.eflux.payments.api;

public interface PaymentApiConfigurationAccount {

	PaymentApiConfiguration getBoletoApiConfiguration();

	void setBoletoApiConfiguration(PaymentApiConfiguration configuracaoBoleto);

	String getToken();

	void setToken(String token);

	String getInstrucoes();

	void setInstrucoes(String instrucoes);

}