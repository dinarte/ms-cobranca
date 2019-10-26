package br.com.dfframeworck.converters;

public interface IConverter<T> {
	
	public T parse(String value, Class<?> type);

}
