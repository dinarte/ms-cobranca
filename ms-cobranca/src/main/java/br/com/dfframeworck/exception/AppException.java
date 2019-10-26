package br.com.dfframeworck.exception;

/**
 * Exception mãe de todas as exceptions esperadas disparadas pela aplicão.
 * @author dinar_000
 *
 */
public abstract class AppException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public AppException(String msg) {
		super(msg);
	}

	/**
	 * Este método devolve o tipo da exceptions filha que foi disparada, por conveção
	 * o tipo será o prefixo do nome da classe em caixa baixa, por exemplo, para uma exception 
	 * chamada ErroException este método retornorá "erro" como o tipo.
	 * @return
	 */
	public  String getType() {
		return this.getClass().getSimpleName().replace("Exception", "").toLowerCase();
	}
}
