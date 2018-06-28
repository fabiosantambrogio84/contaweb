package applet.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.DateFormatter;

import applet.barcode.Barcode;
import applet.barcode.BarcodeHandler;
import applet.barcode.BarcodeSplitter;
import applet.common.DateFormattedVerifier;
import applet.common.NumberFormattedVerifier;
import applet.common.Settings;
import applet.db.DbConnector;
import vo.Articolo;
import vo.Cliente;
import vo.DDT;
import vo.DettaglioDDT;
import vo.DettaglioOrdine;
import vo.EvasioneOrdine;
import vo.Iva;
import vo.PrezzoConSconto;
import vo.PuntoConsegna;

public class DDTWindow {

    private static final int TABLE_COLUMNS = 13;
    private DDT currentDDT = null;
    private boolean modalitaAggiornamento = false;

    private JPanel contentPane = null;
    private JPanel panelIntestazione = null;
    private JPanel panelDettaglio = null;
    private JPanel panelSpedizione = null;
    private JPanel panelBottoni = null;
    private JPanel panelLettore = null;
    private JLabel labelSelezionaCliente = null;
    private JTextArea txaCliente = null;
    private JButton buttonTrovaCliente = null;
    private JTextArea txaDestCliente = null;
    private JButton buttonDestClientePrec = null;
    private JButton buttonDestClienteSuc = null;
    private JLabel labelDestCliente = null;
    private JLabel labelTrasportoCura = null;
    private JComboBox cmbTrasporto = null;
    // private JLabel labelAspettoEst = null;
    // private JTextField txtAspettoEsteriore = null;
    private JLabel labelColli = null;
    private JTextField txtColli = null;
    private JLabel labelDataTrasporto = null;
    private JLabel labelOraTrasporto = null;
    // private JLabel labelCausale = null;
    // private JTextField txtCausale = null;
    private JFormattedTextField txtOraTrasporto = null;
    private JFormattedTextField txtDataTrasporto = null;
    private JButton buttonNuovo = null;
    private JButton buttonModifica = null;
    private JButton buttonSSN = null;
    private JButton buttonSN = null;
    private JButton buttonStampa = null;
    private JCheckBox cbNumerAutomatico = null;
    private JTextField txtNumDDT = null;
    private JLabel labelNumDDT = null;
    private JTextField txtNumDDT2 = null;
    private JPanel panelDettaglioRiga = null;
    private JScrollPane scrollPane = null;
    private JTable table = null;

    private Cliente clienteSelezionato = null;
    private Collection dettagliOrdini = null;
    private ListIterator<PuntoConsegna> itrPuntiConsegna;
    private PuntoConsegna puntoConsegnaSelezionato = null;

    private JLabel lblCodice = null;
    private JLabel lblQta = null;
    private JLabel lblLotto = null;
    private JButton buttonInserisciRiga = null;
    private JButton buttonCancellaArticolo = null;
    private JButton buttonAnnulla = null;
    private JButton buttonOk = null;
    private JTextField txtCodiceArticolo = null;
    private JTextField txtQtaArticolo = null;
    private JTextField txtLottoArticolo = null;
    private JLabel lblDescrizioneArticolo = null;
    private JButton buttonTrovaArticolo = null;
    private JTextField txtDescrizioneArticolo = null;
    private JLabel lblUMArticolo = null;
    private JTextField txtUMArticolo = null;
    private JLabel lblPezzi = null;
    private JLabel lblListinoArticolo = null;
    private JTextField txtPezzi = null;
    private JLabel lblPrezzo = null;
    private JTextField txtPrezzoArticolo = null;
    private JLabel lblScontoArticolo = null;
    private JTextField txtScontoArticolo = null;
    private JLabel lblIva = null;
    private JComboBox cmbIva = null;
    private JTextField txtTotaleDDT = null;
    private JLabel lblTotaleDDT = null;
    private JComboBox cmbModalita = null;

    private JLabel lblStatoLettore = null;

    private boolean updateRow = false;
    private JFormattedTextField txtDataDDT = null;
    private JLabel lblDataDDT = null;
    private Articolo articolo = null;

    // private Collection clienti;
    // private Collection articoli;
    private ChooseDialog listaClienti;
    private ChooseDialog listaArticoli;
    private JApplet applet;

    private static final int COL_ID_ORDINI = 11;
    private static final int COL_LOTTO = 9;

    @SuppressWarnings("unchecked")
    private DDT getDDT() throws Exception {
        DDT ddt;
        if (modalitaAggiornamento)
            ddt = currentDDT;
        else
            ddt = new DDT();
        if (cbNumerAutomatico.isSelected())
            ddt.setNumeroProgressivo(null);
        else {
            if (txtNumDDT2.getText().equalsIgnoreCase("")) {
                ddt.setNumeroProgressivo(Integer.valueOf(txtNumDDT.getText()));
                ddt.setNumeroProgressivo2("");
            } else {
                ddt.setNumeroProgressivo(Integer.valueOf(txtNumDDT.getText()));
                ddt.setNumeroProgressivo2(txtNumDDT2.getText());
            }
        }
        if (clienteSelezionato == null)
            throw new Exception("Selezionare un cliente");
        ddt.setCliente(clienteSelezionato);

        if (puntoConsegnaSelezionato == null)
            throw new Exception("Selezionare un punto di consegna");

        ddt.setPuntoConsegna(puntoConsegnaSelezionato);

        if (table.getRowCount() == 0)
            throw new Exception("Inserire degli articoli");

        ddt.setDettagliDDT(getDettagliDDT());

        ddt.setColli(Integer.valueOf(txtColli.getText()));
        // ddt.setAspettoEsteriore(txtAspettoEsteriore.getText());
        ddt.setAspettoEsteriore("scatole");
        ddt.setTrasporto((String) cmbTrasporto.getSelectedItem());
        DateFormat data = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
        try {
            ddt.setData(data.parse(txtDataDDT.getText()));
            ddt.setDataTrasporto(data.parse(txtDataTrasporto.getText()));
            data = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
            ddt.setOraTrasporto(data.parse(txtOraTrasporto.getText()));
        } catch (ParseException e) {
            throw new Exception("Le date non sono corrette");
        }
        // ddt.setCausale(txtCausale.getText());
        ddt.setCausale("vendita");

        return ddt;
    }

    @SuppressWarnings("unchecked")
    private Vector getDettagliDDT() throws ParseException {
        Vector articoli = new Vector();
        NumberFormat nf = DecimalFormat.getInstance(Locale.ITALIAN);
        for (int i = 0; i < table.getRowCount(); ++i) {
            DettaglioDDT dettaglioDDT = new DettaglioDDT();
            if (((String) table.getModel().getValueAt(i, 9)).equalsIgnoreCase(""))
                continue;
            if (table.getModel().getValueAt(i, 10) != null)
                dettaglioDDT.setId((Integer) table.getModel().getValueAt(i, 10));
            dettaglioDDT.setCodiceArticolo((String) table.getValueAt(i, 0));
            dettaglioDDT.setDescrizioneArticolo((String) table.getValueAt(i, 1));
            dettaglioDDT.setQta(
                    new BigDecimal(nf.parse((String) table.getValueAt(i, 2)).doubleValue()).setScale(3, BigDecimal.ROUND_HALF_UP));
            dettaglioDDT.setPezzi((Integer) table.getValueAt(i, 3));
            dettaglioDDT.setPrezzo(
                    new BigDecimal(nf.parse((String) table.getValueAt(i, 5)).doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
            dettaglioDDT.setSconto(
                    new BigDecimal(nf.parse((String) table.getValueAt(i, 6)).doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
            dettaglioDDT.setIva((Integer) table.getValueAt(i, 7));
            dettaglioDDT.setUm((String) table.getValueAt(i, 8));
            dettaglioDDT.setLotto((String) table.getValueAt(i, COL_LOTTO));

            Vector<EvasioneOrdine> vector = new Vector<EvasioneOrdine>();
            dettaglioDDT.setEvasioniOrdini(vector);

            if (table.getModel().getValueAt(i, COL_ID_ORDINI) != null) {
                if (table.getModel().getValueAt(i, COL_ID_ORDINI) instanceof Collection)
                    dettaglioDDT.getEvasioniOrdini()
                            .addAll((Collection<? extends EvasioneOrdine>) table.getModel().getValueAt(i, COL_ID_ORDINI));
                else
                    dettaglioDDT.getEvasioniOrdini().add(table.getModel().getValueAt(i, COL_ID_ORDINI));
            }

            dettaglioDDT.setArticolo((Articolo) table.getModel().getValueAt(i, 12));
            if (dettaglioDDT.getArticolo() != null)
                dettaglioDDT.setIdArticolo(dettaglioDDT.getArticolo().getId());

            articoli.add(dettaglioDDT);
        }
        return articoli;
    }

    private boolean checkArticoloCorrente() {
        if (txtDescrizioneArticolo.getText().equalsIgnoreCase(""))
            return false;
        if (txtPrezzoArticolo.getText().equalsIgnoreCase(""))
            return false;
        if (txtUMArticolo.getText().equalsIgnoreCase(""))
            return false;
        if (txtQtaArticolo.getText().equalsIgnoreCase(""))
            return false;
        if (txtPezzi.getText().equalsIgnoreCase(""))
            return false;
        if (txtLottoArticolo.getText().equalsIgnoreCase(""))
            return false;

        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.parse(txtPrezzoArticolo.getText());
            numberFormat.parse(txtQtaArticolo.getText());
            numberFormat.parse(txtPezzi.getText());
            if (!txtScontoArticolo.getText().equalsIgnoreCase(""))
                numberFormat.parse(txtScontoArticolo.getText());
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private void setModalitaAggiornamentoArticolo(boolean modalita) {
        // contentPane.getRootPane().setDefaultButton(buttonOk);
        // txtCodiceArticolo.setEnabled(!modalita);
        // buttonTrovaArticolo.setEnabled(!modalita);
        // txtDescrizioneArticolo.setEnabled(!modalita);
        // txtUMArticolo.setEnabled(!modalita);
        txtQtaArticolo.setEnabled(modalita);
        txtPrezzoArticolo.setEnabled(modalita);
        txtScontoArticolo.setEnabled(modalita);
        txtPezzi.setEnabled(modalita);
        // cmbIva.setEnabled(!modalita);
        txtLottoArticolo.setEnabled(modalita);
        buttonInserisciRiga.setEnabled(!modalita);
        buttonOk.setEnabled(modalita);
        // buttonOk.setDefaultCapable(true);
        buttonAnnulla.setEnabled(modalita);
        if (modalita)
            txtCodiceArticolo.requestFocus();
        buttonCancellaArticolo.setEnabled(modalita);
        updateRow = modalita;
        table.setEnabled(!modalita);
    }

    private void editTable() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            // AGGIORNO I CAMPI E IMPOSTO MODALITA AGGIORNAMENTO RIGA
            txtCodiceArticolo.setText((String) table.getModel().getValueAt(selectedRow, 0));
            txtDescrizioneArticolo.setText((String) table.getModel().getValueAt(selectedRow, 1));
            txtQtaArticolo.setText((String) table.getModel().getValueAt(selectedRow, 2));
            if (table.getModel().getValueAt(selectedRow, 3) != null)
                txtPezzi.setText(table.getModel().getValueAt(selectedRow, 3).toString());
            else
                txtPezzi.setText("");
            txtPrezzoArticolo.setText((String) table.getModel().getValueAt(selectedRow, 5));
            txtScontoArticolo.setText((String) table.getModel().getValueAt(selectedRow, 6));
            BigDecimal iva = (BigDecimal) table.getModel().getValueAt(selectedRow, 7);
            txtUMArticolo.setText((String) table.getModel().getValueAt(selectedRow, 8));
            txtLottoArticolo.setText((String) table.getModel().getValueAt(selectedRow, COL_LOTTO));

            for (int i = 0; i < cmbIva.getItemCount(); ++i)
                if (iva.floatValue() == ((Iva) cmbIva.getItemAt(i)).getValore().floatValue())
                    cmbIva.setSelectedIndex(i);
            articolo = (Articolo) table.getModel().getValueAt(selectedRow, 12);
            setModalitaAggiornamentoArticolo(true);
        }
    }

    private void setPrezzoArticolo(PrezzoConSconto prezzo) {
        txtCodiceArticolo.setText(prezzo.getArticolo().getCodiceArticolo());
        txtDescrizioneArticolo.setText(prezzo.getArticolo().getDescrizione());
        txtUMArticolo.setText(prezzo.getArticolo().getUm());
        BigDecimal number = prezzo.getPrezzo();
        DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance(Locale.ITALIAN);
        formatter.applyPattern("####.00");
        number = number.setScale(2, BigDecimal.ROUND_HALF_UP);
        txtPrezzoArticolo.setText(formatter.format(number.doubleValue()));
        number = prezzo.getSconto();
        number = number.setScale(2, BigDecimal.ROUND_HALF_UP);
        formatter.applyPattern("####.##");
        txtScontoArticolo.setText(formatter.format(number.doubleValue()));
        BigDecimal iva = new BigDecimal(prezzo.getArticolo().getIva().getValore());
        for (int i = 0; i < cmbIva.getItemCount(); ++i)
            if (iva.floatValue() == ((Iva) cmbIva.getItemAt(i)).getValore().floatValue())
                cmbIva.setSelectedIndex(i);
        lblListinoArticolo.setText("Listino: " + prezzo.getListino().getDescrizione());
        articolo = prezzo.getArticolo();
    }

    private void inserisciArticoloCorrente() {
        Object[] dati = new Object[TABLE_COLUMNS];
        dati[0] = txtCodiceArticolo.getText();
        dati[1] = txtDescrizioneArticolo.getText();
        dati[2] = txtQtaArticolo.getText();
        dati[3] = Integer.valueOf(txtPezzi.getText());
        dati[4] = null;
        dati[5] = txtPrezzoArticolo.getText();
        dati[6] = txtScontoArticolo.getText();
        dati[7] = ((Iva) cmbIva.getSelectedItem()).getValore();
        dati[8] = txtUMArticolo.getText();
        dati[COL_LOTTO] = txtLottoArticolo.getText();
        dati[10] = null;
        dati[COL_ID_ORDINI] = null;
        dati[12] = articolo;
        DettagliTableModel model = (DettagliTableModel) table.getModel();
        Integer nuoviPezziEvad = null;
        if (!updateRow) { // MODALITA INSERIMENTO
            boolean trovato = false;
            for (int i = 0; i < table.getRowCount() && !trovato; ++i) {
                String lotto = (String) model.getValueAt(i, COL_LOTTO);
                String codice = (String) model.getValueAt(i, 0);
                if ((articolo.getId().intValue() == ((Articolo) table.getModel().getValueAt(i, 12)).getId().intValue())
                        && (lotto.equalsIgnoreCase((String) dati[COL_LOTTO]) || lotto.equalsIgnoreCase(""))) {
                    // AGGIORNO LA QUANTITA
                    trovato = true;
                    DecimalFormat format = new DecimalFormat("#0.000");
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setDecimalSeparator(',');
                    format.setDecimalFormatSymbols(symbols);

                    BigDecimal qta;
                    try {
                        qta = BigDecimal.valueOf(format.parse((String) dati[2]).doubleValue());
                        BigDecimal qta2 = BigDecimal.valueOf(format.parse((String) model.getValueAt(i, 2)).doubleValue());
                        qta = qta.add(qta2);
                        model.setValueAt(format.format(qta), i, 2);
                        model.setValueAt(txtCodiceArticolo.getText(), i, 0);

                        // AGGIORNO PEZZI
                        Integer pezziEsistenti = (Integer) model.getValueAt(i, 3);
                        if (model.getValueAt(i, 4) != null)
                            nuoviPezziEvad = (Integer) model.getValueAt(i, 4) - Integer.valueOf(txtPezzi.getText());
                        model.setValueAt(pezziEsistenti + (Integer) dati[3], i, 3);

                        if (((String) model.getValueAt(i, COL_LOTTO)).equalsIgnoreCase(""))
                            model.setValueAt(txtLottoArticolo.getText(), i, COL_LOTTO);
                        table.repaint();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (codice.equalsIgnoreCase((String) dati[0])) {
                    // COPIO DATI RELATIVI ORDINE
                    dati[COL_ID_ORDINI] = model.getValueAt(i, COL_ID_ORDINI);
                    dati[4] = model.getValueAt(i, 4);
                    if (model.getValueAt(i, 4) != null)
                        nuoviPezziEvad = (Integer) model.getValueAt(i, 4) - (Integer.valueOf(txtPezzi.getText()));
                }
            }
            if (!trovato)
                model.addRow(dati);
        } else {
            int selectedRow = table.getSelectedRow();
            dati[10] = table.getModel().getValueAt(selectedRow, 10); // INV DDT
            dati[COL_ID_ORDINI] = table.getModel().getValueAt(selectedRow, COL_ID_ORDINI); // ID ORDINI
            Integer vecchiPezzi = 0;
            if (table.getValueAt(selectedRow, 3) != null)
                vecchiPezzi = (Integer) table.getModel().getValueAt(selectedRow, 3);
            if (table.getValueAt(selectedRow, 4) != null)
                nuoviPezziEvad = (Integer) table.getModel().getValueAt(selectedRow, 4)
                        - (Integer.valueOf(txtPezzi.getText()) - vecchiPezzi); // PEZZI DA EVADERE
            model.removeRow(selectedRow);
            model.insertRow(selectedRow, dati);
        }

        if (nuoviPezziEvad != null)
            for (int i = 0; i < table.getRowCount(); ++i)
                if (((String) table.getModel().getValueAt(i, 0)).equalsIgnoreCase(getTxtCodiceArticolo().getText()))
                    table.setValueAt(nuoviPezziEvad, i, 4);

        try {
            BarcodeHandler.getInstance().setNextReadLotto(false);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setModalitaInserimentoArticolo(boolean modalita) {
        // contentPane.getRootPane().setDefaultButton(buttonOk);
        txtCodiceArticolo.setEnabled(modalita);
        buttonTrovaArticolo.setEnabled(modalita);
        // txtDescrizioneArticolo.setEnabled(modalita);
        // txtUMArticolo.setEnabled(modalita);
        txtQtaArticolo.setEnabled(modalita);
        txtPezzi.setEnabled(modalita);
        txtPrezzoArticolo.setEnabled(modalita);
        txtScontoArticolo.setEnabled(modalita);
        // cmbIva.setEnabled(modalita);
        txtLottoArticolo.setEnabled(modalita);
        buttonInserisciRiga.setEnabled(!modalita);
        buttonOk.setEnabled(modalita);
        buttonAnnulla.setEnabled(modalita);
        updateRow = !modalita;
        // TOGLIE EVENTUALI INPUTVERIFIER
        txtQtaArticolo.setInputVerifier(null);
        txtPrezzoArticolo.setInputVerifier(null);
        txtScontoArticolo.setInputVerifier(null);
        if (modalita)
            txtCodiceArticolo.requestFocus();
        else
            buttonInserisciRiga.requestFocus();
        // LI RIMETTE
        txtQtaArticolo.setInputVerifier(new NumberFormattedVerifier(3));
        txtPrezzoArticolo.setInputVerifier(new NumberFormattedVerifier(2));
        txtScontoArticolo.setInputVerifier(new NumberFormattedVerifier(0));
    }

    private void pulisciCampiArticolo() {
        txtCodiceArticolo.setText("");
        txtDescrizioneArticolo.setText("");
        txtUMArticolo.setText("");
        txtQtaArticolo.setText("");
        txtPezzi.setText("");
        txtPrezzoArticolo.setText("");
        txtScontoArticolo.setText("");
        lblListinoArticolo.setText("");
        articolo = null;

        // IMPOSTO IL VALORE PREDEFINITO
        // DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        // txtLottoArticolo.setText(dateFormat.format(Calendar.getInstance().getTime()));
        txtLottoArticolo.setText("");
    }

    private void setPuntoConsegna(PuntoConsegna puntoConsegna) {
        if (puntoConsegna == null) {
            txaDestCliente.setText("Nessun punto di consegna selezionato");
            puntoConsegnaSelezionato = null;
        } else {
            String txtDest = puntoConsegna.getNome() + "\n" + puntoConsegna.getCap() + " " + puntoConsegna.getIndirizzo() + "\n"
                    + puntoConsegna.getLocalita() + " " + puntoConsegna.getProv();
            txaDestCliente.setText(txtDest);
            puntoConsegnaSelezionato = puntoConsegna;

            if (itrPuntiConsegna.hasPrevious())
                getButtonDestClientePrec().setEnabled(true);
            else
                getButtonDestClientePrec().setEnabled(false);

            if (itrPuntiConsegna.hasNext())
                getButtonDestClienteSuc().setEnabled(true);
            else
                getButtonDestClienteSuc().setEnabled(false);

            loadOrdineCliente();
        }
    }

    public void setCliente(Object idCliente) {
        applet.setContentPane(getContentPane());
        applet.validate();
        applet.getContentPane().requestFocusInWindow();
        Cliente cliente = new DbConnector().getCliente((Integer) idCliente);
        setCliente(cliente);
    }

    public void setCliente(Cliente cliente) {
        if (cliente != null && cliente.getPuntiConsegna().size() > 0) {
            clienteSelezionato = cliente;
            txaCliente.setText(cliente.getRs() + "\n" + cliente.getRs2());
            buttonDestClientePrec.setEnabled(true);
            buttonDestClienteSuc.setEnabled(true);
            itrPuntiConsegna = cliente.getPuntiConsegna().listIterator();
            setPuntoConsegna(itrPuntiConsegna.next());

            if (cliente.getPuntiConsegna().size() == 1) {
                getButtonDestClientePrec().setEnabled(false);
                getButtonDestClienteSuc().setEnabled(false);
            }

            return;
        }

        if (cliente != null && cliente.getPuntiConsegna().size() == 0)
            JOptionPane.showMessageDialog(contentPane, "Questo cliente non puo' essere selezionato perche' non ci sono destinazioni",
                    "Attenzione", JOptionPane.ERROR_MESSAGE);

        txaCliente.setText("Nessun cliente selezionato");
        buttonDestClientePrec.setEnabled(false);
        buttonDestClienteSuc.setEnabled(false);
        clienteSelezionato = null;
        itrPuntiConsegna = null;
        setPuntoConsegna(null);
        getButtonInserisciRiga().requestFocusInWindow();
    }

    public void setModalitaInserimentoDDT() {
        modalitaAggiornamento = false;
        buttonModifica.setEnabled(false);
        buttonStampa.setEnabled(false);
        buttonSSN.setEnabled(true);
        buttonSN.setEnabled(true);
        cbNumerAutomatico.setEnabled(true);
        cbNumerAutomatico.setSelected(true);
        txtNumDDT.setText("");
        txtNumDDT2.setText("");
        setModalitaAggiornamentoArticolo(false);
        setModalitaInserimentoArticolo(false);
        // PULISCO TUTTI I CAMPI
        setCliente(null);
        setPuntoConsegna(null);
        // DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        // txtOraTrasporto.setText(dateFormat.format(new Date()));
        // dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // txtDataTrasporto.setText(dateFormat.format(new Date()));
        pulisciCampiArticolo();
        ((DettagliTableModel) table.getModel()).removeAllElements();
        txtColli.setText("0");
        txtTotaleDDT.setText("");
        contentPane.requestFocusInWindow();
    }

    @SuppressWarnings("unchecked")
    public void setModalitaAggiornamentoDDT() {
        try {
            modalitaAggiornamento = true;
            pulisciCampiArticolo();
            setModalitaInserimentoArticolo(false);
            setModalitaAggiornamentoArticolo(false);
            setCliente(currentDDT.getCliente());
            setPuntoConsegna(currentDDT.getPuntoConsegna());
            txtNumDDT.setText(currentDDT.getNumeroProgressivo().toString());
            txtNumDDT2.setText(currentDDT.getNumeroProgressivo2());
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
            txtDataDDT.setText(df.format(currentDDT.getData()));
            ((DettagliTableModel) table.getModel()).removeAllElements();
            ListIterator itr = currentDDT.getDettagliDDT().listIterator();
            while (itr.hasNext()) {
                DettaglioDDT dettaglio = (DettaglioDDT) itr.next();
                Object[] dati = new Object[TABLE_COLUMNS];
                dati[0] = dettaglio.getCodiceArticolo();
                dati[1] = dettaglio.getDescrizioneArticolo();
                BigDecimal number = dettaglio.getQta();
                NumberFormat formatter = DecimalFormat.getInstance(Locale.ITALIAN);
                formatter.setMinimumFractionDigits(3);
                dati[2] = formatter.format(number.doubleValue());
                dati[3] = dettaglio.getPezzi();

                Iterator itr2 = dettaglio.getEvasioniOrdini().iterator();
                int evasioni = 0;
                while (itr2.hasNext()) {
                    EvasioneOrdine eo = (EvasioneOrdine) itr2.next();
                    evasioni += eo.getDettaglioOrdine().getPezziDaEvadere();
                }

                dati[4] = evasioni;
                formatter.setMinimumFractionDigits(2);
                number = dettaglio.getPrezzo();
                dati[5] = formatter.format(number.doubleValue());
                formatter.setMinimumFractionDigits(1);
                number = dettaglio.getSconto();
                dati[6] = formatter.format(number.doubleValue());
                dati[7] = dettaglio.getIva();
                dati[8] = dettaglio.getUm();
                dati[COL_LOTTO] = dettaglio.getLotto();
                dati[10] = dettaglio.getId();
                dati[COL_ID_ORDINI] = dettaglio.getEvasioniOrdini();
                dati[12] = dettaglio.getArticolo();
                DettagliTableModel model = (DettagliTableModel) table.getModel();
                model.addRow(dati);
            }

            txtColli.setText(String.valueOf(currentDDT.getColli()));
            for (int i = 0; i < cmbTrasporto.getItemCount(); ++i)
                if (currentDDT.getTrasporto().equalsIgnoreCase((String) cmbTrasporto.getItemAt(i)))
                    cmbTrasporto.setSelectedIndex(i);
            // txtAspettoEsteriore.setText(currentDDT.getAspettoEsteriore());
            txtDataTrasporto.setText(df.format(currentDDT.getDataTrasporto()));
            df = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
            txtOraTrasporto.setText(df.format(currentDDT.getOraTrasporto()));
            // txtCausale.setText(currentDDT.getCausale());
            aggiornaTotaleDettaglio();
            cbNumerAutomatico.setSelected(false);
            cbNumerAutomatico.setEnabled(false);
            buttonSN.setEnabled(false);
            buttonSSN.setEnabled(false);
            // txtNumDDT.setEnabled(false);
            // txtNumDDT2.setEnabled(false);
            txtNumDDT.setEnabled(true);
            txtNumDDT2.setEnabled(true);
            buttonModifica.setEnabled(true);
            buttonStampa.setEnabled(true);
            contentPane.requestFocusInWindow();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(contentPane, e.getMessage(), "Attenzione", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @wbp.parser.constructor
     */
    public DDTWindow() throws Exception {

    }

    public DDTWindow(long idDDT, JApplet applet) throws Exception {
        this(applet);
        currentDDT = new DbConnector().getDDT(idDDT);
        modalitaAggiornamento = true;
    }

    public DDTWindow(JApplet applet) throws SQLException {
        try {
            BarcodeHandler.getInstance().setWindow(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.applet = applet;

        String[] colH = { "Codice", "Descrizione" };
        listaArticoli = new ChooseDialog("Scegli articolo", colH, 2, new DbConnector().getListaArticoli(), this, "setArticolo");

        String[] colH2 = { "Ragione sociale" };
        listaClienti = new ChooseDialog("Scegli cliente", colH2, 1, new DbConnector().getListaClienti(), this, "setCliente");
    }

    /**
     * @wbp.parser.entryPoint
     */
    public JPanel getContentPane() {
        if (contentPane == null) {
            contentPane = new JPanel(new GridBagLayout());
            contentPane.setSize(new Dimension(620, 650));
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 10);
            c.fill = GridBagConstraints.HORIZONTAL;
            contentPane.add(getPanelLettore(), c);
            c.gridy = 1;
            contentPane.add(getPanelIntestazione(), c);
            c.gridy = 2;
            c.weighty = 1;
            c.fill = GridBagConstraints.BOTH;
            contentPane.add(getPanelDettaglio(), c);
            c.weighty = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridy = 3;
            contentPane.add(getPanelSpedizione(), c);
            c.gridy = 4;
            contentPane.add(getPanelBottoni(), c);

            try {
                contentPane.addKeyListener(BarcodeHandler.getInstance());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return contentPane;
    }

    private JPanel getPanelIntestazione() {
        if (panelIntestazione == null) {
            lblDataDDT = new JLabel();
            lblDataDDT.setText("data:");
            labelNumDDT = new JLabel();
            labelNumDDT.setText("/");
            labelDestCliente = new JLabel();
            labelDestCliente.setText("Seleziona destinazione:");
            labelSelezionaCliente = new JLabel();
            labelSelezionaCliente.setText("Seleziona Cliente:");
            panelIntestazione = new JPanel();
            panelIntestazione.setBorder(BorderFactory.createTitledBorder(null, "Intestazione", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), new Color(0, 70, 213)));
            panelIntestazione.setMinimumSize(new Dimension(contentPane.getWidth(), 135));
            panelIntestazione.setPreferredSize(new Dimension(contentPane.getWidth(), 135));
            panelIntestazione.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            panelIntestazione.add(getCbNumerAutomatico(), c);
            c.gridx = 1;
            panelIntestazione.add(getTxtNumDDT(), c);
            c.gridx = 2;
            panelIntestazione.add(labelNumDDT, c);
            c.gridx = 3;
            panelIntestazione.add(getTxtNumDDT2(), c);
            c.gridx = 4;
            c.anchor = GridBagConstraints.EAST;
            c.weightx = 0.5;
            panelIntestazione.add(lblDataDDT, c);
            c.anchor = GridBagConstraints.WEST;
            c.gridx = 5;
            c.weightx = 0;
            panelIntestazione.add(getTxtDataDDT(), c);
            c.weighty = 1;
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 6;
            c.fill = GridBagConstraints.BOTH;
            c.anchor = GridBagConstraints.NORTHWEST;
            JPanel panelCliente = new JPanel();
            panelCliente.setLayout(new GridBagLayout());
            panelIntestazione.add(panelCliente, c);
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 0.7;
            c.fill = GridBagConstraints.BOTH;
            c.anchor = GridBagConstraints.SOUTHWEST;
            panelCliente.add(labelSelezionaCliente, c);
            c.anchor = GridBagConstraints.NORTHWEST;
            c.gridy = 1;
            c.gridheight = 2;
            panelCliente.add(getTxaCliente(), c);
            c.gridx = 1;
            c.gridy = 1;
            c.gridheight = 1;
            c.weightx = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(0, 2, 0, 2);
            panelCliente.add(getButtonTrovaCliente(), c);
            c.gridx = 1;
            c.gridy = 1;
            c.anchor = GridBagConstraints.SOUTHWEST;
            // panelCliente.add(getButtonClientePrec(), c);
            c.gridx = 1;
            c.gridy = 2;
            // panelCliente.add(getButtonClienteSuc(), c);
            c.gridx = 2;
            c.gridy = 0;
            c.weightx = 0.5;
            panelCliente.add(labelDestCliente, c);
            c.anchor = GridBagConstraints.NORTHWEST;
            c.gridy = 1;
            c.weighty = 1;
            c.gridheight = 2;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 0;
            panelCliente.add(getTxaDestCliente(), c);
            c.anchor = GridBagConstraints.NORTHWEST;
            c.gridx = 3;
            c.gridy = 1;
            c.gridheight = 1;
            c.weightx = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            panelCliente.add(getButtonDestClientePrec(), c);
            c.gridx = 3;
            c.gridy = 2;
            c.anchor = GridBagConstraints.SOUTHWEST;
            panelCliente.add(getButtonDestClienteSuc(), c);
        }
        return panelIntestazione;
    }

    private JPanel getPanelDettaglio() {
        if (panelDettaglio == null) {
            panelDettaglio = new JPanel();
            panelDettaglio.setLayout(new BorderLayout());
            panelDettaglio.setBorder(BorderFactory.createTitledBorder(null, "Dettaglio", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), new Color(0, 70, 213)));
            // panelDettaglio.setMinimumSize(new Dimension(frame.getWidth(),0));
            // panelDettaglio.setPreferredSize(new Dimension(frame.getWidth(), 0));
            panelDettaglio.add(getScrollPane(), BorderLayout.CENTER);
            panelDettaglio.add(getPanelDettaglioRiga(), BorderLayout.PAGE_END);
        }
        return panelDettaglio;
    }

    private JPanel getPanelSpedizione() {
        if (panelSpedizione == null) {
            // labelCausale = new JLabel();
            // labelCausale.setText("Causale:");
            labelOraTrasporto = new JLabel();
            labelOraTrasporto.setText("Ora trasporto:");
            labelDataTrasporto = new JLabel();
            labelDataTrasporto.setText("Data trasporto:");
            labelColli = new JLabel();
            labelColli.setText("N. colli:");
            // labelAspettoEst = new JLabel();
            // labelAspettoEst.setText("Aspetto esteriore:");
            labelTrasportoCura = new JLabel();
            labelTrasportoCura.setText("Trasporto a cura del:");
            panelSpedizione = new JPanel();
            panelSpedizione.setBorder(BorderFactory.createTitledBorder(null, "Spedizione", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), new Color(0, 70, 213)));
            // panelSpedizione.setMinimumSize(new Dimension(frame.getWidth(),106));
            // panelSpedizione.setPreferredSize(new Dimension(frame.getWidth(),106));
            panelSpedizione.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(2, 0, 0, 0);
            c.ipadx = 5;
            c.ipady = 0;
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.EAST;
            panelSpedizione.add(labelColli, c);
            c.gridy = 1;
            panelSpedizione.add(labelTrasportoCura, c);
            /*
             * c.gridy = 2; panelSpedizione.add(labelAspettoEst, c);
             */
            c.gridx = 1;
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;
            panelSpedizione.add(getTxtColli(), c);
            c.gridy = 1;
            panelSpedizione.add(getCmbTrasporto(), c);
            /*
             * c.gridy = 2; panelSpedizione.add(getTxtAspettoEsteriore(), c);
             */
            c.gridx = 2;
            c.gridy = 0;
            c.weightx = 1;
            c.anchor = GridBagConstraints.EAST;
            panelSpedizione.add(labelDataTrasporto, c);
            c.gridy = 1;
            panelSpedizione.add(labelOraTrasporto, c);
            /*
             * c.gridy = 2; panelSpedizione.add(labelCausale, c);
             */
            c.gridx = 3;
            c.gridy = 0;
            c.weightx = 0;
            c.anchor = GridBagConstraints.WEST;
            panelSpedizione.add(getTxtDataTrasporto(), c);
            c.gridy = 1;
            panelSpedizione.add(getTxtOraTrasporto(), c);
            /*
             * c.gridy = 2; c.fill = GridBagConstraints.HORIZONTAL; panelSpedizione.add(getTxtCausale(), c);
             */
        }
        return panelSpedizione;
    }

    private JPanel getPanelBottoni() {
        if (panelBottoni == null) {
            panelBottoni = new JPanel();
            panelBottoni.setLayout(new BoxLayout(panelBottoni, BoxLayout.LINE_AXIS));
            panelBottoni.add(Box.createRigidArea(new Dimension(2, 0)));
            panelBottoni.add(getButtonNuovo());
            panelBottoni.add(Box.createRigidArea(new Dimension(5, 0)));
            panelBottoni.add(getButtonSN());
            panelBottoni.add(Box.createRigidArea(new Dimension(5, 0)));
            panelBottoni.add(getButtonSSN());
            panelBottoni.add(Box.createHorizontalGlue());
            panelBottoni.add(getButtonModifica());
            panelBottoni.add(Box.createRigidArea(new Dimension(5, 0)));
            panelBottoni.add(getButtonStampa());
            panelBottoni.add(Box.createRigidArea(new Dimension(2, 0)));
        }
        return panelBottoni;
    }

    private JPanel getPanelLettore() {
        if (panelLettore == null) {
            panelLettore = new JPanel();
            panelLettore.setLayout(new BorderLayout());
            // panelLettore.setPreferredSize(new Dimension(0,32));
            // panelLettore.setSize(new Dimension(0,50));

            lblStatoLettore = new JLabel();
            lblStatoLettore.setHorizontalAlignment(SwingConstants.CENTER);
            lblStatoLettore.setForeground(Color.RED);

            panelLettore.add(lblStatoLettore, BorderLayout.CENTER);
        }
        return panelLettore;
    }

    private JTextArea getTxaCliente() {
        if (txaCliente == null) {
            txaCliente = new JTextArea();
            txaCliente.setLineWrap(true);
            txaCliente.setEnabled(true);
            txaCliente.setEditable(false);
            txaCliente.setFont(new Font("Tahoma", Font.PLAIN, 11));
            txaCliente.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        return txaCliente;
    }

    private JButton getButtonTrovaCliente() {
        if (buttonTrovaCliente == null) {
            buttonTrovaCliente = new JButton();
            buttonTrovaCliente.setText("Trova");
            buttonTrovaCliente.setMnemonic('T');
            buttonTrovaCliente.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    applet.setContentPane(listaClienti.getContentPanel());
                    applet.validate();
                    listaClienti.getTxtTrova().requestFocus();

                    // applet.getContentPane().add(dialog.getContentPanel());
                    /*
                     * dialog.getDialog().setVisible(true); if (dialog.getSelectedElement() != null) {
                     * 
                     * } else buttonTrovaCliente.requestFocus();
                     */
                }
            });
            try {
                buttonTrovaCliente.addKeyListener(BarcodeHandler.getInstance());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return buttonTrovaCliente;
    }

    private JTextArea getTxaDestCliente() {
        if (txaDestCliente == null) {
            txaDestCliente = new JTextArea();
            txaDestCliente.setEditable(false);
            txaDestCliente.setLineWrap(true);
            txaDestCliente.setBackground(Color.white);
            txaDestCliente.setFont(new Font("Tahoma", Font.PLAIN, 11));
            txaDestCliente.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        return txaDestCliente;
    }

    private JButton getButtonDestClientePrec() {
        if (buttonDestClientePrec == null) {
            buttonDestClientePrec = new JButton();
            buttonDestClientePrec.setText("Prec");
            buttonDestClientePrec.setMnemonic('r');
            buttonDestClientePrec.setEnabled(false);
            buttonDestClientePrec.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setPuntoConsegna(itrPuntiConsegna.previous());
                }
            });
        }
        return buttonDestClientePrec;
    }

    private JButton getButtonDestClienteSuc() {
        if (buttonDestClienteSuc == null) {
            buttonDestClienteSuc = new JButton();
            buttonDestClienteSuc.setText("Suc");
            buttonDestClienteSuc.setMnemonic('u');
            buttonDestClienteSuc.setEnabled(false);
            buttonDestClienteSuc.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setPuntoConsegna(itrPuntiConsegna.next());
                }
            });
        }
        return buttonDestClienteSuc;
    }

    private JComboBox getCmbTrasporto() {
        if (cmbTrasporto == null) {
            String[] tipo = { "mittente", "destinatario", "vettore" };
            cmbTrasporto = new JComboBox(tipo);
        }
        return cmbTrasporto;
    }

    private JComboBox getCmbModalita() {
        if (cmbModalita == null) {
            String[] tipo = { "cod. prodotto", "cod. barre" };
            cmbModalita = new JComboBox(tipo);
            // 20170121 - Modifica per imporre "cod. prodotto" come default.
            // | prima era setSelectedIndex(1) |
            cmbModalita.setSelectedIndex(0);
            cmbModalita.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (cmbModalita.getSelectedIndex() == 1) {
                        setModalitaAggiornamentoArticolo(true);
                        setModalitaInserimentoArticolo(true);
                        txtCodiceArticolo.setEditable(true);
                    }
                }
            });
        }
        return cmbModalita;
    }

    /*
     * private JTextField getTxtAspettoEsteriore() { if (txtAspettoEsteriore == null) { txtAspettoEsteriore = new JTextField();
     * txtAspettoEsteriore.setText("scatole"); txtAspettoEsteriore.setColumns(6); txtAspettoEsteriore.setMinimumSize(new
     * Dimension(70,21)); } return txtAspettoEsteriore; }
     */

    private JTextField getTxtColli() {
        if (txtColli == null) {
            txtColli = new JTextField();
            txtColli.setInputVerifier(new NumberFormattedVerifier(0));
            txtColli.setText("0");
            txtColli.setColumns(6);
            txtColli.setMinimumSize(new Dimension(50, 21));
            txtColli.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent evt) {
                    ((JTextField) evt.getSource()).selectAll();
                }
            });
        }
        return txtColli;
    }

    /*
     * private JTextField getTxtCausale() { if (txtCausale == null) { txtCausale = new JTextField(); txtCausale.setText("vendita");
     * txtCausale.setColumns(6); txtCausale.setMinimumSize(new Dimension(90,21)); } return txtCausale; }
     */

    private JTextField getTxtOraTrasporto() {
        if (txtOraTrasporto == null) {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
            DateFormatter dateFormatter = new DateFormatter(dateFormat);
            txtOraTrasporto = new JFormattedTextField(dateFormatter);
            txtOraTrasporto.setInputVerifier(new DateFormattedVerifier("HH:mm"));
            txtOraTrasporto.setText(dateFormat.format(new Date()));
            txtOraTrasporto.setMinimumSize(new Dimension(50, 21));
        }
        return txtOraTrasporto;
    }

    private JTextField getTxtDataTrasporto() {
        if (txtDataTrasporto == null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
            DateFormatter dateFormatter = new DateFormatter(dateFormat);
            txtDataTrasporto = new JFormattedTextField(dateFormatter);
            txtDataTrasporto.setInputVerifier(new DateFormattedVerifier("dd/MM/yyyy"));
            txtDataTrasporto.setText(dateFormat.format(new Date()));
            txtDataTrasporto.setMinimumSize(new Dimension(90, 21));
        }
        return txtDataTrasporto;
    }

    private JButton getButtonNuovo() {
        if (buttonNuovo == null) {
            buttonNuovo = new JButton();
            buttonNuovo.setText("Nuovo DDT");
            buttonNuovo.setMnemonic('o');
            buttonNuovo.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setModalitaInserimentoDDT();
                }
            });
        }
        return buttonNuovo;
    }

    private JButton getButtonModifica() {
        if (buttonModifica == null) {
            buttonModifica = new JButton();
            buttonModifica.setText("Modifica");
            buttonModifica.setMnemonic('d');
            buttonModifica.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        DDT ddt = getDDT();
                        // new DbConnector().saveDDT(ddt);
                        int update = JOptionPane.showConfirmDialog(contentPane, "Vuoi aggiornare l'ordine?", "Conferma",
                                JOptionPane.YES_NO_OPTION);
                        new DbConnector().saveDDT(ddt, update == 0);
                        JOptionPane.showMessageDialog(contentPane, "Il DDT � stato aggiornato con successo", "Successo",
                                JOptionPane.INFORMATION_MESSAGE);
                        buttonModifica.setEnabled(false);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(contentPane, e1.getMessage(), "Attenzione", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
        return buttonModifica;
    }

    private JButton getButtonSSN() {
        if (buttonSSN == null) {
            buttonSSN = new JButton();
            buttonSSN.setText("Salva/Stampa/Nuovo");
            buttonSSN.setMnemonic('S');
            buttonSSN.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        DDT ddt = getDDT();
                        // SALVO L'OGGETTO DDT
                        // int idDDT = new DbConnector().saveDDT(ddt);
                        int idDDT = new DbConnector().saveDDT(ddt, true);
                        JOptionPane.showMessageDialog(getPanelBottoni(), "Il DDT � stato salvato con successo", "Successo",
                                JOptionPane.INFORMATION_MESSAGE);
                        setModalitaInserimentoDDT();
                        stampaDDT(idDDT);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(getPanelBottoni(), e1.getMessage(), "Attenzione", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
        return buttonSSN;
    }

    private JButton getButtonSN() {
        if (buttonSN == null) {
            buttonSN = new JButton();
            buttonSN.setText("Salva/Nuovo");
            buttonSN.setMnemonic('v');
            buttonSN.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        DDT ddt = getDDT();
                        // new DbConnector().saveDDT(ddt);
                        new DbConnector().saveDDT(ddt, true);
                        JOptionPane.showMessageDialog(contentPane, "Il DDT � stato salvato con successo", "Successo",
                                JOptionPane.INFORMATION_MESSAGE);
                        setModalitaInserimentoDDT();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(contentPane, e1.getMessage(), "Attenzione", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
        return buttonSN;
    }

    private JButton getButtonStampa() {
        if (buttonStampa == null) {
            buttonStampa = new JButton();
            buttonStampa.setText("Stampa");
            buttonStampa.setMnemonic('m');
            buttonStampa.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        int idDDT = currentDDT.getId();

                        if (!currentDDT.getFatturato()) {
                            int result = JOptionPane.showConfirmDialog(getPanelBottoni(),
                                    "Attenzione, prima di procedere con la stampa e' necessario salvare il documento. Continuare?",
                                    "Attenzione", JOptionPane.OK_CANCEL_OPTION);
                            if (result == JOptionPane.OK_OPTION) {
                                DDT ddt = getDDT();
                                // new DbConnector().saveDDT(ddt);
                                new DbConnector().saveDDT(ddt, true);
                                JOptionPane.showMessageDialog(getPanelBottoni(), "Il DDT � stato salvato con successo", "Successo",
                                        JOptionPane.INFORMATION_MESSAGE);
                                stampaDDT(idDDT);
                            }
                        } else
                            stampaDDT(idDDT);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(getPanelBottoni(), e1.getMessage(), "Attenzione", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
        return buttonStampa;
    }

    private void stampaDDT(int idDDT) {
        URL url;
        try {
            /*
             * String osname = System.getProperty("os.name"); if (!osname.startsWith("Windows")) {
             * JOptionPane.showMessageDialog(panelBottoni, "La stampa � supportata solamente su sistemi Windows"); return; }
             */

            url = new URL(Settings.getInstance().getPrintUrl() + "?id=" + idDDT);
            URLConnection uc = url.openConnection();
            InputStream is = uc.getInputStream();

            String osname = System.getProperty("os.name");
            if (osname.startsWith("Windows")) {
                // SALVO IL PDF IN UNA CARTELLA TEMPORANEA
                int length = uc.getContentLength();
                byte[] array = new byte[length];
                is.read(array);

                File file = File.createTempFile("pdf" + idDDT, ".pdf");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(array);
                fos.flush();
                fos.close();

                // LO STAMPO USANDO ACROBAT READER
                String command = "cmd /C start acrord32 /p /h " + file.getAbsolutePath() + "";
                Runtime rn = Runtime.getRuntime();
                Process process = rn.exec(command);
                process.waitFor();
                return;
            }

            PrintService ps = PrintServiceLookup.lookupDefaultPrintService();
            ps.getSupportedDocFlavors();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.PDF;
            Doc doc = new SimpleDoc(is, flavor, null);

            DocPrintJob job = ps.createPrintJob();
            PrintRequestAttributeSet set = new HashPrintRequestAttributeSet();
            set.add(MediaSizeName.NA_LETTER);
            set.add(new JobName("Stampa DDT " + idDDT, null));
            job.print(doc, set);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelBottoni, "Errore nella stampa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private JCheckBox getCbNumerAutomatico() {
        if (cbNumerAutomatico == null) {
            cbNumerAutomatico = new JCheckBox();
            cbNumerAutomatico.setSelected(true);
            cbNumerAutomatico.setText("Numerazione automatica");
            cbNumerAutomatico.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (!cbNumerAutomatico.isSelected()) {
                        txtNumDDT.setEnabled(true);
                        txtNumDDT2.setEnabled(true);
                    } else {
                        txtNumDDT.setText("");
                        txtNumDDT2.setText("");
                        txtNumDDT.setEnabled(false);
                        txtNumDDT2.setEnabled(false);
                    }
                }
            });
        }
        return cbNumerAutomatico;
    }

    private JTextField getTxtNumDDT() {
        if (txtNumDDT == null) {
            txtNumDDT = new JTextField();
            txtNumDDT.setEnabled(false);
            txtNumDDT.setColumns(5);
        }
        return txtNumDDT;
    }

    private JTextField getTxtNumDDT2() {
        if (txtNumDDT2 == null) {
            txtNumDDT2 = new JTextField();
            txtNumDDT2.setEnabled(false);
            txtNumDDT2.setColumns(3);
        }
        return txtNumDDT2;
    }

    private JPanel getPanelDettaglioRiga() {
        if (panelDettaglioRiga == null) {
            lblIva = new JLabel();
            lblIva.setText("Iva");
            lblScontoArticolo = new JLabel();
            lblScontoArticolo.setText("Sconto");
            lblPrezzo = new JLabel();
            lblPrezzo.setText("Prezzo");
            lblPezzi = new JLabel();
            lblPezzi.setText("Pezzi");
            lblUMArticolo = new JLabel();
            lblUMArticolo.setText("UM");
            lblDescrizioneArticolo = new JLabel();
            lblDescrizioneArticolo.setText("Descr.");
            lblLotto = new JLabel();
            lblLotto.setText("Lotto");
            lblQta = new JLabel();
            lblQta.setText("Q.ta'");
            lblCodice = new JLabel();
            lblCodice.setText("Codice");
            JLabel lblCercaCodice = new JLabel();
            lblCercaCodice.setText("Cerca_2017");
            lblTotaleDDT = new JLabel();
            lblTotaleDDT.setText("Totale Euro:");
            lblListinoArticolo = new JLabel();
            lblListinoArticolo.setText("");

            panelDettaglioRiga = new JPanel();
            panelDettaglioRiga.setMinimumSize(new Dimension(panelDettaglio.getWidth(), 111));
            panelDettaglioRiga.setMaximumSize(new Dimension(panelDettaglio.getWidth(), 111));
            panelDettaglioRiga.setPreferredSize(new Dimension(panelDettaglio.getWidth(), 111));
            panelDettaglioRiga.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;
            c.insets = new Insets(0, 2, 0, 0);
            panelDettaglioRiga.add(lblCercaCodice, c);
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 0;
            panelDettaglioRiga.add(getCmbModalita(), c);
            c.gridx = 2;
            c.gridy = 0;
            c.weightx = 0;
            panelDettaglioRiga.add(lblCodice, c);
            c.gridx = 3;
            c.gridy = 0;
            c.weightx = 0;
            panelDettaglioRiga.add(getTxtCodiceArticolo(), c);
            c.gridx = 4;
            c.gridy = 0;
            c.weightx = 0.1;
            panelDettaglioRiga.add(getButtonTrovaArticolo(), c);
            // c.gridx = 5;
            // c.gridy = 0;
            // c.weightx = 0.1;
            // panelDettaglioRiga.add(lblDescrizioneArticolo, c);
            c.gridx = 5;
            c.gridy = 0;
            c.gridwidth = 4;
            c.weightx = 0.8;
            c.fill = GridBagConstraints.HORIZONTAL;
            panelDettaglioRiga.add(getTxtDescrizioneArticolo(), c);
            c.gridwidth = 1;
            c.gridx = 9;
            c.gridy = 0;
            c.weightx = 0;
            c.fill = GridBagConstraints.NONE;
            panelDettaglioRiga.add(lblUMArticolo, c);
            c.gridx = 10;
            c.gridy = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            panelDettaglioRiga.add(getTxtUMArticolo(), c);
            c.gridx = 0;
            c.gridy = 1;
            c.fill = GridBagConstraints.NONE;
            panelDettaglioRiga.add(lblQta, c);
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            panelDettaglioRiga.add(getTxtQtaArticolo(), c);
            c.gridx = 3;
            c.gridy = 1;
            panelDettaglioRiga.add(lblPezzi, c);
            c.gridx = 4;
            c.gridy = 1;
            c.weightx = 0.2;
            panelDettaglioRiga.add(getTxtPezzi(), c);
            c.gridx = 5;
            c.gridy = 1;
            c.weightx = 0;
            panelDettaglioRiga.add(lblPrezzo, c);
            c.gridx = 6;
            c.gridy = 1;
            c.weightx = 0.5;
            panelDettaglioRiga.add(getTxtPrezzoArticolo(), c);
            c.gridx = 7;
            c.gridy = 1;
            c.weightx = 0;
            panelDettaglioRiga.add(lblScontoArticolo, c);
            c.gridx = 8;
            c.gridy = 1;
            c.weightx = 0.1;
            panelDettaglioRiga.add(getTxtScontoArticolo(), c);
            c.gridx = 9;
            c.gridy = 1;
            c.weightx = 0;
            panelDettaglioRiga.add(lblIva, c);
            c.gridx = 10;
            c.gridy = 1;
            panelDettaglioRiga.add(getCmbIva(), c);

            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 2;
            panelDettaglioRiga.add(lblLotto, c);
            c.gridx = 1;
            c.gridy = 2;
            c.gridwidth = 4;
            c.weightx = 1;
            c.insets = new Insets(2, 2, 0, 0);
            c.fill = GridBagConstraints.HORIZONTAL;
            panelDettaglioRiga.add(getTxtLottoArticolo(), c);

            c.gridx = 5;
            c.gridy = 2;
            c.gridwidth = 3;
            c.weightx = 1;
            c.insets = new Insets(2, 2, 0, 0);
            panelDettaglioRiga.add(lblListinoArticolo, c);

            JPanel panelBottoni = new JPanel();
            panelBottoni.setLayout(new BoxLayout(panelBottoni, BoxLayout.LINE_AXIS));
            panelBottoni.add(getButtonInserisciRiga());
            panelBottoni.add(Box.createRigidArea(new Dimension(5, 0)));
            panelBottoni.add(getButtonCancellaArticolo());
            panelBottoni.add(Box.createHorizontalGlue());
            panelBottoni.add(Box.createRigidArea(new Dimension(20, 20)));
            panelBottoni.add(lblTotaleDDT);
            panelBottoni.add(getTxtTotaleDDT());
            panelBottoni.add(Box.createHorizontalGlue());
            panelBottoni.add(getButtonOk());
            panelBottoni.add(Box.createRigidArea(new Dimension(5, 0)));
            panelBottoni.add(getButtonAnnulla());
            c.gridwidth = 11;
            c.gridx = 0;
            c.gridy = 3;
            c.weighty = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            panelDettaglioRiga.add(panelBottoni, c);
        }
        return panelDettaglioRiga;
    }

    private JTextField getTxtTotaleDDT() {
        if (txtTotaleDDT == null) {
            txtTotaleDDT = new JTextField();
            txtTotaleDDT.setEnabled(false);
            txtTotaleDDT.setColumns(6);
            txtTotaleDDT.setMinimumSize(new Dimension(60, 21));
        }
        return txtTotaleDDT;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getTable());
        }
        return scrollPane;
    }

    private JTable getTable() {
        if (table == null) {
            table = new JTable();
            table.setModel(new DettagliTableModel());
            table.getTableHeader().setReorderingAllowed(false);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setRowSelectionAllowed(true);

            TableColumn colModel = table.getColumnModel().getColumn(0); // CODICE
            colModel.setMaxWidth(60);
            colModel = table.getColumnModel().getColumn(2); // QTA
            colModel.setMaxWidth(50);
            colModel = table.getColumnModel().getColumn(3); // PEZZI RIGA
            colModel.setMaxWidth(30);
            colModel = table.getColumnModel().getColumn(4); // PEZZI DA EVADERE
            colModel.setMaxWidth(30);
            colModel = table.getColumnModel().getColumn(5); // PREZZI
            colModel.setMaxWidth(50);
            colModel = table.getColumnModel().getColumn(6); // SCONTO
            colModel.setMaxWidth(35);
            colModel = table.getColumnModel().getColumn(7); // IVA
            colModel.setMaxWidth(25);
            colModel = table.getColumnModel().getColumn(8); // UM
            colModel.setMaxWidth(25);
            colModel = table.getColumnModel().getColumn(COL_LOTTO); // LOTTO
            colModel.setMaxWidth(70);

            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent arg0) {
                    if (arg0.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(arg0)) {
                        editTable();
                    }

                    if (SwingUtilities.isRightMouseButton(arg0)) {
                        // Check if there is an order associated to the row.
                        // If there is ask to remove it
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            DettagliTableModel model = (DettagliTableModel) table.getModel();
                            if (model.getValueAt(selectedRow, COL_ID_ORDINI) != null) {
                                try {
                                    if (model.getValueAt(selectedRow, COL_ID_ORDINI) instanceof ArrayList) {
                                        ArrayList ordini = (ArrayList) model.getValueAt(selectedRow, COL_ID_ORDINI);
                                        String message = "Ci sono degli ordini associati con questa riga. Si desidera cancellare gli articoli indicati da questi ordini?";
                                        int res = JOptionPane.showConfirmDialog(table, message, "Attenzione!",
                                                JOptionPane.OK_CANCEL_OPTION);

                                        if (res == JOptionPane.OK_OPTION) {
                                            Iterator itr = ordini.iterator();
                                            while (itr.hasNext()) {
                                                EvasioneOrdine eo = (EvasioneOrdine) itr.next();
                                                cancellaRigaOrdine(eo);
                                            }
                                        }

                                    } else {
                                        EvasioneOrdine eo = (EvasioneOrdine) model.getValueAt(selectedRow, COL_ID_ORDINI);
                                        String message = "Si desidera cancellare questo articolo dall'ordine "
                                                + eo.getDettaglioOrdine().getIdOrdine() + "?";
                                        int res = JOptionPane.showConfirmDialog(table, message, "Attenzione!",
                                                JOptionPane.OK_CANCEL_OPTION);

                                        if (res == JOptionPane.OK_OPTION)
                                            cancellaRigaOrdine(eo);
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                }

                private void cancellaRigaOrdine(EvasioneOrdine eo) {
                    if (new DbConnector().cancellaRigaOrdine(eo)) {
                        JOptionPane.showMessageDialog(table, "Cancellazione avvenuta con successo", "Informazione",
                                JOptionPane.INFORMATION_MESSAGE);
                        // Aggiorna riga sulla tabella
                        DettagliTableModel model = (DettagliTableModel) table.getModel();
                        int selectedRow = table.getSelectedRow();

                        if ((model.getValueAt(selectedRow, COL_LOTTO) != null
                                && ((String) model.getValueAt(selectedRow, COL_LOTTO)).equalsIgnoreCase(""))
                                || model.getValueAt(selectedRow, COL_LOTTO) == null)
                            model.removeRow(selectedRow);
                        else
                            model.setValueAt(null, selectedRow, COL_ID_ORDINI);

                    } else
                        JOptionPane.showMessageDialog(table, "Errori nella cancellazione", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            });

            table.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent arg0) {
                    if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                        editTable();
                    }
                }
            });

            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                private static final long serialVersionUID = 1L;

                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                        int row, int column) {
                    Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    if (!isSelected) {
                        cell.setBackground(Color.white);
                        cell.setForeground(Color.black);
                    }

                    if (((String) table.getValueAt(row, COL_LOTTO)).equalsIgnoreCase("") && !isSelected) {
                        cell.setBackground(Color.red);
                    } else if (table.getValueAt(row, 4) != null && ((Integer) table.getValueAt(row, 4) > 0) && !isSelected)
                        cell.setBackground(Color.yellow);
                    else if (table.getValueAt(row, 4) != null && ((Integer) table.getValueAt(row, 4) < 0) && !isSelected)
                        cell.setBackground(Color.green);

                    return cell;
                }
            });
        }
        return table;
    }

    private JButton getButtonInserisciRiga() {
        if (buttonInserisciRiga == null) {
            buttonInserisciRiga = new JButton();
            buttonInserisciRiga.setMnemonic(KeyEvent.VK_N);
            buttonInserisciRiga.setBounds(new Rectangle(5, 80, 116, 21));
            buttonInserisciRiga.setText("Nuovo articolo");
            buttonInserisciRiga.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (clienteSelezionato != null) {
                        setModalitaInserimentoArticolo(true);
                    } else
                        JOptionPane.showMessageDialog(contentPane, "Selezionare un cliente", "Attenzione",
                                JOptionPane.WARNING_MESSAGE);
                }
            });
            try {
                buttonInserisciRiga.addKeyListener(BarcodeHandler.getInstance());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return buttonInserisciRiga;
    }

    private JButton getButtonCancellaArticolo() {
        if (buttonCancellaArticolo == null) {
            buttonCancellaArticolo = new JButton();
            buttonCancellaArticolo.setBounds(new Rectangle(125, 80, 116, 21));
            buttonCancellaArticolo.setText("Cancella articolo");
            buttonCancellaArticolo.setMnemonic('l');
            buttonCancellaArticolo.setEnabled(false);
            buttonCancellaArticolo.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    String codiceArticolo = (String) table.getValueAt(selectedRow, 0);
                    for (int i = 0; i < table.getRowCount(); ++i) {
                        String codiceNuovo = (String) table.getValueAt(i, 0);
                        if (codiceNuovo.equalsIgnoreCase(codiceArticolo) && i != selectedRow) {
                            table.setValueAt((Integer) table.getValueAt(i, 4) + (Integer) table.getValueAt(selectedRow, 3), i, 4);
                        }
                    }
                    ((DettagliTableModel) table.getModel()).removeRow(selectedRow);
                    pulisciCampiArticolo();
                    setModalitaAggiornamentoArticolo(false);
                    aggiornaTotaleDettaglio();
                }
            });
        }
        return buttonCancellaArticolo;
    }

    private void aggiornaTotaleDettaglio() {
        try {
            DDT ddt = new DDT();
            ddt.setDettagliDDT(getDettagliDDT());
            // BigDecimal totale = DDT.calcolaTotale(getDettagliDDT());
            BigDecimal totale = ddt.calcolaTotalePerApplet();
            DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
            txtTotaleDDT.setText(df.format(totale.doubleValue()));
        } catch (ParseException e1) {
        }
    }

    private JButton getButtonAnnulla() {
        if (buttonAnnulla == null) {
            buttonAnnulla = new JButton();
            buttonAnnulla.setText("Annulla");
            buttonAnnulla.setMnemonic('a');
            buttonAnnulla.setEnabled(false);
            buttonAnnulla.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    pulisciCampiArticolo();
                    setModalitaInserimentoArticolo(false);
                    setModalitaAggiornamentoArticolo(false);
                    try {
                        BarcodeHandler.getInstance().setNextReadLotto(false);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            });
        }
        return buttonAnnulla;
    }

    private JButton getButtonOk() {
        if (buttonOk == null) {
            buttonOk = new JButton();
            buttonOk.setText("Ok");
            buttonOk.setEnabled(false);
            buttonOk.setDefaultCapable(true);
            buttonOk.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    inserisciRigaCorrente();
                }
            });
        }
        return buttonOk;
    }

    private void inserisciRigaCorrente() {
        if (checkArticoloCorrente()) {
            inserisciArticoloCorrente();
            // setModalitaAggiornamentoArticolo(false);
            // setModalitaInserimentoArticolo(false);
            if (getCmbModalita().getSelectedIndex() == 0) {
                setModalitaAggiornamentoArticolo(false);
                setModalitaInserimentoArticolo(false);
            } else {
                setModalitaAggiornamentoArticolo(true);
                setModalitaInserimentoArticolo(true);
                txtCodiceArticolo.requestFocus();
            }
            pulisciCampiArticolo();
            aggiornaTotaleDettaglio();
        } else {
            JOptionPane.showMessageDialog(contentPane, "I dati inseriti non sono corretti", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private JTextField getTxtCodiceArticolo() {
        if (txtCodiceArticolo == null) {
            txtCodiceArticolo = new JTextField();
            txtCodiceArticolo.setColumns(6);
            txtCodiceArticolo.setMinimumSize(new Dimension(60, 21));
            txtCodiceArticolo.setEnabled(false);
            txtCodiceArticolo.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, java.util.Collections.EMPTY_SET);
            txtCodiceArticolo.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_TAB || e.getKeyChar() == KeyEvent.VK_ENTER) {
                        try {
                            if (!txtCodiceArticolo.getText().equalsIgnoreCase("")) {
                                PrezzoConSconto prezzoArticolo = null;
                                if (getCmbModalita().getSelectedIndex() == 0)
                                    prezzoArticolo = new DbConnector().getPrezzoArticolo(txtCodiceArticolo.getText(),
                                            clienteSelezionato.getId());
                                else
                                    prezzoArticolo = new DbConnector().getPrezzoArticoloFromBarcode(txtCodiceArticolo.getText(),
                                            clienteSelezionato.getId());

                                if (prezzoArticolo != null) {
                                    setPrezzoArticolo(prezzoArticolo);

                                    if (getCmbModalita().getSelectedIndex() == 0) {
                                        txtQtaArticolo.requestFocusInWindow();
                                    } else {
                                        if (!prezzoArticolo.getArticolo().isCompleteBarCode()) { // l'articolo non ha una qta
                                                                                                 // predefinita
                                            txtQtaArticolo.setText(BarcodeSplitter.readQta(txtCodiceArticolo.getText()).toString());
                                        } else {
                                            txtQtaArticolo.setText(prezzoArticolo.getArticolo().getQtaPredefinita().toString());
                                        }
                                        txtPezzi.setText("1");
                                        txtLottoArticolo.requestFocusInWindow();
                                    }

                                } else {
                                    articolo = null;
                                    buttonTrovaArticolo.requestFocus();
                                    txtDescrizioneArticolo.setText("");
                                    txtUMArticolo.setText("");
                                }
                            } else {
                                buttonTrovaArticolo.requestFocus();
                            }
                        } catch (Exception e1) {
                            buttonTrovaArticolo.requestFocus();
                        }
                    }
                }
            });
        }
        return txtCodiceArticolo;
    }

    private JTextField getTxtQtaArticolo() {
        if (txtQtaArticolo == null) {
            txtQtaArticolo = new JTextField();
            txtQtaArticolo.setEnabled(false);
            txtQtaArticolo.setColumns(6);
            txtQtaArticolo.setMinimumSize(new Dimension(60, 22));
            txtQtaArticolo.setInputVerifier(new NumberFormattedVerifier(3));
            // txtQtaArticolo.addKeyListener(new KeyAdapter() {
            // public void keyPressed(KeyEvent e) {
            // if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            // buttonOk.requestFocus();
            // }
            // }
            // });
            // txtQtaArticolo.addKeyListener(BarcodeHandler.getInstance());
        }
        return txtQtaArticolo;
    }

    private JTextField getTxtLottoArticolo() {
        if (txtLottoArticolo == null) {
            txtLottoArticolo = new JTextField();
            txtLottoArticolo.setEnabled(false);
            txtLottoArticolo.setColumns(12);
            txtLottoArticolo.setMinimumSize(new Dimension(180, 22));
            txtLottoArticolo.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        buttonOk.doClick();
                    }
                }
            });
            // IMPOSTO IL VALORE PREDEFINITO
            // DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            // txtLottoArticolo.setText(dateFormat.format(Calendar.getInstance().getTime()));
            txtLottoArticolo.setText("");
        }
        return txtLottoArticolo;
    }

    public void setArticolo(Object codice) {
        applet.setContentPane(getContentPane());
        applet.validate();
        applet.getContentPane().requestFocusInWindow();
        // contentPane.getRootPane().setDefaultButton(buttonOk);

        if (codice != null) {
            PrezzoConSconto prezzoArticolo = new DbConnector().getPrezzoArticolo((String) codice, clienteSelezionato.getId());
            if (prezzoArticolo != null) {
                setPrezzoArticolo(prezzoArticolo);
                txtQtaArticolo.requestFocus();
            } else {
                txtCodiceArticolo.requestFocus();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private JButton getButtonTrovaArticolo() {
        if (buttonTrovaArticolo == null) {
            buttonTrovaArticolo = new JButton();
            buttonTrovaArticolo.setText("...");
            buttonTrovaArticolo.setEnabled(false);
            buttonTrovaArticolo.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {

                    applet.setContentPane(listaArticoli.getContentPanel());
                    applet.validate();
                    listaArticoli.getTxtTrova().requestFocus();

                    /*
                     * dialog.getDialog().setVisible(true); PrezzoConSconto prezzoArticolo = new
                     * DbConnector().getPrezzoArticolo((String) dialog.getSelectedElement(), clienteSelezionato.getId()); if
                     * (prezzoArticolo != null) { setPrezzoArticolo(prezzoArticolo); txtQtaArticolo.requestFocus(); } else {
                     * txtDescrizioneArticolo.requestFocus(); }
                     */
                }
            });
            buttonTrovaArticolo.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        buttonTrovaArticolo.doClick();
                        e.consume();
                    }
                }
            });

        }
        return buttonTrovaArticolo;
    }

    private JTextField getTxtDescrizioneArticolo() {
        if (txtDescrizioneArticolo == null) {
            txtDescrizioneArticolo = new JTextField();
            txtDescrizioneArticolo.setEnabled(false);
            txtDescrizioneArticolo.setMinimumSize(new Dimension(70, 22));
        }
        return txtDescrizioneArticolo;
    }

    private JTextField getTxtUMArticolo() {
        if (txtUMArticolo == null) {
            txtUMArticolo = new JTextField();
            txtUMArticolo.setEnabled(false);
            txtUMArticolo.setColumns(2);
            txtUMArticolo.setMinimumSize(new Dimension(20, 22));
        }
        return txtUMArticolo;
    }

    private JTextField getTxtPezzi() {
        if (txtPezzi == null) {
            txtPezzi = new JTextField();
            txtPezzi.setEnabled(false);
            txtPezzi.setColumns(7);
            txtPezzi.setMinimumSize(new Dimension(70, 22));
        }
        return txtPezzi;
    }

    private JTextField getTxtPrezzoArticolo() {
        if (txtPrezzoArticolo == null) {
            txtPrezzoArticolo = new JTextField();
            txtPrezzoArticolo.setEnabled(false);
            txtPrezzoArticolo.setColumns(6);
            txtPrezzoArticolo.setMinimumSize(new Dimension(70, 22));
            txtPrezzoArticolo.setInputVerifier(new NumberFormattedVerifier(2));
            txtPrezzoArticolo.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        // buttonOk.requestFocus();
                    }
                }
            });
        }
        return txtPrezzoArticolo;
    }

    private JTextField getTxtScontoArticolo() {
        if (txtScontoArticolo == null) {
            txtScontoArticolo = new JTextField();
            txtScontoArticolo.setEnabled(false);
            txtScontoArticolo.setColumns(3);
            txtScontoArticolo.setMinimumSize(new Dimension(30, 22));
            txtScontoArticolo.setInputVerifier(new NumberFormattedVerifier(0));
            txtScontoArticolo.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        // buttonOk.requestFocus();
                    }
                }
            });
        }
        return txtScontoArticolo;
    }

    private JComboBox getCmbIva() {
        if (cmbIva == null) {
            cmbIva = new JComboBox();
            cmbIva.setEnabled(false);
            DefaultComboBoxModel cmb = (DefaultComboBoxModel) cmbIva.getModel();
            try {
                Collection ivas = new DbConnector().getListaIva();
                Iterator itr = ivas.iterator();
                while (itr.hasNext()) {
                    cmb.addElement(itr.next());
                }
            } catch (Exception e) {
                cmb.removeAllElements();
            }
        }
        return cmbIva;
    }

    private JTextField getTxtDataDDT() {
        if (txtDataDDT == null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
            DateFormatter dateFormatter = new DateFormatter(dateFormat);
            txtDataDDT = new JFormattedTextField(dateFormatter);
            txtDataDDT.setInputVerifier(new DateFormattedVerifier("dd/MM/yyyy"));
            Calendar calendar = Calendar.getInstance();
            txtDataDDT.setText(dateFormat.format(calendar.getTime()));
            txtDataDDT.setColumns(10);
            txtDataDDT.setMinimumSize(new Dimension(90, 21));
        }
        return txtDataDDT;
    }

    public void barcodeRead(Barcode barcode) {
        if (barcode != null) {
            if (barcode.isNumeroLotto()) {
                // inserisco il lotto e inserisco l'articolo nella tabella
                txtLottoArticolo.setText(barcode.getBarcodeString());
                inserisciRigaCorrente();
            } else {
                try {
                    PrezzoConSconto dati = barcode.getDatiAssociati();
                    if (dati != null)
                        setPrezzoArticolo(dati);
                    BigDecimal number;
                    if (dati.getArticolo().isCompleteBarCode())
                        number = dati.getArticolo().getQtaPredefinita();
                    else
                        number = barcode.getQtaLetta();

                    DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
                    formatter.applyPattern("###0.000");
                    number = number.setScale(3, BigDecimal.ROUND_HALF_UP);
                    txtQtaArticolo.setText(formatter.format(number));
                    txtPezzi.setText("1");

                    setModalitaInserimentoArticolo(true);
                    txtLottoArticolo.requestFocus();
                } catch (Exception e) {
                }
            }
        } else {
            // TODO: BIP SONORO
        }
    }

    public Cliente getClienteSelezionato() {
        return clienteSelezionato;
    }

    public void loadOrdineCliente() {

        DettagliTableModel model = (DettagliTableModel) table.getModel();
        Map<Integer, Integer> values = new HashMap<Integer, Integer>();

        ((DettagliTableModel) getTable().getModel()).removeAllElements();
        txtTotaleDDT.setText("");

        if (puntoConsegnaSelezionato == null)
            return;

        Integer idPuntoConsegna = -1;
        if (clienteSelezionato.getPuntiConsegna().size() > 1)
            idPuntoConsegna = puntoConsegnaSelezionato.getId();

        dettagliOrdini = new DbConnector().getListaDettagliOrdini(clienteSelezionato.getId(), idPuntoConsegna,
                getTxtDataTrasporto().getText());
        Iterator itr = dettagliOrdini.iterator();
        Set noteMostrate = new HashSet();
        while (itr.hasNext()) {
            DettaglioOrdine det = (DettaglioOrdine) itr.next();

            if (det.getOrdine() != null && !noteMostrate.contains(det.getIdOrdine().intValue()) && det.getOrdine().getNote() != null
                    && !det.getOrdine().getNote().equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(contentPane,
                        "Nota ordine " + det.getOrdine().getProgressivoCompleto() + ":" + "\n" + det.getOrdine().getNote());
                noteMostrate.add(det.getIdOrdine().intValue());
            }

            EvasioneOrdine eo = new EvasioneOrdine();
            eo.setIdDettaglioOrdine(det.getId());
            eo.setDettaglioOrdine(det);
            if (values.containsKey(det.getIdArticolo())) {
                Integer row = values.get(det.getIdArticolo());
                model.setValueAt((Integer) model.getValueAt(row, 4) + det.getPezziDaEvadere(), row, 4);
                if (model.getValueAt(row, COL_ID_ORDINI) instanceof ArrayList) {
                    ((ArrayList<EvasioneOrdine>) model.getValueAt(row, COL_ID_ORDINI)).add(eo);
                } else {
                    ArrayList<EvasioneOrdine> list = new ArrayList<EvasioneOrdine>();
                    list.add((EvasioneOrdine) model.getValueAt(row, COL_ID_ORDINI));
                    list.add(eo);
                    model.setValueAt(list, row, COL_ID_ORDINI);
                }
            } else {
                Object[] dati = new Object[TABLE_COLUMNS];
                dati[0] = det.getArticolo().getCodiceArticolo();
                dati[1] = det.getArticolo().getDescrizione();
                dati[2] = "0";
                dati[3] = 0;
                dati[4] = det.getPezziDaEvadere();
                PrezzoConSconto prezzoArticolo = new DbConnector().getPrezzoArticolo(det.getArticolo().getCodiceArticolo(),
                        clienteSelezionato.getId());
                NumberFormat formatter = DecimalFormat.getInstance(Locale.ITALIAN);
                formatter.setMinimumFractionDigits(2);
                dati[5] = formatter.format(prezzoArticolo.getPrezzo());
                formatter.setMinimumFractionDigits(1);
                dati[6] = formatter.format(prezzoArticolo.getSconto());
                dati[7] = det.getArticolo().getIva().getValore();
                dati[8] = det.getArticolo().getUm();
                dati[COL_LOTTO] = "";
                dati[10] = null;
                dati[COL_ID_ORDINI] = eo;
                dati[12] = det.getArticolo();
                model.addRow(dati);
                values.put(det.getIdArticolo(), model.getRowCount() - 1);
            }
        }
    }
}
