package forms;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import dao.Clienti;
import dao.DDTs;
import dao.DataAccessException;
import dao.Fatture;
import dao.Ordini;
import dao.Pagamenti;
import dao.PagamentiEseguiti;
import vo.Cliente;
import vo.DDT;
import vo.DettaglioDDT;
import vo.Fattura;
import vo.PagamentoEseguito;

public class EditPagamentoEseguito extends Edit {
    private static final long serialVersionUID = 1L;
    private PagamentoEseguito pagamentoEseguito;
    private Collection listPagamenti = null;

    public String ddt() throws DataAccessException {
        if ("Salva".equals(getAction())) {
            DDTs ddts = new DDTs();
            DDT ddt = new DDT();
            ddt.setId(this.id);
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
            new DDTs().pagato(ddt, this.pagamentoEseguito);

            return "success";
        }
        try {
            DDT ddt = new DDT();
            ddt.setId(this.id);
            ddt = (DDT) new DDTs().findWithAllReferences(ddt);

            this.pagamentoEseguito = new PagamentoEseguito();
            this.pagamentoEseguito.setData(new Date());
            this.pagamentoEseguito.setDescrizione("Pagamento " + ddt.getDescrizioneBreveDDT());
            this.pagamentoEseguito.setImporto(ddt.calcolaTotale().subtract(ddt.getAcconto()));

            Cliente c = new Clienti().find(ddt.getIdCliente().intValue());
            this.pagamentoEseguito.setIdPagamento(c.getIdPagamento());
            this.pagamentoEseguito.setIdCliente(c.getId());
        } catch (Exception e) {
            stampaErrore("EditPagamentoEseguito.input()", e);
            return "error";
        }
        return "input";
    }

    public String getDDT() {
        try {
            DDTs ddts = new DDTs();
            DDT ddt = new DDT();
            ddt.setId(this.id);
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
            return "error";
        }
        return "success";
    }

    public String fattura() throws DataAccessException {
        if ("Salva".equals(getAction())) {
            Fattura fattura = new Fattura();
            fattura.setId(this.id);
            fattura = (Fattura) new Fatture().findWithAllReferences(fattura);
            DDTs ddts = new DDTs();
            Iterator itr = fattura.getDettagliFattura().iterator();
            while (itr.hasNext()) {
                ddts.completeReferences((DDT) itr.next());
            }
            fattura.calcolaTotali();

            new Fatture().pagato(fattura, this.pagamentoEseguito);

            return "success";
        }
        try {
            Fattura fattura = new Fattura();
            fattura.setId(this.id);
            fattura = (Fattura) new Fatture().findWithAllReferences(fattura);

            fattura.calcolaTotali();
            this.pagamentoEseguito = new PagamentoEseguito();
            this.pagamentoEseguito.setData(new Date());
            this.pagamentoEseguito.setDescrizione("Pagamento " + fattura.getDescrizioneBreveDDT());
            this.pagamentoEseguito.setImporto(fattura.getTotaleFattura().subtract(fattura.getAcconto()));

            Cliente c = new Clienti().find(fattura.getIdCliente().intValue());
            this.pagamentoEseguito.setIdPagamento(c.getIdPagamento());
            this.pagamentoEseguito.setIdCliente(c.getId());
        } catch (Exception e) {
            stampaErrore("EditPagamentoEseguito.input()", e);
            return "error";
        }
        return "input";
    }

    public void setListPagamenti(Collection listPagamenti) {
        this.listPagamenti = listPagamenti;
    }

    @Override
    public String input() throws DataAccessException {
        if (this.id != null) {
            try {
                if (getAction().equalsIgnoreCase("delete")) {
                    return delete();
                }
                if (getAction().equalsIgnoreCase("edit")) {
                    this.pagamentoEseguito = new PagamentoEseguito();
                    this.pagamentoEseguito.setId(this.id);
                    this.pagamentoEseguito = ((PagamentoEseguito) new PagamentiEseguiti()
                            .findWithAllReferences(this.pagamentoEseguito));
                }
            } catch (Exception e) {
                stampaErrore("EditOrdine.input()", e);
                return "error";
            }
        }
        return "input";
    }

    public Collection getListPagamenti() {
        if (this.listPagamenti == null) {
            try {
                this.listPagamenti = new Pagamenti().getElements();
            } catch (Exception e) {
                return null;
            }
        }
        return this.listPagamenti;
    }

    public void setPagamentoEseguito(PagamentoEseguito pagamentoEseguito) {
        this.pagamentoEseguito = pagamentoEseguito;
    }

    public PagamentoEseguito getPagamentoEseguito() {
        return this.pagamentoEseguito;
    }

    @Override
    protected String store() {
        return null;
    }

    @Override
    protected String delete() {
        try {
            this.pagamentoEseguito = new PagamentoEseguito();
            this.pagamentoEseguito.setId(this.id);
            this.pagamentoEseguito = ((PagamentoEseguito) new PagamentiEseguiti().findWithAllReferences(this.pagamentoEseguito));

            try {
                if (this.pagamentoEseguito.getFattura() != null) {
                    Fattura fattura = new Fattura();
                    fattura.setId(this.pagamentoEseguito.getFattura().getId());
                    fattura = (Fattura) new Fatture().find(fattura);
                    fattura.setAcconto(fattura.getAcconto().subtract(this.pagamentoEseguito.getImporto()));
                    fattura.setPagato(Boolean.valueOf(false));
                    new Fatture().store(fattura);

                    new Fatture().completeReferences(fattura);

                    BigDecimal importo = pagamentoEseguito.getImporto();

                    DDTs ddts = new DDTs();
                    @SuppressWarnings("rawtypes")
                    Vector itr = fattura.getDettagliFattura();
                    for (int idx = itr.size() - 1; idx >= 0; idx--) {
                        if (importo.compareTo(new BigDecimal(0)) <= 0)
                            break;

                        DDT ddtFattura = (DDT) itr.elementAt(idx);
                        if (ddtFattura.getAcconto().compareTo(new BigDecimal(0)) <= 0)
                            continue;

                        ddts.completeReferences(ddtFattura);

                        BigDecimal totale = ddtFattura.getAcconto();

                        if (importo.compareTo(totale) >= 0) {
                            ddtFattura.setAcconto(new BigDecimal(0));
                            ddtFattura.setPagato(false);
                            importo = importo.subtract(totale);
                        } else {
                            ddtFattura.setAcconto(ddtFattura.getAcconto().subtract(importo));
                            ddtFattura.setPagato(false);
                            importo = importo.subtract(importo);
                        }

                        if (ddtFattura.getAcconto().floatValue() <= ddtFattura.calcolaTotale().floatValue())
                            ddtFattura.setPagato(false);
                        ddts.store(ddtFattura);
                    }

                }

                if (this.pagamentoEseguito.getDdt() != null) {
                    DDTs ddts = new DDTs();

                    DDT ddt = new DDT();
                    ddt.setId(this.pagamentoEseguito.getDdt().getId());
                    ddt = (DDT) ddts.find(ddt);
                    ddt.setAcconto(ddt.getAcconto().subtract(this.pagamentoEseguito.getImporto()));
                    ddt.setPagato(Boolean.valueOf(false));
                    ddts.completeReferences(ddt);

                    if (ddt.getFattura() != null) {
                        ddt.getFattura().setPagato(false);
                        ddt.getFattura().setAcconto(ddt.getFattura().getAcconto().subtract(pagamentoEseguito.getImporto()));
                        new Fatture().store(ddt.getFattura());
                    }

                    ddts.store(ddt);
                }

            } catch (Exception e) {

            }

            new PagamentiEseguiti().delete(this.pagamentoEseguito);

        } catch (Exception e) {
            stampaErrore("EditOrdine.delete()", e);
            return "error_delete";
        }
        return "success";
    }
}
