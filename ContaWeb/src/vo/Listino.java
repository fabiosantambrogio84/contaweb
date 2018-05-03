package vo;

public class Listino extends VOElement {

    private static final long serialVersionUID = -3104370428732288500L;

    private String descrizione;

    private Integer idListinoRef;

    private Listino listinoRef;

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getIdListinoRef() {
        return idListinoRef;
    }

    public void setIdListinoRef(Integer idListinoRef) {
        this.idListinoRef = idListinoRef;
    }

    public Listino getListinoRef() {
        return listinoRef;
    }

    public void setListinoRef(Listino listinoRef) {
        this.listinoRef = listinoRef;
    }

}
