package br.com.eflux.empreendimento.domain;

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
import br.com.dfframeworck.security.Functionality;
import br.com.eflux.comum.domain.Pessoa;

/**
 * Unidade representa o bem que pode ser vendido, um lote, uma casa, um apartamento, etc... 
 * @author dinarte
 *
 */
@Entity
@Table(schema="empreendimento", name="unidade")
@AutoCrud(name="Unidades", description="Lotes, Casas, Apartamentos, etc...", 
funtionality=@Functionality(isPublic=false, name="Gerenciar Unidades", menu="root->Empreendimentos->unidades"))
public class Unidade implements Persistable<Long>{
	
	@Id
	@GeneratedValue(generator = "unidadeGenerator")
	@GenericGenerator(name = "unidadeGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.unidade_seq") })
	@Column(name = "id_unidade", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	/**
	 * Nome da unidade em questão, pode ser por exempo o número do lote L-01, ou o número de um apartamento 302, etc...
	 */
	@Column(name = "nome", unique = false, nullable = false, insertable = true, updatable = true, length=80)
	private String nome;
	
	/**
	 * Descrição das características da unidade, como por exemplo: casa com 2 quartos, etc..  
	 */
	@Column(name = "descricao", unique = false, nullable = true, insertable = true, updatable = true, length=800)
	private String descricao;
	
	/**
	 * áera da unidade em metros quadrados.
	 */
	@Column(name = "area", unique = false, nullable = true, insertable = true, updatable = true)
	private Double area;
	
	/**
	 * Seequencial imobiliário da unidade 
	 */
	@Column(name = "sequencial_imobiliario", unique = false, nullable = true, insertable = true, updatable = true, length=80)
	private String sequancialImobiliario;
	
	@Column(name = "matricula", unique = false, nullable = true, insertable = true, updatable = true, length=80)
	private String matricula;
	
	/**
	 * Preenchido com a pessoa que recebeu o imóvel em caso de permuta.
	 */
	@ManyToOne
	@JoinColumn(name="id_pessoa_permulta")
	private Pessoa pessoaPermulta;

	
	
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Pessoa getPessoaPermulta() {
		return pessoaPermulta;
	}

	public void setPessoaPermulta(Pessoa pessoaPermulta) {
		this.pessoaPermulta = pessoaPermulta;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public String getSequancialImobiliario() {
		return sequancialImobiliario;
	}

	public void setSequancialImobiliario(String sequancialImobiliario) {
		this.sequancialImobiliario = sequancialImobiliario;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}
	
}
