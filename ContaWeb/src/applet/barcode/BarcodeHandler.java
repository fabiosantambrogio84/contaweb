package applet.barcode;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import applet.swing.DDTWindow;
import vo.PrezzoConSconto;

import applet.common.Settings;
import applet.db.DbConnector;

public class BarcodeHandler implements KeyListener {

	private static BarcodeHandler bh = null;
	
	private boolean isNextReadLotto = false;
	private String barcodeStr = "";
	DDTWindow window = null;

	// private boolean isReading = false;

	static public BarcodeHandler getInstance() throws Exception {
		try {
            if (bh == null)
                bh = new BarcodeHandler();	
		} catch (Exception e) {
            e.printStackTrace();
		}
		return bh;
	}
	
	public Barcode processaLettura(String barcodeStr) throws Exception {
		Barcode barcode = new Barcode();
		barcode.setBarcodeString(barcodeStr);
		barcode.setNumeroLotto(isNextReadLotto);

		try {
			if (!isNextReadLotto) {
				PrezzoConSconto dati = new DbConnector().getPrezzoArticoloFromBarcode(barcodeStr, window.getClienteSelezionato().getId());
				barcode.setDatiAssociati(dati);
				if (!dati.getArticolo().isCompleteBarCode()) { //l'articolo non ha una qta predefinita
					barcode.setQtaLetta(BarcodeSplitter.readQta(barcodeStr));
				} else
					barcode.setQtaLetta(dati.getArticolo().getQtaPredefinita());
			}
			isNextReadLotto = !isNextReadLotto;
		} catch (Exception e) {
			throw e;
		}
		
		return barcode;
	}

	public boolean isNextReadLotto() {
		return isNextReadLotto;
	}

	public void setNextReadLotto(boolean isNextReadLotto) {
		this.isNextReadLotto = isNextReadLotto;
	}

	public void keyPressed(KeyEvent arg0) {
	}

	public void keyReleased(KeyEvent arg0) {
		
	}

	public void keyTyped(KeyEvent event) {
		//		if ((event).getKeyChar() == Settings.getInstance().getHeader()) {
		//			isReading = true;
		//			barcodeStr = "";
		//			((KeyEvent)event).consume();
		//		} else if (isReading) {
			if ((event).getKeyChar() == Settings.getInstance().getTerminator()) {
				//FINE LETTURA
				Barcode barcode = null;
				try {
					barcode = processaLettura(barcodeStr);
				} catch (Exception e) {
                    e.printStackTrace();
                }
				window.barcodeRead(barcode);

				((KeyEvent)event).consume();
				// isReading = false;
				barcodeStr = "";
			} else {
				//STO LEGGENDO. AGGIUNGO NELLA STRINGA E BUTTO VIA IL CARATTERE
				barcodeStr = barcodeStr + event.getKeyChar();
				event.consume();
			}
		//		}
	}

	public DDTWindow getWindow() {
		return window;
	}

	public void setWindow(DDTWindow window) {
		this.window = window;
	}
}
