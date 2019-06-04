package dao;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Vector;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;

import vo.Cliente;
import vo.ListinoAssociato;

public class Clienti extends DataAccessObject {

    private static final long serialVersionUID = -6807714934563818898L;

    private boolean filterByListino = false;

    private Integer idFornitore;

    private Integer idListino;

    @Override
    protected void setCriteriaUsingFilterKey() {
        Criteria criteria = getCriteria();

        if (filterKey != null && filterKey.length() > 0) {
            // NON E' UNA DATA CERCO NEGLI ALTRI CAMPI
            criteria.addLike("rs", "%" + filterKey + "%");

            try {
                // FILTRO PER NUMERO
                NumberFormat nf = NumberFormat.getNumberInstance();
                Number number = nf.parse(filterKey);
                Criteria criteriaNprog = new Criteria();
                criteriaNprog.addEqualTo("id", number);
                criteria.addOrCriteria(criteriaNprog);
            } catch (Exception e) {

            }
        }
    }

    public void store(Cliente value) throws DataAccessException {

        /*
         * //Cancella eventuali telefonate non pi� presenti
         * 
         * if (value.getId() != null && value.getTelefonate() != null) { Cliente cliente = new Cliente(); cliente.setId(value.getId());
         * cliente = (Cliente) findWithAllReferences(cliente);
         * 
         * if (cliente.getTelefonate() != null) { Iterator itr = cliente.getTelefonate().iterator(); while (itr.hasNext()) { Telefonata
         * tel = (Telefonata) itr.next(); Iterator itr2 = value.getTelefonate().iterator(); boolean trovato = false; while
         * (itr2.hasNext() && !trovato) { Telefonata tel2 = (Telefonata) itr2.next(); if (tel2.getGiorno().intValue() ==
         * tel.getGiorno().intValue()) { trovato = true; tel2.setIdCliente(tel.getIdCliente()); tel2.setCliente(tel.getCliente());
         * tel2.setId(tel.getId()); } } if (!trovato) delete(tel); } } }
         */

        super.store(value);
    }

    public Clienti() {
        this.elementClass = Cliente.class;
    }

    public Cliente find(int idCliente) throws DataAccessException {
        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        cliente = (Cliente) find(cliente);
        return cliente;
    }

    public void setOrderByDescrizione(int order) {
        useDefaultCriteria = false;
        if (order == ORDER_DESC)
            getQueryByCriteria().addOrderByDescending("rs");
        else
            getQueryByCriteria().addOrderByAscending("rs");
    }

    @Override
    protected void setDefaultCriteria() {
        // setOrderByDescrizione(ORDER_DESC);
        getQueryByCriteria().addOrderByDescending("id");
    }

    // CLAUDIO: modifiche per CONAD
    public void setFilterByConadFormat() {
        Criteria criteria = getCriteria();
        Criteria eqZero = new Criteria();
        Criteria eqUno = new Criteria();
        eqZero.addEqualTo("formatoConad", 1);
        eqUno.addEqualTo("formatoConad", 2);
        eqZero.addOrCriteria(eqUno);
        criteria.addAndCriteria(eqZero);
    }

    public Collection<?> getFiltratoConad() throws DataAccessException {
        Clienti cliente = new Clienti();
        cliente.setFilterByConadFormat();
        cliente.setOrderByDescrizione(ORDER_ASC);
        return cliente.getElements();
    }

    public void setFilterByAutista(Integer idAutista) {
        Criteria criteria = getCriteria();
        Criteria autistaId = new Criteria();
        autistaId.addEqualTo("idAutista", idAutista);
        criteria.addAndCriteria(autistaId);
    }

    public void setFilterByListino(Integer idFornitore, Integer idListino) {
        this.filterByListino = true;
        this.idFornitore = idFornitore;
        this.idListino = idListino;
    }

    @Override
    public Collection<?> getElements() throws DataAccessException {
        if (filterByListino) {
            Listini listini = new Listini();
            return listini.getClientiByListino(idFornitore, idListino, filterKey, startAtIndex, stopAtIndex);
        } else
            return super.getElements();

    }

    @Override
    public Integer getCount() throws DataAccessException {
        if (filterByListino) {
            Listini listini = new Listini();
            return listini.getCountClientiByListino(idFornitore, idListino, filterKey);
        } else
            return super.getCount();
    }

    public Collection<?> getFiltratoAutista(Integer idAutista) throws DataAccessException {
        Clienti cliente = new Clienti();
        cliente.setFilterByAutista(idAutista);
        cliente.setOrderByDescrizione(ORDER_ASC);
        return cliente.getElements();
    }

    public void setFilterByAgente(Integer idAgente) {
        Criteria criteria = getCriteria();
        Criteria agenteId = new Criteria();
        agenteId.addEqualTo("idAgente", idAgente);
        criteria.addAndCriteria(agenteId);
    }

    public Collection<?> getFiltratoAgente(Integer idAgente) throws DataAccessException {
        Clienti cliente = new Clienti();
        cliente.setFilterByAgente(idAgente);
        cliente.setOrderByDescrizione(ORDER_ASC);
        return cliente.getElements();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void salvaListiniAssociati(int idCliente, Vector listiniAssociati) throws DataAccessException {
        PersistenceBroker broker = null;
        try {
            DataAccessObject obj = new DataAccessObject();
            for (int i = 0; i < listiniAssociati.size(); ++i) {
                ListinoAssociato la = (ListinoAssociato) listiniAssociati.get(i);
                la.setIdCliente(idCliente);
                la = (ListinoAssociato) obj.completeReferences(la);
                listiniAssociati.set(i, la);
            }

            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();
            // CARICO L'OGGETTO CLIENTE
            Cliente cliente = find(idCliente);
            cliente.setListiniAssociati(listiniAssociati);
            store(cliente);
            broker.commitTransaction();
            broker.close();
        } catch (Exception e) {
            broker.abortTransaction();
            throw new DataAccessException(e.getMessage());
        } finally {
            if (broker != null)
                broker.close();
        }
    }

    public Collection<?> getListaPerDDT() throws DataAccessException {
        getCriteria().addEqualTo("bloccaDDT", false);
        return getElements();
    }

    public Collection<?> getListaPerDDTFilter(String q) throws DataAccessException {
        Criteria criteria = getCriteria();
        criteria.addEqualTo("bloccaDDT", false);
        criteria.addLike("rs", "%" + q + "%");

        return getElements();
    }
    
    public void setFilterByBloccaDDT() {
        Criteria criteria = getCriteria();
        Criteria bloccaDdtCriteria = new Criteria();
        bloccaDdtCriteria.addEqualTo("bloccaDDT", false);
        criteria.addAndCriteria(bloccaDdtCriteria);
    }
}
