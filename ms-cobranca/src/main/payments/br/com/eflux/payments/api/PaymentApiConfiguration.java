package br.com.eflux.payments.api;

public interface PaymentApiConfiguration {

	String getUri();

	void setUri(String uri);

	String getApiImplementation();

	void setApiImplementation(String apiImplementation);

	String getUserApi();

	void setUserApi(String userApi);

	String getPass();

	void setPass(String pass);

}