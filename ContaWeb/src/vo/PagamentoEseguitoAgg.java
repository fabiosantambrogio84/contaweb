package vo;

import java.sql.Timestamp;

import com.google.common.base.MoreObjects;

public class PagamentoEseguitoAgg extends VOElement {

    private static final long serialVersionUID = -9145310597015515017L;

    private Timestamp dataCreazione;

    private String note;

    public void setDataCreazione(Timestamp dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Timestamp getDataCreazione() {
        return this.dataCreazione;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", getId()).add("stato", getStato())
                .add("dataCreazione", getDataCreazione()).add("note", getNote()).toString();
    }

}
