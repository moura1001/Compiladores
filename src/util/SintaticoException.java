package util;

public class SintaticoException extends Exception {
	
	public SintaticoException(){
		super("Erro! Sintaxe inválida.");
	}
	
	public SintaticoException(String mensagem){
		super(mensagem);
	}
}
