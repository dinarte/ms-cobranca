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

/**
 * Determina o tipo de um dado lançamento financeiro, como por exemplo: Parcela, Balão, Chaves, etc... 
 * @author dinarte
 *
 */
@Entity
@Table(schema="financeiro", name="tipo_lancamento")
public class TipoLancamento  implements Persistable<Long>{
	
	@Id
	@GeneratedValue(generator = "tipoLancamentoGenerator")
	@GenericGenerator(name = "tipoLancamentoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.situacao_contrato_seq") })
	@Column(name = "id_situacao_contrato", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	@Column(name = "nome", unique = true, nullable = false, insertable = true, updatable = true, length=80)
	private String nome;
	
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



	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}
	
}
