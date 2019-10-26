package br.com.eflux.comum.domain;

public enum EstadoCivilEnum {

	SOLTEIRO(1, "Solteiro"), CASADO(2, "Casado"), SEPARADO(3, "Separado"), DIVORCIADO(4, "Divorciado"), VIÚVO(5,
			"Viúvo"), OUTROS(6, "Outros");

	private int id;
	private String descricao;

	EstadoCivilEnum(int id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
