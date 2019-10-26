package br.com.eflux.financeiro.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Persistable;

import br.com.eflux.comum.domain.Pessoa;
import br.com.eflux.empreendimento.domain.Unidade;

/**
 * O contrarto é gerado quando uma unidade é associada a um cliente (pessoa) e determinará quais os termos 
 * dessa relação, o campo naturezaContrato por exemplo, determinará se essa relação é uma Venda, uma Doação ou um Permuta, etc...
 * @author dinarte
 */
@Entity
@Table(schema="financeiro", name="contrato")
public class Contrato implements Persistable<Long> {
	
	@Id
	@GeneratedValue(generator = "contatoGenerator")
	@GenericGenerator(name = "contatoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.contrato_seq") })
	@Column(name = "id_contrato", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	@Column(name="numero_contrato")
	private String numeroContrato;
	
	/**
	 * Unidade negociada no contrato
	 */
	@ManyToOne
	@JoinColumn(name="id_unidade", nullable=false)
	private Unidade unidade;
	
	/**
	 * Cliente / parte / contratante que está sendo assiciada a unidade em questão. 
	 */
	@ManyToOne
	@JoinColumn(name="id_pessoa", nullable=false)
	private Pessoa pessoa;
	
	/**
	 * Data em que o contrato foi firmado
	 */
	@Column(name="data", nullable=false)
	private Date data;
	
	/**
	 * Data base para correção financeira do contrato
	 */
	@Column(name="data_base")
	private Date dataBase;
	
	/**
	 * Valor da unidade em questão previsto na tabela de preços
	 */
	@Column(name="valor_tabela", nullable=false)
	private BigDecimal valorTabela;
	
	/**
	 * Valor no qual o contrato foi firmado
	 */
	@Column(name="valor", nullable=false)
	private BigDecimal valor;
	
	/**
	 * Caso true, impede que seja gerado boletos ou pagamentos via 
	 * cartão de crédito para o contrato em questão.
	 */
	@Column(name="bloquear_cobranca", nullable=false)
	private boolean bloquearCobranca;
	
	/**
	 * Determina em que situação o contrato em questão se encontra
	 */
	@ManyToOne
	@JoinColumn(name="id_situacao_venda", nullable=false)
	private SituacaoContrato situacaoVenda;
	
	/**
	 * Determina a natureza do contrato
	 */
	@Enumerated(EnumType.STRING)
	@Column(name="natureza", nullable=false)
	private NaturezaContratoEnum natureza;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Date getDataVenda() {
		return data;
	}

	public void setDataVenda(Date dataVenda) {
		this.data = dataVenda;
	}

	public Date getDataBase() {
		return dataBase;
	}

	public void setDataBase(Date dataBase) {
		this.dataBase = dataBase;
	}

	public BigDecimal getValorTabela() {
		return valorTabela;
	}

	public void setValorTabela(BigDecimal valorTabela) {
		this.valorTabela = valorTabela;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public boolean isBloquearCobranca() {
		return bloquearCobranca;
	}

	public void setBloquearCobranca(boolean bloquearCobranca) {
		this.bloquearCobranca = bloquearCobranca;
	}

	public SituacaoContrato getSituacaoVenda() {
		return situacaoVenda;
	}

	public void setSituacaoVenda(SituacaoContrato situacaoVenda) {
		this.situacaoVenda = situacaoVenda;
	}

	public NaturezaContratoEnum getNatureza() {
		return natureza;
	}

	public void setNatureza(NaturezaContratoEnum natureza) {
		this.natureza = natureza;
	}
	
	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}

}
