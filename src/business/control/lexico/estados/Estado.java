package business.control.lexico.estados;

//import infra.LeitorPascal;
import business.control.lexico.Lexico;
import business.model.Token;

public abstract class Estado {
	
	protected abstract String getClassificacao();
	
	public Estado lerCaractereComentarioAbre(char caractere) {
		Lexico lexico = Lexico.obterInstancia();
		String classificacao = getClassificacao();
		Token token = new Token(lexico.getBufferEntrada(), classificacao, lexico.getContadorLinha());
		
		lexico.adicionarToken(token);
		//LeitorPascal.obterInstancia().salvarArquivoTokens(token);
		
		
		Estado estado = new EstadoInicial();
		return estado.lerCaractereComentarioAbre(caractere);
	}

	public Estado lerCaractereComentarioFecha(char caractere) {
		return null;
	}
	
	public Estado lerCaractereDescarte(char caractere){
		Lexico lexico = Lexico.obterInstancia();
		String classificacao = getClassificacao();
		Token token = new Token(lexico.getBufferEntrada(), classificacao, lexico.getContadorLinha());

		lexico.adicionarToken(token);
		//LeitorPascal.obterInstancia().salvarArquivoTokens(token);
		
		
		Estado estado = new EstadoInicial();
		return estado.lerCaractereDescarte(caractere);
	}
	
	public Estado lerCaractereAlfabetico(char caractere){
		Lexico lexico = Lexico.obterInstancia();
		String classificacao = getClassificacao();
		Token token = new Token(lexico.getBufferEntrada(), classificacao, lexico.getContadorLinha());

		lexico.adicionarToken(token);
		//LeitorPascal.obterInstancia().salvarArquivoTokens(token);
		
		
		Estado estado = new EstadoInicial();
		return estado.lerCaractereAlfabetico(caractere);
	}

	public Estado lerCaractereNumerico(char caractere) {
		Lexico lexico = Lexico.obterInstancia();
		String classificacao = getClassificacao();
		Token token = new Token(lexico.getBufferEntrada(), classificacao, lexico.getContadorLinha());

		lexico.adicionarToken(token);
		//LeitorPascal.obterInstancia().salvarArquivoTokens(token);
		
		
		Estado estado = new EstadoInicial();
		return estado.lerCaractereNumerico(caractere);
	}

	public Estado lerCaractereDelimitador(char caractere) {
		Lexico lexico = Lexico.obterInstancia();
		String classificacao = getClassificacao();
		Token token = new Token(lexico.getBufferEntrada(), classificacao, lexico.getContadorLinha());

		lexico.adicionarToken(token);
		//LeitorPascal.obterInstancia().salvarArquivoTokens(token);
		
		
		Estado estado = new EstadoInicial();
		return estado.lerCaractereDelimitador(caractere);
	}
	
	public Estado lerCaractereRelacional(char caractere) {
		Lexico lexico = Lexico.obterInstancia();
		String classificacao = getClassificacao();
		Token token = new Token(lexico.getBufferEntrada(), classificacao, lexico.getContadorLinha());

		lexico.adicionarToken(token);
		//LeitorPascal.obterInstancia().salvarArquivoTokens(token);
		
		
		Estado estado = new EstadoInicial();
		return estado.lerCaractereRelacional(caractere);
	}

	public Estado lerCaractereAditivo(char caractere) {
		Lexico lexico = Lexico.obterInstancia();
		String classificacao = getClassificacao();
		Token token = new Token(lexico.getBufferEntrada(), classificacao, lexico.getContadorLinha());

		lexico.adicionarToken(token);
		//LeitorPascal.obterInstancia().salvarArquivoTokens(token);
		
		
		Estado estado = new EstadoInicial();
		return estado.lerCaractereAditivo(caractere);
	}

	public Estado lerCaractereMultiplicativo(char caractere) {
		Lexico lexico = Lexico.obterInstancia();
		String classificacao = getClassificacao();
		Token token = new Token(lexico.getBufferEntrada(), classificacao, lexico.getContadorLinha());

		lexico.adicionarToken(token);
		//LeitorPascal.obterInstancia().salvarArquivoTokens(token);
		
		
		Estado estado = new EstadoInicial();
		return estado.lerCaractereMultiplicativo(caractere);
	}

	//public Estado lerCaractereIndefinidoAlfabeto(char caractere) {
	//	return null;
	//}
	
}
