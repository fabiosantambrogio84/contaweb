package stampe;

import java.util.Collection;
import java.util.Date;

import dao.Fatture;
import formatters.CommerciantiParser;
import web.GenericAction;

// CLAUDIO: modifiche CONAD
public class ExportCommercianti extends GenericAction {

    private static final long serialVersionUID = 5386226024102800837L;

    private Date daData = null;

    private Date alData = null;

    protected String textResponse = null;

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

    @Override
    public String execute() {
        Fatture fat = new Fatture();
        if (daData != null && alData != null) {
            fat.setFilterDataDa(daData);
            fat.setFilterDataA(alData);
            fat.setOrderByCliente();
            try {
                Collection<?> fatture = fat.getElements();
                CommerciantiParser parser = new CommerciantiParser();
                textResponse = parser.creaCsv(fatture);
            } catch (Exception e) {
                // TODO: handle exception
                textResponse = "Messaggio di errore - " + e.getMessage();
                return "success";
            }
        } else {
            textResponse = "Messaggio di errore - Devi impostare le date Da e A";
        }
        return "success";
    }
}
