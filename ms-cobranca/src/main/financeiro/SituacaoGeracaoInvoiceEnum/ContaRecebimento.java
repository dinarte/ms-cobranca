package SituacaoGeracaoInvoiceEnum;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.repository.Migrable;
import br.com.dfframeworck.security.Functionality;
import br.com.eflux.comum.domain.Pessoa;

/**
 * Uma conta de recebimento é uma conta pela qual se recebe pagamentos, pode ser uma conta bancária, a tesouraria, ou uma conta em uma finthec.
 * @author dinarte
 *
 */
@Entity
@Table(name = "conta_recebimento", schema = "financeiro")
@Cacheable
@AutoCrud(name="Conta de Recebimento", description="Contas de Recebimento", 
funtionality=@Functionality(isPublic=false, name="Conta de Recebimento", menu="root->Financeiro->contaRecebimento", icon="fa fa-bank"))
public class ContaRecebimento implements Persistable<Long>, Migrable<Long>{
	
	
	@Id
	@GeneratedValue(generator = "contaRecebimentoGenerator")
	@GenericGenerator(name = "contaRecebimentoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "comum.conta_recebimento_seq") })
	@Column(name = "id_conta_recebimento")
	private Long id;
	
	/**
	 * Identifica a conta de forma textual como por exemplo:
	 * (Bradesco Ag123 CC:1234-5) ou (Tesouraria), etc...
	 */
	@EnableAutoCrudField(label="Descrição", enableForFilter=true, enableForList=true, ordinal=1)
	@Column(name = "descricao")
	private String descricao;
	
	/**
	 * Pessoa a qual a conta pertence.
	 */
	@ManyToOne
	@JoinColumn(name="id_pessoa")
	@EnableAutoCrudField(label="Pessoa", enableForFilter=true, enableForList=true, ordinal=2, lookUpFieldName="nome")
	private Pessoa pessoa;	
	
	@Column(name="original_id")
	private String originalId;
	
	

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return descricao;
	}


}
