package applet.swing;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class InventarioTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private Vector data;
	private int columnCount = 0;
	private String[] colHeads;
	
	public InventarioTableModel(String[] colHeads) {
		this.colHeads = colHeads;
		columnCount = colHeads.length;
		data = new Vector();
	}
	
	public Object[][] getData() {
		Object[][] result = new Object[data.size()][colHeads.length];
		for(int i=0;i<data.size();++i)
			for(int j=0;j<colHeads.length; ++j)
				result[i][j] = ((Vector)data.get(i)).get(j);
		return result;
	}
	
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public String getColumnName(int columnIndex) {
		return colHeads[columnIndex];
	}

	public int getRowCount() {
		return data.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return ((Vector)data.get(rowIndex)).get(columnIndex);
	}
	
	public Object getId(int index) {
		return ((Vector)data.get(index)).get(colHeads.length);
	}
	
	@SuppressWarnings("unchecked")
	public void insertRow(int index, Vector dati) {
		data.set(index, dati);
		this.fireTableDataChanged();
	}
	
	@SuppressWarnings("unchecked")
	public void addRow(Vector data) {
		this.data.add(data);
		this.fireTableDataChanged();
	}
	
	public void removeRow(int index) {
		data.remove(index);
		this.fireTableDataChanged();
	}

	public void removeAllElements() {
		data.removeAllElements();
		this.fireTableDataChanged();
	}
}
