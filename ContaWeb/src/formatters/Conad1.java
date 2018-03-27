package formatters;

import vo.DDT;
import vo.Fattura;
import vo.DettaglioDDT;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.Vector;

public class Conad1 implements ConadParser {
	public String format(Collection listaFatture) {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat();
		DecimalFormat df = new DecimalFormat();
		df.applyPattern("#");
		sdf.applyPattern("yyMMdd");
		String leftAlign = String.format("%1$60s", "");
		int i = 0;
		for (Object fatt : listaFatture){
			Fattura fattura = (Fattura)fatt;
			for (Object ddt : fattura.getDettagliFattura()){
				DDT bolla = (DDT) ddt;
				//record testata
				sb.append("01");
				//progressivo
				sb.append(String.format("%05d", ++i));
				//numero fattura
				sb.append(String.format("%06d", fattura.getId()));
				//data fattura
				sb.append(sdf.format(fattura.getData()));
				//numero bolla
				sb.append(String.format("%06d", bolla.getId()));
				//data bolla
				sb.append(sdf.format(bolla.getData()));
				//codice fornitore
				sb.append("6800           ");
				//tipo fornitore
				sb.append(" ");
				//codice cliente
				sb.append(new String("COOP VENETO"+leftAlign).substring(0, 15));
				//codice cooperativa
				// sb.append(new String(bolla.getPuntoConsegna().getCodConad()+leftAlign).substring(0, 15));
				if(bolla.getPuntoConsegna() != null) sb.append(new String(bolla.getPuntoConsegna().getCodConad()+leftAlign).substring(0, 15));
				else sb.append(new String(leftAlign).substring(0, 15));
				//codice socio
				// sb.append(String.format("%1$15s", bolla.getPuntoConsegna().getCodConad()));
				if(bolla.getPuntoConsegna() != null) sb.append(String.format("%1$15s", bolla.getPuntoConsegna().getCodConad()));
				else sb.append(String.format("%1$15s", " "));
				//tipo socio
				sb.append(" ");
				//tipo documento
				sb.append("F");
				//codice divisa
				sb.append("EUR");
				//libero
				sb.append(String.format("%1$25s", ""));
				//riservato
				sb.append(String.format("%1$6s", ""));
				sb.append("\n");
				Vector vDettaglio = fattura.getDettagliFattura();
				int j = 0;
				for (Object ddtDet: bolla.getDettagliDDT()){
					DettaglioDDT ddtDett = (DettaglioDDT)ddtDet;
					//tipo record
					sb.append("02");
					//progressivo
					sb.append(String.format("%05d", ++j));
					//codice articolo
					sb.append(String.format("%1$15s", ddtDett.getCodiceArticolo()).substring(0, 15));
					//descrizione articolo
					sb.append(new String(ddtDett.getDescrizioneArticolo() + leftAlign).substring(0, 30));
					//unita misura
					sb.append(new String(ddtDett.getUm() + leftAlign).substring(0, 2));
					df.applyPattern("0000000");
					//quantita
					sb.append(df.format(ddtDett.getQta().floatValue()*100));
					//prezzo
					df.applyPattern("000000000");
					sb.append(df.format(ddtDett.getPrezzo().subtract(ddtDett.getSconto()).floatValue()*1000));
					//totale
					sb.append(df.format(ddtDett.getTotale().floatValue()*1000));
					//pezzi
					sb.append(String.format("%04d",ddtDett.getPezzi()));
					//tipo IVA
					sb.append(" ");
					//aliquota IVA
					sb.append(String.format("%02d", ddtDett.getIva().intValue()));
					//tipo movimento
					sb.append("1");
					//tipo cessione
					sb.append("1");
					//numero ordine conad
					sb.append(String.format("%06d", 0));
					//codice listino
					sb.append(String.format("%1$2s", ""));
					//tipo articolo
					sb.append(" ");
					//tipo contratto
					sb.append(" ");
					//tipo trattamento
					sb.append(" ");
					//costo trasporto
					sb.append("00000");
					//codice contabile
					sb.append(" ");
					//tipo reso
					sb.append(" ");
					//prezzo catalogo
					sb.append("0000000");
					//libero
					sb.append("   ");
					//data ordine
					sb.append("000000");
					//riservato
					sb.append(String.format("%1$6s", ""));
					
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}
}
