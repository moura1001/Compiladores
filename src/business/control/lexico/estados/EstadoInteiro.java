package business.control.lexico.estados;

//import infra.LeitorPascal;
import business.control.lexico.Lexico;

public class EstadoInteiro extends Estado {
	
	protected String getClassificacao() {
		return "Número inteiro";
	}
	
	public Estado lerCaractereNumerico(char caractere) {
		String valor = Lexico.obterInstancia().getBufferEntrada() + caractere;
		Lexico.obterInstancia().setBufferEntrada(valor);
		return this;
	}

	public Estado lerCaractereDelimitador(char caractere) {
		if(caractere == '.') {
			
			String valor = Lexico.obterInstancia().getBufferEntrada() + caractere;
			Lexico.obterInstancia().setBufferEntrada(valor);
			return new EstadoReal();
			
		} else
			return super.lerCaractereDelimitador(caractere);
	}

}
