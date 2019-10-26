package br.com.eflux.comum.domain;

public enum MesEnum {
	JANEIRO(0, "Janeiro"), FEVEREIRO(1, "Fevereiro"), MARCO(2, "Março"), ABRIL(3, "Abril"), MAIO(4, "Maio"), JUNHO(5,
			"Junho"), JULHO(6, "Julho"), AGOSTO(7, "Agosto"), SETEMBRO(8,
					"Setembro"), OUTUBRO(9, "Outubro"), NOVEMBRO(10, "Novembro"), DEZEMBRO(11, "Dezembro");

	private final int id;
	private final String nome;

	MesEnum(final int id, final String nome) {
		this.id = id;
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public static MesEnum getById(int id) {
		for (MesEnum m : values()) {
			if (m.id == id) {
				return m;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return nome;
	}

}
