package email;

public class FatturaEmailResult {

    private Integer numeroFattura;
    
    private String ragioneSociale;
    
    private String ragioneSociale2;
    
    private String email;
    
    private String emailPec;
    
    private String esitoInvioEmail;
    
    private String esitoSavePdf;
    
    private String esitoSaveEmail;
    
    public FatturaEmailResult() {
    }

    public Integer getNumeroFattura() {
        return numeroFattura;
    }

    public void setNumeroFattura(Integer numeroFattura) {
        this.numeroFattura = numeroFattura;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }
    
    public String getRagioneSociale2() {
        return ragioneSociale2;
    }

    public void setRagioneSociale2(String ragioneSociale2) {
        this.ragioneSociale2 = ragioneSociale2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailPec() {
        return emailPec;
    }

    public void setEmailPec(String emailPec) {
        this.emailPec = emailPec;
    }
    
    public String getEsitoInvioEmail() {
        return esitoInvioEmail;
    }

    public void setEsitoInvioEmail(String esitoInvioEmail) {
        this.esitoInvioEmail = esitoInvioEmail;
    }
    
    public String getEsitoSavePdf() {
        return esitoSavePdf;
    }

    public void setEsitoSavePdf(String esitoSavePdf) {
        this.esitoSavePdf = esitoSavePdf;
    }

    public String getEsitoSaveEmail() {
        return esitoSaveEmail;
    }

    public void setEsitoSaveEmail(String esitoSaveEmail) {
        this.esitoSaveEmail = esitoSaveEmail;
    }

}
