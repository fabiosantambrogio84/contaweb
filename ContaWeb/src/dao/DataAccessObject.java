package dao;

import java.util.Collection;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;

import vo.Property;
import vo.VOElement;

public class DataAccessObject extends stampe.PrintPDF {

    /* COSTANTI */
    static public int ORDER_DESC = 0;
    static public int ORDER_ASC = 1;
    static public int PAG_NO = 0;
    static public int PAG_UP = 1;
    static public int PAG_DW = 2;
    /* FINE COSTANTI */

    protected int startAtIndex = -1;
    protected int stopAtIndex = -1;

    protected QueryByCriteria query = null;
    protected Criteria criteria = null;
    protected Class elementClass;

    protected String filterKey = null;
    protected boolean useDefaultCriteria = true;

    /* METODI SCELTA DEL CRITERIO */
    protected void setDefaultCriteria() {
    }

    protected QueryByCriteria getQueryByCriteria() {
        if (query == null) {
            query = QueryFactory.newQuery(elementClass, getCriteria());
            if (useDefaultCriteria) {
                setDefaultCriteria();
            }
        }
        return query;
    }
    /* FINE METODI SCELTA DEL CRITERIO */

    public VOElement find(VOElement keyObject) throws DataAccessException {
        PersistenceBroker broker = null;
        VOElement element = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            Query query = new QueryByCriteria(keyObject);
            element = (VOElement) broker.getObjectByQuery(query);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null)
                broker.close();
        }
        return element;
    }

    public Property find(Property keyObject) throws DataAccessException {
        PersistenceBroker broker = null;
        Property element = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            Query query = new QueryByCriteria(keyObject);
            element = (Property) broker.getObjectByQuery(query);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null)
                broker.close();
        }
        return element;
    }

    public VOElement find(VOElement keyObject, PersistenceBroker broker) throws DataAccessException {
        if (keyObject == null)
            throw new DataAccessException("key is null");

        VOElement element = null;
        try {
            Query query = new QueryByCriteria(keyObject);
            element = (VOElement) broker.getObjectByQuery(query);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return element;
    }

    public Property find(Property keyObject, PersistenceBroker broker) throws DataAccessException {
        if (keyObject == null)
            throw new DataAccessException("key is null");

        Property element = null;
        try {
            Query query = new QueryByCriteria(keyObject);
            element = (Property) broker.getObjectByQuery(query);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return element;
    }

    public VOElement findWithAllReferences(VOElement keyObject, PersistenceBroker broker) throws DataAccessException {
        if (keyObject == null)
            throw new DataAccessException("key is null");

        VOElement element = null;
        try {
            Query query = new QueryByCriteria(keyObject);
            if (query == null) {
                return null;
                // broker.store(arg0);
            }
            element = (VOElement) broker.getObjectByQuery(query);
            if (element != null)
                broker.retrieveAllReferences(element);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return element;
    }

    public Property findWithAllReferences(Property keyObject, PersistenceBroker broker) throws DataAccessException {
        if (keyObject == null)
            throw new DataAccessException("key is null");

        Property element = null;
        try {
            Query query = new QueryByCriteria(keyObject);
            element = (Property) broker.getObjectByQuery(query);
            broker.retrieveAllReferences(element);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return element;
    }

    public VOElement findWithAllReferences(VOElement keyObject) throws DataAccessException {
        if (keyObject == null)
            throw new DataAccessException("key is null");

        PersistenceBroker broker = null;
        VOElement element = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            element = findWithAllReferences(keyObject, broker);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null)
                broker.close();
        }
        return element;
    }

    public Property findWithAllReferences(Property keyObject) throws DataAccessException {
        if (keyObject == null)
            throw new DataAccessException("key is null");

        PersistenceBroker broker = null;
        Property element = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            element = findWithAllReferences(keyObject, broker);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null)
                broker.close();
        }
        return element;
    }

    public VOElement completeReferences(VOElement keyObject) throws DataAccessException {
        if (keyObject == null)
            throw new DataAccessException("key is null");

        PersistenceBroker broker = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.retrieveAllReferences(keyObject);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null)
                broker.close();
        }
        return keyObject;
    }

    public Collection getElements() throws DataAccessException {
        PersistenceBroker broker = null;
        Collection results = null;

        /*
         * *** Criteria criteria = null;
         */
        QueryByCriteria queryByCriteria = null;

        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();

            /*
             * **. criteria = new Criteria(); criteria.addLessOrEqualThan("stock", new Integer(20));
             * criteria.addGreaterOrEqualThan("price", new Double(100000.0)); // queryByCriteria = new QueryByCriteria(); .
             */

            // FILTER KEY
            if (filterKey != null && filterKey.length() > 0) {
                setCriteriaUsingFilterKey();
            }

            // CASO PAGINANTE
            if ((startAtIndex != -1) && (stopAtIndex != -1)) {
                getQueryByCriteria().setStartAtIndex(startAtIndex);
                getQueryByCriteria().setEndAtIndex(stopAtIndex - 1);
            }

            queryByCriteria = getQueryByCriteria();

            results = broker.getCollectionByQuery(queryByCriteria);
            // results = broker.getCollectionByQuery(getQueryByCriteria());
            broker.close();
        } catch (Exception e) {
            // 20161213
            // ALTER TABLE table_name ADD column_name column_type(60) AFTER prev_column;
            String tmpString = " - DB has to be updated at least at ver 0.9.9d - ";
            tmpString = tmpString + e.getMessage();
            // int tmpInt = tmpString.compareToIgnoreCase("Unknown column 'A0.idOrdine'");
            // broker.close();
            // broker = PersistenceBrokerFactory.createPersistenceBroker(arg0, arg1, arg2) .defaultPersistenceBroker();
            // tmpString = broker.query().toString();
            // if (tmpInt < 0) {
            // tmpString = queryByCriteria.toString().replaceAll(",A0.idOrdine", "");
            // }
            throw new DataAccessException(tmpString);
        } finally {
            if (broker != null)
                broker.close();
        }

        return results;
    }

    public Integer getCount() throws DataAccessException {
        PersistenceBroker broker = null;
        Integer results = 0;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();

            setCriteriaUsingFilterKey();

            results = broker.getCount(getQueryByCriteria());

            broker.close();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null)
                broker.close();
        }

        return results;
    }

    protected void setCriteriaUsingFilterKey() {

    }

    public String store(VOElement value) throws DataAccessException {
        PersistenceBroker broker = null;
        String result = ERROR;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();
            broker.store(value);
            broker.commitTransaction();
            broker.serviceConnectionManager().releaseConnection();
            broker.close();
            result = SUCCESS;
        } catch (Exception e) {
            broker.abortTransaction();
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null)
                broker.close();
        }
        return result;
    }

    // public String store(Property value) throws DataAccessException {
    // PersistenceBroker broker = null;
    // try {
    // broker = PersistenceBrokerFactory.defaultPersistenceBroker();
    // // Property oldProp = new Property();
    // // oldProp.setKey(value.getKey());
    // // query = new QueryByCriteria(oldProp);
    // // Property prop = (Property) broker.getObjectByQuery(query);
    // // if (prop == null) return ERROR;
    // broker.beginTransaction();
    // // prop.setValue(value.getValue());
    // // broker.store(prop);
    // broker.store(value);
    // broker.commitTransaction();
    // broker.serviceConnectionManager().releaseConnection();
    // broker.close();
    // } catch (Exception e) {
    // broker.abortTransaction();
    // throw new DataAccessException(e.getMessage());
    // } finally {
    // if (broker != null)
    // broker.close();
    // }
    // return SUCCESS;
    // }

    public String store(VOElement[] values) throws DataAccessException {
        PersistenceBroker broker = null;
        String result = ERROR;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();
            for (int i = 0; i < values.length; i++)
                broker.store(values[i]);
            broker.commitTransaction();
            broker.serviceConnectionManager().releaseConnection();
            broker.close();
            result = SUCCESS;
        } catch (Exception e) {
            broker.abortTransaction();
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null)
                broker.close();
        }
        return result;
    }

    public void delete(VOElement value) throws DataAccessException {
        PersistenceBroker broker = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();
            broker.delete(value);
            broker.commitTransaction();
            broker.serviceConnectionManager().releaseConnection();
            broker.close();
        } catch (Exception e) {
            broker.abortTransaction();
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null)
                broker.close();
        }
    }

    public void setPaginazione(int rigaIniziale, int rigaFinale) {
        startAtIndex = rigaIniziale;
        stopAtIndex = rigaFinale;
    }

    public Criteria getCriteria() {
        if (criteria == null)
            criteria = new Criteria();
        return criteria;
    }

    public void setFilterKey(String filterKey) {
        this.filterKey = filterKey;
    }
}
