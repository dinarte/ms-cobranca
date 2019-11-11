package br.com.eflux.financeiro.vo;

import java.math.BigDecimal;

public interface InadiplentesVo {

	public Long getIdContrato();
	
	public String getContrato();
	
	public Long getDebitos();
	
	public BigDecimal getValorAtualizado();
}
