package br.com.eflux.empreendimento.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@AutoCrud(name="Zonas", description="Gerenciar Zonas do Empreendimento", 
funtionality=@Functionality(isPublic=false, name="Gerenciar Zonas de uma Unidade"))
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
	@NotNull
	private String nome;

	/**
	 * Empreendimento no qual a zona pertence.
	 */
	@NotNull
	@EnableAutoCrudField(label="Empreendimento", enableForList=true, enableForFilter=true, lookUpFieldName="nome", ordinal=2)
	@ManyToOne
	@JoinColumn(name="id_empreendimento", nullable = false)
	private Empreendimento empreendimento;
	
	/**
	 * Zona um nível a cima na hieraquia, por exemplo, se a zona em questão é Quadra, a zona pai poderia ser Etapa...
	 */
	@EnableAutoCrudField(label="Zona Pai", enableForList=true, enableForFilter=true, lookUpFieldName="fullName", 
			ordinal=3, ui="custom/zonaSelectMenu")
	@ManyToOne(optional=true)
	@JoinColumn(name="id_zona_pai", nullable=true)
	private Zona zonaPai;
	
	/**
	 * Nível na hierarquia das zonas.
	 */
	@Column(name="nivel")
	private String nivel = "0";

	
	@JoinColumn(name="original_id")
	private String originalId;
	
	
	
	public String getFullName() {
		
		if (Objects.isNull(zonaPai))
			return this.nome;
		
		List<String> zonasPais = new ArrayList<String>();
		Zona zonaAtual = this;
		zonasPais.add(this.nome);
		while (zonaAtual.getZonaPai() != null) {
			zonasPais.add(zonaAtual.getZonaPai().getNome());
			zonaAtual = zonaAtual.getZonaPai();
		}
		
		Collections.reverse(zonasPais);
		
		String fullName = zonasPais.stream().map(zona-> zona.concat(" / ")).reduce(String::concat).get();
		fullName = fullName.substring(0,fullName.lastIndexOf(" / "));
		
		return fullName;
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
	
	

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}
	
	@Override
	public String toString() {
		return getFullName();
	}
	
	
}
