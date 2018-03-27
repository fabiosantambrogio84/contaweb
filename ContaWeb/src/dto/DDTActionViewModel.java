package dto;

import java.io.Serializable;

public class DDTActionViewModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DDTViewModel ddt;
	private java.util.List<DettaglioDDTViewModel> art;
	
	public DDTViewModel getDdt() {
		return ddt;
	}
	public void setDdt(DDTViewModel ddt) {
		this.ddt = ddt;
	}
	public java.util.List<DettaglioDDTViewModel> getArt() {
		return art;
	}
	public void setArt(java.util.List<DettaglioDDTViewModel> art) {
		this.art = art;
	}
}
