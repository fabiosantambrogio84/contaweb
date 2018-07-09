package web;

import api.DdtApi;
import dto.DDTActionViewModel;

public class DdtApiAction extends GenericAction {

    private String id;
    private String otherId;
    private String index;
    private String prodottiRimossi;
    private DDTActionViewModel ddtObject;
    private String term;

    private static final long serialVersionUID = 1L;

    @Override
    @SuppressWarnings("unchecked")
    public String execute() throws Exception {

        return SUCCESS;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCliente() throws Exception {

        try {
            DdtApi api = new DdtApi();
            setTextResponse(api.getCliente(Integer.parseInt(id)).toString());
            return SUCCESS;
        } catch (Exception ex) {

        }

        return ERROR;
    }

    public String getClienteSelect2() throws Exception {

        try {
            DdtApi api = new DdtApi();
            setTextResponse(api.getClienteSelect2(term).toString());
            return SUCCESS;
        } catch (Exception ex) {

        }

        return ERROR;
    }

    public String getArticoloSelect2() throws Exception {

        try {
            DdtApi api = new DdtApi();
            setTextResponse(api.getArticoliSelect2(term).toString());
            return SUCCESS;
        } catch (Exception ex) {

        }

        return ERROR;
    }

    public String getClienteDestinazione() throws Exception {

        try {
            DdtApi api = new DdtApi();
            setTextResponse(api.getClienteDestinazione(Integer.parseInt(id), Integer.parseInt(index)).toString());
            return SUCCESS;
        } catch (Exception ex) {

        }

        return ERROR;
    }

    public String getArticolo() throws Exception {

        try {
            DdtApi api = new DdtApi();
            setTextResponse(api.getArticolo(Integer.parseInt(id), Integer.parseInt(otherId)).toString());
            return SUCCESS;
        } catch (Exception ex) {

        }

        return ERROR;
    }

    public String saveChanges() throws Exception {

        try {
            DdtApi api = new DdtApi();
            setTextResponse(api.saveDDT(ddtObject).toString());
            return SUCCESS;
        } catch (Exception ex) {

        }

        return ERROR;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getProdottiRimossi() {
        return prodottiRimossi;
    }

    public void setProdottiRimossi(String prodottiRimossi) {
        this.prodottiRimossi = prodottiRimossi;
    }

    public DDTActionViewModel getDdtObject() {
        return ddtObject;
    }

    public void setDdtObject(DDTActionViewModel ddtObject) {
        this.ddtObject = ddtObject;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getClienteDestinazioni() throws Exception {
        /* recupero i punti di consegna del cliente */
        try {
            DdtApi api = new DdtApi();
            setTextResponse(api.getClienteDestinazioni(Integer.parseInt(id)).toString());
            return SUCCESS;
        } catch (Exception ex) {
        }

        return ERROR;
    }

}
