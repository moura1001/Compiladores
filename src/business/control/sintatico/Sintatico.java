package business.control.sintatico;

import business.control.lexico.Lexico;
import business.control.semantico.Semantico;
import business.model.Token;
import util.SintaticoException;

public class Sintatico {
	private static Sintatico singleton;
	
	private Token token;
	
	private Lexico lexico;
	private Semantico semantico;
	
	private Sintatico() {
		this.lexico = Lexico.obterInstancia();
		this.semantico = Semantico.obterInstancia();
	}

	public static Sintatico obterInstancia() {
        if (singleton == null) {
        	singleton = new Sintatico();
        }
        return singleton;
    }
	
	public void analiseSintatica() throws SintaticoException {
		//System.out.print("Identificadores: ");
		
		this.token = this.lexico.getProximoToken();
		
		try {
			this.programa();
		
		} catch(SintaticoException e) {
			throw e;
		}
		
		System.out.println("\nAnálise sintática completada com sucesso.");
		System.out.println("Análise semântica completada com sucesso.\n");
		
	}
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 1 da gramática.
	 * */
	private void programa() throws SintaticoException {
		if(this.token.getToken().equals("program")) {
			
			this.token = this.lexico.getProximoToken();
			
			if(this.token.getClassificacao().equals("Identificador")){
				
				//Parte do analisador semântico - Tabela de símbolos
				this.semantico.entrarNovoEscopo();
				this.semantico.declararIdentificador(this.token, false);
				/////////////////////////////////////////////////
				
				//Parte do analisador semântico - Verificação de tipos
				this.semantico.atribuirTipoIdentificadores("program");
				/////////////////////////////////////////////////
				
				this.token = this.lexico.getProximoToken();
				
				if(this.token.getToken().equals(";")) {
					
					this.token = this.lexico.getProximoToken();					
					try {
					
						this.declaracoesVariaveis();					
						this.declaracoesDeSubprogramas();					
						
						if(!this.comandoComposto())
							//throw new SintaticoException("Erro comando composto");
							throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
									+ ". O programa estava esperando a palavra reservada begin"
									+ " para iniciar o bloco do programa principal e veio o token "
									+ this.token.getToken() + " que é " + this.token.getClassificacao());
					
					} catch(SintaticoException e) {
						throw e;
					}
					
					if(!this.token.getToken().equals("."))
						throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
								+ ". O programa estava esperando o delimitador . e veio o token "
								+ this.token.getToken() + " que é " + this.token.getClassificacao());
					
				
				} else
					throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
							+ ". O programa estava esperando o delimitador ; e veio o token "
							+ this.token.getToken() + " que é " + this.token.getClassificacao());
			
			} else
				throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
						+ ". O programa estava esperando um identificador e veio o token "
						+ this.token.getToken() + " que é " + this.token.getClassificacao());
		
		} else
			throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
					+ ". O programa estava esperando a palavra reservada program e veio o token "
					+ this.token.getToken() + " que é " + this.token.getClassificacao());
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 2 da gramática.
	 * */
	private void declaracoesVariaveis() throws SintaticoException {
		if(this.token.getToken().equals("var")) {
			
			this.token = this.lexico.getProximoToken();
			try {
				
				this.listaDeclaracoesVariaveis();
			
			} catch(SintaticoException e) {
				throw e;
			}
		
		}
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 3 da gramática. Foi necessário um procedimento
	 * para a retirada de recursão à esquerda
	 * */
	private void listaDeclaracoesVariaveis() throws SintaticoException {
		try {
			
			this.listaDeIdentificadores();
			
			if(this.token.getToken().equals(":")) {
			
				this.token = this.lexico.getProximoToken();
				this.tipo();
				
				if(this.token.getToken().equals(";")) {
				
					this.token = this.lexico.getProximoToken();
					this.listaDeclaracoesVariaveis2();
				
				} else
					throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
							+ ". O programa estava esperando o delimitador ; e veio o token "
							+ this.token.getToken() + " que é " + this.token.getClassificacao());
			
			} else
				throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
						+ ". O programa estava esperando o delimitador : e veio o token "
						+ this.token.getToken() + " que é " + this.token.getClassificacao());
		
		} catch(SintaticoException e) {
			throw e;
		}
	}
	
	private void listaDeclaracoesVariaveis2() throws SintaticoException {
		try {
			
			if(!this.listaDeIdentificadores())
				return;
			
			if(this.token.getToken().equals(":")) {
			
				this.token = this.lexico.getProximoToken();
				this.tipo();
				
				if(this.token.getToken().equals(";")) {
				
					this.token = this.lexico.getProximoToken();
					this.listaDeclaracoesVariaveis2();
					
				} else
					throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
							+ ". O programa estava esperando o delimitador ; e veio o token "
							+ this.token.getToken() + " que é " + this.token.getClassificacao());
			
			} else
				throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
						+ ". O programa estava esperando o delimitador : e veio o token "
						+ this.token.getToken() + " que é " + this.token.getClassificacao());
		
		} catch(SintaticoException e) {
			throw e;
		}
	}
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 4 da gramática. Foi necessário um procedimento
	 * para a retirada de recursão à esquerda
	 * */
	private boolean listaDeIdentificadores() throws SintaticoException {
		if(this.token.getClassificacao().equals("Identificador")) {
			
			//Parte do analisador semântico - Tabela de símbolos
			this.semantico.declararIdentificador(this.token, false);
			////////////////////////////////////////////////////////////
			
			this.token = this.lexico.getProximoToken();
			try {
				this.listaDeIdentificadores2();			
			
			} catch(SintaticoException e) {
				throw e;
			}
			
			return true;
		
		}
		
		return false;
	}
	
	private void listaDeIdentificadores2() throws SintaticoException {
		if(this.token.getToken().equals(",")) {
			
			this.token = this.lexico.getProximoToken();
			
			if(this.token.getClassificacao().equals("Identificador")) {
				
				//Parte do analisador semântico - Tabela de símbolos
				this.semantico.declararIdentificador(this.token, false);
				///////////////////////////////////////////////////////
				
				this.token = this.lexico.getProximoToken();
				this.listaDeIdentificadores2();
			
			} else
				throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
						+ ". O programa estava esperando um identificador e veio o token "
						+ this.token.getToken() + " que é " + this.token.getClassificacao());
		
		}
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 5 da gramática.
	* */
	private void tipo() throws SintaticoException {
		if(!(this.token.getToken().equals("integer") || this.token.getToken().equals("real") || this.token.getToken().equals("boolean")))
			throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
					+ ". O programa estava esperando um tipo integer, real ou boolean e veio o token "
					+ this.token.getToken() + " que é " + this.token.getClassificacao());
		
		
		//Parte do analisador semântico - Verificação de tipos
		this.semantico.atribuirTipoIdentificadores(this.token.getToken());
		/////////////////////////////////////////////////
		
		
		this.token = this.lexico.getProximoToken();
	}
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 6 da gramática. Foi necessário um procedimento
	 * para a retirada de recursão à esquerda
	 * */
	private void declaracoesDeSubprogramas() throws SintaticoException {
		try {
		
			this.declaracoesDeSubprogramas2();
		
		} catch(SintaticoException e) {
			throw e;
		}
	}
	
	private void declaracoesDeSubprogramas2() throws SintaticoException {
		try {
			
			if(!this.declaracaoDeSubprograma())
				return;
			
			if(this.token.getToken().equals(";")) {
			
				this.token = this.lexico.getProximoToken();
				this.declaracoesDeSubprogramas2();
			
			} else
				throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
					+ ". O programa estava esperando o delimitador ; e veio o token "
					+ this.token.getToken() + " que é " + this.token.getClassificacao());
		
		} catch(SintaticoException e) {
			throw e;
		}
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 7 da gramática.
	 * */
	private boolean declaracaoDeSubprograma() throws SintaticoException {
		if(this.token.getToken().equals("procedure")) {
		
			this.token = this.lexico.getProximoToken();
			
			if(this.token.getClassificacao().equals("Identificador")) {
				
				//Parte do analisador semântico - Tabela de símbolos
				this.semantico.declararIdentificador(this.token, true);
				
				//Parte do analisador semântico - Verificação de tipos
				this.semantico.atribuirTipoIdentificadores("procedimento");
				/////////////////////////////////////////////////
				
				this.semantico.entrarNovoEscopo();
				/////////////////////////////////////////////////
			
				this.token = this.lexico.getProximoToken();
				
				try {
				
					this.argumentos();
				
				} catch(SintaticoException e) {
					throw e;
				}
				
				if(!this.token.getToken().equals(";"))
					throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
							+ ". O programa estava esperando o delimitador ; e veio o token "
							+ this.token.getToken() + " que é " + this.token.getClassificacao());
				
				this.token = this.lexico.getProximoToken();
				
				try {
				
					this.declaracoesVariaveis();
					this.declaracoesDeSubprogramas();
					
					if(!this.comandoComposto())
						//throw new SintaticoException("Erro comando composto!");
						throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
								+ ". O programa estava esperando a palavra reservada begin"
								+ " para iniciar o bloco de definição do subprograma e veio o token "
								+ this.token.getToken() + " que é " + this.token.getClassificacao());
				
				} catch(SintaticoException e) {
					throw e;
				}
				
				return true;				
			
			} else
				throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
						+ ". O programa estava esperando um identificador e veio o token "
						+ this.token.getToken() + " que é " + this.token.getClassificacao());
		
		}
		
		return false;
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 8 da gramática.
	 * */
	private void argumentos() throws SintaticoException {
		if(this.token.getToken().equals("(")) {
		
			this.token = this.lexico.getProximoToken();
			
			try {
			
				//Parte do analisador semântico - Verificação de tipos
				this.semantico.setIsDeclaracaoParametros(true);
				/////////////////////////////////////////////////
				
				this.listaDeParametros();
				
				//Parte do analisador semântico - Verificação de tipos
				this.semantico.setIsDeclaracaoParametros(false);
				/////////////////////////////////////////////////
			
			} catch(SintaticoException e) {
				throw e;
			}
			
			if(!this.token.getToken().equals(")"))
				throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
						+ ". O programa estava esperando o delimitador ) e veio o token "
						+ this.token.getToken() + " que é " + this.token.getClassificacao());
			
			this.token = this.lexico.getProximoToken();
		
		}
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 9 da gramática. Foi necessário um procedimento
	 * para a retirada de recursão à esquerda
	 * */
	private void listaDeParametros() throws SintaticoException {
		try {
		
			if(!this.listaDeIdentificadores())
				throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
						+ ". O programa estava esperando um identificador e veio o token "
						+ this.token.getToken() + " que é " + this.token.getClassificacao());
		
		} catch(SintaticoException e) {
			throw e;
		}
		
		if(this.token.getToken().equals(":")) {
		
			this.token = this.lexico.getProximoToken();
			
			try {
			
				this.tipo();
				this.listaDeParametros2();
			
			} catch(SintaticoException e) {
				throw e;
			}
		
		} else
			throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
					+ ". O programa estava esperando o delimitador : e veio o token "
					+ this.token.getToken() + " que é " + this.token.getClassificacao());
	}
	
	private void listaDeParametros2() throws SintaticoException {
		if(this.token.getToken().equals(";")) {
		
			this.token = this.lexico.getProximoToken();
			
			try {
			
				this.listaDeIdentificadores();
			
			} catch(SintaticoException e) {
				throw e;
			}
			
			if(this.token.getToken().equals(":")) {
				
				this.token = this.lexico.getProximoToken();
				
				try {
				
					this.tipo();
					this.listaDeParametros2();
				
				} catch(SintaticoException e) {
					throw e;
				}
			
			} else
				throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
						+ ". O programa estava esperando o delimitador : e veio o token "
						+ this.token.getToken() + " que é " + this.token.getClassificacao());
		
		}
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 10 da gramática.
	 * */
	private boolean comandoComposto() throws SintaticoException {
		if(this.token.getToken().equals("begin")) {
			
			//Parte do analisador semântico - Tabela de símbolos
			this.semantico.incrementarNivelEscopo();
			/////////////////////////////////////////////////
		
			this.token = this.lexico.getProximoToken();
			
			try {
			
				this.comandosOpcionais();
			
			} catch(SintaticoException e) {
				throw e;
			}
			
			if(!this.token.getToken().equals("end"))
				throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
						+ ". O programa estava esperando a palavra reservada end e veio o token "
						+ this.token.getToken() + " que é " + this.token.getClassificacao());
			
			
			//Parte do analisador semântico - Tabela de símbolos
			this.semantico.decrementarNivelEscopo();
			if(this.semantico.getNivelEscopo() == 0)
				this.semantico.sairEscopoAtual();
			/////////////////////////////////////////////////
			
			this.token = this.lexico.getProximoToken();
			return true;
		
		}
		
		return false;
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 11 da gramática.
	 * */
	private void comandosOpcionais() throws SintaticoException {
		try {
			
			this.listaDeComandos();
		
		} catch(SintaticoException e) {
				throw e;
		}
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 12 da gramática. Foi necessário um procedimento
	 * para a retirada de recursão à esquerda
	 * */
	private void listaDeComandos() throws SintaticoException {
		try {
		
			if(!this.comando())
				return;
			
			this.listaDeComandos2();
		
		} catch(SintaticoException e) {
			throw e;
		}
	}
	
	private void listaDeComandos2() throws SintaticoException {
		if(this.token.getToken().equals(";")) {
		
			this.token = this.lexico.getProximoToken();
			
			try {
			
				if(!this.comando())
					return;
				
				this.listaDeComandos2();
			
			} catch(SintaticoException e) {
				throw e;
			}
		
		} else
			throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
					+ ". O programa estava esperando o delimitador ; e veio o token "
					+ this.token.getToken() + " que é " + this.token.getClassificacao());
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 13 da gramática.
	 * */
	private boolean comando() throws SintaticoException {
		
		int linha = -1;
		
		if(this.token.getToken().equals("if")) {
			
			linha = this.token.getLinha();
			
			this.token = this.lexico.getProximoToken();
			this.expressao();
			
			
			//Parte do analisador semântico - Verificação de tipos
			this.semantico.verificarTipoPcTDaExpressao("boolean", linha);
			/////////////////////////////////////////////////
			
			
			if(this.token.getToken().equals("then")) {
			
				this.token = this.lexico.getProximoToken();
				this.comando();				
				this.parteElse();
			
			} else
				throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
						+ ". O programa estava esperando a palavra reservada then e veio o token "
						+ this.token.getToken() + " que é " + this.token.getClassificacao());
			
			return true;			
		
		}
		
		if(this.token.getToken().equals("while")) {
			
			linha = this.token.getLinha();
			
			this.token = this.lexico.getProximoToken();
			this.expressao();
			
			
			//Parte do analisador semântico - Verificação de tipos
			this.semantico.verificarTipoPcTDaExpressao("boolean", linha);
			/////////////////////////////////////////////////
			
			
			if(this.token.getToken().equals("do")) {
			
				this.token = this.lexico.getProximoToken();
				this.comando();
			
			} else
				throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
						+ ". O programa estava esperando a palavra reservada do e veio o token "
						+ this.token.getToken() + " que é " + this.token.getClassificacao());
			
			return true;	
		
		}
		
		
		if(this.variavel()) {
			
			Token auxToken = this.token;
			linha = this.token.getLinha();
			
			this.token = this.lexico.getProximoToken();
							
			if(this.token.getToken().equals(":=")) {
			
				//Parte do analisador semântico - Tabela de símbolos
				this.semantico.usarIdentificador(auxToken, false);
				/////////////////////////////////////////////////				
				
				//Parte do analisador semântico - Verificação de tipos
				String tipo = this.semantico.getUltimoIdentificadorUsado().getTipo();
				/////////////////////////////////////////////////
				
				this.token = this.lexico.getProximoToken();
				this.expressao();
				
				//Parte do analisador semântico - Verificação de tipos
				this.semantico.verificarTipoPcTDaExpressao(tipo, linha);
				/////////////////////////////////////////////////
				
				return true;
			
			}
			
			/* Nesse momento é sabido que não se trata de uma expressão de atribuição.
			 * Então será necessário obter novamente o token identificador, que não é
			 * uma variável. Daí, o próximo passo testa se é a ativação de um procedimento.
			 * */			
			this.lexico.retrocederPosicaoProximoToken();
			this.lexico.retrocederPosicaoProximoToken();
			this.token = this.lexico.getProximoToken();
		
		}
		
		try {
			
			if(this.ativacaoDeProcedimento())
				return true;
			
			if(this.comandoComposto())
				return true;
		
		
		} catch(SintaticoException e) {
				throw e;
		}
		
		
		return false;
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 14 da gramática.
	 * */
	private void parteElse() throws SintaticoException {
		if(this.token.getToken().equals("else")) {
		
			this.token = this.lexico.getProximoToken();
			
			try {
				
				this.comando();
			
			} catch(SintaticoException e) {
				throw e;
			}
		
		}
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 15 da gramática.
	 * */
	private boolean variavel() {
		if(this.token.getClassificacao().equals("Identificador"))
			return true;
		else
			return false;
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 16 da gramática. Foi necessário um procedimento
	 * para a retirada de não determinismo
	 * */
	private boolean ativacaoDeProcedimento() throws SintaticoException {
		if(this.token.getClassificacao().equals("Identificador")) {
			
			//Parte do analisador semântico - Tabela de símbolos
			this.semantico.usarIdentificador(this.token, true);
			/////////////////////////////////////////////////
		
			this.token = this.lexico.getProximoToken();
			try {
			
				this.ativacaoDeProcedimento2();
			
			} catch(SintaticoException e) {
				throw e;
			}
			
			return true;
		
		} else
			return false;
	}
	
	private void ativacaoDeProcedimento2() throws SintaticoException {
		int linha = this.token.getLinha();
		
		if(this.token.getToken().equals("(")) {
		
			this.token = this.lexico.getProximoToken();
			try{
			
				this.listaDeExpressoes();
			
			} catch(SintaticoException e) {
				throw e;
			}
			
			if(!this.token.getToken().equals(")"))
				throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
						+ ". O programa estava esperando o delimitador ) e veio o token "
						+ this.token.getToken() + " que é " + this.token.getClassificacao());
			
			
			//Parte do analisador semântico - Verificação de tipos
			this.semantico.verificarAtivacaoProcedimento(linha);
			/////////////////////////////////////////////////
			
			this.token = this.lexico.getProximoToken();
		
		}
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 17 da gramática. Foi necessário um procedimento
	 * para a retirada de recursão à esquerda
	 * */
	private void listaDeExpressoes() throws SintaticoException {
		try {
			
			this.expressao();
			
			//Parte do analisador semântico - Verificação de tipos
			String tipo = this.semantico.getTipoPcT();
			this.semantico.adicionarTipoParametroAtivacaoProcedimento(tipo);
			/////////////////////////////////////////////////
			
			this.listaDeExpressoes2();
		
		} catch(SintaticoException e) {
				throw e;
		}
	}
	
	private void listaDeExpressoes2() throws SintaticoException {
		if(this.token.getToken().equals(",")) {
		
			this.token = this.lexico.getProximoToken();
			try {
				
				this.expressao();
				
				//Parte do analisador semântico - Verificação de tipos
				String tipo = this.semantico.getTipoPcT();
				this.semantico.adicionarTipoParametroAtivacaoProcedimento(tipo);
				/////////////////////////////////////////////////
				
				this.listaDeExpressoes2();
			
			} catch(SintaticoException e) {
					throw e;
			}
		
		}
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 18 da gramática. Foi necessário um procedimento
	 * para a retirada de não determinismo
	 * */
	private void expressao() throws SintaticoException {
		try {
			
			this.expressaoSimples();			
			this.expressao2();
		
		} catch(SintaticoException e) {
			throw e;
		}
	}
	
	private void expressao2() throws SintaticoException {
		
		int linha = this.token.getLinha();
		
		if(!this.operadorRelacional())
			return;
		
		try {
		
			//linha = this.token.getLinha();
			
			this.expressaoSimples();
			
			//Parte do analisador semântico - Verificação de tipos
			this.semantico.verificarCompatibilidadeTiposParaOperacaoRelacional(linha);
			/////////////////////////////////////////////////
		
		} catch(SintaticoException e) {
			throw e;
		}
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 19 da gramática. Foi necessário um procedimento
	 * para a retirada de recursão à esquerda
	 * */
	private void expressaoSimples() throws SintaticoException {
		
		int linha = this.token.getLinha();
		boolean temSinal = this.sinal();
		
		try {
			
			this.termo();
			
			//Parte do analisador semântico - Verificação de tipos
			if(temSinal)
				this.semantico.verificarTipoPcTParaOperadorSinal(linha);
			/////////////////////////////////////////////////
			
			this.expressaoSimples2();
		
		} catch(SintaticoException e) {
			throw e;
		}
	}
	
	private void expressaoSimples2() throws SintaticoException {
		
		int linha = this.token.getLinha();
		int oprAditivo;
		if((oprAditivo = this.operadorAditivo()) == 0)
			return;
		
		try {
			
			this.termo();
			
			//Parte do analisador semântico - Verificação de tipos
			if(oprAditivo == 1)
				this.semantico.verificarCompatibilidadeTiposParaOperacaoAritmetica(linha);
			else
				this.semantico.verificarCompatibilidadeTiposParaOperacaoLogica(linha);
			/////////////////////////////////////////////////
			
			this.expressaoSimples2();
		
		} catch(SintaticoException e) {
			throw e;
		}
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 20 da gramática. Foi necessário um procedimento
	 * para a retirada de recursão à esquerda
	 * */
	private void termo() throws SintaticoException {
		try {
			
			this.fator();
			this.termo2();
		
		} catch(SintaticoException e) {
			throw e;
		}
	}
	
	private void termo2() throws SintaticoException {
		
		int linha = this.token.getLinha();
		int oprMultiplicativo;
		if((oprMultiplicativo = this.operadorMultiplicativo()) == 0)
			return;
		
		try {
			
			this.fator();
			
			//Parte do analisador semântico - Verificação de tipos
			if(oprMultiplicativo == 1)
				this.semantico.verificarCompatibilidadeTiposParaOperacaoAritmetica(linha);
			else
				this.semantico.verificarCompatibilidadeTiposParaOperacaoLogica(linha);
			/////////////////////////////////////////////////
			
			this.termo2();
		
		} catch(SintaticoException e) {
			throw e;
		}
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 21 da gramática.
	 * */
	private void fator() throws SintaticoException {
		
		if(this.token.getClassificacao().equals("Identificador")) {
			
			//Parte do analisador semântico - Tabela de símbolos
			this.semantico.usarIdentificador(this.token, false);
			/////////////////////////////////////////////////
			
			this.token = this.lexico.getProximoToken();
			
			
			//Parte do analisador semântico - Verificação de tipos
			String tipo = this.semantico.getUltimoIdentificadorUsado().getTipo();
			this.semantico.armazenarTipoNaPcT(tipo);
			/////////////////////////////////////////////////
			
			return;			
		
		}
		
		if(this.token.getClassificacao().equals("Número inteiro")) {
			
			//Parte do analisador semântico - Verificação de tipos
			this.semantico.armazenarTipoNaPcT("integer");
			/////////////////////////////////////////////////
			
			this.token = this.lexico.getProximoToken();
			
			return;			
		
		}
		
		if(this.token.getClassificacao().equals("Número real")) {
			
			//Parte do analisador semântico - Verificação de tipos
			this.semantico.armazenarTipoNaPcT("real");
			/////////////////////////////////////////////////
			
			this.token = this.lexico.getProximoToken();
			
			return;			
		
		}
		
		if(this.token.getClassificacao().equals("Booleano")) {
			
			//Parte do analisador semântico - Verificação de tipos
			this.semantico.armazenarTipoNaPcT("boolean");
			/////////////////////////////////////////////////
			
			this.token = this.lexico.getProximoToken();
			
			return;			
		
		}
		
		if(this.token.getToken().equals("(")) {
			
			this.token = this.lexico.getProximoToken();
			try {
			
				this.expressao();
			
			} catch(SintaticoException e) {
				throw e;
			}
			
			if(!this.token.getToken().equals(")"))
				throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
						+ ". O programa estava esperando o delimitador ) e veio o token "
						+ this.token.getToken() + " que é " + this.token.getClassificacao());
			
			this.token = this.lexico.getProximoToken();
			
			return;
		
		}
		
		if(this.token.getToken().equals("not")) {

			int linha = this.token.getLinha();
		
			this.token = this.lexico.getProximoToken();
			try {
				
				this.fator();
				
				//Parte do analisador semântico - Verificação de tipos
				this.semantico.verificarTipoPcTParaOperadorNot(linha);
				/////////////////////////////////////////////////
				
			
			} catch(SintaticoException e) {
				throw e;
			}
			
			return;
		
		}
		
		//throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
		//		+ ". O programa estava esperando um fator e veio o token " 
		//		+ this.token.getToken() + " que é " + this.token.getClassificacao());
		throw new SintaticoException("Erro sintático na linha " + this.token.getLinha()
				+ ". Expressão inválida.");
	}
	
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 22 da gramática.
	 * */
	private boolean sinal() {
		if(this.token.getToken().equals("+") || this.token.getToken().equals("-")) {
			this.token = this.lexico.getProximoToken();
			return true;
		}
		
		return false;
	}
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 23 da gramática.
	 * */
	private boolean operadorRelacional() {
		/*if(this.token.getToken().matches("=|<|>|<=|>=|<>")) {
			this.token = this.lexico.getProximoToken();
			return true;
		
		}*/
		
		/*if(this.token.getToken().equals("=") || this.token.getToken().equals("<>")) {
			this.token = this.lexico.getProximoToken();
			return 1;
		
		} else if(this.token.getToken().matches("<|>|<=|>=")) {
			this.token = this.lexico.getProximoToken();
			return 2;
		}
		
		return 0;*/
		
		if(this.token.getClassificacao().equals("Operador relacional")) {
			this.token = this.lexico.getProximoToken();
			return true;
		}
		
		return false;
	}
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 24 da gramática.
	 * */
	private int operadorAditivo() {
		if(this.token.getToken().equals("+") || this.token.getToken().equals("-")) {
			this.token = this.lexico.getProximoToken();
			return 1;
		
		} else if(this.token.getToken().equals("or")) {
			this.token = this.lexico.getProximoToken();
			return 2;
		}
		
		/*if(this.token.getClassificacao().equals("Operador aditivo")) {
			this.token = this.lexico.getProximoToken();
			return true;
		}*/
		
		return 0;
	}
	
	////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////
	/* Regra 25 da gramática.
	 * */
	private int operadorMultiplicativo() {
		if(this.token.getToken().equals("*") || this.token.getToken().equals("/")) {
			this.token = this.lexico.getProximoToken();
			return 1;
		
		} else if(this.token.getToken().equals("and")) {
			this.token = this.lexico.getProximoToken();
			return 2;
		}
		
		/*if(this.token.getClassificacao().equals("Operador multiplicativo")) {
			this.token = this.lexico.getProximoToken();
			return true;
		}*/
		
		return 0;
	}
}
