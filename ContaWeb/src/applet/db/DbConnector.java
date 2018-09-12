package applet.db;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import applet.common.Settings;
import vo.Articolo;
import vo.BollaAcquisto;
import vo.Cliente;
import vo.DDT;
import vo.EvasioneOrdine;
import vo.Fornitore;
import vo.NotaAccredito;
import vo.PrezzoConSconto;

public class DbConnector {

    private String replaceSpaces(String input) {
        return input.replaceAll(" ", "%20");
    }

    private Object getObject(String action, String[] paramsName, Object[] params) {
        String sUrl = null;
        try {
            sUrl = Settings.getInstance().getBaseActionUrl() + action + Settings.actionSuffix;
            // AGGIUNGO PARAMETRI
            if (params != null) {
                sUrl += "?" + paramsName[0] + "=" + params[0];

                for (int i = 1; i < params.length; ++i) {
                    sUrl += "&" + paramsName[i] + "=" + params[i];
                }
            }
            URL url = new java.net.URL(sUrl);
            URLConnection con = url.openConnection();
            con.setRequestProperty("Content-type", "application/x-java-serialized-object");
            ObjectInputStream in = new ObjectInputStream(con.getInputStream());
            Object returned = in.readObject();
            in.close();
            return returned;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Collection getListaClienti() {
        return (Collection) getObject(Settings.actionListaClienti, null, null);
    }

    public Collection getListaFornitori() {
        return (Collection) getObject(Settings.actionListaFornitori, null, null);
    }

    public Cliente getCliente(Integer id) {
        String[] paramsName = new String[1];
        Object[] params = new Object[1];

        paramsName[0] = "idCliente";
        params[0] = id;

        return (Cliente) getObject(Settings.actionGetCliente, paramsName, params);
    }

    public Articolo getArticolo(String id) {
        String[] paramsName = new String[1];
        Object[] params = new Object[1];

        paramsName[0] = "id";
        params[0] = replaceSpaces(id);

        return (Articolo) getObject(Settings.actionGetArticolo, paramsName, params);
    }

    public Collection getListaArticoli() {
        return (Collection) getObject(Settings.actionListaArticoli, null, null);
    }

    public Collection getListaArticoli(Fornitore fornitore) {
        String[] paramsName = new String[1];
        Object[] params = new Object[1];

        paramsName[0] = "idFornitore";
        params[0] = fornitore.getId();
        return (Collection) getObject(Settings.actionListaArticoli, paramsName, params);
    }

    public PrezzoConSconto getPrezzoArticolo(String codiceArticolo, Integer idCliente) {
        String[] paramsName = new String[3];
        Object[] params = new Object[3];

        paramsName[0] = "codiceArticolo";
        params[0] = replaceSpaces(codiceArticolo);

        paramsName[1] = "idCliente";
        params[1] = idCliente;

        paramsName[2] = "data";
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
        params[2] = df.format(new Date());

        return (PrezzoConSconto) getObject(Settings.actionGetPrezzoArticolo, paramsName, params);
    }

    public PrezzoConSconto getPrezzoArticoloFromBarcode(String barcode, Integer idCliente) throws Exception {
        String[] paramsName = new String[3];
        Object[] params = new Object[3];
        paramsName[0] = "barcode";
        params[0] = barcode;
        paramsName[1] = "idCliente";
        params[1] = idCliente;
        paramsName[2] = "data";
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
        params[2] = df.format(new Date());

        Object obj = getObject(Settings.actionGetPrezzoArticoloDaBC, paramsName, params);
        if (!(obj instanceof PrezzoConSconto))
            throw (Exception) obj;

        return (PrezzoConSconto) obj;
    }

    public Collection getListaIva() {
        return (Collection) getObject(Settings.actionListaIva, null, null);
    }

    // public Integer saveDDT(DDT ddt) throws Exception {
    public Integer saveDDT(DDT ddt, boolean updateOrdini) throws Exception {
        // String sUrl = Settings.getInstance().getBaseActionUrl() + Settings.actionSaveDDT + Settings.actionSuffix;
        String sUrl = "";
        if (updateOrdini)
            sUrl = Settings.getInstance().getBaseActionUrl() + Settings.actionSaveDDT + Settings.actionSuffix;
        else
            sUrl = Settings.getInstance().getBaseActionUrl() + Settings.actionSaveDDT2 + Settings.actionSuffix;
        URL url = new URL(sUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);
        con.setDefaultUseCaches(false);
        con.setRequestProperty("Content-Type", "java-internal/" + ddt.getClass().getName());

        ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
        out.writeObject(ddt);
        out.flush();
        out.close();

        InputStream ins = con.getInputStream();
        ObjectInputStream os = new ObjectInputStream(ins);
        Object obj = os.readObject();
        ins.close();

        if (obj instanceof Exception)
            throw (Exception) obj;

        return (Integer) obj;
    }

    public Integer saveNotaAccredito(NotaAccredito ddt) throws Exception {
        String sUrl = Settings.getInstance().getBaseActionUrl() + Settings.actionSaveNotaAccredito + Settings.actionSuffix;
        URL url = new URL(sUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);
        con.setDefaultUseCaches(false);
        con.setRequestProperty("Content-Type", "java-internal/" + ddt.getClass().getName());

        ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
        out.writeObject(ddt);
        out.flush();
        out.close();

        InputStream ins = con.getInputStream();
        ObjectInputStream os = new ObjectInputStream(ins);
        Object obj = os.readObject();
        ins.close();

        if (obj instanceof Exception)
            throw (Exception) obj;

        return (Integer) obj;
    }

    public DDT getDDT(long idDDT) {
        String[] paramsName = new String[1];
        Object[] params = new Object[1];
        paramsName[0] = "id";
        params[0] = idDDT;
        return (DDT) getObject(Settings.actionGetDDT, paramsName, params);
    }

    public NotaAccredito getNotaAccredito(long idDDT) {
        String[] paramsName = new String[1];
        Object[] params = new Object[1];
        paramsName[0] = "id";
        params[0] = idDDT;
        return (NotaAccredito) getObject(Settings.actionGetNotaAccredito, paramsName, params);
    }

    public Fornitore getFornitore(Integer idFornitore) {
        String[] paramsName = new String[1];
        Object[] params = new Object[1];
        paramsName[0] = "id";
        params[0] = idFornitore;
        return (Fornitore) getObject(Settings.actionGetFornitore, paramsName, params);
    }

    public Integer saveBollaAcquisto(BollaAcquisto ba) throws Exception {
        String sUrl = Settings.getInstance().getBaseActionUrl() + Settings.actionSaveBollaAcquisto + Settings.actionSuffix;
        URL url = new URL(sUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);
        con.setDefaultUseCaches(false);
        con.setRequestProperty("Content-Type", "java-internal/" + ba.getClass().getName());

        ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
        out.writeObject(ba);
        out.flush();
        out.close();

        InputStream ins = con.getInputStream();
        ObjectInputStream os = new ObjectInputStream(ins);
        Object obj = os.readObject();
        ins.close();

        if (obj instanceof Exception)
            throw (Exception) obj;

        return (Integer) obj;
    }

    public BollaAcquisto getBollaAcquisto(long id) {
        String[] paramsName = new String[1];
        Object[] params = new Object[1];
        paramsName[0] = "id";
        params[0] = id;
        return (BollaAcquisto) getObject(Settings.actionGetBollaAcquisto, paramsName, params);
    }

    public Articolo getArticolo(String id, Fornitore fornitore) {
        String[] paramsName = new String[2];
        Object[] params = new Object[2];

        paramsName[0] = "id";
        params[0] = replaceSpaces(id);

        paramsName[1] = "idFornitore";
        params[1] = fornitore.getId();

        Object obj = getObject(Settings.actionGetArticolo, paramsName, params);
        if (obj instanceof Articolo)
            return (Articolo) obj;
        else
            return null;
    }

    public Articolo getArticoloByBarCode(String id, Fornitore fornitore) {
        String[] paramsName = new String[2];
        Object[] params = new Object[2];

        paramsName[0] = "id";
        params[0] = replaceSpaces(id);

        paramsName[1] = "idFornitore";
        params[1] = fornitore.getId();

        Object obj = getObject(Settings.actionGetArticoloByBarCode, paramsName, params);
        if (obj instanceof Articolo)
            return (Articolo) obj;
        else
            return null;
    }

    public Collection getListaDettagliOrdini(Integer idCliente, Integer idPuntoConsegna, String data) {
        String[] paramsName = new String[3];
        Object[] params = new Object[3];

        paramsName[0] = "id";
        params[0] = idCliente;

        paramsName[1] = "idPuntoConsegna";
        params[1] = idPuntoConsegna;

        paramsName[2] = "date";
        params[2] = data;

        return (Collection) getObject(Settings.actionGetListaDettagliOrdini, paramsName, params);
    }

    public Boolean cancellaRigaOrdine(EvasioneOrdine eo) {
        String[] paramsName = new String[1];
        Object[] params = new Object[1];

        paramsName[0] = "id";
        params[0] = eo.getDettaglioOrdine().getId();
        return (Boolean) getObject(Settings.actionCancellaRigaOrdine, paramsName, params);
    }
}
