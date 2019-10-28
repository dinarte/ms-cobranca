package br.com.eflux.financeiro.domain;

import java.util.Date;
import java.util.Objects;

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
 * Mapeamento das correções monetárias a serem aplcadas nas parcelas.
 * @author dinarte
 *
 */
@AutoCrud(name="Pessoas", description="Pedro Vitor", 
funtionality=@Functionality(isPublic=false, name="Teste Pedro", menu="root->Cadastros Básicos->pedro"))
@Entity
@Table(schema="financeiro", name="correcao")
public class Correcao implements Persistable<Long>, Migrable<Long>{
	
	@Id
	@GeneratedValue(generator = "correcaoGenerator")
	@GenericGenerator(name = "correcaoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.correcao_seq") })
	@Column(name = "id_correcao", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	@EnableAutoCrudField(label="Descrição", enableForFilter=true)
	private String descricao;
	
	private Double porcentagem;
	
	private Date DataCorrecao;
	
	private String originalId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Double porcentagem) {
		this.porcentagem = porcentagem;
	}

	public Date getDataCorrecao() {
		return DataCorrecao;
	}

	public void setDataCorrecao(Date dataCorrecao) {
		DataCorrecao = dataCorrecao;
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
