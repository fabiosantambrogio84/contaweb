package web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.ojb.broker.query.Criteria;

import vo.Articolo;
import vo.BollaAcquisto;
import vo.DettaglioBollaAcquisto;
import vo.DettaglioOrdine;
import vo.Fornitore;
import dao.Articoli;
import dao.BolleAcquisto;
import dao.DataAccessException;
import dao.Fornitori;
import dao.Ordini;

public class AppletController extends GenericAction {
	private static final long serialVersionUID = 1L;

	private Date date = null;
	private Integer idPuntoConsegna = null;
	private Integer idFornitore = null;
	
	private Object id = null;
	
	private BollaAcquisto bollaAcquisto = null;
	
	public BollaAcquisto getBollaAcquisto() {
		return bollaAcquisto;
	}

	public void setBollaAcquisto(BollaAcquisto bollaAcquisto) {
		this.bollaAcquisto = bollaAcquisto;
	}

	public Integer getIdFornitore() {
		return idFornitore;
	}

	public void setIdFornitore(Integer idFornitore) {
		this.idFornitore = idFornitore;
	}
	
	public String listaFornitori() {
		try {
			setSerializedObject(new Fornitori().getElements());
		} catch (DataAccessException e) {
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String getFornitore() {
		try {
			Fornitore fornitore = new Fornitore();
			fornitore.setId(Integer.parseInt(((String[])id)[0]));
			setSerializedObject(new Fornitori().findWithAllReferences(fornitore));
		} catch (Exception e) {
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String getObjBollaAcquisto() {
		try {
			BollaAcquisto ba = new BollaAcquisto();
			ba.setId(Integer.parseInt(((String[])id)[0]));
			BolleAcquisto bolle = new BolleAcquisto();
			ba = (BollaAcquisto) bolle.findWithAllReferences(ba);
			Iterator itr = ba.getInventario().iterator();
			while (itr.hasNext()) {
				bolle.completeReferences((DettaglioBollaAcquisto) itr.next());
			}
			setSerializedObject(ba);
		} catch (Exception e) {
			return ERROR;
		}
		return SUCCESS;
	}

	public String getArticolo() {
		try {
			Articolo articolo = new Articolo();
			articolo.setCodiceArticolo(((String[]) id)[0]);
			articolo.setIdFornitore(idFornitore);
			articolo = (Articolo) new Articoli().findWithAllReferences(articolo);
			if (articolo == null)
				throw new Exception();
			setSerializedObject(articolo);
		} catch (Exception e) {
			setSerializedObject(e);
		}
		return SUCCESS;
	}
	
	public String getArticoloByBarCode() {
		try {
			
			String barcode = ((String[]) id)[0];
			String barcodeIncomplete = barcode.substring(0, barcode.length() - 6);
			
			Articolo articolo = new Articolo();
			articolo.setBarCode(barcode);
			articolo.setIdFornitore(idFornitore);
			articolo = (Articolo) new Articoli().findWithAllReferences(articolo);
			
			if (articolo == null) 
				articolo.setBarCode(barcodeIncomplete);
			articolo = (Articolo) new Articoli().findWithAllReferences(articolo);
			
			if (articolo == null) 
				throw new Exception();
			
			
			setSerializedObject(articolo);
		} catch (Exception e) {
			setSerializedObject(e);
		}
		return SUCCESS;
	}
	
	public String getDettagliOrdini() {
		/* GET LIST DETTAGLI USING IDCLIENTE,IDPUNTOCONSEGNA AND DATE */
		Collection<DettaglioOrdine> col = null;		
		try {
			if (idPuntoConsegna != null && idPuntoConsegna != -1)
				col = new Ordini().getDettagliPerCliente(Integer.parseInt(((String[])id)[0]),idPuntoConsegna, date);
			else
				col = new Ordini().getDettagliPerCliente(Integer.parseInt(((String[])id)[0]),null, date);
			if (col == null)
				col = new ArrayList<DettaglioOrdine>();
			setSerializedObject(col);
		} catch (Exception e) {
			setSerializedObject(e);
		}
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String listaArticoli() {
		try {
			Collection articoli = null;
			if (idFornitore == null)
				articoli = new Articoli().getElements();
			else
				articoli = new Articoli().getElements(idFornitore);

			setSerializedObject(articoli);
		} catch (DataAccessException e) {
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String saveBollaAcquisto() {
		try {
			new BolleAcquisto().store(bollaAcquisto);
			setSerializedObject(bollaAcquisto.getId());
		} catch (DataAccessException e) {
			stampaErrore("AppletController.saveBollaAcquisto()",e);
			setSerializedObject(new Exception("DB: Errore nel salvataggio del ddt."));
		}
		return SUCCESS;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String cancRigaOrdine() {
		try {
			DettaglioOrdine det = new DettaglioOrdine();
			det.setId(Integer.parseInt(((String[])id)[0]));
			
			Ordini ordini = new Ordini();
			det = (DettaglioOrdine) ordini.find(det);
			ordini.delete(det);
			
			setSerializedObject(new Boolean(true));
		} catch (Exception e) {
			stampaErrore("AppletController.cancRigaOrdine()",e);
			setSerializedObject(new Boolean(false));
		}
		return SUCCESS;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Integer getIdPuntoConsegna() {
		return idPuntoConsegna;
	}

	public void setIdPuntoConsegna(Integer idPuntoConsegna) {
		this.idPuntoConsegna = idPuntoConsegna;
	}
}
