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

import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.repository.Migrable;
import br.com.dfframeworck.security.Functionality;

/**
 * Débitos gerados a partir de um contrato que foi firmado, 
 * nem todo contrato gera débidos, o que vai determinar isso é a natureza e a situacao do contrato.
 * @author dinarte
 *
 */
@Entity
@Table(schema="financeiro", name="debito")
@AutoCrud(name="Débitos", description="Débitos do Contrato", 
funtionality=@Functionality(isPublic=false, name="Débitos do Contrato", menu="root->Financeiro->debito"))
public class Debito implements Persistable<Long>, Migrable<Long> {
	
	@Id
	@GeneratedValue(generator = "lancamentoGenerator")
	@GenericGenerator(name = "lancamentoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.lancamento_seq") })
	@Column(name = "id_lancamento", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	@EnableAutoCrudField(label="Número", enableForList=true, ordinal=1)
	private Integer numero;
	
	@EnableAutoCrudField(label="Contrato", enableForFilter=true, enableForList=true, ordinal=2, lookUpFieldName="numero")
	@ManyToOne
	@JoinColumn(name="id_contrato")
	private Contrato contrato;

	@EnableAutoCrudField(label="Tipo", enableForFilter=true, enableForList=true, ordinal=3, lookUpFieldName="nome")
	@ManyToOne
	@JoinColumn(name="id_tipo_lancamento")
	private TipoLancamento tipoLancamento;
	
	@EnableAutoCrudField(label="Número", enableForFilter=true, enableForList=true, ordinal=4)
	private BigDecimal valorOriginal;
	
	@EnableAutoCrudField(label="Júros por Atrazo", enableForList=true, ordinal=5)
	private BigDecimal jurosAtrazo;

	@EnableAutoCrudField(label="Multa por Atrazo", enableForList=true, ordinal=6)
	private BigDecimal multaAtrazo;
	
	@EnableAutoCrudField(label="Júros Remuneratório", enableForList=true, ordinal=7)
	private BigDecimal jurosRemuneratorio;
	
	@EnableAutoCrudField(label="Correção", enableForList=true, ordinal=8)
	private BigDecimal correcao;
	
	@EnableAutoCrudField(label="Data", enableForList=true, ordinal=9)
	private Date dataLancamento;
	
	@EnableAutoCrudField(label="Vencimento", enableForList=true, ordinal=10)
	private Date dataVencimento;
	
	@EnableAutoCrudField(label="Data", ordinal=11)
	private Date dataUltimoPagamento;
	
	@EnableAutoCrudField(label="Valor Pago", ordinal=12)
	private  BigDecimal valorPago;
	
	@EnableAutoCrudField(label="Quitada", ordinal=13)
	private boolean quitada;
	
	@EnableAutoCrudField(label="Observações", ordinal=14)
	private String descricao;
	
	@Column(name="originalId")
	private String originalId;

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

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public BigDecimal getJurosRemuneratorio() {
		return jurosRemuneratorio;
	}

	public void setJurosRemuneratorio(BigDecimal jurosRemuneratorio) {
		this.jurosRemuneratorio = jurosRemuneratorio;
	}

	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}

}
