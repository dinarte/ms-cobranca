package br.com.eflux.financeiro.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import br.com.eflux.comum.domain.Usuario;

public class BaixaDebitoDTO {
	
	@NotNull(message="Informar o Identificador do Débito é obrigatório.")
	private Long idDebito;
	
	@NumberFormat(style = Style.NUMBER, pattern = "#,###.###")
	@NotNull(message="Informar o valor pago  é obrigatório.")
	private  BigDecimal valorPago;
	
	@NotNull(message="Informar a data do pagamento é obrigatório.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataUltimoPagamento;
	
	private Usuario usuario;
	
	@NotNull(message="Informar o motivo é obrigatório.")
	@Size(min=5, max=200, message="O campo Motivo deve ter no mínimo 5 e no máximo 200 caracteres.")
	private String motivoBaixaManual;

	public Long getIdDebito() {
		return idDebito;
	}

	public void setIdDebito(Long idDebito) {
		this.idDebito = idDebito;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public Date getDataUltimoPagamento() {
		return dataUltimoPagamento;
	}

	public void setDataUltimoPagamento(Date dataUltimoPagamento) {
		this.dataUltimoPagamento = dataUltimoPagamento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getMotivoBaixaManual() {
		return motivoBaixaManual;
	}

	public void setMotivoBaixaManual(String motivoBaixaManual) {
		this.motivoBaixaManual = motivoBaixaManual;
	}
	
	
	
}
