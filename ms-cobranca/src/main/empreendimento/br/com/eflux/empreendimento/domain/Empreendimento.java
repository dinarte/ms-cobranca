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
import br.com.dfframeworck.security.Functionality;

/**
 * Empreendimento que será controlado pelo sistema.
 * @author dinarte
 *
 */
@Entity
@Table(schema="empreendimento", name="emprendimento")
@AutoCrud(name="Emmpreendimento", description="Empreendimentos disponíveis", 
funtionality=@Functionality(isPublic=false, name="Gerenciar Empreendimentos", menu="root->Empreendimentos->empreendimento"))
public class Empreendimento implements Persistable<Long> {
	
	@Id
	@GeneratedValue(generator = "empreendimentoGenerator")
	@GenericGenerator(name = "empreendimentoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.empreendimento_seq") })
	@Column(name = "id_empreendimento", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	/**
	 * Decreve de forma objetiva o empreendimento, exemplo: Solar de Pinun... 
	 */
	@EnableAutoCrudField(label="Nome", enableForFilter=true)
	@Column(name = "nome", unique = true, nullable = false, insertable = true, updatable = true, length=80)
	private String nome;
	
	/**
	 * Incorporadora a qual pertence o empreendimento
	 */
	@ManyToOne
	@JoinColumn(name = "id_empresa")
	@EnableAutoCrudField(label="Incorporadora", enableForFilter=true)
	private Incorporadora incorporadora;

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
	
	
	
	public Incorporadora getIncorporadora() {
		return incorporadora;
	}

	public void setIncorporadora(Incorporadora incorporadora) {
		this.incorporadora = incorporadora;
	}

	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}
	
	
}