package br.com.dfframeworck.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Disparada quando erros de validação de formulários acontecem.
 * @author Dinarte
 *
 */
public class ValidacaoException extends AppException {

	
	private static final long serialVersionUID = 1L;
	
	public List<String> msgs = new ArrayList<>();
	
	public ValidacaoException(String msg) {
		super(msg);
		msgs.add(msg);
	}

	public ValidacaoException(List<String> msgs) {
		super(msgs.toString());
		this.msgs = msgs;
	}
	
	
	
}
