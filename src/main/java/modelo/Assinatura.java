package modelo;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;

public class Assinatura {
	private String alias;
	private char[] senha;
	    
    private CMSSignedDataGenerator gen;
	
	public Assinatura() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, OperatorCreationException, CMSException {
		char[] senha_ = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
		
		this.senha = senha_;
		this.alias = "f22c0321-1a9a-4877-9295-73092bb9aa94";
		
		iniciar(new FileInputStream(".\\pkcs12\\Desafio Estagio Java.p12"));
	}
	
	public Assinatura(String caminho, String alias, char[] senha) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, OperatorCreationException, CMSException {
		this.alias = alias;
		this.senha = senha;

		iniciar(new FileInputStream(caminho));
	}
	
	public Assinatura(byte[] bytes_caminho, String alias, char[] senha) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, OperatorCreationException, CMSException {
		this.alias = alias;
		this.senha = senha;

		iniciar(new ByteArrayInputStream(bytes_caminho));
	}
	
	private void iniciar(InputStream caminho) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, OperatorCreationException, CMSException {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(caminho, senha);
			
		PrivateKey key = (PrivateKey) keyStore.getKey(alias, senha);
			
		java.security.cert.Certificate cer = keyStore.getCertificate(alias);
		X509CertificateHolder cert = new X509CertificateHolder(cer.getEncoded());
			
		gen = new CMSSignedDataGenerator();
		List certList = new ArrayList();
		certList.add(cert);
		Store certs = new JcaCertStore(certList);
		ContentSigner sha256Signer = new JcaContentSignerBuilder("SHA256withRSA").build((PrivateKey) key);
		gen.addSignerInfoGenerator(
		                new JcaSignerInfoGeneratorBuilder(
		                     new JcaDigestCalculatorProviderBuilder().build())
		                     .build(sha256Signer, cert));
		gen.addCertificates(certs);
	}
	
	
	public CMSSignedData assinar(byte[] mensagem) throws CMSException {
		return assinar(mensagem, true);
	}
	
	public CMSSignedData assinar(byte[] mensagem, boolean encapsulate) throws CMSException {
	    CMSTypedData msg = new CMSProcessableByteArray(mensagem);
	    CMSSignedData signed = gen.generate(msg, encapsulate);
	    
		return signed;
	}
	
}
