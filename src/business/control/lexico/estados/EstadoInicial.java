package business.control.lexico.estados;

import business.control.lexico.Lexico;
import business.model.Token;

public class EstadoInicial extends Estado {
	
	protected String getClassificacao() {
		return "";
	}

	public Estado lerCaractereComentarioAbre(char caractere) {
		Lexico lexico = Lexico.obterInstancia();
		Token token = new Token("{", "Comentário", lexico.getContadorLinha());
		lexico.setTokenComentarioErro(token);
		return new EstadoComentario();
	}

	//public Estado lerCaractereComentarioFecha(char caractere) {
	//	return null;
	//}
	
	public Estado lerCaractereDescarte(char caractere){
		if(caractere == '\n')
			Lexico.obterInstancia().incrementarContadorLinha();
		Lexico.obterInstancia().setBufferEntrada("");
		return this;
	}
	
	public Estado lerCaractereAlfabetico(char caractere){
		Lexico.obterInstancia().setBufferEntrada(String.valueOf(caractere));
		return new EstadoIdentificador();
	}

	public Estado lerCaractereNumerico(char caractere) {
		Lexico.obterInstancia().setBufferEntrada(String.valueOf(caractere));
		return new EstadoInteiro();
	}

	public Estado lerCaractereDelimitador(char caractere) {
		Lexico.obterInstancia().setBufferEntrada(String.valueOf(caractere));
		return new EstadoDelimitador();
	}
	
	public Estado lerCaractereRelacional(char caractere) {
		Lexico.obterInstancia().setBufferEntrada(String.valueOf(caractere));
		if(caractere == '=')
			return new EstadoRelacional1();
		else
			return new EstadoRelacional2();
	}

	public Estado lerCaractereAditivo(char caractere) {
		Lexico.obterInstancia().setBufferEntrada(String.valueOf(caractere));
		return new EstadoAditivo();
	}

	public Estado lerCaractereMultiplicativo(char caractere) {
		Lexico.obterInstancia().setBufferEntrada(String.valueOf(caractere));
		return new EstadoMultiplicativo();
	}

}
