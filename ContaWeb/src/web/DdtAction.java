package web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import applet.db.DbConnector;
import dao.DDTs;
import dto.DDTActionViewModel;
import dto.DDTViewModel;
import vo.DDT;
import vo.DettaglioDDT;
import vo.EvasioneOrdine;
import dto.DettaglioDDTViewModel;

public class DdtAction  extends GenericAction {
	
	private DDTActionViewModel ddtObject;
	private String idDDT;
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public String execute() throws Exception {  
		DDTs ddts = new DDTs();
        DDT ddt = new DDT();
        Vector<DettaglioDDT> dettagli = new Vector<DettaglioDDT>();
        if(!idDDT.equals("0")){

            ddt.setId(Integer.parseInt(idDDT));
            ddt = (DDT)ddts.findWithAllReferences(ddt); 

            dettagli = ddt.getDettagliDDT(); 
        }
        
        ddtObject = new DDTActionViewModel();
        ddtObject.setDdt(new DDTViewModel(ddt));
        
        java.util.List<DettaglioDDTViewModel> dettaglioDDT = new ArrayList<DettaglioDDTViewModel>();
        int i = 0;
        for (DettaglioDDT dettaglio : dettagli)
        {
        	Vector evasioniOrdine = dettaglio.getEvasioniOrdini();
        	dettaglioDDT.add(i, new DettaglioDDTViewModel(dettaglio, evasioniOrdine));
        	i++;
        }
        
        ddtObject.setArt(dettaglioDDT);
        
		return SUCCESS;
    }

	public String getidDDT() {
		return idDDT;
	}

	public void setidDDT(String idDDT) {
		this.idDDT = idDDT;
	}

	public DDTActionViewModel getDdtObject() {
		return ddtObject;
	}

	public void setDdtObject(DDTActionViewModel ddtObject) {
		this.ddtObject = ddtObject;
	}
}
