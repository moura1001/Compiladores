package business.model;

public class Token {
	private String token, classificacao;
	private int linha;
	
	public Token(String t, String c, int l){
		token = t;
		classificacao = c;
		linha = l;
	}

	public String getToken() {
		return token;
	}

	public String getClassificacao() {
		return classificacao;
	}

	public int getLinha() {
		return linha;
	}

}
