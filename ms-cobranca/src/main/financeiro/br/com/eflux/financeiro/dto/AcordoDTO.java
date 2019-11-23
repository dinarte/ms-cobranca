package br.com.eflux.financeiro.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import br.com.dfframeworck.util.SerializationUtils;
import br.com.eflux.config.financeiro.domain.ConfiguracaoAcordo;
import br.com.eflux.financeiro.domain.Contrato;
import br.com.eflux.financeiro.domain.Debito;

public class AcordoDTO {
	
	@NotNull
	private Contrato contrato;
	
	@NotNull
	private BigDecimal totalAcordo;

	@NotNull @Min(0)
	private BigDecimal valorEntrada = new BigDecimal(0);
	
	@NotNull @Min(1) 
	private Integer quantidadeParcelas = 1;
	
	@NotNull @Min(1) @Max(30)
	private Integer diaVencimento = 5; 
	
	@NotNull
	private List<Debito> debitosDoAcordo;
	
	private List<Debito> parcelasDoAcordo;
	
	private ConfiguracaoAcordo configuracao;
	

	/**
	 * Retorna o total devedor dos Débitos selecionados no acordo.
	 * @return
	 */
	public BigDecimal getTotalDevedor() {
		return debitosDoAcordo.stream().map(Debito::getValorAtualizado).reduce(BigDecimal::add).get();
	}

	
	/**
	 * Total em juros por atrazo dos débitos selecionados no acordo.
	 * @return
	 */
	public BigDecimal getTotalJurosAtrazo() {
		return debitosDoAcordo.stream().map(Debito::getJurosAtrazo).reduce(BigDecimal::add).get();
	}

	/**
	 * Total em multas por atrazo
	 * @return
	 */
	public BigDecimal getTotalMultaAtrazo() {
		return debitosDoAcordo.stream().map(Debito::getMultaAtrazo).reduce(BigDecimal::add).get();
	}
	
	
	/**
	 * Retorna as débitos a serem pagos do acordo de acordo com o total de parcelas informado. 
	 * @return
	 */
	public List<Debito> gerarParcelasDoAcordo(ConfiguracaoAcordo config) {
		
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, config.getQuantidadeDiasVencimentoEntrada());
		Date dataVencimentoEntrada = new Date( calendar.getTime().getTime() );
		
		calendar.set(Calendar.DAY_OF_MONTH, diaVencimento);
		
		
		
		//entrda
		parcelasDoAcordo = new ArrayList<>();
		if (valorEntrada.compareTo(new BigDecimal(0))==1)
			parcelasDoAcordo.add(new Debito(contrato, 1, valorEntrada, dataVencimentoEntrada, config.getTipoLancamentoEntrada()));
				
		//demais parcelas
		BigDecimal valorParcela = totalAcordo.subtract(valorEntrada).divide(new BigDecimal(quantidadeParcelas),2);      
		int[] i = {1};
		List<Debito> parcelasGeradas =  Arrays.asList(new Debito[quantidadeParcelas])
				.stream()
				.map(d -> new Debito(contrato, i[0]++, valorParcela, dataVencimentoEntrada, config.getTipoLancamento()))
				.collect(Collectors.toList());
		
		//setando o vencimento das parcelas corretamente
		parcelasGeradas.stream().filter(d->d.getTipoLancamento().equals(config.getTipoLancamento())).forEach(d->{
			calendar.add(Calendar.MONTH, 1);
			d.setDataVencimento(calendar.getTime());
		});
		
		parcelasDoAcordo.addAll(parcelasGeradas);
		
		return parcelasDoAcordo;
	}

	
	/**
	 * Retorna as débitos a serem pagos do acordo de acordo com o total de parcelas informado. 
	 * @return
	 */
	public List<Debito> getParcelasDoAcordo() {
		return parcelasDoAcordo;
	}

	
	
	public void setParcelasDoAcordo(List<Debito> parcelasDoAcordo) {
		this.parcelasDoAcordo = parcelasDoAcordo;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public BigDecimal getTotalAcordo() {
		return totalAcordo;
	}

	public void setTotalAcordo(BigDecimal totalAcordo) {
		this.totalAcordo = totalAcordo;
	}

	public BigDecimal getValorEntrada() {
		return valorEntrada;
	}

	public void setValorEntrada(BigDecimal valorEntrada) {
		this.valorEntrada = valorEntrada;
	}

	public Integer getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(Integer quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public List<Debito> getDebitosDoAcordo() {
		return debitosDoAcordo;
	}

	public void setDebitosDoAcordo(List<Debito> debitosDoAcordo) {
		this.debitosDoAcordo = debitosDoAcordo;
	}


	public Integer getDiaVencimento() {
		return diaVencimento;
	}


	public void setDiaVencimento(Integer diaVencimento) {
		this.diaVencimento = diaVencimento;
	}
	
	
	
	public ConfiguracaoAcordo getConfiguracao() {
		return configuracao;
	}


	public void setConfiguracao(ConfiguracaoAcordo configuracao) {
		this.configuracao = configuracao;
	}


	@Override
	public String toString() {
		return SerializationUtils.toJson(this);
	}
	
	public static void main(String[] args) {
		BigDecimal zero = new BigDecimal(0);
		System.out.println(zero.compareTo(new BigDecimal(0)));
		System.out.println(zero.compareTo(new BigDecimal(-1)));
		System.out.println(zero.compareTo(new BigDecimal(1)));
	}

}
