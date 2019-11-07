package br.com.eflux.empreendimento.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Persistable;

import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.repository.Migrable;
import br.com.dfframeworck.security.Functionality;
import br.com.eflux.comum.domain.Pessoa;

/**
 * mapeia as incorporadoras existentes.
 * @author dinarte
 *
 */
@Entity
@Table(schema="empreendimento", name="incorporadora")
@AutoCrud(name="Incorporadoras", description="Gerenciar Incoorporadoras", 
funtionality=@Functionality(isPublic=false, name="Gerenciar Incorporadoras", menu="root->Empreendimentos->incorporadora", icon="fa fa-industry"))
public class Incorporadora implements Persistable<Long>, Migrable<Long>{
	
	@Id
	@GeneratedValue(generator = "incorporadoraGenerator")
	@GenericGenerator(name = "incorporadoraGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.encorporadora_seq") })
	@Column(name = "id_incorporadora", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	/**
	 * Pessoa, geralmente jurídica da incorporadora
	 */
	@OneToOne
	@JoinColumn(name="id_pessoa")
	@EnableAutoCrudField(label="Incorporadora", enableForList=true, enableForFilter=true, lookUpFieldName="nome")
	private Pessoa pessoa;
	
	@JoinColumn(name="original_id")
	private String originalId;

	@Transient 
	public String getNome() {
		if (pessoa != null)
			return pessoa.getNome();
		return "#"+id;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
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
		if (Objects.isNull(pessoa))
			return super.toString();
		return pessoa.getNome();
	}
	

}
