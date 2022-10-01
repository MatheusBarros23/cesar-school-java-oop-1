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
	
	public void imprimirOpcoes() {		
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
	
	public void incluir() {
		Conta novaConta = pegarNovaConta();
		repositorio.incluir(novaConta);
	}
	
	public void alterar() {
		Conta conta = buscar();
		if (conta != null) {
			int resultado = repositorio.alterar(conta.getNumero(), LocalDate.now());
			if (resultado == 0) {
				System.out.println("Conta alterada com sucesso");
			}
		}
	}
	
	public void excluir() {
		long numero = pegarNumero();
		if (numero > 0) {
			int resultado = repositorio.excluir(numero);
			if (resultado == 0) {
				System.out.println("Conta excluída com sucesso");
			}
		}
	}
	
	public Conta buscar() {
		long numero = pegarNumero();
		Conta aux = repositorio.retornarConta(numero);
		if (aux == null) {
			return null;
		}
		imprimirConta(aux);
		return aux;
	}
	
	public void operacao(Operacoes operacao) {
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
	
	public void creditar(Conta conta, double valor) {
		if (conta.creditar(valor) == 0) {
			System.out.println("Novo saldo: " + conta.getSaldo());
		}
	}
	
	public void debitar(Conta conta, double valor) {
		if (conta.debitar(valor) == 0) {
			System.out.println("Novo saldo: " + conta.getSaldo());
		}
	}
	
	public void alterarStatus(Status status) {
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
	
	public void encerrar(Conta conta) {
		if (conta.getStatus() != Status.ENCERRADA) {
			Status anterior = conta.getStatus();
			conta.setStatus(Status.ENCERRADA);
			printarMudanca(anterior, conta);
		}
	}
	
	public void bloquear(Conta conta) {
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
	
	public void desbloquear(Conta conta) {
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
	
	public void printarMudanca(Status anterior, Conta conta) {
		System.out.println("O status da conta " + conta.getNumero() + " foi alterado");
		System.out.println("Anterior: " + anterior);
		System.out.println("Novo: " + conta.getStatus());
	}
	
	public Conta pegarNovaConta() {
		System.out.print("Informe numero da conta: ");
		int posicao = SC.nextInt();
		Status statusSelecionado = pegarStatus();
		LocalDate data = pegarData();
		return new Conta(posicao, statusSelecionado, data);
	}
	
	public Status pegarStatus() {
		int numeroStatus = -1;
		Status status = null;
		do {
			System.out.print("Informe o status: ");
			numeroStatus = SC.nextInt();
			switch (numeroStatus) {
				case 1:
					status = Status.ATIVA;
					break;
				case 2: 
					status = Status.BLOQUEADA;
					break;
				case 3:
					status = Status.ENCERRADA;
				default:
					System.out.println("Status desconhecido");
					break;
			}
		} while (numeroStatus < 1 || numeroStatus > Status.values().length);
		return status;
	}
	
	
	public LocalDate pegarData() {
		LocalDate dataInput = null;
		LocalDate hoje = LocalDate.now();
		LocalDate dataLimite = hoje.minusMonths(1);
		int dia, mes, ano;
		boolean invalidDataInput = true;
		
		while(invalidDataInput) {
			System.out.print("Dia: ");
			dia = SC.nextInt();
			System.out.print("Mes: ");
			mes = SC.nextInt();
			System.out.print("Ano: ");
			ano = SC.nextInt();
			dataInput = LocalDate.of(ano, mes, dia);
			
			if (dataInput.equals(hoje)) {
				invalidDataInput = false;
				System.out.println("Validado!");
			} else if (dataInput.isAfter(dataLimite) && dataInput.isBefore(hoje)) {
				invalidDataInput = false;
				System.out.println("Validado!");
			} else {
				System.out.println("Invalido");
			}
		}
		return dataInput;
	}
	
	public void imprimirConta(Conta conta) {
		System.out.println("Numero: " + conta.getNumero());
		System.out.println("Status: " + conta.calcularEscore());
		System.out.println("Data de abertura: "+ conta.getDataAbertura());
		System.out.printf("Saldo: R$%.2f\n", conta.getSaldo());
	}
	
	public long pegarNumero() {
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
	
	public double pegarValor() {
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
