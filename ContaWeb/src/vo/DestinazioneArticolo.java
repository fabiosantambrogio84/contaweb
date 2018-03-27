package vo;

import java.util.Vector;

public class DestinazioneArticolo extends VOElement {

	private static final long serialVersionUID = 1L;

	private String nome;
	private Integer posizione;
	private java.util.Collection articoli = null;


	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public  java.util.Collection getArticoli() {
		return articoli;
	}

	public void setArticoli( java.util.Collection articoli) {
		this.articoli = articoli;
	}

	public Integer getPosizione() {
		return posizione;
	}

	public void setPosizione(Integer posizione) {
		this.posizione = posizione;
	}

	
}
