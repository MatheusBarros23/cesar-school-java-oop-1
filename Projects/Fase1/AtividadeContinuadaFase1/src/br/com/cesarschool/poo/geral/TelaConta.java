package br.com.cesarschool.poo.geral;

import java.time.LocalDate;
import java.util.Scanner;

public class TelaConta {
	private RepositorioConta repositorio = new RepositorioConta();
	private static final Scanner SC = new Scanner(System.in);
	
	public void executarTela() {
		while (true) {
			imprimirOpcoes();
			int entrada = SC.nextInt();
			escolherEntrada(entrada);
		}
	}
	
	public void escolherEntrada(int entrada) {
		switch(entrada) {
			case 1:
				incluir();
				break;
			case 2:
				alterar();
				break;
			case 3:
				alterarStatus(Status.ENCERRADA);
				break;
			case 4:
				alterarStatus(Status.BLOQUEADA);
				break;
			case 5:
				alterarStatus(Status.ATIVA);
				break;
			case 6:
				excluir();
				break;
			case 7:
				buscar();
				break;
			case 8:
				operacao(Operacoes.CREDITAR);
				break;
			case 9:
				operacao(Operacoes.DEBITAR);
				break;
			case 10:
				System.out.println("Saindo...");
				System.exit(0);
				break;
			default:
				System.out.println("Opcao invalida");
				break;
		}
	}
	
	private void imprimirOpcoes() {		
		System.out.println("1- Incluir");
		System.out.println("2- Alterar");
		System.out.println("3- Encerrar");
		System.out.println("4- Bloquear");
		System.out.println("5- Ativar");
		System.out.println("6- Excluir");
		System.out.println("7- Buscar");
		System.out.println("8- Creditar");
		System.out.println("9- Debitar");
		System.out.println("10- Sair");
		System.out.print("Digite a opcao: ");
	}	
	
	private void incluir() {
		Conta novaConta = obterConta();
		int resultado = repositorio.incluir(novaConta);
		if (resultado == 0) {
			System.out.println("Conta adicionada com sucesso");
		}
	}
	
	private void alterar() {
		Conta conta = buscar();
		if (conta != null) {
			int resultado = repositorio.alterar(conta.getNumero(), LocalDate.now());
			if (resultado == 0) {
				System.out.println("Conta alterada com sucesso");
			}
		}
	}
	
	private void excluir() {
		long numero = pegarNumero();
		if (numero > 0) {
			int resultado = repositorio.excluir(numero);
			if (resultado == 0) {
				System.out.println("Conta excluída com sucesso");
			}
		}
	}
	
	private Conta buscar() {
		long numero = pegarNumero();
		Conta aux = repositorio.retornarConta(numero);
		if (aux == null) {
			return null;
		}
		imprimirConta(aux);
		return aux;
	}
	
	private void operacao(Operacoes operacao) {
		long numero = pegarNumero();
		Conta aux = repositorio.retornarConta(numero);
		if (aux == null) {
			return;
		}
		double valor = pegarValor();
		if (operacao == Operacoes.CREDITAR) {
			creditar(aux, valor);
		} else if (operacao == Operacoes.DEBITAR) {
			debitar(aux, valor);
		} else {
			System.out.println("Operacao desconhecida");
		}
	}
	
	private void creditar(Conta conta, double valor) {
		if (conta.creditar(valor) == 0) {
			System.out.println("Novo saldo: " + conta.getSaldo());
		}
	}
	
	private void debitar(Conta conta, double valor) {
		if (conta.debitar(valor) == 0) {
			System.out.println("Novo saldo: " + conta.getSaldo());
		}
	}
	
	private void alterarStatus(Status status) {
		long numero = pegarNumero();
		Conta aux = repositorio.retornarConta(numero);
		if (aux == null) {
			return;
		}
		if (status == Status.ENCERRADA) {
			encerrar(aux);
		} else if (status == Status.BLOQUEADA) {
			bloquear(aux);
		} else if (status == Status.ATIVA) {
			desbloquear(aux);
		} else {
			System.out.println("Status desconhecido");
		}
	}
	
	private void encerrar(Conta conta) {
		if (conta.getStatus() != Status.ENCERRADA) {
			Status anterior = conta.getStatus();
			conta.setStatus(Status.ENCERRADA);
			printarMudanca(anterior, conta);
		}
	}
	
	private void bloquear(Conta conta) {
		if (conta.getStatus() == Status.BLOQUEADA) {
			System.out.println("A conta ja esta bloqueada");
		}
		else if (conta.getStatus() == Status.ENCERRADA) {
			System.out.println("A conta foi encerrada, abortando alteracao...");
		} else {
			Status anterior = conta.getStatus();
			conta.setStatus(Status.BLOQUEADA);
			printarMudanca(anterior, conta);
		}
	}
	
	private void desbloquear(Conta conta) {
		if (conta.getStatus() == Status.ATIVA) {
			System.out.println("A conta ja esta ativa");
		}
		else if (conta.getStatus() == Status.ENCERRADA) {
			System.out.println("A conta foi encerrada, abortando alteracao...");
		} else {
			Status anterior = conta.getStatus();
			conta.setStatus(Status.ATIVA);
			printarMudanca(anterior, conta);
		}
	}
	
	private void printarMudanca(Status anterior, Conta conta) {
		System.out.println("O status da conta " + conta.getNumero() + " foi alterado");
		System.out.println("Anterior: " + anterior);
		System.out.println("Novo: " + conta.getStatus());
	}
	
	private Conta obterConta() {
		int numero = repositorio.getTamanho() + 1;
		LocalDate data = LocalDate.now();
		return new Conta(numero, Status.ATIVA, data);
	}
	
	private void imprimirConta(Conta conta) {
		System.out.println("Numero: " + conta.getNumero());
		System.out.println("Status: " + conta.calcularEscore());
		System.out.println("Data de abertura: "+ conta.getDataAbertura());
		System.out.printf("Saldo: R$%.2f\n", conta.getSaldo());
	}
	
	private long pegarNumero() {
		long numero = -1L;
		do {
			System.out.print("Informe o numero da conta: ");
			numero = SC.nextLong();
			if (numero <= 0) {
				System.out.println("Informe um numero valido");
			}
			else if (numero == -1) {
				System.out.println("Abortando operacao...");
				return -1L;
			}
		} while (numero <= 0);
		return numero;
	}
	
	private double pegarValor() {
		double valor = -1.0;
		do {
			System.out.print("Informe o valor: ");
			valor = SC.nextDouble();
			if (valor <= 0) {
				System.out.println("Informe um valor valido");
			}
			else if (valor == -1) {
				System.out.println("Abortando operacao...");
				return -1.0;
			}
		} while (valor <= 0);
		return valor;
	}
}
