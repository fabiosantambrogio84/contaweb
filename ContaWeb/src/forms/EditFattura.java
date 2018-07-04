package forms;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import dao.Clienti;
import dao.DDTs;
import dao.DataAccessException;
import dao.Fatture;
import stampemgr.StampeMgr;
import vo.Cliente;
import vo.DDT;
import vo.Fattura;

public class EditFattura extends Edit {

    private static final long serialVersionUID = 1L;

    private Integer id = null;
    private Collection listClienti = null;
    private BigDecimal totaleFattura = null;

    private Fattura fattura = null;

    private Collection listaDDTs = null;

    public Collection getListaDDTs() {
        return listaDDTs;
    }

    public void setListaDDTs(Collection listaDDTs) {
        this.listaDDTs = listaDDTs;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String input() {
        try {
            if (getAction().equalsIgnoreCase("delete")) {
                return delete();
            }

            if (getAction().equalsIgnoreCase("edit")) {
                Fatture fatture = new Fatture();
                Fattura fattura = new Fattura();
                fattura.setId(id);
                this.fattura = (Fattura) fatture.findWithAllReferences(fattura);
                // CARICO I DDT
                DDTs ddts = new DDTs();
                Iterator itr = this.fattura.getDettagliFattura().iterator();
                while (itr.hasNext())
                    ddts.completeReferences((DDT) itr.next());
                this.fattura.calcolaTotali();
                return "input_read_only";
            }

        } catch (Exception e) {
            stampaErrore("EditFattura.input()", e);
            return ERROR;
        }
        return INPUT;
    }

    @SuppressWarnings("unchecked")
    public String aggiornaDDTs() {
        try {
            if (fattura == null) {
                listaDDTs = new ArrayList();
                return SUCCESS;
            }

            Fatture fatture = new Fatture();
            Cliente cliente = new Cliente();
            cliente.setId(fattura.getIdCliente());
            listaDDTs = fatture.getDDTsFatturabili(cliente, fattura.getData());
        } catch (Exception e) {
            stampaErrore("EditFattura.aggiornaDDTs()", e);
            return ERROR;
        }
        return SUCCESS;
    }

    public String calcolaTotaleFattura() throws DataAccessException {
        totaleFattura = new BigDecimal(0).setScale(2);

        if ((listaDDTs != null) && (listaDDTs.size() != 0)) {
            DDTs ddts = new DDTs();
            Iterator itr = listaDDTs.iterator();
            while (itr.hasNext()) {
                Integer idDDT = Integer.valueOf((String) itr.next());
                DDT ddt = new DDT();
                ddt.setId(idDDT);
                ddt = (DDT) ddts.findWithAllReferences(ddt);
                totaleFattura = totaleFattura.add(ddt.calcolaTotale());
            }
        }

        // APPLICO LO SCONTO FINE FATTURA
        if (fattura != null && fattura.getSconto() != null && fattura.getSconto().compareTo(new BigDecimal(0)) != 0) {
            BigDecimal sconto;
            sconto = totaleFattura.multiply(fattura.getSconto().divide(new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_UP))
                    .setScale(2, BigDecimal.ROUND_HALF_UP));
            sconto = sconto.setScale(2, BigDecimal.ROUND_HALF_UP);
            totaleFattura = totaleFattura.subtract(sconto);

        }

        return SUCCESS;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected String store() {
        try {
            Fatture fatture = new Fatture();
            if (getAction().equalsIgnoreCase("insert")) {
                // IMPOSTO I DDTS
                if (listaDDTs == null)
                    throw new Exception("Nessun DDT selezionato");

                // CARICO IL CLIENTE
                fattura.setCliente(new Clienti().find(fattura.getIdCliente()));
                Vector dettagliFattura = new Vector();
                Iterator itr = listaDDTs.iterator();
                DDTs ddts = new DDTs();
                boolean pagata = true;
                BigDecimal acconto = new BigDecimal(0);
                while (itr.hasNext()) {
                    String idDDT = (String) itr.next();
                    DDT ddt = new DDT();
                    ddt.setId(Integer.valueOf(idDDT));
                    ddt = (DDT) ddts.findWithAllReferences(ddt);
                    ddt.setFattura(fattura);
                    dettagliFattura.add(ddt);
                    if (ddt.getPagato() == null || !ddt.getPagato())
                        pagata = false;
                    acconto.add(ddt.getAcconto());
                }
                fattura.setPagato(pagata);
                fattura.setDettagliFattura(dettagliFattura);

                if (fattura.getSconto() == null)
                    fattura.setSconto(new BigDecimal(0));
            } else {
                Boolean pagato = fattura.getPagato();
                Integer numero = fattura.getNumeroProgressivo();
                Fattura fattura = new Fattura();
                fattura.setId(this.fattura.getId());
                this.fattura = (Fattura) fatture.find(fattura);
                this.fattura.setPagato(pagato);
                this.fattura.setNumeroProgressivo(numero);
            }

            fatture.store(fattura);
        } catch (SQLException e) {
            addActionError("Errore: numero di fattura gia esistente!");
            return ERROR;
        } catch (Exception e) {
            stampaErrore("EditFattura.store()", e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    protected String delete() {
        try {
            Fattura fattura = new Fattura();
            fattura.setId(id);
            Fatture fatture = new Fatture();
            fattura = (Fattura) fatture.findWithAllReferences(fattura);
            // CANCELLO IL PDF
            StampeMgr.getInstance().cancellaPDFDocumento(fattura);
            new Fatture().delete(fattura);
        } catch (Exception e) {
            stampaErrore("EditFattura.delete()", e);
            return ERROR_DELETE;
        }
        return SUCCESS;
    }

    public Collection getListClienti() {
        if (listClienti == null) {
            try {
                listClienti = new Clienti().getElements();
            } catch (Exception e) {
                return null;
            }
        }
        return listClienti;
    }

    public void setListClienti(Collection listClienti) {
        this.listClienti = listClienti;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Fattura getFattura() {
        return fattura;
    }

    public void setFattura(Fattura fattura) {
        this.fattura = fattura;
    }

    public BigDecimal getTotaleFattura() {
        return totaleFattura;
    }

    public void setTotaleFattura(BigDecimal totaleFattura) {
        this.totaleFattura = totaleFattura;
    }
}