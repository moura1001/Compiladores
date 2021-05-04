package infra;

//import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
//import java.io.FileReader;
//import java.io.FileWriter;
import java.io.IOException;

import business.control.lexico.Lexico;
//import business.model.Token;
import util.ComentarioException;
import util.IdentificadorException;
import util.SimboloException;

public class LeitorPascal {
	
	private static LeitorPascal singleton;

	private static final String DIRETORIO = "recursos/";
	
	//private String arquivoLexico;
	//private boolean primeiraLinha;
	//private BufferedReader bufferedReader;
	
	private LeitorPascal(){
		//this.primeiraLinha = true;
		//this.bufferedReader = null;
	}
	
	public static LeitorPascal obterInstancia() {
        if (singleton == null) {
        	singleton = new LeitorPascal();
        }
        return singleton;
    }

	public void lerArquivoPascal(String nomeArquivo) throws SimboloException, IdentificadorException, ComentarioException {

		//this.arquivoLexico = DIRETORIO + nomeArquivo.split("\\.")[0] + ".lexico";
		
		FileInputStream arquivo = null;
		File f = new File(DIRETORIO + nomeArquivo);

		try {
			arquivo = new FileInputStream(f);
		
		} catch (IOException e) {
			System.err.println(e);
		}

		int r;
		char caracter;

		try {
			while ((r = arquivo.read()) != -1) {

				caracter = (char) r;
				// System.out.println(caracter);
				try {
					Lexico.obterInstancia().consumirCaracter(caracter);

				} catch (SimboloException | IdentificadorException | ComentarioException e) {
					throw e;
				}
			}

		} catch (IOException e) {
			System.err.println("Erro ao tentar ler o arquivo");
		
		} finally {
			try {
				
				if(arquivo != null)
					arquivo.close();
			
			} catch(IOException e) {
				System.err.println(e);
			}
		}

	}

	/*public void salvarArquivoTokens(Token token) {
		
		FileWriter fileWriter = null;
		BufferedWriter out = null;
		
		try {
						
			if(this.primeiraLinha){
				fileWriter = new FileWriter(this.arquivoLexico);
				this.primeiraLinha = false;
			
			} else
				fileWriter = new FileWriter(this.arquivoLexico, true);
			
			out = new BufferedWriter(fileWriter);

			String vToken = token.getToken();
			String classificacao = token.getClassificacao();
			// String linha = token.getLinha();
			out.write(vToken + "\t" + classificacao + "\t" + token.getLinha() + "\n");
			out.flush();

		} catch (IOException e) {
			System.err.println(e);
		
		} finally {
			try {
				
				if(out != null)
					out.close();
			
			} catch(IOException e) {
				System.err.println(e);
			}
		}
	}*/

	/*public Token lerArquivoTokens() {
		
		try {
			if(this.bufferedReader == null)
				this.bufferedReader = new BufferedReader(new FileReader(this.arquivoLexico));
			
			String linhaAtual;
			if((linhaAtual = this.bufferedReader.readLine()) != null) {
				String[] t = linhaAtual.split("[\\t\\n]");
				String vToken = t[0];
				String classificacao = t[1];
				int linha = Integer.valueOf(t[2]);
				
				return new Token(vToken, classificacao, linha);
			
			} else
				this.bufferedReader.close();
		
		} catch (IOException e) {
			System.err.println(e);
		}
		
		return null;
	}*/
	
	//public String getNomeArquivo() {
	//	return this.arquivoLexico;
	//}

}
