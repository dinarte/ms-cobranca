package br.com.eflux.financeiro.domain;

import java.math.BigDecimal;
import java.util.Date;
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
 * Mapeamento das correções monetárias a serem aplcadas nas parcelas.
 * @author dinarte
 *
 */
@Entity
@Table(schema="financeiro", name="correcao")
public class Correcao implements Persistable<Long>{
	
	@Id
	@GeneratedValue(generator = "correcaoGenerator")
	@GenericGenerator(name = "correcaoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.correcao_seq") })
	@Column(name = "id_correcao", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	private String descricao;
	
	private Double porcentagem;
	
	private Date DataCorrecao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Double porcentagem) {
		this.porcentagem = porcentagem;
	}

	public Date getDataCorrecao() {
		return DataCorrecao;
	}

	public void setDataCorrecao(Date dataCorrecao) {
		DataCorrecao = dataCorrecao;
	}

	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}
	
	

}
