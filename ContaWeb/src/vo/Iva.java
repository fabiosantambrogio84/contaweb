package vo;

public class Iva extends VOElement {

    private static final long serialVersionUID = 4932182164185778296L;

    private Integer valore = null;

    public Integer getValore() {
        return valore;
    }

    public void setValore(Integer valore) {
        this.valore = valore;
    }

    @Override
    public String toString() {
        if (valore == null)
            return "0";
        return getValore().toString();
    }

}
