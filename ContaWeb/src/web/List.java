package web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.opensymphony.xwork2.ActionContext;

import dao.Agenti;
import dao.Articoli;
import dao.Autisti;
import dao.BolleAcquisto;
import dao.CategorieArticolo;
import dao.Clienti;
import dao.DDTs;
import dao.DataAccessException;
import dao.DataAccessObject;
import dao.DestinazioniArticolo;
import dao.Fatture;
import dao.Fornitori;
import dao.Giacenze;
import dao.Ivas;
import dao.Listini;
import dao.NoteAccredito;
import dao.Ordini;
import dao.OrdiniFornitori;
import dao.Pagamenti;
import dao.PagamentiEseguiti;
import dao.Sconti;
import dao.Settings;
import utils.DeleteOrdini;
import utils.StatoOrdini;
import vo.Autista;
import vo.CategoriaArticolo;
import vo.DDT;
import vo.Fattura;
import vo.Fornitore;
import vo.Listino;
import vo.Pagamento;

@SuppressWarnings("rawtypes")
public class List extends GenericAction {

    private static final long serialVersionUID = 1L;

    private Collection datiTabella = null;
    private int pageCount = 0;
    private Integer itemCount = 0;
    private Integer pagina = null;
    private int LIST_ELEMS = 0;
    private String filterKey = null;
    private String filterStato = null;
    private String currentAction = null;
    protected Date filterDataDa = null;
    protected Date filterDataA = null;
    protected String filterPagamento = null;
    protected String filterArticolo = null;
    protected String filterAutista = null;
    private Integer filterCliente = null;
    private Integer filterFornitore = null;
    private Integer filterListino = null;
    private Collection listClienti = null;
    protected String filterImporto = null;
    private String filterAttivo = null;
    private BigDecimal totaleAcconto = new BigDecimal(0);
    private BigDecimal totaleGuadagno = new BigDecimal(0);
    private BigDecimal totaleImporto = new BigDecimal(0);
    private BigDecimal totaleImponibile = new BigDecimal(0);
    private BigDecimal totaleCosto = new BigDecimal(0);
    private Object id;
    private Integer idAutista;
    private Boolean warning = false;

    // private Collection pagamenti;
    private Collection<Autista> listAutori;
    private Collection<CategoriaArticolo> listCategorie;
    private Collection<Fornitore> listFornitori;
    private Collection<Listino> listListini;
    private Collection<Pagamento> pagamenti;
    private Collection<String> listAttivo;
    private String filterStatoOrdini = null;
    private Collection<?> statoOrdini = null;
    private Collection<?> deleteOrdini = null;
    private Collection<?> listClientiNoBloccaDdt = null;

	public List() {
        this.LIST_ELEMS = Integer.valueOf(Settings.getInstance().getValue("list.elementi")); // .intValue();
        this.statoOrdini = StatoOrdini.list();
        this.setDeleteOrdini(DeleteOrdini.list());
        
        this.listAttivo = new ArrayList<String>();
        this.listAttivo.add("si");
        this.listAttivo.add("no");
        
    }

    public String getFilterArticolo() {
        return filterArticolo;
    }

    public void setFilterArticolo(String filterArticolo) {
        this.filterArticolo = filterArticolo;
    }

    public String getFilterAutista() {
        return filterAutista;
    }

    public void setFilterAutista(String filterAutista) {
        this.filterAutista = filterAutista;
    }

    public String getFilterImporto() {
        return filterImporto;
    }

    public void setFilterImporto(String filterImporto) {
        this.filterImporto = filterImporto;
    }

    public String getFilterAttivo() {
        return filterAttivo;
    }

    public void setFilterAttivo(String filterAttivo) {
        this.filterAttivo = filterAttivo;
    }
    
    @SuppressWarnings("unchecked")
    public Collection<Autista> getListAutisti() {
        if (listAutori == null) {
            try {
                listAutori = new Autisti().getElements();
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
        return listAutori;
    }

    public Collection<Pagamento> getPagamenti() {
        if (pagamenti == null) {
            PagamentiEseguiti pe = new PagamentiEseguiti();
            pe.setFilterDataDa(this.getFilterDataDa());
            pe.setFilterDataA(this.getFilterDataA());
            pe.setFilterKey(this.getFilterKey());
            pe.setFilterPagamento(this.getFilterPagamento());
            pe.setPagamenti();
            pagamenti = pe.getPagamenti();
        }
        return pagamenti;
    }

    @SuppressWarnings("unchecked")
    public Collection<CategoriaArticolo> getListCategorie() {
        if (listCategorie == null) {
            try {
                listCategorie = new CategorieArticolo().getElements();
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
        return listCategorie;
    }

    @SuppressWarnings("unchecked")
    public Collection<Fornitore> getListFornitori() {
        if (listFornitori == null) {
            try {
                Fornitori dao = new Fornitori();
                dao.setOrderByDescrizione(Fornitori.ORDER_ASC);
                listFornitori = dao.getElements();
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
        return listFornitori;
    }

    @SuppressWarnings("unchecked")
    public Collection<Listino> getListListini() {
        if (listListini == null) {
            try {
                Listini dao = new Listini();
                dao.setOrderByDescrizione(Listini.ORDER_ASC);
                listListini = dao.getElements();
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
        return listListini;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

    public Integer getIdAutista() {
        return idAutista;
    }

    public void setIdAutista(Integer idAutista) {
        this.idAutista = idAutista;
    }

    public void setListAutisti(Collection<Autista> listAutisti) {
        this.listAutori = listAutisti;
    }

    public void setListPagamenti(Collection<Pagamento> pagamenti) {
        this.pagamenti = pagamenti;
    }

    public Object getId() {
        return this.id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Collection getDatiTabella() {
        return this.datiTabella;
    }

    public void setDatiTabella(Collection datiTabella) {
        this.datiTabella = datiTabella;
    }

    public Collection getListClienti() {
        if (listClienti == null) {
            try {
                Clienti dao = new Clienti();
                dao.setOrderByDescrizione(Clienti.ORDER_ASC);
                listClienti = dao.getElements();
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

    @SuppressWarnings({ "unchecked" })
    private Collection getElements(DataAccessObject obj) throws Exception {
        itemCount = obj.getCount();
        pageCount = (int) Math.ceil(itemCount / this.LIST_ELEMS) + 1;

        if (this.pagina == null)
            this.pagina = 1;
        if (this.pagina <= 0)
            this.pagina = 1;
        if (this.pagina > pageCount)
            this.pagina = pageCount;
        int startRow = (this.pagina - 1) * this.LIST_ELEMS + 1;
        int endRow = startRow + this.LIST_ELEMS;
        obj.setPaginazione(startRow, endRow);

        Collection collection = obj.getElements();

        if (currentAction.equalsIgnoreCase("ddtList") || currentAction.equalsIgnoreCase("fattureList")) {
            if (this.filterImporto != null && this.filterImporto.length() > 0) {
                collection.removeAll(collection);
                Collection tmpCollection = obj.getElements();
                Iterator tmpItr;
                DDT tmpDDT;
                Fattura tmpFattura;
                BigDecimal tmpImporto;
                BigDecimal tmpTotale = BigDecimal.ZERO;
                Integer tmpElementInResult = 0;

                while (tmpElementInResult < this.LIST_ELEMS && startRow < (itemCount - 1)) {
                    tmpItr = tmpCollection.iterator();
                    while (tmpItr.hasNext()) {
                        tmpImporto = new BigDecimal(filterImporto.replaceAll(",", "."));
                        if (currentAction.equalsIgnoreCase("ddtList")) {
                            tmpDDT = (DDT) tmpItr.next();
                            tmpTotale = tmpDDT.calcolaTotale();
                        } else if (currentAction.equalsIgnoreCase("fattureList")) {
                            tmpFattura = (Fattura) tmpItr.next();
                            // tmpFattura.calcolaTotali();
                            tmpTotale = tmpFattura.getTotaleFattura();
                        }
                        // if (!tmpTotale.equals(tmpImporto)) // Double.parseDouble(filterImporto))
                        if (tmpTotale.doubleValue() != tmpImporto.doubleValue()) // Double.parseDouble(filterImporto))
                            tmpItr.remove();
                    }
                    tmpElementInResult = tmpElementInResult + tmpCollection.size();
                    if (tmpElementInResult <= this.LIST_ELEMS) {
                        collection.addAll(tmpCollection);
                        startRow = endRow;
                        endRow = startRow + this.LIST_ELEMS - tmpCollection.size();
                        if (endRow >= itemCount)
                            endRow = itemCount - 1;
                        if (startRow < endRow) {
                            obj.setPaginazione(startRow, endRow);
                            tmpCollection = obj.getElements();
                        }
                    }
                }
            }
            calcolaTotali(collection);
        }
        return collection;
    }

    private int calcolaTotali(Collection collection) {
        int tmpReturn = 1;
        totaleAcconto = new BigDecimal(0);
        totaleImporto = new BigDecimal(0);
        totaleImponibile = new BigDecimal(0);
        totaleCosto = new BigDecimal(0);
        totaleGuadagno = new BigDecimal(0);
        if (currentAction.equalsIgnoreCase("ddtList") && !collection.isEmpty()) {
            Iterator tmpItr;
            DDT tmpDDT;
            // BigDecimal tmpImporto;
            // BigDecimal tmpTotale = BigDecimal.ZERO;
            // Integer tmpElementInResult = 0;
            tmpItr = collection.iterator();
            while (tmpItr.hasNext()) {
                tmpDDT = (DDT) tmpItr.next();
                tmpDDT.calcolaTotale();
                totaleAcconto = totaleAcconto.add(tmpDDT.getAcconto());
                totaleImponibile = totaleImponibile.add(tmpDDT.getTotaleImponibile());
                totaleImporto = totaleImporto.add(tmpDDT.getTotaleImponibile()).add(tmpDDT.getTotaleImposta());
                totaleCosto = totaleCosto.add(tmpDDT.getTotaleCosto());
                totaleGuadagno = totaleGuadagno.add(tmpDDT.getGuadagno());
            }
            tmpReturn = 0;
        } else
            tmpReturn = 0;
        return tmpReturn;
    }

    public String getFilterKey() {
        return this.filterKey;
    }

    public void setFilterKey(String filterKey) {
        this.filterKey = filterKey;
    }

    public Date getFilterDataDa() {
        return filterDataDa;
    }

    public Boolean getWarning() {
        return warning;
    }

    public void setWarning(Boolean warning) {
        this.warning = warning;
    }

    public void setFilterDataDa(Date filterDataDa) {
        this.filterDataDa = filterDataDa;
    }

    public Date getFilterDataA() {
        return filterDataA;
    }

    public void setFilterDataA(Date filterDataA) {
        this.filterDataA = filterDataA;
    }

    public String getFilterPagamento() {
        return filterPagamento;
    }

    public void setFilterPagamento(String filterPagamento) {
        this.filterPagamento = filterPagamento;
    }

    public String getFilterStato() {
        return filterStato;
    }

    public void setFilterStato(String filterStato) {
        this.filterStato = filterStato;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * @return the totaleAcconto
     */
    public BigDecimal getTotaleAcconto() {
        return totaleAcconto;
    }

    /**
     * @param totaleAcconto
     *            the totaleAcconto to set
     */
    public void setTotaleAcconto(BigDecimal totaleAcconto) {
        this.totaleAcconto = totaleAcconto;
    }

    /**
     * @return the totaleImporto
     */
    public BigDecimal getTotaleImporto() {
        return totaleImporto;
    }

    /**
     * @param totaleImporto
     *            the totaleImporto to set
     */
    public void setTotaleImporto(BigDecimal totaleImporto) {
        this.totaleImporto = totaleImporto;
    }

    /**
     * @return the totaleImponibile
     */
    public BigDecimal getTotaleImponibile() {
        return totaleImponibile;
    }

    /**
     * @param totaleImponibile
     *            the totaleImponibile to set
     */
    public void setTotaleImponibile(BigDecimal totaleImponibile) {
        this.totaleImponibile = totaleImponibile;
    }

    /**
     * @return the totaleCosto
     */
    public BigDecimal getTotaleCosto() {
        return totaleCosto;
    }

    /**
     * @param totaleCosto
     *            the totaleCosto to set
     */
    public void setTotaleCosto(BigDecimal totaleCosto) {
        this.totaleCosto = totaleCosto;
    }

    /**
     * @return the totaleGuadagno
     */
    public BigDecimal getTotaleGuadagno() {
        return totaleGuadagno;
    }

    /**
     * @param totaleGuadagno
     *            the totaleGuadagno to set
     */
    public void setTotaleGuadagno(BigDecimal totaleGuadagno) {
        this.totaleGuadagno = totaleGuadagno;
    }

    public Integer getFilterCliente() {
        return filterCliente;
    }

    public void setFilterCliente(Integer filterCliente) {
        this.filterCliente = filterCliente;
    }

    public Integer getFilterListino() {
        return filterListino;
    }

    public void setFilterListino(Integer filterListino) {
        this.filterListino = filterListino;
    }

    public Integer getFilterFornitore() {
        return filterFornitore;
    }

    public void setFilterFornitore(Integer filterFornitore) {
        this.filterFornitore = filterFornitore;
    }

    public String getFilterStatoOrdini() {
        return filterStatoOrdini;
    }

    public void setFilterStatoOrdini(String filterStatoOrdini) {
        this.filterStatoOrdini = filterStatoOrdini;
    }

    public Collection getStatoOrdini() {
        return statoOrdini;
    }

    public void setStatoOrdini(Collection statoOrdini) {
        this.statoOrdini = statoOrdini;
    }

    public Collection<?> getDeleteOrdini() {
        return deleteOrdini;
    }

    public void setDeleteOrdini(Collection<?> deleteOrdini) {
        this.deleteOrdini = deleteOrdini;
    }

    @Override
    public String execute() throws Exception {
        currentAction = ActionContext.getContext().getName();
        Collection collection_obj = null;
        DataAccessObject obj = null;

        try {
            if (currentAction.equalsIgnoreCase("fornitoriList")) {
                obj = new Fornitori();
            }
            if (currentAction.equalsIgnoreCase("articoliList")) {
                obj = new Articoli();
                ((Articoli) obj).setOrderByDescrizione(DataAccessObject.ORDER_ASC);
            }
            if (currentAction.equalsIgnoreCase("clientiList")) {
                obj = new Clienti();
                // ((Clienti)obj).setOrderByDescrizione(Clienti.ORDER_ASC);
                if (filterFornitore != null || filterListino != null)
                    ((Clienti) obj).setFilterByListino(filterFornitore, filterListino);
            }
            if (currentAction.equalsIgnoreCase("noteAccreditoList")) {
                obj = new NoteAccredito();
                ((NoteAccredito) obj).setFilterDataDa(this.getFilterDataDa());
                ((NoteAccredito) obj).setFilterDataA(this.getFilterDataA());
                ((NoteAccredito) obj).setFilterStato(this.getFilterStato());
                ((NoteAccredito) obj).setFilterPagamento(this.getFilterPagamento());
                ((NoteAccredito) obj).setFilterArticolo(this.getFilterArticolo());
            }
            if (currentAction.equalsIgnoreCase("ddtList")) {
                obj = new DDTs();
                ((DDTs) obj).setFilterDataDa(this.getFilterDataDa());
                ((DDTs) obj).setFilterDataA(this.getFilterDataA());
                ((DDTs) obj).setFilterStato(this.getFilterStato());
                ((DDTs) obj).setFilterPagamento(this.getFilterPagamento());
                ((DDTs) obj).setFilterArticolo(this.getFilterArticolo());
                ((DDTs) obj).setFilterAutista(this.getFilterAutista());
                ((DDTs) obj).setFilterImporto(this.getFilterImporto());
            }
            if (currentAction.equalsIgnoreCase("pagamentiList")) {
                obj = new Pagamenti();
            }
            if (currentAction.equalsIgnoreCase("scontiList")) {
                obj = new Sconti();
            }
            if (currentAction.equalsIgnoreCase("fattureList")) {
                obj = new Fatture();
                ((Fatture) obj).setFilterDataDa(this.getFilterDataDa());
                ((Fatture) obj).setFilterDataA(this.getFilterDataA());
                ((Fatture) obj).setFilterStato(this.getFilterStato());
                ((Fatture) obj).setFilterPagamento(this.getFilterPagamento());
                ((Fatture) obj).setFilterArticolo(this.getFilterArticolo());
                ((Fatture) obj).setFilterImporto(this.getFilterImporto());
            }
            if (currentAction.equalsIgnoreCase("pagamentiEseguitiList")) {
                obj = new PagamentiEseguiti();
                ((PagamentiEseguiti) obj).setFilterDataDa(this.getFilterDataDa());
                ((PagamentiEseguiti) obj).setFilterDataA(this.getFilterDataA());
                ((PagamentiEseguiti) obj).setFilterPagamento(this.getFilterPagamento());
                ((PagamentiEseguiti) obj).setPagamenti();
            }
            if (currentAction.equalsIgnoreCase("giacenzeList")) {
                obj = new Giacenze();
                if (filterAttivo != null && !filterAttivo.equals(""))
                    ((Giacenze) obj).setFilterByAttivo(filterAttivo);
            }
            if (currentAction.equalsIgnoreCase("bolleAcquistoList")) {
                obj = new BolleAcquisto();
            }
            if (currentAction.equalsIgnoreCase("ordiniList")) {
                obj = new Ordini();
                ((Ordini) obj).setStato(filterStatoOrdini);
                ((Ordini) obj).setWarning(warning);
                if (getFilterCliente() != null)
                    ((Ordini) obj).setIdCliente(getFilterCliente());
            }
            if (currentAction.equalsIgnoreCase("ordiniAutistaList")) {
                obj = new Ordini();
            }
            if (currentAction.equalsIgnoreCase("ordiniFornitoriList")) {
                obj = new OrdiniFornitori();
            }
            if (currentAction.equalsIgnoreCase("autistiList")) {
                obj = new Autisti();
            }
            if (currentAction.equalsIgnoreCase("agentiList")) {
                obj = new Agenti();
            }
            if (currentAction.equalsIgnoreCase("ivaList")) {
                obj = new Ivas();
            }
            if (currentAction.equalsIgnoreCase("categorieArticoliList")) {
                obj = new CategorieArticolo();
            }
            if (currentAction.equalsIgnoreCase("destinazioniArticoliList")) {
                obj = new DestinazioniArticolo();
            }
            if (currentAction.equalsIgnoreCase("listiniList")) {
                obj = new Listini();
            }
            if (this.filterKey != null) {
                obj.setFilterKey(this.filterKey);
            }
            if (collection_obj == null) {
                collection_obj = getElements(obj);
            }

            /*
             * if(this.warning) { if (obj instanceof Ordini) { Collection status_1 = new ArrayList();
             * 
             * Iterator its = collection_obj.iterator(); while(its.hasNext()) { Ordine ord = (Ordine)its.next(); if(ord.getStato() ==
             * 1) { status_1.add(ord); }
             * 
             * } collection_obj = status_1; } }
             */

            setDatiTabella(collection_obj);
        } catch (Exception e) {
            stampaErrore("List.execute()", e);
            return "error";
        }
        return "success";
    }

    public Collection<?> getListClientiNoBloccaDdt() {
    	if (listClientiNoBloccaDdt == null) {
            try {
                Clienti dao = new Clienti();
                dao.setFilterByBloccaDDT();
                dao.setOrderByDescrizione(Clienti.ORDER_ASC);
                listClientiNoBloccaDdt = dao.getElements();
            } catch (Exception e) {
                stampaMessaggio("EditOrdine.getListClientiNoBloccaDdt()", e);
                return null;
            }
        }
        return listClientiNoBloccaDdt;
	}

	public void setListClientiNoBloccaDdt(Collection<?> listClientiNoBloccaDdt) {
		this.listClientiNoBloccaDdt = listClientiNoBloccaDdt;
	}

	public Collection<String> getListAttivo(){
		return listAttivo;
	}	
}
