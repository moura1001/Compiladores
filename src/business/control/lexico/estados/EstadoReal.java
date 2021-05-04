package business.control.lexico.estados;

import business.control.lexico.Lexico;

public class EstadoReal extends Estado {
	
	protected String getClassificacao() {
		return "Número real";
	}
	
	public Estado lerCaractereNumerico(char caractere) {
		String valor = Lexico.obterInstancia().getBufferEntrada() + caractere;
		Lexico.obterInstancia().setBufferEntrada(valor);
		return this;
	}

}
