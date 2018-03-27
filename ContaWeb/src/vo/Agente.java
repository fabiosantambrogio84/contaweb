package vo;

public class Agente extends VOElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nome;
	private String cognome;
	private String telefono;
	private String email;
	private String indirizzo;

	public String getNominativo()
	{
		return nome + " " + cognome;		
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/* ***
	public String getData()
	{
		return this.data;
	}
	  
	public void setData(String data)
	{
		this.data = data;
  	}
	*** */ 
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	
}
