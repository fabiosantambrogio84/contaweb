package forms;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

// import vo.Agente;
import dao.Agenti;
import dao.Articoli;
import dao.Autisti;
import dao.Clienti;
import dao.DDTs;
import dao.DataAccessException;
import dao.Listini;
import dao.Ordini;
import dao.PuntiConsegna;
import dao.StatisticheVendite;
import dao.Telefonate;
import utils.DeleteOrdini;
import vo.Articolo;
import vo.Autista;
import vo.Cliente;
import vo.DDT;
import vo.DettaglioOrdine;
import vo.ListinoAssociato;
import vo.Ordine;
import vo.PrezzoConSconto;
import vo.PuntoConsegna;
import vo.Telefonata;

@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class EditOrdine extends Edit {

    private static final long serialVersionUID = 1L;

    private String codiceArticolo = null;
    private Integer codiceArticoloKey = null;
    private Date dataSpedizione = null;
    private Vector dettagliOrdine = new Vector();
    protected Integer id = null;
    // private Integer idAgenti = null;
    private Integer idArticolo = null;
    private Integer idAutisti = null;
    private Integer idCliente = null;
    private Integer idListinoAssociato = 0;
    private Integer idOrdine = null;
    private Integer idPuntoConsegna = null;
    private Collection listaArticoli = null;
    private Collection listAgenti = null;
    private Collection listAutisti = null;
    private Collection listClienti = null;
    private Collection listCodiciArticoli = null;
    private Vector<ListinoAssociato> listiniAssociati = null;
    private Collection listListiniAssociati = null;
    private Collection listPuntiConsegna = null;
    private Integer orarioSpedizione = Ordine.ORARIO_MATTINA;
    private Map<Integer, String> orariSpedizione = null;
    private Ordine ordine = null;
    private Vector prezziDaListini = new Vector();
    private Collection statMese;
    private Collection statMeseCliente;
    private Collection statSettimana;
    private Collection statSettimanaCliente;
    private InputStream stream = null;
    private String typeList = null;
    private Integer indexPrezziDaListini = 0;

    private String filterDeleteOrdini = null;

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    public Integer getCodiceArticoloKey() {
        return codiceArticoloKey;
    }

    public void setCodiceArticoloKey(Integer codiceArticoloKey) {
        this.codiceArticoloKey = codiceArticoloKey;
    }

    /*
     * *** public Integer getIdAgenti() { return idAgenti; }
     * 
     * public void setIdAgenti(Integer idAgenti) { this.idAgenti = idAgenti; }
     */

    public Integer getIdAutisti() {
        return idAutisti;
    }

    public void setIdAutista(Integer idAutista) {
        this.idAutisti = idAutista;
    }

    public Integer getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(Integer idOrdine) {
        this.idOrdine = idOrdine;
    }

    public Integer getIdPuntoConsegna() {
        return idPuntoConsegna;
    }

    public void setIdPuntoConsegna(Integer idPuntoConsegna) {
        this.idPuntoConsegna = idPuntoConsegna;
    }

    public Collection getListPuntiConsegna() throws DataAccessException {
        PuntoConsegna pc = new PuntoConsegna();

        listPuntiConsegna = new ArrayList();
        if (ordine == null || ordine.getIdCliente() == null || ordine.getIdCliente() == -1) {
            pc.setNome("Nessuna destinazione");
            pc.setId(-1);
            listPuntiConsegna.add(pc);
        } else {
            // pc.setNome("Nessuna in particolare");
            // pc.setId(-1);
            // listPuntiConsegna.add(pc);

            PuntiConsegna punti = new PuntiConsegna(ordine.getIdCliente());
            listPuntiConsegna.addAll(punti.getElements());
        }

        return listPuntiConsegna;
    }

    public Collection getListAutisti() {
        if (listAutisti == null) {
            try {
                listAutisti = new Autisti().getElements();
            } catch (Exception e) {
                stampaMessaggio("EditOrdine.getListAutisti()", e);
                return null;
            }
        }
        return listAutisti;
    }

    public Collection getListAgenti() {
        if (listAgenti == null) {
            try {
                listAgenti = new Agenti().getElements();
            } catch (Exception e) {
                stampaMessaggio("EditOrdine.getListAgenti()", e);
                return null;
            }
        }
        return listAgenti;
    }

    /*
     * **. public void setListAgenti(Collection listAgenti) { this.listAgenti = listAgenti; } .
     */

    public void setListAutisti(Collection listAutisti) {
        this.listAutisti = listAutisti;
    }

    public void setListPuntiConsegna(Collection listPuntiConsegna) {
        this.listPuntiConsegna = listPuntiConsegna;
    }

    public String getTypeList() {
        return typeList;
    }

    public void setTypeList(String typeList) {
        this.typeList = typeList;
    }

    public String getPuntiConsegnaPerOrdini() throws DataAccessException {
        if (ordine.getId() != null) {
            Ordine ordineTemp = new Ordine();
            ordineTemp.setId(ordine.getId());
            ordineTemp = (Ordine) new Ordini().find(ordineTemp);
            idPuntoConsegna = ordineTemp.getIdPuntoConsegna();

            // if (!evaluatePrezziDaListini().equalsIgnoreCase(SUCCESS))
            // return ERROR;

        }

        return SUCCESS;
    }

    public String getPrezziDaListini() {
        String tmpString;
        if (indexPrezziDaListini > prezziDaListini.size())
            return "-";
        tmpString = prezziDaListini.get(indexPrezziDaListini).toString();
        indexPrezziDaListini++;
        return tmpString;
    }

    public String getPrezzoDaListini(Articolo tmpArticolo) throws DataAccessException {
        Cliente tmpCliente = new Cliente();
        tmpCliente = ordine.getCliente();
        tmpCliente = (Cliente) new Clienti().findWithAllReferences(tmpCliente);

        Listini listini = new Listini();

        PrezzoConSconto tmpPrezzoConSconto = null;
        tmpPrezzoConSconto = listini.getPrezzoConSconto(tmpArticolo, tmpCliente, dataSpedizione);

        if (tmpPrezzoConSconto != null) {
            prezziDaListini.add(tmpPrezzoConSconto.getPrezzo());
            return tmpPrezzoConSconto.getPrezzo().toString();
        }
        return ERROR;
    }

    public String evaluatePrezziDaListini(Articolo articolo) {
        PrezzoConSconto tmpPrezzoConSconto = null;
        Listini listini = new Listini();
        Cliente tmpCliente = new Cliente();
        Articolo tmpArticolo = new Articolo();

        if (articolo.getId() == null)
            return "nd.0";
        /*
         * if ((articolo.getAttivo() == null) || (articolo.getAttivo().equals(articolo.NOT_ACTIVE))) return "nd.1"; if
         * (articolo.getIdArticolo() == null) return "nd.2";
         */

        try {
            tmpArticolo = (Articolo) new Articoli().findWithAllReferences(articolo);
        } catch (DataAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "exception_articolo";
        }
        Date tmpDataSpedizione = null;

        if (idCliente == null) {
            if (ordine == null)
                if (idOrdine == null)
                    return "nd.4";
                else {
                    ordine = new Ordine();
                    ordine.setId(id);
                    try {
                        ordine = (Ordine) new Ordini().findWithAllReferences(ordine);
                    } catch (DataAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return "exception ordine";
                    }
                }
            if (ordine == null)
                return "nd.5";
            tmpCliente = ordine.getCliente();
        } else {
            tmpCliente.setId(idCliente);
        }
        try {
            tmpCliente = (Cliente) new Clienti().findWithAllReferences(tmpCliente);
        } catch (DataAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "exception cliente";
        }

        if (dataSpedizione != null)
            tmpDataSpedizione = dataSpedizione;
        else if (ordine != null)
            tmpDataSpedizione = ordine.getDataSpedizione();
        else
            tmpDataSpedizione = new Date();

        tmpPrezzoConSconto = listini.getPrezzoConSconto(tmpArticolo, tmpCliente, tmpDataSpedizione);

        if (tmpPrezzoConSconto == null)
            return "nd.6";

        return tmpPrezzoConSconto.getPrezzo().toString();
    }

    public String evaluatePrezziDaListini() throws DataAccessException {
        if (ordine.getId() != null) {
            indexPrezziDaListini = 0;
            Listini listini = new Listini();

            // ArrayList<PrezzoConSconto> prezziDaListini = new ArrayList();
            PrezzoConSconto tmpPrezzoConSconto = null;
            DettaglioOrdine tmpDettaglioOrdine = null;
            Articolo tmpArticolo = new Articolo();
            Cliente tmpCliente = new Cliente();

            for (int i = 0; i < dettagliOrdine.size(); i++) {
                tmpDettaglioOrdine = (DettaglioOrdine) dettagliOrdine.get(i);
                tmpArticolo = tmpDettaglioOrdine.getArticolo();
                tmpArticolo = (Articolo) new Articoli().findWithAllReferences(tmpArticolo);
                tmpCliente = ordine.getCliente();
                tmpCliente = (Cliente) new Clienti().findWithAllReferences(tmpCliente);

                tmpPrezzoConSconto = listini.getPrezzoConSconto(tmpArticolo, tmpCliente, dataSpedizione);

                if (tmpPrezzoConSconto != null)
                    prezziDaListini.add(tmpPrezzoConSconto.getPrezzo());
            }

            if (dettagliOrdine.size() != prezziDaListini.size())
                return ERROR;
        }

        return SUCCESS;
    }

    public Map getOrariSpedizione() {
        if (orariSpedizione == null) {
            orariSpedizione = new HashMap<Integer, String>();
            orariSpedizione.put(Ordine.ORARIO_POMERIGGIO, "pomeriggio");
            orariSpedizione.put(Ordine.ORARIO_MATTINA, "mattina");
        }
        return orariSpedizione;
    }

    public String getArticoloDetails() {
        Articoli articoli = new Articoli();
        String details = "100";
        try {
            Articolo articolo = articoli.find(idArticolo);
            String prezzo = "nd";
            prezzo = evaluatePrezziDaListini(articolo);

            details = "200@" + articolo.getId() + "@" + articolo.getCodiceArticolo() + "@" + articolo.getDescrizione() + "@" + prezzo;
        } catch (DataAccessException e) {
            details = "100";
        }

        stream = new ByteArrayInputStream(details.getBytes());
        return SUCCESS;
    }

    public Integer getOrarioSpedizione() {
        return orarioSpedizione;
    }

    public void setOrarioSpedizione(Integer orarioSpedizione) {
        this.orarioSpedizione = orarioSpedizione;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /*
     * private void layListArticoli(Collection list) { Iterator itr = list.iterator(); Integer countFornitori = 0; Fornitore previous =
     * null; while (itr.hasNext()) { DettaglioOrdine det = (DettaglioOrdine) itr.next(); if (previous == null || previous.getId() !=
     * det.getArticolo().getFornitore().getId()) { previous = det.getArticolo().getFornitore(); ++countFornitori; } }
     * 
     * Integer size = (int)Math.ceil((float)(list.size() + countFornitori)/(float)6); DettaglioOrdine[][] table = new
     * DettaglioOrdine[size][6]; itr = list.iterator();
     * 
     * int row = 0; int column = 0; previous = null; while (itr.hasNext()) { DettaglioOrdine det = (DettaglioOrdine) itr.next(); if
     * (previous == null || previous.getId() != det.getArticolo().getFornitore().getId()) { DettaglioOrdine fake = new
     * DettaglioOrdine(); fake.setArticolo(new Articolo()); fake.getArticolo().setId(null);
     * fake.getArticolo().setDescrizione(det.getArticolo().getFornitore().getDescrizione()); table[row][column] = fake; previous =
     * det.getArticolo().getFornitore(); if (row == size - 1) { column++; row = 0; } else row++; }
     * 
     * table[row][column] = det; if (row == size - 1) { column++; row = 0; } else row++; }
     * 
     * listaArticoli = new Vector(); for(int i=0; i<size;++i) { Vector elements = new Vector(); for(int j=0;j<6;++j) { if (table[i][j]
     * != null) elements.add(table[i][j]); else { elements.add(new DettaglioOrdine()); } } listaArticoli.add(elements); } }
     */

    @Override
    public String input() throws DataAccessException {
        if (id != null) {
            try {
                if (getAction().equalsIgnoreCase("delete")) {
                    return delete();
                }

                if (getAction().equalsIgnoreCase("edit")) {

                    ordine = new Ordine();
                    ordine.setId(id);
                    ordine = (Ordine) new Ordini().findWithAllReferences(ordine);
                    orarioSpedizione = ordine.getOrarioSpedizione();
                    dataSpedizione = ordine.getDataSpedizione();

                    dettagliOrdine = ordine.getDettagliOrdine();
                    // *.* idAgenti = ordine.getIdAgenti();
                    idAutisti = ordine.getIdAutista();
                    // Collection list = new Ordini().getDettagliAttivi(ordine.getDettagliOrdine());
                    // layListArticoli(list);

                    if (!evaluatePrezziDaListini().equalsIgnoreCase(SUCCESS))
                        return ERROR;

                    // Listini listini = new Listini();
                    // listiniAssociati = listini.getListiniAssociatiByCliente(ordine.getCliente());
                    // ArrayList col = new ArrayList();
                    //
                    // if (listiniAssociati.size() != 0)
                    // idListinoAssociato = 1;
                    // Iterator itr = listiniAssociati.iterator();
                    // ListinoAssociato listinoAssociato = new ListinoAssociato();
                    // while (itr.hasNext()) {
                    // listinoAssociato = (ListinoAssociato) itr.next();
                    // // listListiniAssociati.add(listinoAssociato); // .add((ListinoAssociato)itr.next())
                    // col.add(listinoAssociato);
                    // }
                    // listListiniAssociati = col;
                }

            } catch (Exception e) {
                stampaErrore("EditOrdine.input() - edit: ", e);
                return ERROR;
            }
        }

        if (getAction().equalsIgnoreCase("insert")) {
            try {
                // Collection list = new Ordini().getDettagliAttivi();
                // layListArticoli(list);
                if (idCliente != null) {
                    ordine = new Ordine();
                    ordine.setIdCliente(idCliente);
                    Cliente cliente = new Clienti().find(idCliente);
                    if (cliente != null) {
                        if (cliente.getIdAutista() != null)
                            ordine.setIdAutista(cliente.getIdAutista());

                        if (cliente.getIdAgente() != null)
                            ordine.setIdAgente(cliente.getIdAgente());
                    }
                    if (idAutisti != null)
                        ordine.setIdAutista(idAutisti);
                }
            } catch (Exception e) {
                stampaErrore("EditOrdine.input() - insert", e);
                return ERROR;
            }
        }

        /*
         * if (!getAction().equalsIgnoreCase("delete") && (listCodiciArticoli == null)) { Articoli art = new Articoli();
         * listCodiciArticoli = new ArrayList();
         * 
         * Collection list = art.getActiveElements(); Iterator itr = list.iterator(); while (itr.hasNext()) { Articolo a = (Articolo)
         * itr.next(); listCodiciArticoli.add(a.getCodiceArticolo()); } }
         */

        return INPUT;
    }

    public Collection getListListiniAssociati() {
        return listListiniAssociati;
    }

    public Integer getIdListinoAssociato() {
        return idListinoAssociato;
    }

    public void setListListiniAssociati(Collection listListiniAssociati) {
        this.listListiniAssociati = listListiniAssociati;
    }

    public Vector<ListinoAssociato> getListiniAssociati() {
        return listiniAssociati;
    }

    public String searchCodiciArticoli() {
        Articoli articoli = new Articoli();
        Articoli articoli2 = new Articoli();
        String response = "[";

        if (codiceArticolo != null) {
            Collection<Articolo> list;
            ArrayList<Articolo> listtot = new ArrayList<Articolo>();
            try {
                list = articoli.listDaCodiceDescrParziale(codiceArticolo);
                listtot.addAll(list);
            } catch (DataAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return ERROR;
            }

            Iterator<Articolo> itr = listtot.iterator();
            ArrayList<Integer> ctr = new ArrayList<Integer>();

            while (itr.hasNext()) {
                Articolo a = itr.next();
                if (!(ctr.contains(a.getId())) || ctr.isEmpty()) {
                    ctr.add(a.getId());
                    if (response.equalsIgnoreCase("["))
                        response += "{\"label\" : \"" + a.getCodiceArticolo() + " - " + a.getDescrizione() + "\", \"value\" :  \""
                                + a.getCodiceArticolo() + " - " + a.getDescrizione() + "\", \"id\" : \"" + a.getId() + "\"}";
                    else
                        response += ",{\"label\" : \"" + a.getCodiceArticolo() + " - " + a.getDescrizione() + "\", \"value\" :  \""
                                + a.getCodiceArticolo() + " - " + a.getDescrizione() + "\", \"id\" : \"" + a.getId() + "\"}";
                }
            }
        }

        response += "]";
        setTextResponse(response);
        return SUCCESS;
    }

    /*
     * private static Collection<Articoli> removeDuplicates(Collection<Articoli> list) {
     * 
     * // Store unique items in result. ArrayList<String> result = new ArrayList<>();
     * 
     * // Record encountered Strings in HashSet. HashSet<String> set = new HashSet<>();
     * 
     * // Loop over argument list. for (String item : list) {
     * 
     * // If String is not in set, add it to the list and the set. if (!set.contains(item)) { result.add(item); set.add(item); } }
     * return result; }
     */

    /*
     * *.. public void storeAgent() { try {
     * 
     * Ordine ordineVecchio = new Ordine(); ordineVecchio.setId(idOrdine); ordineVecchio = (Ordine) new Ordini().find(ordineVecchio);
     * 
     * ordineVecchio.setAgente(new Agenti().find(idAgenti)); ordineVecchio.setIdAgenti(idAgenti);
     * 
     * new Ordini().store(ordineVecchio); } catch (Exception e) { stampaErrore("EditOrdine.storeAgent()",e); } } ..
     */

    public void storeAutista() {
        try {
            Ordine ordineVecchio = new Ordine();
            ordineVecchio.setId(idOrdine);
            ordineVecchio = (Ordine) new Ordini().find(ordineVecchio);
            ordineVecchio.setAutista(new Autisti().find(idAutisti));
            ordineVecchio.setIdAutista(idAutisti);
            new Ordini().store(ordineVecchio);

            Collection<DDT> ddts = new DDTs().getDDTsByIdOrdine(idOrdine);
            if (ddts.size() == 1) {
                DDT ddt = (DDT) ddts.toArray()[0];
                ddt = (DDT) new DDTs().find(ddt);
                ddt.setIdAutista(idAutisti);
                ddt.setAutista(new Autisti().find(idAutisti));
                new DDTs().store(ddt);
            }
        } catch (Exception e) {
            stampaErrore("EditOrdine.storeAutista()", e);
        }
    }

    public void storeAutistaTelefonata() {
        try {
            Telefonata tel = new Telefonata();
            tel.setId(idOrdine);
            tel = (Telefonata) new Telefonate().find(tel);
            tel.setAutista(new Autisti().find(idAutisti));
            tel.setIdAutista(idAutisti);
            new Telefonate().store(tel);
        } catch (Exception e) {
            stampaErrore("EditOrdine.storeAutista()", e);
        }
    }

    public void storeDateShip() {
        try {

            Ordine ordineVecchio = new Ordine();
            ordineVecchio.setId(idOrdine);
            ordineVecchio = (Ordine) new Ordini().find(ordineVecchio);

            ordineVecchio.setDataSpedizione(dataSpedizione);

            new Ordini().store(ordineVecchio);
        } catch (Exception e) {
            stampaErrore("EditOrdine.storeDateShip()", e);
        }
    }

    @Override
    protected String store() {
        String result = ERROR;
        try {
            // FIXME: What kind of problem is it?
            if (ordine.getNote() != null && ordine.getNote().startsWith(", "))
                ordine.setNote(ordine.getNote().substring(2));

            if (ordine.getId() != null) {
                Ordine ordineVecchio = new Ordine();
                ordineVecchio.setId(ordine.getId());
                ordineVecchio = (Ordine) new Ordini().find(ordineVecchio);
                ordine.setNumeroProgressivo(ordineVecchio.getNumeroProgressivo());
                ordine.setAnnoContabile(ordineVecchio.getAnnoContabile());
            }
            if (idPuntoConsegna != -1) {
                ordine.setIdPuntoConsegna(idPuntoConsegna);
                ordine.setPuntoConsegna(new PuntiConsegna(ordine.getIdCliente()).find(idPuntoConsegna));
            }

            /*
             * .** if(ordine.getIdAgenti()!=null) { ordine.setAgente(new Agenti().find(ordine.getIdAgenti()));
             * ordine.setIdAgenti(ordine.getIdAgenti()); } .**
             */

            if (ordine.getIdAutista() != null) {
                Autista autista = new Autisti().find(ordine.getIdAutista());
                ordine.setAutista(autista);
                ordine.setIdAutista(ordine.getIdAutista());

                /* Aggiorno il DDT associo (se esiste) */
                Collection<DDT> ddts = new DDTs().getDDTsByIdOrdine(ordine.getId());
                if (ddts.size() == 1) {
                    DDT ddt = (DDT) ddts.toArray()[0];
                    ddt = (DDT) new DDTs().find(ddt);
                    ddt.setIdAutista(ordine.getIdAutista());
                    ddt.setAutista(autista);
                    new DDTs().store(ddt);
                }
            }

            if (ordine.getIdAgente() != null) {
                ordine.setAgente(new Agenti().find(ordine.getIdAgente()));
                ordine.setIdAgente(ordine.getIdAgente());
            }

            // ordine.setIdAgenti(idAgenti);
            ordine.setDataSpedizione(dataSpedizione);
            ordine.setDataCreazione(new Date());
            ordine.setCliente(new Clienti().find(ordine.getIdCliente()));
            ordine.setDettagliOrdine(dettagliOrdine);
            Iterator itr = dettagliOrdine.iterator();
            while (itr.hasNext()) {
                DettaglioOrdine ord = (DettaglioOrdine) itr.next();
                ord.setOrdine(ordine);
                ord.setPezziDaEvadere(ord.getPezziOrdinati());
                ord.setPezziDaOrdinare(ord.getPezziOrdinati());
            }
            Ordini tmpOrdini = new Ordini();
            result = tmpOrdini.store(ordine);
            if (!result.equalsIgnoreCase(SUCCESS)) {
                stampaErrore("EditOrdine.store(): Store doesn't work");
                result = ERROR;
                return result;
            }
            // new Ordini().store(ordine);
        } catch (Exception e) {
            stampaErrore("EditOrdine.store()", e);
            result = ERROR;
            return result;
        }

        if (typeList != null && !typeList.equalsIgnoreCase(""))
            if (typeList.equalsIgnoreCase("giorno"))
                result = "success_giorno";
            else
                result = "success_week";
        else
            return SUCCESS;
        return result;
    }

    @Override
    protected String delete() {
        try {
            ordine = new Ordine();
            ordine.setId(id);
            new Ordini().delete(ordine);
        } catch (Exception e) {
            stampaErrore("EditOrdine.delete()", e);
            return ERROR_DELETE;
        }
        return SUCCESS;
    }

    public String deleteAll() {
        try {
            /* recupero il filtro di cancellazione sullo stato */
            int statoOrdiniToDelete = DeleteOrdini.getOutputFromLabel(filterDeleteOrdini);

            /* recupero la lista di ordini */
            Collection list = new Ordini().getElements();

            /* cancello gli ordini a seconda dello stato */
            for (Object ordine : list) {
                Ordine ord = (Ordine) ordine;
                if (statoOrdiniToDelete == -1) {
                    new Ordini().delete(ord);
                } else {
                    if (ord.getStatoOrdine() == statoOrdiniToDelete) {
                        new Ordini().delete(ord);
                    }
                }
            }
        } catch (Exception e) {
            stampaErrore("EditOrdine.deleteAll()", e);
            return ERROR_DELETE;
        }
        return SUCCESS;
    }

    public Collection getListClienti() {
        if (listClienti == null) {
            try {
                Clienti dao = new Clienti();
                dao.setOrderByDescrizione(Clienti.ORDER_ASC);
                listClienti = dao.getElements();
                // .*. listClienti = new Clienti().getElements();
            } catch (Exception e) {
                stampaMessaggio("EditOrdine.getListClienti()", e);
                return null;
            }
        }
        return listClienti;
    }

    public void setListClienti(Collection listClienti) {
        this.listClienti = listClienti;
    }

    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }

    public Date getDataSpedizione() {
        if (dataSpedizione == null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, 1);
            dataSpedizione = cal.getTime();
        }
        return dataSpedizione;
    }

    public void setDataSpedizione(Date dataSpedizione) {
        this.dataSpedizione = dataSpedizione;
    }

    public Collection getListaArticoli() {
        return listaArticoli;
    }

    public void setListaArticoli(Collection listaArticoli) {
        this.listaArticoli = listaArticoli;
    }

    public Vector getDettagliOrdine() {
        return dettagliOrdine;
    }

    public void setDettagliOrdine(Vector dettagliOrdine) {
        this.dettagliOrdine = dettagliOrdine;
    }

    public Integer getIdArticolo() {
        return idArticolo;
    }

    public void setIdArticolo(Integer idArticolo) {
        this.idArticolo = idArticolo;
    }

    public void setOrariSpedizione(Map<Integer, String> orariSpedizione) {
        this.orariSpedizione = orariSpedizione;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Collection getListCodiciArticoli() throws DataAccessException {
        return listCodiciArticoli;
    }

    public void setListCodiciArticoli(Collection listCodiciArticoli) {
        this.listCodiciArticoli = listCodiciArticoli;
    }

    public String getStatistiche() throws DataAccessException {
        StatisticheVendite sv = new StatisticheVendite();

        statSettimana = sv.calcolaArtVendutiSettimana();
        statSettimanaCliente = sv.calcolaArtVendutiSettimana(ordine.getIdCliente(), idPuntoConsegna);

        statMese = sv.calcolaArtVendutiMese();
        statMeseCliente = sv.calcolaArtVendutiMese(ordine.getIdCliente(), idPuntoConsegna);

        return SUCCESS;
    }

    public Collection getStatSettimanaCliente() {
        return statSettimanaCliente;
    }

    public void setStatSettimanaCliente(Collection statSettimanaCliente) {
        this.statSettimanaCliente = statSettimanaCliente;
    }

    public Collection getStatSettimana() {
        return statSettimana;
    }

    public void setStatSettimana(Collection statSettimana) {
        this.statSettimana = statSettimana;
    }

    public Collection getStatMeseCliente() {
        return statMeseCliente;
    }

    public void setStatMeseCliente(Collection statMeseCliente) {
        this.statMeseCliente = statMeseCliente;
    }

    public Collection getStatMese() {
        return statMese;
    }

    public void setStatMese(Collection statMese) {
        this.statMese = statMese;
    }

    public String getFilterDeleteOrdini() {
        return filterDeleteOrdini;
    }

    public void setFilterDeleteOrdini(String filterDeleteOrdini) {
        this.filterDeleteOrdini = filterDeleteOrdini;
    }
}