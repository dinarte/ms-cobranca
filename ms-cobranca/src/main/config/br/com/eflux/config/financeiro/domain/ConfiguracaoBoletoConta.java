package br.com.eflux.config.financeiro.domain;

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
import br.com.eflux.financeiro.domain.ContaRecebimento;
import br.com.eflux.payments.api.PaymentApiConfiguration;
import br.com.eflux.payments.api.PaymentApiConfigurationAccount;

/**
 * Indica para cada conta bancária, qual configuração de boleto deverá ser utilizada.
 * @author dinarte
 *
 */
@Entity
@Table(name = "configuracao_boleto_conta", schema = "config")
@Cacheable
@AutoCrud(name="Configurar Contas", description="Adicionar Conta Bancária em Configuração", 
funtionality=@Functionality(isPublic=false, name="Adicionar Contas", menu="root->Configurações->Boletos API->Adicionar Contas"))
public class ConfiguracaoBoletoConta implements Persistable<Long>, Migrable<Long>, PaymentApiConfigurationAccount {
	
	
	@Id
	@GeneratedValue(generator = "configBoletoContaGenerator")
	@GenericGenerator(name = "configBoletoContaGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "config.configuracao_boleto_conta_seq") })
	@Column(name = "id_configuracao_boleto_conta")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="id_conta_bancaria")
	@EnableAutoCrudField(label="Conta Bancária", enableForFilter=true, enableForList=true, ordinal=1)
	private ContaRecebimento contaBancaria;

	@ManyToOne
	@JoinColumn(name="id_configuracao_boleto")
	@EnableAutoCrudField(label="Configuração", enableForFilter=true, enableForList=true, ordinal=2)
	private ConfiguracaoBoleto configuracaoBoleto;
	
	@EnableAutoCrudField(label="Token de identificação da conta na api de boletos", enableForList=true, ordinal=3)
	private String token;
	
	@EnableAutoCrudField(label="Instruções para pagamento do boleto", enableForList=false, ordinal=4)
	private String instrucoes;
	
	@Column(name="original_id")
	private String originalId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public PaymentApiConfiguration getBoletoApiConfiguration() {
		return configuracaoBoleto;
	}

	@Override
	public void setBoletoApiConfiguration(PaymentApiConfiguration configuracaoBoleto) {
		this.configuracaoBoleto = (ConfiguracaoBoleto) configuracaoBoleto;
	}
	
	public void setConfiguracaoBoleto(ConfiguracaoBoleto configuracaoBoleto) {
		this.configuracaoBoleto = configuracaoBoleto;
	}

	public ContaRecebimento getContaBancaria() {
		return contaBancaria;
	}

	public void setContaBancaria(ContaRecebimento contaBancaria) {
		this.contaBancaria = contaBancaria;
	}

	public String getOriginalId() {
		return originalId;
	}
	

	@Override
	public String getToken() {
		return token;
	}

	@Override
	public void setToken(String token) {
		this.token = token;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}
	

	@Override
	public String getInstrucoes() {
		return instrucoes;
	}

	@Override
	public void setInstrucoes(String instrucoes) {
		this.instrucoes = instrucoes;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}

}
