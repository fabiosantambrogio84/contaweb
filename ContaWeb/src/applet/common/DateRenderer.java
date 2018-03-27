package applet.common;

import javax.swing.table.DefaultTableCellRenderer;

public class DateRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	public DateRenderer() { super(); }
	public void setValue(Object value) {
		//CONVERTO LA DATA IN UN FORMATO LEGGIBILE
		try {
			String sValue =(String)value;
			setText(sValue.substring(8,10) + "/" + sValue.substring(5,7) + "/" + sValue.substring(0,4));
		} catch (Exception e) {
			setText("");
		}
    }
}
