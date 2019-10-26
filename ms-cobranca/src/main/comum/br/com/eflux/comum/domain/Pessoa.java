package br.com.eflux.comum.domain;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Persistable;

import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.repository.Migrable;
import br.com.dfframeworck.security.Functionality;

/**
 * Cadastro pessoal das pessoas envolvidas, pode ser de um cliente, de uma incorporadora, etc... 
 * @author Dinarte
 *
 */

@Entity
@Table(name = "pessoa", schema = "comum")
@AutoCrud(name="Pessoas", description="Pessoas e Empresas", 
	funtionality=@Functionality(isPublic=false, name="Gerenciar Pessoas e Empresas", menu="root->Cadastros Básicos->pessoas"))
public class Pessoa implements Persistable<Long>, Migrable<Long>{

	@Id
	@GeneratedValue(generator = "pessoaGenerator")
	@GenericGenerator(name = "pessoaGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "comum.pessoa_seq") })
	@Column(name = "id_pessoa", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;

	@EnableAutoCrudField(label="Nome", enableForFilter=true)
	@Column(name = "nome", nullable = false)
	private String nome;


	@Column(name = "abreviacao")
	private String abreviacao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_nascimento")
	private Date dataNascimento;

	@Enumerated(EnumType.STRING)
	@Column(name = "sexo")
	private SexoEnum sexo;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado_civil", nullable = true)
	private EstadoCivilEnum estadoCivil;

	@Column(name = "naturalidade")
	private String naturalidade;

	@ManyToOne
	@JoinColumn(name = "id_pais_nascimento")
	private Pais paisNascimento;

	@Column(name = "cpf", nullable = true, unique = false)
	private String cpf;

	@Column(name = "rg")
	private String rg;

	@Column(name = "orgao_emissor_rg")
	private String orgaoEmissorRg;

	@Column(name = "inscricao_municipal")
	private String inscricaoMunicipal;

	@Column(name = "email")
	private String email;

	@Column(name = "logradouro")
	private String logradouro;

	@Column(name = "numero")
	private String numero;

	@Column(name = "complemento")
	private String complemento;

	@Column(name = "bairro")
	private String bairro;

	@Column(name = "cep")
	private String cep;

	@ManyToOne
	@JoinColumn(name = "municipio_id")
	private Municipio municipio;

	@Column(name = "telefone_fixo")
	private String telefoneFixo;

	@Column(name = "telefone_celular")
	private String telefoneCelular;

	@Column(name = "telefone_corporativo")
	private String telefoneCorporativo;
	
	@Column(name = "original_id")
	private String originalId;

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

	public String getAbreviacao() {
		return abreviacao;
	}

	public void setAbreviacao(String abreviacao) {
		this.abreviacao = abreviacao;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public SexoEnum getSexo() {
		return sexo;
	}

	public void setSexo(SexoEnum sexo) {
		this.sexo = sexo;
	}

	public EstadoCivilEnum getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivilEnum estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getNaturalidade() {
		return naturalidade;
	}

	public void setNaturalidade(String naturalidade) {
		this.naturalidade = naturalidade;
	}

	public Pais getPaisNascimento() {
		return paisNascimento;
	}

	public void setPaisNascimento(Pais paisNascimento) {
		this.paisNascimento = paisNascimento;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getOrgaoEmissorRg() {
		return orgaoEmissorRg;
	}

	public void setOrgaoEmissorRg(String orgaoEmissorRg) {
		this.orgaoEmissorRg = orgaoEmissorRg;
	}

	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public String getTelefoneFixo() {
		return telefoneFixo;
	}

	public void setTelefoneFixo(String telefoneFixo) {
		this.telefoneFixo = telefoneFixo;
	}

	public String getTelefoneCelular() {
		return telefoneCelular;
	}

	public void setTelefoneCelular(String telefoneCelular) {
		this.telefoneCelular = telefoneCelular;
	}

	public String getTelefoneCorporativo() {
		return telefoneCorporativo;
	}

	public void setTelefoneCorporativo(String telefoneCorporativo) {
		this.telefoneCorporativo = telefoneCorporativo;
	}
	
	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}

	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}
	
	
}