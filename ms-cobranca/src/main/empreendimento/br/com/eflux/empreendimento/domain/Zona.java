package br.com.eflux.empreendimento.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * A Zona representa um agrupamento de unidades de um determinado empreendimento, 
 * como por exemplo: Quadra, Torre etc... É possível montar uma estrutura hierárquica 
 * através do campo zonaPai
 * @author dinarte
 *
 */
@Entity
@Table(schema="empreendimento", name="zona")
public class Zona {

	@Id
	@GeneratedValue(generator = "zonaGenerator")
	@GenericGenerator(name = "zonaGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.zona_seq") })
	@Column(name = "id_zona", unique = true, nullable = false, insertable = true, updatable = true)
	private int id;
	
	/**
	 * Descreve o nome da zona, exemplo: Etapa, Quadra, Torre, etc... 
	 */
	@Column(name = "nome", unique = false, nullable = false, insertable = true, updatable = true, length=80)
	private String nome;
	
	/**
	 * Zona um nível a cima na hieraquia, por exemplo, se a zona em questão é Quadra, a zona pai poderia ser Etapa...
	 */
	@ManyToOne
	@JoinColumn(name="id_zona_pai")
	private Zona zonaPai;
	
	/**
	 * Empreendimento no qual a zona pertence.
	 */
	@ManyToOne
	@JoinColumn(name="id_empreendimento", nullable = false)
	private Empreendimento empreendimento;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Zona getZonaPai() {
		return zonaPai;
	}

	public void setZonaPai(Zona zonaPai) {
		this.zonaPai = zonaPai;
	}
	
}
