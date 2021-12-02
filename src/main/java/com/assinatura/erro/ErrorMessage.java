package com.assinatura.erro;

public class ErrorMessage {
	private String erro;
	private String classError;
	public ErrorMessage(String erro, String classError) {
		this.classError = classError;
		this.erro = erro;
	}
	
	public String getErro() { return erro; }
	public String getClassError() { return classError; }
}
