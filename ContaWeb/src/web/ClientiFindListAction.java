package web;

import java.util.Collection;

import dao.Clienti;
import vo.Cliente;

public class ClientiFindListAction extends GenericAction {

    private Collection<Cliente> clienti;

    private static final long serialVersionUID = 1L;

    @Override
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        Clienti clientiDao = new Clienti();
        clienti = (Collection<Cliente>) clientiDao.getListaPerDDT();

        return SUCCESS;
    }

    public Collection<Cliente> getClienti() {
        return clienti;
    }
}
