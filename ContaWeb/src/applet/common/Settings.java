package applet.common;

public class Settings {

    private static Settings settings = null;

    public static final String actionSuffix = ".do";
    private String basePath = "";
    private String printUrl = null;
    private String printNotaUrl = null;
    private String baseActionUrl = null;

    public static final String actionListaFornitori = "listaFornitori";
    public static final String actionListaClienti = "listaClienti";
    public static final String actionListaArticoli = "listaArticoli";
    public static final String actionGetCliente = "getCliente";
    public static final String actionGetPrezzoArticolo = "getPrezzoArticolo";
    public static final String actionGetPrezzoArticoloDaBC = "getPrezzoArticoloDaBC";
    public static final String actionGetDDT = "getDDT";
    public static final String actionGetNotaAccredito = "getNotaAccredito";
    public static final String actionListaIva = "listaIva";
    public static final String actionSaveDDT = "saveDDT";
    public static final String actionSaveDDT2 = "saveDDT2";
    public static final String actionSaveNotaAccredito = "saveNotaAccredito";
    public static final String actionGetArticolo = "getArticolo";
    public static final String actionGetArticoloByBarCode = "getArticoloByBarCode";
    public static final String actionGetFornitore = "getFornitore";
    public static final String actionSaveBollaAcquisto = "saveBollaAcquisto";
    public static final String actionGetBollaAcquisto = "getBollaAcquisto";
    public static final String actionGetListaDettagliOrdini = "getDettagliOrdini";
    public static final String actionGetListaOrdiniDaEvadere = "getOrdiniDaEvadere";
    public static final String actionCancellaRigaOrdine = "cancRigaOrdine";

    public String getBaseActionUrl() {
        if (baseActionUrl == null)
            baseActionUrl = basePath + "applet/";
        return baseActionUrl;
    }

    public void setBaseActionUrl(String baseActionUrl) {
        this.baseActionUrl = baseActionUrl;
    }

    public void setBasePath(String databasePath) {
        //this.basePath = databasePath.replace("ContaWeb", "ContaWeb_v1.0.1");
        this.basePath = databasePath;
    }

    public static Settings getInstance() {
        if (settings == null) {
            settings = new Settings();
        }
        return settings;
    }

    public String getBasePath() {
        return basePath;
    }

    public char getHeader() {
        return '@';
    }

    public char getTerminator() {
        return '\n';
    }

    public String getPrintUrl() {
        if (printUrl == null)
            printUrl = basePath + "printDDT" + actionSuffix;
        return printUrl;
    }

    public String getPrintNotaUrl() {
        if (printNotaUrl == null)
            printNotaUrl = basePath + "printNotaAccredito" + actionSuffix;
        return printNotaUrl;
    }

    public void setPrintNotaUrl(String printUrl) {
        this.printNotaUrl = printUrl;
    }

    public void setPrintUrl(String printUrl) {
        this.printUrl = printUrl;
    }

}
