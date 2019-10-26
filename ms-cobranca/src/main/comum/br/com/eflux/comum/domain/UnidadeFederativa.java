/* Direitos de Propriedade da SIG Software e Consultoria em TI, CNPJ 13.406.686/0001-67,
 * NÃO ALTERE OU REMOVA O COPYRIGHT DO CABEÇALHO DESTE ARQUIVO.
 *
 * Este código é um código proprietário e não pode ser redistribuído, copiado ou
 * modificado sem expressa autorização de seu proprietário. Violações do direito autoral
 * podem ser noticadas através de contato@esig.com.br.
 *
 */
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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Persistable;

import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.repository.Migrable;
import br.com.dfframeworck.security.Functionality;

/**
 * Estados da federação
 * @author Dinarte
 */
@Entity
@Table(name = "unidade_federativa", schema = "comum")
@Cacheable
@AutoCrud(name="Unidade Federativa", description = "Representa os Estados ou Províncias de um País", 
	funtionality= @Functionality(name="Estados do BR", isPublic=false, menu="root->Cadastros Básicos->uf"))
public class UnidadeFederativa implements Persistable<Long>, Migrable<Long>{

	@Id
	@GeneratedValue(generator = "ufGenerator")
	@GenericGenerator(name = "ufGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "comum.unidade_federativa_seq") })
	@Column(name = "id_unidade_federativa")
	private Long id;

	@EnableAutoCrudField(label="Estado")
	@Column(name = "nome")
	private String nome;

	@EnableAutoCrudField(label="Sigla")
	@Column(name = "sigla")
	private String sigla;

	@EnableAutoCrudField(label="País", lookUpFieldName="nome")
	@ManyToOne
	@JoinColumn(name = "id_pais")
	private Pais pais;
	
	@Column(name = "original_id")
	private String originalId;

	public UnidadeFederativa() {
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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}
	
	
	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}

	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}
}
