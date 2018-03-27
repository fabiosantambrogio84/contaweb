 package vo;

public class Telefonata extends VOElement {

	private static final long serialVersionUID = -7089278446121908253L;
	
	private Integer idCliente;
	private Integer idAutista;
	private Integer idPuntoConsegna;
	private Integer prefissoTelefono1;
	private String numeroTelefono1;
	private Integer prefissoTelefono2;
	private String numeroTelefono2;
	private Integer prefissoTelefono3;
	private String numeroTelefono3;
	private String note;
	private Integer giorno;
	private Integer orario;
	private Boolean eseguita;
	private Autista autista;
	private Cliente cliente;
	private PuntoConsegna puntoConsegna;

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Integer getGiorno() {
		return giorno;
	}

	public void setGiorno(Integer giorno) {
		this.giorno = giorno;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Integer getIdPuntoConsegna() {
		return idPuntoConsegna;
	}

	public void setIdPuntoConsegna(Integer idPuntoConsegna) {
		this.idPuntoConsegna = idPuntoConsegna;
	}

	public Integer getPrefissoTelefono1() {
		if (prefissoTelefono1 == null)
			return 39;

		return prefissoTelefono1;
	}

	public void setPrefissoTelefono1(Integer prefissoTelefono1) {
		this.prefissoTelefono1 = prefissoTelefono1;
	}

	public String getNumeroTelefono1() {
		return numeroTelefono1;
	}

	public void setNumeroTelefono1(String numeroTelefono1) {
		this.numeroTelefono1 = numeroTelefono1;
	}

	public Integer getPrefissoTelefono2() {
		if (prefissoTelefono2 == null)
			return 39;

		return prefissoTelefono2;
	}

	public void setPrefissoTelefono2(Integer prefissoTelefono2) {
		this.prefissoTelefono2 = prefissoTelefono2;
	}

	public String getNumeroTelefono2() {
		return numeroTelefono2;
	}

	public void setNumeroTelefono2(String numeroTelefono2) {
		this.numeroTelefono2 = numeroTelefono2;
	}

	public Integer getPrefissoTelefono3() {
		if (prefissoTelefono3 == null)
			return 39;

		return prefissoTelefono3;
	}

	public void setPrefissoTelefono3(Integer prefissoTelefono3) {
		this.prefissoTelefono3 = prefissoTelefono3;
	}

	public String getNumeroTelefono3() {
		return numeroTelefono3;
	}

	public void setNumeroTelefono3(String numeroTelefono3) {
		this.numeroTelefono3 = numeroTelefono3;
	}
	
	public String getTelefono1Completo() {
		return prefissoTelefono1 + numeroTelefono1;
	}
	
	public String getTelefono2Completo() {
		return prefissoTelefono2 + numeroTelefono2;
	}
	
	public String getTelefono3Completo() {
		return prefissoTelefono3 + numeroTelefono3;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public PuntoConsegna getPuntoConsegna() {
		return puntoConsegna;
	}

	public void setPuntoConsegna(PuntoConsegna puntoConsegna) {
		this.puntoConsegna = puntoConsegna;
	}

	public Integer getOrario() {
		return orario;
	}

	public void setOrario(Integer orario) {
		this.orario = orario;
	}

	public Boolean getEseguita() {
		return eseguita;
	}

	public void setEseguita(Boolean eseguita) {
		this.eseguita = eseguita;
	}

	public Autista getAutista() {
		return autista;
	}

	public void setAutista(Autista autista) {
		this.autista = autista;
	}

	public Integer getIdAutista() {
		return idAutista;
	}

	public void setIdAutista(Integer idAutista) {
		this.idAutista = idAutista;
	}
}
