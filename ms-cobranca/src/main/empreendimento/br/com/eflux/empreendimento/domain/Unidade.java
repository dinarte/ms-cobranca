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

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.repository.Migrable;
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
funtionality=@Functionality(isPublic=false, name="Gerenciar Unidades"))
public class Unidade implements Persistable<Long>, Migrable<Long>{
	
	@Id
	@GeneratedValue(generator = "unidadeGenerator")
	@GenericGenerator(name = "unidadeGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.unidade_seq") })
	@Column(name = "id_unidade", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="id_zona")
	@EnableAutoCrudField(label="Zona", enableForFilter=true, enableForList=true, ordinal=1, lookUpFieldName="fullName", ui="custom/zonaSelectMenu")
	private Zona zona;
	
	/**
	 * Nome da unidade em questão, pode ser por exempo o número do lote L-01, ou o número de um apartamento 302, etc...
	 */
	@EnableAutoCrudField(label="Nome", enableForFilter=true, enableForList=true, ordinal=2)
	@Column(name = "nome", unique = false, nullable = false, insertable = true, updatable = true, length=80)
	private String nome;
	
	/**
	 * Descrição das características da unidade, como por exemplo: casa com 2 quartos, etc..  
	 */
	@EnableAutoCrudField(label="Descrição", enableForList=true, ordinal=3)
	@Column(name = "descricao", unique = false, nullable = true, insertable = true, updatable = true, length=800)
	private String descricao;
	
	/**
	 * áera da unidade em metros quadrados.
	 */
	@EnableAutoCrudField(label="Área", enableForList=true, ordinal=4)
	@Column(name = "area", unique = false, nullable = true, insertable = true, updatable = true)
	private Double area;
	
	/**
	 * Seequencial imobiliário da unidade 
	 */
	@EnableAutoCrudField(label="Sequencial Imobiliário", ordinal=5)
	@Column(name = "sequencial_imobiliario", unique = false, nullable = true, insertable = true, updatable = true, length=80)
	private String sequancialImobiliario;
	
	@EnableAutoCrudField(label="Matrícula", ordinal=6)
	@Column(name = "matricula", unique = false, nullable = true, insertable = true, updatable = true, length=80)
	private String matricula;
	
	/**
	 * Preenchido com a pessoa que recebeu o imóvel em caso de permuta.
	 */
	@EnableAutoCrudField(label="Permuta", ordinal=7)
	@ManyToOne
	@JoinColumn(name="id_pessoa_permulta")
	private Pessoa pessoaPermulta;
	
	@Column(name="original_id")
	private String originalId;

	
	public String getFulName() {
		if (Objects.isNull(zona))
			return nome;
		return zona.getFullName().concat("->").concat(nome);
	}
	
	
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
	
	@JsonIgnore
	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}

	public Zona getZona() {
		return zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
	}

	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}
	
	@Override
	public String toString() {
		return nome;
	}
	
}
