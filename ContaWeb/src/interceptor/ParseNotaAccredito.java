package interceptor;

import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import vo.NotaAccredito;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import forms.EditNotaAccredito;

public class ParseNotaAccredito extends AbstractInterceptor {

	private static final long serialVersionUID = -2077053559639709510L;

	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		EditNotaAccredito action = (EditNotaAccredito) arg0.getAction();
		
		try {
			//COPIO IL DDT NEL PARAMETRO DDT
			HttpServletRequest request = ServletActionContext.getRequest();
			InputStream io = request.getInputStream();
			ObjectInputStream oio = new ObjectInputStream(io);
			action.setNotaAccredito((NotaAccredito) oio.readObject());
		} catch (Exception e) {}
		
		return action.store(); 
	}

}
