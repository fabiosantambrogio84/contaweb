package api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.Articoli;
import dao.Clienti;
import dao.DDTs;
import dao.Listini;
import dao.PuntiConsegna;
import dto.DDTActionViewModel;
import dto.DDTViewModel;
import dto.DettaglioDDTViewModel;
import formatters.Select2Result;
import resultset.ResultDto;
import vo.Articolo;
import vo.Cliente;
import vo.DDT;
import vo.DettaglioDDT;
import vo.PrezzoConSconto;
import vo.PuntoConsegna;

public class DdtApi {

    public JSONObject test() throws Exception {
        try {
            return new JSONObject().put("test", "test val");
        } catch (Exception ex) {

        }

        return null;
    }

    public JSONObject getCliente(int idCliente) throws Exception {
        try {
            Clienti clienti = new Clienti();
            Cliente cliente = clienti.find(idCliente);
            return new JSONObject(cliente);
        } catch (Exception ex) {

        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public JSONObject getClienteSelect2(String term) throws Exception {
        try {
            Collection<Cliente> clienti;
            Clienti clientiDao = new Clienti();
            if (term.equals("**"))
                clienti = (Collection<Cliente>) clientiDao.getListaPerDDT();
            else
                clienti = (Collection<Cliente>) clientiDao.getListaPerDDTFilter(term);

            Collection<Select2Result> ress = new ArrayList<Select2Result>();
            for (Cliente cli : clienti) {
                Select2Result res = new Select2Result();
                res.setId(cli.getId().toString());
                res.setText(cli.getRs());
                ress.add(res);
            }

            JSONObject obj = new JSONObject();
            obj.put("results", ress);

            return obj;
        } catch (Exception ex) {

        }

        return null;
    }

    public JSONObject getArticoliSelect2(String term) throws Exception {
        try {
            Collection<Articolo> arts;
            Articoli articoli = new Articoli();
            if (term.equals("**"))
                arts = articoli.getActiveElements();
            else
                arts = articoli.getListaArticoliFilter(term);

            Collection<Select2Result> ress = new ArrayList<Select2Result>();
            for (Articolo art : arts) {
                Select2Result res = new Select2Result();
                res.setId(art.getId().toString());
                res.setText(art.getDescCompleta());
                ress.add(res);
            }

            JSONObject obj = new JSONObject();
            obj.put("results", ress);

            return obj;
        } catch (Exception ex) {

        }

        return null;
    }

    public JSONObject getClienteDestinazione(int idCliente, int index) throws Exception {
        try {
            PuntiConsegna pcs = new PuntiConsegna(idCliente);
            PuntoConsegna pc = pcs.findPrimaryByCliente(index);
            return new JSONObject(pc);
        } catch (Exception ex) {

        }

        return null;
    }

    public JSONArray getClienteDestinazioni(int idCliente) throws Exception {
        try {
            PuntiConsegna pcs = new PuntiConsegna(idCliente);
            PuntoConsegna[] puntiConsegna = pcs.findByCliente();
            return new JSONArray(puntiConsegna);
        } catch (Exception ex) {
        }
        return null;
    }

    public JSONObject getArticolo(int idArticolo, int idCliente) throws Exception {
        try {
            Articoli arts = new Articoli();
            Articolo art = arts.find(idArticolo);
            Clienti clienti = new Clienti();
            Cliente cliente = clienti.find(idCliente);
            Listini listini = new Listini();
            Vector listiniAss = listini.getListiniAssociatiByCliente(cliente);
            cliente.setListiniAssociati(listiniAss);

            Calendar today = Calendar.getInstance();
            PrezzoConSconto prezzo = listini.getPrezzoConSconto(art, cliente, today.getTime());

            art.setPrezzoConSconto(prezzo.getPrezzo());

            return new JSONObject(art);
        } catch (Exception ex) {

        }

        return null;
    }

    public JSONObject saveDDT(DDTActionViewModel ddtViewModel) throws Exception {
        ResultDto res = new ResultDto();

        try {

            DDTs ddts = new DDTs();
            DDTViewModel ddtVM = ddtViewModel.getDdt();
            boolean isUpdate = ddtVM.getId() != null && !ddtVM.getId().equals("");
            DDT ddt = new DDT();
            Vector<DettaglioDDT> ddtDett = new Vector<DettaglioDDT>();
            if (isUpdate) {
                // modifica ddt

                ddt.setId(Integer.parseInt(ddtVM.getId()));
                ddt = (DDT) ddts.findWithAllReferences(ddt);
            }

            List<DettaglioDDTViewModel> dettVm = ddtViewModel.getArt();

            Vector<DettaglioDDT> dettagliToAdd = new Vector<DettaglioDDT>();
            Vector<DettaglioDDT> dettagliToRemove = new Vector<DettaglioDDT>();

            if (ddt.getDettagliDDT() != null) {
                // modifica ddt dettagli
                ddtDett = ddt.getDettagliDDT();

                for (DettaglioDDTViewModel dvm : dettVm) {

                    if (dvm.getId().equals("0")) {
                        DettaglioDDT dettNew = new DettaglioDDT();
                        dettNew = dvm.MapDettaglioDDT(dettNew);
                        dettagliToAdd.add(dettNew);

                        continue;
                    }

                    for (DettaglioDDT dett : ddtDett) {

                        if (dett.getId() == Integer.parseInt(dvm.getId())) {
                            dett = dvm.MapDettaglioDDT(dett);
                            break;
                        }
                    }
                }

                for (DettaglioDDT dett : ddtDett) {

                    boolean found = false;
                    for (DettaglioDDTViewModel dvm : dettVm) {

                        if (dett.getId() == Integer.parseInt(dvm.getId())) {

                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        dettagliToRemove.add(dett);
                    }
                }
            } else {
                // nuovo ddt dettagli
                for (DettaglioDDTViewModel dvm : dettVm) {

                    DettaglioDDT dettNew = new DettaglioDDT();
                    dettNew = dvm.MapDettaglioDDT(dettNew);
                    dettagliToAdd.add(dettNew);
                }
            }

            for (DettaglioDDT dettAdd : dettagliToAdd) {
                ddtDett.add(dettAdd);
            }

            for (DettaglioDDT dettRemove : dettagliToRemove) {
                ddtDett.remove(dettRemove);
            }

            ddt = ddtVM.MapDDT(ddt);
            ddt.setDettagliDDT(ddtDett);

            String ress = ddts.store(ddt, isUpdate);

            if (ress == "success") {
                res.setSuccess(true);
                res.setId(ddt.getId());
                res.setMessage("Salvataggio avvenuto con successo");
            } else {
                res.setSuccess(false);
                res.setMessage("Errore durante il salvataggio");
            }

        } catch (Exception ex) {
            res.setSuccess(false);
            res.setMessage("Eccezione durante il salvataggio - ex: " + ex.getMessage());
        }

        return new JSONObject(res);
    }
}
