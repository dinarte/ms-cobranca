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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.repository.Migrable;
import br.com.dfframeworck.security.Functionality;
import br.com.eflux.empreendimento.domain.Empreendimento;
import br.com.eflux.empreendimento.domain.Incorporadora;
import br.com.eflux.empreendimento.domain.Unidade;
import br.com.eflux.financeiro.domain.TipoLancamento;

/**
 * Armazena as configurações necessárias para se criar um acoirdo no sistema.
 * @author dinarte
 *
 */
@Entity
@Table(name = "configuracao_acordo", schema = "config")
@Cacheable
@AutoCrud(name="Configurar Acordo", description="Configurar Acordos", 
funtionality=@Functionality(isPublic=false, name="Configurar Acordos", menu="root->Configurações->Acordos"))
public class ConfiguracaoAcordo implements Persistable<Long>, Migrable<Long> {

	
	@Id
	@GeneratedValue(generator = "configAcordoGenerator")
	@GenericGenerator(name = "configAcordoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "config.configuracao_acordo_seq") })
	@Column(name = "id_configuracao_acordo")
	private Long id;
	
	/**
	 * Descreve de forma suscinta a configuração do acordo pae afins de identificação
	 */
	@EnableAutoCrudField(label="Descrição", enableForList=true, ordinal=0)
	@NotNull
	@Size(min=5, max=80)
	private String descricao;
	
	/**
	 * Caso informado a configuração será válida para todos os contratos da incorporadora informada, caso contrário
	 * a configuração será valida para todos os contratos do sistema
	 */
	@ManyToOne
	@JoinColumn(name="id_incorporadora", nullable=true)
	@EnableAutoCrudField(label="Incorporadora", enableForList=true, lookUpFieldName="nome", ordinal=1)
	private Incorporadora incorporadora;
	
	/**
	 * Caso informado a configuração será válida apenas para os contratos relativos ao empreendimento informado, caso contrário
	 * a configuração será válida apenas para a incorporadora informada. 
	 */
	@ManyToOne
	@JoinColumn(name="id_empreendimento", nullable=true)
	@EnableAutoCrudField(label="Empreendimento", enableForList=true, lookUpFieldName="nome", ordinal=2)
	private Empreendimento empreendimento;
	
	/**
	 * Caso informada a configuração será válida apenas para o contrato relativo a unidade informada, caso contrário
	 * a configuração será válida apenas para o empreendimento informado. 
	 */
	@ManyToOne
	@JoinColumn(name="id_unidade", nullable=true)
	@EnableAutoCrudField(label="Unidade", enableForList=true, lookUpFieldName="nome", ordinal=3)
	private Unidade unidade;
	
	/**
	 * Determina qual o tipo de lançamento que deverá ser criado os débitos gerados de um acordo.
	 */
	@ManyToOne
	@JoinColumn(name="id_tipo_lancamento", nullable=true)
	@EnableAutoCrudField(label="Tipo Lançamento Padrão", lookUpFieldName="nome", ordinal=4)
	@NotNull
	private TipoLancamento tipoLancamento;
	
	/**
	 * Determina qual o tipo de lançamento que deverá ser criado o débito degardo para uma entrada de um acordo.
	 */
	@ManyToOne
	@JoinColumn(name="id_tipo_lancamento_entrada", nullable=true)
	@EnableAutoCrudField(label="Tipo Lançamento Entrada", lookUpFieldName="nome", ordinal=5)
	@NotNull
	private TipoLancamento tipoLancamentoEntrada;
	
	/**
	 * Determina a quantidade de dias que o vencimento da entrada será gerado a partir da data de geração do acordo.
	 */
	@Column(name="quantidade_dias_vencimento_entrada")
	@EnableAutoCrudField(label="Dias Vencimento Entrada", ordinal=6)
	@NotNull
	private Integer quantidadeDiasVencimentoEntrada = 5;
	
	/**
	 * Determina a porcentagem máxima do desconto concedido em cima do valor ataualizado.
	 */
	@Column(name="porcentagem_maxima_desconto")
	@EnableAutoCrudField(label="Desconto Máximo (%)", ordinal=7)
	@NotNull
	private Double porcentagemMaximaDesconto = 10.0; 
	
	/**
	 * Total de parcelas vencidas que dispara o cancelamento automático do acordo.
	 */
	@Column(name="quantidade_parcelas_vencidas_cancelamento")
	@EnableAutoCrudField(label="Parcelas Vencidas Cancelamento", ordinal=8)
	@NotNull
	private Integer quantidadeParcelasVencidasCancelamento = 2;
	
	
	@Column(name="titulo_documento")
	@EnableAutoCrudField(label="Título do Documento", description="Título do Documento gerado pelo acordo, exe: Termo de Acordo", ordinal=9)
	@NotNull
	@Size(min=5, max=80)
	private String tituloDocumento;
	
	@Column(name="texto_documento")
	@EnableAutoCrudField(label="Texto do Documento", description="Título do Documento gerado pelo acordo, exe: Termo de Acordo", ordinal=10, ui="inputTextArea")
	@Type(type="text")
	private String textoDocumento;
	
	@Column(name="original_id")
	private String originalId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Incorporadora getIncorporadora() {
		return incorporadora;
	}

	public void setIncorporadora(Incorporadora incorporadora) {
		this.incorporadora = incorporadora;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}


	public Integer getQuantidadeDiasVencimentoEntrada() {
		return quantidadeDiasVencimentoEntrada;
	}

	public void setQuantidadeDiasVencimentoEntrada(Integer quantidadeDiasVencimentoEntrada) {
		this.quantidadeDiasVencimentoEntrada = quantidadeDiasVencimentoEntrada;
	}

	public Double getPorcentagemMaximaDesconto() {
		return porcentagemMaximaDesconto;
	}

	public void setPorcentagemMaximaDesconto(Double porcentagemMaximaDesconto) {
		this.porcentagemMaximaDesconto = porcentagemMaximaDesconto;
	}

	public Integer getQuantidadeParcelasVencidasCancelamento() {
		return quantidadeParcelasVencidasCancelamento;
	}

	public void setQuantidadeParcelasVencidasCancelamento(Integer quantidadeParcelasVencidasCancelamento) {
		this.quantidadeParcelasVencidasCancelamento = quantidadeParcelasVencidasCancelamento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoLancamento getTipoLancamentoEntrada() {
		return tipoLancamentoEntrada;
	}

	public void setTipoLancamentoEntrada(TipoLancamento tipoLancamentoEntrada) {
		this.tipoLancamentoEntrada = tipoLancamentoEntrada;
	}
	
	

	public String getTituloDocumento() {
		return tituloDocumento;
	}

	public void setTituloDocumento(String tituloDocumento) {
		this.tituloDocumento = tituloDocumento;
	}

	public String getTextoDocumento() {
		return textoDocumento;
	}

	public void setTextoDocumento(String textoDocumento) {
		this.textoDocumento = textoDocumento;
	}

	@Override
	public void setOriginalId(String id) {
		this.originalId = id;
	}

	@Override
	public String getOriginalId() {
		return originalId;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}
	
	
	
}
