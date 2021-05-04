package main;

import business.control.lexico.Lexico;
import business.control.sintatico.Sintatico;
import util.ComentarioException;
import util.IdentificadorException;
import util.SimboloException;
import util.SintaticoException;

public class Main {

	public static void main(String[] args) {
		
		try {
			Lexico.obterInstancia().analiseLexica("pascal7.txt");
		
		} catch(ComentarioException | SimboloException | IdentificadorException e) {
			System.err.println(e);
			System.exit(-1);
		}
		
		try {
			Sintatico.obterInstancia().analiseSintatica();
		
		} catch(SintaticoException e) {
			System.err.println(e);
			System.exit(-2);
		}
		
		System.out.println("Programa encerrado.");
	}

}
