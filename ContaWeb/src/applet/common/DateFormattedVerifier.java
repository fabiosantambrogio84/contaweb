package applet.common;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class DateFormattedVerifier extends InputVerifier  {
	String parse = "";
	public DateFormattedVerifier(String parse) {
		this.parse = parse;
	}
    public boolean verify(JComponent input) {
		if (input instanceof JFormattedTextField) {
			DateFormat dateFormat = new SimpleDateFormat(parse, Locale.ITALIAN);
		 	JFormattedTextField tf = (JFormattedTextField)input;
		    try {
		        tf.setText(dateFormat.format(dateFormat.parse((String)tf.getText())));
		        if (tf.isEnabled())
		       	 tf.setBackground(UIManager.getColor("TextField.background"));
		        else
		       	 tf.setBackground(UIManager.getColor("TextField.inactiveBackground"));
		   	 return true;
		     } catch (Exception pe) {
		   	  tf.setText(dateFormat.format(new Date()));
		   	  if (tf.isEnabled())
		   	  	tf.setBackground(Color.RED);
		   	  else
		   		tf.setBackground(UIManager.getColor("TextField.inactiveBackground"));  
		   	  return false;
		     }
		}
		
		if (input instanceof JTextField) {
			DateFormat dateFormat = new SimpleDateFormat(parse, Locale.ITALIAN);
			JTextField tf = (JTextField)input;
			if (tf.getText().length() > 0) {
			    try {
			        tf.setText(dateFormat.format(dateFormat.parse((String)tf.getText())));
			        if (tf.isEnabled())
			       	 tf.setBackground(UIManager.getColor("TextField.background"));
			        else
			       	 tf.setBackground(UIManager.getColor("TextField.inactiveBackground"));
			   	return true;
			    } catch (Exception pe) {
			   	  tf.setText(dateFormat.format(new Date()));
			   	  if (tf.isEnabled())
			   	  	tf.setBackground(Color.RED);
			   	  else
			   		tf.setBackground(UIManager.getColor("TextField.inactiveBackground"));  
			   	  return false;
			    }
			} else {
				if (tf.isEnabled())
					tf.setBackground(UIManager.getColor("TextField.background"));
				else
					tf.setBackground(UIManager.getColor("TextField.inactiveBackground"));
			}
		}
		
        return true;
     }

}
