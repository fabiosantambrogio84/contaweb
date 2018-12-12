package formatters;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import dao.DataAccessException;
import dao.PagamentiEseguiti;
import vo.Cliente;
import vo.Fattura;
import vo.PagamentoEseguito;

public class RibaParser implements ConadParser {

	@Override
	public String format(Collection fatture) {
		StringBuilder sb = new StringBuilder();
		
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("ddMMyy");
		
		DecimalFormat df = new DecimalFormat();
		df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
		df.applyPattern("#.00");
		
		PagamentiEseguiti pagamentiEseguiti = new PagamentiEseguiti(); 
		
		String codice_sia = "12L96";
		String codice_fiscale = "RBNGPP56A14L551I";
		String codice_abi = "02008";
		String codice_cab = "59760";
		String cc = "000041279248";
		Date data_invio = new Date();
		int totale = 0;
		int importo = 0;
	
		//RECORD DI TESTA IB
		sb.append(" ");
		sb.append("IB");
		sb.append(codice_sia);
		sb.append(codice_abi);
		sb.append(sdf.format(data_invio));
		sb.append(String.format("%1$-20s", "URBANI ALIMENTARI"));
		sb.append(String.format("%1$6s", ""));	// Campo Libero
		sb.append(String.format("%1$59s", ""));	// Campo Vuoto - Filler
		sb.append(String.format("%1$7s", ""));  // Qualificatore flusso
		sb.append(String.format("%1$2s", ""));  // Campo Vuoto - Filler
		sb.append("E");  // Divisa
		sb.append(" ");  // Campo Vuoto - Filler
		sb.append(String.format("%1$5s", ""));  // Campo non disponibile
		sb.append("\n");
		
		int idx = 1;
		for (Object obj : fatture) {
			Fattura fattura = (Fattura)obj;		
			fattura.calcolaTotali();
			
			Cliente cliente = fattura.getCliente();
						
			Calendar c = Calendar.getInstance(); 
			c.setTime(fattura.getData()); 
			c.add(Calendar.DATE, cliente.getPagamento().getScadenza());
			Date scadenza = c.getTime();
			
			importo = (int)(fattura.getDaPagare().doubleValue() * 100);
			totale += importo;
			
			PagamentoEseguito pagamento = new PagamentoEseguito();
			pagamento.setIdCliente(cliente.getId());
			pagamento.setCliente(cliente);
			pagamento.setData(data_invio);
			pagamento.setFattura(fattura);			
			pagamento.setDescrizione("Pagamento " + fattura.getDescrizioneBreveDDT());
		    pagamento.setImporto(fattura.getDaPagare());
		    pagamento.setIdPagamento(cliente.getIdPagamento());
		    try {
				pagamentiEseguiti.store(pagamento);
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		    			
			
			//RECORD 14
			sb.append(" ");
			sb.append("14");		
			sb.append(String.format("%07d", idx));				
			sb.append(String.format("%1$12s", ""));  // Campo Vuoto - Filler
			sb.append(sdf.format(scadenza));
			sb.append("30000");
			sb.append(String.format("%013d", importo));
			sb.append("-");
			sb.append(codice_abi);
			sb.append(codice_cab);
			sb.append(String.format("%1$12s", cc));
			sb.append(String.format("%1$5s", cliente.getBancaABI().trim()));
			sb.append(String.format("%1$5s", cliente.getBancaCAB().trim()));
			sb.append(String.format("%1$12s", ""));  // Campo Vuoto - Filler
			sb.append(codice_sia);
			sb.append("4");
			sb.append(String.format("%1$-16s", cliente.getId()));  // Campo Vuoto - Filler
			sb.append(" ");
			sb.append(String.format("%1$5s", ""));  // Campo Vuoto - Filler
			sb.append("E");
			sb.append("\n");
			
			//RECORD 20
			sb.append(" ");
			sb.append("20");
			sb.append(String.format("%07d", idx));
			sb.append(String.format("%1$-24s", "URBANI ALIMENTARI"));
			sb.append(String.format("%1$-24s", "Via 11 Settembre, 17"));  // Campo Vuoto - Filler
			sb.append(String.format("%1$-24s", "37035 S. Giovanni"));  // Campo Vuoto - Filler
			sb.append(String.format("%1$-24s", "Ilarione (VR)"));  // Campo Vuoto - Filler
			sb.append(String.format("%1$14s", ""));  // Campo Vuoto - Filler
			sb.append("\n");			
			
			//RECORD 30
			sb.append(" ");
			sb.append("30");
			sb.append(String.format("%07d", idx));
			sb.append(String.format("%1$-30s", org.apache.commons.lang.StringUtils.left(cliente.getRs(), 30)));
			sb.append(String.format("%1$-30s", org.apache.commons.lang.StringUtils.left(cliente.getRs2(), 30)));
			sb.append(String.format("%1$-16s",  cliente.getPiva() != null ? cliente.getPiva() : cliente.getCodiceFiscale()));  // Campo Vuoto - Filler
			sb.append(String.format("%1$34s", ""));  // Campo Vuoto - Filler
			sb.append("\n");			
			
			//RECORD 40
			sb.append(" ");
			sb.append("40");
			sb.append(String.format("%07d", idx));
			sb.append(String.format("%1$-30s", org.apache.commons.lang.StringUtils.left(cliente.getIndirizzo(), 30)));
			sb.append(String.format("%1$5s", org.apache.commons.lang.StringUtils.left(cliente.getCap(), 5)));
			sb.append(String.format("%1$-25s", org.apache.commons.lang.StringUtils.left(cliente.getLocalita(), 25)));  
			sb.append(String.format("%1$50s", ""));
			sb.append("\n");
			
			//RECORD 50
			sb.append(" ");
			sb.append("50");
			sb.append(String.format("%07d", idx));
			sb.append(String.format("%1$-40s", org.apache.commons.lang.StringUtils.left(pagamento.getDescrizione(), 40)));
			sb.append(String.format("%1$40s", ""));
			sb.append(String.format("%1$10s", ""));
			sb.append(String.format("%1$-16s", codice_fiscale));
			sb.append(String.format("%1$4s", ""));
			sb.append("\n");
			
			
			//RECORD 51
			sb.append(" ");
			sb.append("51");
			sb.append(String.format("%07d", idx));
			sb.append(String.format("%010d", pagamento.getId()));
			sb.append(String.format("%1$20s", "URBANI ALIMENTARI"));
			sb.append(String.format("%1$15s", ""));
			sb.append(String.format("%1$10s", ""));
			sb.append(String.format("%1$6s", ""));
			sb.append(String.format("%1$49s", ""));
			sb.append("\n");
			
			
			//RECORD 70
			sb.append(" ");
			sb.append("70");
			sb.append(String.format("%07d", idx));
			sb.append(String.format("%1$78s", ""));
			sb.append(String.format("%1$12s", ""));
			sb.append(" ");
			sb.append(" ");
			sb.append(" ");
			sb.append(String.format("%1$17s", ""));
			sb.append("\n");
			
			idx++;				
		}
		
		idx--;
		
		//RECORD DI CODA EF
		sb.append(" ");
		sb.append("EF");
		sb.append(codice_sia);
		sb.append(codice_abi);
		sb.append(sdf.format(data_invio));
		sb.append(String.format("%1$-20s", "URBANI ALIMENTARI"));
		sb.append(String.format("%1$6s", ""));	// Campo Libero
		sb.append(String.format("%07d", idx));	
		sb.append(String.format("%015d", totale));	
		sb.append(String.format("%015d", 0));	
		sb.append(String.format("%07d", (idx * 7 + 2)));	
		sb.append(String.format("%1$24s", ""));  // Campo Vuoto - Filler
		sb.append("E");  // Divisa
		sb.append(" ");  // Campo Vuoto - Filler
		sb.append(String.format("%1$5s", ""));  // Campo non disponibile
		sb.append("\n");
		
		
		
		return sb.toString();
	}
}
