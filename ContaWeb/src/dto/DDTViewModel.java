package dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import dao.Autisti;
import dao.Clienti;
import dao.DataAccessException;
import dao.Ivas;
import dao.PuntiConsegna;
import vo.Autista;
import vo.Cliente;
import vo.DDT;
import vo.Iva;
import vo.PuntoConsegna;

public class DDTViewModel implements Serializable {

    private static final long serialVersionUID = 8717634528473867067L;

    private String annoContabile;
    private String colli;
    private String data;
    private String dataTrasporto;
    private String id;
    private String idCliente;
    private String idAutista;
    private String idPuntoConsegna;
    private String numeroProgressivo;
    private String oraTrasporto;
    private String totaleImponibile;
    private String trasporto;
    private Cliente cliente;
    private vo.PuntoConsegna puntoConsegna;
    private Collection<Cliente> listClienti;
    // private vo.Autista autista;

    public DDTViewModel(DDT ddt) {

        if (ddt != null && ddt.getId() != null) {
            annoContabile = ddt.getAnnoContabile().toString();
            colli = ddt.getColli().toString();
            data = ddt.getDataForModal().toString();
            dataTrasporto = ddt.getDataTrasportoForModal();
            id = ddt.getId().toString();
            idCliente = ddt.getIdCliente().toString();
            idPuntoConsegna = ddt.getIdPuntoConsegna().toString();
            numeroProgressivo = ddt.getNumeroProgressivo().toString();
            oraTrasporto = ddt.getOraTrasportoForModal();
            totaleImponibile = ddt.calcolaTotale().toString();
            trasporto = ddt.getTrasporto();
            puntoConsegna = ddt.getPuntoConsegna();
            cliente = ddt.getCliente();
            idAutista = ddt.getIdAutista().toString();
            // autista = ddt.getAutista();
        }
    }

    public DDTViewModel() {

    }

    public String getDataTrasporto() {
        return dataTrasporto;
    }

    public void setDataTrasporto(String dataTrasporto) {
        this.dataTrasporto = dataTrasporto;
    }

    public String getColli() {
        return colli;
    }

    public void setColli(String colli) {
        this.colli = colli;
    }

    public String getAnnoContabile() {
        return annoContabile;
    }

    public void setAnnoContabile(String annoContabile) {
        this.annoContabile = annoContabile;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdPuntoConsegna() {
        return idPuntoConsegna;
    }

    public void setIdPuntoConsegna(String idPuntoConsegna) {
        this.idPuntoConsegna = idPuntoConsegna;
    }

    public String getNumeroProgressivo() {
        return numeroProgressivo;
    }

    public void setNumeroProgressivo(String numeroProgressivo) {
        this.numeroProgressivo = numeroProgressivo;
    }

    public String getOraTrasporto() {
        return oraTrasporto;
    }

    public void setOraTrasporto(String oraTrasporto) {
        this.oraTrasporto = oraTrasporto;
    }

    public String getTotaleImponibile() {
        return totaleImponibile;
    }

    public void setTotaleImponibile(String totaleImponibile) {
        this.totaleImponibile = totaleImponibile;
    }

    public String getTrasporto() {
        return trasporto;
    }

    public void setTrasporto(String trasporto) {
        this.trasporto = trasporto;
    }

    public Collection<Iva> getIvas() {
        try {
            Ivas ivas = new Ivas();
            return ivas.getElements();
        } catch (DataAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public Collection<Autista> getAutistis() {
        try {
            Autisti auts = new Autisti();
            return auts.getElements();
        } catch (DataAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public PuntoConsegna getPuntoConsegna() {
        return puntoConsegna;
    }

    public void setPuntoConsegna(PuntoConsegna puntoConsegna) {
        this.puntoConsegna = puntoConsegna;
    }

    public DDT MapDDT(DDT ddt) throws ParseException, NumberFormatException, DataAccessException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date data = formatter.parse(getData());
        Date dataTrasporto = formatter.parse(getDataTrasporto());

        Calendar today = Calendar.getInstance();
        String ora = getOraTrasporto();
        String[] time = ora.split(":");
        Integer hour = Integer.parseInt(time[0]);
        Integer minute = Integer.parseInt(time[1]);
        today.set(Calendar.HOUR_OF_DAY, hour);
        today.set(Calendar.MINUTE, minute);
        Date oraTrasporto = today.getTime();

        DecimalFormat formatterD = new DecimalFormat();
        formatterD.setParseBigDecimal(true);
        BigDecimal totaleImponibile = (BigDecimal) formatterD.parse(getTotaleImponibile());

        if (getAnnoContabile().equals("")) {
            Integer year = Calendar.getInstance().get(Calendar.YEAR);
            setAnnoContabile(year.toString());
        }

        ddt.setAnnoContabile(Integer.parseInt(getAnnoContabile()));
        ddt.setColli(Integer.parseInt(getColli()));
        ddt.setData(data);
        ddt.setDataTrasporto(dataTrasporto);
        ddt.setIdCliente(Integer.parseInt(getIdCliente()));
        ddt.setIdAutista(Integer.parseInt(getIdAutista()));
        ddt.setIdPuntoConsegna(Integer.parseInt(getIdPuntoConsegna()));

        if (getNumeroProgressivo().equals("")) {
            ddt.setNumeroProgressivo(null);
        } else {
            ddt.setNumeroProgressivo(Integer.parseInt(getNumeroProgressivo()));
        }

        ddt.setOraTrasporto(oraTrasporto);
        ddt.setTotaleImponibile(totaleImponibile);
        ddt.setTrasporto(getTrasporto());
        ddt.setPuntoConsegna(getPuntoConsegna());
        ddt.setCliente(getCliente());

        Clienti clienti = new Clienti();
        Cliente cliente = clienti.find(Integer.parseInt(getIdCliente()));
        ddt.setCliente(cliente);

        Autisti auts = new Autisti();
        Autista aut = auts.find(Integer.parseInt(getIdAutista()));
        ddt.setAutista(aut);

        if (getPuntoConsegna() == null) {
            PuntiConsegna pcs = new PuntiConsegna();
            PuntoConsegna pc = pcs.find(Integer.parseInt(getIdPuntoConsegna()));
            ddt.setPuntoConsegna(pc);
        }

        if (ddt.getCausale() == null) {
            ddt.setCausale("vendita");
        }

        if (ddt.getAspettoEsteriore() == null) {
            ddt.setAspettoEsteriore("scatole");
        }

        return ddt;
    }

    // public vo.Autista getAutista() {
    // return autista;
    // }
    //
    // public void setAutista(vo.Autista autista) {
    // this.autista = autista;
    // }

    public String getIdAutista() {
        return idAutista;
    }

    public void setIdAutista(String idAutista) {
        this.idAutista = idAutista;
    }

    @SuppressWarnings("unchecked")
    public Collection<Cliente> getListClienti() {
        if (listClienti == null) {
            try {
                Clienti dao = new Clienti();
                dao.setOrderByDescrizione(Clienti.ORDER_ASC);
                listClienti = (Collection<Cliente>) dao.getElements();
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
        return listClienti;
    }

}
