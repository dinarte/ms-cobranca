package br.com.dfframeworck.util;

public class SerializationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SerializationException(String msg, Throwable e) {
		super(msg, e);
	}
}
