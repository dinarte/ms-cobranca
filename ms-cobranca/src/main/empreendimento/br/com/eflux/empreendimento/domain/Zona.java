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
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.repository.Migrable;
import br.com.dfframeworck.security.Functionality;

/**
 * A Zona representa um agrupamento de unidades de um determinado empreendimento, 
 * como por exemplo: Quadra, Torre etc... É possível montar uma estrutura hierárquica 
 * através do campo zonaPai
 * @author dinarte
 *
 */
@Entity
@Table(schema="empreendimento", name="zona")
@AutoCrud(name="Incoirporadoras", description="Gerenciar Incoorporadoras", 
funtionality=@Functionality(isPublic=false, name="Gerenciar Zonas de uma Unidade", menu="root->Empreendimentos->unidade", icon="fa fa-sitemap"))
public class Zona implements Persistable<Long>, Migrable<Long>{

	@Id
	@GeneratedValue(generator = "zonaGenerator")
	@GenericGenerator(name = "zonaGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.zona_seq") })
	@Column(name = "id_zona", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	/**
	 * Descreve o nome da zona, exemplo: Etapa, Quadra, Torre, etc... 
	 */
	@EnableAutoCrudField(label="Nome", enableForList=true, enableForFilter=true, ordinal=1)
	@Column(name = "nome", unique = false, nullable = false, insertable = true, updatable = true, length=80)
	private String nome;

	/**
	 * Empreendimento no qual a zona pertence.
	 */
	@EnableAutoCrudField(label="Empreendimento", enableForList=true, enableForFilter=true, lookUpFieldName="nome", ordinal=2)
	@ManyToOne
	@JoinColumn(name="id_empreendimento", nullable = false)
	private Empreendimento empreendimento;
	
	/**
	 * Zona um nível a cima na hieraquia, por exemplo, se a zona em questão é Quadra, a zona pai poderia ser Etapa...
	 */
	@EnableAutoCrudField(label="Zona Pai", enableForList=true, enableForFilter=true, lookUpFieldName="nome", ordinal=3)
	@ManyToOne
	@JoinColumn(name="id_zona_pai")
	private Zona zonaPai;
	

	
	@JoinColumn(name="original_id")
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

	public Zona getZonaPai() {
		return zonaPai;
	}

	public void setZonaPai(Zona zonaPai) {
		this.zonaPai = zonaPai;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
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
	
	@Override
	public String toString() {
		return nome;
	}
	
	
}
