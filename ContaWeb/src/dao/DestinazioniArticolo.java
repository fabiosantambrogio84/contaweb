package dao;

import vo.DestinazioneArticolo;


@SuppressWarnings("serial")
public class DestinazioniArticolo extends DataAccessObject {
	
	public void store(DestinazioneArticolo vo) throws DataAccessException{
		super.store(vo);
	}
	
	public DestinazioniArticolo(){
		this.elementClass = DestinazioneArticolo.class;
	}
	
	public DestinazioneArticolo find(int id) throws DataAccessException {
		DestinazioneArticolo autista = new DestinazioneArticolo();
		autista.setId(id);
		autista = (DestinazioneArticolo)find(autista);
		return autista;
	}
	
	public void setOrderByDescrizione(int order){
		if (order == ORDER_DESC)
			getQueryByCriteria().addOrderByDescending("posizione");
		else
			getQueryByCriteria().addOrderByAscending("posizione");
	}
	
	protected void setDefaultCriteria() {
		setOrderByDescrizione(ORDER_ASC);
	}

}
