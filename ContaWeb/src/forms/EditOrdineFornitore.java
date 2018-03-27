package forms;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import dao.Articoli;
import dao.DataAccessException;
import dao.Fornitori;
import dao.OrdiniFornitori;
import dao.Settings;
import stampemgr.OrdineFornitorePrintHandler;
import vo.Articolo;
import vo.DettaglioOrdine;
import vo.DettaglioOrdineFornitore;
import vo.Fornitore;
import vo.OrdineFornitore;
import vo.RichiestaOrdine;

public class EditOrdineFornitore extends Edit {

	private static final long serialVersionUID = 1L;
	
	protected Integer id = null;
	private List<Fornitore> listFornitori = null;
	private OrdineFornitore ordineFornitore = null;
	private Date dataDal = null;
	private Date dataAl = null;
	private Vector dettagliOrdineFornitore = new Vector();
	private Vector dettagliOrdine = new Vector();
	
	private InputStream stream = null;
	private String idArticolo = null;
	private Integer idFornitore = null;

	public Integer getIdFornitore() {
		return idFornitore;
	}

	public void setIdFornitore(Integer idFornitore) {
		this.idFornitore = idFornitore;
	}

	public String getIdArticolo() {
		return idArticolo;
	}

	public void setIdArticolo(String idArticolo) {
		this.idArticolo = idArticolo;
	}

	public InputStream getStream() {
		return stream;
	}

	public void setStream(InputStream stream) {
		this.stream = stream;
	}

	public Vector getDettagliOrdineFornitore() {
		return dettagliOrdineFornitore;
	}

	public void setDettagliOrdineFornitore(Vector dettagliOrdineFornitore) {
		this.dettagliOrdineFornitore = dettagliOrdineFornitore;
	}

	public Date getDataDal() {
		return dataDal;
	}

	public void setDataDal(Date dataDal) {
		this.dataDal = dataDal;
	}

	public Date getDataAl() {
		return dataAl;
	}

	public void setDataAl(Date dataAl) {
		this.dataAl = dataAl;
	}

	public OrdineFornitore getOrdineFornitore() {
		return ordineFornitore;
	}

	public void setOrdineFornitore(OrdineFornitore ordineFornitore) {
		this.ordineFornitore = ordineFornitore;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	public String input() {
		try {
	  		if (id != null) {
	  			if (getAction().equalsIgnoreCase("delete")) {
	  				return delete();
	  			}
	  			
  				if (getAction().equalsIgnoreCase("dettagli")) {
  					ordineFornitore = new OrdineFornitore();
  					ordineFornitore.setId(id);
  					ordineFornitore = (OrdineFornitore) new OrdiniFornitori().findWithAllReferences(ordineFornitore);
  					return "dettagli";
  				}
	  		}
  		
			if (getAction().equalsIgnoreCase("insert")) {
				listFornitori = (List<Fornitore>) new Fornitori().getElements();
		  	}
		} catch (Exception e) {
			stampaErrore("EditFornitoreOrdine.input()",e);
			return ERROR;
		}
  		return INPUT;
	 }
	 
	protected String store() {
		try {
			Iterator itr = dettagliOrdine.iterator();
			Map<Integer, Collection<RichiestaOrdine>> map = new HashMap<Integer, Collection<RichiestaOrdine>>();
			
			while (itr.hasNext()) {
				DettaglioOrdine det = (DettaglioOrdine) itr.next();
				
				RichiestaOrdine ro = new RichiestaOrdine();
				
				ro.setIdDettaglioOrdine(det.getId());
				ro.setQta(det.getPezziDaOrdinare());
				ro.setDettaglioOrdine(det);
				
				if (!map.containsKey(det.getIdArticolo().intValue()))
					map.put(det.getIdArticolo().intValue(), new ArrayList<RichiestaOrdine>());
				map.get(det.getIdArticolo().intValue()).add(ro);
				
			}
			
			itr = dettagliOrdineFornitore.iterator();
			while (itr.hasNext()) {
				DettaglioOrdineFornitore det = (DettaglioOrdineFornitore) itr.next();
				Iterator itr2 = map.get(det.getIdArticolo().intValue()).iterator();
				while (itr2.hasNext()) {
					RichiestaOrdine ro = (RichiestaOrdine) itr2.next();
					ro.setDettaglioOrdineFornitore(det);
				}
				det.setRichiesteOrdini(map.get(det.getIdArticolo().intValue()));
			}
			
			ordineFornitore.setDettagliOrdineFornitore(dettagliOrdineFornitore);
			OrdiniFornitori ordiniFornitori = new OrdiniFornitori();
			
			ordiniFornitori.store(ordineFornitore);
		} catch (Exception e) {
			stampaErrore("EditOrdine.store()",e);
			return ERROR;
		}
		return SUCCESS;
	 }
	 
	protected String delete() {
		 try {
			 OrdineFornitore of = new OrdineFornitore();
			 of.setId(id);
			 new OrdiniFornitori().delete(of);
		} catch (Exception e) {
			stampaErrore("EditFornitoreOrdine.delete()",e);
			return ERROR_DELETE;
		}
	 	 return SUCCESS;
	}
	
	public String listaDettagliFornitori() throws DataAccessException {
		OrdiniFornitori of = new OrdiniFornitori();
		// *** if (ordineFornitore != null) 
		if (ordineFornitore != null && ordineFornitore.getIdFornitore() != null) 
			ordineFornitore.setDettagliOrdineFornitore(of.getDettagliFromOrdiniClienti(ordineFornitore,dataDal,dataAl));
		else if(ordineFornitore != null)
			ordineFornitore.setDettagliOrdineFornitore(of.getDettagliFromOrdiniClienti(dataDal,dataAl));
		return SUCCESS;
	}

	public List<Fornitore> getListFornitori() {
		return listFornitori;
	}

	public void setListFornitori(List<Fornitore> listFornitori) {
		this.listFornitori = listFornitori;
	}

	public String getArticoloDetails() {
		Articoli articoli = new Articoli();
		String details = "100";
		try {
			Articolo articolo = articoli.find(idArticolo);
			if (articolo.getIdFornitore().intValue() == idFornitore.intValue())
				details = "200@" + articolo.getId() + "@" + articolo.getCodiceArticolo() + "@" + articolo.getDescrizione();
		} catch (DataAccessException e) {
			details = "100";
		}
		
		stream = new ByteArrayInputStream(details.getBytes());
		return SUCCESS;
	}
	
	public String spedisciMail() {
		try {
			OrdineFornitore ordineFornitore = new OrdineFornitore();
			ordineFornitore.setId(id);
			ordineFornitore = (OrdineFornitore) new OrdiniFornitori().findWithAllReferences(ordineFornitore);
			
			if (ordineFornitore.getFornitore().getEmailAddress() == null)
				throw new DataAccessException("Il fornitore non ha associato un indirizzo email");

			/* **. 
			final String user = Settings.getInstance().getValue("mail.smtpUser");
			final String password = Settings.getInstance().getValue("mail.smtpPassword");
			**. */
			String smtpHost = Settings.getInstance().getValue("pec.smtpHost");
			final String user = Settings.getInstance().getValue("pec.smtpUser");
			final String password = Settings.getInstance().getValue("pec.smtpPassword");
			
			Properties props = new Properties();
			
			/* *.* 
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.host", Settings.getInstance().getValue("mail.smtpHost"));
			props.setProperty("mail.smtp.auth", "true");
			*.* */
			props.setProperty("mail.transport.protocol", "smtps");
			props.setProperty("mail.smtps.host", smtpHost);
			props.setProperty("mail.smtps.auth", "true");
            // props.setProperty("mail.user", Settings.getInstance().getValue("mail.smtpUser"));
            // props.setProperty("mail.password", Settings.getInstance().getValue("mail.smtpPassword"));
            // props.setProperty("mail.smtp.starttls.enable","true");						
						
  			Session mailSession = Session.getInstance(props,new javax.mail.Authenticator()
			{
				public PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication( user , password);
				}
			});  
  			
  			Transport transport = mailSession.getTransport();

  			MimeMessage message = new MimeMessage(mailSession);
  			message.setFrom(new InternetAddress(Settings.getInstance().getValue("ordiniFornitori.fromAddress")));
  			message.setSubject(Settings.getInstance().getValue("ordiniFornitori.mailSubjectPrefix") + ordineFornitore.getProgressivoCompleto());
  			message.addRecipient(Message.RecipientType.TO,new InternetAddress(ordineFornitore.getFornitore().getEmailAddress()));

  			OrdineFornitorePrintHandler oh = new OrdineFornitorePrintHandler(ordineFornitore);
  			if (!oh.isPDF())
  				oh.creaPDF();
  			
  			File fileAttachment = new File(oh.buildPDFPath(false));
  		    MimeBodyPart messageBodyPart = new MimeBodyPart();
  		    messageBodyPart.setText(Settings.getInstance().getValue("ordiniFornitori.mailContent"));
  		    Multipart multipart = new MimeMultipart();
  		    multipart.addBodyPart(messageBodyPart);
  		    messageBodyPart = new MimeBodyPart();
  		    DataSource source = new FileDataSource(fileAttachment);
  		    messageBodyPart.setDataHandler(new DataHandler(source));
  		    messageBodyPart.setFileName(fileAttachment.getName());
  		    multipart.addBodyPart(messageBodyPart);
  		    
  		    message.setContent(multipart);
  		    
  			transport.connect();
  			transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
  			transport.close();
  			message.addHeader("Sent-Date", new Date().toString());
  			//SAVE MESSAGE COPY IN FOLDER
  			OutputStream out = null;
  			try {
	  			out = new FileOutputStream(Settings.getInstance().getValue("mail.repositoryOrdiniFornitori") + "Mail_" + message.getMessageID());
	  			
	  			message.writeTo(out);
	  			out.flush();
	  			out.close();
  			} catch (Exception e) {
  				if (out != null)
  					out.close(); 
  			}
  			//Imposta l'ordine come spedito
  			new OrdiniFornitori().setSpedito(ordineFornitore);
			
		} catch (Exception e) {
			stampaErrore("EditOrdineFornitore.spedisciMail()",e);
			return ERROR;
		}
		return SUCCESS;
	}

	public Vector getDettagliOrdine() {
		return dettagliOrdine;
	}

	public void setDettagliOrdine(Vector dettagliOrdine) {
		this.dettagliOrdine = dettagliOrdine;
	}
}