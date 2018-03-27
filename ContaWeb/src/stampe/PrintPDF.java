package stampe;

import web.GenericAction;

public class PrintPDF extends GenericAction  {
	private static final long serialVersionUID = 1L;

	protected byte[] pdfFile = null;

	public byte[] getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(byte[] pdfFile) {
		this.pdfFile = pdfFile;
	}
	
}
