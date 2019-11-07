package br.com.eflux.payments.api;

public class PaymentApiException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public PaymentApiException(String msg) {
		super(msg);
	}	

}

