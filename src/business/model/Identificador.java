package business.model;

public class Identificador {
	private String token, tipo;
	private boolean temTipo;
	
	public Identificador(String token){
		this.token = token;
		this.tipo = "desconhecido";
	}
	
	public Identificador(String token, boolean marcacao){
		this(token);
		this.temTipo = marcacao;
	}
	
	public String getToken() {
		return this.token;
	}

	public String getTipo() {
		return this.tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isMarcado() {
		return this.temTipo;
	}
	
	public void marcar() {
		this.temTipo = true;
	}
}
