package web;

import java.util.Map;

import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class GenericAction extends ActionSupport implements SessionAware,ApplicationAware {

	private static final long serialVersionUID = 1L;
	
    private Map session;
    private Map application;
    
    private String textResponse = null;
    private Object serializedObject = null;
    protected String messageSuccess = null;
    
    public String getMessageSuccess() {
		return messageSuccess;
	}

	public void setMessageSuccess(String messageSuccess) {
		this.messageSuccess = messageSuccess;
	}

	public Object getSerializedObject() {
		return serializedObject;
	}

	public void setSerializedObject(Object serializedObject) {
		this.serializedObject = serializedObject;
	}

	public void setTextResponse(String textResponse) {
		this.textResponse = textResponse;
	}

	public void stampaErrore(String posizione, Exception e) {
    	addActionError("Eccezione CRITICA in " + posizione + ": " + e.getMessage());
    }
	
	public void stampaErrore(String messaggio) {
    	addActionError(messaggio);
    }
	
	public void stampaMessaggio(String posizione, Exception e) {
    	addActionMessage("Eccezione NON critica in " + posizione + ": " + e.getMessage());
    }

	public void stampaMessaggio(String messaggio) {
    	addActionMessage(messaggio);
    }
    
    public Map getApplication() {
		return application;
	}

	public void setSession(Map value) {
        session = value;
    }

    public Map getSession() {
        return session;
    }

	public void setApplication(Map arg0) {
		this.application = arg0;
	}

	public String getTextResponse() {
		return textResponse;
	}
}
