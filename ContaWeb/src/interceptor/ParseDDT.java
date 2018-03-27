package interceptor;

import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import vo.DDT;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import forms.EditDDT;

public class ParseDDT extends AbstractInterceptor {

	private static final long serialVersionUID = -2077053559639709510L;

	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		EditDDT action = (EditDDT) arg0.getAction();
		
		try {
			//COPIO IL DDT NEL PARAMETRO DDT
			HttpServletRequest request = ServletActionContext.getRequest();
			InputStream io = request.getInputStream();
			ObjectInputStream oio = new ObjectInputStream(io);
			action.setDdt((DDT) oio.readObject());
		} catch (Exception e) {}
		
		if(arg0.getProxy().getActionName().equals("saveDDT2")) return action.store2();
		else return action.store(); 
	}

}
