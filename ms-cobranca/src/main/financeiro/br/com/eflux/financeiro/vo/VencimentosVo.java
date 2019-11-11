package br.com.eflux.financeiro.vo;

import java.math.BigDecimal;
import java.util.Date;

public interface VencimentosVo {
	
	public Long getIdContrato();
	
	public String getContrato();
	
	public String getTipoDebito();
	
	public Long getIdDebito();

	public Long getNumero();
	
	public BigDecimal getValorAtualizado();
	
	public Date getDataVencimento();

}
