package vo;

public class Configurazione extends VOElement {

	private static final long serialVersionUID = 1L;
	
	static public final int NORMAL = 0;
	static public final int WARNING = 1;
	static public final int ERROR = 2;
	static public final int STORED = 3;
	static public final int NOT_ACTIVE = 4;

	private String elemento;
	private String value;

	public Configurazione() {
	}
	
	public Integer getStato() {
		return NORMAL;
	}
	
	public String getElemento() {
		return elemento;
	}

	public void setElemento(String elemento) {
		this.elemento = elemento;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
