package applet.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

public class ChooseDialog {

    private JDialog frame = null;
    private JPanel contentPanel = null;
    private JButton buttonTrovaCliente = null;
    private JTextField txtTrovaCliente = null;
    private JScrollPane scrollPane = null;
    private JTable table = null;
    private String title = "No title selected";
    private String[] colH = null;
    private Object[][] data;
    private int searchCol;

    private Object selectedElement;
    private Collection listElements;

    private Object parent;
    private String updateMethod;

    public ChooseDialog(String title, String[] colH, int searchCol, Collection listElements, Object parent, String updateMethod) {
        this.title = title;
        this.listElements = listElements;
        this.colH = colH;
        this.searchCol = searchCol;
        this.parent = parent;
        this.updateMethod = updateMethod;
    }

    public Object getSelectedElement() {
        return selectedElement;
    }

    public JDialog getDialog() {
        if (frame == null) {
            frame = new JDialog();
            frame.setSize(new Dimension(314, 531));
            frame.setModal(true);
            frame.setTitle(title);
            frame.setContentPane(getContentPanel());
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {
                    txtTrovaCliente.requestFocus();
                }
            });
        }
        return frame;
    }

    /**
     * This method initializes contentPanel
     * 
     * @return javax.swing.JPanel
     */
    public JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            contentPanel.add(getButtonTrovaCliente(), c);
            c.gridx = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            contentPanel.add(getTxtTrova(), c);

            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 2;
            c.weighty = 1.0;
            c.fill = GridBagConstraints.BOTH;
            contentPanel.add(getScrollPane(), c);
        }
        return contentPanel;
    }

    /**
     * This method initializes buttonTrovaCliente
     * 
     * @return javax.swing.JButton
     */
    private JButton getButtonTrovaCliente() {
        if (buttonTrovaCliente == null) {
            buttonTrovaCliente = new JButton();
            buttonTrovaCliente.setBounds(new Rectangle(10, 10, 99, 22));
            buttonTrovaCliente.setText("Trova");
            buttonTrovaCliente.setMnemonic('T');
            buttonTrovaCliente.setDefaultCapable(true);
            buttonTrovaCliente.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String string = txtTrovaCliente.getText().toUpperCase();
                    int size = data.length;
                    int i;

                    Collection lst = new ArrayList();

                    for (i = 0; i < size; ++i) {

                        // model = new DBTableModel(colH,listElements);

                        if (data[i][searchCol - 1].toString().toUpperCase().indexOf(string) >= 0) {

                            lst.add(((java.util.Vector) listElements).get(i));

                            // table.setRowSelectionInterval(i, i);
                            // Rectangle rect = table.getVisibleRect();
                            // int centerY = rect.y + rect.height/2;
                            // Rectangle cellRectangle = table.getCellRect(i, 0, true);
                            //
                            // if (centerY < cellRectangle.y) {
                            // cellRectangle.y = cellRectangle.y - rect.y + centerY;
                            // } else {
                            // cellRectangle.y = cellRectangle.y + rect.y - centerY;
                            // }
                            // table.scrollRectToVisible(cellRectangle);
                            //
                            // table.requestFocus();
                            // return;
                        }
                    }

                    table.setModel(new DBTableModel(colH, lst));
                }
            });
        }
        return buttonTrovaCliente;
    }

    /**
     * This method initializes textTrovaCliente
     * 
     * @return javax.swing.JTextField
     */
    public JTextField getTxtTrova() {
        if (txtTrovaCliente == null) {
            txtTrovaCliente = new JTextField();
            txtTrovaCliente.setBounds(new Rectangle(115, 10, 181, 21));
            txtTrovaCliente.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent arg0) {
                    if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
                        buttonTrovaCliente.doClick();
                }
            });
        }
        return txtTrovaCliente;
    }

    /**
     * This method initializes scrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setBounds(new Rectangle(10, 35, 287, 446));
            scrollPane.setViewportView(getTable());
        }
        return scrollPane;
    }

    private void saveAndClose() {
        ListSelectionModel lsm = table.getSelectionModel();
        if (!lsm.isSelectionEmpty()) {
            int selectedRow = lsm.getMinSelectionIndex();
            TableModel model = table.getModel();
            Object id = model.getValueAt(selectedRow, model.getColumnCount());
            selectedElement = id;
            if (frame != null && frame.isVisible())
                frame.setVisible(false);
            getTxtTrova().setText("");

            // Chiama il metodo che ritornerà alla schermata precedente.
            if (parent != null && updateMethod != null) {
                try {
                    Method method = parent.getClass().getMethod(updateMethod, Object.class);
                    method.invoke(parent, selectedElement);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * This method initializes table
     * 
     * @return javax.swing.JTable
     * 
     */
    private JTable getTable() {
        if (table == null) {
            DBTableModel model;
            model = new DBTableModel(colH, listElements);
            data = model.getData();
            table = new JTable(model);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent arg0) {
                    if (arg0.getClickCount() == 2)
                        saveAndClose();
                }
            });

            table.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent arg0) {
                    if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
                        saveAndClose();
                }
            });
        }

        return table;
    }
}
