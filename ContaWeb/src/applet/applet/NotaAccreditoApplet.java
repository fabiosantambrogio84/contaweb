package applet.applet;

import javax.swing.JApplet;
import javax.swing.UIManager;

import applet.common.Settings;
import applet.swing.NotaAccreditoWindow;

public class NotaAccreditoApplet extends JApplet {

	private static final long serialVersionUID = 1L;
	
	/** 
	 * This is the default constructor
	 */
	public NotaAccreditoApplet() {
		super();		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void init() {
		NotaAccreditoWindow window;
		try {
			//IMPOSTO I PARAMETRI
			Settings.getInstance().setBasePath(getParameter("url"));
			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//WindowsLookAndFeel.setMnemonicHidden(false);
			//this.setLocale(Locale.US);
			
			String id = getParameter("id");
			if (id != null) {
				window = new NotaAccreditoWindow(Long.valueOf(id),this);
				setContentPane(window.getContentPane());
				window.setModalitaAggiornamentoDDT();
			} else {
				window = new NotaAccreditoWindow(this);
				setContentPane(window.getContentPane());
				window.setModalitaInserimentoDDT();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
