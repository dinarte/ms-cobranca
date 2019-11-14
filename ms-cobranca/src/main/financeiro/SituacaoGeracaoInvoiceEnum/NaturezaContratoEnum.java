package SituacaoGeracaoInvoiceEnum;

/**
 * Destina-se a caracterizar um contrato de acordo com sua natoreza.
 * @author dinarte
 *
 */
public enum NaturezaContratoEnum {
	
	VENDA("Venda", true),
	
	DOACAO("Doação", false),
	
	PERMUTA("Permuta", false);

	String descricao;
	
	boolean geraFinanceiro;
	
	private NaturezaContratoEnum(String descricao, boolean geraFinanceiro) {
		this.descricao = descricao;
		this.geraFinanceiro = geraFinanceiro;
	}
	
}
