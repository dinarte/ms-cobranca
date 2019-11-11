package br.com.eflux.financeiro.vo;

import java.math.BigDecimal;

public interface IndicadoresVo {

	public String getEmpreendimento();
	public Long getContratosAtivos();
	public BigDecimal getReceitaPrevista();
	public BigDecimal getReceitaRealizada();
	public BigDecimal getValorVencido();

}
