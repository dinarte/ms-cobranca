package br.com.dfframeworck.exception;

/**
 * QUando  disparada a aplicacação adionará uma msg tradata no sistema.
 * @author dinar_000
 *
 */
public class LoginException extends AppException{

	private static final long serialVersionUID = 1L;

	public LoginException(String msg) {
		super(msg);
		
	}
}
