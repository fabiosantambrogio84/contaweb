package forms;

import java.math.BigDecimal;
import java.util.Collection;

import dao.Articoli;
import dao.DataAccessException;
import dao.Listini;
import dao.Prezzi;
import vo.Articolo;
import vo.Listino;
import vo.Prezzo;

public class EditListino extends Edit {

    private static final long serialVersionUID = -2343419772046019464L;

    private Integer id = null;

    private Listino listino = null;

    private double aumento = 0;

    private String tipo = null;

    private Integer idFornitore = null;

    private Integer idCategoria = null;

    private Collection<Listino> listListini;

    @Override
    protected String store() {
        try {
            Listini listini = new Listini();
            Integer idListinoRef = listino.getIdListinoRef();
            if (idListinoRef != null) {
                Listino listinoRef = listini.find(idListinoRef);
                listino.setListinoRef(listinoRef);
            }
            /* salvo il nuovo listino */
            listini.store(listino);

            /* recupero il listino appena creato (qui ho l'id) */
            Listino newListino = (Listino) listini.find(listino);

            if (idListinoRef != null) {
                /* seleziono i prezzi del listino di riferimento */
                Collection<Prezzo> prezziRif = listini.getPrezziByListino(idListinoRef);
                for (Prezzo prezzoRif : prezziRif) {
                    prezzoRif.setIdListino(newListino.getId());
                    prezzoRif.setListino(newListino);
                }

                /* elimino gli eventuali prezzi già settati */
                Prezzi prezzi = new Prezzi();
                prezzi.deletePrezziByListino(newListino.getId());

                /* inserisco i prezzi per il listino */
                prezzi.insertPrezzi(prezziRif);
            }
        } catch (Exception e) {
            return ERROR;
        }
        return SUCCESS;
    }

    public String varia() {

        if (aumento != 0) {
            // TODO Auto-generated method stub
            try {

                Collection<Articolo> articoli = null;
                if (getIdFornitore() != null && getIdCategoria() != null)
                    articoli = new Articoli().getListaArticoliAttivi(getIdFornitore(), getIdCategoria());
                else if (getIdFornitore() != null && getIdCategoria() == null)
                    articoli = new Articoli().getListaArticoliFornitore(getIdFornitore());
                else if (getIdFornitore() == null && getIdCategoria() != null)
                    articoli = new Articoli().getListaArticoliCategoria(getIdCategoria());
                else
                    articoli = new Articoli().getListaArticoliAttivi();

                BigDecimal diff = new BigDecimal(aumento);

                for (Articolo articolo : articoli) {
                    new Articoli().completeReferences(articolo);
                    if (articolo.getPrezzi() == null)
                        continue;
                    for (Object obj : articolo.getPrezzi()) {
                        Prezzo prezzo = (Prezzo) obj;
                        if (prezzo.getIdListino() != id)
                            continue;

                        BigDecimal pz = prezzo.getPrezzo();

                        if ("0".equals(tipo))
                            pz = pz.add(diff).setScale(2, 4).setScale(2, 0);
                        else {

                            BigDecimal sconto = pz.multiply(diff).divide(new BigDecimal(100)).setScale(2, 4).setScale(2, 0);
                            pz = pz.add(sconto);
                        }

                        prezzo.setPrezzo(pz);

                        break;
                    }

                    new Articoli().store(articolo);
                }

            } catch (Exception e) {
                // TODO: handle exception
                return ERROR;
            }
        }

        return SUCCESS;
    }

    @Override
    protected String delete() {
        // TODO Auto-generated method stub
        try {
            // Collection clienti = new Clienti().getFiltratoAgente(id);
            // if (clienti.isEmpty()){
            Listino agente = new Listino();
            agente.setId(id);
            new Listini().delete(agente);
            // } else {
            // return ERROR_DELETE;
            // }
        } catch (Exception e) {
            stampaErrore("Edit Agente", e);
            // TODO: handle exception
            return ERROR_DELETE;
        }

        return SUCCESS;
    }

    @Override
    public String input() {
        try {
            if (getAction().equalsIgnoreCase("edit")) {
                Listini Agenti = new Listini();
                listino = Agenti.find(id);
            } else if (getAction().equalsIgnoreCase("delete")) {
                return delete();
            } else {
                listino = new Listino();
            }
        } catch (Exception e) {
            // TODO: handle exception
            return ERROR;
        }
        return INPUT;
    }

    public Listino getListino() {
        return listino;
    }

    public void setListino(Listino listino) {
        this.listino = listino;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public double getAumento() {
        return aumento;
    }

    public void setAumento(double aumento) {
        this.aumento = aumento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Integer getIdFornitore() {
        return idFornitore;
    }

    public void setIdFornitore(Integer idFornitore) {
        this.idFornitore = idFornitore;
    }

    public Collection<Listino> getListListini() {
        if (listListini == null) {
            try {
                Listini dao = new Listini();
                dao.setOrderByDescrizione(Listini.ORDER_ASC);
                listListini = dao.getListiniFiltrati(id);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
        return listListini;
    }

    public void setListListini(Collection<Listino> listListini) {
        this.listListini = listListini;
    }

}
