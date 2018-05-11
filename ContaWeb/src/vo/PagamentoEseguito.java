package vo;

import java.math.BigDecimal;
import java.util.Date;

import com.google.common.base.MoreObjects;

public class PagamentoEseguito extends VOElement {

    private static final long serialVersionUID = 4932182164185788296L;

    private Date data = null;

    private String descrizione;

    private String notes;

    private BigDecimal importo;

    private Integer idPagamento;

    private Pagamento pagamento;

    private Integer idCliente;

    private Cliente cliente;

    private Integer idFattura;

    private Fattura fattura;

    private Integer idDDT;

    private DDT ddt;

    private Integer idPagamentoEseguitoAgg;

    private PagamentoEseguitoAgg pagamentoEseguitoAgg;

    public Date getData() {
        return this.data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getImporto() {
        return this.importo;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    public Integer getIdPagamento() {
        return this.idPagamento;
    }

    public void setIdPagamento(Integer idpagamento) {
        this.idPagamento = idpagamento;
    }

    public Pagamento getPagamento() {
        return this.pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public Integer getIdCliente() {
        return this.idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Integer getIdFattura() {
        return this.idFattura;
    }

    public void setIdFattura(Integer idFattura) {
        this.idFattura = idFattura;
    }

    public Fattura getFattura() {
        return this.fattura;
    }

    public void setFattura(Fattura fattura) {
        this.fattura = fattura;
    }

    public Integer getIdDDT() {
        return this.idDDT;
    }

    public void setIdDDT(Integer idDDT) {
        this.idDDT = idDDT;
    }

    public DDT getDdt() {
        return this.ddt;
    }

    public void setDdt(DDT ddt) {
        this.ddt = ddt;
    }

    public Integer getIdPagamentoEseguitoAgg() {
        return idPagamentoEseguitoAgg;
    }

    public void setIdPagamentoEseguitoAgg(Integer idPagamentoEseguitoAgg) {
        this.idPagamentoEseguitoAgg = idPagamentoEseguitoAgg;
    }

    public PagamentoEseguitoAgg getPagamentoEseguitoAgg() {
        return pagamentoEseguitoAgg;
    }

    public void setPagamentoEseguitoAgg(PagamentoEseguitoAgg pagamentoEseguitoAgg) {
        this.pagamentoEseguitoAgg = pagamentoEseguitoAgg;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", getId()).add("stato", getStato()).add("data", getData())
                .add("descrizione", getDescrizione()).add("notes", getNotes()).add("importo", getImporto())
                .add("idPagamento", getIdPagamento()).add("pagamento", getPagamento()).add("idCliente", getIdCliente())
                .add("cliente", getCliente()).add("idFattura", getIdFattura()).add("fattura", getFattura()).add("idDDT", getIdDDT())
                .add("ddt", getDdt()).add("idPagamentoEseguitoAgg", getIdPagamentoEseguitoAgg())
                .add("pagamentoEseguitoAgg", getPagamentoEseguitoAgg()).toString();
    }
}
