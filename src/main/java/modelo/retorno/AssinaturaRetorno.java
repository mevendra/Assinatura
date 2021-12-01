package modelo.retorno;

import java.io.IOException;
import java.util.Base64;

import org.bouncycastle.cms.CMSSignedData;

public class AssinaturaRetorno {
	private String conteudo;
	private String base64;
	
	public AssinaturaRetorno(CMSSignedData signed) throws IOException {
		this.conteudo = signed.getSignedContent().toString();
		this.base64 = Base64.getEncoder().encodeToString(signed.getEncoded());
	}
	public AssinaturaRetorno() {
		this.conteudo = "oi";
		this.base64 = "a";
	}

	public String getConteudo() {
		return conteudo;
	}

	public String getBase64() {
		return base64;
	}
	
	
}
