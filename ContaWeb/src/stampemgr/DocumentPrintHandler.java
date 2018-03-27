package stampemgr;

abstract class DocumentPrintHandler {
	public abstract boolean isPDF();
	public abstract byte[] leggiPDF() throws Exception;
	public abstract byte[] creaPDF() throws Exception;
	public abstract void deletePDF();
	/* public abstract String creaTXT(); */
}
