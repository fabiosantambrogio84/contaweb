package email;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.sun.mail.smtp.SMTPMessage;

public class AdminEmailSender extends AbstractEmailSender {

    private static final Logger logger = Logger.getLogger(AdminEmailSender.class);

    private final static String FROM = "urbani.giuseppe@urbanialimentari.com";

    private final static String TO = "info@urbanialimentari.com";

    private final static String SUBJECT = "ContaWeb: report spedizione fatture ";

    private Session mailSession;

    private Transport transport;

    private boolean pec;

    AdminEmailSender(String protocol, String host, int port, String username, String password, boolean auth, boolean pec) {
        super(protocol, host, port, username, password, auth);
        this.pec = pec;
    }

    public void sendFattureReport(List<FatturaEmailResult> fattureResults) throws Exception {
        String emailOrPec = null;
        if (pec) {
            emailOrPec = "PEC";
        } else {
            emailOrPec = "EMAIL";
        }
        logger.info("Invio report spedizione fatture " + emailOrPec + "...");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            long executionTs = System.currentTimeMillis();

            mailSession = creaMailSession();
            transport = getTransport();

            logger.info("Session email creata con successo.");

            File file = null;
            String filename = "";

            /* crea il contenuto dell'email */
            String content = "";
            if (fattureResults != null && !fattureResults.isEmpty()) {
                content = "In allegato i risultati dell'operazione <b>'Spedizione Fatture " + emailOrPec
                        + "'</b>.<br/> Data esecuzione: " + sdf.format(new Date());

                /* crea il file csv da allegare */
                file = creaCsv(fattureResults, executionTs);

                logger.info("Allegato csv creato con successo.");

                /* crea il nome del file */
                filename = "ReportSpedizioneFatture" + emailOrPec + "_" + executionTs + ".csv";
            } else {
                content = "'<b>Spedizione Fatture '" + emailOrPec + "</b>: nessuna fattura da inviare. Data esecuzione: "
                        + sdf.format(new Date());
            }

            MimeMessage message = new MimeMessage(mailSession);

            /* setta l'indirizzo del mittente */
            message.setFrom(new InternetAddress(FROM));

            /* setta l'oggetto */
            message.setSubject(SUBJECT + emailOrPec);

            /* setta l'indirizzo del destinatario */
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(TO));

            /* crea il body e l'eventuale allegato */
            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(content, "utf-8", "html");
            multipart.addBodyPart(messageBodyPart);
            if (file != null) {
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);
            }
            message.setContent(multipart);

            SMTPMessage msg = new SMTPMessage(message);
            transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
            transport.close();

            logger.info("Email inviata con successo a " + TO);

            if (file != null) {
                file.delete();
                logger.info("File temporaneo cancellato con successo.");
            }
        } catch (Exception e) {
            logger.error("Errore durante l'invio dell'email all'amministratore.", e);
        }
    }

    private File creaCsv(List<FatturaEmailResult> fattureResults, long executionTs) throws Exception {
        File temp = File.createTempFile("fattureResults_" + executionTs, ".csv");
        temp.deleteOnExit();
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
        bw.append("NUMERO_FATTURA,RAGIONE_SOCIALE,RAGIONE_SOCIALE_2,EMAIL,EMAIL_PEC,INVIO_EMAIL,SALVATAGGIO_PDF,SALVATAGGIO_EMAIL");
        bw.newLine();
        for (FatturaEmailResult fatturaResult : fattureResults) {
            String csvRow = "";
            csvRow = csvRow + fatturaResult.getNumeroFattura() + ",";
            csvRow = csvRow + fatturaResult.getRagioneSociale() + ",";
            csvRow = csvRow + fatturaResult.getRagioneSociale2() + ",";
            csvRow = csvRow + fatturaResult.getEmail() + ",";
            csvRow = csvRow + fatturaResult.getEmailPec() + ",";
            csvRow = csvRow + fatturaResult.getEsitoInvioEmail() + ",";
            csvRow = csvRow + fatturaResult.getEsitoSavePdf() + ",";
            csvRow = csvRow + fatturaResult.getEsitoSaveEmail() + "\r\n";
            bw.append(csvRow);
        }
        bw.close();
        return temp;
    }
}
