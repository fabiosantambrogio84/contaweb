package forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import dao.Clienti;
import dao.DataAccessException;
import dao.PuntiConsegna;
import dao.Telefonate;
import vo.Cliente;
import vo.PuntoConsegna;
import vo.Telefonata;

public class EditTelefonata extends Edit {

    private static final long serialVersionUID = 1L;

    private Telefonata telefonata = new Telefonata();

    private Collection listPuntiConsegna = null;
    private Integer idPuntoConsegna = null;
    private Collection listClienti = null;
    private Integer idCliente = null;
    private String typeList;
    private Map giorni;

    public Map getGiorni() {
        if (giorni == null) {
            giorni = new TreeMap();
            giorni.put(0, "Lunedi");
            giorni.put(1, "Martedi");
            giorni.put(2, "Mercoledi");
            giorni.put(3, "Giovedi");
            giorni.put(4, "Venerdi");
            giorni.put(5, "Sabato");
        }
        return giorni;
    }

    public void setGiorni(Map giorni) {
        this.giorni = giorni;
    }

    public String getTypeList() {
        return typeList;
    }

    public void setTypeList(String typeList) {
        this.typeList = typeList;
    }

    public Collection getListClienti() {
        if (listClienti == null) {
            try {
                Cliente cliente = new Cliente();

                cliente.setId(-1);
                cliente.setRs("Nessun cliente");

                listClienti = new ArrayList();
                listClienti.add(cliente);
                listClienti.addAll(new Clienti().getElements());

            } catch (Exception e) {
                return null;
            }
        }
        return listClienti;
    }

    public void setListClienti(Collection listClienti) {
        this.listClienti = listClienti;
    }

    public String getPuntiConsegna() {
        if (id != null) {
            Telefonata tel = new Telefonata();
            tel.setId(id);
            try {
                telefonata = (Telefonata) new Telefonate().find(tel);
            } catch (DataAccessException e) {
                return ERROR;
            }

        }
        return SUCCESS;
    }

    @Override
    public String input() {
        try {

            if (getAction().equalsIgnoreCase("edit")) {
                Telefonate tel = new Telefonate();
                telefonata = new Telefonata();
                telefonata.setId(id);
                telefonata = (Telefonata) tel.find(telefonata);
                idPuntoConsegna = telefonata.getIdPuntoConsegna();
            }

            if (getAction().equalsIgnoreCase("insert")) {
                telefonata = new Telefonata();
                telefonata.setOrario(11);
            }
        } catch (Exception e) {
            stampaErrore("EditTelefonata.input()", e);
            return ERROR;
        }

        return INPUT;
    }

    @Override
    protected String delete() {
        try {
            Telefonata tel = new Telefonata();
            tel.setId(id);

            new Telefonate().delete(tel);

            if (typeList.equalsIgnoreCase("giorno"))
                return SUCCESS;
            else
                return "success_settimana";
        } catch (Exception e) {
            stampaErrore("EditTelefonata.delete()", e);
            return ERROR;
        }
    }

    @Override
    public String store() {

        try {
            if (telefonata.getId() != null) {
                Telefonata old = new Telefonata();
                old.setId(telefonata.getId());
                old = (Telefonata) new Telefonate().find(old);
                telefonata.setAutista(old.getAutista());
                telefonata.setIdAutista(old.getIdAutista());
            }
            Clienti clienti = new Clienti();
            if (telefonata.getIdCliente() == -1) {
                telefonata.setIdCliente(null);
            } else {
                telefonata.setCliente(clienti.find(telefonata.getIdCliente()));
                PuntiConsegna pc = new PuntiConsegna(telefonata.getIdCliente());
                if (idPuntoConsegna == -1)
                    telefonata.setIdPuntoConsegna(null);
                else {
                    telefonata.setIdPuntoConsegna(idPuntoConsegna);
                    telefonata.setPuntoConsegna(pc.find(telefonata.getIdPuntoConsegna()));
                }
            }
            new Telefonate().store(telefonata);
        } catch (Exception e) {
            stampaErrore("EditTelefonata.store()", e);
            return ERROR;
        }

        if (typeList.equalsIgnoreCase("settimana"))
            return "success_settimana";
        else
            return SUCCESS;
    }

    public Telefonata getTelefonata() {
        return telefonata;
    }

    public void setTelefonata(Telefonata telefonata) {
        this.telefonata = telefonata;
    }

    public Collection getListPuntiConsegna() throws DataAccessException {
        PuntoConsegna pc = new PuntoConsegna();

        listPuntiConsegna = new ArrayList();
        if (telefonata == null || telefonata.getIdCliente() == null || telefonata.getIdCliente() == -1) {
            pc.setNome("Nessuna destinazione");
            pc.setId(-1);
            listPuntiConsegna.add(pc);
        } else {
            pc.setNome("Nessuna in particolare");
            pc.setId(-1);
            listPuntiConsegna.add(pc);

            PuntiConsegna punti = new PuntiConsegna(telefonata.getIdCliente());
            listPuntiConsegna.addAll(punti.getElements());
        }

        return listPuntiConsegna;
    }

    public void setListPuntiConsegna(Collection listPuntiConsegna) {
        this.listPuntiConsegna = listPuntiConsegna;
    }

    public Integer getIdPuntoConsegna() {
        return idPuntoConsegna;
    }

    public void setIdPuntoConsegna(Integer idPuntoConsegna) {
        this.idPuntoConsegna = idPuntoConsegna;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }
}