package business.model;

import java.util.ArrayList;

public class Procedimento extends Identificador {
	private ArrayList<Identificador> parametros;
	
	public Procedimento(String token){
		super(token);
		this.parametros = new ArrayList<Identificador>();
	}
	
	public void adicionarParametro(Identificador parametro) {
		this.parametros.add(parametro);
	}
	
	public Identificador getParametro(int index) {
		if(!(index < 0 || index >= this.parametros.size()))
			return this.parametros.get(index);
		
		throw new IndexOutOfBoundsException();
	}
	
	public int getQuantidadeParametros() {
		return this.parametros.size();
	}
}
