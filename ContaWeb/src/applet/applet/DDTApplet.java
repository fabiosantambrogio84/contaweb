package applet.applet;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Locale;

import javax.swing.JApplet;
import javax.swing.UIManager;

import applet.common.Settings;
import applet.swing.DDTWindow;

public class DDTApplet extends JApplet {

	private static final long serialVersionUID = 1L;
	
	/** 
	 * This is the default constructor
	 */
	public DDTApplet() {
		super();		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void init() {
		DDTWindow window;
		try {
			//IMPOSTO I PARAMETRI
			Settings.getInstance().setBasePath(getParameter("url"));
			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//this.setLocale(Locale.US);
			
			String id = getParameter("id");
			if (id != null) {
				window = new DDTWindow(Long.valueOf(id),this);
				setContentPane(window.getContentPane());
				window.setModalitaAggiornamentoDDT();
			} else {
				window = new DDTWindow(this);
				setContentPane(window.getContentPane());
				window.setModalitaInserimentoDDT();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
