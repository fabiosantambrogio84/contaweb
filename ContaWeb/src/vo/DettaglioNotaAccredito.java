package vo;

import java.math.BigDecimal;

public class DettaglioNotaAccredito extends VOElement {

    private static final long serialVersionUID = -7302540693232770767L;

    private Integer idArticolo;
    private String codiceArticolo;
    private String descrizioneArticolo;
    private String um;
    private BigDecimal qta;
    private BigDecimal prezzo;
    private BigDecimal sconto;
    private BigDecimal totale;
    private Integer iva;
    private String lotto;
    private NotaAccredito notaAccredito;
    private Integer idNotaAccredito;
    private Articolo articolo;
    private Integer pezzi;

    public Integer getPezzi() {
        return pezzi;
    }

    public void setPezzi(Integer pezzi) {
        this.pezzi = pezzi;
    }

    public BigDecimal calcolaImponibile() {
        BigDecimal totale = new BigDecimal(0);

        if (prezzo != null && qta != null)
            totale = prezzo.multiply(qta).setScale(2, BigDecimal.ROUND_HALF_UP);

        if (sconto != null) {
            BigDecimal totaleSconto = totale.multiply(sconto.divide(new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_UP)))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            totale = totale.subtract(totaleSconto);
        }

        return totale;
    }

    public Articolo getArticolo() {
        return articolo;
    }

    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
    }

    public NotaAccredito getNotaAccredito() {
        return notaAccredito;
    }

    public void setNotaAccredito(NotaAccredito ddt) {
        this.notaAccredito = ddt;
    }

    public Integer getIdNotaAccredito() {
        return idNotaAccredito;
    }

    public void setIdNotaAccredito(Integer idNotaAccredito) {
        this.idNotaAccredito = idNotaAccredito;
    }

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    public String getDescrizioneArticolo() {
        return descrizioneArticolo;
    }

    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.descrizioneArticolo = descrizioneArticolo;
    }

    public Integer getIva() {
        return iva;
    }

    public void setIva(Integer iva) {
        this.iva = iva;
    }

    public String getLotto() {
        return lotto;
    }

    public void setLotto(String lotto) {
        this.lotto = lotto;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo.setScale(2);
    }

    public BigDecimal getQta() {
        return qta;
    }

    public void setQta(BigDecimal qta) {
        this.qta = qta.setScale(3);
    }

    public BigDecimal getSconto() {
        return sconto;
    }

    public void setSconto(BigDecimal sconto) {
        this.sconto = sconto;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public Integer getIdArticolo() {
        return idArticolo;
    }

    public void setIdArticolo(Integer idArticolo) {
        this.idArticolo = idArticolo;
    }

}
