package business.control.semantico;

import java.util.ArrayList;

import business.model.Identificador;
import business.model.Procedimento;
import business.model.Token;
import util.SemanticoException;

public class Semantico {
	private static Semantico singleton;
	
	private int nivelEscopo;
	
	private PilhaIdentificadores pilhaIdentificadores;
	private PilhaControleTipos pilhaPcT;
	
	private Identificador ultimoIdentificadorUsado;
	
	private Procedimento ultimoProcedimentoDeclarado;
	private Identificador ultimoProcedimentoUsado;
	private boolean isDeclaracaoParametros;
	private ArrayList<String> tiposParametrosUltimaAticacaoProcedimento;
	
	private Semantico() {
		this.nivelEscopo = 0;
		this.pilhaIdentificadores = new PilhaIdentificadores();
		this.pilhaPcT = new PilhaControleTipos();
		this.ultimoIdentificadorUsado = null;
		this.ultimoProcedimentoDeclarado = null;
		this.ultimoProcedimentoUsado = null;
		this.isDeclaracaoParametros = false;
		this.tiposParametrosUltimaAticacaoProcedimento = new ArrayList<String>();
	}

	public static Semantico obterInstancia() {
        if (singleton == null) {
        	singleton = new Semantico();
        }
        return singleton;
    }
	
	public int getNivelEscopo() {
		return this.nivelEscopo;
	}
	
	public void incrementarNivelEscopo() {
		this.nivelEscopo++;
	}
	
	public void decrementarNivelEscopo() {
		this.nivelEscopo--;
	}
	
	public Identificador getUltimoIdentificadorUsado() {
		return this.ultimoIdentificadorUsado;
	}
	
	public void setIsDeclaracaoParametros(boolean isDeclaracaoParametros) {
		this.isDeclaracaoParametros = isDeclaracaoParametros;
	}
	
	private void lancarExcessao(SemanticoException excessao) {
		try {
			throw excessao;
		
		} catch (SemanticoException e) {
			System.err.println(e);
			System.exit(-3);
		}
	}
	
	public void declararIdentificador(Token token, boolean isProcedimento) {
		if(this.pilhaIdentificadores.procurarAntesDeDeclarar(token.getToken()) == null) {
			//System.out.print(token.getToken() + ", ");
			
			if(!isProcedimento) {
			
				Identificador identificador = new Identificador(token.getToken());
				this.pilhaIdentificadores.push(identificador);
				
				if(this.isDeclaracaoParametros)
					this.ultimoProcedimentoDeclarado.adicionarParametro(identificador);
			
			
			} else {
				Procedimento procedimento = new Procedimento(token.getToken());
				this.pilhaIdentificadores.push(procedimento);
				this.ultimoProcedimentoDeclarado = procedimento;
			}
			
			return;
		
		} else
			this.lancarExcessao(new SemanticoException("Erro semântico na linha " + token.getLinha()
										+ ". O identificador " + token.getToken()
										+ " já existe no escopo atual."));
	}
	
	public void usarIdentificador(Token token, boolean isProcedimento) {
		Identificador identificador = this.pilhaIdentificadores.procurarAntesDeUsar(token.getToken());
		
		if(identificador != null) {
		
			if(!identificador.getTipo().equals("program")) {
			
				this.ultimoIdentificadorUsado = identificador;
				
				if(isProcedimento) {
					if(identificador.getTipo().equals("procedimento"))
						this.ultimoProcedimentoUsado = identificador;
					
					else
						this.lancarExcessao(new SemanticoException("Erro semântico na linha " + token.getLinha()
													+ ". Expressão ilegal utilizando o token "
													+ token.getToken() + " que é uma variável."));						
				}
				
				//this.verificarTipo(identificador, token.getLinha());
				return;
			
			} else
				this.lancarExcessao(new SemanticoException("Erro semântico na linha " + token.getLinha()
											+ ". O identificador " + token.getToken()
											+ ", nome do programa, não pode ser usado"
											+ " em comandos e expressões."));				
		
		} else
			this.lancarExcessao(new SemanticoException("Erro semântico na linha " + token.getLinha()
										+ ". O identificador " + token.getToken()
										+ " não pode ser usado no escopo atual pois"
										+ " não foi declarado anteriormente."));
	}
	
	public void entrarNovoEscopo() {
		this.pilhaIdentificadores.push(new Identificador("$", true));
	}
	
	public void sairEscopoAtual() {
		while(!this.pilhaIdentificadores.pop().getToken().equals("$"))
			;
	}
	
	public void atribuirTipoIdentificadores(String tipo) {
		int index = this.pilhaIdentificadores.size() - 1;
		
		Identificador identificador = this.pilhaIdentificadores.peek();
		
		while(index >= 0 &&
				!identificador.isMarcado()) {
			index--;
			identificador.setTipo(tipo);
			identificador.marcar();
			//System.out.print(identificador.getToken() + " (tipo " + identificador.getTipo() + "), ");
			if(!(index < 0))
				identificador = this.pilhaIdentificadores.get(index);
		}
	}
	
	public void armazenarTipoNaPcT(String tipo) {
		this.pilhaPcT.push(tipo);
	}
	
	public void verificarTipoPcTDaExpressao(String tipo, int linha) {
		/* Caso essa chamada seja para verificar o tipo de uma expressão
		 * X para uma atribuição do tipo Y := X, se Y for do tipo real
		 * o tipo resultante da expressão X pode ser real ou integer
		 * */
		if((tipo.equals("real") && this.pilhaPcT.topo().matches("real|integer")) ||
				this.pilhaPcT.topo().equals(tipo)) {
			
			this.pilhaPcT.clear();
			return;
		
		} else
			this.lancarExcessaoIncompatibilidadeTipos(tipo, linha);
	}
	
	public void verificarTipoPcTParaOperadorSinal(int linha) {
		if(!(this.pilhaPcT.topo().equals("integer") || this.pilhaPcT.topo().equals("real")))
			this.lancarExcessaoIncompatibilidadeTipos("integer ou real", linha);
	}
	
	public void verificarTipoPcTParaOperadorNot(int linha) {
		if(!this.pilhaPcT.topo().equals("boolean"))
			this.lancarExcessaoIncompatibilidadeTipos("boolean", linha);
	}
	
	public String getTipoPcT() {
		String tipo = this.pilhaPcT.topo();
		this.pilhaPcT.clear();
		return tipo;
	}
	
	private void lancarExcessaoIncompatibilidadeTipos(String tipo, int linha) {
		try {
			throw new SemanticoException("Erro semântico na linha " + linha
					+ ". O resultado da expressão deveria ser " + tipo
					+ " e veio " + this.pilhaPcT.topo());
		} catch (SemanticoException e) {
			System.err.println(e);
			System.exit(-3);
		}
	}
	
	private void atualizarPcT(String tipoResultante) {
		this.pilhaPcT.pop();
		this.pilhaPcT.pop();
		this.pilhaPcT.push(tipoResultante);
	}
	
	public void verificarCompatibilidadeTiposParaOperacaoAritmetica(int linha) {
		String topo = this.pilhaPcT.topo();
		String subTopo = this.pilhaPcT.subTopo();
		
		if(topo.equals("integer") && subTopo.equals("integer")) {
			this.atualizarPcT("integer");
		
		} else if(topo.equals("real") && subTopo.equals("real")) {
			this.atualizarPcT("real");
		
		} else if(topo.equals("integer") && subTopo.equals("real")) {
			this.atualizarPcT("real");
		
		} else if(topo.equals("real") && subTopo.equals("integer")) {
			this.atualizarPcT("real");
		
		} else
			this.lancarExcessao(new SemanticoException("Erro! Incompatibilidade de tipos na operação "
										+ "aritmética da linha " + linha + "."));
	}
	
	public void verificarCompatibilidadeTiposParaOperacaoLogica(int linha) {
		String topo = this.pilhaPcT.topo();
		String subTopo = this.pilhaPcT.subTopo();
		
		if(topo.equals("boolean") && subTopo.equals("boolean")) {
			this.atualizarPcT("boolean");
		
		} else
			this.lancarExcessao(new SemanticoException("Erro! Incompatibilidade de tipos na operação "
										+ "lógica da linha " + linha + "."));
	}
	
	public void verificarCompatibilidadeTiposParaOperacaoRelacional(int linha) {
		String topo = this.pilhaPcT.topo();
		String subTopo = this.pilhaPcT.subTopo();
		
		if(topo.equals("integer") && subTopo.equals("integer")) {
			this.atualizarPcT("boolean");
		
		} else if(topo.equals("real") && subTopo.equals("real")) {
			this.atualizarPcT("boolean");
		
		} else if(topo.equals("integer") && subTopo.equals("real")) {
			this.atualizarPcT("boolean");
		
		} else if(topo.equals("real") && subTopo.equals("integer")) {
			this.atualizarPcT("boolean");
		
		//} else if(topo.equals("boolean") && subTopo.equals("boolean")) {
		//	this.atualizarPcT("boolean");
		
		} else
			this.lancarExcessao(new SemanticoException("Erro! Incompatibilidade de tipos na operação "
										+ "relacional da linha " + linha + "."));
	}
	
	public void adicionarTipoParametroAtivacaoProcedimento(String tipo) {
		this.tiposParametrosUltimaAticacaoProcedimento.add(tipo);
	}
	
	public void verificarAtivacaoProcedimento(int linha) {
		
		Procedimento procedimento = (Procedimento) this.ultimoProcedimentoUsado;
		if(procedimento.getQuantidadeParametros() == this.tiposParametrosUltimaAticacaoProcedimento.size()) {
		
			for(int i = 0; i < procedimento.getQuantidadeParametros(); i++) {
				String tipoEsperado = procedimento.getParametro(i).getTipo();
				String tipoUsadoNaChamada = this.tiposParametrosUltimaAticacaoProcedimento.get(i);
				
				if(!tipoEsperado.equals(tipoUsadoNaChamada))
					this.lancarExcessao(new SemanticoException("Erro semântico na linha " + linha
												+ ". Incompatibilidade de tipos para o parâmetro número " + (i+1)
												+ " na chamada do procedimento " + procedimento.getToken()));
			}
		
		} else
			this.lancarExcessao(new SemanticoException("Erro semântico na linha " + linha
										+ ". Número errado de parâmetros na chamada do procedimento "
										+ procedimento.getToken()));
		
		this.tiposParametrosUltimaAticacaoProcedimento.clear();
	}
}
