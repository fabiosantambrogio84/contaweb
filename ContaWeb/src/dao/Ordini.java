package dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;

import utils.StatoOrdini;
import vo.Articolo;
import vo.DettaglioDDT;
import vo.DettaglioOrdine;
import vo.EvasioneOrdine;
import vo.Ordine;
import vo.VOElement;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Ordini extends DataAccessObject {

    private static final long serialVersionUID = 5556507389970934071L;

    private Boolean warning = false;
    private Integer idCliente = null;

    private String stato = null;
    private Integer statoInt = null;

    public Ordini() {
        this.elementClass = Ordine.class;
    }

    public Boolean getWarning() {
        return warning;
    }

    public void setWarning(Boolean warning) {
        this.warning = warning;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
        this.setStatoInt(StatoOrdini.getOutputFromLabel(stato));
    }

    public Integer getStatoInt() {
        return statoInt != null ? statoInt : -1;
    }

    public void setStatoInt(Integer statoInt) {
        this.statoInt = statoInt;
    }

    @Override
    public String store(VOElement element) throws DataAccessException {
        // convert date
        String result = ERROR;
        Ordine ordine = (Ordine) element;
        ordine.setDataCreazione(new java.sql.Date(ordine.getDataCreazione().getTime()));
        ordine.setDataSpedizione(new java.sql.Date(ordine.getDataSpedizione().getTime()));

        if (ordine.getId() != null) {
            Map<Integer, DettaglioOrdine> dettagliAttivi = new HashMap<Integer, DettaglioOrdine>();
            Iterator<?> itr = ordine.getDettagliOrdine().iterator();
            while (itr.hasNext()) {
                DettaglioOrdine det = (DettaglioOrdine) itr.next();
                dettagliAttivi.put(det.getIdArticolo().intValue(), det);
            }

            // cancella dettagli non sono pi� presenti o aggiorna gli altri
            Criteria criteria = new Criteria();
            criteria.addEqualTo("idOrdine", ordine.getId());
            Query query = QueryFactory.newQuery(DettaglioOrdine.class, criteria);
            PersistenceBroker broker = null;
            boolean nuovo = true;
            boolean completo = true;
            try {
                broker = PersistenceBrokerFactory.defaultPersistenceBroker();
                broker.beginTransaction();
                Collection<?> righe = broker.getCollectionByQuery(query);
                itr = righe.iterator();
                while (itr.hasNext()) {
                    DettaglioOrdine det = (DettaglioOrdine) itr.next();
                    if (!dettagliAttivi.containsKey(det.getIdArticolo().intValue())) { // CANCELLO ID VECCHI
                        broker.delete(det);
                    } else { // AGGIORNO ID, PEZZI DA EVADERE E PEZZI DA ORDINARE
                        DettaglioOrdine detAttivo = dettagliAttivi.get(det.getIdArticolo().intValue());
                        detAttivo.setId(det.getId());
                        detAttivo.setIdOrdine(ordine.getId());
                        Articolo articolo = new Articolo();
                        articolo.setId(detAttivo.getIdArticolo().intValue());
                        detAttivo.setArticolo((Articolo) find(articolo, broker));
                        int differenza = detAttivo.getPezziOrdinati() - det.getPezziOrdinati();
                        detAttivo.setPezziDaEvadere(det.getPezziDaEvadere() + differenza);
                        detAttivo.setPezziDaOrdinare(det.getPezziDaOrdinare() + differenza);

                        // se ho già evaso una riga anche solo parzialmente...
                        if (detAttivo.getPezziOrdinati() > detAttivo.getPezziDaEvadere())
                            nuovo = false;
                        // se una riga non è evasa completamente non è completo..
                        if (detAttivo.getPezziDaEvadere() > 0)
                            completo = false;
                    }
                }

                if (nuovo)
                    ordine.setStatoOrdine(Ordine.DA_EVADERE);
                else if (completo)
                    ordine.setStatoOrdine(Ordine.EVASO);
                else
                    ordine.setStatoOrdine(Ordine.PARZIALMENTE_EVASO);

                broker.store(ordine);
                broker.commitTransaction();
                // ToDo Storing verification
                result = SUCCESS;
            } catch (Exception e) {
                broker.abortTransaction();
                throw new DataAccessException(e.getMessage());
            } finally {
                if (broker != null)
                    broker.close();
            }
        } else {
            PersistenceBroker broker = null;
            try {
                Calendar cal = Calendar.getInstance();
                ordine.setAnnoContabile(cal.get(Calendar.YEAR));

                broker = PersistenceBrokerFactory.defaultPersistenceBroker();
                broker.beginTransaction();

                Criteria crit = new Criteria();
                crit.addEqualTo("annoContabile", ordine.getAnnoContabile());
                ReportQueryByCriteria q = QueryFactory.newReportQuery(Ordine.class, new String[] { "max(numeroProgressivo)" }, crit,
                        true);
                Iterator<?> iter = broker.getReportQueryIteratorByQuery(q);
                Object[] obj = (Object[]) iter.next();
                Integer n = 0;
                if (obj[0] != null)
                    n = (Integer) obj[0];
                ordine.setNumeroProgressivo(++n);
                ordine.setStatoOrdine(Ordine.DA_EVADERE);
                broker.store(ordine);

                broker.commitTransaction();
                result = SUCCESS;
            } catch (Exception e) {
                broker.abortTransaction();
                throw new DataAccessException(e.getMessage());
            } finally {
                if (broker != null)
                    broker.close();
            }
        }
        return result;
    }

    public Collection<DettaglioOrdine> getDettagliPerCliente(Integer idCliente, Integer idPuntoConsegna, Date dataConsegna)
            throws DataAccessException {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("ordine.idCliente", idCliente);
        if (idPuntoConsegna != null)
            criteria.addEqualTo("ordine.idPuntoConsegna", idPuntoConsegna);
        criteria.addLessOrEqualThan("ordine.dataSpedizione", dataConsegna);
        criteria.addGreaterThan("pezziDaEvadere", 0);
        Collection righe = null;
        PersistenceBroker broker = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            Query query = QueryFactory.newQuery(DettaglioOrdine.class, criteria);
            righe = broker.getCollectionByQuery(query);
            Iterator<?> itr = righe.iterator();
            while (itr.hasNext()) {
                DettaglioOrdine det = (DettaglioOrdine) itr.next();
                completeReferences(det);
            }
        } finally {
            if (broker != null)
                broker.close();
        }
        return righe;
    }

    public Collection getDettagliAncoraDaEvadere(DettaglioDDT dt) throws DataAccessException {
        Collection col = new ArrayList<EvasioneOrdine>();

        Iterator<EvasioneOrdine> itrOld = dt.getEvasioniOrdini().iterator();
        Set<Integer> set = new HashSet<Integer>();
        while (itrOld.hasNext()) {
            EvasioneOrdine eo = itrOld.next();
            set.add(eo.getIdDettaglioOrdine());
        }

        Collection ordiniNuovi = getDettagliPerCliente(dt.getDdt().getIdCliente(), dt.getDdt().getIdPuntoConsegna(),
                dt.getDdt().getDataTrasporto());
        Iterator<DettaglioOrdine> itr = ordiniNuovi.iterator();
        while (itr.hasNext()) {
            DettaglioOrdine det = itr.next();
            if (det.getIdArticolo().intValue() == dt.getIdArticolo().intValue() && !set.contains(det.getId())) {
                EvasioneOrdine eo = new EvasioneOrdine();
                eo.setIdDettaglioOrdine(det.getId());
                eo.setDettaglioOrdine(det);
                col.add(eo);
            }
        }

        return col;
    }

    public void aggiornaEvasioni(int pezziDaAggiungere, DettaglioDDT dt, PersistenceBroker broker) throws DataAccessException {
        Iterator<?> itr2 = dt.getEvasioniOrdini().iterator();
        while (itr2.hasNext() && pezziDaAggiungere != 0) {

            broker.beginTransaction();

            EvasioneOrdine eo = (EvasioneOrdine) itr2.next();
            eo.setIdDettaglioDDT(dt.getId());
            eo.setDettaglioDDT(dt);
            DettaglioOrdine det = eo.getDettaglioOrdine();
            det.setIdOrdine(eo.getDettaglioOrdine().getIdOrdine());
            Ordine ordine = new Ordine();
            ordine.setId(det.getIdOrdine());
            ordine = (Ordine) new Ordini().find(ordine);
            det.setOrdine(ordine);
            if (pezziDaAggiungere >= det.getPezziDaEvadere()) {
                pezziDaAggiungere -= det.getPezziDaEvadere();
                det.setPezziDaEvadere(0);
            } else {
                if ((det.getPezziDaEvadere() - pezziDaAggiungere) > det.getPezziOrdinati()) {
                    pezziDaAggiungere += det.getPezziOrdinati() - det.getPezziDaEvadere();
                    det.setPezziDaEvadere(det.getPezziOrdinati());
                } else {
                    det.setPezziDaEvadere(det.getPezziDaEvadere() - pezziDaAggiungere);
                    pezziDaAggiungere = 0;
                }
            }
            if (ordine != null)
                store(ordine);
            broker.store(det);
            broker.store(eo);
            broker.commitTransaction();
        }

    }

    public void stornaEvasioni(int pezziDaTogliere, DettaglioDDT dt, PersistenceBroker broker) throws DataAccessException {
        Iterator<?> itr2 = dt.getEvasioniOrdini().iterator();
        while (itr2.hasNext() && pezziDaTogliere != 0) {
            EvasioneOrdine eo = (EvasioneOrdine) itr2.next();
            eo.setIdDettaglioDDT(dt.getId());
            eo.setDettaglioDDT(dt);
            DettaglioOrdine det = eo.getDettaglioOrdine();
            det.setIdOrdine(eo.getDettaglioOrdine().getIdOrdine());
            Ordine ordine = new Ordine();
            ordine.setId(det.getIdOrdine());
            det.setOrdine((Ordine) new Ordini().find(ordine));
            if (pezziDaTogliere + det.getPezziDaEvadere() >= det.getPezziOrdinati()) {
                pezziDaTogliere -= (det.getPezziOrdinati() - det.getPezziDaEvadere());
                det.setPezziDaEvadere(det.getPezziOrdinati());
            } else {
                det.setPezziDaEvadere(det.getPezziDaEvadere() + pezziDaTogliere);
                pezziDaTogliere = 0;
            }
            broker.store(det);
            broker.store(eo);
        }
    }

    public Collection<Ordine> getListaOrdini() throws DataAccessException {
        getQueryByCriteria().addOrderByDescending("pezziDaEvadere");
        getQueryByCriteria().addOrderByAscending("dataCreazione");
        return getElements();
    }

    /*
     * *** public Collection setOrderByDaEvadere(int order) throws DataAccessException { if (order == ORDER_DESC) {
     * getQueryByCriteria().addOrderByDescending("dataCreazione"); } else { getQueryByCriteria().addOrderByAscending("dataCreazione");
     * }
     * 
     * Collection<Ordine> warning_obj = new ArrayList<Ordine>();
     * 
     * Collection<Ordine> normal_obj = new ArrayList<Ordine>();
     * 
     * Collection<Ordine> list_obj = getElements();
     * 
     * Iterator<Ordine> itr = list_obj.iterator();
     * 
     * while(itr.hasNext()) {
     * 
     * Ordine element = itr.next();
     * 
     * if(element.getStato() == 1) { warning_obj.add(element); } else { normal_obj.add(element); }
     * 
     * }
     * 
     * warning_obj.addAll(normal_obj);
     * 
     * return warning_obj; }
     */

    public Collection<Ordine> getListaOrdiniByAutistaDate(Integer idAutista, String data) throws DataAccessException {
        getCriteria().addGreaterOrEqualThan("dataSpedizione", data);
        getCriteria().addEqualTo("idAutista", idAutista);
        return getElements();
    }

    public Collection<Ordine> getListaOrdiniperautistadata(Integer idAutista) throws DataAccessException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ITALIAN);
        Calendar cal = Calendar.getInstance(Locale.ITALY);

        getCriteria().addGreaterOrEqualThan("dataSpedizione", dateFormat.format(cal.getTime()));
        getCriteria().addEqualTo("idAutista", idAutista);
        return getElements();

    }

    @Override
    public void setDefaultCriteria() {
        /*
         * *** getQueryByCriteria().addOrderByDescending("id");
         */
        QueryByCriteria query = getQueryByCriteria();
        query.addOrderByAscending("statoOrdine");
        query.addOrderByDescending("id");

        Criteria criteria = query.getCriteria();

        Integer statoInt = getStatoInt();
        if (statoInt != -1) {
            criteria.addEqualTo("statoOrdine", statoInt);
        }

        // if(this.warning){
        // List<Integer> list = new ArrayList<Integer>();
        // list.add(0);
        // list.add(1);
        // criteria.addIn("statoOrdine", list);
        // }

        if (getIdCliente() != null) {
            criteria.addEqualTo("cliente.id", idCliente);
        }
    }

}
