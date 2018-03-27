package resultset;

import java.io.ObjectOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import web.GenericAction;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

public class ObjectResult implements Result {

	private static final long serialVersionUID = 1152246125752620422L;

	public void execute(ActionInvocation arg0) throws Exception {
		GenericAction action = (GenericAction) arg0.getAction();
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/x-java-serialized-object");
		ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
		out.writeObject(action.getSerializedObject());
		out.flush();
	}

}
