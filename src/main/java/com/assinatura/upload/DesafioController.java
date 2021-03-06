package com.assinatura.upload;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.assinatura.erro.ErrorMessage;

import modelo.Arquivo;
import modelo.ArquivoAssinado;
import modelo.Assinatura;
import modelo.Validacao;
import modelo.retorno.AssinaturaRetorno;
import modelo.retorno.ValidacaoRetorno;

@Controller
public class DesafioController {
	Arquivo arq = null;

	@GetMapping("/signatureForm")
	public String signatureForm(Model model) throws IOException {
		System.out.println("signatureForm");

		return "signatureForm";
	}
	
	@GetMapping("/verifyForm")
	public String verifyForm(Model model) throws IOException {
		System.out.println("verifyForm");

		return "verifyForm";
	}
	
	@PostMapping("/signature/")
	public ResponseEntity<AssinaturaRetorno> handleSignature( 
								@RequestParam(required=true) MultipartFile fileToSign,
								@RequestParam(required=true) MultipartFile filePFX,
								@RequestParam(required=true) String alias,
								@RequestParam(required=true) String senha) throws Exception {
		
		
		if (filePFX.getContentType().equals("application/x-pkcs12")) {
			try {
				Arquivo arq = new Arquivo(fileToSign.getBytes());
				Assinatura assina = new Assinatura(filePFX.getBytes(), alias, senha.toCharArray());
				
				byte[] bytes = arq.getString().getBytes();
				CMSSignedData signed = assina.assinar(bytes);
				AssinaturaRetorno ret = new AssinaturaRetorno(signed);
				
				return ResponseEntity.ok(ret);
			} catch (NoSuchAlgorithmException|KeyStoreException|CertificateException|UnrecoverableKeyException|OperatorCreationException|CMSException e) {
				System.out.println(e);
				throw new Exception("Um erro aconteceu durante a cria????o de certificados");
			} catch (IOException e) {
				System.out.println(e);
				throw new Exception("Um erro aconteceu ao lidar com um dos arquivos");
			} catch (Exception e) {
				System.out.println(e);
				throw new Exception("Um erro n??o esperado aconteceu");
			}
		} else {
			throw new Exception("Arquivo PFX n??o est?? no formato correto");
		}
	}
	
	@PostMapping("/verify/")
	public ResponseEntity<ValidacaoRetorno> handleVerify( 	@RequestParam(required=true) MultipartFile fileToVerify) {
		
		boolean ehValida = false;
		try {
			if (fileToVerify.getContentType().equals("application/pkcs7-signature")) {
				ArquivoAssinado arq = new ArquivoAssinado(fileToVerify.getBytes());
				
				ehValida = Validacao.valida(arq);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		ValidacaoRetorno ret = new ValidacaoRetorno(ehValida);
		return ResponseEntity.ok(ret);
	}
	
	@GetMapping("/verify/")
	public String handleVerify() {
		System.out.println("handleVerify");

		return "redirect:/verifyForm";
	}
	
	@GetMapping("/signature/")
	public String handleSignature() {
		System.out.println("handleSignature");

		return "redirect:/signatureForm";
	}
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ErrorMessage handlerException(Exception e) {
		ErrorMessage erro = new ErrorMessage(e.getMessage(), e.getClass().getName());		
		
		return erro;
	}
}
