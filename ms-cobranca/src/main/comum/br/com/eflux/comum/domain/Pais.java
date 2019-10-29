package br.com.eflux.comum.domain;

import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Persistable;

import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.repository.Migrable;
import br.com.dfframeworck.security.Functionality;

/**
 * Pais existentes
 * @author dinarte
 *
 */
@Entity
@Table(name = "pais", schema = "comum")
@Cacheable
@AutoCrud(name="País", description="Países do Sistema", 
funtionality=@Functionality(isPublic=false, name="Gerenciar Países", menu="root->Cadastros Básicos->pais"))
public class Pais implements Persistable<Long>, Migrable<Long> {

	@Id
	@GeneratedValue(generator = "paisGenerator")
	@GenericGenerator(name = "paisGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "comum.pais_seq") })
	@Column(name = "id_pais")
	private Long id;

	@EnableAutoCrudField(label="País", enableForFilter=true, enableForList=true, ordinal=1)
	@Column(name = "nome_pais")
	private String nome;

	@EnableAutoCrudField(label="Ativo", ui="simpleCheckBox", enableForList=true, ordinal=2)
	@Column(name = "ativo")
	private boolean ativo;
	
	@Column(name="original_id")
	private String originalId;

	public Pais() {
	}

	public Pais(Long id) {
		this.id = id;
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

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
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
	
	@Override
	public String toString() {
		return nome;
	}

}
