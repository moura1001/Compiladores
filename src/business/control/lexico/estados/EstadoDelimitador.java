package business.control.lexico.estados;

import business.control.lexico.Lexico;

public class EstadoDelimitador extends Estado {

	protected String getClassificacao() {
		return "Delimitador";
	}
	
	public Estado lerCaractereRelacional(char caractere) {
		if(caractere == '=') {
			String valor = Lexico.obterInstancia().getBufferEntrada() + caractere;
			Lexico.obterInstancia().setBufferEntrada(valor);
			return new EstadoAtribuicao();
		} else
			return super.lerCaractereRelacional(caractere);
	}

}
