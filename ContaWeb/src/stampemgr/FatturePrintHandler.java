package stampemgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import dao.DataAccessException;
import dao.RiepilogoFatture;
import dao.Settings;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import vo.VOElement;

public class FatturePrintHandler extends DocumentPrintHandler {

    private RiepilogoFatture fattura;

    public FatturePrintHandler(VOElement element) {
        fattura = (RiepilogoFatture) element;
    }

    @SuppressWarnings("unchecked")
    private Map preparaParametriStampaFattura(RiepilogoFatture fattura) throws DataAccessException {
        Map params = new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yy");

        params.put("dataDal", fattura.getDataDal());
        params.put("dataAl", fattura.getDataAl());
        params.put("totale", fattura.getTotale());

        return params;
    }

    @SuppressWarnings("unchecked")
    private JRDataSource preparaDettagliStampaFattura(RiepilogoFatture fattura) throws DataAccessException {
        JRBeanArrayDataSource dataSource;

        dataSource = new JRBeanArrayDataSource(fattura.getListaFatture().toArray());
        return dataSource;
    }

    @Override
    public byte[] creaPDF() throws Exception {
        File file = null;
        FileOutputStream fos = null;

        try {

            InputStream jspTemplate = null;

            if (this.fattura.getTipo() == "NP") {
                jspTemplate = ServletActionContext.getServletContext().getResourceAsStream("/stampe/RiepilogoFattureNonPagate.jasper");
            } else if (this.fattura.getTipo() == "CC") {
                jspTemplate = ServletActionContext.getServletContext().getResourceAsStream("/stampe/RiepilogoFattureCC.jasper");
            } else {
                jspTemplate = ServletActionContext.getServletContext().getResourceAsStream("/stampe/RiepilogoFatture.jasper");
            }

            JRDataSource ds = preparaDettagliStampaFattura(fattura);
            Map params = preparaParametriStampaFattura(fattura);

            params.put("REPORT_LOCALE", Locale.ITALIAN);
            byte[] filePDF = JasperRunManager.runReportToPdf(jspTemplate, params, ds);

            String percorsoFile = buildPDFPath(true);

            // SALVO IL PDF
            file = new File(percorsoFile);
            file.createNewFile();
            fos = new FileOutputStream(file);
            fos.write(filePDF);
            fos.flush();
            fos.close();

            return filePDF;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (fos != null)
                fos.close();
        }
    }

    public String getPDFPath() {
        return buildPDFPath(false);
    }

    private String buildPDFPath(boolean mkdirs) {
        File file = null;
        Calendar cal = Calendar.getInstance();
        // cal.setTime(fattura.getDataDal());
        String ps = System.getProperty("file.separator");
        String percorsoAnno = Settings.getInstance().getValue("print.pdfRepository") + "" + "RiepilogoFatture" + ps
                + cal.get(Calendar.YEAR);
        String percorsoMese = percorsoAnno + ps + (cal.get(Calendar.MONTH) + 1);

        cal.setTime(fattura.getDataDal());

        String annoDAL = "" + cal.get(Calendar.YEAR);
        String meseDAL = "" + cal.get(Calendar.MONTH) + 1;
        String giornoDAL = "" + cal.get(Calendar.DAY_OF_MONTH);

        String dal = giornoDAL + "-" + meseDAL + "-" + annoDAL;

        cal.setTime(fattura.getDataAl());

        String annoAL = "" + cal.get(Calendar.YEAR);
        String meseAL = "" + cal.get(Calendar.MONTH) + 1;
        String giornoAL = "" + cal.get(Calendar.DAY_OF_MONTH);

        String al = giornoAL + "-" + meseAL + "-" + annoAL;

        String percorsoFile = "";
        if (fattura.getTipo() == "NP") {
            percorsoFile = percorsoMese + ps + "RiepilogoFattureNP_" + dal + "_" + al + ".pdf";
        } else if (fattura.getTipo() == "CC") {
            percorsoFile = percorsoMese + ps + "RiepilogoFattureCC_" + dal + "_" + al + ".pdf";
        } else {
            percorsoFile = percorsoMese + ps + "RiepilogoFatture_" + dal + "_" + al + ".pdf";
        }

        file = new File(percorsoFile);
        if (!file.isFile()) {
            if (mkdirs == false)
                return null;
            // CONTROLLO SE ESISTE L'ANNO

            file = new File(percorsoAnno);
            if (!file.isDirectory()) {
                // CREO LA DIRECTORY ANNO
                file.mkdir();
            }
            file = new File(percorsoMese);
            file.mkdir();
        }

        return percorsoFile;
    }

    @Override
    public boolean isPDF() {
        return buildPDFPath(false) != null;
    }

    @Override
    public byte[] leggiPDF() throws Exception {
        String path = buildPDFPath(false);
        File file = new File(path);
        FileInputStream rf = new FileInputStream(file);

        byte[] array = new byte[(int) file.length()];
        rf.read(array);
        return array;
    }

    @Override
    public void deletePDF() {
        if (isPDF()) {
            String path = buildPDFPath(false);
            File file = new File(path);
            file.delete();
        }
    }
}
