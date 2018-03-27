package applet.barcode;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import applet.common.Settings;
import applet.db.DbConnector;
import applet.swing.DDTWindow;
import applet.swing.NotaAccreditoWindow;
import vo.PrezzoConSconto;
import web.GenericAction;

public class BarcodeHandlerNota implements KeyListener {

	private static BarcodeHandlerNota bh = null;
	
	private boolean isNextReadLotto = false;
	private String barcodeStr = "";
	private NotaAccreditoWindow window = null;

	// private boolean isReading = false;

	static public BarcodeHandlerNota getInstance() {
		if (bh == null)
			bh = new BarcodeHandlerNota();	
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
			GenericAction genericAction = new GenericAction();
			genericAction.stampaErrore("applet.barcode.BarcodeHandlerNota.processaLettura(String barcodeStr)", e);
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
		// if ((event).getKeyChar() == Settings.getInstance().getHeader()) {
		//	isReading = true;
		//	barcodeStr = "";
		//	((KeyEvent)event).consume();
		// } else if (isReading) {
			if ((event).getKeyChar() == Settings.getInstance().getTerminator()) {
				//FINE LETTURA
				Barcode barcode = null;
				try {
					barcode = processaLettura(barcodeStr);
				} catch (Exception e) { 
					GenericAction genericAction = new GenericAction();
					genericAction.stampaErrore("applet.barcode.BarcodeHandlerNota.keyTyped(KeyEvent event)", e);
                }
				window.barcodeRead(barcode);
				barcodeStr = "";

				//((KeyEvent)event).consume();
				// isReading = false;
				
			} else {
				//STO LEGGENDO. AGGIUNGO NELLA STRINGA E BUTTO VIA IL CARATTERE
				barcodeStr = barcodeStr + event.getKeyChar();
			}
			event.consume();
		// }
	}

	public NotaAccreditoWindow getWindow() {
		return window;
	}

	public void setWindow(NotaAccreditoWindow window) {
		this.window = window;
	}
}
