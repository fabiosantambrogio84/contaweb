package formatters;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import dao.DataAccessException;
import dao.Fatture;
import dao.PagamentiEseguiti;
import dao.PagamentiEseguitiAgg;
import vo.Cliente;
import vo.Fattura;
import vo.Pagamento;
import vo.PagamentoEseguito;
import vo.PagamentoEseguitoAgg;

public class RibaParser implements ConadParser {

    private static final Logger logger = Logger.getLogger(RibaParser.class);

    private static final String CODICE_SIA = "12L96";

    private static final String CODICE_FISCALE = "RBNGPP56A14L551I";

    private static final String CODICE_ABI = "02008";

    private static final String CODICE_CAB = "59760";

    private static final String CC = "000041279248";

    private SimpleDateFormat sdf = new SimpleDateFormat();

    private DecimalFormat df = new DecimalFormat();

    private PagamentiEseguiti pagamentiEseguiti = new PagamentiEseguiti();

    private PagamentiEseguitiAgg pagamentiEseguitiAgg = new PagamentiEseguitiAgg();

    private Fatture fattureDao = new Fatture();

    @Override
    public String format(Collection<?> fatture) throws Exception {
        logger.info("Inizio creazione file RiBa");

        /* applica il pattern per il formatter delle date */
        sdf.applyPattern("ddMMyy");

        /* applicat il pattern per il formatter dei numeri */
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
        df.applyPattern("#.00");

        /* creo lo string builder */
        StringBuilder sb = new StringBuilder();

        Date data_invio = new Date();
        int totale = 0;
        int importo = 0;
        int idx = 1;

        List<PagamentoFattura> pagamentiFatture = getPagamentiFatture(fatture);

        logger.info("Creo il record di testa IB");

        // RECORD DI TESTA IB
        sb.append(" ");
        sb.append("IB");
        sb.append(CODICE_SIA);
        sb.append(CODICE_ABI);
        sb.append(sdf.format(data_invio));
        sb.append(String.format("%1$-20s", "URBANI ALIMENTARI"));
        sb.append(String.format("%1$6s", "")); // Campo Libero
        sb.append(String.format("%1$59s", "")); // Campo Vuoto - Filler
        sb.append(String.format("%1$7s", "")); // Qualificatore flusso
        sb.append(String.format("%1$2s", "")); // Campo Vuoto - Filler
        sb.append("E"); // Divisa
        sb.append(" "); // Campo Vuoto - Filler
        sb.append(String.format("%1$5s", "")); // Campo non disponibile
        sb.append("\n");

        logger.info("Record di testa IB creato con successo");

        /* creo la lista di righe da inserire */
        List<RiBaRow> rows = new ArrayList<>();

        /* creo la mappa, con chiave la partita iva del cliente, per le fatture da non raggruppare */
        Map<String, Map<Date, List<PagamentoFattura>>> pagFattNotToBeGroupedByPartitaIva = new HashMap<>();
        pagamentiFatture.stream().filter(pf -> !pf.getCliente().isRaggruppaRiba()).forEach(pf -> {
            Date dataScadenza = pf.getDataScadenza();
            List<PagamentoFattura> pagFatt = new ArrayList<>();
            pagFatt.add(pf);
            Map<Date, List<PagamentoFattura>> mapByDate = new HashMap<>();
            mapByDate.put(dataScadenza, pagFatt);
            pagFattNotToBeGroupedByPartitaIva.put(pf.getCliente().getPiva(), mapByDate);
        });

        /*
         * raggruppo la lista delle fatture in base alla partita iva del cliente e alla data scadenza delle fatture (la lista conterra
         * N elementi)
         */
        Map<String, Map<Date, List<PagamentoFattura>>> pagFattToBeGroupedByPartitaIva = pagamentiFatture.stream()
                .filter(pf -> pf.getCliente().isRaggruppaRiba())
                .collect(Collectors.groupingBy(pf -> pf.getCliente().getPiva(), Collectors.groupingBy(pf -> pf.getDataScadenza())));

        /* merge delle due mappe */
        Map<String, Map<Date, List<PagamentoFattura>>> pagamentiFattureByPartitaIva = Stream
                .concat(pagFattNotToBeGroupedByPartitaIva.entrySet().stream(), pagFattToBeGroupedByPartitaIva.entrySet().stream())
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        /* itero sulla mappa */
        for (Map.Entry<String, Map<Date, List<PagamentoFattura>>> entry : pagamentiFattureByPartitaIva.entrySet()) {
            String partitaIva = entry.getKey();
            for (Map.Entry<Date, List<PagamentoFattura>> entry2 : entry.getValue().entrySet()) {
                List<PagamentoFattura> pagFattByPartitaIva = entry2.getValue();

                RiBaRow row = new RiBaRow();
                /* controllo se devo raggruppare i dati */
                if (pagFattByPartitaIva.size() == 1) {
                    PagamentoFattura pagFatt = pagFattByPartitaIva.get(0);
                    Cliente cliente = pagFatt.getCliente();
                    Fattura fattura = pagFatt.getFattura();
                    PagamentoEseguito pagamento = pagFatt.getPagamento();

                    row.setAbiCliente(cliente.getBancaABI());
                    row.setCabCliente(cliente.getBancaCAB());
                    row.setCapCliente(cliente.getCap());
                    row.setDataScadenza(pagFatt.getDataScadenza());
                    row.setDescrizionePagamento("Pagamento " + fattura.getDescrizioneBreveDDT());
                    row.setIdCliente(cliente.getId());
                    row.setIdPagamento(pagamento.getId());
                    row.setImporto((int) (fattura.getDaPagare().doubleValue() * 100));
                    row.setIndirizzoCliente(cliente.getIndirizzo());
                    row.setLocalitaCliente(cliente.getLocalita());
                    row.setPartitaIvaCliente(cliente.getPiva());
                    row.setRagioneSociale2Cliente(cliente.getRs2());
                    row.setRagioneSocialeCliente(cliente.getRs());
                    row.setLogNumProgressivo(String.valueOf(fattura.getNumeroProgressivo()));
                } else {
                    /* recupero il cliente (ne ho N ma hanno tutti gli stessi campi a parte la ragione sociale) */
                    Cliente cliente = pagFattByPartitaIva.stream().map(pf -> pf.getCliente()).findFirst().get();

                    /* calcolo l'importo */
                    Integer importoFattura = 0;
                    for (PagamentoFattura pagFatt : pagFattByPartitaIva) {
                        importoFattura = importoFattura + (int) (pagFatt.getFattura().getDaPagare().doubleValue() * 100);
                    }

                    /* calcolo la stringa dei numeri progressivi delle fatture */
                    String logNumProgressivo = pagFattByPartitaIva.stream()
                            .map(pf -> String.valueOf(pf.getFattura().getNumeroProgressivo())).collect(Collectors.joining(","));

                    /* registro il pagamento eseguito aggregato */
                    PagamentoEseguitoAgg pagEsegAgg = new PagamentoEseguitoAgg();
                    pagEsegAgg.setNote("Fatture: " + logNumProgressivo);
                    pagamentiEseguitiAgg.store(pagEsegAgg);

                    /* aggiorno i singoli pagamenti eseguiti settando l'id del pagamento eseguito aggregato */
                    for (PagamentoFattura pagFatt : pagFattByPartitaIva) {
                        PagamentoEseguito pagEseg = pagFatt.getPagamento();
                        pagEseg.setIdPagamentoEseguitoAgg(pagEsegAgg.getId());
                        pagamentiEseguiti.store(pagEseg);
                    }

                    row.setAbiCliente(cliente.getBancaABI());
                    row.setCabCliente(cliente.getBancaCAB());
                    row.setCapCliente(cliente.getCap());
                    row.setDataScadenza(entry2.getKey());
                    row.setDescrizionePagamento("Pagamento fatture '" + cliente.getRs() + "'");
                    row.setIdCliente(cliente.getId());
                    // TODO: set id pagamento
                    row.setIdPagamento(pagEsegAgg.getId());
                    row.setImporto(importoFattura);
                    row.setIndirizzoCliente(cliente.getIndirizzo());
                    row.setLocalitaCliente(cliente.getLocalita());
                    row.setPartitaIvaCliente(partitaIva);
                    row.setRagioneSociale2Cliente(null);
                    row.setRagioneSocialeCliente(cliente.getRs());
                    row.setLogNumProgressivo(logNumProgressivo);
                }
                rows.add(row);
            }
        }

        for (RiBaRow row : rows) {
            logger.info("Creazione record...");
            /* aggiorno il totale globale */
            totale += row.getImporto();

            /* setto l'indice della riga */
            row.setIndex(idx);

            /* recupero il numero progressivo da inserire nei log */
            String fatturaNumProgressivo = row.getLogNumProgressivo();

            // RECORD 14
            sb.append(" ");
            sb.append("14");
            sb.append(String.format("%07d", idx));
            sb.append(String.format("%1$12s", "")); // Campo Vuoto - Filler
            sb.append(sdf.format(row.getDataScadenza()));
            sb.append("30000");
            sb.append(String.format("%013d", importo));
            sb.append("-");
            sb.append(CODICE_ABI);
            sb.append(CODICE_CAB);
            sb.append(String.format("%1$12s", CC));
            sb.append(String.format("%1$5s", row.getAbiCliente().trim()));
            sb.append(String.format("%1$5s", row.getCabCliente().trim()));
            sb.append(String.format("%1$12s", "")); // Campo Vuoto - Filler
            sb.append(CODICE_SIA);
            sb.append("4");
            sb.append(String.format("%1$-16s", row.getIdCliente())); // Campo Vuoto - Filler
            sb.append(" ");
            sb.append(String.format("%1$5s", "")); // Campo Vuoto - Filler
            sb.append("E");
            sb.append("\n");

            logger.info("Record 14 creato con successo per la fattura '" + fatturaNumProgressivo + "'");

            // RECORD 20
            sb.append(" ");
            sb.append("20");
            sb.append(String.format("%07d", idx));
            sb.append(String.format("%1$-24s", "URBANI ALIMENTARI"));
            sb.append(String.format("%1$-24s", "Via 11 Settembre, 17")); // Campo Vuoto - Filler
            sb.append(String.format("%1$-24s", "37035 S. Giovanni")); // Campo Vuoto - Filler
            sb.append(String.format("%1$-24s", "Ilarione (VR)")); // Campo Vuoto - Filler
            sb.append(String.format("%1$14s", "")); // Campo Vuoto - Filler
            sb.append("\n");

            logger.info("Record 20 creato con successo per la fattura '" + fatturaNumProgressivo + "'");

            // RECORD 30
            sb.append(" ");
            sb.append("30");
            sb.append(String.format("%07d", idx));
            sb.append(String.format("%1$-30s", org.apache.commons.lang.StringUtils.left(row.getRagioneSocialeCliente(), 30)));
            sb.append(String.format("%1$-30s", org.apache.commons.lang.StringUtils.left(row.getRagioneSociale2Cliente(), 30)));
            sb.append(String.format("%1$-16s", row.getPartitaIvaCliente())); // Campo
                                                                             // Vuoto -
                                                                             // Filler

            logger.info("Record 30 creato con successo per la fattura '" + fatturaNumProgressivo + "'");

            sb.append(String.format("%1$34s", "")); // Campo Vuoto - Filler
            sb.append("\n");

            // RECORD 40
            sb.append(" ");
            sb.append("40");
            sb.append(String.format("%07d", idx));
            sb.append(String.format("%1$-30s", org.apache.commons.lang.StringUtils.left(row.getIndirizzoCliente(), 30)));
            sb.append(String.format("%1$5s", org.apache.commons.lang.StringUtils.left(row.getCapCliente(), 5)));
            sb.append(String.format("%1$-25s", org.apache.commons.lang.StringUtils.left(row.getLocalitaCliente(), 25)));
            sb.append(String.format("%1$50s", ""));
            sb.append("\n");

            logger.info("Record 40 creato con successo per la fattura '" + fatturaNumProgressivo + "'");

            // RECORD 50
            sb.append(" ");
            sb.append("50");
            sb.append(String.format("%07d", idx));
            sb.append(String.format("%1$-40s", org.apache.commons.lang.StringUtils.left(row.getDescrizionePagamento(), 40)));
            sb.append(String.format("%1$40s", ""));
            sb.append(String.format("%1$10s", ""));
            sb.append(String.format("%1$-16s", CODICE_FISCALE));
            sb.append(String.format("%1$4s", ""));
            sb.append("\n");

            logger.info("Record 50 creato con successo per la fattura '" + fatturaNumProgressivo + "'");

            // RECORD 51
            sb.append(" ");
            sb.append("51");
            sb.append(String.format("%07d", idx));
            sb.append(String.format("%010d", row.getIdPagamento()));
            sb.append(String.format("%1$20s", "URBANI ALIMENTARI"));
            sb.append(String.format("%1$15s", ""));
            sb.append(String.format("%1$10s", ""));
            sb.append(String.format("%1$6s", ""));
            sb.append(String.format("%1$49s", ""));
            sb.append("\n");

            logger.info("Record 51 creato con successo per la fattura '" + fatturaNumProgressivo + "'");

            // RECORD 70
            sb.append(" ");
            sb.append("70");
            sb.append(String.format("%07d", idx));
            sb.append(String.format("%1$78s", ""));
            sb.append(String.format("%1$12s", ""));
            sb.append(" ");
            sb.append(" ");
            sb.append(" ");
            sb.append(String.format("%1$17s", ""));
            sb.append("\n");

            logger.info("Record 70 creato con successo per la fattura '" + fatturaNumProgressivo + "'");

            idx++;
        }
        idx--;

        // for (Object obj : fatture) {
        // /* recupero la fattura */
        // Fattura fattura = (Fattura) obj;
        // /* calcolo i totali della fattura */
        // fattura.calcolaTotali();
        //
        // logger.info("Fattura " + fattura.getNumeroProgressivo());
        //
        // /* recupero il cliente e il tipo di pagamento */
        // Cliente cliente = fattura.getCliente();
        // Integer idPagamento = cliente.getIdPagamento();
        // Pagamento tipoPagamento = pagamenti.find(idPagamento);
        //
        // /* calcolo la data di scadenza a seconda del tipo di pagamento */
        // Date scadenza = getDataScadenza(fattura.getData(), tipoPagamento);
        //
        // /* calcolo l'importo della fattura e il totale complessivo */
        // importo = (int) (fattura.getDaPagare().doubleValue() * 100);
        // totale += importo;
        //
        // String descrizionePagamento = "Pagamento " + fattura.getDescrizioneBreveDDT();
        //
        // // RECORD 14
        // sb.append(" ");
        // sb.append("14");
        // sb.append(String.format("%07d", idx));
        // sb.append(String.format("%1$12s", "")); // Campo Vuoto - Filler
        // sb.append(sdf.format(scadenza));
        // sb.append("30000");
        // sb.append(String.format("%013d", importo));
        // sb.append("-");
        // sb.append(CODICE_ABI);
        // sb.append(CODICE_CAB);
        // sb.append(String.format("%1$12s", CC));
        // sb.append(String.format("%1$5s", cliente.getBancaABI().trim()));
        // sb.append(String.format("%1$5s", cliente.getBancaCAB().trim()));
        // sb.append(String.format("%1$12s", "")); // Campo Vuoto - Filler
        // sb.append(CODICE_SIA);
        // sb.append("4");
        // sb.append(String.format("%1$-16s", cliente.getId())); // Campo Vuoto - Filler
        // sb.append(" ");
        // sb.append(String.format("%1$5s", "")); // Campo Vuoto - Filler
        // sb.append("E");
        // sb.append("\n");
        //
        // logger.info("Record 14 creato con successo per la fattura " + fattura.getNumeroProgressivo());
        //
        // // RECORD 20
        // sb.append(" ");
        // sb.append("20");
        // sb.append(String.format("%07d", idx));
        // sb.append(String.format("%1$-24s", "URBANI ALIMENTARI"));
        // sb.append(String.format("%1$-24s", "Via 11 Settembre, 17")); // Campo Vuoto - Filler
        // sb.append(String.format("%1$-24s", "37035 S. Giovanni")); // Campo Vuoto - Filler
        // sb.append(String.format("%1$-24s", "Ilarione (VR)")); // Campo Vuoto - Filler
        // sb.append(String.format("%1$14s", "")); // Campo Vuoto - Filler
        // sb.append("\n");
        //
        // logger.info("Record 20 creato con successo per la fattura " + fattura.getNumeroProgressivo());
        //
        // // RECORD 30
        // sb.append(" ");
        // sb.append("30");
        // sb.append(String.format("%07d", idx));
        // sb.append(String.format("%1$-30s", org.apache.commons.lang.StringUtils.left(cliente.getRs(), 30)));
        // sb.append(String.format("%1$-30s", org.apache.commons.lang.StringUtils.left(cliente.getRs2(), 30)));
        // sb.append(String.format("%1$-16s", cliente.getPiva() != null ? cliente.getPiva() : cliente.getCodiceFiscale())); // Campo
        // // Vuoto -
        // // Filler
        //
        // logger.info("Record 30 creato con successo per la fattura " + fattura.getNumeroProgressivo());
        //
        // sb.append(String.format("%1$34s", "")); // Campo Vuoto - Filler
        // sb.append("\n");
        //
        // // RECORD 40
        // sb.append(" ");
        // sb.append("40");
        // sb.append(String.format("%07d", idx));
        // sb.append(String.format("%1$-30s", org.apache.commons.lang.StringUtils.left(cliente.getIndirizzo(), 30)));
        // sb.append(String.format("%1$5s", org.apache.commons.lang.StringUtils.left(cliente.getCap(), 5)));
        // sb.append(String.format("%1$-25s", org.apache.commons.lang.StringUtils.left(cliente.getLocalita(), 25)));
        // sb.append(String.format("%1$50s", ""));
        // sb.append("\n");
        //
        // logger.info("Record 40 creato con successo per la fattura " + fattura.getNumeroProgressivo());
        //
        // // RECORD 50
        // sb.append(" ");
        // sb.append("50");
        // sb.append(String.format("%07d", idx));
        // sb.append(String.format("%1$-40s", org.apache.commons.lang.StringUtils.left(descrizionePagamento, 40)));
        // sb.append(String.format("%1$40s", ""));
        // sb.append(String.format("%1$10s", ""));
        // sb.append(String.format("%1$-16s", CODICE_FISCALE));
        // sb.append(String.format("%1$4s", ""));
        // sb.append("\n");
        //
        // logger.info("Record 50 creato con successo per la fattura " + fattura.getNumeroProgressivo());
        //
        // // RECORD 51
        // sb.append(" ");
        // sb.append("51");
        // sb.append(String.format("%07d", idx));
        // sb.append(String.format("%010d", pagamento.getId()));
        // sb.append(String.format("%1$20s", "URBANI ALIMENTARI"));
        // sb.append(String.format("%1$15s", ""));
        // sb.append(String.format("%1$10s", ""));
        // sb.append(String.format("%1$6s", ""));
        // sb.append(String.format("%1$49s", ""));
        // sb.append("\n");
        //
        // logger.info("Record 51 creato con successo per la fattura " + fattura.getNumeroProgressivo());
        //
        // // RECORD 70
        // sb.append(" ");
        // sb.append("70");
        // sb.append(String.format("%07d", idx));
        // sb.append(String.format("%1$78s", ""));
        // sb.append(String.format("%1$12s", ""));
        // sb.append(" ");
        // sb.append(" ");
        // sb.append(" ");
        // sb.append(String.format("%1$17s", ""));
        // sb.append("\n");
        //
        // logger.info("Record 70 creato con successo per la fattura " + fattura.getNumeroProgressivo());
        //
        // idx++;
        // }
        // idx--;

        logger.info("Record creati con successo");

        // RECORD DI CODA EF
        sb.append(" ");
        sb.append("EF");
        sb.append(CODICE_SIA);
        sb.append(CODICE_ABI);
        sb.append(sdf.format(data_invio));
        sb.append(String.format("%1$-20s", "URBANI ALIMENTARI"));
        sb.append(String.format("%1$6s", "")); // Campo Libero
        sb.append(String.format("%07d", idx));
        sb.append(String.format("%015d", totale));
        sb.append(String.format("%015d", 0));
        sb.append(String.format("%07d", (idx * 7 + 2)));
        sb.append(String.format("%1$24s", "")); // Campo Vuoto - Filler
        sb.append("E"); // Divisa
        sb.append(" "); // Campo Vuoto - Filler
        sb.append(String.format("%1$5s", "")); // Campo non disponibile
        sb.append("\n");

        logger.info("File RiBa creato con successo");

        return sb.toString();
    }

    private Date getDataScadenza(Date dataFattura, Pagamento tipoPagamento) {
        Integer scadenza = tipoPagamento.getScadenza();
        ZonedDateTime date = ZonedDateTime.ofInstant(dataFattura.toInstant(), ZoneId.systemDefault())
                .with(TemporalAdjusters.firstDayOfMonth());
        switch (scadenza) {
        case 30:
            date = date.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
            break;
        case 60:
            date = date.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
            break;
        case 45:
            date = date.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).plusMonths(1).withDayOfMonth(15);
        default:
            break;
        }
        return Date.from(date.toInstant());
    }

    private List<PagamentoFattura> getPagamentiFatture(Collection<?> fatture) {
        List<PagamentoFattura> pagamentiFatture = new ArrayList<>();

        logger.info("Creo i pagamenti per le fatture");
        for (Object obj : fatture) {
            PagamentoFattura pagamentoFattura = new PagamentoFattura();

            /* recupero la fattura */
            Fattura fattura = (Fattura) obj;
            /* calcolo i totali della fattura */
            fattura.calcolaTotali();

            /* recupero il cliente e il tipo di pagamento */
            Cliente cliente = fattura.getCliente();
            Integer idPagamento = cliente.getIdPagamento();

            logger.info("Creazione pagamento per la fattura " + fattura.getNumeroProgressivo());

            /* creo il pagamento */
            PagamentoEseguito pagamento = new PagamentoEseguito();
            pagamento.setIdCliente(cliente.getId());
            pagamento.setCliente(cliente);
            pagamento.setData(new Date());
            pagamento.setFattura(fattura);
            pagamento.setDescrizione("Pagamento " + fattura.getDescrizioneBreveDDT());
            pagamento.setImporto(fattura.getDaPagare());
            pagamento.setIdPagamento(idPagamento);
            try {
                pagamentiEseguiti.store(pagamento);
                fattureDao.pagato(fattura, pagamento);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }

            pagamentoFattura.setFattura(fattura);
            pagamentoFattura.setPagamento(pagamento);

            /* considero il codice fiscale del cliente nel caso in cui non ci sia la partita iva */
            if (cliente.getPiva() == null || cliente.getPiva() == "") {
                cliente.setPiva(cliente.getCodiceFiscale());
            }
            pagamentoFattura.setCliente(cliente);

            /* calcolo la data di scadenza */
            Date dataScadenza = getDataScadenza(fattura.getData(), pagamento.getPagamento());
            pagamentoFattura.setDataScadenza(dataScadenza);

            logger.info("Pagamento creato con successo per la fattura " + fattura.getNumeroProgressivo());

            pagamentiFatture.add(pagamentoFattura);
        }
        logger.info("Pagamenti per le fatture creati con successo");

        return pagamentiFatture;
    }

    public static <T, K extends Comparable<K>> Collector<T, ?, TreeMap<K, List<T>>> sortedGroupingBy(Function<T, K> function) {
        return Collectors.groupingBy(function, TreeMap::new, Collectors.toList());
    }

    private class PagamentoFattura {

        private Fattura fattura;

        private Cliente cliente;

        private PagamentoEseguito pagamento;

        private Date dataScadenza;

        public Fattura getFattura() {
            return fattura;
        }

        public void setFattura(Fattura fattura) {
            this.fattura = fattura;
        }

        public Cliente getCliente() {
            return cliente;
        }

        public void setCliente(Cliente cliente) {
            this.cliente = cliente;
        }

        public PagamentoEseguito getPagamento() {
            return pagamento;
        }

        public void setPagamento(PagamentoEseguito pagamento) {
            this.pagamento = pagamento;
        }

        public Date getDataScadenza() {
            return dataScadenza;
        }

        public void setDataScadenza(Date dataScadenza) {
            this.dataScadenza = dataScadenza;
        }
    }

    private class RiBaRow {

        private Integer index;

        private Date dataScadenza;

        private Integer importo;

        private String abiCliente;

        private String cabCliente;

        private Integer idCliente;

        private String ragioneSocialeCliente;

        private String ragioneSociale2Cliente;

        private String partitaIvaCliente;

        private String indirizzoCliente;

        private String capCliente;

        private String localitaCliente;

        private String descrizionePagamento;

        private Integer idPagamento;

        private String logNumProgressivo;

        @SuppressWarnings("unused")
        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public Date getDataScadenza() {
            return dataScadenza;
        }

        public void setDataScadenza(Date dataScadenza) {
            this.dataScadenza = dataScadenza;
        }

        public Integer getImporto() {
            return importo;
        }

        public void setImporto(Integer importo) {
            this.importo = importo;
        }

        public String getAbiCliente() {
            return abiCliente;
        }

        public void setAbiCliente(String abiCliente) {
            this.abiCliente = abiCliente;
        }

        public String getCabCliente() {
            return cabCliente;
        }

        public void setCabCliente(String cabCliente) {
            this.cabCliente = cabCliente;
        }

        public Integer getIdCliente() {
            return idCliente;
        }

        public void setIdCliente(Integer idCliente) {
            this.idCliente = idCliente;
        }

        public String getRagioneSocialeCliente() {
            return ragioneSocialeCliente;
        }

        public void setRagioneSocialeCliente(String ragioneSocialeCliente) {
            this.ragioneSocialeCliente = ragioneSocialeCliente;
        }

        public String getRagioneSociale2Cliente() {
            return ragioneSociale2Cliente;
        }

        public void setRagioneSociale2Cliente(String ragioneSociale2Cliente) {
            this.ragioneSociale2Cliente = ragioneSociale2Cliente;
        }

        public String getPartitaIvaCliente() {
            return partitaIvaCliente;
        }

        public void setPartitaIvaCliente(String partitaIvaCliente) {
            this.partitaIvaCliente = partitaIvaCliente;
        }

        public String getIndirizzoCliente() {
            return indirizzoCliente;
        }

        public void setIndirizzoCliente(String indirizzoCliente) {
            this.indirizzoCliente = indirizzoCliente;
        }

        public String getCapCliente() {
            return capCliente;
        }

        public void setCapCliente(String capCliente) {
            this.capCliente = capCliente;
        }

        public String getLocalitaCliente() {
            return localitaCliente;
        }

        public void setLocalitaCliente(String localitaCliente) {
            this.localitaCliente = localitaCliente;
        }

        public String getDescrizionePagamento() {
            return descrizionePagamento;
        }

        public void setDescrizionePagamento(String descrizionePagamento) {
            this.descrizionePagamento = descrizionePagamento;
        }

        public Integer getIdPagamento() {
            return idPagamento;
        }

        public void setIdPagamento(Integer idPagamento) {
            this.idPagamento = idPagamento;
        }

        public String getLogNumProgressivo() {
            return logNumProgressivo;
        }

        public void setLogNumProgressivo(String logNumProgressivo) {
            this.logNumProgressivo = logNumProgressivo;
        }
    }

}
