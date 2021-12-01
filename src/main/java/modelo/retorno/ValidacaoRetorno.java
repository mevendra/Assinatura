package modelo.retorno;

public class ValidacaoRetorno {
	String resultado;
	
	public ValidacaoRetorno(boolean valido) {
		if (valido) {
			this.resultado = "VALIDO";
		} else {
			this.resultado = "INVALIDO";
		}
	}
	
	public String getResultado() {
		return resultado;
	}

}
