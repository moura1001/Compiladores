package business.control.lexico.estados;

import business.control.lexico.Lexico;

public class EstadoRelacional2 extends Estado {
	
	protected String getClassificacao() {
		return "Operador relacional";
	}
	
	public Estado lerCaractereRelacional(char caractere) {
		if(caractere == '=' || caractere == '>') {
			String valor = Lexico.obterInstancia().getBufferEntrada() + caractere;
			Lexico.obterInstancia().setBufferEntrada(valor);
			return new EstadoRelacional3();
		
		}else
			return super.lerCaractereRelacional(caractere);
	}
}
