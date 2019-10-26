package br.com.eflux.comum.domain;

public enum TipoPessoaEnum {

	F("Pessoa Física"), J("Pessoa Jurídica");

	private final String nome;

	TipoPessoaEnum(final String nome) {
		this.nome = nome;
	}

	public String getValue() {
		return nome;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public String toString() {
		return nome;
	}

}
