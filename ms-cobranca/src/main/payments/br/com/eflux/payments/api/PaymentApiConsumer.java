package br.com.eflux.payments.api;

import java.util.Date;
import java.util.Map;

import br.com.eflux.financeiro.domain.Debito;

public interface PaymentApiConsumer {
	
	public String getDescription();

	public void basicAuthentication(PaymentApiConfigurationAccount config);

	public Invoice createFromDebito(Debito debito) throws PaymentApiException;

	public Invoice create(Map<String, String> data) throws PaymentApiException;
	
	public byte[] get(String tokenId) throws PaymentApiException;
	
	public void writeOff(String tokenId, String msg) throws PaymentApiException;

	public byte[] duplicate(String tokenId, Date date) throws PaymentApiException;
	
	public BatchFile createBatch(String accountTokenId, BatchFile file) throws PaymentApiException;

}