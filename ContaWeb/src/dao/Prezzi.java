package dao;

import java.util.Collection;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;

import vo.Prezzo;

public class Prezzi extends DataAccessObject {
    private static final long serialVersionUID = 6525257527509786107L;

    public Prezzi() {
        this.elementClass = Prezzo.class;
    }

    public void setOrderByDescrizione(int order) {
        useDefaultCriteria = false;
        if (order == ORDER_DESC)
            getQueryByCriteria().addOrderByDescending("idArticolo");
        else
            getQueryByCriteria().addOrderByAscending("idArticolo");
    }

    @SuppressWarnings("unchecked")
    public Collection<Prezzo> getPrezziByListino(Integer idListino) throws DataAccessException {
        PersistenceBroker broker = null;
        Collection<Prezzo> results = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();

            /* seleziono i prezzi relativi al listino 'idListino' */
            Criteria criteria = new Criteria();
            criteria.addColumnEqualTo("idListino", idListino);
            Query query = QueryFactory.newQuery(elementClass, criteria);
            results = broker.getCollectionByQuery(query);

            broker.close();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null) {
                broker.close();
            }
        }
        return results;
    }

    public void deletePrezziByListino(Integer idListino) throws DataAccessException {
        PersistenceBroker broker = null;
        try {
            Criteria criteria = new Criteria();
            criteria.addColumnEqualTo("idListino", idListino);
            Query query = QueryFactory.newQuery(elementClass, criteria);

            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();
            broker.deleteByQuery(query);
            broker.commitTransaction();
            broker.serviceConnectionManager().releaseConnection();
            broker.close();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null) {
                broker.close();
            }
        }
    }

    public String insertPrezzi(Collection<Prezzo> prezzi) throws DataAccessException {
        PersistenceBroker broker = null;
        String result = ERROR;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();
            for (Prezzo prezzo : prezzi) {
                broker.store(prezzo);
            }
            broker.commitTransaction();
            broker.serviceConnectionManager().releaseConnection();
            broker.close();
            result = SUCCESS;
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null) {
                broker.close();
            }
        }
        return result;
    }
}
