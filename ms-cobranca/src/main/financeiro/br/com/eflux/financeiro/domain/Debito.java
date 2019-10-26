package br.com.eflux.financeiro.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Persistable;

/**
 * Débitos gerados a partir de um contrato que foi firmado, 
 * nem todo contrato gera débidos, o que vai determinar isso é a natureza e a situacao do contrato.
 * @author dinarte
 *
 */
@Entity
@Table(schema="financeiro", name="debito")
public class Debito implements Persistable<Long> {
	
	@Id
	@GeneratedValue(generator = "lancamentoGenerator")
	@GenericGenerator(name = "lancamentoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.lancamento_seq") })
	@Column(name = "id_lancamento", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	private Integer numero;

	@ManyToOne
	@JoinColumn(name="id_tipo_lancamento")
	private TipoLancamento tipoLancamento;
	
	private BigDecimal valorOriginal;
	
	private BigDecimal jurosAtrazo;

	private BigDecimal multaAtrazo;
	
	private BigDecimal correcao;
	
	private Date dataLancamento;
	
	private Date dataVencimento;
	
	private Date dataUltimoPagamento;
	
	private  BigDecimal valorPago;
	
	private boolean quitada;
	
	private String descricao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}



	public BigDecimal getValorOriginal() {
		return valorOriginal;
	}

	public void setValorOriginal(BigDecimal valorOriginal) {
		this.valorOriginal = valorOriginal;
	}

	public BigDecimal getJurosAtrazo() {
		return jurosAtrazo;
	}

	public void setJurosAtrazo(BigDecimal jurosAtrazo) {
		this.jurosAtrazo = jurosAtrazo;
	}

	public BigDecimal getMultaAtrazo() {
		return multaAtrazo;
	}

	public void setMultaAtrazo(BigDecimal multaAtrazo) {
		this.multaAtrazo = multaAtrazo;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataUltimoPagamento() {
		return dataUltimoPagamento;
	}

	public void setDataUltimoPagamento(Date dataUltimoPagamento) {
		this.dataUltimoPagamento = dataUltimoPagamento;
	}

	public BigDecimal getCorrecao() {
		return correcao;
	}

	public void setCorrecao(BigDecimal correcao) {
		this.correcao = correcao;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public boolean isQuitada() {
		return quitada;
	}

	public void setQuitada(boolean quitada) {
		this.quitada = quitada;
	}

}
