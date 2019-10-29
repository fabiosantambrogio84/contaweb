package dao;

import java.math.BigDecimal;
import java.sql.Time;
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
import vo.Articolo;
import vo.DDT;
import vo.DettaglioDDT;
import vo.Fattura;
import vo.PagamentoEseguito;
import vo.PuntoConsegna;
import vo.VOElement;

public class DDTs extends DataAccessObject {
    public DDTs() {
        this.elementClass = DDT.class;
    }

    protected Date filterDataDa = null;
    protected Date filterDataA = null;
    protected String filterStato = null;
    protected String filterPagamento = null;
    protected String filterArticolo = null;
    protected String filterAutista = null;
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

    public String getFilterAutista() {
        return filterAutista;
    }

    public void setFilterAutista(String filterAutista) {
        this.filterAutista = filterAutista;
    }

    public String getFilterImporto() {
        return filterImporto;
    }

    public void setFilterImporto(String filterImporto) {
        this.filterImporto = filterImporto;
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

    public String getFilterStato() {
        return filterStato;
    }

    public void setFilterStato(String filterStato) {
        this.filterStato = filterStato;
    }

    public void setOrderByNprog(int order) {
        getQueryByCriteria().addOrderByDescending("annoContabile");
        if (order == ORDER_DESC) {
            getQueryByCriteria().addOrderByDescending("numeroProgressivo");
        } else {
            getQueryByCriteria().addOrderByAscending("numeroProgressivo");
        }
    }

    @Override
    protected void setDefaultCriteria() {
        setOrderByNprog(ORDER_DESC);
    }

    public DDT completeReferences(DDT ddt) throws DataAccessException {
        DDT ddt2 = (DDT) super.completeReferences(ddt);

        Iterator itr = ddt2.getDettagliDDT().iterator();
        while (itr.hasNext()) {
            DettaglioDDT dt = (DettaglioDDT) itr.next();
            super.completeReferences(dt);
        }
        return ddt2;
    }

    @Override
    public Collection getElements() throws DataAccessException {
        setCriteriaUsingFilterKey();
        Collection elements = super.getElements();
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
        if (this.filterDataDa != null)
            criteria.addGreaterOrEqualThan("data", this.filterDataDa);

        if (this.filterDataA != null)
            criteria.addLessOrEqualThan("data", this.filterDataA);

        if (this.filterPagamento != null && this.filterPagamento.length() > 0)
            criteria.addLike("cliente.pagamento.descrizione", "%" + this.filterPagamento + "%");
        if (this.filterArticolo != null && this.filterArticolo.length() > 0) {
            Criteria subCriteria = new Criteria();
            subCriteria.addEqualToField("idDDT", Criteria.PARENT_QUERY_PREFIX + "id");
            subCriteria.addLike("descrizioneArticolo", "%" + this.filterArticolo + "%");
            criteria.addExists(new QueryByCriteria(DettaglioDDT.class, subCriteria));
        }
        if (this.filterStato != null && this.filterStato.length() > 0)
            if (filterStato.equals("01"))
                criteria.addEqualTo("pagato", true);
            else if (filterStato.equals("02"))
                criteria.addEqualTo("pagato", false);
        if (this.filterAutista != null && this.filterAutista.length() > 0)
            // criteria.addLike("ordine.autista.nome", "%" + this.filterAutista + "%");
            // criteria.addLike("cliente.autista.nome", "%" + this.filterAutista + "%");
            criteria.addLike("autista.nome", "%" + this.filterAutista + "%");
        // 20161122
    }

    public String printRiepilogo() throws Exception { // STAMPA LISTA DOCUMENTI
        BigDecimal totale = new BigDecimal(0);
        BigDecimal totaleAcconto = new BigDecimal(0);
        Collection colFatture = getElements();
        Iterator itr = colFatture.iterator();
        ArrayList listaFatture = new ArrayList();
        while (itr.hasNext()) {
            DDT fattura = (DDT) itr.next();
            listaFatture.add(fattura);
            totale = totale.add(fattura.getTotale());
            totaleAcconto = totaleAcconto.add(fattura.getAcconto());
        }

        DateFormat DF = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
        DF.setLenient(false);

        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.MONTH, -1);
        Date start = c.getTime();

        RiepilogoDDTs pf = new RiepilogoDDTs();
        if (this.filterDataDa != null)
            pf.setDataDal(getFilterDataDa());
        else
            pf.setDataDal(start);
        if (this.filterDataA != null)
            pf.setDataAl(getFilterDataA());
        else
            pf.setDataAl(today);
        pf.setListaDDTs(listaFatture);
        pf.setTotale(totale);
        pf.setTotaleAcconto(totaleAcconto);
        pf.setTipo("F");

        pdfFile = StampeMgr.getInstance().richiediPDFDDTList(pf);

        return "success";
    }

    public void delete(DDT ddt) throws DataAccessException {
        delete(ddt, true);
    }

    public void delete(DDT ddt, boolean updateOrdine) throws DataAccessException {
        ddt = (DDT) findWithAllReferences(ddt);
        if (!ddt.getFatturato().booleanValue()) {
            PersistenceBroker broker = null;
            try {
                broker = PersistenceBrokerFactory.defaultPersistenceBroker();
                broker.beginTransaction();

                Ordini ordini = new Ordini();
                Giacenze giacenze = new Giacenze();
                for (int i = 0; i < ddt.getDettagliDDT().size(); i++) {
                    DettaglioDDT dt = (DettaglioDDT) ddt.getDettagliDDT().get(i);
                    completeReferences(dt);

                    giacenze.delete(dt, broker, true);

                    Integer pezzi = dt.getPezzi();
                    if (pezzi == null) {
                        pezzi = Integer.valueOf(0);
                    }

                    if (updateOrdine && dt.getEvasioniOrdini() != null && pezzi != null) {
                        ordini.stornaEvasioni(pezzi.intValue(), dt, broker);
                    }
                }
                broker.delete(ddt);
                broker.commitTransaction();
            } catch (Exception e) {
                broker.abortTransaction();
                throw new DataAccessException(e.getMessage());
            } finally {
                if (broker != null) {
                    broker.close();
                }
            }
        } else {
            throw new DataAccessException("Non è possibile cancellare DDT fatturati");
        }
    }

    @Override
    public String store(VOElement element) throws DataAccessException {
        return store(element, true);
    }

    public String store(VOElement element, boolean updateOrdine) throws DataAccessException {
        PersistenceBroker broker = null;
        String result = ERROR;

        DDT ddt = (DDT) element;
        if (ddt.getPagato() == null)
            ddt.setPagato(false);

        Calendar cal = Calendar.getInstance();
        cal.setTime(ddt.getData());
        Integer annoContabile = Integer.valueOf(cal.get(1));
        if (ddt.getAnnoContabile() == null) {
            ddt.setAnnoContabile(annoContabile);
        }
        try {
            if (ddt.getAcconto() == null || ddt.getAcconto().compareTo(new BigDecimal(0)) < 0) {
                ddt.setAcconto(BigDecimal.ZERO);
            }
            ddt.setData(new java.sql.Date(ddt.getData().getTime()));
            ddt.setDataTrasporto(new java.sql.Date(ddt.getDataTrasporto().getTime()));
            ddt.setOraTrasporto(new Time(ddt.getOraTrasporto().getTime()));

            Iterator itr = ddt.getDettagliDDT().iterator();
            while (itr.hasNext()) {
                DettaglioDDT dt = (DettaglioDDT) itr.next();
                dt.setTotale(dt.calcolaImponibile());
                dt.setDdt(ddt);
            }
            Vector righeDaCancellare = new Vector();
            Vector righeAggiornate = new Vector();
            if (ddt.getId() != null) {
                broker = PersistenceBrokerFactory.defaultPersistenceBroker();
                Criteria criteria = new Criteria();
                criteria.addEqualTo("idDDT", ddt.getId());
                Query query = QueryFactory.newQuery(DettaglioDDT.class, criteria);
                Collection righe = broker.getCollectionByQuery(query);
                Iterator itrR = righe.iterator();
                while (itrR.hasNext()) {
                    DettaglioDDT dt = (DettaglioDDT) itrR.next();
                    boolean trovato = false;
                    Iterator itrR2 = ddt.getDettagliDDT().iterator();
                    while ((itrR2.hasNext()) && (!trovato)) {
                        DettaglioDDT dt2 = (DettaglioDDT) itrR2.next();
                        if ((dt2.getId() != null) && (dt2.getId().intValue() == dt.getId().intValue())) {
                            trovato = true;
                        }
                    }
                    if (!trovato) {
                        righeDaCancellare.add(dt);
                        completeReferences(dt);
                    } else {
                        righeAggiornate.add(dt);
                    }
                }
                broker.close();
            }
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();
            if (ddt.getNumeroProgressivo() == null) {
                Criteria cri = new Criteria();
                cri.addEqualTo("annoContabile", annoContabile);
                cri.addEqualTo("numeroProgressivo2", "");
                ReportQueryByCriteria q = QueryFactory.newReportQuery(DDT.class, new String[] { "max(numeroProgressivo)" }, cri, true);
                Iterator iter = broker.getReportQueryIteratorByQuery(q);
                Object[] obj = (Object[]) iter.next();
                if (obj[0] != null) {
                    Integer n = (Integer) obj[0];
                    ddt.setNumeroProgressivo(n = Integer.valueOf(n.intValue() + 1));
                } else {
                    ddt.setNumeroProgressivo(Integer.valueOf(1));
                }
                ddt.setNumeroProgressivo2("");
            }

            Vector<DettaglioDDT> det = ddt.getDettagliDDT();
            Iterator itd = ddt.getDettagliDDT().iterator();

            while (itd.hasNext()) {
                DettaglioDDT dett = (DettaglioDDT) itd.next();
                Articolo art = new Articolo();
                art.setId(dett.getIdArticolo());
                dett.setArticolo((Articolo) findWithAllReferences(art));
            }

            ddt.setDettagliDDT(det);

            if (ddt.getPuntoConsegna() == null) {

                PuntoConsegna pc = new PuntoConsegna();
                pc.setIdCliente(ddt.getIdCliente());
                pc.setId(ddt.getIdPuntoConsegna());
                pc = (PuntoConsegna) findWithAllReferences(pc);

                ddt.setPuntoConsegna(pc);
            }

            // 20161213
            if (ddt.getIdOrdine() != null) {
                ddt.setIdOrdine(ddt.getIdOrdine());
            }

            broker.store(ddt);
            broker.commitTransaction();

            Giacenze giacenze = new Giacenze();
            Ordini ordini = new Ordini();
            if (righeDaCancellare.size() > 0) {
                itr = righeDaCancellare.iterator();
                while (itr.hasNext()) {
                    DettaglioDDT dt = (DettaglioDDT) itr.next();
                    delete(dt);
                    giacenze.delete(dt, broker, false);
                    ordini.aggiornaEvasioni(dt.getPezzi().intValue() * -1, dt, broker);
                }
            }
            itr = ddt.getDettagliDDT().iterator();
            while (itr.hasNext()) {
                DettaglioDDT dt = (DettaglioDDT) itr.next();
                giacenze.store(dt, broker);
            }

            itr = ddt.getDettagliDDT().iterator();
            while (itr.hasNext()) {
                DettaglioDDT dt = (DettaglioDDT) itr.next();
                Integer pezzi = dt.getPezzi();
                if (pezzi == null) {
                    pezzi = Integer.valueOf(0);
                }
                boolean trovato = false;
                Iterator itr3 = righeAggiornate.iterator();
                while ((itr3.hasNext()) && (!trovato)) {
                    DettaglioDDT dt2 = (DettaglioDDT) itr3.next();
                    if (dt2.getId().intValue() == dt.getId().intValue()) {
                        trovato = true;
                        if (dt2.getPezzi() != null) {
                            pezzi = Integer.valueOf(pezzi.intValue() - dt2.getPezzi().intValue());
                        }
                    }
                }

                Boolean ok = (dt.getEvasioniOrdini() != null);

                if (updateOrdine && dt.getEvasioniOrdini() != null && pezzi != null) {
                    ordini.aggiornaEvasioni(pezzi.intValue(), dt, broker);
                }
            }
            result = SUCCESS;
        } catch (Exception e) {
            broker.abortTransaction();
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null) {
                broker.close();
            }
        }
        return result;
    }

    public void pagato(DDT ddt, PagamentoEseguito pagamentoEseguito) throws DataAccessException {
        PersistenceBroker broker = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();

            BigDecimal total = ddt.getAcconto().add(pagamentoEseguito.getImporto());
            ddt.setAcconto(total);
            if (total.compareTo(ddt.calcolaTotale()) >= 0) {
                if (ddt.getAcconto().compareTo(new BigDecimal(0)) == 1) {
                    pagamentoEseguito.setDescrizione("Saldo " + ddt.getDescrizioneBreveDDT());
                } else {
                    pagamentoEseguito.setDescrizione("Pagamento " + ddt.getDescrizioneBreveDDT());
                }
                ddt.setPagato(Boolean.valueOf(true));
            } else {
                pagamentoEseguito.setDescrizione("Acconto " + ddt.getDescrizioneBreveDDT());
                ddt.setPagato(Boolean.valueOf(false));
            }

            if (ddt.getFattura() != null) {
                Fattura fattura = ddt.getFattura();
                boolean pagata = true;
                new Fatture().completeReferences(fattura);
                if (fattura.getDettagliFattura() != null) {
                    Iterator itr = fattura.getDettagliFattura().iterator();
                    while (itr.hasNext()) {
                        DDT ddtFattura = (DDT) itr.next();
                        if (ddtFattura.getId().equals(ddt.getId()))
                            ddtFattura = ddt;
                        if (!ddtFattura.getPagato()) {
                            pagata = false;
                            break;
                        }
                    }
                    fattura.setPagato(pagata);
                }

                Fattura fat = new Fattura();
                fat.setId(fattura.getId());
                fat = (Fattura) new Fatture().find(fat);
                fat.setPagato(fattura.getPagato());
                fat.setAcconto(fattura.getAcconto().add(pagamentoEseguito.getImporto()));
                new Fatture().store(fat);
            }

            pagamentoEseguito.setDdt(ddt);
            pagamentoEseguito.setIdDDT(ddt.getId());
            new PagamentiEseguiti().store(pagamentoEseguito);
            store(ddt);
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

    public void nonpagato(DDT ddt, PagamentoEseguito pagamentoEseguito) throws DataAccessException {
        PersistenceBroker broker = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();

            ddt.setAcconto(ddt.getAcconto().subtract(pagamentoEseguito.getImporto()));
            ddt.setPagato(Boolean.valueOf(false));
            store(ddt);
            
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

    public Collection ricercaLotto(String lotto) throws DataAccessException {
        PersistenceBroker broker = null;
        Collection results = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            Criteria criteria = new Criteria();
            criteria.addEqualTo("dettagliDDT.lotto", lotto);
            QueryByCriteria query = QueryFactory.newQuery(DDT.class, criteria);
            query.addOrderByDescending("numeroProgressivo");
            query.addGroupBy("numeroProgressivo");
            query.addGroupBy("data");
            query.addGroupBy("cliente.rs");
            results = broker.getCollectionByQuery(query);
            if (results != null) {
                Iterator itr = results.iterator();
                while (itr.hasNext()) {
                    DDT dt = (DDT) itr.next();
                    completeReferences(dt);
                }
            }
        } finally {
            if (broker != null) {
                broker.close();
            }
        }
        return results;
    }

    public Collection<DDT> getDDTsByIdOrdine(Integer idOrdine) throws DataAccessException {
        getCriteria().addEqualTo("idOrdine", idOrdine);
        return getElements();
    }
}
