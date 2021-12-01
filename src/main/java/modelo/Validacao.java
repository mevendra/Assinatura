package modelo;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;

public class Validacao {
	

	public static boolean valida(ArquivoAssinado arq) throws CertificateException, OperatorCreationException, CMSException {
		return valida(arq.getAssinatura());
	}
	
	public static boolean valida(CMSSignedData signed) throws CertificateException, OperatorCreationException, CMSException {
		Store store = signed.getCertificates(); 
    	SignerInformationStore signers = signed.getSignerInfos(); 
    	Collection c = signers.getSigners(); 
        Iterator it = c.iterator();
        
        boolean retorno = false;
        
        while (it.hasNext()) { 
            SignerInformation signer = (SignerInformation) it.next(); 
            Collection certCollection = store.getMatches(signer.getSID()); 
            Iterator certIt = certCollection.iterator();
            X509CertificateHolder certHolder = (X509CertificateHolder) certIt.next();
            X509Certificate cert = new JcaX509CertificateConverter().getCertificate(certHolder);
            if (signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(cert))) {
                retorno = true;
            } else {
            	retorno = false;
            	break;
            }
        }
		return retorno;
	}
}
