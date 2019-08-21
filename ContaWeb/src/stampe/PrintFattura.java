package stampe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import com.lowagie.text.List;
import com.sun.mail.smtp.SMTPMessage;

import dao.Fatture;
import dao.Settings;
import stampemgr.FatturaPrintHandler;
import stampemgr.StampeMgr;
import vo.Fattura;
import vo.VOElement;

public class PrintFattura extends PrintPDF {
	
	private static final long serialVersionUID = 1417096769427980677L;
	private Integer id = null;
	private String filterKey = null; 

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		try {
			Fattura fattura = new Fattura();
			fattura.setId(id);
			fattura = (Fattura) new Fatture().findWithAllReferences(fattura);
			pdfFile = StampeMgr.getInstance().richiediPDFDocumento(fattura);
		} catch (Exception e) {
			stampaErrore("PrintFattura.execute()", e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String pec() throws Exception {
		try {
			Fattura fattura = new Fattura();
			fattura.setId(id);
			fattura = (Fattura) new Fatture().findWithAllReferences(fattura);
			pdfFile = StampeMgr.getInstance().richiediPDFDocumento(fattura);
			String smtpHost = Settings.getInstance().getValue("pec.smtpHost");
			final String user = Settings.getInstance().getValue("pec.smtpUser");
			final String password = Settings.getInstance().getValue("pec.smtpPassword");
			//java.security.Security.setProperty("ssl.SocketFactory.provider", "org.apache.tomcat.net.SSLSocketFactory");
			//java.security.Security.setProperty("ssl.ServerSocketFactory.provider", "mio.package.SSLServerSocketFactoryImpl");
			// FatturaPrintHandler fph = new FatturaPrintHandler(fattura);
			// String percorsoFattura = fph.getPDFPath();
			
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtps");
			props.setProperty("mail.smtps.host", smtpHost);
			props.setProperty("mail.smtps.auth", "true");
			props.setProperty("mail.smtps.ssl.protocols", "TLSv1.2");
	
			Session mailSession = Session.getInstance(props,new javax.mail.Authenticator()
			{
				public PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication( user , password);
				}
			});  			
			
			Transport transport = mailSession.getTransport(); // <--SMTPS		
			transport.connect(smtpHost, user, password);
			
			FatturaPrintHandler fph = new FatturaPrintHandler(fattura);
			String percorsoFattura = fph.getPDFPath();
			
			MimeMessage message = PrintFatture.buildMail(fattura, percorsoFattura, mailSession);
			SMTPMessage msg = new SMTPMessage(message);
			
			transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
			
			Fatture fatture = new Fatture();
			Fattura dbo = new Fattura();
			dbo.setId(fattura.getId());
			// dbo = (Fattura) new Fatture().findWithAllReferences(dbo);
			dbo = (Fattura) fatture.find(dbo);
			dbo.setSpedito(true);
			// new Fatture().store(dbo);
			fatture.store(dbo);
			
			message.addHeader("Sent-Date", new Date().toString());
			//SAVE MESSAGE COPY IN FOLDER
			OutputStream out = null;
			try {
	  			File file = new File(Settings.getInstance().getValue("mail.repositoryFatture") + "Mail_" + message.getMessageID().replace("<", "").replace(">", ""));
				out = new FileOutputStream(file);		  			
	  			message.writeTo(out);
	  			out.flush();
	  			out.close();
			} catch (Exception e) {
				if (out != null)
					out.close();
				stampaErrore("PrintFattura.pec()", e);
				return ERROR;
			} finally{
				if(out != null){
					out.close();
				}
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			
			stampaErrore("PrintFattura.pec()", e);
			return ERROR;
		}
		
		return SUCCESS;
		
	}
	
	public String mail() throws Exception {
		try {
		Fattura fattura = new Fattura();
		fattura.setId(id);
		fattura = (Fattura) new Fatture().findWithAllReferences(fattura);
		pdfFile = StampeMgr.getInstance().richiediPDFDocumento(fattura);
		
		/* ***
		String smtpHost = Settings.getInstance().getValue("mail.smtpHost");
		final String user = Settings.getInstance().getValue("mail.smtpUser");
		final String password = Settings.getInstance().getValue("mail.smtpPassword");
		FatturaPrintHandler fph = new FatturaPrintHandler(fattura);
		String percorsoFattura = fph.getPDFPath();
		*** */ 
		String smtpHost = Settings.getInstance().getValue("pec.smtpHost");
		final String user = Settings.getInstance().getValue("pec.smtpUser");
		final String password = Settings.getInstance().getValue("pec.smtpPassword");
		
		Properties props = new Properties();
		/* **.
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", smtpHost);
		props.setProperty("mail.smtp.auth", "true");
		**. */
		props.setProperty("mail.transport.protocol", "smtps");
		props.setProperty("mail.smtps.host", smtpHost);
		props.setProperty("mail.smtps.auth", "true");
		props.setProperty("mail.smtps.ssl.protocols", "TLSv1.2");

		Session mailSession = Session.getInstance(props,new javax.mail.Authenticator()
		{
			public PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication( user , password);
			}
		});  			
		
		Transport transport = mailSession.getTransport(); // <--SMTPS		
		transport.connect(smtpHost, user, password);
		
		FatturaPrintHandler fph = new FatturaPrintHandler(fattura);
		String percorsoFattura = fph.getPDFPath();
		MimeMessage message = PrintFatture.buildMail(fattura, percorsoFattura, mailSession);
		SMTPMessage msg = new SMTPMessage(message);
		
		transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
		
		Fatture fatture = new Fatture();
		Fattura dbo = new Fattura();
		dbo.setId(fattura.getId());
		// dbo = (Fattura) new Fatture().findWithAllReferences(dbo);
		dbo = (Fattura) fatture.find(dbo);
		dbo.setSpedito(true);
		// new Fatture().store(dbo);
		fatture.store(dbo);
		
		message.addHeader("Sent-Date", new Date().toString());
		//SAVE MESSAGE COPY IN FOLDER
		OutputStream out = null;
		try {
			File file = new File(Settings.getInstance().getValue("mail.repositoryFatture") + "Mail_" + message.getMessageID().replace("<", "").replace(">", ""));
			out = new FileOutputStream(file);		  			
  			message.writeTo(out);
  			out.flush();
  			out.close();
		} catch (Exception e) {
			if (out != null)
				out.close();
		} finally{
			if(out != null){
				out.close();
			}
		}
				
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		
		return SUCCESS;
		
	}

	public String getFilterKey() {
		return filterKey;
	}

	public void setFilterKey(String filterKey) {
		this.filterKey = filterKey;
	}
}
