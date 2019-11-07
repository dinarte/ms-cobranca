package br.com.eflux.config.financeiro.domain;

import java.util.Objects;

import javax.persistence.Cacheable;
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
import br.com.eflux.payments.api.PaymentApiConfiguration;

/**
 * Contem os campos necessários para configurar a api de geração de boletos.
 * @author dinarte
 *
 */
@Entity
@Table(name = "configuracao_boleto", schema = "config")
@Cacheable
@AutoCrud(name="Configurar Boletos", description="Configurar api de geração de boletos", 
funtionality=@Functionality(isPublic=false, name="Configurar Boleto", menu="root->Configurações->Boletos API->configurar"))
public class ConfiguracaoBoleto implements Persistable<Long>, Migrable<Long>, PaymentApiConfiguration {
	
	@Id
	@GeneratedValue(generator = "configBoletoGenerator")
	@GenericGenerator(name = "configBoletoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "config.configuracao_boleto_seq") })
	@Column(name = "id_configuracao_boleto")
	private Long id;
	
	/**
	 * Identifica o nome da configuração de geração de boleto, por exemplo com o nome de uma cnta, exemplo:
	 * Conta corrente Bradesco 1234-5
	 */
	@EnableAutoCrudField(label="Descrição", enableForFilter=true, enableForList=true, ordinal=1)
	@Column(name = "nome")
	private String nome;
	
	/**
	 * Uri base da api da geração de boletos
	 */
	@EnableAutoCrudField(label="URI da API", enableForList=true, ordinal=2)
	@Column(name = "URI")
	private String uri;
	
	/**
	 * Usuário utilizado para atutenticação na api de boletos
	 */
	@EnableAutoCrudField(label="Usuário / Token", enableForList=true, ordinal=3)
	@Column(name = "user_api")
	private String userApi;
	
	/**
	 * Senha utilizada na api de boletos
	 */
	@EnableAutoCrudField(label="Senha", ordinal=4)
	@Column(name = "pass")
	private String pass;
	
	@EnableAutoCrudField(label="Api utilizada", ordinal=5, ui="selectApiBoletoMenu")
	@Column(name = "api_implementation")
	private String apiImplementation;
	
	@Column(name="original_oi")
	private String originalId;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOriginalId() {
		return originalId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public String getApiImplementation() {
		return apiImplementation;
	}

	@Override
	public void setApiImplementation(String apiImplementation) {
		this.apiImplementation = apiImplementation;
	}

	@Override
	public String getUserApi() {
		return userApi;
	}

	@Override
	public void setUserApi(String userApi) {
		this.userApi = userApi;
	}

	@Override
	public String getPass() {
		return pass;
	}

	@Override
	public void setPass(String pass) {
		this.pass = pass;
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
		return nome;
	}

}
