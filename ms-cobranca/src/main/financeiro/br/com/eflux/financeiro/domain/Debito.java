package br.com.eflux.financeiro.domain;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.springframework.data.domain.Persistable;
import org.springframework.data.util.Optionals;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.repository.Migrable;
import br.com.dfframeworck.security.Functionality;
import br.com.eflux.comum.domain.Usuario;
import br.com.eflux.financeiro.helper.SituacaoDebitoHelper;

/**
 * Débitos gerados a partir de um contrato que foi firmado, 
 * nem todo contrato gera débidos, o que vai determinar isso é a natureza e a situacao do contrato.
 * @author dinarte
 *
 */
@Entity
@Table(schema="financeiro", name="debito")
@AutoCrud(	name="Débitos", 
			description="Débitos do Contrato", 
			orderBy="dataVencimento asc", 
			operations= {"list", "update"},
			funtionality=@Functionality(	isPublic=false, 
											name="Débitos do Contrato", 
											menu="root->Financeiro->debito", 
											icon="fa fa-hand-holding-usd"))

public class Debito implements Persistable<Long>, Migrable<Long> {
	
	@Id
	@GeneratedValue(generator = "lancamentoGenerator")
	@GenericGenerator(name = "lancamentoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "empreendimento.lancamento_seq") })
	@Column(name = "id_debito", unique = true, nullable = false, insertable = true, updatable = true)
	//@EnableAutoCrudField(label="id", enableForFilter=true, enableForList=true, ordinal=0, readOnlyForUpdate=true)
	private Long id;
	
	@EnableAutoCrudField(label="Número", enableForFilter=true, enableForList=true, ordinal=1, readOnlyForUpdate=true, formater="custom/numeroParcelaFormater")
	private Integer numero;
	
	@EnableAutoCrudField(label="Contrato", enableForFilter=true, enableForList=true, ordinal=2, 
			lookUpFieldName="numeroContrato", readOnlyForUpdate=true, ui="autoComplete", formater="custom/contratoFormater")
	@ManyToOne
	@JoinColumn(name="id_contrato")
	private Contrato contrato;

	@EnableAutoCrudField(label="Tipo", enableForFilter=false, enableForList=false, ordinal=3, lookUpFieldName="nome", readOnlyForUpdate=true)
	@ManyToOne
	@JoinColumn(name="id_tipo_lancamento")
	private TipoLancamento tipoLancamento;
	
	@EnableAutoCrudField(label="Valor Original", enableForList=true, ordinal=4, readOnlyForUpdate=true, formater="currencyFormater")
	private BigDecimal valorOriginal;
	
	@EnableAutoCrudField(label="Correção", enableForList=true, ordinal=5, readOnlyForUpdate=true, formater="currencyFormater")
	private BigDecimal correcao;

	@EnableAutoCrudField(label="Remuneração", enableForList=true, ordinal=6, readOnlyForUpdate=true, formater="currencyFormater")
	private BigDecimal jurosRemuneratorio;
	
	@EnableAutoCrudField(label="Multa", enableForList=true, ordinal=7, readOnlyForUpdate=true, formater="currencyFormater")
	private BigDecimal multaAtrazo;

	@EnableAutoCrudField(label="Mora", enableForList=true, ordinal=8, readOnlyForUpdate=true, formater="currencyFormater")
	private BigDecimal jurosAtrazo;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@EnableAutoCrudField(label="Data", enableForList=false, ordinal=9, readOnlyForUpdate=true)
	private Date dataLancamento;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@EnableAutoCrudField(label="Vencimento", enableForList=true, ordinal=10, readOnlyForUpdate=true)
	private Date dataVencimento;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@EnableAutoCrudField(label="Data Ultimo Pagamento", enableForList=true, ordinal=11, readOnlyForUpdate=true)
	private Date dataUltimoPagamento;
	
	@EnableAutoCrudField(label="Valor Pago", ordinal=12, readOnlyForUpdate=true)
	private  BigDecimal valorPago;
	
	@EnableAutoCrudField(label="Situação", enableForFilter=true, enableForList=true, ordinal=13, readOnlyForUpdate=true, formater="custom/situacaoDebitoFormater")
	@Enumerated(EnumType.STRING)
	@Column(name="situacao", nullable=true)
	private SituacaoDebitoEnum situacao;
	
	@EnableAutoCrudField(label="Observações", ordinal=14, readOnlyForUpdate=true)
	private String descricao;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="data_criacao_invoice")
	private Date dataCriacaoInvoice;
	
	@EnableAutoCrudField(label="Situaçao Invoice", ordinal=15, enableForFilter=true, readOnlyForUpdate=true, enableForCreate=false)
	@Column(name="status_criacao_invoice")
	@Enumerated(EnumType.STRING)
	private StatusGeracaoInvoiceEnum statusCriacaoInvoice;
	
	@Column(name="erroCriacaoInvoice")
	@Type(type="text")
	private String erroCriacaoInvoice;
	
	
	@ManyToOne
	@JoinColumn(name="id_usuario_baixa_manual")
	private Usuario usuarioBaixaManual;
	
	@Column
	private String motivoBaixaManual;
	
	
	
	@EnableAutoCrudField(label="Boleto / Invoice", enableForCreate=false, enableForUpdate=false, enableForList=true, ordinal=15, lookUpFieldName="status")
	@ManyToOne
	private Boleto invoice;
	
	/**
	 * Conta de recebimento onde os pagamentos serão efetuados. 
	 */
	@ManyToOne
	@JoinColumn(name="id_conta_recebimento")
	@EnableAutoCrudField(label="Conta Padrão", enableForFilter=true, ordinal=15, lookUpFieldName="descricao")
	private ContaRecebimento contaRecebimento;
	
	@Column(name="originalId")
	private String originalId;
	
	public Debito() {
		super();
	}
	
	public Debito(Contrato contrato, Integer numero,  BigDecimal valorOriginal, Date dataVencimento, TipoLancamento tipoLancamento) {
		this.contrato = contrato;
		this.numero = numero;
		this.valorOriginal = valorOriginal;
		this.dataVencimento = dataVencimento;
		this.tipoLancamento = tipoLancamento;
		this.situacao = SituacaoDebitoEnum.EM_ABERTO;
		this.dataLancamento = new Date();
	}
	
	
	
	@Transient
	public BigDecimal getValorAtualizado(){
		return Optional.ofNullable( getValorOriginal()
										.add(getJurosAtrazo())
										.add(getMultaAtrazo())
										.add(getJurosRemuneratorio())
										.add(getCorrecao()) )
				.orElseGet(()->new BigDecimal(0));
	}
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}



	public BigDecimal getValorOriginal() {
		BigDecimal ret =  Optional
				.ofNullable(valorOriginal)
				.orElseGet(()->new BigDecimal(0));
		return ret;
	}

	public void setValorOriginal(BigDecimal valorOriginal) {
		this.valorOriginal = valorOriginal;
	}

	public BigDecimal getJurosAtrazo() {
		
		if (SituacaoDebitoHelper.getPassiveisDeJurosEMulta().contains(situacao)) {
			if (Objects.nonNull(dataVencimento)) {
				
				long diasVencimento = getDiasVencida();
				
				if (diasVencimento < 0) {
					
					jurosAtrazo = (getValorOriginal()
							.subtract(getValorPago())
							.add(getJurosRemuneratorio())
							.add(getCorrecao())
							.multiply(new BigDecimal(contrato.getPorcentagemJuros()))
							.divide(new BigDecimal(100)))
								.multiply(new BigDecimal(-diasVencimento));
				} else {
					jurosAtrazo = new BigDecimal(0);
				}
				
				return jurosAtrazo;
			}
		}
		return  Objects.nonNull( jurosAtrazo ) ? jurosAtrazo : new BigDecimal(0);
	}

	private long getDiasVencida() {
		Date dataBase = Objects.nonNull(dataUltimoPagamento) ? dataUltimoPagamento : new Date();
		LocalDate dataBaseLocal = dataBase.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate vencimento = dataVencimento.toInstant()
		  .atZone(ZoneId.systemDefault())
		  .toLocalDate();
		long diasVencimento = ChronoUnit.DAYS.between(dataBaseLocal, vencimento);
		return diasVencimento;
	}

	public void setJurosAtrazo(BigDecimal jurosAtrazo) {
		this.jurosAtrazo = jurosAtrazo;
	}

	public BigDecimal getMultaAtrazo() {
		if (Objects.nonNull(dataVencimento)) {
			if (SituacaoDebitoHelper.getPassiveisDeJurosEMulta().contains(situacao)) {
				
				if (getDiasVencida()<0)
				
					multaAtrazo =  (getValorOriginal()
										.subtract(getValorPago())
										.add(getJurosRemuneratorio())
										.add(getCorrecao())
										.multiply(new BigDecimal(contrato.getPorcentagemMulta()))
										.divide(new BigDecimal(100)));
				else
					multaAtrazo = new BigDecimal(0);
			}	
		}
		return Objects.nonNull( multaAtrazo ) ? multaAtrazo : new BigDecimal(0);
	}
	
	public static void main(String[] args) {
		
		Contrato c = new Contrato();
		c.setPorcentagemJuros(1.0);
		Debito d = new Debito();
		d.setContrato(c);
		d.setValorOriginal(new BigDecimal(100.0));
		
	//	d.setCorrecao(new BigDecimal(30));
	//	d.setJurosRemuneratorio(new BigDecimal(70));
		
		Calendar hoje = new GregorianCalendar();
		hoje.set(2019, 10, 22);
		
		d.setDataVencimento(hoje.getTime());
		
		System.out.println(d.getDataVencimentoFormatada("dd-MM-yyyy") + ": " + d.getJurosAtrazo());
		
		
	}
	

	public void setMultaAtrazo(BigDecimal multaAtrazo) {
		this.multaAtrazo = multaAtrazo;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}
	
	public String getDataLancamentoFormatada(String padrao) {
		SimpleDateFormat formato = new SimpleDateFormat(padrao);
		return formato.format(dataLancamento);
	}
	
	public String getDataVencimentoFormatada(String padrao) {
		SimpleDateFormat formato = new SimpleDateFormat(padrao);
		return formato.format(dataVencimento);
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataUltimoPagamento() {
		return dataUltimoPagamento;
	}

	public void setDataUltimoPagamento(Date dataUltimoPagamento) {
		this.dataUltimoPagamento = dataUltimoPagamento;
	}

	public BigDecimal getCorrecao() {
		return Optional.ofNullable(correcao).orElseGet(()->new BigDecimal(0));
	}

	public void setCorrecao(BigDecimal correcao) {
		this.correcao = correcao;
	}

	public BigDecimal getValorPago() {
		return Optional.ofNullable( valorPago ).orElseGet(()->new BigDecimal(0));
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public SituacaoDebitoEnum getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoDebitoEnum situacao) {
		this.situacao = situacao;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public BigDecimal getJurosRemuneratorio() {
		return Optional.ofNullable( jurosRemuneratorio ).orElseGet(() -> new BigDecimal(0));
	}

	public void setJurosRemuneratorio(BigDecimal jurosRemuneratorio) {
		this.jurosRemuneratorio = jurosRemuneratorio;
	}

	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}

	public ContaRecebimento getContaRecebimento() {
		return contaRecebimento;
	}

	public void setContaRecebimento(ContaRecebimento contaRecebimento) {
		this.contaRecebimento = contaRecebimento;
	}

	public Date getDataCriacaoInvoice() {
		return dataCriacaoInvoice;
	}

	public void setDataCriacaoInvoice(Date dataCriacaoInvoice) {
		this.dataCriacaoInvoice = dataCriacaoInvoice;
	}


	@JsonIgnore
	@SuppressWarnings("unchecked")
	public Map<String, Object> getErroCriacaoInvoiceMap() {
		
		ObjectMapper mapper = new ObjectMapper();
        String json = erroCriacaoInvoice;

        Map<String, Object> map;
		try {
			map = mapper.readValue(json, Map.class);
			return map;
		} catch (IOException e) {
			return null;
		}
	}
	

	public String getErroCriacaoInvoice() {
		return erroCriacaoInvoice;
	}
	


	public void setErroCriacaoInvoice(String erroCriacaoInvoice) {
		this.erroCriacaoInvoice = erroCriacaoInvoice;
	}

	public StatusGeracaoInvoiceEnum getStatusCriacaoInvoice() {
		return statusCriacaoInvoice;
	}

	public void setStatusCriacaoInvoice(StatusGeracaoInvoiceEnum statusCriacaoInvoice) {
		this.statusCriacaoInvoice = statusCriacaoInvoice;
	}

	public Boleto getInvoice() {
		return invoice;
	}

	public void setInvoice(Boleto invoice) {
		this.invoice = invoice;
	}

	public Usuario getUsuarioBaixaManual() {
		return usuarioBaixaManual;
	}

	public void setUsuarioBaixaManual(Usuario usuarioBaixaManual) {
		this.usuarioBaixaManual = usuarioBaixaManual;
	}

	public String getMotivoBaixaManual() {
		return motivoBaixaManual;
	}

	public void setMotivoBaixaManual(String motivoBaixaManual) {
		this.motivoBaixaManual = motivoBaixaManual;
	}	
	
}
