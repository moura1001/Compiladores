package util;

public class SemanticoException extends Exception {
	
	public SemanticoException(){
		super("Erro! Semântica inválida.");
	}
	
	public SemanticoException(String mensagem){
		super(mensagem);
	}
}
