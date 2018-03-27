package forms;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

public class NumberConverter extends StrutsTypeConverter {

	@Override
	public Object convertFromString(Map arg0, String[] arg1, Class arg2) {
		try {
			NumberFormat fm = DecimalFormat.getInstance((Locale) arg0.get("com.opensymphony.xwork2.ActionContext.locale"));
			
			BigDecimal value = new BigDecimal(fm.parse(arg1[0]).floatValue());
			value = value.setScale(2,BigDecimal.ROUND_HALF_UP);
			
			return value;
		} catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public String convertToString(Map arg0, Object arg1) {
		return null;
	}

}
