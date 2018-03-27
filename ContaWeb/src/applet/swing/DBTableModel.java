package applet.swing;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import vo.Articolo;
import vo.Cliente;
import vo.Fornitore;
import vo.VOElement;

public class DBTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private Object[][] data;
	private int rowCount = 0;
	private int columnCount = 0;
	private String[] colHeads;
	
	public DBTableModel(String[] colHeads, Collection object) {
		this.colHeads = colHeads;
		columnCount = colHeads.length;
		rowCount = object.size();

		Iterator itr = object.iterator();
		int i = 0;
		data = new Object[rowCount][colHeads.length + 1];
		while (itr.hasNext()) {
			VOElement vo = (VOElement) itr.next();
			
			if (vo instanceof Cliente) {
				Cliente cliente = (Cliente) vo;
				data[i][1] = cliente.getId();
				data[i][0] = cliente.getRs();
			}
			
			if (vo instanceof Articolo) {
				Articolo articolo = (Articolo) vo;
				data[i][2] = articolo.getCodiceArticolo();
				data[i][1] = articolo.getDescrizione();
				data[i][0] = articolo.getCodiceArticolo();
			}
			
			if (vo instanceof Fornitore) {
				Fornitore fornitore = (Fornitore) vo;
				data[i][1] = fornitore.getId();
				data[i][0] = fornitore.getDescrizione();
			}
			
			++i;
		}
	}
	
	public Object[][] getData() {
		return data;
	}
	
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public Class<?> getColumnClass(int columnIndex) {
		return data[0][columnIndex].getClass();
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
		return data[rowIndex][columnIndex];
	}
}
