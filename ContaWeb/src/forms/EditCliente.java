package forms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import dao.Agenti;
import dao.Autisti;
import dao.Clienti;
import dao.DataAccessException;
import dao.Pagamenti;
import vo.Agente;
import vo.Autista;
import vo.Cliente;

public class EditCliente extends Edit {

    private static final long serialVersionUID = 1L;

    private Integer id = null;
    private Cliente cliente = null;
    private Collection listPagamenti = null;
    private Collection listAutisti = null;
    private Collection listAgenti = null;

    // CAMPI PER LA RICHIESTA COMBOBOX AJAX
    private String rs = null;
    private String rsKey = null;

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public String getRsKey() {
        return rsKey;
    }

    public void setRsKey(String rsKey) {
        this.rsKey = rsKey;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Collection getListPagamenti() {
        if (listPagamenti == null) {
            try {
                listPagamenti = new Pagamenti().getElements();
            } catch (Exception e) {
                return null;
            }
        }
        return listPagamenti;
    }

    public void setListPagamenti(Collection listPagamenti) {
        this.listPagamenti = listPagamenti;
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
            if (getAction().equalsIgnoreCase("edit")) {
                Clienti clienti = new Clienti();
                cliente = clienti.find(id);
            } else if (getAction().equalsIgnoreCase("delete"))// AZIONE DELETE
                return delete();
            else {
                cliente = new Cliente();
                cliente.setFido(new BigDecimal(0));
            }

        } catch (Exception e) {
            stampaErrore("EditCliente.input()", e);
            return ERROR;
        }
        return INPUT;
    }

    @Override
    protected String store() {
        try {
            cliente.setDataInserimento(new java.sql.Date(new Date().getTime()));
            cliente.setPagamento(new Pagamenti().find(cliente.getIdPagamento()));
            if (cliente.getIdAutista() == null) {
                cliente.setAutista(null);
            } else {
                cliente.setAutista(new Autisti().find(cliente.getIdAutista()));
            }
            if (cliente.getIdAgente() == null) {
                cliente.setAgente(null);
            } else {
                cliente.setAgente(new Agenti().find(cliente.getIdAgente()));
            }
            if (!cliente.isDittaIndividuale()) {
                cliente.setCognome(null);
                cliente.setNome(null);
            }
            Clienti clienti = new Clienti();
            clienti.store(cliente);
        } catch (Exception e) {
            stampaErrore("EditCliente.store()", e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    protected String delete() {
        try {
            cliente = new Cliente();
            cliente.setId(id);
            new Clienti().delete(cliente);
        } catch (DataAccessException e) {
            stampaErrore("EditCliente.delete()", e);
            return ERROR_DELETE;
        }
        return SUCCESS;
    }

    public Collection getListAutisti() {
        if (listAutisti == null) {
            try {
                Autista vuoto = new Autista();
                vuoto.setId(null);
                vuoto.setNome("Nessuno");
                List lista = new ArrayList();
                lista.add(vuoto);
                lista.addAll(new Autisti().getElements());
                listAutisti = lista;
            } catch (Exception e) {
                // TODO: handle exception
                listAutisti = null;
            }
        }
        return listAutisti;
    }

    public Collection getListAgenti() {
        if (listAgenti == null) {
            try {
                Agente vuoto = new Agente();
                vuoto.setId(null);
                vuoto.setNome("Nessuno");
                List lista = new ArrayList();
                lista.add(vuoto);
                lista.addAll(new Agenti().getElements());
                listAgenti = lista;
            } catch (Exception e) {
                // TODO: handle exception
                listAgenti = null;
            }
        }
        return listAgenti;
    }

    public void setListAutisti(Collection listAutisti) {
        this.listAutisti = listAutisti;
    }
}