package br.com.eflux.comum.domain;

public enum SexoEnum {

	MASCULINO("M", "Masculino"), FEMININO("F", "Feminino");

	private final String codigo;
	private final String nome;

	SexoEnum(final String codigo, final String nome) {
		this.codigo = codigo;
		this.nome = nome;
	}

	public String getValue() {
		return codigo;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public String toString() {
		return nome;
	}
}
