package stampe;

import java.util.Collection;
import java.util.Date;

import dao.Fatture;
import formatters.RibaParser;
import web.GenericAction;

// CLAUDIO: modifiche CONAD
public class ExportRiba extends GenericAction {

    private static final long serialVersionUID = -8373624692273754907L;

    private Date daData = null;
    private Date alData = null;
    protected String textResponse = null;

    @Override
    public String execute() {
        Fatture fat = new Fatture();
        fat.setFilterDataDa(daData);
        fat.setFilterDataA(alData);
        fat.setFilterPagamento("ricevuta bancaria");
        fat.setFilterStato("02");

        try {
            Collection<?> fatture = fat.getElements();
            RibaParser parser = new RibaParser();
            textResponse = parser.format(fatture);
        } catch (Exception e) {
            // TODO: handle exception
            textResponse = "Messaggio di errore - " + e.getMessage();
            return "success";
        }

        return "success";
    }

    public Date getDaData() {
        return daData;
    }

    public void setDaData(Date daData) {
        this.daData = daData;
    }

    public Date getAlData() {
        return alData;
    }

    public void setAlData(Date alData) {
        this.alData = alData;
    }

    @Override
    public String getTextResponse() {
        return textResponse;
    }

    @Override
    public void setTextResponse(String textResponse) {
        this.textResponse = textResponse;
    }

}
