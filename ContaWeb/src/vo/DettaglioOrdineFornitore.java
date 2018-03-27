package vo;

import java.util.Collection;
import java.util.Vector;

public class DettaglioOrdineFornitore extends VOElement {

	private static final long serialVersionUID = 2767346114394280662L;

	private Integer idOrdineFornitore = null;
	private Integer idArticolo = null;
	private Integer pezziOrdinati = null;
	// private Integer PrezziDaListini = 1;

	private OrdineFornitore ordineFornitore = null;
	private Articolo articolo = null;
	private Collection richiesteOrdini = null;
	// private Vector prezziDaListini= new Vector();

	// private Integer indexPrezziDaListini = 0;
	
	public OrdineFornitore getOrdineFornitore() {
		return ordineFornitore;
	}
	public void setOrdineFornitore(OrdineFornitore ordineFornitore) {
		this.ordineFornitore = ordineFornitore;
	}
	public Articolo getArticolo() {
		return articolo;
	}
	public void setArticolo(Articolo articolo) {
		this.articolo = articolo;
	}
	public Integer getIdOrdineFornitore() {
		return idOrdineFornitore;
	}
	public void setIdOrdineFornitore(Integer idOrdineFornitore) {
		this.idOrdineFornitore = idOrdineFornitore;
	}
	public Integer getIdArticolo() {
		return idArticolo;
	}
	public void setIdArticolo(Integer idArticolo) {
		this.idArticolo = idArticolo;
	}
	public Integer getPezziOrdinati() {
		return pezziOrdinati;
	}
	public void setPezziOrdinati(Integer pezziOrdinati) {
		this.pezziOrdinati = pezziOrdinati;
	}
	
	public Collection getRichiesteOrdini() {
		return richiesteOrdini;
	}
	public void setRichiesteOrdini(Collection richiesteOrdini) {
		this.richiesteOrdini = richiesteOrdini;
	}
	
	// public Integer getPrezziDaListini() {
	// 	return PrezziDaListini;
	// }

	/* @SuppressWarnings("unused")
	public String getPrezziDaListini() {
		String tmpString = "-";
		if (indexPrezziDaListini > prezziDaListini.size())
			return tmpString;
		tmpString = prezziDaListini.get(indexPrezziDaListini).toString();
		indexPrezziDaListini++;
		return tmpString;
	}
	
	private String evaluatePrezziDaListini() throws DataAccessException {
		if (ordine.getId() != null) {
			indexPrezziDaListini = 0;
			Listini listini = new Listini();
			
            //ArrayList<PrezzoConSconto> prezziDaListini = new ArrayList();
            PrezzoConSconto tmpPrezzoConSconto = null;
            DettaglioOrdine tmpDettaglioOrdine = null;
            Articolo tmpArticolo = new Articolo();
            Cliente tmpcliente = new Cliente();
            
			for(int i = 0 ; i < dettagliOrdine.size(); i++) {
				tmpDettaglioOrdine = (DettaglioOrdine) dettagliOrdine.get(i);
				tmpArticolo = tmpDettaglioOrdine.getArticolo();
				tmpArticolo = (Articolo) new Articoli().findWithAllReferences(tmpArticolo);
				tmpcliente = ordine.getCliente();
				tmpcliente = (Cliente) new Clienti().findWithAllReferences(tmpcliente);
				
				tmpPrezzoConSconto = listini.getPrezzoConSconto(tmpArticolo, tmpcliente, dataSpedizione);
				
				if(tmpPrezzoConSconto != null)
					prezziDaListini.add(tmpPrezzoConSconto.getPrezzo());
			}
			
			if (dettagliOrdine.size() != prezziDaListini.size())
				return ERROR;
		}
		
		return SUCCESS;
	} */	
}
