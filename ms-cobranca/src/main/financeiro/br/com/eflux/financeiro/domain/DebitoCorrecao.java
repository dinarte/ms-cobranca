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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Correcao que foi aplicada na parcela (memória de calculo)
 * @author dinarte
 *
 */
@Entity
@Table(schema="financeiro", name="debito_correcao")
public class DebitoCorrecao implements Persistable<Long>{
	
	@Id
	@GeneratedValue(generator = "debitocorrecaoGenerator")
	@GenericGenerator(name = "debitocorrecaoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.correcao_seq") })
	@Column(name = "id_debito_correcao", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="id_debito")
	private Debito debito;
	
	@ManyToOne
	@JoinColumn(name="id_correcao")	
	private Correcao correcao;
	
	private BigDecimal valorAcrescentado;
	
	private Date dataAlicacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Debito getDebito() {
		return debito;
	}

	public void setDebito(Debito debito) {
		this.debito = debito;
	}

	public BigDecimal getValorAcrescentado() {
		return valorAcrescentado;
	}

	public void setValorAcrescentado(BigDecimal valorAcrescentado) {
		this.valorAcrescentado = valorAcrescentado;
	}

	public Date getDataAlicacao() {
		return dataAlicacao;
	}

	public void setDataAlicacao(Date dataAlicacao) {
		this.dataAlicacao = dataAlicacao;
	}

	public Correcao getCorrecao() {
		return correcao;
	}

	public void setCorrecao(Correcao correcao) {
		this.correcao = correcao;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}
}
