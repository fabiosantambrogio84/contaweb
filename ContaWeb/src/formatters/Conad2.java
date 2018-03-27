package formatters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Vector;

import vo.DDT;
import vo.DettaglioDDT;
import vo.Fattura;

public class Conad2 implements ConadParser {

	@Override
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
			for (Object vDet : fattura.getDettagliFattura()){
				//tipo record
				DDT vDett = (DDT)vDet;
				//record testata
				sb.append("01");
				//progressivo
				// sb.append(String.format("%05d", ++i));
				sb.append(String.format("%06d", ++i));
				//numero fattura
				// sb.append(String.format("%06d", fattura.getId()));
				sb.append(String.format("%1$5s", fattura.getId()));
				//data fattura
				sb.append(sdf.format(fattura.getData()));
				//numero bolla
				// sb.append(String.format("%06d", vDett.getId()));
				sb.append(String.format("%1$6s", vDett.getNumeroProgressivo()));
				//data bolla
				sb.append(sdf.format(vDett.getData()));
				//no data
				sb.append(String.format("%1$55s", ""));
				//codice cliente
				// sb.append(String.format("%1$6s", ""));
				if(vDett.getPuntoConsegna() != null)
					sb.append(vDett.getPuntoConsegna().getCodConad());
				sb.append("\n");
				// Vector vDettaglio = fattura.getDettagliFattura();
				for (Object ddtDet: vDett.getDettagliDDT()){
					DettaglioDDT ddtDett = (DettaglioDDT)ddtDet;
					//tipo record
					sb.append("02");
					//no data
					sb.append(String.format("%1$12s", "")); //"%1$15s", ""));
					//codice articolo
					sb.append(String.format("%1$7s", ddtDett.getCodiceArticolo()).substring(0, 7));
					//no data
					sb.append(String.format("%1$1s", "")); 
					//descrizione articolo
					sb.append(new String(ddtDett.getDescrizioneArticolo() + leftAlign).substring(0, 30));
					//unita misura
					// sb.append(String.format("%1$2s", ddtDett.getUm()));
					sb.append(String.format("%1$2s", ddtDett.getUm().toUpperCase()));
					df.applyPattern("0000000");
					//quantita
					sb.append(df.format(ddtDett.getQta().floatValue()*100));
					//no data
					sb.append(String.format("%1$9s", ""));
					//prezzo
					df.applyPattern("000000000");
					BigDecimal sconto = ddtDett.getPrezzo().multiply(ddtDett.getSconto()
							.divide(new BigDecimal(100).setScale(3,BigDecimal.ROUND_HALF_UP)))
							.setScale(3,BigDecimal.ROUND_HALF_UP);
					BigDecimal prezzoScontato = ddtDett.getPrezzo()
							.subtract(sconto); 
					// sb.append(df.format(ddtDett.getPrezzo().subtract(ddtDett.getSconto()).floatValue()*1000));
					sb.append(df.format( prezzoScontato.floatValue() * 1000));
					//no data
					sb.append(String.format("%1$5s", ""));
					//aliquota IVA
					// sb.append(String.format("%02d", ddtDett.getIva().intValue()));
					sb.append(String.format("%1$2s", ddtDett.getIva().intValue()));
					//fisso
					sb.append("1");
					
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}

}
