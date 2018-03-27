package applet.swing;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class DettagliTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private Vector data;
	private int rowCount = 0;
	private int columnCount = 10;
	private String[] colHeads = {"Codice", "Descrizione", "Qta'", "P.", "P.E.", "Prezzo", "Sc.", "Iva", "UM", "Lotto" };
	
	public DettagliTableModel() {
		data = new Vector();
	}
	
	public Object[][] getData() {
		Object[][] dataModel = new Object[rowCount][columnCount];
		
		for(int i=0; i< data.size(); ++i) {
			Object[] row = (Object[]) data.get(i);
			for (int j=0; j < columnCount; ++j)
				dataModel[i][j] = row[i];
		}
		
		return dataModel;
	}
	
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public Class<?> getColumnClass(int columnIndex) {
		return colHeads[columnIndex].getClass();
	}

	public int getColumnCount() {
		return columnCount;
	}

	public String getColumnName(int columnIndex) {
		return colHeads[columnIndex];
	}

	public int getRowCount() {
		return rowCount;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object[] row = (Object[]) data.get(rowIndex);
		return row[columnIndex];
	}

	public void removeAllElements() {
		data.clear();
		rowCount = 0;
		fireTableDataChanged();
	}

	public void removeRow(int selectedRow) {
		data.remove(selectedRow);
		rowCount--;
		fireTableDataChanged();
	}

	public void addRow(Object[] dati) {
		data.add(dati);
		rowCount++;
		fireTableDataChanged();
	}

	public void insertRow(int selectedRow, Object[] dati) {
		data.insertElementAt(dati, selectedRow);
		rowCount++;
		fireTableDataChanged();
	}
	
	public void setValueAt(Object obj, int row, int col) {
		Object[] rowData = (Object[]) data.get(row);
		rowData[col] = obj;
		fireTableDataChanged();
	}
}
