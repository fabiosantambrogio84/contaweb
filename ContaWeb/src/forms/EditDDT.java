package forms;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import dao.Articoli;
import dao.Autisti;
import dao.Clienti;
import dao.DDTs;
import dao.DataAccessException;
import dao.Fatture;
import dao.Ivas;
import dao.Listini;
import dao.Ordini;
import dao.Settings;
import vo.Articolo;
import vo.Autista;
import vo.Cliente;
import vo.DDT;
import vo.DettaglioDDT;
import vo.Fattura;
import vo.Ordine;
import vo.PuntoConsegna;

public class EditDDT extends Edit {
    private static final long serialVersionUID = 1L;

    private Integer id = null;
    private Integer idAutista = null;
    private Integer idDDT = null;
    private Integer idCliente = null;
    private Integer idOrdineCliente = null;
    private String codiceArticolo = null;
    private String barcode = null;
    private Date data = null;
    private DDT ddt = null;
    private String appletUrl = null;

    public String getAppletUrl() {
        return appletUrl;
    }

    public void setAppletUrl(String appletUrl) {
        this.appletUrl = appletUrl;
    }

    public DDT getDdt() {
        return ddt;
    }

    public void setDdt(DDT ddt) {
        this.ddt = ddt;
    }

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String input() {
        try {
            appletUrl = Settings.getInstance().getValue("applet.url");
            if (getAction().equalsIgnoreCase("delete")) {
                return delete();
            }
            if (getAction().equalsIgnoreCase("insertWithOrdine")) {
                idOrdineCliente = id;
                id = null;
            }
        } catch (Exception e) {
            stampaErrore("EditDDT.input()", e);
            return ERROR;
        }

        return INPUT;
    }

    @Override
    public String store() {
        try {
            DDTs ddts = new DDTs();
            ddts.store(ddt);
            // SERIALIZZO L'ID PER L'APPLET
            setSerializedObject(ddt.getId());
        } catch (Exception e) {
            stampaErrore("EditDDT.store()", e);
            e.printStackTrace();
            setSerializedObject(new Exception("DB: Errore nel salvataggio del ddt."));
        }
        return SUCCESS;
    }

    public String store2() {
        try {
            DDTs ddts = new DDTs();
            ddts.store(ddt, false);
            // SERIALIZZO L'ID PER L'APPLET
            setSerializedObject(ddt.getId());
        } catch (Exception e) {
            stampaErrore("EditDDT.store()", e);
            e.printStackTrace();
            setSerializedObject(new Exception("DB: Errore nel salvataggio del ddt."));
        }
        return SUCCESS;
    }

    public void storeAutista() {
        try {

            Autista autista = new Autista();

            if (idAutista == null) {
                autista = null;
            } else {
                autista.setId(idAutista);
                Autisti autisti = new Autisti();
                autista = (Autista) autisti.findWithAllReferences(autista);
            }

            DDT ddtVecchio = new DDT();
            ddtVecchio.setId(idDDT);
            ddtVecchio = (DDT) new DDTs().find(ddtVecchio);
            ddtVecchio.setAutista(autista);
            ddtVecchio.setIdAutista(idAutista);

            if (ddtVecchio.getIdFattura() != null) {
                Fattura fattura = new Fattura();
                fattura.setId(ddtVecchio.getIdFattura());
                Fatture fatture = new Fatture();
                Fattura fatt = (Fattura) fatture.find(fattura);
                ddtVecchio.setFattura(fatt);
            }

            new DDTs().store(ddtVecchio, false);

            if (ddtVecchio.getIdOrdine() != null) {
                Ordine ordineVecchio = new Ordine();
                ordineVecchio.setId(ddtVecchio.getIdOrdine());
                ordineVecchio = (Ordine) new Ordini().find(ordineVecchio);
                if (ordineVecchio != null) {
                    ordineVecchio.setAutista(autista);
                    ordineVecchio.setIdAutista(idAutista);
                    new Ordini().store(ordineVecchio);
                }
            }

        } catch (Exception e) {
            stampaErrore("EditDDT.storeAutista()", e);
        }
    }

    @Override
    protected String delete() {
        try {
            DDT ddt = new DDT();
            ddt.setId(id);
            new DDTs().delete(ddt);
        } catch (Exception e) {
            stampaErrore("EditDDT.delete()", e);
            return ERROR_DELETE;
        }
        return SUCCESS;
    }

    protected String delete2() {
        try {
            DDT ddt = new DDT();
            ddt.setId(id);
            new DDTs().delete(ddt, false);
        } catch (Exception e) {
            stampaErrore("EditDDT.delete()", e);
            return ERROR_DELETE;
        }
        return SUCCESS;
    }

    /* ----------------- AZIONI RICHIESTE DALLA APPLET -------------------- */
    @SuppressWarnings("unchecked")
    public String listaClienti() {
        try {
            Collection clienti = new Clienti().getListaPerDDT();
            setSerializedObject(clienti);
        } catch (DataAccessException e) {
            return ERROR;
        }
        return SUCCESS;
    }

    @SuppressWarnings("unchecked")
    public String listaIva() {
        try {
            Collection ivas = new Ivas().getElements();
            setSerializedObject(ivas);
        } catch (DataAccessException e) {
            return ERROR;
        }
        return SUCCESS;
    }

    public String getCliente() {
        try {
            Cliente cliente = new Cliente();
            cliente.setId(idCliente);
            cliente = (Cliente) new Clienti().findWithAllReferences(cliente);

            // Cancella dal cliente tutti i punti di consegna non attivi
            Iterator<PuntoConsegna> itr = cliente.getPuntiConsegna().iterator();
            while (itr.hasNext()) {
                if (!itr.next().getAttivato())
                    itr.remove();
            }

            setSerializedObject(cliente);
        } catch (DataAccessException e) {
            return ERROR;
        }
        return SUCCESS;
    }

    public String getDDT() {
        try {
            DDTs ddts = new DDTs();
            DDT ddt = new DDT();
            ddt.setId(id);
            ddt = (DDT) ddts.findWithAllReferences(ddt);
            new Clienti().completeReferences(ddt.getCliente());
            Iterator<DettaglioDDT> itr = ddt.getDettagliDDT().iterator();
            Ordini ordini = new Ordini();
            while (itr.hasNext()) {
                DettaglioDDT dt = itr.next();
                ddts.completeReferences(dt);
                Collection col = ordini.getDettagliAncoraDaEvadere(dt);
                dt.getEvasioniOrdini().addAll(col);
            }
            setSerializedObject(ddt);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    public String getPrezzoArticolo() {
        try {
            Articolo articolo = new Articolo();
            articolo.setCodiceArticolo(codiceArticolo);
            articolo = (Articolo) new Articoli().findWithAllReferences(articolo);
            Cliente cliente = new Cliente();
            cliente.setId(idCliente);
            cliente = (Cliente) new Clienti().findWithAllReferences(cliente);
            setSerializedObject(new Listini().getPrezzoConSconto(articolo, cliente, data));
        } catch (DataAccessException e) {
            return ERROR;
        }
        return SUCCESS;
    }

    public String getPrezzoArticoloDaBC() {
        try {
            Cliente cliente = new Cliente();
            cliente.setId(idCliente);
            cliente = (Cliente) new Clienti().findWithAllReferences(cliente);
            Articolo articolo = new Articoli().findByBarcode(barcode);
            new Articoli().completeReferences(articolo);
            if (articolo == null) // ARTICOLO NON TROVATO
                throw new Exception("Articolo non trovato");
            setSerializedObject(new Listini().getPrezzoConSconto(articolo, cliente, data));
        } catch (Exception e) {
            setSerializedObject(e);
        }
        return SUCCESS;
    }
    /* --------------- FINE AZIONI RICHIESTE DALLA APPLET ----------------- */

    @Override
    public String execute() throws Exception {

        if (getAction().equalsIgnoreCase("delete2"))
            return delete2();
        return super.execute();
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getIdOrdineCliente() {
        return idOrdineCliente;
    }

    public void setIdOrdineCliente(Integer idOrdineCliente) {
        this.idOrdineCliente = idOrdineCliente;
    }

    public Integer getIdAutista() {
        return idAutista;
    }

    public void setIdAutista(Integer idAutista) {
        this.idAutista = idAutista;
    }

    public Integer getIdDDT() {
        return idDDT;
    }

    public void setIdDDT(Integer idDDT) {
        this.idDDT = idDDT;
    }
}