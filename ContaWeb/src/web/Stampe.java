package web;

import java.util.Collection;
import dao.Clienti;

public class Stampe  extends GenericAction {

	private static final long serialVersionUID = 1L;
	private Collection listaClientiConad = null;
	
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {

		return SUCCESS;
    }
	
	public Collection getListaClientiConad() {
		if (listaClientiConad == null){
			try {
				Clienti gen = new Clienti();
				return gen.getFiltratoConad();
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
		}
		
		return listaClientiConad;
	}

	public void setListaClientiConad(Collection listaClientiConad) {
		this.listaClientiConad = listaClientiConad;
	}
	
}