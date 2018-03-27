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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Locale;
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
import applet.barcode.BarcodeHandlerNota;
import applet.barcode.BarcodeSplitter;
import applet.common.DateFormattedVerifier;
import applet.common.NumberFormattedVerifier;
import applet.common.Settings;
import applet.db.DbConnector;
import dao.NoteAccredito;
import vo.Articolo;
import vo.Cliente;
import vo.DDT;
import vo.DettaglioNotaAccredito;
import vo.Iva;
import vo.NotaAccredito;
import vo.PrezzoConSconto;
import vo.PuntoConsegna;

public class NotaAccreditoWindow {

	private static final int TABLE_COLUMNS = 13;
	private NotaAccredito currentNotaAccredito = null;
	private boolean modalitaAggiornamento = false;

	private JPanel contentPane = null;
	private JPanel panelIntestazione = null;
	private JPanel panelDettaglio = null;
	private JPanel panelCausale = null;
	private JPanel panelBottoni = null;
	private JPanel panelLettore = null;
	private JLabel labelSelezionaCliente = null;
	private JTextArea txaCliente = null;
	private JButton buttonTrovaCliente = null;
	private JTextArea txaDestCliente = null;
	private JButton buttonDestClientePrec = null;
	private JButton buttonDestClienteSuc = null;
	private JLabel labelDestCliente = null;
	private JLabel labelCausale = null;
	private JTextArea txtCausale = null;
	private JButton buttonNuovo = null;
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
	private JButton buttonModifica = null;

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

	//private Collection clienti;
	//private Collection articoli;
	private ChooseDialog listaClienti;
	private ChooseDialog listaArticoli;
	private JApplet applet;
	
	private static final int COL_ID_ORDINI = 11;
	private static final int COL_LOTTO = 9;

	@SuppressWarnings("unchecked")
	private NotaAccredito getNotaAccredito() throws Exception {
		NotaAccredito ddt;
		if (modalitaAggiornamento)
			ddt = currentNotaAccredito;
		else
			ddt = new NotaAccredito();
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
		
		ddt.setDettagliAccredito(getDettagliDDT());
		ddt.setCausale(txtCausale.getText());
		
		ddt.setAspettoEsteriore("scatole");
		
		DateFormat data = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
		try {
			ddt.setData(data.parse(txtDataDDT.getText()));
			data = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
		} catch (ParseException e) {
			throw new Exception("Le date non sono corrette");
		}
		//ddt.setCausale(txtCausale.getText());
		//ddt.setCausale("vendita");
		
		return ddt;
	}
	
	@SuppressWarnings("unchecked")
	private Vector getDettagliDDT() throws ParseException {
		Vector articoli = new Vector();
		NumberFormat nf = DecimalFormat.getInstance(Locale.ITALIAN);
		for(int i=0;i<table.getRowCount();++i) {
			DettaglioNotaAccredito dettaglioDDT = new DettaglioNotaAccredito();
			
			dettaglioDDT.setCodiceArticolo((String)table.getValueAt(i, 0));
			dettaglioDDT.setDescrizioneArticolo((String)table.getValueAt(i,1));
			if (table.getValueAt(i, 2) != null && !table.getValueAt(i, 2).equals("")) 
				dettaglioDDT.setQta(new BigDecimal(nf.parse((String)table.getValueAt(i, 2)).doubleValue()).setScale(3,BigDecimal.ROUND_HALF_UP));
			if (table.getValueAt(i, 3) != null && !table.getValueAt(i, 3).equals(""))
				dettaglioDDT.setPezzi((Integer)table.getValueAt(i,3));
			if (table.getValueAt(i, 5) != null && !table.getValueAt(i, 5).equals(""))
				dettaglioDDT.setPrezzo(new BigDecimal(nf.parse((String)table.getValueAt(i, 5)).doubleValue()).setScale(2,BigDecimal.ROUND_HALF_UP));
			if(table.getValueAt(i,6) != null && table.getValueAt(i,6).toString().trim().length() > 0) 
				dettaglioDDT.setSconto(new BigDecimal(nf.parse((String)table.getValueAt(i,6)).doubleValue()).setScale(2,BigDecimal.ROUND_HALF_UP));
			if (table.getValueAt(i, 7) != null && !table.getValueAt(i, 7).equals(""))
				dettaglioDDT.setIva((BigDecimal)table.getValueAt(i,7));
			dettaglioDDT.setUm((String)table.getValueAt(i,8));
			dettaglioDDT.setLotto((String)table.getValueAt(i,COL_LOTTO));	
			if (table.getModel().getValueAt(i, 10) != null)
				dettaglioDDT.setId((Integer)table.getModel().getValueAt(i, 10));
			
			if(table.getModel().getValueAt(i, 12) != null)
				dettaglioDDT.setArticolo((Articolo) table.getModel().getValueAt(i, 12));
			if (dettaglioDDT.getArticolo() != null)
				dettaglioDDT.setIdArticolo(dettaglioDDT.getArticolo().getId());
			
			articoli.add(dettaglioDDT);
		}
		return articoli;
	}

	private boolean checkArticoloCorrente() {
		return true;
	}

	private void setModalitaAggiornamentoArticolo(boolean modalita) {
		//contentPane.getRootPane().setDefaultButton(buttonOk);
		//txtCodiceArticolo.setEnabled(!modalita);
		//buttonTrovaArticolo.setEnabled(!modalita);
		//txtDescrizioneArticolo.setEnabled(!modalita);
		//txtUMArticolo.setEnabled(!modalita);
		txtQtaArticolo.setEnabled(modalita);
		txtPrezzoArticolo.setEnabled(modalita);
		txtScontoArticolo.setEnabled(modalita);
		txtPezzi.setEnabled(modalita);
		//cmbIva.setEnabled(!modalita);
		txtLottoArticolo.setEnabled(modalita);
		buttonInserisciRiga.setEnabled(!modalita);
		buttonOk.setEnabled(modalita);
		//buttonOk.setDefaultCapable(true);
		buttonAnnulla.setEnabled(modalita);
		buttonModifica.setEnabled(true);
		if (modalita)
			txtCodiceArticolo.requestFocus();
		buttonCancellaArticolo.setEnabled(modalita);
		updateRow = modalita;
		table.setEnabled(!modalita);
	}
	
	private void editTable() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1) {
			//AGGIORNO I CAMPI E IMPOSTO MODALITA AGGIORNAMENTO RIGA
			txtCodiceArticolo.setText((String)table.getModel().getValueAt(selectedRow, 0));
			txtDescrizioneArticolo.setText((String)table.getModel().getValueAt(selectedRow, 1));
			txtQtaArticolo.setText((String)table.getModel().getValueAt(selectedRow, 2));
			if (table.getModel().getValueAt(selectedRow, 3) != null)
				txtPezzi.setText(table.getModel().getValueAt(selectedRow, 3).toString());
			else
				txtPezzi.setText("");

			txtPrezzoArticolo.setText((String)table.getModel().getValueAt(selectedRow, 5));
			txtScontoArticolo.setText((String)table.getModel().getValueAt(selectedRow, 6));
			BigDecimal iva = (BigDecimal)table.getModel().getValueAt(selectedRow, 7);
			txtUMArticolo.setText((String)table.getModel().getValueAt(selectedRow, 8));
			txtLottoArticolo.setText((String)table.getModel().getValueAt(selectedRow, COL_LOTTO));
			
			for (int i=0;i<cmbIva.getItemCount();++i)
				if (iva == ((Iva)cmbIva.getItemAt(i)).getValore())
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
		number = number.setScale(2,BigDecimal.ROUND_HALF_UP);
		txtPrezzoArticolo.setText(formatter.format(number.doubleValue()));
		number = prezzo.getSconto();
		number = number.setScale(2,BigDecimal.ROUND_HALF_UP);
		formatter.applyPattern("####.##");
		txtScontoArticolo.setText(formatter.format(number.doubleValue()));
		BigDecimal iva = prezzo.getArticolo().getIva().getValore();
		for (int i=0;i<cmbIva.getItemCount();++i)
			if (iva == ((Iva)cmbIva.getItemAt(i)).getValore())
					cmbIva.setSelectedIndex(i);
		lblListinoArticolo.setText("Listino: " + prezzo.getListino().getDescrizione());
		articolo = prezzo.getArticolo();
	}
	
	private void inserisciArticoloCorrente() {
		Object[] dati = new Object[TABLE_COLUMNS];
		dati[0] = txtCodiceArticolo.getText();
		dati[1] = txtDescrizioneArticolo.getText();
		dati[2] = txtQtaArticolo.getText();
		if(txtPezzi.getText() != null && txtPezzi.getText().trim().length() > 0) 
            dati[3] = Integer.valueOf(txtPezzi.getText());
		dati[4] = null;
		dati[5] = txtPrezzoArticolo.getText();
		dati[6] = txtScontoArticolo.getText();
		dati[7] = ((Iva)cmbIva.getSelectedItem()).getValore();
		dati[8] = txtUMArticolo.getText();
		dati[COL_LOTTO] = txtLottoArticolo.getText();
		dati[10] = null;
		dati[COL_ID_ORDINI] = null;
		dati[12] = articolo;
		DettagliTableModel model = (DettagliTableModel)table.getModel();
		Integer nuoviPezziEvad = null;
		
		if (!updateRow) {
			model.addRow(dati);
		} else {
			int selectedRow = table.getSelectedRow();
			dati[10] = table.getModel().getValueAt(selectedRow, 10); //INV DDT
			dati[COL_ID_ORDINI] = table.getModel().getValueAt(selectedRow, COL_ID_ORDINI); //ID ORDINI
			Integer vecchiPezzi = 0;
			if (table.getValueAt(selectedRow, 3) != null)
				vecchiPezzi = (Integer) table.getModel().getValueAt(selectedRow, 3);
			if (table.getValueAt(selectedRow, 4) != null)
				nuoviPezziEvad = (Integer)table.getModel().getValueAt(selectedRow, 4) - (Integer.valueOf(txtPezzi.getText()) - (Integer)vecchiPezzi); //PEZZI DA EVADERE
			model.removeRow(selectedRow);
			model.insertRow(selectedRow, dati);
		}
		try {
			BarcodeHandler.getInstance().setNextReadLotto(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setModalitaInserimentoArticolo(boolean modalita) {
		//contentPane.getRootPane().setDefaultButton(buttonOk);
		txtCodiceArticolo.setEnabled(modalita);
		buttonTrovaArticolo.setEnabled(modalita);
		//txtDescrizioneArticolo.setEnabled(modalita);
		//txtUMArticolo.setEnabled(modalita);
		txtQtaArticolo.setEnabled(modalita);
		txtPezzi.setEnabled(modalita);
		txtPrezzoArticolo.setEnabled(modalita);
		txtScontoArticolo.setEnabled(modalita);
		//cmbIva.setEnabled(modalita);
		txtLottoArticolo.setEnabled(modalita);
		buttonInserisciRiga.setEnabled(!modalita);
		buttonOk.setEnabled(modalita);		
		buttonModifica.setEnabled(false);
		buttonAnnulla.setEnabled(modalita);
		updateRow = !modalita;
		//TOGLIE EVENTUALI INPUTVERIFIER
		txtQtaArticolo.setInputVerifier(null);
		txtPrezzoArticolo.setInputVerifier(null);
		txtScontoArticolo.setInputVerifier(null);
		if (modalita)
			txtCodiceArticolo.requestFocus();
		else
			buttonInserisciRiga.requestFocus();
		//LI RIMETTE
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

		//IMPOSTO IL VALORE PREDEFINITO
		//DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		//txtLottoArticolo.setText(dateFormat.format(Calendar.getInstance().getTime()));
		txtLottoArticolo.setText("");
	}
	
	private void setPuntoConsegna(PuntoConsegna puntoConsegna) {
		if (puntoConsegna == null) {
			txaDestCliente.setText("Nessun punto di consegna selezionato");
			puntoConsegnaSelezionato = null;
		} else {
			String txtDest = puntoConsegna.getNome() + "\n" + 
							puntoConsegna.getCap() + " " +
							puntoConsegna.getIndirizzo() + "\n" +
							puntoConsegna.getLocalita() + " " + puntoConsegna.getProv();
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
					
		}
	}
	
	public void setCliente(Object idCliente) {
		applet.setContentPane(getContentPane());
		applet.validate();
		applet.getContentPane().requestFocusInWindow();
		Cliente cliente = new DbConnector().getCliente((Integer)idCliente);
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
			JOptionPane.showMessageDialog(contentPane, 
				"Questo cliente non puo' essere selezionato perche' non ci sono destinazioni",
				"Attenzione",JOptionPane.ERROR_MESSAGE);
		
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
		buttonStampa.setEnabled(false);
		buttonSSN.setEnabled(true);
		buttonSN.setEnabled(true);
		cbNumerAutomatico.setEnabled(true);
		cbNumerAutomatico.setSelected(true);
		txtNumDDT.setText("");
		txtNumDDT2.setText("");
		txtCausale.setText("");
		setModalitaAggiornamentoArticolo(false);
		setModalitaInserimentoArticolo(false);
		//PULISCO TUTTI I CAMPI
		setCliente(null);
		setPuntoConsegna(null);
		//DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		//txtOraTrasporto.setText(dateFormat.format(new Date()));
		//dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		//txtDataTrasporto.setText(dateFormat.format(new Date()));
		pulisciCampiArticolo();
		((DettagliTableModel)table.getModel()).removeAllElements();
		getTxtTotaleDDT().setText("");
		contentPane.requestFocusInWindow();
	}
		
	@SuppressWarnings("unchecked")
	public void setModalitaAggiornamentoDDT() {
		try {
			modalitaAggiornamento = true;
			pulisciCampiArticolo();
			setModalitaInserimentoArticolo(false);
			setModalitaAggiornamentoArticolo(false);
			setCliente(currentNotaAccredito.getCliente());
			setPuntoConsegna(currentNotaAccredito.getPuntoConsegna());
			txtNumDDT.setText(currentNotaAccredito.getNumeroProgressivo().toString());
			txtNumDDT2.setText(currentNotaAccredito.getNumeroProgressivo2());
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
			txtDataDDT.setText(df.format(currentNotaAccredito.getData()));
			((DettagliTableModel)table.getModel()).removeAllElements();
			ListIterator itr = currentNotaAccredito.getDettagliAccredito().listIterator();
			while (itr.hasNext()) {
				DettaglioNotaAccredito dettaglio = (DettaglioNotaAccredito)itr.next();
				Object[] dati = new Object[TABLE_COLUMNS];
				dati[0] = dettaglio.getCodiceArticolo();
				dati[1] = dettaglio.getDescrizioneArticolo();
				BigDecimal number = dettaglio.getQta();
				NumberFormat formatter = DecimalFormat.getInstance(Locale.ITALIAN);
				formatter.setMinimumFractionDigits(3);
				if(number != null)
					dati[2] = formatter.format(number.doubleValue());
				dati[3] = dettaglio.getPezzi();
						
				
				dati[4] = null;
				formatter.setMinimumFractionDigits(2);
				number = dettaglio.getPrezzo();
				if(number != null)
					dati[5] = formatter.format(number.doubleValue());
				formatter.setMinimumFractionDigits(1);
				number = dettaglio.getSconto();
				if(number != null)
					dati[6] =formatter.format(number.doubleValue());
				dati[7] = dettaglio.getIva();
				dati[8] = dettaglio.getUm();
				dati[COL_LOTTO] = dettaglio.getLotto();
				dati[10] = dettaglio.getId();
				dati[COL_ID_ORDINI] = null;
				dati[12] = dettaglio.getArticolo();
				DettagliTableModel model = (DettagliTableModel)table.getModel();
				model.addRow(dati);
			}
			
			txtCausale.setText(currentNotaAccredito.getCausale());
			df = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
			aggiornaTotaleDettaglio();
			cbNumerAutomatico.setSelected(false);
			cbNumerAutomatico.setEnabled(false);
			buttonSN.setEnabled(false);
			buttonSSN.setEnabled(false);
			txtNumDDT.setEnabled(true);
			txtNumDDT2.setEnabled(true);
			buttonStampa.setEnabled(true);
			contentPane.requestFocusInWindow();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(contentPane, e.getMessage(),"Attenzione",JOptionPane.ERROR_MESSAGE);
		} 
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public NotaAccreditoWindow() throws Exception {
		
	}
	
	public NotaAccreditoWindow(long idDDT, JApplet applet) throws Exception {
		this(applet);
		currentNotaAccredito = new DbConnector().getNotaAccredito(idDDT);
		modalitaAggiornamento = true;
	}
	
	public NotaAccreditoWindow(JApplet applet) throws SQLException {
		BarcodeHandlerNota.getInstance().setWindow(this);
		this.applet = applet;
		
		String[] colH = { "Codice", "Descrizione" };
		listaArticoli = new ChooseDialog("Scegli articolo", 
				colH, 2, new DbConnector().getListaArticoli(), this, "setArticolo");
		
		String[] colH2 = { "Ragione sociale" };
		listaClienti = new ChooseDialog("Scegli cliente", 
				colH2, 1, new DbConnector().getListaClienti(), this, "setCliente");
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public JPanel getContentPane() {
		if (contentPane == null) {
			contentPane = new JPanel(new GridBagLayout());
			contentPane.setSize(new Dimension(620,650));
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.insets = new Insets(0,0,0,10);
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
			panelIntestazione.setBorder(BorderFactory.createTitledBorder(null, "Intestazione", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), new Color(0, 70, 213)));
			panelIntestazione.setMinimumSize(new Dimension(contentPane.getWidth(),135));
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
			c.insets = new Insets(0,2,0,2);
			panelCliente.add(getButtonTrovaCliente(), c);
			c.gridx = 1;
			c.gridy = 1;
			c.anchor = GridBagConstraints.SOUTHWEST;
			//panelCliente.add(getButtonClientePrec(), c);
			c.gridx = 1;
			c.gridy = 2;
			//panelCliente.add(getButtonClienteSuc(), c);
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
			panelDettaglio.setBorder(BorderFactory.createTitledBorder(null, "Dettaglio", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), new Color(0, 70, 213)));
			//panelDettaglio.setMinimumSize(new Dimension(frame.getWidth(),0));
			//panelDettaglio.setPreferredSize(new Dimension(frame.getWidth(), 0));
			panelDettaglio.add(getScrollPane(), BorderLayout.CENTER);
			panelDettaglio.add(getPanelDettaglioRiga(), BorderLayout.PAGE_END);
		}
		return panelDettaglio;
	}

	private JPanel getPanelSpedizione() {
		if (panelCausale == null) {
			labelCausale = new JLabel();
			labelCausale.setText("Descrizione:");
			
			panelCausale = new JPanel(new GridBagLayout());
			panelCausale.setSize(panelCausale.getSize().width, 200);
			panelCausale.setBorder(BorderFactory.createTitledBorder(null, "Descrizione sconto", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), new Color(0, 70, 213)));
			
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.insets = new Insets(0,0,0,10);
			c.fill = GridBagConstraints.HORIZONTAL;
			contentPane.add(getPanelLettore(), c);
			c.gridy = 1;
			
			panelCausale.add(getTxtCausale(), c);	
		}
		return panelCausale;
	}

	private JPanel getPanelBottoni() {
		if (panelBottoni == null) {
			panelBottoni = new JPanel();
			panelBottoni.setLayout(new BoxLayout(panelBottoni, BoxLayout.LINE_AXIS));
			panelBottoni.add(Box.createRigidArea(new Dimension(2,0)));
			panelBottoni.add(getButtonNuovo());
			panelBottoni.add(Box.createRigidArea(new Dimension(5,0)));
			panelBottoni.add(getButtonSN());
			panelBottoni.add(Box.createRigidArea(new Dimension(5,0)));
			panelBottoni.add(getButtonSSN());
			panelBottoni.add(Box.createHorizontalGlue());
			panelBottoni.add(getButtonModifica());
			panelBottoni.add(Box.createRigidArea(new Dimension(5,0)));
			panelBottoni.add(getButtonStampa());
			panelBottoni.add(Box.createRigidArea(new Dimension(2,0)));
		}
		return panelBottoni;
	}

	private JPanel getPanelLettore() {
		if (panelLettore == null) {
			panelLettore = new JPanel();
			panelLettore.setLayout(new BorderLayout());
			//panelLettore.setPreferredSize(new Dimension(0,32));
			//panelLettore.setSize(new Dimension(0,50));
			
			lblStatoLettore = new JLabel();
			lblStatoLettore.setHorizontalAlignment(SwingConstants.CENTER);
			lblStatoLettore.setForeground(Color.RED);

			panelLettore.add(lblStatoLettore,BorderLayout.CENTER);
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
				public void actionPerformed(java.awt.event.ActionEvent e) {
					applet.setContentPane(listaClienti.getContentPanel());
					applet.validate();
					listaClienti.getTxtTrova().requestFocus();
					
					//applet.getContentPane().add(dialog.getContentPanel());
					/*dialog.getDialog().setVisible(true);
					if (dialog.getSelectedElement() != null) {
						
					} else
						buttonTrovaCliente.requestFocus();*/
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
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setPuntoConsegna((PuntoConsegna)itrPuntiConsegna.previous());
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
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setPuntoConsegna((PuntoConsegna)itrPuntiConsegna.next());
				}
			});
		}
		return buttonDestClienteSuc;
	}

	
	private JComboBox getCmbModalita() {
		if (cmbModalita == null) {
			String[] tipo = {"cod. prodotto", "cod. barre", "manuale"};
			cmbModalita = new JComboBox(tipo);
			cmbModalita.setSelectedIndex(1);			
			cmbModalita.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					txtDescrizioneArticolo.setEnabled(false);
					txtDescrizioneArticolo.setEditable(false);
					cmbIva.setEditable(false);
					cmbIva.setEnabled(false);
					txtPezzi.setEditable(true);
					txtPezzi.setEnabled(true);
					if(cmbModalita.getSelectedIndex() == 1) {
						setModalitaAggiornamentoArticolo(true);
						setModalitaInserimentoArticolo(true);
						txtCodiceArticolo.setEditable(true);
					} else if(cmbModalita.getSelectedIndex() == 2) {
						setModalitaAggiornamentoArticolo(true);
						setModalitaInserimentoArticolo(true);
						txtCodiceArticolo.setEditable(false);						
						txtDescrizioneArticolo.setEnabled(true);
						txtDescrizioneArticolo.setEditable(true);
						cmbIva.setEditable(true);
						cmbIva.setEnabled(true);
						txtPezzi.setEditable(false);
						txtPezzi.setEnabled(false);
						txtDescrizioneArticolo.requestFocus();
					} 
				}
			});
		}
		return cmbModalita;
	}


	private JButton getButtonModifica() {
		if (buttonModifica == null) {
			buttonModifica = new JButton();
			buttonModifica.setText("Modifica");
			buttonModifica.setMnemonic('d');
			buttonModifica.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						NotaAccredito ddt = getNotaAccredito();
						new DbConnector().saveNotaAccredito(ddt);
						JOptionPane.showMessageDialog(contentPane, "La Nota Accredito � stato aggiornato con successo","Successo",JOptionPane.INFORMATION_MESSAGE);
						buttonModifica.setEnabled(false);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(contentPane, e1.getMessage(),"Attenzione",JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
		return buttonModifica;
	}
	
	private JTextArea getTxtCausale() {
		if (txtCausale == null) {
			txtCausale = new JTextArea(3, 100);
			txtCausale.setMinimumSize(new Dimension(90,100));
		}
		return txtCausale;
	}

	private JButton getButtonNuovo() {
		if (buttonNuovo == null) {
			buttonNuovo = new JButton();
			buttonNuovo.setText("Nuova Nota");
			buttonNuovo.setMnemonic('o');
			buttonNuovo.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setModalitaInserimentoDDT();
				}
			});
		}
		return buttonNuovo;
	}

	private JButton getButtonSSN() {
		if (buttonSSN == null) {
			buttonSSN = new JButton();
			buttonSSN.setText("Salva/Stampa/Nuovo");
			buttonSSN.setMnemonic('S');
			buttonSSN.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						NotaAccredito ddt = getNotaAccredito();
						//SALVO L'OGGETTO DDT
						int idDDT = new DbConnector().saveNotaAccredito(ddt);
						JOptionPane.showMessageDialog(getPanelBottoni(), "Il DDT � stato salvato con successo","Successo",JOptionPane.INFORMATION_MESSAGE);
						setModalitaInserimentoDDT();
						stampaDDT(idDDT);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(getPanelBottoni(), e1.getMessage(),"Attenzione",JOptionPane.ERROR_MESSAGE);
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
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						NotaAccredito ddt = getNotaAccredito();
						new DbConnector().saveNotaAccredito(ddt);
						JOptionPane.showMessageDialog(contentPane, "La Nota di Accredito � stato salvato con successo","Successo",JOptionPane.INFORMATION_MESSAGE);
						setModalitaInserimentoDDT();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(contentPane, e1.getMessage(),"Attenzione",JOptionPane.ERROR_MESSAGE);
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
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						int idDDT = currentNotaAccredito.getId();
						
						if (!currentNotaAccredito.getFatturato()) {
							int result = JOptionPane.showConfirmDialog(getPanelBottoni(), "Attenzione, prima di procedere con la stampa e' necessario salvare il documento. Continuare?","Attenzione",JOptionPane.OK_CANCEL_OPTION);
							if (result == JOptionPane.OK_OPTION) {
								NotaAccredito ddt = getNotaAccredito();
								new DbConnector().saveNotaAccredito(ddt);
								JOptionPane.showMessageDialog(getPanelBottoni(), "Il DDT � stato salvato con successo","Successo",JOptionPane.INFORMATION_MESSAGE);
								stampaDDT(idDDT);
							}
						} else
							stampaDDT(idDDT);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(getPanelBottoni(), e1.getMessage(),"Attenzione",JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
		return buttonStampa;
	}
	
	private void stampaDDT(int idDDT) {
		URL url;
		try {
			/*String osname = System.getProperty("os.name");
			if (!osname.startsWith("Windows")) {
				JOptionPane.showMessageDialog(panelBottoni, "La stampa � supportata solamente su sistemi Windows");
				return;
			}*/
			
			url = new URL(Settings.getInstance().getPrintNotaUrl() + "?id=" + idDDT);
			URLConnection uc = url.openConnection();
			InputStream is = uc.getInputStream();

			String osname = System.getProperty("os.name");
			if (osname.startsWith("Windows")) {
				//SALVO IL PDF IN UNA CARTELLA TEMPORANEA
				int length = uc.getContentLength();
				byte[] array = new byte[length];
				is.read(array);
				
				File file = File.createTempFile("pdf" + idDDT, ".pdf");
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(array);
				fos.flush();
				fos.close();
				
				//LO STAMPO USANDO ACROBAT READER
				String command = "cmd /C start acrord32 /p /h " + file.getAbsolutePath() + "";
			    Runtime rn = Runtime.getRuntime();
			    Process process = rn.exec(command);
			    process.waitFor();
			    return;
			}
			
			PrintService ps = PrintServiceLookup.lookupDefaultPrintService();
			ps.getSupportedDocFlavors();
			DocFlavor flavor = DocFlavor.INPUT_STREAM.PDF;
			Doc doc = new SimpleDoc(is,flavor,null);
			
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
			lblCercaCodice.setText("Cerca");
			lblTotaleDDT = new JLabel();
			lblTotaleDDT.setText("Totale Euro:");
			lblListinoArticolo = new JLabel();
			lblListinoArticolo.setText("");

			panelDettaglioRiga = new JPanel();
			panelDettaglioRiga.setMinimumSize(new Dimension(panelDettaglio.getWidth(),111));
			panelDettaglioRiga.setMaximumSize(new Dimension(panelDettaglio.getWidth(),111));
			panelDettaglioRiga.setPreferredSize(new Dimension(panelDettaglio.getWidth(),111));
			panelDettaglioRiga.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.WEST;
			c.insets = new Insets(0,2,0,0);
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
			//c.gridx = 5;
			//c.gridy = 0;
			//c.weightx = 0.1;
			//panelDettaglioRiga.add(lblDescrizioneArticolo, c);
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
			c.insets = new Insets(2,2,0,0);
			c.fill = GridBagConstraints.HORIZONTAL;
			panelDettaglioRiga.add(getTxtLottoArticolo(), c);
			
			c.gridx = 5;
			c.gridy = 2;
			c.gridwidth = 3;
			c.weightx = 1;
			c.insets = new Insets(2,2,0,0);
			panelDettaglioRiga.add(lblListinoArticolo, c);

			JPanel panelBottoni = new JPanel();
			panelBottoni.setLayout(new BoxLayout(panelBottoni,BoxLayout.LINE_AXIS));
			panelBottoni.add(getButtonInserisciRiga());
			panelBottoni.add(Box.createRigidArea(new Dimension(5,0)));
			panelBottoni.add(getButtonCancellaArticolo());
			panelBottoni.add(Box.createHorizontalGlue());
			panelBottoni.add(Box.createRigidArea(new Dimension(20,20)));
			panelBottoni.add(lblTotaleDDT);
			panelBottoni.add(getTxtTotaleDDT());
			panelBottoni.add(Box.createHorizontalGlue());
			panelBottoni.add(getButtonOk());
			panelBottoni.add(Box.createRigidArea(new Dimension(5,0)));
			panelBottoni.add(getButtonAnnulla());
			c.gridwidth = 11;
			c.gridx = 0;
			c.gridy = 3;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			panelDettaglioRiga.add(panelBottoni,c);
		}
		return panelDettaglioRiga;
	}

	private JTextField getTxtTotaleDDT() {
		if (txtTotaleDDT == null) {
			txtTotaleDDT = new JTextField();
			txtTotaleDDT.setEnabled(false);
			txtTotaleDDT.setColumns(6);
			txtTotaleDDT.setMinimumSize(new Dimension(60,21));
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
			
			TableColumn colModel = table.getColumnModel().getColumn(0); //CODICE
			colModel.setMaxWidth(60);
			colModel = table.getColumnModel().getColumn(2); //QTA
			colModel.setMaxWidth(50);
			colModel = table.getColumnModel().getColumn(3); //PEZZI RIGA
			colModel.setMaxWidth(30);
			colModel = table.getColumnModel().getColumn(4); //PEZZI DA EVADERE
			colModel.setMaxWidth(30);
			colModel = table.getColumnModel().getColumn(5); //PREZZI
			colModel.setMaxWidth(50);
			colModel = table.getColumnModel().getColumn(6); //SCONTO
			colModel.setMaxWidth(35);
			colModel = table.getColumnModel().getColumn(7); //IVA
			colModel.setMaxWidth(25);
			colModel = table.getColumnModel().getColumn(8); //UM
			colModel.setMaxWidth(25);
			colModel = table.getColumnModel().getColumn(COL_LOTTO); //LOTTO
			colModel.setMaxWidth(70);
			
			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent arg0) {
					if (arg0.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(arg0)) {
						editTable();
					}
				
					if (SwingUtilities.isRightMouseButton(arg0)) {
						//Check if there is an order associated to the row.
						//If there is ask to remove it
						int selectedRow = table.getSelectedRow();
						if (selectedRow != -1) {
							DettagliTableModel model = (DettagliTableModel) table.getModel();							
						}
					}
				}
			});
			
			table.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent arg0) {
					if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
						editTable();
					}
				}
			});
			
			table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					Component cell = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
					
					if (!isSelected) {
						cell.setBackground(Color.white);
						cell.setForeground(Color.black);
					}
									
					
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
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (clienteSelezionato != null) {
						setModalitaInserimentoArticolo(true);
					} else
						JOptionPane.showMessageDialog(contentPane, "Selezionare un cliente","Attenzione",JOptionPane.WARNING_MESSAGE);
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
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int selectedRow = table.getSelectedRow();
					((DettagliTableModel)table.getModel()).removeRow(selectedRow);
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
			NotaAccredito ddt = new NotaAccredito();
			ddt.setDettagliAccredito(getDettagliDDT());
			//BigDecimal totale = DDT.calcolaTotale(getDettagliDDT());
			BigDecimal totale = ddt.calcolaTotale();
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
			if(getCmbModalita().getSelectedIndex() == 0)
			{
				setModalitaAggiornamentoArticolo(false);
				setModalitaInserimentoArticolo(false);
			}
			else if(getCmbModalita().getSelectedIndex() == 1)
			{
				setModalitaAggiornamentoArticolo(true);
				setModalitaInserimentoArticolo(true);
				txtCodiceArticolo.requestFocus();
			}
			else if(getCmbModalita().getSelectedIndex() == 2)
			{
				setModalitaAggiornamentoArticolo(true);
				setModalitaInserimentoArticolo(true);
				txtDescrizioneArticolo.requestFocus();
			}
			pulisciCampiArticolo();
			aggiornaTotaleDettaglio();
		} else {
			JOptionPane.showMessageDialog(contentPane, "I dati inseriti non sono corretti","Errore",JOptionPane.ERROR_MESSAGE);
		}
	}

	@SuppressWarnings("unchecked")
	private JTextField getTxtCodiceArticolo() {
		if (txtCodiceArticolo == null) {
			txtCodiceArticolo = new JTextField();
			txtCodiceArticolo.setColumns(6);
			txtCodiceArticolo.setMinimumSize(new Dimension(60,21));
			txtCodiceArticolo.setEnabled(false);
			txtCodiceArticolo.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
					java.util.Collections.EMPTY_SET);
			txtCodiceArticolo.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyChar() == KeyEvent.VK_TAB 
							|| e.getKeyChar() == KeyEvent.VK_ENTER) {
						try {
							if (!txtCodiceArticolo.getText().equalsIgnoreCase("")) {
								PrezzoConSconto prezzoArticolo = null;
								if(getCmbModalita().getSelectedIndex() == 0)
									prezzoArticolo = new DbConnector().getPrezzoArticolo(txtCodiceArticolo.getText(), clienteSelezionato.getId());
								else 
									prezzoArticolo = new DbConnector().getPrezzoArticoloFromBarcode(txtCodiceArticolo.getText(), clienteSelezionato.getId());
								
								if (prezzoArticolo != null) {
									setPrezzoArticolo(prezzoArticolo);
									
									if(getCmbModalita().getSelectedIndex() == 0)
									{
										txtQtaArticolo.requestFocusInWindow();
									}
									else 
									{
										if (!prezzoArticolo.getArticolo().isCompleteBarCode()) { //l'articolo non ha una qta predefinita
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
			txtQtaArticolo.setMinimumSize(new Dimension(60,22));
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
			txtLottoArticolo.setMinimumSize(new Dimension(180,22));
			txtLottoArticolo.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyChar() == KeyEvent.VK_ENTER) {
						buttonOk.doClick();
					}
				}
			});
			//IMPOSTO IL VALORE PREDEFINITO
			//DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
			//txtLottoArticolo.setText(dateFormat.format(Calendar.getInstance().getTime()));
			txtLottoArticolo.setText("");
		}
		return txtLottoArticolo;
	}
	
	public void setArticolo(Object codice) {
		applet.setContentPane(getContentPane());
		applet.validate();
		applet.getContentPane().requestFocusInWindow();
		//contentPane.getRootPane().setDefaultButton(buttonOk);
		
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
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					applet.setContentPane(listaArticoli.getContentPanel());
					applet.validate();
					listaArticoli.getTxtTrova().requestFocus();
					
						/*dialog.getDialog().setVisible(true);
						PrezzoConSconto prezzoArticolo = new DbConnector().getPrezzoArticolo((String) dialog.getSelectedElement(), clienteSelezionato.getId());
						if (prezzoArticolo != null) {
							setPrezzoArticolo(prezzoArticolo);
							txtQtaArticolo.requestFocus();
						} else {
							txtDescrizioneArticolo.requestFocus();
						}*/
				}
			});
			buttonTrovaArticolo.addKeyListener(new KeyAdapter() {
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
			txtDescrizioneArticolo.setMinimumSize(new Dimension(70,22));
		}
		return txtDescrizioneArticolo;
	}

	private JTextField getTxtUMArticolo() {
		if (txtUMArticolo == null) {
			txtUMArticolo = new JTextField();
			txtUMArticolo.setEnabled(false);
			txtUMArticolo.setColumns(2);
			txtUMArticolo.setMinimumSize(new Dimension(20,22));
		}
		return txtUMArticolo;
	}

	private JTextField getTxtPezzi() {
		if (txtPezzi == null) {
			txtPezzi = new JTextField();
			txtPezzi.setEnabled(false);
			txtPezzi.setColumns(7);
			txtPezzi.setMinimumSize(new Dimension(70,22));
		}
		return txtPezzi;
	}

	private JTextField getTxtPrezzoArticolo() {
		if (txtPrezzoArticolo == null) {
			txtPrezzoArticolo = new JTextField();
			txtPrezzoArticolo.setEnabled(false);
			txtPrezzoArticolo.setColumns(6);
			txtPrezzoArticolo.setMinimumSize(new Dimension(70,22));
			txtPrezzoArticolo.setInputVerifier(new NumberFormattedVerifier(2));
			txtPrezzoArticolo.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyChar() == KeyEvent.VK_ENTER) {
						//buttonOk.requestFocus();
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
			txtScontoArticolo.setMinimumSize(new Dimension(30,22));
			txtScontoArticolo.setInputVerifier(new NumberFormattedVerifier(0));
			txtScontoArticolo.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyChar() == KeyEvent.VK_ENTER) {
						//buttonOk.requestFocus();
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
			DefaultComboBoxModel cmb = (DefaultComboBoxModel)cmbIva.getModel();
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
			txtDataDDT.setMinimumSize(new Dimension(90,21));
		}
		return txtDataDDT;
	}

	public void barcodeRead(Barcode barcode) {
		if (barcode != null) {
			if (barcode.isNumeroLotto()) {
				//inserisco il lotto e inserisco l'articolo nella tabella
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
					number = number.setScale(3,BigDecimal.ROUND_HALF_UP);
					txtQtaArticolo.setText(formatter.format(number));
					txtPezzi.setText("1");
					
					setModalitaInserimentoArticolo(true);
					txtLottoArticolo.requestFocus();
				} catch (Exception e) {}
			}
		} else {
			//TODO: BIP SONORO
		}
	}

	public Cliente getClienteSelezionato() {
		return clienteSelezionato;
	}
}
