package util;

public class SimboloException extends Exception {

	public SimboloException(){
		super("Erro! S�mbolo n�o pertencente a linguagem.");
	}
	
	public SimboloException(String mensagem){
		super(mensagem);
	}
}
