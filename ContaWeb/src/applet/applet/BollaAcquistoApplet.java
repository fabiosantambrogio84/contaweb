package applet.applet;

import java.util.Locale;

import javax.swing.JApplet;
import javax.swing.UIManager;

import applet.common.Settings;
import applet.swing.BollaAcquistoWindow;

//import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

public class BollaAcquistoApplet extends JApplet {

	private static final long serialVersionUID = 8054087467764867421L;
	
	public void init() {
		BollaAcquistoWindow window = null;
		try {
			Settings.getInstance().setBasePath(getParameter("url"));
			//WindowsLookAndFeel.setMnemonicHidden(false);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//this.setLocale(Locale.ITALIAN);
			
			//VISUALIZZO LA FINESTRA
			String id = getParameter("id");
			if (id != null)
				window = new BollaAcquistoWindow(Long.valueOf(id));
			else
				window = new BollaAcquistoWindow();
				
			setContentPane(window.getPanel());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
