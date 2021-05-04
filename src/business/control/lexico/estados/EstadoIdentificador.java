package business.control.lexico.estados;

//import infra.LeitorPascal;
import business.control.lexico.Lexico;
import business.model.Token;

public class EstadoIdentificador extends Estado {
	
	protected String getClassificacao() {
		return "Identificador";
	}

	public Estado lerCaractereAlfabetico(char caractere) {
		String valor = Lexico.obterInstancia().getBufferEntrada() + caractere;
		Lexico.obterInstancia().setBufferEntrada(valor);
		return this;
	}

	public Estado lerCaractereNumerico(char caractere) {
		String valor = Lexico.obterInstancia().getBufferEntrada() + caractere;
		Lexico.obterInstancia().setBufferEntrada(valor);
		return this;
	}
	
	public Estado lerCaractereComentarioAbre(char caractere) {
		Lexico lexico = Lexico.obterInstancia();
		String classificacao;
		
		if(lexico.isPalavraReservada(lexico.getBufferEntrada()))
			classificacao = lexico.getClassificacaoPalavraReservada(lexico.getBufferEntrada());
		else
			classificacao = getClassificacao();
		Token token = new Token(lexico.getBufferEntrada(), classificacao, lexico.getContadorLinha());
		
		lexico.adicionarToken(token);
		//LeitorPascal.obterInstancia().salvarArquivoTokens(token);
		
		
		lexico.setBufferEntrada(String.valueOf(caractere));
		Estado estado = new EstadoInicial();
		return estado.lerCaractereComentarioAbre(caractere);
	}

	public Estado lerCaractereComentarioFecha(char caractere) {
		Lexico lexico = Lexico.obterInstancia();
		String classificacao;
		
		if(lexico.isPalavraReservada(lexico.getBufferEntrada()))
			classificacao = lexico.getClassificacaoPalavraReservada(lexico.getBufferEntrada());
		else
			classificacao = getClassificacao();
		Token token = new Token(lexico.getBufferEntrada(), classificacao, lexico.getContadorLinha());

		lexico.adicionarToken(token);
		//LeitorPascal.obterInstancia().salvarArquivoTokens(token);
		
		
		lexico.setBufferEntrada(String.valueOf(caractere));
		Estado estado = new EstadoInicial();
		return estado.lerCaractereComentarioFecha(caractere);
	}
	
	public Estado lerCaractereDescarte(char caractere){
		Lexico lexico = Lexico.obterInstancia();
		String classificacao;
		
		if(lexico.isPalavraReservada(lexico.getBufferEntrada()))
			classificacao = lexico.getClassificacaoPalavraReservada(lexico.getBufferEntrada());
		else
			classificacao = getClassificacao();
		Token token = new Token(lexico.getBufferEntrada(), classificacao, lexico.getContadorLinha());

		lexico.adicionarToken(token);
		//LeitorPascal.obterInstancia().salvarArquivoTokens(token);
		
		
		lexico.setBufferEntrada(String.valueOf(caractere));
		Estado estado = new EstadoInicial();
		return estado.lerCaractereDescarte(caractere);
	}

	public Estado lerCaractereDelimitador(char caractere) {
		Lexico lexico = Lexico.obterInstancia();
		String classificacao;
		
		if(lexico.isPalavraReservada(lexico.getBufferEntrada()))
			classificacao = lexico.getClassificacaoPalavraReservada(lexico.getBufferEntrada());
		else
			classificacao = getClassificacao();
		Token token = new Token(lexico.getBufferEntrada(), classificacao, lexico.getContadorLinha());

		lexico.adicionarToken(token);
		//LeitorPascal.obterInstancia().salvarArquivoTokens(token);
		
		
		lexico.setBufferEntrada(String.valueOf(caractere));
		Estado estado = new EstadoInicial();
		return estado.lerCaractereDelimitador(caractere);
	}
	
	public Estado lerCaractereRelacional(char caractere) {
		Lexico lexico = Lexico.obterInstancia();
		String classificacao;
		
		if(lexico.isPalavraReservada(lexico.getBufferEntrada()))
			classificacao = lexico.getClassificacaoPalavraReservada(lexico.getBufferEntrada());
		else
			classificacao = getClassificacao();
		Token token = new Token(lexico.getBufferEntrada(), classificacao, lexico.getContadorLinha());

		lexico.adicionarToken(token);
		//LeitorPascal.obterInstancia().salvarArquivoTokens(token);
		
		
		lexico.setBufferEntrada(String.valueOf(caractere));
		Estado estado = new EstadoInicial();
		return estado.lerCaractereRelacional(caractere);
	}

	public Estado lerCaractereAditivo(char caractere) {
		Lexico lexico = Lexico.obterInstancia();
		String classificacao;
		
		if(lexico.isPalavraReservada(lexico.getBufferEntrada()))
			classificacao = lexico.getClassificacaoPalavraReservada(lexico.getBufferEntrada());
		else
			classificacao = getClassificacao();
		Token token = new Token(lexico.getBufferEntrada(), classificacao, lexico.getContadorLinha());

		lexico.adicionarToken(token);
		//LeitorPascal.obterInstancia().salvarArquivoTokens(token);
		
		
		lexico.setBufferEntrada(String.valueOf(caractere));
		Estado estado = new EstadoInicial();
		return estado.lerCaractereAditivo(caractere);
	}

	public Estado lerCaractereMultiplicativo(char caractere) {
		Lexico lexico = Lexico.obterInstancia();
		String classificacao;
		
		if(lexico.isPalavraReservada(lexico.getBufferEntrada()))
			classificacao = lexico.getClassificacaoPalavraReservada(lexico.getBufferEntrada());
		else
			classificacao = getClassificacao();
		Token token = new Token(lexico.getBufferEntrada(), classificacao, lexico.getContadorLinha());

		lexico.adicionarToken(token);
		//LeitorPascal.obterInstancia().salvarArquivoTokens(token);
		
		
		lexico.setBufferEntrada(String.valueOf(caractere));
		Estado estado = new EstadoInicial();
		return estado.lerCaractereMultiplicativo(caractere);
	}

}
