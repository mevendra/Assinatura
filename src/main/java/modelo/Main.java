package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;

import org.bouncycastle.cms.CMSSignedData;

public class Main {

	public static void main(String[] args) {
		try {
			//Obtenção do resumo criptografico utilizando SHA-256
			Arquivo arquivo = new Arquivo(".\\arquivos\\doc.txt");
			String resumoHex = arquivo.getString();
			System.out.println("Resumo Criptografico: \n" + resumoHex);
			
			//Anexar resumo a documento
			File novoArquivo = new File(".\\arquivos\\resumoHex.txt");
			novoArquivo.createNewFile();	    
			FileOutputStream outputStream = new FileOutputStream(novoArquivo);
		    outputStream.write(resumoHex.getBytes());
			System.out.println("Resumo salvo em: " + novoArquivo.getPath());
		    
		    //Gerando uma assinatura digital	
			String caminho = ".\\pkcs12\\Desafio Estagio Java.p12";
			String alias = "f22c0321-1a9a-4877-9295-73092bb9aa94";
		    char[] senha = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
			Assinatura assina = new Assinatura(caminho, alias, senha);
			
			//Assinando o documento
			CMSSignedData signed = assina.assinar(arquivo.getBytes(), true);
			System.out.println("Assinatura: " + signed.toString());
			
			//Salvando documento
			ArquivoAssinado arq_assinado = new ArquivoAssinado(signed.getEncoded());
			arq_assinado.salvar(".\\arquivos\\", "arquivo_assinado");
			System.out.println("Documento assinado salvo");
			
			//Verificando assinatura
			boolean verifica = Validacao.valida(signed);
			System.out.println("A assinatura é: " + verifica);
			
			//Verificando assinatura a partir do documento salvo
			File file_salvo = new File(".\\arquivos\\arquivo_assinado.p7s");
			ArquivoAssinado arq_salvo = new ArquivoAssinado(Files.readAllBytes(file_salvo.toPath()));
			boolean verifica_salvo = Validacao.valida(arq_salvo);
			System.out.println("A assinatura do arquivo é: " + verifica_salvo);
			
		    
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
