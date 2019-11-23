package br.com.eflux.financeiro.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.dfframeworck.repository.Migrable;
import br.com.eflux.config.financeiro.domain.ConfiguracaoAcordo;

/**
 * Quando uma o mais parcelas de um contrato deixam de ser pagas, um acordo pode ser feito entre o contratante e o contratado, 
 * um acordo consiste em agrupar débitos em aberto ou venvidas, acordar um novo valor e dividir novamente em novas parcelas. 
 * As parcelas originais que entram em um acordo, devem mudar seu status para NEGOCIADA, e as novas parcelas entraram no contrato 
 * e no acordo e terão o status EM_ABERTO e segiram o fluxo normal de pagamento, podendo assumir normalmente os status de 
 * VENCIDA ou QUITADA.
 * @author dinarte
 *
 */
@Entity
@Table(name = "acordo", schema = "financeiro")
public class Acordo implements Persistable<Long>, Migrable<Long> {

	@Id
	@GeneratedValue(generator = "acordoGenerator")
	@GenericGenerator(name = "acordoGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "financeiro.acordo_seq") })
	@Column(name = "id_acordo", unique = true, nullable = false)
	private Long id;

	/**
	 * Situação do acordo.
	 */
	@Enumerated(EnumType.STRING)
	@JoinColumn(name = "id_status_acordo", nullable = false)
	private StatusAcordoEnum statusAcordo = StatusAcordoEnum.ATIVO;


	/**
	 * Contrato ao qual o acordo se refere.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_contrato", nullable = false)
	private Contrato contrato;


	/**
	 * Débitos agrupados em um acordo
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "acordo")
	private Set<AcordoDebito> debitos;

	/**
	 * Novo valor a ser cobrado através do acordo.
	 */
	@Column(name = "valor_acordado")
	private BigDecimal valorAcordado;
	
	/**
	 * Configuração utilizada para gerar o acordo
	 */
	@ManyToOne
	@JoinColumn(name = "id_configuracao_acordo")
	private ConfiguracaoAcordo configuracao;

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_cancelamento")
	private Date dataCancelamento;

	@Column(name = "justificativa_cancelamento")
	private String justificativaCancelamento;
	
	@Column(name = "original_id")
	private String originalId;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StatusAcordoEnum getStatusAcordo() {
		return statusAcordo;
	}

	public void setStatusAcordo(StatusAcordoEnum statusAcordo) {
		this.statusAcordo = statusAcordo;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public Set<AcordoDebito> getDebitos() {
		return debitos;
	}

	public void setDebitos(Set<AcordoDebito> debitos) {
		this.debitos = debitos;
	}


	public BigDecimal getValorAcordado() {
		return valorAcordado;
	}

	public void setValorAcordado(BigDecimal valorAcordado) {
		this.valorAcordado = valorAcordado;
	}

	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public String getJustificativaCancelamento() {
		return justificativaCancelamento;
	}

	public void setJustificativaCancelamento(String justificativaCancelamento) {
		this.justificativaCancelamento = justificativaCancelamento;
	}

	public ConfiguracaoAcordo getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(ConfiguracaoAcordo configuracao) {
		this.configuracao = configuracao;
	}

	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}

	@Override
	@JsonIgnore
	public boolean isNew() {
		return Objects.isNull(id) || id.equals(0L);
	}

}