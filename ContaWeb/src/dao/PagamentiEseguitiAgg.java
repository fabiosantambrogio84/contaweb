package dao;

import java.util.Collection;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import vo.PagamentoEseguitoAgg;

public class PagamentiEseguitiAgg extends DataAccessObject {

    private static final long serialVersionUID = -3827915155639684973L;

    public PagamentiEseguitiAgg() {
        this.elementClass = PagamentoEseguitoAgg.class;
    }

    public PagamentoEseguitoAgg find(Integer id) throws DataAccessException {
        PagamentoEseguitoAgg pagamentoEseguitoAgg = new PagamentoEseguitoAgg();
        pagamentoEseguitoAgg.setId(id);
        return (PagamentoEseguitoAgg) find(pagamentoEseguitoAgg);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<?> getElements() throws DataAccessException {
        PersistenceBroker broker = null;
        Collection<PagamentoEseguitoAgg> results = null;
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
    protected void setDefaultCriteria() {
        query.addOrderByDescending("id");
    }

    public void store(PagamentoEseguitoAgg pagamentoEseguitoAgg) throws DataAccessException {
        super.store(pagamentoEseguitoAgg);
    }
}
