package br.com.eflux.financeiro.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


/**
 * Débitos envolvidos em um acordo, aqui são relacionados todos os débitos envlvidos em um acordo.
 * @author dinarte
 *
 */

@Entity
@Table(schema="financeiro", name="acordo_debito")
public class AcordoDebito {
	
	@Id
	@GeneratedValue(generator = "acordoGenerator")
	@GenericGenerator(name = "acordoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "financeiro.acordo_seq") })
	@Column(name = "id_acordo_debito", unique = true, nullable = false)
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_acordo")
	private Acordo acordo;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="id_debito")
	private Debito debito;


	
	@Enumerated(EnumType.STRING)
	private OrigemDebitoAcordoEnum origem;
	
	public AcordoDebito() {
		super();
	}
	
	public AcordoDebito(Acordo acordo, Debito debito, OrigemDebitoAcordoEnum origem) {
		this.acordo = acordo;
		this.debito = debito;
		this.origem = origem;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Acordo getAcordo() {
		return acordo;
	}

	public void setAcordo(Acordo acordo) {
		this.acordo = acordo;
	}

	public Debito getDebito() {
		return debito;
	}

	public void setDebito(Debito debito) {
		this.debito = debito;
	}

	public OrigemDebitoAcordoEnum getOrigem() {
		return origem;
	}

	public void setOrigem(OrigemDebitoAcordoEnum origem) {
		this.origem = origem;
	}

}
