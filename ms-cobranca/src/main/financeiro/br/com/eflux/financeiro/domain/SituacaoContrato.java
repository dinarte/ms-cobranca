package br.com.eflux.financeiro.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Persistable;

/**
 * Indica as situações que um contrato pode assumir, como por exemplo: PREPARAÇAO, ATIVO, CANCELADO etc...    
 * @author dinarte
 */
@Entity
@Table(schema="financeiro", name="situacao_contrato")
public class SituacaoContrato implements Persistable<Long> {
	
	@Id
	@GeneratedValue(generator = "sitContratoGenerator")
	@GenericGenerator(name = "sitContratoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.situacao_contrato_seq") })
	@Column(name = "id_situacao_contrato", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	/**
	 * Descreve a situação em questão
	 */
	@Column(name = "nome", unique = true, nullable = false, insertable = true, updatable = true, length=80)
	private String nome;
	
	/**
	 * Caso true, ativa a processo para gerar os lançamentos do contrato quando o mesmo assumir 
	 * a situação em questão.
	 */
	@Column(name = "gera_financeiro", nullable = false, insertable = true, updatable = true)
	private boolean geraFinanceiro;
	
	/**
	 * Informa que uma unidade relacionada a um contrato com a situação em questão, ainda permanecerá disponível, 
	 * como por exemplo, uma situação CENCELADA, a unidade referente a este contrato, deverá ser disponibilizada para 
	 * ser associada a outro contrato
	 */
	@Column(name = "disponibilizar_unidade", nullable = false, insertable = true, updatable = true)
	private boolean disponibilizarUnidade;
	
	/**
	 * Determina se um contrato com esse status está ativo.
	 */
	@Column(name = "contrato_ativo", nullable = false, insertable = true, updatable = true)
	private boolean contrato_ativo;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isGeraFinanceiro() {
		return geraFinanceiro;
	}

	public void setGeraFinanceiro(boolean geraFinanceiro) {
		this.geraFinanceiro = geraFinanceiro;
	}

	public boolean isDisponibilizarUnidade() {
		return disponibilizarUnidade;
	}

	public void setDisponibilizarUnidade(boolean disponibilizarUnidade) {
		this.disponibilizarUnidade = disponibilizarUnidade;
	}

	public boolean isContrato_ativo() {
		return contrato_ativo;
	}

	public void setContrato_ativo(boolean contrato_ativo) {
		this.contrato_ativo = contrato_ativo;
	}
	
	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}
	
}
