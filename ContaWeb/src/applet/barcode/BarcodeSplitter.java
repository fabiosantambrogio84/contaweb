package applet.barcode;

import java.math.BigDecimal;

public class BarcodeSplitter {

    public static String getIncompletePart(String barcodeComplete) throws Exception {
        try {
    		return barcodeComplete.substring(0, barcodeComplete.length() - 6);
		} catch (Exception e) {
            e.printStackTrace();
		}
        return "";
	}

	public static BigDecimal readQta(String barcodeStr) throws Exception {
        try {
            double value = Double.parseDouble(barcodeStr.substring(barcodeStr.length() - 6, barcodeStr.length()));
            value = value / 10000;
            return new BigDecimal(value).setScale(3,BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
            e.printStackTrace();
		}
        return new BigDecimal(0);
	}
}
