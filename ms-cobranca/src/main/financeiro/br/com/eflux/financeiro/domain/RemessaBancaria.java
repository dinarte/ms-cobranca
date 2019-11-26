package br.com.eflux.financeiro.domain;

import java.util.Date;
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
import br.com.eflux.payments.api.BatchFile;

@Entity
@Table(schema="financeiro", name="remessa_bancaria")
@AutoCrud(name="Remessa Bancária", description="Remessas", orderBy="dataCriacao desc", operations= {"list"},
	funtionality=@Functionality(isPublic=false, name="Remessas", menu="root->Financeiro->Remessas Bancárias->Gerenciar",icon="fas fa-file-code"))
public class RemessaBancaria implements BatchFile, Persistable<Long> {

	@Id
	@GeneratedValue(generator = "remessaGenerator")
	@GenericGenerator(name = "remessaGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "financeiro.remessa_bancaria_seq") })
	@Column(name = "id_remessa_bancaria", unique = true, nullable = false, insertable = true, updatable = true)
	private Long id;
	
	
	private String tokenId;
	
	@EnableAutoCrudField(label="Arquivo", enableForList=true, ordinal=1)
	private String name;
	
	private String location;
	
	private byte[] file;
	
	@ManyToOne
	@JoinColumn(name="id_conta_recebimento")
	@EnableAutoCrudField(label="Conta de Recebimento", enableForList=true, enableForFilter=true, ordinal=0, lookUpFieldName="descricao")
	private ContaRecebimento contaRecebimento;
	
	@EnableAutoCrudField(label="Data Criação", enableForList=true, ordinal=3, formater="dateTimeFormater")
	private Date dataCriacao = new Date();

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public ContaRecebimento getContaRecebimento() {
		return contaRecebimento;
	}

	public void setContaRecebimento(ContaRecebimento contaRecebimento) {
		this.contaRecebimento = contaRecebimento;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}
	
	
}
