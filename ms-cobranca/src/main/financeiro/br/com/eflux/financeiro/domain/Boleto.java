package br.com.eflux.financeiro.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.eflux.payments.api.Invoice;

@Entity
@Table(schema="financeiro", name="boleto")
public class Boleto implements Invoice{
	
	@Id
	private String tokenId;
	
	private String location;
	
	private String nossoNumero;
	
	private String status = STATUS_NOVO;
	
	private byte[] pdfFile;
	
	@Override
	public String getTokenId() {
		return tokenId;
	}

	@Override
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	@Override
	public String getLocation() {
		return location;
	}

	@Override
	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String getNossoNumero() {
		return nossoNumero;
	}

	@Override
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	public String getSTATUS_NOVO() {
		return STATUS_NOVO;
	}

	public String getSTATUS_REGISTRADO() {
		return STATUS_REGISTRADO;
	}

	public String getSTATUS_LIQUIDADO() {
		return STATUS_LIQUIDADO;
	}

	public String getSTATUS_CANCELADO() {
		return STATUS_CANCELADO;
	}

	public byte[] getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(byte[] pdfFile) {
		this.pdfFile = pdfFile;
	}



}
