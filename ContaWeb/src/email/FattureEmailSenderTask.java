package email;

public class FattureEmailSenderTask implements Runnable{

    private final FattureEmailSender fattureEmailSender;
    
    public FattureEmailSenderTask(FattureEmailSender fattureEmailSender) {
        this.fattureEmailSender = fattureEmailSender;
    }
    
    @Override
    public void run() {
        fattureEmailSender.invia();
    }

}
