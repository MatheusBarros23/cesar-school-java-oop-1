package br.com.cesarschool.poo.geral;

public enum Status {
	ATIVA (1, "Ativa"), 
	ENCERRADA (2, "Encerrada"), 
	BLOQUEADA (3, "Bloqueada");
	
	private int indice;
	private String classificacao;

	Status(int indice, String classificacao) {
		this.indice = indice;
		this.classificacao = classificacao;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public int getIndice() {
		return indice;
	}

	public String getClassificacao() {
		return classificacao;
	}
	
	
}
