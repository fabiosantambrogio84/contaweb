package vo;

import java.util.Date;

public class Autista extends VOElement {

	private static final long serialVersionUID = 1L;
	
	private String nome;
	private String cognome;
	private String telefono;

	public String getNominativo() {
		return nome + " " + cognome;		
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCognome() {
		return cognome;
	}
	
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

}
