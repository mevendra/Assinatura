package modelo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;

public class ArquivoAssinado {
	private CMSSignedData assinatura;
	
	public ArquivoAssinado(byte[] bytes) throws CMSException {
		this.assinatura = new CMSSignedData(bytes);
	}
	
	public void salvar(String caminho, String nome) throws IOException { 
		File file = new File(caminho + nome + ".p7s");
	    file.createNewFile();
	    
	    FileOutputStream outputStream = new FileOutputStream(file);
	    outputStream.write(assinatura.getEncoded());
	    
	    outputStream.close();
	}
	
	public CMSSignedData getAssinatura() { return assinatura; }
}
