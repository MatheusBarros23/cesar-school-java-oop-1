package br.com.cesarschool.poo.geral;

import java.time.LocalDate;

public class RepositorioConta {
	private static final int SUCESSO = 0;
	private static final int ERRO_MAX_CONTAS = -1;
	private static final int CONTA_NAO_ACHADA = -1;
	private static final int ERRO_DESCONHECIDO = -2;
	
	
	private static final int MAX_CONTAS = 1024;
	private Conta[] cadastroConta = new Conta[MAX_CONTAS];
	private int tamanho = 0;
	
	public int getTamanho() {
		return tamanho;
	}

	public int incluir(Conta conta) {
		if (tamanho == MAX_CONTAS) {
			System.out.println("Tamanho máximo de contas atingido");
			return ERRO_MAX_CONTAS;
		}
		for (int i = 0; i < cadastroConta.length; i++) {
			if (cadastroConta[i] == null) {
				cadastroConta[i] = conta;
				tamanho++;
				return SUCESSO;
			}
		}
		System.out.println("Erro desconhecido");
		return ERRO_DESCONHECIDO;
	}
	
	public int buscar(long numero) {
		for (int i = 0; i < cadastroConta.length; i++) {
			if (cadastroConta[i] != null && cadastroConta[i].getNumero() == numero) {
				return i;
			}
		}
		System.out.println("A conta " + numero + " nao foi localizada");
		return CONTA_NAO_ACHADA;
	}
	
	public Conta retornarConta(long numero) {
		int indice = buscar(numero);
		if (indice < 0) {
			return null;
		}
		return cadastroConta[indice];
	}
	
	public int alterar(long numero, LocalDate novaDataAbertura) {
		int indice = buscar(numero);
		if (indice < 0) {
			return CONTA_NAO_ACHADA;
		}
		cadastroConta[indice].setDataAbertura(novaDataAbertura);
		return SUCESSO;
	}
	
	public int excluir(long numero) {
		int indice = buscar(numero);
		if (indice < 0) {
			return CONTA_NAO_ACHADA;
		}
		cadastroConta[indice] = null;
		tamanho--;
		return SUCESSO;
	}
	
	
}
