package modelo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Arquivo {
	private byte[] bytes;
	private String hex;
	
	public Arquivo(String nome) throws IOException, NoSuchAlgorithmException {
        File file = new File(nome);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        
		MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
		this.bytes = algorithm.digest(fileContent);
		
		StringBuilder hexString = new StringBuilder();
	    for (byte b : this.bytes) {
	    	hexString.append(String.format("%02X", 0xFF & b));
	    }
	    this.hex = hexString.toString();
	}
	
	public Arquivo(byte[] bytes) throws IOException, NoSuchAlgorithmException {
        byte[] fileContent = bytes;
        
		MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
		this.bytes = algorithm.digest(fileContent);
		
		StringBuilder hexString = new StringBuilder();
	    for (byte b : this.bytes) {
	    	hexString.append(String.format("%02X", 0xFF & b));
	    }
	    this.hex = hexString.toString();
	}

	public String getString() { return hex; }
	public byte[] getBytes() { return bytes; }
}
