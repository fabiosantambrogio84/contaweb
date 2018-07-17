package applet.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.text.DateFormatter;

import applet.common.DateFormattedVerifier;
import applet.common.NumberFormattedVerifier;
import applet.db.DbConnector;
import vo.Articolo;
import vo.BollaAcquisto;
import vo.DettaglioBollaAcquisto;
import vo.Fornitore;

public class BollaAcquistoWindow {

    private static final int PANEL_WIDTH = 620;
    private static final String[] TABLE_HEADERS = { "Codice", "Descrizione", "Qta'", "Prezzo", "Sc.%", "Lotto", "Scadenza", "id" };
    private Integer currentIdArticolo;

    private JPanel panel = null;
    private JPanel panelIntestazione = null;
    private JPanel panelDettagli = null;
    private JPanel panelBottoni = null;

    private JButton bScegliFornitore = null;
    private JButton bScegliArticolo = null;
    private JButton bNuovoArticolo = null;
    private JButton bCancellaArticolo = null;
    private JButton bOk = null;
    private JButton bCancella = null;
    private JButton bNuovaBolla = null;
    private JButton bSalvaBolla = null;

    private JLabel lblDataDocumento = null;
    private JLabel lblColliDocumento = null;
    private JLabel lblDescrizioneFornitore = null;
    private JLabel lblDescrizioneArticolo = null;
    private JLabel lblQtaArticolo = null;
    private JLabel lblLottoArticolo = null;
    private JLabel lblCodiceArticolo = null;
    private JLabel lblNumeroDocumento = null;
    private JLabel lblPrezzoArticolo = null;
    private JLabel lblScontoArticolo = null;
    private JLabel lblScadenzaArticolo = null;
    private JLabel lblTotaleBolla = null;

    private JTextField txtDataDocumento = null;
    private JTextField txtColli = null;
    private JTextField txtQtaArticolo = null;
    private JTextField txtLottoArticolo = null;
    private JTextField txtNumeroDocumento = null;
    private JTextField txtCodiceArticolo = null;
    private JTextField txtPrezzoArticolo = null;
    private JTextField txtScontoArticolo = null;
    private JTextField txtScadenzaArticolo = null;
    private JTextField txtTotaleBolla = null;
    private JComboBox cmbModalita = null;

    private JTable table = null;
    private JScrollPane scrollPane;

    private Integer updateRow = null;

    private Articolo selectedArticolo = null;
    private Fornitore selectedFornitore = null;
    private BollaAcquisto bollaAcquisto = null;

    public BollaAcquistoWindow(long id) {
        this();
        bollaAcquisto = new DbConnector().getBollaAcquisto(id);
        aggiornaFinestra();
    }

    @SuppressWarnings("unchecked")
    private void aggiornaFinestra() {
        getPanel();
        lblDescrizioneFornitore.setText(bollaAcquisto.getFornitore().getDescrizione());
        getBScegliFornitore().setEnabled(false);
        selectedFornitore = bollaAcquisto.getFornitore();
        getTxtNumeroDocumento().setText(bollaAcquisto.getNumeroBolla());
        getTxtNumeroDocumento().setEnabled(false);
        getTxtColli().setText(bollaAcquisto.getColli().toString());
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
        getTxtDataDocumento().setText(df.format(bollaAcquisto.getData()));
        DecimalFormat nf = (DecimalFormat) DecimalFormat.getInstance(Locale.ITALIAN);
        // AGGIORNO DETTAGLIO
        Iterator itr = bollaAcquisto.getInventario().iterator();
        while (itr.hasNext()) {
            DettaglioBollaAcquisto dba = (DettaglioBollaAcquisto) itr.next();
            Vector dati = new Vector();
            dati.add(dba.getArticolo().getCodiceArticolo());
            dati.add(dba.getArticolo().getDescrizione());
            nf.setMinimumFractionDigits(3);
            dati.add(nf.format(dba.getQta()));
            nf.setMinimumFractionDigits(2);
            dati.add(nf.format(dba.getPrezzo()));
            if (dba.getSconto() != null)
                dati.add(nf.format(dba.getSconto()));
            else
                dati.add(null);
            dati.add(dba.getLotto());
            if (dba.getDataScadenza() != null) {
                dati.add(df.format(dba.getDataScadenza()));
            } else
                dati.add(null);
            dati.add(dba.getIdArticolo());
            dati.add(dba.getId());
            ((InventarioTableModel) getTable().getModel()).addRow(dati);
        }
        calcolaTotaleBolla();
    }

    public BollaAcquistoWindow() {

    }

    public void setModalitaNuovaBollaAcquisto() {
        bollaAcquisto = null;
        selectedFornitore = null;
        lblDescrizioneFornitore.setText("");
        disabilitaMascheraDettaglio();
        getTable().setModel(new InventarioTableModel(TABLE_HEADERS));
        getTxtNumeroDocumento().setText("");
        getTxtColli().setText("0");
        getBSalvaBolla().setEnabled(true);
        getBScegliFornitore().setEnabled(true);
        getTxtNumeroDocumento().setEnabled(true);
        getBScegliFornitore().requestFocus();
    }

    public JTable getTable() {
        if (table == null) {
            table = new JTable() {
                private static final long serialVersionUID = 1L;

                @Override
                public boolean isCellEditable(int rowIndex, int vColIndex) {
                    return false;
                }
            };
            table.getTableHeader().setReorderingAllowed(false);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setRowSelectionAllowed(true);

            table.setModel(new InventarioTableModel(TABLE_HEADERS));

            TableColumn colModel = table.getColumnModel().getColumn(0);
            colModel.setMaxWidth(60);
            colModel = table.getColumnModel().getColumn(2); // QTA
            colModel.setMaxWidth(50);
            colModel = table.getColumnModel().getColumn(3); // PREZZO
            colModel.setMaxWidth(55);
            colModel = table.getColumnModel().getColumn(4); // SCONTO
            colModel.setMaxWidth(35);
            colModel = table.getColumnModel().getColumn(5); // LOTTO
            colModel.setMaxWidth(100);
            colModel = table.getColumnModel().getColumn(6); // SCADENZA
            colModel.setMaxWidth(60);
            colModel = table.getColumnModel().getColumn(7); // ID
            colModel.setMaxWidth(0);

            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent arg0) {
                    if (arg0.getClickCount() == 2) {
                        editTable();
                    }
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
        }
        return table;
    }

    private void editTable() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            // AGGIORNO I CAMPI E IMPOSTO MODALITA AGGIORNAMENTO RIGA
            getTxtCodiceArticolo().setText((String) table.getModel().getValueAt(selectedRow, 0));
            selectedArticolo = new DbConnector().getArticolo(getTxtCodiceArticolo().getText());
            lblDescrizioneArticolo.setText((String) table.getModel().getValueAt(selectedRow, 1));
            getTxtQtaArticolo().setText((String) table.getModel().getValueAt(selectedRow, 2));
            getTxtPrezzoArticolo().setText((String) table.getModel().getValueAt(selectedRow, 3));
            getTxtScontoArticolo().setText((String) table.getModel().getValueAt(selectedRow, 4));
            getTxtLottoArticolo().setText((String) table.getModel().getValueAt(selectedRow, 5));
            getTxtScadenzaArticolo().setText((String) table.getModel().getValueAt(selectedRow, 6));
            currentIdArticolo = (Integer) table.getModel().getValueAt(selectedRow, 7);
            updateRow = selectedRow;
            setModalitaAggiornamentoArticolo();
        }
    }

    private void cancellaRigaSelezionata() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            ((InventarioTableModel) getTable().getModel()).removeRow(selectedRow);
        }
        getTable().repaint();
        disabilitaMascheraDettaglio();
    }

    private JComboBox getCmbModalita() {
        if (cmbModalita == null) {
            String[] tipo = { "cod. prodotto", "cod. barre" };
            cmbModalita = new JComboBox(tipo);
            // 20170121 - Modifica per imporre "cod. prodotto" come default.
            // | prima era setSelectedIndex(1) |
            cmbModalita.setSelectedIndex(0);
        }
        return cmbModalita;
    }

    public void setModalitaAggiornamentoArticolo() {
        getBNuovoArticolo().setEnabled(false);
        getBCancellaArticolo().setEnabled(true);
        getBCancella().setEnabled(true);
        getBOk().setEnabled(true);
        getTxtQtaArticolo().setEnabled(true);
        getTxtLottoArticolo().setEnabled(true);
        getTxtPrezzoArticolo().setEnabled(true);
        getTxtScontoArticolo().setEnabled(true);
        getTxtScadenzaArticolo().setEnabled(true);
        getTxtQtaArticolo().requestFocus();
        getPanel().getRootPane().setDefaultButton(getBOk());
    }

    public JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getTable());
        }
        return scrollPane;
    }

    public JTextField getTxtDataDocumento() {
        if (txtDataDocumento == null) {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
            DateFormatter dateFormatter = new DateFormatter(dateFormat);
            txtDataDocumento = new JFormattedTextField(dateFormatter);
            Calendar calendar = Calendar.getInstance();
            txtDataDocumento.setText(dateFormat.format(calendar.getTime()));
            txtDataDocumento.setMinimumSize(new Dimension(75, 23));
            txtDataDocumento.setInputVerifier(new DateFormattedVerifier("dd/MM/yy"));
        }
        return txtDataDocumento;
    }

    public JButton getBScegliFornitore() {
        if (bScegliFornitore == null) {
            bScegliFornitore = new JButton("Scegli Fornitore");
            bScegliFornitore.setMnemonic('F');
            bScegliFornitore.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ChooseDialog dialog;
                    String[] colH = { "Descrizione" };
                    try {
                        Collection fornitori = new DbConnector().getListaFornitori();
                        dialog = new ChooseDialog("Scegli Fornitore", colH, 1, fornitori, null, null);
                        dialog.getDialog().setVisible(true);
                        Integer idFornitore = (Integer) dialog.getSelectedElement();
                        selectedFornitore = new DbConnector().getFornitore(idFornitore);
                        lblDescrizioneFornitore.setText(selectedFornitore.getDescrizione());
                        disabilitaMascheraDettaglio();
                        ((InventarioTableModel) getTable().getModel()).removeAllElements();
                        getTxtNumeroDocumento().requestFocus();
                    } catch (Exception e1) {
                    }

                }
            });

        }
        return bScegliFornitore;
    }

    public JPanel getPanelBottoni() {
        if (panelBottoni == null) {
            panelBottoni = new JPanel();
            panelBottoni.setMinimumSize(new Dimension(PANEL_WIDTH, 30));
            panelBottoni.setPreferredSize(new Dimension(PANEL_WIDTH, 30));
            panelBottoni.setLayout(new BoxLayout(panelBottoni, BoxLayout.X_AXIS));
            panelBottoni.add(getBNuovaBolla());
            panelBottoni.add(Box.createHorizontalGlue());
            panelBottoni.add(getBSalvaBolla());
        }
        return panelBottoni;
    }

    public JPanel getPanelDettagli() {
        if (panelDettagli == null) {
            panelDettagli = new JPanel();
            panelDettagli.setBorder(BorderFactory.createTitledBorder(null, "Dettagli", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), new Color(0, 70, 213)));

            panelDettagli.setLayout(new GridBagLayout());

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(2, 5, 2, 5);
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1;
            c.weighty = 1;
            c.gridwidth = 7;
            panelDettagli.add(getScrollPane(), c);

            c.weightx = 0;
            c.weighty = 0;
            c.gridy = 1;
            c.anchor = GridBagConstraints.WEST;
            c.gridwidth = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0;
            c.gridx = 0;
            panelDettagli.add(getCmbModalita(), c);
            c.gridx = 1;
            lblCodiceArticolo = new JLabel("Cod.");
            panelDettagli.add(lblCodiceArticolo, c);
            c.gridx = 2;
            panelDettagli.add(getTxtCodiceArticolo(), c);
            c.gridx = 3;
            panelDettagli.add(getBScegliArticolo(), c);
            c.gridx = 4;
            c.gridwidth = 4;
            lblDescrizioneArticolo = new JLabel("");
            panelDettagli.add(lblDescrizioneArticolo, c);

            c.gridx = 0;
            c.gridy = 2;
            c.gridwidth = 1;
            lblQtaArticolo = new JLabel("Q.ta':");
            panelDettagli.add(lblQtaArticolo, c);
            c.gridx = 1;
            panelDettagli.add(getTxtQtaArticolo(), c);
            c.gridx = 2;
            lblLottoArticolo = new JLabel("Lotto:");
            panelDettagli.add(lblLottoArticolo, c);
            c.gridx = 3;
            c.gridwidth = 4;
            c.fill = GridBagConstraints.NONE;
            panelDettagli.add(getTxtLottoArticolo(), c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridy = 3;
            c.gridx = 0;
            c.gridwidth = 1;
            lblPrezzoArticolo = new JLabel("Prezzo:");
            panelDettagli.add(lblPrezzoArticolo, c);
            c.gridx = 1;
            panelDettagli.add(getTxtPrezzoArticolo(), c);
            c.gridx = 2;
            lblScontoArticolo = new JLabel("Sconto:");
            panelDettagli.add(lblScontoArticolo, c);
            c.gridx = 3;
            panelDettagli.add(getTxtScontoArticolo(), c);
            c.gridx = 4;
            lblScadenzaArticolo = new JLabel("Scadenza:");
            panelDettagli.add(lblScadenzaArticolo, c);
            c.gridx = 5;
            c.fill = GridBagConstraints.NONE;
            panelDettagli.add(getTxtScadenzaArticolo(), c);
            c.fill = GridBagConstraints.HORIZONTAL;

            c.gridx = 0;
            c.gridy = 4;
            c.gridwidth = 2;
            panelDettagli.add(getBNuovoArticolo(), c);
            c.gridx = 2;
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.EAST;
            panelDettagli.add(getBCancellaArticolo(), c);
            c.gridx = 4;
            c.weightx = 1;
            panelDettagli.add(getBOk(), c);
            c.gridx = 6;
            c.weightx = 0;
            c.gridwidth = 1;
            panelDettagli.add(getBCancella(), c);

            disabilitaMascheraDettaglio();
        }
        return panelDettagli;
    }

    public JPanel getPanelIntestazione() {
        if (panelIntestazione == null) {
            panelIntestazione = new JPanel();
            panelIntestazione.setBorder(BorderFactory.createTitledBorder(null, "Intestazione", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), new Color(0, 70, 213)));
            panelIntestazione.setMinimumSize(new Dimension(PANEL_WIDTH, 120));
            panelIntestazione.setPreferredSize(new Dimension(PANEL_WIDTH, 120));
            panelIntestazione.setLayout(new GridBagLayout());

            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(3, 5, 3, 5);
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.fill = GridBagConstraints.HORIZONTAL;
            panelIntestazione.add(getBScegliFornitore(), c);

            c.gridx = 2;
            c.weightx = 1;
            lblDescrizioneFornitore = new JLabel("");
            panelIntestazione.add(lblDescrizioneFornitore, c);

            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 1;
            c.weightx = 0;
            c.anchor = GridBagConstraints.WEST;
            lblNumeroDocumento = new JLabel("Numero:");
            panelIntestazione.add(lblNumeroDocumento, c);

            c.gridx = 1;
            panelIntestazione.add(getTxtNumeroDocumento(), c);

            c.gridx = 0;
            c.gridy = 2;
            lblDataDocumento = new JLabel("Data:");
            panelIntestazione.add(lblDataDocumento, c);

            c.gridx = 1;
            c.anchor = GridBagConstraints.WEST;
            panelIntestazione.add(getTxtDataDocumento(), c);

            c.gridx = 2;
            c.gridy = 1;
            c.anchor = GridBagConstraints.EAST;
            c.weightx = 0.9;
            c.fill = GridBagConstraints.NONE;
            lblColliDocumento = new JLabel("Colli:");
            panelIntestazione.add(lblColliDocumento, c);

            c.gridy = 2;
            lblTotaleBolla = new JLabel("Totale bolla:");
            panelIntestazione.add(lblTotaleBolla, c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridy = 1;
            c.gridx = 3;
            c.weightx = 0.1;
            panelIntestazione.add(getTxtColli(), c);

            c.gridy = 2;
            panelIntestazione.add(getTxtTotaleBolla(), c);
        }
        return panelIntestazione;
    }

    public JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 10);
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(getPanelIntestazione(), c);
            c.gridy = 1;
            c.weighty = 1;
            c.fill = GridBagConstraints.BOTH;
            panel.add(getPanelDettagli(), c);
            c.weighty = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridy = 2;
            panel.add(getPanelBottoni(), c);
        }
        return panel;
    }

    public JTextField getTxtColli() {
        if (txtColli == null) {
            txtColli = new JTextField();
            txtColli.setMinimumSize(new Dimension(70, 23));
            txtColli.setText("0");
            txtColli.setInputVerifier(new NumberFormattedVerifier(0));
            txtColli.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent evt) {
                    ((JTextField) evt.getSource()).selectAll();
                }
            });
        }
        return txtColli;
    }

    public JButton getBScegliArticolo() {
        if (bScegliArticolo == null) {
            bScegliArticolo = new JButton("...");
            bScegliArticolo.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ChooseDialog dialog;
                    String[] colH = { "Codice", "Descrizione" };
                    try {
                        Collection articoli = new DbConnector().getListaArticoli(selectedFornitore);
                        dialog = new ChooseDialog("Scegli articolo", colH, 2, articoli, null, null);
                        dialog.getDialog().setVisible(true);
                        String idArticolo = (String) dialog.getSelectedElement();
                        if (idArticolo != null) {
                            selectedArticolo = new DbConnector().getArticolo(idArticolo);
                            prevalorizzaRiga();
                        }
                    } catch (Exception e1) {
                    }

                }
            });
        }
        return bScegliArticolo;
    }

    private void prevalorizzaRiga() {
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.ITALIAN);
        lblDescrizioneArticolo.setText(selectedArticolo.getDescrizione());
        getTxtCodiceArticolo().setText(selectedArticolo.getCodiceArticolo().toString());
        currentIdArticolo = selectedArticolo.getId();
        df.setMinimumFractionDigits(2);
        if (selectedArticolo.getPrezzoAcquisto() != null)
            getTxtPrezzoArticolo().setText(df.format(selectedArticolo.getPrezzoAcquisto()));
        else
            getTxtPrezzoArticolo().setText("");

        getTxtQtaArticolo().requestFocus();
    }

    public JTextField getTxtLottoArticolo() {
        if (txtLottoArticolo == null) {
            txtLottoArticolo = new JTextField();
            txtLottoArticolo.setMinimumSize(new Dimension(200, 23));

        }
        return txtLottoArticolo;
    }

    public JTextField getTxtQtaArticolo() {
        if (txtQtaArticolo == null) {
            txtQtaArticolo = new JTextField();
            txtQtaArticolo.setMinimumSize(new Dimension(70, 23));
            txtQtaArticolo.setInputVerifier(new NumberFormattedVerifier(3));
        }
        return txtQtaArticolo;
    }

    public JButton getBCancella() {
        if (bCancella == null) {
            bCancella = new JButton("Cancella");
            bCancella.setMnemonic('l');
            bCancella.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    disabilitaMascheraDettaglio();

                    getBNuovoArticolo().requestFocus();
                    getPanel().getRootPane().setDefaultButton(getBNuovoArticolo());
                }
            });
        }
        return bCancella;
    }

    public JButton getBCancellaArticolo() {
        if (bCancellaArticolo == null) {
            bCancellaArticolo = new JButton("Cancella Articolo");
            bCancellaArticolo.setMnemonic('C');
            bCancellaArticolo.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    cancellaRigaSelezionata();
                    calcolaTotaleBolla();
                    getBNuovoArticolo().requestFocus();
                    getPanel().getRootPane().setDefaultButton(getBNuovoArticolo());
                }
            });
        }
        return bCancellaArticolo;
    }

    public JButton getBNuovoArticolo() {
        if (bNuovoArticolo == null) {
            bNuovoArticolo = new JButton("Nuovo Articolo");
            bNuovoArticolo.setMnemonic('N');
            bNuovoArticolo.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (selectedFornitore == null) {
                        JOptionPane.showMessageDialog(getPanelDettagli(), "Selezionare un fornitore", "Errore",
                                JOptionPane.ERROR_MESSAGE);
                    } else
                        setModalitaInserimentoArticolo();
                }
            });
        }
        return bNuovoArticolo;
    }

    public JButton getBOk() {
        if (bOk == null) {
            bOk = new JButton("Ok");
            bOk.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (controllaRigaCorretta()) {
                        inserisciRigaCorrente();
                        calcolaTotaleBolla();
                        disabilitaMascheraDettaglio();
                        getBNuovoArticolo().requestFocus();
                        // getPanel().getRootPane().setDefaultButton(getBNuovoArticolo());
                        getBNuovoArticolo().doClick();
                    }
                }
            });
        }
        return bOk;
    }

    private void calcolaTotaleBolla() {
        InventarioTableModel model = (InventarioTableModel) getTable().getModel();
        BigDecimal totale = new BigDecimal(0);
        NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
        for (int i = 0; i < model.getRowCount(); ++i) {
            try {
                BigDecimal qta = new BigDecimal(nf.parse((String) model.getValueAt(i, 2)).toString());
                BigDecimal prezzo = new BigDecimal(nf.parse((String) model.getValueAt(i, 3)).toString());
                BigDecimal totaleRiga = qta.multiply(prezzo).setScale(2, BigDecimal.ROUND_HALF_UP);
                totale = totale.add(totaleRiga);
            } catch (ParseException e) {
            }
        }
        getTxtTotaleBolla().setText(nf.format(totale));
    }

    @SuppressWarnings("unchecked")
    private void inserisciRigaCorrente() {
        Vector dati = new Vector();
        DecimalFormat nf = (DecimalFormat) DecimalFormat.getInstance(Locale.ITALIAN);
        dati.add(getTxtCodiceArticolo().getText());
        dati.add(lblDescrizioneArticolo.getText());
        nf.setMinimumFractionDigits(3);
        nf.setMaximumFractionDigits(3);
        try {
            dati.add(nf.format(nf.parse(getTxtQtaArticolo().getText())));
            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);
            dati.add(nf.format(nf.parse(getTxtPrezzoArticolo().getText())));
            if (getTxtScontoArticolo().getText().equalsIgnoreCase(""))
                dati.add(null);
            else
                dati.add(nf.format(nf.parse(getTxtScontoArticolo().getText())));
        } catch (ParseException e) {
        }
        dati.add(getTxtLottoArticolo().getText());
        if (getTxtScadenzaArticolo().getText().equalsIgnoreCase(""))
            dati.add(null);
        else
            dati.add(getTxtScadenzaArticolo().getText());
        dati.add(currentIdArticolo);

        InventarioTableModel model = (InventarioTableModel) table.getModel();

        if (updateRow != null) {
            dati.add(model.getId(updateRow));
            model.insertRow(updateRow, dati);
        } else {
            dati.add(null);
            model.addRow(dati);
            table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
            table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 1, true));
        }
    }

    private boolean controllaRigaCorretta() {
        if (selectedArticolo == null) {
            JOptionPane.showMessageDialog(getPanelDettagli(), "Selezionare un articolo", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.ITALIAN);

        try {
            df.parse(getTxtQtaArticolo().getText());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(getPanelDettagli(), "Impostare la qta dell'articolo", "Errore", JOptionPane.ERROR_MESSAGE);
            getTxtQtaArticolo().requestFocus();
            return false;
        }

        try {
            df.parse(getTxtPrezzoArticolo().getText());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(getPanelDettagli(), "Impostare il prezzo dell'articolo", "Errore",
                    JOptionPane.ERROR_MESSAGE);
            getTxtPrezzoArticolo().requestFocus();
            return false;
        }

        if (getTxtScontoArticolo().getText().length() > 0) {
            try {
                df.parse(getTxtScontoArticolo().getText());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(getPanelDettagli(), "Impostare lo sconto dell'articolo", "Errore",
                        JOptionPane.ERROR_MESSAGE);
                getTxtScontoArticolo().requestFocus();
                return false;
            }
        }

        if (getTxtLottoArticolo().getText().length() == 0) {
            JOptionPane.showMessageDialog(getPanelDettagli(), "Lotto non corretto", "Errore", JOptionPane.ERROR_MESSAGE);
            getTxtLottoArticolo().requestFocus();
            return false;
        }

        if (getTxtScadenzaArticolo().getText().length() > 0) {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);

            try {
                dateFormat.parse(getTxtScadenzaArticolo().getText());
            } catch (ParseException e) {
                getTxtScadenzaArticolo().requestFocus();
                JOptionPane.showMessageDialog(getPanelDettagli(), "Data scadenza non corretta", "Errore", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    public JTextField getTxtNumeroDocumento() {
        if (txtNumeroDocumento == null) {
            txtNumeroDocumento = new JTextField();
            txtNumeroDocumento.setMinimumSize(new Dimension(90, 23));
        }
        return txtNumeroDocumento;
    }

    public void setModalitaInserimentoArticolo() {
        updateRow = null;
        getTxtCodiceArticolo().setEnabled(true);
        getTxtPrezzoArticolo().setEnabled(true);
        getTxtScontoArticolo().setEnabled(true);
        getBCancella().setEnabled(true);
        getBOk().setEnabled(true);
        getBScegliArticolo().setEnabled(true);
        getBCancellaArticolo().setEnabled(false);
        getTxtQtaArticolo().setEnabled(true);
        getTxtLottoArticolo().setEnabled(true);
        getTxtScadenzaArticolo().setEnabled(true);

        getTxtQtaArticolo().setInputVerifier(null);
        getTxtPrezzoArticolo().setInputVerifier(null);

        getTxtCodiceArticolo().requestFocus();

        getTxtQtaArticolo().setInputVerifier(new NumberFormattedVerifier(3));
        getTxtPrezzoArticolo().setInputVerifier(new NumberFormattedVerifier(2));

        getPanel().getRootPane().setDefaultButton(getBOk());
    }

    public void disabilitaMascheraDettaglio() {
        getBCancella().setEnabled(false);
        getBOk().setEnabled(false);
        getBCancellaArticolo().setEnabled(false);
        getBNuovoArticolo().setEnabled(true);
        getBScegliArticolo().setEnabled(false);
        selectedArticolo = null;
        currentIdArticolo = null;
        lblDescrizioneArticolo.setText("");

        getTxtQtaArticolo().setText("");
        getTxtQtaArticolo().setEnabled(false);
        getTxtLottoArticolo().setText("");
        getTxtLottoArticolo().setEnabled(false);
        getTxtPrezzoArticolo().setEnabled(false);
        getTxtPrezzoArticolo().setText("");
        getTxtScontoArticolo().setEnabled(false);
        getTxtScontoArticolo().setText("");
        getTxtCodiceArticolo().setEnabled(false);
        getTxtCodiceArticolo().setText("");
        getTxtScadenzaArticolo().setEnabled(false);
        getTxtScadenzaArticolo().setText("");
    }

    @SuppressWarnings("unchecked")
    public JTextField getTxtCodiceArticolo() {
        if (txtCodiceArticolo == null) {
            txtCodiceArticolo = new JTextField();
            txtCodiceArticolo.setMinimumSize(new Dimension(70, 23));
            txtCodiceArticolo.setColumns(6);
            txtCodiceArticolo.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, java.util.Collections.EMPTY_SET);
            txtCodiceArticolo.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_TAB || e.getKeyChar() == KeyEvent.VK_ENTER) {
                        try {
                            if (!txtCodiceArticolo.getText().equalsIgnoreCase("")) {

                                if (getCmbModalita().getSelectedIndex() == 0)
                                    selectedArticolo = new DbConnector().getArticolo(txtCodiceArticolo.getText(), selectedFornitore);
                                else
                                    selectedArticolo = new DbConnector()
                                            .getArticoloByBarCode(txtCodiceArticolo.getText().substring(0, 7), selectedFornitore);

                                if (selectedArticolo != null) {

                                    if (getCmbModalita().getSelectedIndex() == 0) {
                                        txtQtaArticolo.requestFocusInWindow();
                                        prevalorizzaRiga();
                                    } else {
                                        // getTxtQtaArticolo().requestFocusInWindow();

                                        DecimalFormat dfUS = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
                                        DecimalFormat dfIT = (DecimalFormat) DecimalFormat.getInstance(Locale.ITALIAN);

                                        BigDecimal qta = null;
                                        if (!selectedArticolo.isCompleteBarCode()) { // l'articolo non ha una qta predefinita
                                            // qta = BarcodeSplitter.readQta(txtCodiceArticolo.getText());
                                            String barcodeStr = txtCodiceArticolo.getText();
                                            double value = (long) dfUS
                                                    .parse(barcodeStr.substring(barcodeStr.length() - 6, barcodeStr.length()));
                                            value = value / 10000;
                                            qta = new BigDecimal(value).setScale(3, BigDecimal.ROUND_HALF_UP);
                                            txtQtaArticolo.setText(dfIT.format(qta.doubleValue()));
                                        } else {
                                            qta = selectedArticolo.getQtaPredefinita();
                                            txtQtaArticolo.setText(dfUS.format(qta.doubleValue()));
                                        }

                                        // txtPezzi.setText("1");
                                        prevalorizzaRiga();
                                        e.consume();
                                        txtLottoArticolo.requestFocusInWindow();
                                    }

                                } else {
                                    lblDescrizioneArticolo.setText("");
                                    getTxtPrezzoArticolo().setText("");
                                    getTxtScontoArticolo().setText("");
                                    getTxtLottoArticolo().setText("");
                                    getTxtScadenzaArticolo().setText("");
                                    getTxtQtaArticolo().setText("");
                                    getBScegliArticolo().requestFocus();
                                }
                            } else {
                                getBScegliArticolo().requestFocus();
                            }
                        } catch (Exception e1) {
                            getBScegliArticolo().requestFocus();
                        }
                    }
                }
            });
        }
        return txtCodiceArticolo;
    }

    public JTextField getTxtPrezzoArticolo() {
        if (txtPrezzoArticolo == null) {
            txtPrezzoArticolo = new JTextField();
            txtPrezzoArticolo.setMinimumSize(new Dimension(70, 23));
            txtPrezzoArticolo.setInputVerifier(new NumberFormattedVerifier(2));
        }
        return txtPrezzoArticolo;
    }

    public JTextField getTxtScontoArticolo() {
        if (txtScontoArticolo == null) {
            txtScontoArticolo = new JTextField();
            txtScontoArticolo.setMinimumSize(new Dimension(70, 23));
        }
        return txtScontoArticolo;
    }

    public JTextField getTxtScadenzaArticolo() {
        if (txtScadenzaArticolo == null) {
            txtScadenzaArticolo = new JTextField();
            txtScadenzaArticolo.setMinimumSize(new Dimension(80, 23));
        }
        return txtScadenzaArticolo;
    }

    public JButton getBNuovaBolla() {
        if (bNuovaBolla == null) {
            bNuovaBolla = new JButton("Nuova bolla acquisto");
            bNuovaBolla.setMnemonic('u');
            bNuovaBolla.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (table.getRowCount() == 0 || JOptionPane.showConfirmDialog(bNuovaBolla,
                            "Attenzione! Le eventuali modifiche correnti verranno annullate. " + "Si desidera continuare?",
                            "Attenzione!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                        setModalitaNuovaBollaAcquisto();
                }
            });
        }
        return bNuovaBolla;
    }

    public JButton getBSalvaBolla() {
        if (bSalvaBolla == null) {
            bSalvaBolla = new JButton("Salva");
            bSalvaBolla.setMnemonic('s');
            bSalvaBolla.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        BollaAcquisto ba = getBollaAcquisto();
                        new DbConnector().saveBollaAcquisto(ba);
                        JOptionPane.showMessageDialog(getPanelBottoni(), "La bolla e' stata salvata correttamente", "Informazione",
                                JOptionPane.INFORMATION_MESSAGE);
                        getBSalvaBolla().setEnabled(false);

                        bollaAcquisto = null;
                        disabilitaMascheraDettaglio();
                        getTable().setModel(new InventarioTableModel(TABLE_HEADERS));
                        getTxtNumeroDocumento().setText("");
                        getTxtColli().setText("0");
                        getBSalvaBolla().setEnabled(true);
                        getBScegliFornitore().setEnabled(true);
                        getTxtNumeroDocumento().setEnabled(true);
                        getTxtNumeroDocumento().requestFocus();

                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(getPanelBottoni(), e1.getMessage(), "Attenzione", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
        return bSalvaBolla;
    }

    @SuppressWarnings("unchecked")
    private BollaAcquisto getBollaAcquisto() throws Exception {
        BollaAcquisto ba = null;
        if (bollaAcquisto != null)
            ba = bollaAcquisto;
        else
            ba = new BollaAcquisto();

        try {
            if (selectedFornitore == null)
                throw new Exception("Selezionare un fornitore");
            ba.setIdFornitore(selectedFornitore.getId());
            ba.setFornitore(selectedFornitore);
            if (getTxtNumeroDocumento().getText().length() == 0)
                throw new Exception("Inserire il numero della bolla");

            ba.setNumeroBolla(getTxtNumeroDocumento().getText());
            ba.setColli(Integer.parseInt(getTxtColli().getText()));

            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
            try {
                ba.setData(df.parse(getTxtDataDocumento().getText()));
            } catch (Exception e) {
                throw new Exception("Inserire la data del documento");
            }

            // DETTAGLI BOLLA
            Vector dettagli = new Vector();
            DecimalFormat nf = (DecimalFormat) DecimalFormat.getInstance(Locale.ITALIAN);
            for (int i = 0; i < getTable().getRowCount(); ++i) {
                DettaglioBollaAcquisto dba = new DettaglioBollaAcquisto();
                dba.setIdArticolo((Integer) getTable().getValueAt(i, 7));
                dba.setQta(new BigDecimal(nf.parse((String) getTable().getValueAt(i, 2)).doubleValue()).setScale(3,
                        BigDecimal.ROUND_HALF_UP));
                dba.setPrezzo(new BigDecimal(nf.parse((String) getTable().getValueAt(i, 3)).doubleValue()).setScale(2,
                        BigDecimal.ROUND_HALF_UP));
                if (getTable().getValueAt(i, 4) != null) {
                    dba.setSconto(new BigDecimal(nf.parse((String) getTable().getValueAt(i, 4)).doubleValue()).setScale(2,
                            BigDecimal.ROUND_HALF_UP));
                }
                dba.setLotto((String) getTable().getValueAt(i, 5));
                if (getTable().getValueAt(i, 6) != null) {
                    dba.setDataScadenza(df.parse((String) getTable().getValueAt(i, 6)));
                }
                dba.setId((Integer) ((InventarioTableModel) getTable().getModel()).getId(i));
                dettagli.add(dba);
            }

            ba.setInventario(dettagli);
            return ba;
        } catch (Exception e) {
            throw e;
        }
    }

    public JTextField getTxtTotaleBolla() {
        if (txtTotaleBolla == null) {
            txtTotaleBolla = new JTextField();
            txtTotaleBolla.setEnabled(false);
            txtTotaleBolla.setMinimumSize(new Dimension(120, 23));
        }
        return txtTotaleBolla;
    }
}
