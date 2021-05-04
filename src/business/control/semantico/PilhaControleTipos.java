package business.control.semantico;

import java.util.ArrayList;
import java.util.EmptyStackException;

class PilhaControleTipos {
	private ArrayList<String> elementos;
	
	public PilhaControleTipos() {
		this.elementos = new ArrayList<String>();
	}
	
	//public int size() {
	//	return this.elementos.size();
	//}
	
	public void clear() {
		this.elementos.clear();
	}
	
	public String topo() throws EmptyStackException {
		if(!this.elementos.isEmpty())
			return this.elementos.get(this.elementos.size() - 1);
		
		throw new EmptyStackException();
	}
	
	public String subTopo() throws IndexOutOfBoundsException {
		if(this.elementos.size() > 1)
			return this.elementos.get(this.elementos.size() - 2);
		
		throw new IndexOutOfBoundsException();
	}
	
	public void push(String item) {
		this.elementos.add(item);
	}
	
	public String pop() throws EmptyStackException {
		if(!this.elementos.isEmpty())
			return this.elementos.remove(this.elementos.size() - 1);
		
		throw new EmptyStackException();
	}
}
