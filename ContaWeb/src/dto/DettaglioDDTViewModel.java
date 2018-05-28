package dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Vector;

import vo.DettaglioDDT;
import vo.EvasioneOrdine;

public class DettaglioDDTViewModel implements Serializable {

    private static final long serialVersionUID = 1L;

    public DettaglioDDTViewModel(DettaglioDDT ddt, Vector evasioniOrdine) {

        if (ddt != null && ddt.getIdArticolo() != null) {
            codiceArticolo = ddt.getCodiceArticolo();
            descrizioneArticolo = ddt.getDescrizioneArticolo();
            idArticolo = ddt.getIdArticolo().toString();
            iva = ddt.getIva().toString();
            lotto = ddt.getLotto();
            pezzi = ddt.getPezzi().toString();
            prezzo = ddt.getPrezzo().toString();
            qta = ddt.getQta().toString();
            sconto = ddt.getSconto().toString();
            um = ddt.getUm().toString();
            id = ddt.getId().toString();

            Integer evasioni = 0;
            if (evasioniOrdine != null) {

                Iterator itr2 = evasioniOrdine.iterator();
                while (itr2.hasNext()) {
                    EvasioneOrdine eo = (EvasioneOrdine) itr2.next();
                    evasioni += eo.getDettaglioOrdine().getPezziDaEvadere();
                }
            }

            setPezziEvasi(evasioni.toString());
        }
    }

    public DettaglioDDTViewModel() {

    }

    private String codiceArticolo;
    private String descrizioneArticolo;
    private String idArticolo;
    private String iva;
    private String lotto;
    private String pezzi;
    private String prezzo;
    private String qta;
    private String sconto;
    private String um;
    private String id;
    private String pezziEvasi;

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    public String getIdArticolo() {
        return idArticolo;
    }

    public void setIdArticolo(String idArticolo) {
        this.idArticolo = idArticolo;
    }

    public String getDescrizioneArticolo() {
        return descrizioneArticolo;
    }

    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.descrizioneArticolo = descrizioneArticolo;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getLotto() {
        return lotto;
    }

    public void setLotto(String lotto) {
        this.lotto = lotto;
    }

    public String getPezzi() {
        return pezzi;
    }

    public void setPezzi(String pezzi) {
        this.pezzi = pezzi;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    public String getQta() {
        return qta;
    }

    public void setQta(String qta) {
        this.qta = qta;
    }

    public String getSconto() {
        return sconto;
    }

    public void setSconto(String sconto) {
        this.sconto = sconto;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DettaglioDDT MapDettaglioDDT(DettaglioDDT ddt) throws ParseException {

        DecimalFormat formatterD = new DecimalFormat();
        formatterD.setParseBigDecimal(true);
        BigDecimal iva = (BigDecimal) formatterD.parse(getIva());
        BigDecimal prezzo = (BigDecimal) formatterD.parse(getPrezzo());
        BigDecimal qta = (BigDecimal) formatterD.parse(getQta());
        BigDecimal sconto = (BigDecimal) formatterD.parse(getSconto());

        ddt.setCodiceArticolo(getCodiceArticolo());
        ddt.setDescrizioneArticolo(getDescrizioneArticolo());
        ddt.setIdArticolo(Integer.parseInt(getIdArticolo()));
        ddt.setIva(iva);
        ddt.setLotto(getLotto());
        ddt.setPezzi(Integer.parseInt(getPezzi()));
        ddt.setPrezzo(prezzo);
        ddt.setQta(qta);
        ddt.setSconto(sconto);
        ddt.setUm(getUm());
        if (getId() != "0") {
            ddt.setId(Integer.parseInt(getId()));
        } else {
            ddt.setId(null);
        }

        return ddt;
    }

    public String getPezziEvasi() {
        return pezziEvasi;
    }

    public void setPezziEvasi(String pezziEvasi) {
        this.pezziEvasi = pezziEvasi;
    }
}
