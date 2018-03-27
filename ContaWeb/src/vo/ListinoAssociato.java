package vo;

public class ListinoAssociato extends VOElement {

	private static final long serialVersionUID = -3104370428732288500L;

	private Integer idListino;
	private int idCliente;
	private int idFornitore;
	private Fornitore fornitore;
	private Cliente cliente;
	private Listino listino;

	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
		this.idCliente = cliente.getId();
	}
	public Fornitore getFornitore() {
		return fornitore;
	}
	public void setFornitore(Fornitore fornitore) {
		this.fornitore = fornitore;
		this.idFornitore = fornitore.getId();
	}
	public int getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	public int getIdFornitore() {
		return idFornitore;
	}
	public void setIdFornitore(int idFornitore) {
		this.idFornitore = idFornitore;
	}
	public Integer getIdListino() {
		return idListino;
	}
	public void setIdListino(Integer idListino) {
		this.idListino = idListino;
	}
	public Listino getListino() {
		return listino;
	}
	public void setListino(Listino listino) {
		this.listino = listino;
		this.idListino = listino.getId();
	}
}
