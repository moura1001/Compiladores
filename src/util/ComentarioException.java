package util;

public class ComentarioException extends Exception {
	
	public ComentarioException(){
		super("Erro! Coment�rio aberto e n�o fechado.");
	}
	
	public ComentarioException(String mensagem){
		super(mensagem);
	}
}
