package br.com.eflux.comum.domain;

import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Persistable;

import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.repository.Migrable;
import br.com.dfframeworck.security.Functionality;

/**
 * Entidade que representa um município brasileiro.
 *
 * @author Dinarte
 */
@Entity
@Table(name = "municipio", schema = "comum")
@Cacheable
@AutoCrud(name="Município", description="Municípios", 
funtionality=@Functionality(isPublic=false, name="Gerenciar Municípios", menu="root->Cadastros Básicos->municipio"))
public class Municipio implements Persistable<Long> , Migrable<Long>{

	@Id
	@GeneratedValue(generator = "muniGenerator")
	@GenericGenerator(name = "muniGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "comum.municipio_seq") })
	@Column(name = "id_municipio")
	private Long id;

	@EnableAutoCrudField(label="Município", enableForFilter=true)
	@Column(name = "nome")
	private String nome;

	@EnableAutoCrudField(label="CEP / Postal Code")
	@Column(name = "cep")
	private String cep;

	@EnableAutoCrudField(label="Estado / Província", lookUpFieldName="nome", enableForFilter=true)
	@JoinColumn(name = "uf_id")
	@ManyToOne
	private UnidadeFederativa uf;

	@EnableAutoCrudField(label="Código IBGE")
	@Column(name = "cod_ibge")
	private String codIBGE;

	@EnableAutoCrudField(label="Ativo", enableForFilter=true)
	@Column(name = "ativo")
	private boolean ativo;
	
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

	public String getCodIBGE() {
		return codIBGE;
	}

	public void setCodIBGE(String codIBGE) {
		this.codIBGE = codIBGE;
	}     

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public UnidadeFederativa getUf() {
		return uf;
	}

	public void setUf(UnidadeFederativa uf) {
		this.uf = uf;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
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
