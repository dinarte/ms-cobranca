package br.com.eflux.financeiro.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.repository.Migrable;
import br.com.dfframeworck.security.Functionality;

/**
 * Determina o tipo de um dado lançamento financeiro, como por exemplo: Parcela, Balão, Chaves, etc... 
 * @author dinarte
 */
@Entity
@Table(schema="financeiro", name="tipo_lancamento")
@AutoCrud(name="Tipo de Lançamento", description="Tipos de Lançamentos de Débito", 
funtionality=@Functionality(isPublic=false, name="Tipos de Lançamento", menu="root->Financeiro->Tabelas->tipoLancamento", icon="fa fa-table"))
public class TipoLancamento  implements Persistable<Long>, Migrable<Long>{
	
	@Id
	@GeneratedValue(generator = "tipoLancamentoGenerator")
	@GenericGenerator(name = "tipoLancamentoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.situacao_contrato_seq") })
	@Column(name = "id_tipo_lancamento", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	@EnableAutoCrudField(label="Tipo de Lançamento", enableForFilter=true, enableForList=true, ordinal=1)
	@Column(name = "nome", unique = true, nullable = false, insertable = true, updatable = true, length=80)
	private String nome;
	
	@Column(name="original_id")
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

	@JsonIgnore
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
