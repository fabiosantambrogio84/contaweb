package email;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;


public abstract class AbstractEmailSender {
    
    private String protocol;
    
    public String host;
    
    public int port;
    
    public String username;
    
    public String password;
    
    public boolean auth;
    
    private Properties props;
    
    private Session mailSession;
        
    public AbstractEmailSender(String protocol, String host, int port, String username, String password, boolean auth) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.auth = auth;
    }
    
    protected Session creaMailSession() throws Exception{
        props = new Properties();
        props.setProperty("mail.transport.protocol", protocol);
        props.setProperty("mail.smtps.host", host);
        props.setProperty("mail.smtps.auth", auth ? "true" : "false");
        props.setProperty("mail.smtp.connectiontimeout", "5000");
        props.setProperty("mail.debug", "false");

        mailSession = Session.getInstance(props, new javax.mail.Authenticator(){
            public PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication( username , password);
            }
        });             
        return mailSession;
    }
    
    protected Transport getTransport() throws Exception{
        Transport transport = mailSession.getTransport();       
        transport.connect(host, port, username, password);
        return transport;
    }
    
}
