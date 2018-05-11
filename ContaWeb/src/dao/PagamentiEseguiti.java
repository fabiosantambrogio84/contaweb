package dao;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;

import vo.Fattura;
import vo.Pagamento;
import vo.PagamentoEseguito;

public class PagamentiEseguiti extends DataAccessObject {

    private static final long serialVersionUID = 2006834321938329478L;

    protected Date filterDataDa = null;
    protected Date filterDataA = null;
    protected String filterPagamento = null;

    public Collection<Pagamento> pagamenti = null;

    public PagamentoEseguito find(Integer id) throws DataAccessException {
        PagamentoEseguito pagamento = new PagamentoEseguito();
        pagamento.setId(id);
        return (PagamentoEseguito) find(pagamento);
    }

    public Collection findByFattura(Fattura fattura) {
        PersistenceBroker broker = null;
        Collection results = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            Criteria criteria = new Criteria();
            criteria.addEqualTo("idFattura", fattura.getId());
            QueryByCriteria query = QueryFactory.newQuery(PagamentoEseguito.class, criteria);
            results = broker.getCollectionByQuery(query);
        } finally {
            if (broker != null) {
                broker.close();
            }
        }
        return results;
    }

    public void setPagamenti() {
        Iterator itr = null;
        PersistenceBroker broker = null;
        PagamentoEseguito paymentCopyExe = null;
        Pagamento ctrPay = null;
        Vector<Pagamento> pays = new Vector<Pagamento>();
        Boolean ctr = false;
        broker = PersistenceBrokerFactory.defaultPersistenceBroker();

        setCriteriaUsingFilterKey();

        itr = broker.getCollectionByQuery(getQueryByCriteria()).iterator();
        broker.close();

        while (itr.hasNext()) {

            paymentCopyExe = (PagamentoEseguito) itr.next();

            PagamentoEseguito paymentExe = new PagamentoEseguito();
            paymentExe.setPagamento(paymentCopyExe.getPagamento());
            paymentExe.setImporto(paymentCopyExe.getImporto());
            paymentExe.setId(paymentCopyExe.getId());

            Pagamento payment = new Pagamento();

            payment.setId(paymentExe.getPagamento().getId());
            payment.setAmount(paymentExe.getPagamento().getAmount());
            payment.setDescrizione(paymentExe.getPagamento().getDescrizione());
            payment.setScadenza(paymentExe.getPagamento().getScadenza());

            if (pays != null) {
                Iterator citr = pays.iterator();
                Integer i = 0;
                ctr = false;

                while (citr.hasNext()) {
                    ctrPay = (Pagamento) citr.next();

                    if (ctrPay.getId() == payment.getId()) {

                        BigDecimal amount = ctrPay.getAmount();

                        BigDecimal total = amount.add(paymentExe.getImporto());

                        ctrPay.setAmount(total);

                        // pays.set(i, ctrPay);

                        ctr = true;
                    }

                    i++;

                    if (ctr) {
                        break;
                    }
                }
            }

            if (!ctr) {
                if (payment.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                    payment.setAmount(paymentExe.getImporto());
                }

                pays.add(payment);
            }
        }

        this.pagamenti = pays;
    }

    public Collection<Pagamento> getPagamenti() {
        return pagamenti;
    }

    @Override
    public Collection getElements() throws DataAccessException {
        PersistenceBroker broker = null;
        Collection results = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();

            setCriteriaUsingFilterKey();

            // CASO PAGINANTE
            if ((startAtIndex != -1) && (stopAtIndex != -1)) {
                getQueryByCriteria().setStartAtIndex(startAtIndex);
                getQueryByCriteria().setEndAtIndex(stopAtIndex - 1);
            }

            results = broker.getCollectionByQuery(getQueryByCriteria());
            broker.close();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null)
                broker.close();
        }

        return results;
    }

    @Override
    protected void setCriteriaUsingFilterKey() {
        Criteria criteria = getCriteria();

        if (this.filterKey != null && this.filterKey.length() > 0) {
            criteria.addLike("cliente.rs", "%" + this.filterKey + "%");
        }

        if (this.filterPagamento != null && this.filterPagamento.length() > 0) {
            criteria.addLike("pagamento.descrizione", "%" + this.filterPagamento + "%");
        }

        DateFormat DF = new SimpleDateFormat("yyyy-M-dd", Locale.ITALIAN);
        DF.setLenient(false);
        if (this.filterDataDa != null) {
            criteria.addGreaterOrEqualThan("data", this.filterDataDa);
        }
        if (this.filterDataA != null) {
            criteria.addLessOrEqualThan("data", this.filterDataA);
        }

    }

    @Override
    protected void setDefaultCriteria() {
        query.addOrderByDescending("data");
        query.addOrderByDescending("id");
    }

    public void store(PagamentoEseguito pagamentoEseguito) throws DataAccessException {
        // DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN);
        pagamentoEseguito.setData(new java.sql.Date(pagamentoEseguito.getData().getTime()));
        super.store(pagamentoEseguito);
    }

    public PagamentiEseguiti() {
        this.elementClass = PagamentoEseguito.class;
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

    public String getFilterPagamento() {
        return filterPagamento;
    }

    public void setFilterPagamento(String filterPagamento) {
        this.filterPagamento = filterPagamento;
    }

}
