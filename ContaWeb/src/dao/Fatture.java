package dao;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;

import stampemgr.StampeMgr;
import vo.Cliente;
import vo.DDT;
import vo.DettaglioDDT;
import vo.Fattura;
import vo.PagamentoEseguito;

public class Fatture extends DataAccessObject implements Runnable {

    private static final long serialVersionUID = -246742806256539024L;

    private static Integer avanzamentoProcesso = Integer.valueOf(0);
    private static Boolean processoAttivo = Boolean.valueOf(false);
    private static String messaggioErrore = null;
    private java.util.Date dataLimite = null;

    protected Date filterDataDa = null;
    protected Date filterDataA = null;
    protected String filterStato = null;
    protected String filterPagamento = null;
    protected String filterArticolo = null;
    protected String filterImporto = null;

    public String getFilterPagamento() {
        return filterPagamento;
    }

    public void setFilterPagamento(String filterPagamento) {
        this.filterPagamento = filterPagamento;
    }

    public String getFilterArticolo() {
        return filterArticolo;
    }

    public void setFilterArticolo(String filterArticolo) {
        this.filterArticolo = filterArticolo;
    }

    public String getFilterImporto() {
        return filterImporto;
    }

    public void setFilterImporto(String filterImporto) {
        this.filterImporto = filterImporto;
    }

    public String getFilterStato() {
        return filterStato;
    }

    public void setFilterStato(String filterStato) {
        this.filterStato = filterStato;
    }

    public java.util.Date getDataLimite() {
        return this.dataLimite;
    }

    public void setDataLimite(java.util.Date dataLimite) {
        this.dataLimite = dataLimite;
    }

    public Date getFilterDataDa() {
        return filterDataDa;
    }

    public void setFilterDataDa(Date filterDataDa) {
        this.filterDataDa = filterDataDa;
    }

    public Date getFilterDataA() {
        return filterDataA;
    }

    public void setFilterDataA(Date filterDataA) {
        this.filterDataA = filterDataA;
    }

    public Fatture() {
        this.elementClass = Fattura.class;
    }

    @Override
    protected void setDefaultCriteria() {
        /*
         * *** getQueryByCriteria().addOrderByDescending("idFattura");
         */
        getQueryByCriteria().addOrderByDescending("annoContabile");
        getQueryByCriteria().addOrderByDescending("numeroProgressivo");
    }

    public void setOrderByCliente() {
        this.useDefaultCriteria = false;
        getQueryByCriteria().addOrderByAscending("cliente.rs");
    }

    public void setOrderByNumeroFattura() {
        this.useDefaultCriteria = false;
        getQueryByCriteria().addOrderByAscending("idFattura");
    }

    public void setOrderByData() {
        getQueryByCriteria().addOrderByAscending("data");
    }

    public Collection<?> getPerDataCliente(java.util.Date daData, java.util.Date aData, Integer idCliente) throws DataAccessException {
        Criteria criteria = getCriteria();
        criteria.addEqualTo("cliente.id", idCliente);
        criteria.addBetween("data", daData, aData);
        return getElements();
    }

    @Override
    public Collection<?> getElements() throws DataAccessException {
        setCriteriaUsingFilterKey();
        Collection<?> elements = super.getElements();

        Iterator<?> itr = elements.iterator();
        while (itr.hasNext()) {
            Fattura fattura = (Fattura) itr.next();
            completeReferences(fattura);
            Iterator<?> ddtItr = fattura.getDettagliFattura().iterator();
            while (ddtItr.hasNext()) {
                DDT ddt = (DDT) ddtItr.next();
                completeReferences(ddt);
            }
            fattura.calcolaTotali();
        }
        return elements;
    }

    @Override
    protected void setCriteriaUsingFilterKey() {
        Criteria criteria = getCriteria();
        if (this.filterKey != null && this.filterKey.length() > 0) {
            criteria.addLike("cliente.rs", this.filterKey + "%");
            Criteria criteriaAgente = new Criteria();
            criteriaAgente.addLike("cliente.agente.cognome", this.filterKey + "%");
            criteria.addOrCriteria(criteriaAgente);
            try {
                NumberFormat nf = NumberFormat.getNumberInstance();
                Number number = nf.parse(this.filterKey);
                Criteria criteriaNprog = new Criteria();
                criteriaNprog.addEqualTo("numeroProgressivo", number);
                criteria.addOrCriteria(criteriaNprog);
            } catch (Exception localException1) {
            }
        }

        DateFormat DF = new SimpleDateFormat("yyyy-M-dd", Locale.ITALIAN);
        DF.setLenient(false);
        if (this.filterDataDa != null) {
            criteria.addGreaterOrEqualThan("data", this.filterDataDa);
        }

        if (this.filterDataA != null)
            criteria.addLessOrEqualThan("data", this.filterDataA);

        if (this.filterPagamento != null && this.filterPagamento.length() > 0) {
            criteria.addLike("cliente.pagamento.descrizione", "%" + this.filterPagamento + "%");
        }

        if (this.filterArticolo != null && this.filterArticolo.length() > 0) {
            Criteria subCriteria = new Criteria();
            subCriteria.addPathClass("ddt", DDT.class);
            subCriteria.addEqualToField("ddt.idFattura", Criteria.PARENT_QUERY_PREFIX + "id");
            subCriteria.addLike("descrizioneArticolo", "%" + this.filterArticolo + "%");
            criteria.addExists(new QueryByCriteria(DettaglioDDT.class, subCriteria));
        }

        if (this.filterStato != null && this.filterStato.length() > 0) {
            if (filterStato.equals("01"))
                criteria.addEqualTo("pagato", true);
            else if (filterStato.equals("02"))
                criteria.addEqualTo("pagato", false);
        }
    }

    public String printRiepilogo() throws Exception { // STAMPA LISTA DOCUMENTI
        BigDecimal totale = new BigDecimal(0);
        Collection colFatture = getElements();
        Iterator itr = colFatture.iterator();
        ArrayList listaFatture = new ArrayList();
        while (itr.hasNext()) {
            Fattura fattura = (Fattura) itr.next();
            listaFatture.add(fattura);
            totale = totale.add(fattura.getTotaleFattura());
        }
        DateFormat DF = new SimpleDateFormat("yyyy-M-dd", Locale.ITALIAN);
        DF.setLenient(false);
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.MONTH, -1);
        Date start = c.getTime();
        RiepilogoFatture pf = new RiepilogoFatture();
        if (this.filterDataDa != null)
            pf.setDataDal(getFilterDataDa());
        else
            pf.setDataDal(start);
        if (this.filterDataA != null)
            pf.setDataAl(getFilterDataA());
        else
            pf.setDataAl(today);
        pf.setListaFatture(listaFatture);
        pf.setTotale(totale);
        pf.setTipo("F");
        pdfFile = StampeMgr.getInstance().richiediPDFDocumentoList(pf);
        return "success";
    }

    public void store(Fattura fattura) throws DataAccessException {
        PersistenceBroker broker = null;
        try {
            fattura.setData(new java.sql.Date(fattura.getData().getTime()));
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();
            if (fattura.getAcconto() == null || fattura.getAcconto().compareTo(new BigDecimal(0)) < 0) {
                fattura.setAcconto(BigDecimal.ZERO);
            }

            if (fattura.getSpedito() == null) {
                fattura.setSpedito(false);
            }
            if ((fattura.getNumeroProgressivo() == null) && (fattura.getAnnoContabile() == null)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fattura.getData());
                Integer annoContabile = Integer.valueOf(calendar.get(1));
                fattura.setAnnoContabile(annoContabile);
                Criteria cri = new Criteria();
                cri.addEqualTo("annoContabile", annoContabile);
                ReportQueryByCriteria q = QueryFactory.newReportQuery(Fattura.class, cri);
                q.setAttributes(new String[] { "max(numeroProgressivo)" });
                Iterator iter = broker.getReportQueryIteratorByQuery(q);
                Object[] obj = (Object[]) iter.next();
                if (obj[0] != null) {
                    Integer n = (Integer) obj[0];
                    fattura.setNumeroProgressivo(n = Integer.valueOf(n.intValue() + 1));
                } else {
                    fattura.setNumeroProgressivo(Integer.valueOf(1));
                }
            }
            broker.store(fattura);
            if (fattura.getDettagliFattura() != null) {
                Iterator itr = fattura.getDettagliFattura().iterator();
                while (itr.hasNext()) {
                    DDT ddt = (DDT) itr.next();
                    ddt.setDettagliDDT(null);
                    if (fattura.getPagato().booleanValue())
                        ddt.setPagato(Boolean.valueOf(true));
                    broker.store(ddt);
                }
            }
            broker.commitTransaction();
            broker.close();
        } catch (Exception e) {
            broker.abortTransaction();
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null) {
                broker.close();
            }
        }
    }

    public void pagato(Fattura fattura, PagamentoEseguito pagamentoEseguito) throws DataAccessException {
        PersistenceBroker broker = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();

            BigDecimal acconto = fattura.getAcconto();
            BigDecimal total = fattura.getAcconto().add(pagamentoEseguito.getImporto());
            fattura.setAcconto(total);
            if (total.compareTo(fattura.getTotaleFattura()) >= 0) {
                if (acconto.compareTo(new BigDecimal(0)) == 1) {
                    pagamentoEseguito.setDescrizione("Saldo " + fattura.getDescrizioneBreveDDT());
                } else {
                    pagamentoEseguito.setDescrizione("Pagamento " + fattura.getDescrizioneBreveDDT());
                }
                fattura.setPagato(Boolean.valueOf(true));
            } else {
                pagamentoEseguito.setDescrizione("Acconto " + fattura.getDescrizioneBreveDDT());
                fattura.setPagato(Boolean.valueOf(false));
            }
            pagamentoEseguito.setFattura(fattura);
            pagamentoEseguito.setIdFattura(fattura.getId());
            new PagamentiEseguiti().store(pagamentoEseguito);
            store(fattura);
            new Fatture().completeReferences(fattura);
            BigDecimal importo = pagamentoEseguito.getImporto();
            Iterator itr = fattura.getDettagliFattura().iterator();
            while (itr.hasNext()) {
                DDT ddtFattura = (DDT) itr.next();
                if (importo.compareTo(new BigDecimal(0)) <= 0)
                    break;
                if (ddtFattura.getPagato())
                    continue;
                BigDecimal totale = ddtFattura.calcolaTotale();
                BigDecimal resto = totale.subtract(ddtFattura.getAcconto());
                if (importo.compareTo(resto) >= 0) {
                    ddtFattura.setAcconto(totale);
                    ddtFattura.setPagato(true);
                    importo = importo.subtract(resto);
                } else {
                    ddtFattura.setAcconto(ddtFattura.getAcconto().add(importo));
                    ddtFattura.setPagato(false);
                    importo = importo.subtract(importo);
                }
                new DDTs().completeReferences(ddtFattura);
                new DDTs().store(ddtFattura);
            }
            broker.commitTransaction();
            broker.close();
        } catch (Exception e) {
            broker.abortTransaction();
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null) {
                broker.close();
            }
        }
    }

    public void nonpagato(Fattura fattura, PagamentoEseguito pagamentoEseguito) throws DataAccessException {
        PersistenceBroker broker = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();
            fattura.setPagato(Boolean.valueOf(false));
            store(fattura);
            if (pagamentoEseguito.getId() != null) {
                new PagamentiEseguiti().delete(pagamentoEseguito);
            }
            broker.commitTransaction();
            broker.close();
        } catch (Exception e) {
            broker.abortTransaction();
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null) {
                broker.close();
            }
        }
    }

    public void delete(Fattura fattura) throws DataAccessException {
        PersistenceBroker broker = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            fattura = (Fattura) completeReferences(fattura);
            broker.beginTransaction();
            Iterator itr = fattura.getDettagliFattura().iterator();
            DDTs ddts = new DDTs();
            while (itr.hasNext()) {
                DDT ddt = (DDT) itr.next();
                ddt = ddts.completeReferences(ddt);
                ddt.setFattura(null);
                ddt.setIdFattura(null);
                broker.store(ddt);
            }
            broker.delete(fattura);
            broker.commitTransaction();
            broker.close();
        } catch (Exception e) {
            broker.abortTransaction();
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null) {
                broker.close();
            }
        }
    }

    public Collection<?> getDDTsFatturabili(Cliente cliente, java.util.Date dataLimite) throws DataAccessException {
        PersistenceBroker broker = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            Criteria criteria = new Criteria();
            criteria.addLessOrEqualThan("data", dataLimite);
            criteria.addColumnIsNull("idFattura");
            criteria.addColumnEqualTo("idCliente", cliente.getId());
            Query query = QueryFactory.newQuery(DDT.class, criteria);
            return broker.getCollectionByQuery(query);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null) {
                broker.close();
            }
        }
    }

    public Collection<?> getFatture(java.util.Date dataInizio, java.util.Date dataFine) throws DataAccessException {
        getCriteria().addBetween("data", dataInizio, dataFine);
        return getElements();
    }

    public Collection<?> getFattureNonPagate(java.util.Date dataInizio, java.util.Date dataFine) throws DataAccessException {
        getCriteria().addBetween("data", dataInizio, dataFine);
        getCriteria().addEqualTo("pagato", Boolean.valueOf(false));
        return getElements();
    }

    public Collection<?> getFattureNoMailNoPEC(java.util.Date dataInizio, java.util.Date dataFine) throws DataAccessException {
        getCriteria().addBetween("data", dataInizio, dataFine);
        getCriteria().addEqualTo("spedito", 0);
        Criteria criteriaPec1 = new Criteria();
        Criteria criteriaPec2 = new Criteria();
        criteriaPec1.addIsNull("cliente.emailPec");
        criteriaPec2.addEqualTo("cliente.emailPec", "");
        criteriaPec1.addOrCriteria(criteriaPec2);
        Criteria criteriaEmail1 = new Criteria();
        Criteria criteriaEmail2 = new Criteria();
        criteriaEmail1.addIsNull("cliente.email");
        criteriaEmail2.addEqualTo("cliente.email", "");
        criteriaEmail1.addOrCriteria(criteriaEmail2);
        getCriteria().addAndCriteria(criteriaPec1);
        getCriteria().addAndCriteria(criteriaEmail1);
        return getElements();
    }

    public Collection<?> getFatturePEC(java.util.Date dataInizio, java.util.Date dataFine) throws DataAccessException {
        getCriteria().addBetween("data", dataInizio, dataFine);
        getCriteria().addEqualTo("spedito", 0);
        getCriteria().addNotNull("cliente.emailPec");
        getCriteria().addNotEqualTo("cliente.emailPec", "");
        return getElements();
    }

    public Collection<?> getFattureMail(java.util.Date dataInizio, java.util.Date dataFine) throws DataAccessException {
        getCriteria().addBetween("data", dataInizio, dataFine);
        getCriteria().addEqualTo("spedito", 0);
        getCriteria().addNotNull("cliente.email");
        getCriteria().addNotEqualTo("cliente.email", "");
        return getElements();
    }

    public Integer getAvanzamentoProcesso() {
        return avanzamentoProcesso;
    }

    public Boolean getProcessoAttivo() {
        return processoAttivo;
    }

    public void processaFatture() {
        processoAttivo = Boolean.valueOf(true);
        messaggioErrore = null;
        try {
            Fatture fatture = new Fatture();
            DDTs ddts = new DDTs();
            Clienti clienti = new Clienti();
            clienti.setOrderByDescrizione(ORDER_ASC);
            Collection collection = clienti.getElements();
            Iterator itr = collection.iterator();
            int n = collection.size();
            int i = 0;
            while (itr.hasNext()) {
                Cliente cliente = (Cliente) itr.next();
                Collection cddts = fatture.getDDTsFatturabili(cliente, this.dataLimite);
                if (cddts.size() > 0) {
                    Fattura fattura = new Fattura();
                    fattura.setIdCliente(cliente.getId());
                    fattura.setCliente(cliente);
                    fattura.setData(this.dataLimite);
                    fattura.setCausale("vendita");
                    fattura.setNoteFattura("");
                    fattura.setSpedito(false);
                    fattura.setPagato(Boolean.valueOf(false));
                    fattura.setSconto(new BigDecimal(0));
                    Vector vector = new Vector();
                    Iterator itr2 = cddts.iterator();
                    Boolean pagata = true;
                    while (itr2.hasNext()) {
                        DDT ddt = (DDT) itr2.next();
                        ddts.completeReferences(ddt);
                        ddt.setFattura(fattura);
                        vector.add(ddt);
                        if (!ddt.getPagato())
                            pagata = false;
                    }
                    fattura.setPagato(pagata);
                    fattura.setDettagliFattura(vector);
                    fatture.store(fattura);
                }
                i++;
                avanzamentoProcesso = Integer.valueOf(Math.round(i / n * 100.0F));
            }
        } catch (Exception e1) {
            messaggioErrore = "Errore nella creazione delle fatture";
        }
        processoAttivo = Boolean.valueOf(false);
        avanzamentoProcesso = Integer.valueOf(0);
    }

    @Override
    public void run() {
        processaFatture();
    }

    public String getMessaggioErrore() {
        return messaggioErrore;
    }
}
