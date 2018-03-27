package resultset;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import stampe.PrintPDF;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

public class PdfResult implements Result {

	private static final long serialVersionUID = 1152246125752620422L;

	public void execute(ActionInvocation arg0) throws Exception {
		PrintPDF action = (PrintPDF) arg0.getAction();
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setHeader("content-disposition","inline; filename=documento.pdf");
		response.setContentLength(action.getPdfFile().length);
		response.setContentType("application/pdf");
		response.getOutputStream().write(action.getPdfFile());
		response.getOutputStream().flush();
	}

}
