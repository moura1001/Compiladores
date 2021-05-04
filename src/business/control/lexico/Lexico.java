package business.control.lexico;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import infra.LeitorPascal;
import business.control.lexico.estados.Estado;
import business.control.lexico.estados.EstadoComentario;
import business.control.lexico.estados.EstadoIdentificador;
import business.control.lexico.estados.EstadoInicial;
import business.model.Token;
import util.ComentarioException;
import util.IdentificadorException;
import util.SimboloException;

public class Lexico {
	private static Lexico singleton;
	
	private Map<String, String> palavrasReservadas;
	
	private int contadorLinha;
	
	private String bufferEntrada;
	
	private Estado estado;
	
	private List<Token> tokens;
	private int posicaoProximoToken;
	
	private Token tokenComentarioErro;
	
	private Lexico(){
		this.iniciarPalavrasReservadas();
		this.contadorLinha = 1;
		this.bufferEntrada = "";
		this.estado = new EstadoInicial();
		this.tokens = new ArrayList<Token>();
		this.posicaoProximoToken = 0;
		this.tokenComentarioErro = null;
	}
	
	private void iniciarPalavrasReservadas(){
		this.palavrasReservadas = new HashMap<String, String>();
		this.palavrasReservadas.put("program", "Palavra chave");
		this.palavrasReservadas.put("var", "Palavra chave");
		this.palavrasReservadas.put("integer", "Palavra chave");
		this.palavrasReservadas.put("real", "Palavra chave");
		this.palavrasReservadas.put("boolean", "Palavra chave");
		this.palavrasReservadas.put("procedure", "Palavra chave");
		this.palavrasReservadas.put("begin", "Palavra chave");
		this.palavrasReservadas.put("end", "Palavra chave");
		this.palavrasReservadas.put("if", "Palavra chave");
		this.palavrasReservadas.put("then", "Palavra chave");
		this.palavrasReservadas.put("else", "Palavra chave");
		this.palavrasReservadas.put("while", "Palavra chave");
		this.palavrasReservadas.put("do", "Palavra chave");
		this.palavrasReservadas.put("not", "Palavra chave");
		this.palavrasReservadas.put("or", "Operador aditivo");
		this.palavrasReservadas.put("and", "Operador multiplicativo");
		this.palavrasReservadas.put("true", "Booleano");
		this.palavrasReservadas.put("false", "Booleano");
    }
	
	public static Lexico obterInstancia() {
        if (singleton == null) {
        	singleton = new Lexico();
        }
        return singleton;
    }
	
	private void formatarTabela(){
		String leftAlignFormat = "| %-16s | %-23s | %-6d |%n";

		System.out.format("+------------------+-------------------------+--------+%n");
		System.out.format("|      Token       |      Classificação      | Linha  |%n");
		System.out.format("+------------------+-------------------------+--------+%n");
		for (int i = 0; i < this.tokens.size(); i++) {
			Token token = this.tokens.get(i);
		    System.out.format(leftAlignFormat, token.getToken(), token.getClassificacao(), token.getLinha());
		}
		System.out.format("+------------------+-------------------------+--------+%n");
		System.out.println("\n");
	}
	
	public void analiseLexica(String nomeArquivo) throws ComentarioException, SimboloException, IdentificadorException{
		
		try {
			LeitorPascal.obterInstancia().lerArquivoPascal(nomeArquivo);
		
		} catch(SimboloException | IdentificadorException e){
			throw e;
		}
		
		this.estado = this.estado.lerCaractereDescarte(' ');
		
		if(this.estado instanceof EstadoComentario) {
			String m = "Erro! Comentário " + this.tokenComentarioErro.getToken() +
						" aberto na contadorLinha " + this.tokenComentarioErro.getLinha() +
						" e não fechado.";
			throw new ComentarioException(m);
		}
		
		this.formatarTabela();
		//System.out.println("Arquivo de tokens criado com sucesso.");
		System.out.println("Análise lexica completada com sucesso.");
		
	}
	
	public void consumirCaracter(char caracter) throws SimboloException, IdentificadorException, ComentarioException{
		
		String c = Character.toString(caracter);
		
		if(c.matches("[\r\n\t ]")){
			this.estado = this.estado.lerCaractereDescarte(caracter);			
		
		} else if(c.matches("[{]"))
			this.estado = this.estado.lerCaractereComentarioAbre(caracter);
		
		else if(c.matches("[}]")){
			if(!(this.estado instanceof EstadoComentario)) {
				String m = "Erro! Comentário } fechado na contadorLinha " + this.contadorLinha +
						" e não aberto.";
				throw new ComentarioException(m);
			}
			this.estado = this.estado.lerCaractereComentarioFecha(caracter);
		}
		
		else if(c.matches("[a-zA-Z]"))
			this.estado = this.estado.lerCaractereAlfabetico(caracter);
		
		else if(c.matches("[_]")){
			if(!(this.estado instanceof EstadoIdentificador) && !(this.estado instanceof EstadoComentario)) {
				String m = "Erro! Identificador na contadorLinha " + this.contadorLinha +
						" não pode começar com '_' e sim com uma letra.";
				throw new IdentificadorException(m);
			}
			this.estado = this.estado.lerCaractereAlfabetico(caracter);
		}
		
		else if(c.matches("[0-9]"))
			this.estado = this.estado.lerCaractereNumerico(caracter);
		
		else if(c.matches("[;.:(),]"))
			this.estado = this.estado.lerCaractereDelimitador(caracter);
		
		else if(c.matches("[=<>]"))
			this.estado = this.estado.lerCaractereRelacional(caracter);
		
		else if(c.matches("[+-]"))
			this.estado = this.estado.lerCaractereAditivo(caracter);
		
		else if(c.matches("[*/]"))
			this.estado = this.estado.lerCaractereMultiplicativo(caracter);
		
		else if(!(this.estado instanceof EstadoComentario)) {
			String m = "Erro! Símbolo " + caracter +
					" na contadorLinha " + this.contadorLinha +
					" não pertence a linguagem.";
			throw new SimboloException(m);
		}
	}
	
	public void incrementarContadorLinha() {
		this.contadorLinha++;
	}
	
	public int getContadorLinha(){
		return this.contadorLinha;
	}
	
	public String getBufferEntrada(){
		return this.bufferEntrada;
	}
	
	public void setBufferEntrada(String valor){
		this.bufferEntrada = valor;
	}
	
	public void adicionarToken(Token token){
		this.tokens.add(token);
	}
	
	public Token getProximoToken() {
		return this.tokens.get(this.posicaoProximoToken++);
	}
	
	public void retrocederPosicaoProximoToken() {
		this.posicaoProximoToken--;
	}
	
	//public void avancarPosicaoProximoToken() {
	//	this.posicaoProximoToken++;
	//}
	
	public boolean isPalavraReservada(String palavra) {
		return this.palavrasReservadas.containsKey(palavra);
	}
	
	public String getClassificacaoPalavraReservada(String palavra) {
		return this.palavrasReservadas.get(palavra);
	}
	
	public void setTokenComentarioErro(Token token) {
		this.tokenComentarioErro = token;
	}

}
