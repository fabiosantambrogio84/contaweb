package formatters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import utils.CSVUtils;
import vo.Cliente;
import vo.Fattura;

public class CommerciantiParser implements ConadParser {

    private static final String[] CSV_FATTURE_HEADERS = new String[] { "CFIS-DITTA", "REGISTRO", "MESE", "IDPAESE", "PARTITA-IVA",
            "CFIS-CLIFOR", "DENOMINAZIONE", "COGNOME", "NOME", "TIPO-DOCUMENTO", "DATA-FATTURA(GGMMAAAA)", "NUM-FATTURA", "DATA-REG",
            "IMPONIBILE", "IMPOSTA", "ALIQUOTA", "NATURA", "DETRAIBILE(PERC)", "DEDUCIBILE(S/N)", "ESIGIB-IVA", "SEDE-INDIR",
            "SEDE-NUM", "SEDE-CAP", "SEDE-COMUNE", "SEDE-PROV", "SEDE-NAZ", "SORG-INDIR", "SORG-NUM", "SORG-CAP", "SORG-COMUNE",
            "SORG-PROV", "SORG-NAZ", "RFIS-ID-PAESE", "RFIS-PIVA", "RFIS-DENOM", "RFIS-NOME", "RFIS-COGNOME" };

    private static final String DEFAULT_DATE_PATTERN = "ddMMyyyy";

    private static final String DEFAULT_DECIMAL_PATTERN = "#.00";

    private static final String DEFAULT_EMPTY = "";

    private static final String CODICE_FISCALE = "RBNGPP56A14L551I";

    private static final String REGISTRO = "E";

    private static final String ID_PAESE = "IT";

    private static final String TIPO_DOCUMENTO = "TD01 Fattura";

    private static final String ESIGIBILITA_IMMEDIATA = "I";

    private SimpleDateFormat sdf = new SimpleDateFormat();

    private DecimalFormat df = new DecimalFormat();

    @SuppressWarnings("deprecation")
    @Override
    public String format(Collection<?> listaFatture) {
        StringBuilder sb = new StringBuilder();

        sdf.applyPattern("ddMMyyyy");

        df = new DecimalFormat();
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
        df.applyPattern("#.00");

        sb.append(
                "CFIS-DITTA;REGISTRO;MESE;IDPAESE;PARTITA-IVA;CFIS-CLIFOR;DENOMINAZIONE;COGNOME;NOME;TIPO-DOCUMENTO;DATA-FATTURA(GGMMAAAA);NUM-FATTURA;DATA-REG;IMPONIBILE;IMPOSTA;ALIQUOTA;NATURA;DETRAIBILE(PERC);DEDUCIBILE(S/N);ESIGIB-IVA;SEDE-INDIR;SEDE-NUM;SEDE-CAP;SEDE-COMUNE;SEDE-PROV;SEDE-NAZ;SORG-INDIR;SORG-NUM;SORG-CAP;SORG-COMUNE;SORG-PROV;SORG-NAZ;RFIS-ID-PAESE;RFIS-PIVA;RFIS-DENOM;RFIS-NOME;RFIS-COGNOME");
        sb.append("\n");

        for (Object fatt : listaFatture) {
            Fattura fattura = (Fattura) fatt;
            fattura.calcolaTotali();
            Cliente cliente = fattura.getCliente();
            HashMap<BigDecimal, BigDecimal[]> imponibili = fattura.getImponibili();

            for (BigDecimal aliquota : imponibili.keySet()) {
                // CFIS-DITTA;REGISTRO;MESE;IDPAESE;PARTITA-IVA;CFIS-CLIFOR;DENOMINAZIONE;COGNOME;NOME;TIPO-DOCUMENTO;
                // DATA-FATTURA(GGMMAAAA);NUM-FATTURA;DATA-REG;IMPONIBILE;IMPOSTA;ALIQUOTA;
                // NATURA;DETRAIBILE(PERC);DEDUCIBILE(S/N);ESIGIB-IVA;SEDE-INDIR;SEDE-NUM;SEDE-CAP;SEDE-COMUNE;SEDE-PROV;SEDE-NAZ;SORG-INDIR;SORG-NUM;SORG-CAP;SORG-COMUNE;SORG-PROV;SORG-NAZ;RFIS-ID-PAESE;RFIS-PIVA;RFIS-DENOM;RFIS-NOME;RFIS-COGNOME
                // RBNGPP56A14L551I;E;12;IT;4151300235;4151300235;ADAMI MONICA ALIMENTARI;;;TD01 Fattura;
                // 31072017;1527;31072017;64,36;6,44;10;;;;I;;;;;;;;;;;;;;;;;
                sb.append("RBNGPP56A14L551I");
                sb.append(";");
                sb.append("E");
                sb.append(";");
                sb.append(fattura.getData().getMonth() + 1);
                sb.append(";");
                sb.append("IT");
                sb.append(";");
                sb.append(cliente.getPiva() != null ? cliente.getPiva() : cliente.getCodiceFiscale());
                sb.append(";");
                sb.append(cliente.getCodiceFiscale() != null ? cliente.getCodiceFiscale() : "");
                sb.append(";");
                sb.append(cliente.getRs().replaceAll(";", " ").toUpperCase());
                if (cliente.getRs2() != null && cliente.getRs2().trim().length() > 0)
                    sb.append(" " + cliente.getRs2().replaceAll(";", " ").toUpperCase());
                sb.append(";");
                sb.append(";");
                sb.append(";");
                sb.append("TD01 Fattura");
                sb.append(";");
                sb.append(sdf.format(fattura.getData()));
                sb.append(";");
                sb.append(fattura.getNumeroProgressivo());
                sb.append(";");
                sb.append(sdf.format(fattura.getData()));
                sb.append(";");
                sb.append(df.format(imponibili.get(aliquota)[0]));
                sb.append(";");
                sb.append(df.format(imponibili.get(aliquota)[1]));
                sb.append(";");
                sb.append(df.format(aliquota.doubleValue()));
                sb.append(";");
                sb.append(";");
                sb.append(";");
                sb.append(";");
                sb.append("");
                sb.append(";");
                sb.append(";;;;;;;;;;;;;;;;");
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public String creaCsv(Collection<?> listaFatture) throws Exception {
        List<String> rows = new ArrayList<>();

        /* aggiungo la riga degli headers */
        rows.add(CSVUtils.writeLine(Arrays.asList(CSV_FATTURE_HEADERS)));

        /* pattern date */
        sdf.applyPattern(DEFAULT_DATE_PATTERN);

        /* pattern decimali */
        df = new DecimalFormat(DEFAULT_DECIMAL_PATTERN);
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));

        /* itera su ogni fattura */
        for (Object fatt : listaFatture) {
            Fattura fattura = (Fattura) fatt;
            fattura.calcolaTotali();
            Cliente cliente = fattura.getCliente();
            HashMap<BigDecimal, BigDecimal[]> imponibili = fattura.getImponibili();

            /* itera su ogni imponibile della fattura corrente */
            for (BigDecimal aliquota : imponibili.keySet()) {
                /* inizializza la riga da inserire nel csv */
                List<String> values = new ArrayList<>();

                /* recupero la partita iva del cliente */
                String clientePartitaIva = cliente.getPiva();

                /* recupero il codice fiscale del cliente */
                String clienteCodiceFiscale = cliente.getCodiceFiscale();

                /* aggiungo CFIS-DITTA */
                values.add(CODICE_FISCALE);

                /* aggiungo REGISTRO */
                values.add(REGISTRO);

                /* aggiungo MESE */
                values.add(getMese(fattura.getData()));

                /* aggiungo IDPAESE */
                values.add(ID_PAESE);

                /* aggiungo PARTITA_IVA */
                values.add(clientePartitaIva);

                /* aggiungo CFIS-CLIFOR */
                if (clientePartitaIva != null && clientePartitaIva != "") {
                    values.add(clientePartitaIva);
                } else {
                    values.add(clienteCodiceFiscale);
                }

                /* aggiungo DENOMINAZIONE */
                if (!cliente.isDittaIndividuale()) {
                    String clienteDenominazione = cliente.getRs().toUpperCase();
                    if (cliente.getRs2() != null && cliente.getRs2() != "") {
                        clienteDenominazione += " " + cliente.getRs2().toUpperCase();
                    }
                    values.add(clienteDenominazione);
                } else {
                    values.add(DEFAULT_EMPTY);
                }

                /* aggiungo COGNOME */
                if (cliente.isDittaIndividuale()) {
                    values.add(cliente.getCognome());
                } else {
                    values.add(DEFAULT_EMPTY);
                }

                /* aggiungo NOME */
                if (cliente.isDittaIndividuale()) {
                    values.add(cliente.getNome());
                } else {
                    values.add(DEFAULT_EMPTY);
                }

                /* aggiungo TIPO DOCUMENTO */
                values.add(TIPO_DOCUMENTO);

                /* aggiungo DATA FATTURA */
                values.add(sdf.format(fattura.getData()));

                /* aggiungo NUM FATTURA */
                values.add(String.valueOf(fattura.getNumeroProgressivo()));

                /* aggiungo DATA-REG */
                values.add(sdf.format(fattura.getData()));

                /* aggiungo IMPONIBILE */
                values.add(df.format(imponibili.get(aliquota)[0]));

                /* aggiungo IMPOSTA */
                values.add(df.format(imponibili.get(aliquota)[1]));

                /* aggiungo ALIQUOTA */
                values.add(df.format(aliquota.doubleValue()));

                /* aggiungo NATURA */
                values.add(DEFAULT_EMPTY);

                /* aggiungo DETRAIBILE(PERC) */
                values.add(DEFAULT_EMPTY);

                /* aggiungo DEDUCIBILE(S/N) */
                values.add(DEFAULT_EMPTY);

                /* aggiungo ESIGIB-IVA */
                values.add(ESIGIBILITA_IMMEDIATA);

                /* aggiungo SEDE-INDIR */
                values.add(DEFAULT_EMPTY);

                /* aggiungo SEDE-NUM */
                values.add(DEFAULT_EMPTY);

                /* aggiungo SEDE-CAP */
                values.add(cliente.getCap());

                /* aggiungo SEDE-COMUNE */
                values.add(DEFAULT_EMPTY);

                /* aggiungo SEDE-PROV */
                String clienteProvincia = cliente.getProv();
                if (clienteProvincia != null && clienteProvincia != "") {
                    values.add(clienteProvincia);
                } else {
                    values.add(DEFAULT_EMPTY);
                }

                /* aggiungo SEDE-NAZ */
                values.add(DEFAULT_EMPTY);

                /* aggiungo SORG-INDIR */
                values.add(DEFAULT_EMPTY);

                /* aggiungo SORG-NUM */
                values.add(DEFAULT_EMPTY);

                /* aggiungo SORG-CAP */
                values.add(DEFAULT_EMPTY);

                /* aggiungo SORG-COMUNE */
                values.add(DEFAULT_EMPTY);

                /* aggiungo SORG-PROV */
                values.add(DEFAULT_EMPTY);

                /* aggiungo SORG-NAZ */
                values.add(DEFAULT_EMPTY);

                /* aggiungo RFIS-ID-PAESE */
                values.add(DEFAULT_EMPTY);

                /* aggiungo RFIS-PIVA */
                values.add(DEFAULT_EMPTY);

                /* aggiungo RFIS-DENOM */
                values.add(DEFAULT_EMPTY);

                /* aggiungo RFIS-NOME */
                values.add(DEFAULT_EMPTY);

                /* aggiungo RFIS-COGNOME */
                values.add(DEFAULT_EMPTY);

                /* aggiungo la riga alla lista di righe */
                rows.add(CSVUtils.writeLine(values));
            }
        }
        return CSVUtils.writeToString(rows);
    }

    private String getMese(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        if (month <= 5) {
            return "6";
        } else {
            return "12";
        }
    }

}
