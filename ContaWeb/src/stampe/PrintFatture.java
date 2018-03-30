package stampe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

import dao.DataAccessObject;
import dao.Fatture;
import dao.Ivas;
import dao.RiepilogoFatture;
import dao.Settings;
import email.FattureEmailSender;
import stampemgr.StampeMgr;
import vo.Fattura;
import vo.Iva;
import vo.VOElement;

@SuppressWarnings("rawtypes")
public class PrintFatture extends PrintPDF {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(PrintFatture.class);

    private Date dataDal = null;
    private Date dataAl = null;

    private List listaFatture = null;

    public Date getDataAl() {
        return dataAl;
    }

    public void setDataAl(Date dataAl) {
        this.dataAl = dataAl;
    }

    public Date getDataDal() {
        return dataDal;
    }

    public void setDataDal(Date dataDal) {
        this.dataDal = dataDal;
    }

    @Override
    public String execute() { // STAMPA LISTA DOCUMENTI
        try {
            Fatture fatture = new Fatture();
            Collection listaFatture = fatture.getFatture(dataDal, dataAl);

            Iterator itr = listaFatture.iterator();
            StampeMgr stmgr = StampeMgr.getInstance();

            // CONCATENO I PDFs
            Document doc = null;
            PdfCopy writer = null;
            PdfImportedPage page = null;

            ByteArrayOutputStream fos = new ByteArrayOutputStream();
            while (itr.hasNext()) {
                PdfReader reader = new PdfReader(stmgr.richiediPDFDocumento((VOElement) itr.next()));
                reader.consolidateNamedDestinations();
                int n = reader.getNumberOfPages();

                if (doc == null) {
                    doc = new Document(reader.getPageSizeWithRotation(1));
                    writer = new PdfCopy(doc, fos);
                    doc.open();
                }

                for (int i = 0; i < n; ++i) {
                    if (reader.getPageContent(i + 1).length > 100) {
                        page = writer.getImportedPage(reader, i + 1);
                        writer.addPage(page);
                    }
                }
            }
            if (doc != null)
                doc.close();

            pdfFile = fos.toByteArray();
        } catch (Exception e) {
            stampaErrore("PrintFatture.execute()", e);
            return ERROR;
        }
        return SUCCESS;
    }

    // CLAUDIO - Modifica stampa PEC e no PEC
    public String printNoMailNoPec() { // STAMPA LISTA DOCUMENTI
        /* modifica Fabio 27/03/2018 */
        try {
            Fatture fatture = new Fatture();
            Collection listaFatture = fatture.getFattureNoMailNoPEC(dataDal, dataAl);

            Iterator itr = listaFatture.iterator();
            StampeMgr stmgr = StampeMgr.getInstance();

            // CONCATENO I PDFs
            Document doc = null;
            PdfCopy writer = null;
            PdfImportedPage page = null;

            ByteArrayOutputStream fos = new ByteArrayOutputStream();
            while (itr.hasNext()) {
                PdfReader reader = new PdfReader(stmgr.richiediPDFDocumento((VOElement) itr.next()));
                reader.consolidateNamedDestinations();
                int n = reader.getNumberOfPages();

                if (doc == null) {
                    doc = new Document(reader.getPageSizeWithRotation(1));
                    writer = new PdfCopy(doc, fos);
                    doc.open();
                }

                for (int i = 0; i < n; ++i) {
                    if (reader.getPageContent(i + 1).length > 100) {
                        page = writer.getImportedPage(reader, i + 1);
                        writer.addPage(page);
                    }
                }
            }
            if (doc != null)
                doc.close();

            pdfFile = fos.toByteArray();
        } catch (Exception e) {
            stampaErrore("PrintFatture.execute()", e);
            return ERROR;
        }
        return SUCCESS;
    }

    // //CLAUDIO modifiche stampa fatture PEC no PEC
    // public String printPec() { //STAMPA LISTA DOCUMENTI
    // StampeMgr stmgr = StampeMgr.getInstance();
    // try {
    // Fatture fatture = new Fatture();
    // Collection listaFatture = fatture.getFatturePEC(dataDal, dataAl);
    // Iterator itr = listaFatture.iterator();
    // String smtpHost = Settings.getInstance().getValue("pec.smtpHost");
    // final String user = Settings.getInstance().getValue("pec.smtpUser");
    // final String password = Settings.getInstance().getValue("pec.smtpPassword");
    // //java.security.Security.setProperty("ssl.SocketFactory.provider", "org.apache.tomcat.net.SSLSocketFactory");
    // //java.security.Security.setProperty("ssl.ServerSocketFactory.provider", "mio.package.SSLServerSocketFactoryImpl");
    //
    // Properties props = new Properties();
    // props.setProperty("mail.transport.protocol", "smtps");
    // props.setProperty("mail.smtps.host", smtpHost);
    // props.setProperty("mail.smtps.auth", "true");
    //
    // Session mailSession = Session.getInstance(props,new javax.mail.Authenticator()
    // {
    // public PasswordAuthentication getPasswordAuthentication()
    // {
    // return new PasswordAuthentication( user , password);
    // }
    // });
    //
    // Transport transport = mailSession.getTransport(); // <--SMTPS
    // transport.connect(smtpHost, user, password);
    //
    // while (itr.hasNext()){
    // VOElement fat = (VOElement) itr.next();
    // stmgr.richiediPDFDocumento(fat);
    // FatturaPrintHandler fph = new FatturaPrintHandler(fat);
    // String percorsoFattura = fph.getPDFPath();
    //
    // MimeMessage message = buildMail((Fattura) fat, percorsoFattura, mailSession);
    // SMTPMessage msg = new SMTPMessage(message);
    //
    // transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
    //
    // Fattura dbo = new Fattura();
    // dbo.setId(fat.getId());
    // // dbo = (Fattura) new Fatture().findWithAllReferences(dbo);
    // dbo = (Fattura) fatture.find(dbo);
    // dbo.setSpedito(true);
    // fatture.store(dbo);
    //
    // message.addHeader("Sent-Date", new Date().toString());
    // //SAVE MESSAGE COPY IN FOLDER
    // OutputStream out = null;
    // try {
    // out = new FileOutputStream(Settings.getInstance().getValue("mail.repositoryFatture") + "Mail_" + message.getMessageID());
    // message.writeTo(out);
    // out.flush();
    // out.close();
    // } catch (Exception e) {
    // if (out != null)
    // out.close();
    // }
    // }
    //
    // transport.close();
    //
    // } catch (Exception e) {
    // stampaErrore("PrintFatture.printPec()", e);
    // return ERROR;
    // }
    // return SUCCESS;
    // }
    //
    // CLAUDIO modifiche stampa fatture PEC no PEC
    // public String printMail() { //STAMPA LISTA DOCUMENTI
    // StampeMgr stmgr = StampeMgr.getInstance();
    // try {
    // String host = Settings.getInstance().getValue("mail.smtpHost");
    // final String username = Settings.getInstance().getValue("mail.smtpUser");
    // final String password = Settings.getInstance().getValue("mail.smtpPassword");
    //
    // FattureEmailSender emailSender = new FattureEmailSender("smtps", host, 465, username, password, true);
    //
    // Fatture fatture = new Fatture();
    // Collection listaFatture = fatture.getFattureMail(dataDal, dataAl);
    // Iterator itr = listaFatture.iterator();
    //
    //
    // while (itr.hasNext()){
    // VOElement fat = (VOElement) itr.next();
    // stmgr.richiediPDFDocumento(fat);
    // FatturaPrintHandler fph = new FatturaPrintHandler(fat);
    // String percorsoFattura = fph.getPDFPath();
    //
    // MimeMessage message = buildMail((Fattura) fat, percorsoFattura, mailSession);
    // SMTPMessage msg = new SMTPMessage(message);
    //
    // transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
    //
    // Fattura dbo = new Fattura();
    // dbo.setId(fat.getId());
    // // dbo = (Fattura) new Fatture().findWithAllReferences(dbo);
    // dbo = (Fattura) fatture.find(dbo);
    // dbo.setSpedito(true);
    // fatture.store(dbo);
    //
    // message.addHeader("Sent-Date", new Date().toString());
    // //SAVE MESSAGE COPY IN FOLDER
    // OutputStream out = null;
    // try {
    // out = new FileOutputStream(Settings.getInstance().getValue("mail.repositoryFatture") + "Mail_" + message.getMessageID());
    // message.writeTo(out);
    // out.flush();
    // out.close();
    // } catch (Exception e) {
    // if (out != null)
    // out.close();
    // }
    // }
    //
    // transport.close();
    //
    // } catch (Exception e) {
    // stampaErrore("PrintFatture.printMail()", e);
    // return ERROR;
    // }
    // return SUCCESS;
    // }

    public static MimeMessage buildMail(Fattura fattura, String percorsoFattura, Session session)
            throws AddressException, MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(Settings.getInstance().getValue("fatture.fromAddress")));
        message.setSubject(Settings.getInstance().getValue("fatture.mailSubjectPrefix") + fattura.getNumeroProgressivo());
        // message.addRecipient(Message.RecipientType.TO,new InternetAddress(fattura.getCliente().getEmailPec()));
        if (fattura.getCliente().getEmailPec() != null && fattura.getCliente().getEmailPec().trim().length() > 0)
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(fattura.getCliente().getEmailPec()));
        else
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(fattura.getCliente().getEmail()));
        File fileAttachment = new File(percorsoFattura);

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(Settings.getInstance().getValue("fatture.mailContent"));
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(fileAttachment);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileAttachment.getName());
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        return message;
    }

    @SuppressWarnings("unchecked")
    public String printRiepilogoFatture() throws Exception {

        BigDecimal totale = new BigDecimal(0);

        try {
            // COMPILO LA LISTA DELLE FATTURE
            Fatture fatture = new Fatture();
            fatture.setOrderByCliente();
            Collection colFatture = fatture.getFatture(dataDal, dataAl);
            Iterator itr = colFatture.iterator();
            listaFatture = new ArrayList();

            while (itr.hasNext()) {
                Fattura fattura = (Fattura) itr.next();
                listaFatture.add(fattura);
                totale = totale.add(fattura.getTotaleFattura());
            }

            if (listaFatture.size() == 0) {
                return ERROR;
            }

        } catch (Exception e) {
            return ERROR;
        }

        RiepilogoFatture pf = new RiepilogoFatture();
        pf.setDataAl(dataAl);
        pf.setDataDal(dataDal);
        pf.setListaFatture(listaFatture);
        pf.setTotale(totale);
        pf.setTipo("F");

        pdfFile = StampeMgr.getInstance().richiediPDFDocumentoList(pf);

        return SUCCESS;
    }

    /* STAMPA LE FATTURE NON PAGATE */
    @SuppressWarnings("unchecked")
    public String printRiepilogoFattureNP() throws Exception {
        try {
            // COMPILO LA LISTA DELLE FATTURE
            Fatture fatture = new Fatture();
            fatture.setOrderByCliente();
            Collection colFatture = fatture.getFattureNonPagate(dataDal, dataAl);
            Iterator itr = colFatture.iterator();
            listaFatture = new ArrayList();

            while (itr.hasNext()) {
                Fattura fattura = (Fattura) itr.next();
                listaFatture.add(fattura);
            }

            if (listaFatture.size() == 0) {
                return ERROR;
            }

        } catch (Exception e) {
            return ERROR;
        }

        RiepilogoFatture pf = new RiepilogoFatture();
        pf.setDataAl(dataAl);
        pf.setDataDal(dataDal);
        pf.setListaFatture(listaFatture);
        pf.setTipo("NP");

        pdfFile = StampeMgr.getInstance().richiediPDFDocumentoList(pf);

        return SUCCESS;
    }

    @SuppressWarnings("unchecked")
    public String printRiepilogoFattureCC() throws Exception {
        try {
            // COMPILO LA LISTA DELLE FATTURE
            Fatture fatture = new Fatture();
            fatture.setOrderByNumeroFattura();
            Collection colFatture = fatture.getFatture(dataDal, dataAl);
            Iterator itr = colFatture.iterator();
            listaFatture = new ArrayList();

            while (itr.hasNext()) {
                Fattura fattura = (Fattura) itr.next();

                // CREO IL NUOVO OGGETTO
                RigaFattureCommercianti rf = new RigaFattureCommercianti();
                rf.setNumeroFattura(fattura.getNumeroProgressivo());
                rf.setDataFattura(fattura.getData());
                rf.setCliente(fattura.getCliente());
                rf.setTotaleFattura(fattura.getTotaleFattura());

                // CALCOLO IMPONIBILI
                fattura.calcolaTotali();
                HashMap<BigDecimal, BigDecimal[]> imponibili = fattura.getImponibili();

                Ivas dao = new Ivas();
                dao.setOrderByDescrizione(DataAccessObject.ORDER_ASC);
                Collection aliquote = dao.getElements();

                int index = 1;
                for (Object obj : aliquote) {
                    Iva iva = (Iva) obj;
                    if (!imponibili.containsKey(iva.getValore()))
                        continue;

                    BigDecimal imponibile = imponibili.get(iva.getValore())[0];
                    BigDecimal imposta = imponibili.get(iva.getValore())[1];

                    if (index == 1) {
                        rf.setIva1(iva.getValore());
                        rf.setImponibile1(imponibile);
                        rf.setImposta1(imposta);

                    } else if (index == 2) {
                        rf.setIva2(iva.getValore());
                        rf.setImponibile2(imponibile);
                        rf.setImposta2(imposta);
                    } else if (index == 3) {
                        rf.setIva3(iva.getValore());
                        rf.setImponibile3(imponibile);
                        rf.setImposta3(imposta);
                    }

                    if (index == 3)
                        break;

                    index++;
                }

                listaFatture.add(rf);
            }
        } catch (Exception e) {
            return ERROR;
        }

        RiepilogoFatture pf = new RiepilogoFatture();
        pf.setDataAl(dataAl);
        pf.setDataDal(dataDal);
        pf.setListaFatture(listaFatture);
        pf.setTipo("CC");

        pdfFile = StampeMgr.getInstance().richiediPDFDocumentoList(pf);

        return SUCCESS;
    }

    public List getListaFatture() {
        return listaFatture;
    }

    public void setListaFatture(List listaFatture) {
        this.listaFatture = listaFatture;
    }

    public String printListaFattureMail() {
        try {
            logger.info("Invio fatture via mail...");

            /* recupero le credenziali per l'invio email */
            final String host = Settings.getInstance().getValue("mail.smtpHost");
            final String username = Settings.getInstance().getValue("mail.smtpUser");
            final String password = Settings.getInstance().getValue("mail.smtpPassword");
            final int port = 465;

            /* recupera le fatture */
            Fatture fatture = new Fatture();
            Collection listaFatture = fatture.getFattureMail(dataDal, dataAl);

            logger.info("Lista fatture ottenuta. Numero elementi: " + listaFatture.size());
            logger.info("Prepara invio email. Host=" + host + ", protocol=smtps, port=" + port + ", username=" + username
                    + ", password=" + password + ", pec=false");

            /* prepara l'invio email */
            FattureEmailSender emailSender = new FattureEmailSender(listaFatture, fatture, "smtps", host, port, username, password,
                    true, false);

            /* invia le fatture in un nuovo thread (in modo asincrono) */
            // ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
            // newCachedThreadPool.submit(new FattureEmailSenderTask(emailSender));

            /* invia le fatture */
            emailSender.invia();

            logger.info("Email inviate con successo.");
        } catch (Exception e) {
            logger.error("Errore invio fatture mail", e);
            stampaErrore("PrintFatture.printMail()", e);
            return ERROR;
        }
        return SUCCESS;
    }

    public String printListaFatturePec() {
        try {
            logger.info("Invio fatture via PEC...");

            final String host = Settings.getInstance().getValue("pec.smtpHost");
            final String username = Settings.getInstance().getValue("pec.smtpUser");
            final String password = Settings.getInstance().getValue("pec.smtpPassword");
            final int port = 465;

            /* recupera le fatture */
            Fatture fatture = new Fatture();
            Collection listaFatture = fatture.getFatturePEC(dataDal, dataAl);

            logger.info("Lista fatture ottenuta. Numero elementi: " + listaFatture.size());
            logger.info("Prepara invio email. Host=" + host + ", protocol=smtps, port=" + port + ", username=" + username
                    + ", password=" + password + ", pec=true");

            /* prepara l'invio email PEC */
            FattureEmailSender emailSender = new FattureEmailSender(listaFatture, fatture, "smtps", host, port, username, password,
                    true, true);

            /* invia le fatture */
            emailSender.invia();

            logger.info("Email PEC inviate con successo.");
        } catch (Exception e) {
            logger.error("Errore invio fatture PEC", e);
            stampaErrore("PrintFatture.printPec()", e);
            return ERROR;
        }
        return SUCCESS;
    }
}
