package email;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.sun.mail.smtp.SMTPMessage;

import dao.Fatture;
import dao.Settings;
import stampemgr.FatturaPrintHandler;
import stampemgr.StampeMgr;
import vo.Cliente;
import vo.Fattura;
import vo.VOElement;

public class FattureEmailSender extends AbstractEmailSender{

    private static final Logger logger = Logger.getLogger(FattureEmailSender.class);
    
    private StampeMgr stmgr;
        
    private String from = Settings.getInstance().getValue("fatture.fromAddress");
    
    private String subjectPrefix = Settings.getInstance().getValue("fatture.mailSubjectPrefix"); 
    
    private String repositoryMail = Settings.getInstance().getValue("mail.repositoryFatture");
        
    private Session mailSession;
    
    private Transport transport;
    
    private Collection<?> fattureElements;
    
    private Fatture fatture;
    
    private boolean pec;
    
    public FattureEmailSender(Collection<?> fattureElements, Fatture fatture, String protocol, String host, int port, String username, String password, boolean auth, boolean pec) {
        super(protocol, host, port, username, password, auth);
        this.fattureElements = fattureElements;
        this.fatture = fatture;
        this.stmgr = StampeMgr.getInstance();
        this.pec = pec;
    }
        
    private String getPdfPath(VOElement fattura){
        String path = null;
        try{
            stmgr.richiediPDFDocumento(fattura);
            FatturaPrintHandler fph = new FatturaPrintHandler(fattura);
            path = fph.getPDFPath();
            return path;
        }catch(Exception e){
            return null;
        }
    }
    
    private MimeMessage creaMessaggio(Fattura fattura, String pdfPath) throws Exception{
        MimeMessage message = new MimeMessage(mailSession);
        
        /* setta l'indirizzo del mittente */
        String from = username;
        if(pec){
            from = this.from;
        }
        message.setFrom(new InternetAddress(from));
        
        /* setta l'oggetto */
        message.setSubject(subjectPrefix + fattura.getNumeroProgressivo());
        
        /* recupera l'indirizzo email del cliente */
        String to = null;
        if(pec){
            to = fattura.getCliente().getEmailPec();
        }else{
            to = fattura.getCliente().getEmail();
        }
        
        /* setta l'indirizzo del destinatario */
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        
        /* recupera il pdf della fattura */
        File fileAttachment = new File(pdfPath);
        
        /* crea il body e gli allegati */
        Multipart multipart = new MimeMultipart();
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(Settings.getInstance().getValue("fatture.mailContent"));
        multipart.addBodyPart(messageBodyPart);
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(fileAttachment);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileAttachment.getName());
        multipart.addBodyPart(messageBodyPart);
        
        /* setta il contenuto (testo ed allegati) dell'email */
        message.setContent(multipart);
        
        return message;
    }
    
    private void aggiornaStato(Fatture fatture, VOElement fattura) throws Exception{
        Fattura dbo = new Fattura();
        dbo.setId(fattura.getId());
        dbo = (Fattura) fatture.find(dbo);
        dbo.setSpedito(true);
        fatture.store(dbo);
    }
    
    private void salvaMessaggio(MimeMessage message) throws Exception{
        OutputStream out = null;
        try {
            out = new FileOutputStream(repositoryMail + "Mail_" + System.currentTimeMillis()/*message.getMessageID()*/);                   
            message.writeTo(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if (out != null)
                out.close();
        }
    }
    
    public void invia(){
        try {
            List<FatturaEmailResult> results = new ArrayList<>();
            
            /* crea mail session */
            mailSession = creaMailSession();
            transport = getTransport();
            
            logger.info("Sessione invio email creata con successo.");
            
            /* itera sulle fatture*/
            Iterator<?> iter = fattureElements.iterator();
            while (iter.hasNext()){
                boolean savePdf = false;
                boolean invioEmail = false;
                boolean saveEmail = false;
                
                VOElement fattura = (VOElement) iter.next();
                
                /* recupera i dati del cliente */
                Cliente cliente = ((Fattura)fattura).getCliente();
                String ragioneSociale = cliente.getRs();
                String ragioneSociale2 = cliente.getRs2();
                String email = cliente.getEmail();
                String emailPec = cliente.getEmailPec();
                
                /* crea e salva il pdf della fattura */
                String pdfPath = getPdfPath(fattura);
                
                logger.info("Fattura id="+fattura.getId()+" cliente="+ragioneSociale+" : file pdf creato con successo in "+pdfPath);
                
                if(pdfPath != null && pdfPath != ""){
                    savePdf = true;
                    MimeMessage message = null;
                    try{
                        /* crea il messaggio */
                        message = creaMessaggio((Fattura)fattura, pdfPath);
                        
                        logger.info("Messaggio email per fattura num="+((Fattura)fattura).getNumeroProgressivo()+ " creato con successo");
                        
                        /* invia il messaggio */
                        SMTPMessage msg = new SMTPMessage(message);
                        transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
                        
                        logger.info("Fattura inviata con successo a "+email+"(PEC="+emailPec+")");
                        
                        /* aggiorno lo stato della fattura */
                        aggiornaStato(fatture, fattura); 
                        
                        invioEmail = true;
                    }catch(Exception e){
                        invioEmail = false;
                    }
                    
                    /* salvo il messaggio inviato */
                    if(invioEmail){
                        try{
                            salvaMessaggio(message);
                            saveEmail = true;
                            logger.info("Messaggio email salvato con successo.");
                        }catch(Exception e){
                            saveEmail = false;
                        }
                    }
                }else{
                    savePdf = false;
                    invioEmail = false;
                    saveEmail = false;
                }
                
                /* crea il risultato per la fattura */
                FatturaEmailResult result = new FatturaEmailResult();
                result.setRagioneSociale(ragioneSociale);
                result.setRagioneSociale2(ragioneSociale2);
                result.setEmail(email);
                result.setEmailPec(emailPec);
                result.setNumeroFattura(((Fattura)fattura).getNumeroProgressivo());
                result.setEsitoInvioEmail(invioEmail ? "OK" : "KO");
                result.setEsitoSavePdf(savePdf ? "OK" : "KO");
                result.setEsitoSaveEmail(saveEmail ? "OK" : "KO");
                
                /* aggiungi il risultato alla lista */
                results.add(result);
            }
            logger.info("Preparo l'invio dell'email all'amministratore");
            
            /* invio email amministrazione */
            final String host = Settings.getInstance().getValue("mail.smtpHost");
            final String username = Settings.getInstance().getValue("mail.smtpUser");
            final String password = Settings.getInstance().getValue("mail.smtpPassword");
            final int port = 465;
            
            AdminEmailSender adminEmailSender = new AdminEmailSender("smtps", host, port, username, password, true, pec);
            adminEmailSender.sendFattureReport(results);
            
            logger.info("Report invio fatture email/pec inviato con successo all'amministratore");
        } catch (Exception e) {
            logger.error("Errore nell'invio delle fatture email/pec", e);
        } finally{
            try {
                transport.close();
            } catch (MessagingException e) {
            }
        }
    };
    
}
