package applet.common;

import java.awt.Color;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class NumberFormattedVerifier extends InputVerifier {
	 private int cifre = 0;
	 
	 public NumberFormattedVerifier(int cifre) {
		 this.cifre = cifre;
	 }
	 
    public boolean verify(JComponent input) {
        if (input instanceof JTextField) {
       	 NumberFormat number = NumberFormat.getNumberInstance(Locale.ITALIAN);
       	 number.setMinimumFractionDigits(cifre);
       	 number.setMaximumFractionDigits(cifre);
       	 JTextField tf = (JTextField)input;
            try {
                tf.setText(number.format(number.parse(tf.getText())));
                if (tf.isEnabled())
               	 tf.setBackground(UIManager.getColor("TextField.background"));
                else
               	 tf.setBackground(UIManager.getColor("TextField.inactiveBackground"));
           	 return true;
             } catch (ParseException pe) {
           	  tf.setText("");
           	  if (tf.isEnabled())
           	  	tf.setBackground(Color.RED);
           	  else
           		tf.setBackground(UIManager.getColor("TextField.inactiveBackground"));  
           	  return false;
             }
         }
         return true;
     }
     public boolean shouldYieldFocus(JComponent input) {
   	  boolean result = verify(input);	    	  	
   	  return result;
     }
}
