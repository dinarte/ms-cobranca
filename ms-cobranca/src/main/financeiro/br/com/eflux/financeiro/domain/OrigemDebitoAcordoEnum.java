package br.com.eflux.financeiro.domain;

/**
 * Diferencia a origem do débito presente em um acordo, se as parcelas: 
 * 
 * @author dinarte
 *
 */
public enum OrigemDebitoAcordoEnum {

	/**
	 * Débitos que são a origem do acordo, as parcelas em aberto e/ou vencidas que foram acordadas.
	 */
	CONTRATO,
	
	/**
	 * Débitos que foram gerados a partir de um acordo, ou seja, os que representam as novas
	 * parcelas que foram criadas para quitação da dívida original 
	 */
	ACORDO;
	
}
