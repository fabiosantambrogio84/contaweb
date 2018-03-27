package vo;

public class PuntoConsegna extends VOElement {

	private static final long serialVersionUID = -4481268223848364996L;

	private Integer idCliente;
	private Cliente cliente;
	private String nome;
	private String indirizzo;
	private String cap;
	private String localita;
	private String prov;
	private Boolean attivato;
	private String codConad;

	public Boolean getAttivato() {
		return attivato;
	}

	public void setAttivato(Boolean attivato) {
		this.attivato = attivato;
	}

	public Cliente getCliente() {
		return cliente;
	}
	
	public String getNomeLocalita() {
		if (localita != null)
			return nome + " - " + localita;
		else return nome;
	}
	
	public String getCap() {
		return cap;
	}
	
	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getIndirizzo() {
		return indirizzo;
	}
	
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	
	public String getLocalita() {
		return localita;
	}
	
	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getCodConad() {
		return codConad;
	}

	public void setCodConad(String codConad) {
		this.codConad = codConad;
	}
}
