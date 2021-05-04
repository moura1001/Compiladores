package util;

public class IdentificadorException extends Exception {
	
	public IdentificadorException(){
		super("Erro! Identificador precisa começar com uma letra.");
	}
	
	public IdentificadorException(String mensagem){
		super(mensagem);
	}
}
