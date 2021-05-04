package business.control.lexico.estados;

import business.control.lexico.Lexico;

public class EstadoComentario extends Estado {
	
	protected String getClassificacao() {
		return "";
	}

	public Estado lerCaractereComentarioAbre(char caractere) {
		return this;
	}
	
	public Estado lerCaractereComentarioFecha(char caractere) {
		return new EstadoInicial();
	}
	
	public Estado lerCaractereDescarte(char caractere) {
		if(caractere == '\n')
			Lexico.obterInstancia().incrementarContadorLinha();
		return this;
	}

	public Estado lerCaractereAlfabetico(char caractere) {
		return this;
	}

	public Estado lerCaractereNumerico(char caractere) {
		return this;
	}

	public Estado lerCaractereDelimitador(char caractere) {
		return this;
	}
	
	public Estado lerCaractereRelacional(char caractere) {
		return this;
	}
	
	public Estado lerCaractereAditivo(char caractere) {
		return this;
	}
	
	public Estado lerCaractereMultiplicativo(char caractere) {
		return this;
	}

	//public Estado lerCaractereIndefinidoAlfabeto(char caractere) {
	//	return this;
	//}

}
