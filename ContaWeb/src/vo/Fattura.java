package vo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

public class Fattura extends VOElement {
    private static final long serialVersionUID = -9089372456122908253L;
    private Integer numeroProgressivo = null;
    private Integer annoContabile = null;
    private Integer idCliente = null;
    private Cliente cliente = null;
    private String causale = null;
    private BigDecimal sconto = null;
    private Date data = null;
    private String noteFattura = null;
    private Boolean pagato = null;
    private Boolean spedito = null;
    private Vector dettagliFattura = null;
    private BigDecimal acconto = null;
    private BigDecimal totaleImponibile = null;
    private BigDecimal totaleImposta = null;
    private BigDecimal totaleMerce = null;
    private BigDecimal totaleFattura = null;
    private HashMap<BigDecimal, BigDecimal[]> imponibili = null;

    public String getDescrizioneBreveDDT() {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
        return "Fattura n. " + this.numeroProgressivo + " del " + date.format(this.data);
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

    public Date getData() {
        return this.data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getIdCliente() {
        return this.idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNoteFattura() {
        return this.noteFattura;
    }

    public void setNoteFattura(String noteFattura) {
        this.noteFattura = noteFattura;
    }

    public Boolean getPagato() {
        return this.pagato;
    }

    public void setPagato(Boolean pagato) {
        this.pagato = pagato;
    }

    public BigDecimal getSconto() {
        return this.sconto;
    }

    public void setSconto(BigDecimal sconto) {
        this.sconto = sconto;
    }

    public Vector getDettagliFattura() {
        return this.dettagliFattura;
    }

    public void setDettagliFattura(Vector dettagliFattura) {
        this.dettagliFattura = dettagliFattura;
    }

    public BigDecimal getTotaleFattura() {
        return this.totaleFattura;
    }

    public BigDecimal getDaPagare() {
        return getTotaleFattura().subtract(acconto);
    }

    public BigDecimal getTotaleMerce() {
        return this.totaleMerce;
    }

    public Boolean getFatturato() {
        return Boolean.valueOf(false);
    }

    public BigDecimal getTotaleImponibile() {
        return this.totaleImponibile;
    }

    public BigDecimal getTotaleImposta() {
        return this.totaleImposta;
    }

    public void calcolaTotali() {
        this.imponibili = new HashMap<BigDecimal, BigDecimal[]>();
        this.totaleImponibile = new BigDecimal(0);
        this.totaleImposta = new BigDecimal(0);

        for (int i = 0; i < this.dettagliFattura.size(); i++) {
            DDT ddt = (DDT) this.dettagliFattura.get(i);
            for (int j = 0; j < ddt.getDettagliDDT().size(); j++) {
                DettaglioDDT dt = (DettaglioDDT) ddt.getDettagliDDT().get(j);
                BigDecimal iva = new BigDecimal(dt.getIva());
                BigDecimal imponibile = dt.calcolaImponibile();

                if (this.imponibili.containsKey(iva)) {
                    BigDecimal totale = this.imponibili.get(iva)[0];
                    this.imponibili.get(iva)[0] = totale.add(imponibile);
                } else {
                    this.imponibili.put(iva, new BigDecimal[2]);
                    this.imponibili.get(iva)[0] = imponibile;
                    this.imponibili.get(iva)[1] = new BigDecimal(0);
                }

                this.totaleImponibile = this.totaleImponibile.add(imponibile);
            }
        }

        for (BigDecimal iva : this.imponibili.keySet()) {
            BigDecimal imponibile = this.imponibili.get(iva)[0];
            BigDecimal imposta = imponibile.multiply(iva.divide(new BigDecimal(100)).setScale(2, 4)).setScale(2, 0);

            this.imponibili.get(iva)[1] = this.imponibili.get(iva)[1].add(imposta);
            this.totaleImposta = this.totaleImposta.add(imposta);
        }

        this.totaleMerce = this.totaleImponibile.add(this.totaleImposta);

        BigDecimal sconto = this.totaleMerce.multiply(this.sconto.divide(new BigDecimal(100)).setScale(2, 4));
        this.totaleFattura = this.totaleMerce.subtract(sconto);
    }

    public HashMap<BigDecimal, BigDecimal[]> getImponibili() {
        return this.imponibili;
    }

    @Override
    public Integer getStato() {
        if (getPagato().booleanValue()) {
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

    public Integer getNumeroProgressivo() {
        return this.numeroProgressivo;
    }

    public void setNumeroProgressivo(Integer numeroProgressivo) {
        this.numeroProgressivo = numeroProgressivo;
    }

    public void setAcconto(BigDecimal acconto) {
        this.acconto = acconto;
    }

    public BigDecimal getAcconto() {
        return this.acconto;
    }

    public Boolean getSpedito() {
        return spedito;
    }

    public void setSpedito(Boolean spedito) {
        this.spedito = spedito;
    }

    @Override
    public String toString(){
    	StringBuilder sb = new StringBuilder();
        sb.append("id: ").append(getId()).append(", ");
        sb.append("numeroProgressivo: ").append(getNumeroProgressivo()).append(", ");
        sb.append("annoContabile: ").append(getAnnoContabile()).append(", ");
        sb.append("idCliente: ").append(getIdCliente()).append(", ");
        sb.append("cliente: ").append(getCliente()).append(", ");
        sb.append("causale: ").append(getCausale()).append(", ");
        sb.append("sconto: ").append(getSconto()).append(", ");
        sb.append("data: ").append(getData()).append(", ");
        sb.append("noteFattura: ").append(getNoteFattura()).append(", ");
        sb.append("pagato: ").append(getPagato()).append(", ");
        sb.append("spedito: ").append(getSpedito()).append(", ");
        sb.append("dettagliFattura: ").append(getDettagliFattura()).append(", ");
        sb.append("acconto: ").append(getAcconto()).append(", ");
        sb.append("totaleImponibile: ").append(getTotaleImponibile()).append(", ");
        sb.append("totaleImposta: ").append(getTotaleImposta()).append(", ");
        sb.append("totaleMerce: ").append(getTotaleMerce()).append(", ");
        sb.append("totaleFattura: ").append(getTotaleFattura()).append(", ");
        sb.append("imponibili: ").append("[");
        if(imponibili != null && !imponibili.isEmpty()){
            for (Map.Entry<BigDecimal, BigDecimal[]> entry : imponibili.entrySet()) {
                sb.append(entry.getKey()).append(": {");
                BigDecimal[] tempArray = entry.getValue();
                for(int i=0; i<tempArray.length; i++){
                	sb.append(tempArray[0]).append(", ");
                }
                sb.append("}; ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
