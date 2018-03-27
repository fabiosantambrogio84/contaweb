package dao;

import java.util.Collection;

import vo.Cliente;
import vo.PuntoConsegna;

public class PuntiConsegna extends DataAccessObject {
	
	private Integer idCliente;
	
	public PuntiConsegna(Integer idCliente) {
		this.elementClass = PuntoConsegna.class;
		this.idCliente = idCliente;
	}

	public PuntiConsegna() { 
		this.elementClass = PuntoConsegna.class;
	}
	
	public PuntoConsegna find(int idPuntoConsegna) throws DataAccessException {
		PuntoConsegna puntoConsegna = new PuntoConsegna();
		puntoConsegna.setId(idPuntoConsegna);
		puntoConsegna = (PuntoConsegna)findWithAllReferences(puntoConsegna);
		return puntoConsegna;
	}
	
	public PuntoConsegna findPrimaryByCliente(int index) throws DataAccessException {
		
		Collection<PuntoConsegna> puntoconsegna;
		setDefaultCriteria();
		puntoconsegna = getElements();
		Object[] arrPuntiConsegna = puntoconsegna.toArray();
		if(arrPuntiConsegna.length <= index)
			index = 0;
		
		return (PuntoConsegna) puntoconsegna.toArray()[index];
	}
	
	protected void setDefaultCriteria() {
		if (idCliente != null)
			getCriteria().addColumnEqualTo("idCliente", idCliente);
	}
}
