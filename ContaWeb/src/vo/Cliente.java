package vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

@SuppressWarnings("rawtypes")
public class Cliente extends VOElement {

    private static final long serialVersionUID = -4652820330359236949L;

    private String rs;
    private String rs2;
    private String indirizzo;
    private String localita;
    private String prov;
    private String cap;
    private String piva;
    private String email;
    private String emailPec;
    private Integer prefissoTelefono;
    private String numeroTelefono;
    private String codiceFiscale;
    private String bancaDescrizione;
    private String bancaABI;
    private String bancaCAB;
    private String bancaCC;
    private BigDecimal fido;
    private Integer idPagamento;
    private Pagamento pagamento;
    private Date dataInserimento;
    private Boolean bloccaDDT;
    private Boolean nascondiPrezzi;
    private Vector puntiConsegna;
    private Vector listiniAssociati;
    private Vector telefonate;
    private String note;
    private Integer formatoConad;
    private Integer idAgente = null;
    private Integer idAutista = null;
    private Autista autista;
    private Agente agente;

    private Boolean dittaIndividuale;

    private String nome;

    private String cognome;

    private Boolean raggruppaRiba;

    private String nomeRaggruppamentoRiba;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getBloccaDDT() {
        return bloccaDDT;
    }

    @Override
    public Integer getStato() {
        if (bloccaDDT)
            return NOT_ACTIVE;
        return NORMAL;
    }

    public void setBloccaDDT(Boolean bloccaDDT) {
        this.bloccaDDT = bloccaDDT;
    }

    public Vector getListiniAssociati() {
        return listiniAssociati;
    }

    public void setListiniAssociati(Vector listiniAssociati) {
        this.listiniAssociati = listiniAssociati;
    }

    public Boolean getNascondiPrezzi() {
        return nascondiPrezzi;
    }

    public void setNascondiPrezzi(Boolean nascondiPrezzi) {
        this.nascondiPrezzi = nascondiPrezzi;
    }

    public Vector getPuntiConsegna() {
        return puntiConsegna;
    }

    public void setPuntiConsegna(Vector puntiConsegna) {
        this.puntiConsegna = puntiConsegna;
    }

    public void setFido(BigDecimal fido) {
        this.fido = fido;
    }

    public String getBancaABI() {
        return bancaABI;
    }

    public void setBancaABI(String bancaABI) {
        this.bancaABI = bancaABI;
    }

    public String getBancaCAB() {
        return bancaCAB;
    }

    public void setBancaCAB(String bancaCAB) {
        this.bancaCAB = bancaCAB;
    }

    public String getBancaCC() {
        return bancaCC;
    }

    public void setBancaCC(String bancaCC) {
        this.bancaCC = bancaCC;
    }

    public String getBancaDescrizione() {
        return bancaDescrizione;
    }

    public void setBancaDescrizione(String bancaDescrizione) {
        this.bancaDescrizione = bancaDescrizione;
    }

    public boolean isBloccaDDT() {
        return bloccaDDT;
    }

    public void setBloccaDDT(boolean bloccaDDT) {
        this.bloccaDDT = bloccaDDT;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public Date getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Date dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getFido() {
        return fido;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getLocalita() {
        return localita;
    }

    public void setLocalita(String localita) {
        this.localita = localita;
    }

    public boolean isNascondiPrezzi() {
        return nascondiPrezzi;
    }

    public void setNascondiPrezzi(boolean nascondiPrezzi) {
        this.nascondiPrezzi = nascondiPrezzi;
    }

    public String getPiva() {
        return piva;
    }

    public void setPiva(String piva) {
        this.piva = piva;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public String getRs2() {
        return rs2;
    }

    public void setRs2(String rs2) {
        this.rs2 = rs2;
    }

    public Integer getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(Integer idPagamento) {
        this.idPagamento = idPagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public Vector getTelefonate() {
        return telefonate;
    }

    public void setTelefonate(Vector telefonate) {
        this.telefonate = telefonate;
    }

    public Integer getPrefissoTelefono() {
        if (prefissoTelefono == null)
            return 39;

        return prefissoTelefono;
    }

    public void setPrefissoTelefono(Integer prefissoTelefono) {
        this.prefissoTelefono = prefissoTelefono;
    }

    public String getTelefonoCompleto() {
        return prefissoTelefono + numeroTelefono;
    }

    public String getEmailPec() {
        return emailPec;
    }

    public void setEmailPec(String emailPec) {
        this.emailPec = emailPec;
    }

    public Integer getFormatoConad() {
        return formatoConad;
    }

    public void setFormatoConad(Integer formatoConad) {
        this.formatoConad = formatoConad;
    }

    public Integer getIdAutista() {
        return idAutista;
    }

    public void setIdAutista(Integer idAutista) {
        this.idAutista = idAutista;
    }

    public Autista getAutista() {
        return autista;
    }

    public void setAutista(Autista autista) {
        this.autista = autista;
    }

    public Integer getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(Integer idAgente) {
        this.idAgente = idAgente;
    }

    public Agente getAgente() {
        return agente;
    }

    public void setAgente(Agente agente) {
        this.agente = agente;
    }

    public Boolean getDittaIndividuale() {
        return dittaIndividuale;
    }

    public boolean isDittaIndividuale() {
        return dittaIndividuale;
    }

    public void setDittaIndividuale(boolean dittaIndividuale) {
        this.dittaIndividuale = dittaIndividuale;
    }

    public void setDittaIndividuale(Boolean dittaIndividuale) {
        this.dittaIndividuale = dittaIndividuale;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Boolean getRaggruppaRiba() {
        return raggruppaRiba;
    }

    public boolean isRaggruppaRiba() {
        return raggruppaRiba;
    }

    public void setRaggruppaRiba(boolean raggruppaRiba) {
        this.raggruppaRiba = raggruppaRiba;
    }

    public void setRaggruppaRiba(Boolean raggruppaRiba) {
        this.raggruppaRiba = raggruppaRiba;
    }

    public String getNomeRaggruppamentoRiba() {
        return nomeRaggruppamentoRiba;
    }

    public void setNomeRaggruppamentoRiba(String nomeRaggruppamentoRiba) {
        this.nomeRaggruppamentoRiba = nomeRaggruppamentoRiba;
    }

    @Override
    public String toString(){
    	StringBuilder sb = new StringBuilder();
        sb.append("id: ").append(getId()).append(", ");
        sb.append("nome: ").append(getNome()).append(", ");
        sb.append("cognome: ").append(getCognome()).append(", ");
        sb.append("rs: ").append(getRs()).append(", ");
        sb.append("rs2: ").append(getRs2()).append(", ");
        sb.append("indirizzo: ").append(getIndirizzo()).append(", ");
        sb.append("localita: ").append(getLocalita()).append(", ");
        sb.append("prov: ").append(getProv()).append(", ");
        sb.append("cap: ").append(getCap()).append(", ");
        sb.append("piva: ").append(getPiva()).append(", ");
        sb.append("email: ").append(getEmail()).append(", ");
        sb.append("emailPec: ").append(getEmailPec()).append(", ");
        sb.append("prefissoTelefono: ").append(getPrefissoTelefono()).append(", ");
        sb.append("numeroTelefono: ").append(getNumeroTelefono()).append(", ");
        sb.append("codiceFiscale: ").append(getCodiceFiscale()).append(", ");
        sb.append("bancaDescrizione: ").append(getBancaDescrizione()).append(", ");
        sb.append("bancaABI: ").append(getBancaABI()).append(", ");
        sb.append("bancaCAB: ").append(getBancaCAB()).append(", ");
        sb.append("bancaCC: ").append(getBancaCC()).append(", ");
        sb.append("fido: ").append(getFido()).append(", ");
        sb.append("idPagamento: ").append(getIdPagamento()).append(", ");
        sb.append("pagamento: ").append(getPagamento()).append(", ");
        sb.append("dataInserimento: ").append(getDataInserimento()).append(", ");
        sb.append("bloccaDDT: ").append(getBloccaDDT()).append(", ");
        sb.append("nascondiPrezzi: ").append(getNascondiPrezzi()).append(", ");
        sb.append("puntiConsegna: ").append(getPuntiConsegna()).append(", ");
        sb.append("listiniAssociati: ").append(getListiniAssociati()).append(", ");
        sb.append("telefonate: ").append(getTelefonate()).append(", ");
        sb.append("note: ").append(getNote()).append(", ");
        sb.append("formatoConad: ").append(getFormatoConad()).append(", ");
        sb.append("idAgente: ").append(getIdAgente()).append(", ");
        sb.append("idAutista: ").append(getIdAutista()).append(", ");
        sb.append("autista: ").append(getAutista()).append(", ");
        sb.append("agente: ").append(getAgente()).append(", ");
        if(dittaIndividuale != null){
        	sb.append("dittaIndividuale: ").append(isDittaIndividuale()).append(", ");
        } else{
        	sb.append("dittaIndividuale: , ");
        }
        if(raggruppaRiba != null){
        	sb.append("raggruppaRiba: ").append(isRaggruppaRiba()).append(", ");
        } else{
        	sb.append("raggruppaRiba: , ");
        }
        if(nomeRaggruppamentoRiba != null){
        	sb.append("nomeRaggruppamentoRiba: ").append(getNomeRaggruppamentoRiba()).append(";");
        } else{
        	sb.append("nomeRaggruppamentoRiba: , ");
        }
        return sb.toString();
    }
    
}
