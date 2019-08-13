package forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import dao.Articoli;
import dao.Giacenze;
import dao.Sconti;
import vo.Giacenza;
import vo.MovimentoGiacenza;

public class EditGiacenza extends Edit {

   private static final long serialVersionUID = 1L;

   private Collection listArticoli = null;
   private Collection dettagli = null;
   private Giacenza giacenza;
   private String idArticoli = null;

   public Giacenza getGiacenza() {
		return giacenza;
	}

	public void setGiacenza(Giacenza giacenza) {
		this.giacenza = giacenza;
	}

	public Collection getDettagli() {
		return dettagli;
	}

	public void setDettagli(Collection dettagli) {
		this.dettagli = dettagli;
	}
   
   @SuppressWarnings("unchecked")
   public Collection getListArticoli() {
		if (listArticoli == null) {
			try {
	  			listArticoli = new ArrayList();
	  			listArticoli.addAll(new Articoli().getElements());
			} catch (Exception e) {
				return null;
			}
		}
		return listArticoli;
	}
	
	public void setListArticoli(Collection listArticoli) {
		this.listArticoli = listArticoli;
	}
	
	public String getIdArticoli(){
		return idArticoli;
	}

	public void setIdArticoli(String idArticoli){
		this.idArticoli = idArticoli;
	}

	@SuppressWarnings("unchecked")
	public String input() {
  		try {
			if (getAction().equalsIgnoreCase("delete")) {
				return delete();
			}
			
			if (getAction().equalsIgnoreCase("deleteBulk")) {
				return deleteBulk();
			}
  			
  			if (getAction().equalsIgnoreCase("edit")) {
  				Giacenze giacenze = new Giacenze();
  				giacenza = giacenze.find(id);
  				giacenze.completeReferences(giacenza);
  				Iterator itr = giacenza.getMovimenti().iterator();
  				while (itr.hasNext()) {
  					giacenze.completeReferences((MovimentoGiacenza) itr.next());
  				}
  			}
  			
  			 if (getAction().equalsIgnoreCase("dettagli")) {
  				 dettagli = new Giacenze().getDettagliPerArticolo(id);
  				 return "dettagli";
  			 }

			} catch (Exception e) {
				stampaErrore("EditGiacenza.input()",e);
				return ERROR;
			}
  		return INPUT;
	 }
	 
	 protected String store() {
	 	try {
	 		new Giacenze().store(giacenza);
		} catch (Exception e) {
			stampaErrore("EditGiacenza.store()",e);
			return ERROR;
		}
	 	return SUCCESS;
	 }
	 
	 protected String delete() {
	 	try {
	 		Giacenza giacenza = new Giacenza();
	 		giacenza.setId(id);
	 		new Giacenze().delete(giacenza);
		} catch (Exception e) {
			stampaErrore("EditGiacenza.delete()",e);
			return ERROR;
		}
	 	return SUCCESS;
	 }
	
	 protected String deleteBulk() {
		try {
			Giacenze giacenze = new Giacenze();
			
			List<String> idsList = Arrays.asList(idArticoli.split("-"));
			if(idsList != null && !idsList.isEmpty()){
				giacenze.setQueryDeleteBulkByCriteriaIdArticoli(idsList);
				giacenze.deleteBulk();
			}
		} catch (Exception e) {
			stampaErrore("EditGiacenza.deleteBulk()", e);
			return ERROR_DELETE;
		}
		return SUCCESS;
	}
}