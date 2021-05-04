package util;

public class SimboloException extends Exception {

	public SimboloException(){
		super("Erro! Símbolo não pertencente a linguagem.");
	}
	
	public SimboloException(String mensagem){
		super(mensagem);
	}
}
