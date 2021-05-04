package util;

public class ComentarioException extends Exception {
	
	public ComentarioException(){
		super("Erro! Comentário aberto e não fechado.");
	}
	
	public ComentarioException(String mensagem){
		super(mensagem);
	}
}
