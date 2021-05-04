package business.control.semantico;

import java.util.ArrayList;
import java.util.EmptyStackException;

import business.model.Identificador;

class PilhaIdentificadores {
	private ArrayList<Identificador> elementos;
	
	public PilhaIdentificadores() {
		this.elementos = new ArrayList<Identificador>();
	}
	
	public int size() {
		return this.elementos.size();
	}
	
	public Identificador peek() throws EmptyStackException {
		if(!this.elementos.isEmpty())
			return this.elementos.get(this.elementos.size() - 1);
		
		throw new EmptyStackException();
	}
	
	public void push(Identificador item) {
		this.elementos.add(item);
	}
	
	public Identificador pop() throws EmptyStackException {
		if(!this.elementos.isEmpty())
			return this.elementos.remove(this.elementos.size() - 1);
		
		throw new EmptyStackException();
	}
	
	public Identificador procurarAntesDeDeclarar(String item) {
		if(!this.elementos.isEmpty()) {
		
			int index = this.elementos.size() - 1;
			while(!this.elementos.get(index).getToken().equals("$")) {
				if(this.elementos.get(index).getToken().equals(item))
					return this.elementos.get(index);
				index--;
			}
		
		}
		
		return null;
	}
	
	public Identificador procurarAntesDeUsar(String item) {
		if(!this.elementos.isEmpty()) {
		
			int index = this.elementos.size() - 1;
			while(index >= 0) {
				if(this.elementos.get(index).getToken().equals(item))
					return this.elementos.get(index);
				index--;
			}
		
		}
		
		return null;
	}
	
	public Identificador get(int index) {
		if(!(index < 0 || index >= this.elementos.size()))
			return this.elementos.get(index);
		
		throw new IndexOutOfBoundsException();
	}
}
