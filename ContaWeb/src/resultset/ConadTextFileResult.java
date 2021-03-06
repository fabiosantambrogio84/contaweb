package resultset;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import stampe.ExportConad;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

public class ConadTextFileResult implements Result {

	private static final long serialVersionUID = 1152246125752620422L;

	@Override
	public void execute(ActionInvocation arg0) throws Exception {
		ExportConad action = (ExportConad) arg0.getAction();
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setHeader("content-disposition","attachment; filename=conad.txt");
		response.setContentLength(action.getTextResponse().length());
		response.setContentType("text/plain");
		response.getOutputStream().write(action.getTextResponse().getBytes());
		response.getOutputStream().flush();
	}

}
