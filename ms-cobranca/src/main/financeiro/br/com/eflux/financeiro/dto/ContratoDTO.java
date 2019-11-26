package br.com.eflux.financeiro.dto;

import java.math.BigDecimal;
import java.util.List;

import br.com.eflux.financeiro.domain.Acordo;
import br.com.eflux.financeiro.domain.Contrato;
import br.com.eflux.financeiro.domain.Debito;
import br.com.eflux.financeiro.domain.SituacaoDebitoEnum;
import br.com.eflux.financeiro.domain.TipoLancamento;
import br.com.eflux.financeiro.helper.SituacaoDebitoHelper;

/**
 * Representa um contrato com todos os seus itens como débitos, acordos etc.  
 * @author Dinarte
 */
public class ContratoDTO {
	
	private Contrato contrato;
	
	private List<Debito> debitoList;
	
	private List<Acordo> acordoList;

	
	public Long getQtdParcelasFaltantes() {
		return debitoList.stream().filter(d -> SituacaoDebitoHelper.getAPagar().contains(d.getSituacao()) ).count();
	}
	
	public BigDecimal getTotalPago() {
		return debitoList.stream().map(Debito::getValorPago).reduce(BigDecimal::add).get();
	}
	
	public BigDecimal getTotalDevedor() {
		return debitoList.stream().filter(d-> SituacaoDebitoHelper.getAtivas().contains(d.getSituacao())).map(d -> d.getValorAtualizado().subtract( d.getValorPago())).reduce(BigDecimal::add).get();
	}
	
	public Long getTotalDebitos(TipoLancamento tipo) {
		return debitoList.stream().filter(d -> d.getTipoLancamento().equals(tipo)).count();
	}
	
	
	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public List<Debito> getDebitoList() {
		return debitoList;
	}

	public void setDebitoList(List<Debito> debitoList) {
		this.debitoList = debitoList;
	}

	public List<Acordo> getAcordoList() {
		return acordoList;
	}

	public void setAcordoList(List<Acordo> acordoList) {
		this.acordoList = acordoList;
	}

}
