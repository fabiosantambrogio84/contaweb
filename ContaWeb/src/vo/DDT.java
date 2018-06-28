package vo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Vector;

import dao.DDTs;
import dao.Ordini;

public class DDT extends VOElement {
    private static final long serialVersionUID = 3056413010720785672L;
    private Cliente cliente;
    private BigDecimal guadagnoPercentuale = BigDecimal.ZERO;
    private BigDecimal guadagno = BigDecimal.ZERO;
    private Ordine ordine = null;
    private Autista autista = null;
    private PuntoConsegna puntoConsegna;
    // 20161212
    private Integer idAutista;
    private Integer idCliente;
    // 20161212
    private Integer idOrdine;
    private Integer idPuntoConsegna;
    private Integer numeroProgressivo;
    private String numeroProgressivo2;
    private Integer annoContabile;
    private Date data;
    private Vector dettagliDDT;
    private Integer colli;
    private String trasporto;
    private String aspettoEsteriore;
    private Date dataTrasporto;
    private Date oraTrasporto;
    private String causale;
    private Boolean pagato = null;
    private BigDecimal acconto;
    private BigDecimal totaleImponibile;
    private BigDecimal totaleImposta;
    private BigDecimal totaleCosto;
    private Integer idFattura = null;
    private Fattura fattura = null;
    private Iva iva;

    public Fattura getFattura() {
        return this.fattura;
    }

    public void setFattura(Fattura fattura) {
        this.fattura = fattura;
    }

    public Integer getIdFattura() {
        return this.idFattura;
    }

    public void setIdFattura(Integer idFattura) {
        this.idFattura = idFattura;
    }

    public Boolean getFatturato() {
        if (this.idFattura != null) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    public String getAspettoEsteriore() {
        return this.aspettoEsteriore;
    }

    public void setAspettoEsteriore(String aspettoEsteriore) {
        this.aspettoEsteriore = aspettoEsteriore;
    }

    public String getCausale() {
        return this.causale;
    }

    public void setCausale(String causale) {
        this.causale = causale;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Integer getColli() {
        return this.colli;
    }

    public void setColli(Integer colli) {
        this.colli = colli;
    }

    public Date getData() {
        return this.data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getDataTrasporto() {
        return this.dataTrasporto;
    }

    public void setDataTrasporto(Date dataTrasporto) {
        this.dataTrasporto = dataTrasporto;
    }

    public Vector getDettagliDDT() {
        return this.dettagliDDT;
    }

    public void setDettagliDDT(Vector dettagliDDT) {
        this.dettagliDDT = dettagliDDT;
    }

    public Integer getNumeroProgressivo() {
        return this.numeroProgressivo;
    }

    public void setNumeroProgressivo(Integer numeroProgressivo) {
        this.numeroProgressivo = numeroProgressivo;
    }

    public Date getOraTrasporto() {
        return this.oraTrasporto;
    }

    public void setOraTrasporto(Date oraTrasporto) {
        this.oraTrasporto = oraTrasporto;
    }

    public PuntoConsegna getPuntoConsegna() {
        return this.puntoConsegna;
    }

    public void setPuntoConsegna(PuntoConsegna puntoConsegna) {
        this.puntoConsegna = puntoConsegna;
    }

    public String getTrasporto() {
        return this.trasporto;
    }

    public BigDecimal getDaPagare() {
        return getTotale().subtract(acconto);
    }

    public BigDecimal getTotale() {
        return calcolaTotale();
    }

    public void setTrasporto(String trasporto) {
        this.trasporto = trasporto;
    }

    public BigDecimal calcolaTotale() {
        return calcolaTotale(this.dettagliDDT);
    }

    public BigDecimal calcolaTotalePerApplet() {
        return calcolaTotalePerApplet(this.dettagliDDT);
    }

    public String getDataForModal() {
        try {
            String NEW_FORMAT = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(NEW_FORMAT);
            Date d = getData();
            sdf.applyPattern(NEW_FORMAT);
            return sdf.format(d);
        } catch (Exception ex) {
            // ignored
        }

        return "";
    }

    public String getDataTrasportoForModal() {
        try {

            String NEW_FORMAT = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(NEW_FORMAT);
            Date d = getDataTrasporto();
            sdf.applyPattern(NEW_FORMAT);
            return sdf.format(d);
        } catch (Exception ex) {
            // ignored
        }

        return "";
    }

    public String getOraTrasportoForModal() {
        try {

            String NEW_FORMAT = "hh:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(NEW_FORMAT);
            Date d = getOraTrasporto();
            sdf.applyPattern(NEW_FORMAT);
            return sdf.format(d);
        } catch (Exception ex) {
            // ignored
        }

        return "";
    }

    public BigDecimal calcolaTotale(Vector articoli) {
        BigDecimal totale = new BigDecimal(0);
        this.totaleImponibile = new BigDecimal(0);
        this.totaleImposta = new BigDecimal(0);
        this.totaleCosto = new BigDecimal(0);
        this.guadagnoPercentuale = new BigDecimal(0);
        if (articoli != null) {
            ListIterator itr = articoli.listIterator();
            BigDecimal[] imponibili = new BigDecimal[100];
            while (itr.hasNext()) {
                DettaglioDDT dettaglio = (DettaglioDDT) itr.next();
                Articolo tmpArticolo = new Articolo();
                tmpArticolo.setId(dettaglio.getIdArticolo());
                if (tmpArticolo.getId() != null) {
                    dao.Articoli tmpArticoli = new dao.Articoli();
                    try {
                        tmpArticolo = tmpArticoli.find(tmpArticolo.getId());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (tmpArticolo.getPrezzoAcquisto() != null) {
                        BigDecimal tmpPrezzoAcquisto = tmpArticolo.getPrezzoAcquisto();
                        tmpPrezzoAcquisto = tmpPrezzoAcquisto.multiply(dettaglio.getQta()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        totaleCosto = totaleCosto.add(tmpPrezzoAcquisto); // * dettaglio.getPezzi()
                    }

                }
                BigDecimal imponibile = dettaglio.calcolaImponibile();
                if (imponibili[dettaglio.getIva().intValue()] == null) {
                    imponibili[dettaglio.getIva().intValue()] = imponibile;
                } else {
                    imponibili[dettaglio.getIva().intValue()] = imponibili[dettaglio.getIva().intValue()].add(imponibile);
                }
            }
            for (int i = 0; i < 100; i++) {
                if (imponibili[i] != null) {
                    BigDecimal imp = imponibili[i];
                    BigDecimal totaleIva = imp.multiply(new BigDecimal(i).divide(new BigDecimal(100)).setScale(2, 4)).setScale(2, 0);
                    totale = totale.add(imp.add(totaleIva));
                    this.totaleImponibile = this.totaleImponibile.add(imp);
                    this.totaleImposta = this.totaleImposta.add(totaleIva);
                }
            }
            if (!totaleCosto.equals(BigDecimal.ZERO)) {
                double tmpGuadagno = totaleImponibile.doubleValue() - totaleCosto.doubleValue();
                guadagno = guadagno.valueOf(tmpGuadagno);
                guadagno = guadagno.setScale(2, BigDecimal.ROUND_HALF_UP);
                tmpGuadagno = tmpGuadagno / totaleCosto.doubleValue() * 100;
                guadagnoPercentuale = BigDecimal.valueOf(tmpGuadagno);
                guadagnoPercentuale = guadagnoPercentuale.setScale(0, BigDecimal.ROUND_HALF_UP);
            }
        }
        return totale;
    }

    public BigDecimal calcolaTotalePerApplet(Vector articoli) {
        BigDecimal totale = new BigDecimal(0);
        this.totaleImponibile = new BigDecimal(0);
        this.totaleImposta = new BigDecimal(0);
        if (articoli != null) {
            ListIterator itr = articoli.listIterator();

            BigDecimal[] imponibili = new BigDecimal[100];
            while (itr.hasNext()) {
                DettaglioDDT dettaglio = (DettaglioDDT) itr.next();
                BigDecimal imponibile = dettaglio.calcolaImponibile();
                if (imponibili[dettaglio.getIva().intValue()] == null) {
                    imponibili[dettaglio.getIva().intValue()] = imponibile;
                } else {
                    imponibili[dettaglio.getIva().intValue()] = imponibili[dettaglio.getIva().intValue()].add(imponibile);
                }
            }
            for (int i = 0; i < 100; i++) {
                if (imponibili[i] != null) {
                    BigDecimal imp = imponibili[i];
                    BigDecimal totaleIva = imp.multiply(new BigDecimal(i).divide(new BigDecimal(100)).setScale(2, 4)).setScale(2, 0);
                    totale = totale.add(imp.add(totaleIva));

                    this.totaleImponibile = this.totaleImponibile.add(imp);
                    this.totaleImposta = this.totaleImposta.add(totaleIva);
                }
            }
        }
        return totale;
    }

    public Integer getIdCliente() {
        return this.idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    // 20161212
    public Integer getIdOrdine() {
        return this.idOrdine;
    }

    // 20161212
    public void setIdOrdine(Integer idOrdine) {
        this.idOrdine = idOrdine;
    }

    public Integer getIdPuntoConsegna() {
        return this.idPuntoConsegna;
    }

    public void setIdPuntoConsegna(Integer idPuntoConsegna) {
        this.idPuntoConsegna = idPuntoConsegna;
    }

    public String getDescrizioneBreveDDT() {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
        return "DDT n. " + this.numeroProgressivo + " del " + date.format(this.data);
    }

    @Override
    public Integer getStato() {
        if ((getPagato() != null) && (getPagato().booleanValue())) {
            return Integer.valueOf(0);
        } else if (getAcconto() != null && getAcconto().compareTo(new BigDecimal(0)) > 0)
            return Integer.valueOf(5);

        return Integer.valueOf(1);
    }

    public Integer getAnnoContabile() {
        return this.annoContabile;
    }

    public void setAnnoContabile(Integer annoContabile) {
        this.annoContabile = annoContabile;
    }

    public String getNumeroProgressivo2() {
        return this.numeroProgressivo2;
    }

    public void setNumeroProgressivo2(String numeroProgressivo2) {
        this.numeroProgressivo2 = numeroProgressivo2;
    }

    public String getNumeroProgressivoCompleto() {
        if (!this.numeroProgressivo2.equalsIgnoreCase("")) {
            return this.numeroProgressivo.toString() + "/" + this.numeroProgressivo2;
        }
        return this.numeroProgressivo.toString();
    }

    public void setPagato(Boolean pagato) {
        this.pagato = pagato;
    }

    public Boolean getPagato() {
        return this.pagato;
    }

    public void setAcconto(BigDecimal acconto) {
        this.acconto = acconto;
    }

    public BigDecimal getAcconto() {
        return this.acconto;
    }

    public BigDecimal getTotaleImponibile() {
        return totaleImponibile;
    }

    public void setTotaleImponibile(BigDecimal totaleImponibile) {
        this.totaleImponibile = totaleImponibile;
    }

    public BigDecimal getTotaleCosto() {
        return totaleCosto;
    }

    public void setTotaleCosto(BigDecimal totaleCosto) {
        this.totaleCosto = totaleCosto;
    }

    public BigDecimal getTotaleImposta() {
        return totaleImposta;
    }

    public void setTotaleImposta(BigDecimal totaleImposta) {
        this.totaleImposta = totaleImposta;
    }

    /**
     * @return the ordine
     */
    public Ordine getOrdine() {
        // 20161212
        if (this.ordine == null) {
            if (this.idOrdine == null) {
                retrieveOrdineMatchingDDT();
                if (ordine != null)
                    idOrdine = ordine.getId();
            } else {
                ordine.setId(idOrdine);
                Ordini ordini = new Ordini();
                try {
                    ordine = (Ordine) ordini.findWithAllReferences(ordine);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (ordine != null) {
                DDTs tmpDDTs = new DDTs();
                try {
                    if (!tmpDDTs.store(this).equalsIgnoreCase("success"))
                        return null;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return ordine;
    }

    /**
     * @param ordine
     *            the ordine to set
     */
    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }

    /**
     * @return the status of update: 0 = done with success. others = failed.
     */
    private int updateAutistaFromOrdine() {
        int result = 1;
        Ordini tmpOrdini = new Ordini();
        Ordine tmpOrdine = new Ordine();
        tmpOrdini.setDefaultCriteria();
        Iterator itrOrdine = null;
        try {
            itrOrdine = tmpOrdini.getElements().iterator();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            result = 2;
        }

        if (!itrOrdine.hasNext())
            result = 3;

        DDT tmpDDT = new DDT();
        DDTs tmpDDTs = new DDTs();
        while (itrOrdine.hasNext()) {
            tmpOrdine = (Ordine) itrOrdine.next();
            tmpDDT = retrieveDDTMatchingOrdine(tmpOrdine);
            try {
                tmpDDT = (DDT) tmpDDTs.findWithAllReferences(tmpDDT);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                result = 4;
            }
            tmpDDT.getCliente().setAutista(tmpOrdine.getAutista());
            try {
                // 20161122
                if (tmpDDTs.store(tmpDDT).equalsIgnoreCase("success"))
                    result = 0;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                result = 5;
            }
        }
        return result;
    }

    /**
     * @update ordine related with current DDT
     */
    private DDT retrieveDDTMatchingOrdine(Ordine ordineArg) {
        DDT tmpDDT = null; // new DDT();
        if (ordineArg == null)
            return tmpDDT;
        DDTs tmpDDTs = new DDTs();
        // tmpDDTs.setDefaultCriteria(); // Devo estrarre tutti i DDT
        Integer numElementsOrdineArg = 0;
        Integer tmpIdCliente = ordineArg.getIdCliente();
        if (tmpIdCliente != null) {
            tmpDDT.setIdCliente(tmpIdCliente);
            Date tmpDataSpedizione = ordineArg.getDataSpedizione();
            if (tmpDataSpedizione != null)
                tmpDDT.setDataTrasporto(tmpDataSpedizione);
            try {
                tmpDDT = (DDT) tmpDDTs.findWithAllReferences(tmpDDT);
                if (tmpDDT != null) {
                    Iterator itrOrdineArg = null;
                    itrOrdineArg = ordineArg.getDettagliOrdine().iterator();
                    numElementsOrdineArg = ordineArg.getDettagliOrdine().size();

                    DettaglioOrdine tmpDettaglioOrdineArg;
                    DettaglioDDT tmpDettaglioDDT;
                    Integer tmpIdArticoloOrdineArg;
                    Integer tmpIdArticoloDDT;
                    Integer findFlag = 0;
                    while (itrOrdineArg.hasNext()) {
                        tmpDettaglioOrdineArg = (DettaglioOrdine) itrOrdineArg.next();
                        tmpIdArticoloOrdineArg = tmpDettaglioOrdineArg.getIdArticolo();
                        Iterator itrDDT = tmpDDT.dettagliDDT.iterator();
                        if (numElementsOrdineArg >= tmpDDT.dettagliDDT.size()) {
                            while (itrDDT.hasNext()) {
                                tmpDettaglioDDT = (DettaglioDDT) itrDDT.next();
                                tmpIdArticoloDDT = tmpDettaglioDDT.getIdArticolo();
                                if (tmpIdArticoloOrdineArg.intValue() == tmpIdArticoloDDT.intValue())
                                    findFlag++;
                            }
                        } else
                            tmpDDT = null;
                    }
                    if (findFlag != ordineArg.getDettagliOrdine().size())
                        tmpDDT = null;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return tmpDDT;
    }

    /**
     * @update ordine related with current DDT
     */
    private void retrieveOrdineMatchingDDT() {
        Ordine tmpOrdine = new Ordine();
        Ordini tmpOrdini = new Ordini();
        Integer numElements = 0;
        ordine = null;
        if (idCliente != null) {
            tmpOrdine.setIdCliente(idCliente);
            // if (annoContabile != null)
            // tmpOrdine.setAnnoContabile(annoContabile);
            // if (idPuntoConsegna != null)
            // tmpOrdine.setIdPuntoConsegna(idPuntoConsegna);
            if (dataTrasporto != null)
                tmpOrdine.setDataSpedizione(dataTrasporto);
            try {
                tmpOrdine = (Ordine) tmpOrdini.findWithAllReferences(tmpOrdine);
                if (tmpOrdine != null) {
                    Iterator itr = null;
                    itr = tmpOrdine.getDettagliOrdine().iterator();
                    numElements = tmpOrdine.getDettagliOrdine().size();
                    DettaglioOrdine tmpDettaglioOrdine;
                    DettaglioDDT tmpDettaglioDDT;
                    Integer tmpIdArticoloOrdine;
                    Integer tmpIdArticoloDDT;
                    Integer findFlag = 0;
                    while (itr.hasNext()) {
                        tmpDettaglioOrdine = (DettaglioOrdine) itr.next();
                        tmpIdArticoloOrdine = tmpDettaglioOrdine.getIdArticolo();
                        Iterator itrDDT = this.dettagliDDT.iterator();
                        if (numElements >= this.dettagliDDT.size()) {
                            while (itrDDT.hasNext()) {
                                tmpDettaglioDDT = (DettaglioDDT) itrDDT.next();
                                tmpIdArticoloDDT = tmpDettaglioDDT.getIdArticolo();
                                if (tmpIdArticoloOrdine.intValue() == tmpIdArticoloDDT.intValue())
                                    findFlag++;
                            }
                        } else
                            ordine = null;
                    }
                    if (findFlag == this.dettagliDDT.size())
                        ordine = tmpOrdine;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * @return the guadagnoPercentuale
     */
    public BigDecimal getGuadagnoPercentuale() {
        return guadagnoPercentuale;
    }

    /**
     * @param guadagnoPercentuale
     *            the guadagnoPercentuale to set
     */
    public void setGuadagnoPercentuale(BigDecimal guadagnoPercentuale) {
        this.guadagnoPercentuale = guadagnoPercentuale;
    }

    /**
     * @return the guadagno
     */
    public BigDecimal getGuadagno() {
        return guadagno;
    }

    /**
     * @param guadagno
     *            the guadagno to set
     */
    public void setGuadagno(BigDecimal guadagno) {
        this.guadagno = guadagno;
    }

    // 20170125
    /**
     * @return the idAutista
     */
    public Integer getIdAutista() {
        return idAutista;
    }

    // 20170125
    /**
     * @param idAutista
     *            the idAutista to set
     */
    public void setIdAutista(Integer idAutista) {
        this.idAutista = idAutista;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Collection<Iva> getIvas() {
        try {
            dao.Ivas ivas = new dao.Ivas();
            return ivas.getElements();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @return the autista
     */
    public Autista getAutista() {
        if (autista != null)
            return autista;

        if (idAutista == null) {
            if (ordine != null) {
                idAutista = ordine.getIdAutista();
            } else {
                ordine = getOrdine();
                if (idOrdine != null) {
                    if (ordine != null)
                        idAutista = ordine.getIdAutista();
                }
            }
        }
        if (idAutista == null)
            return null;

        autista = new Autista();
        autista.setId(idAutista);
        dao.Autisti autisti = new dao.Autisti();
        try {
            autista = (Autista) autisti.findWithAllReferences(autista);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (autista != null) {
            dao.DDTs tmpDDTs = new dao.DDTs();
            try {
                if (!tmpDDTs.store(this).equalsIgnoreCase("success"))
                    return null;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return autista;
    }

    /**
     * @param autista
     *            the autista to set
     */
    public void setAutista(Autista autista) {
        this.autista = autista;
    }

    public Iva getIva() {
        return iva;
    }

    public void setIva(Iva iva) {
        this.iva = iva;
    }

}
