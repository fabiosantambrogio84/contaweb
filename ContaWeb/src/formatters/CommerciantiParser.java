package formatters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

import vo.Cliente;
import vo.DDT;
import vo.DettaglioDDT;
import vo.Fattura;

public class CommerciantiParser implements ConadParser {

	@Override
	public String format(Collection listaFatture) {
		StringBuilder sb = new StringBuilder();
		
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("ddMMyyyy");
		
		DecimalFormat df = new DecimalFormat();
		df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
		df.applyPattern("#.00");
	
		sb.append("CFIS-DITTA;REGISTRO;MESE;IDPAESE;PARTITA-IVA;CFIS-CLIFOR;DENOMINAZIONE;COGNOME;NOME;TIPO-DOCUMENTO;DATA-FATTURA(GGMMAAAA);NUM-FATTURA;DATA-REG;IMPONIBILE;IMPOSTA;ALIQUOTA;NATURA;DETRAIBILE(PERC);DEDUCIBILE(S/N);ESIGIB-IVA;SEDE-INDIR;SEDE-NUM;SEDE-CAP;SEDE-COMUNE;SEDE-PROV;SEDE-NAZ;SORG-INDIR;SORG-NUM;SORG-CAP;SORG-COMUNE;SORG-PROV;SORG-NAZ;RFIS-ID-PAESE;RFIS-PIVA;RFIS-DENOM;RFIS-NOME;RFIS-COGNOME");sb.append("\n");
		
		for (Object fatt : listaFatture){
			Fattura fattura = (Fattura)fatt;
			fattura.calcolaTotali();
			Cliente cliente = fattura.getCliente();
			HashMap<BigDecimal, BigDecimal[]> imponibili = 
					fattura.getImponibili();
			
			for (BigDecimal aliquota : imponibili.keySet()) {	
				//CFIS-DITTA;REGISTRO;MESE;IDPAESE;PARTITA-IVA;CFIS-CLIFOR;DENOMINAZIONE;COGNOME;NOME;TIPO-DOCUMENTO;
				//DATA-FATTURA(GGMMAAAA);NUM-FATTURA;DATA-REG;IMPONIBILE;IMPOSTA;ALIQUOTA;
				//NATURA;DETRAIBILE(PERC);DEDUCIBILE(S/N);ESIGIB-IVA;SEDE-INDIR;SEDE-NUM;SEDE-CAP;SEDE-COMUNE;SEDE-PROV;SEDE-NAZ;SORG-INDIR;SORG-NUM;SORG-CAP;SORG-COMUNE;SORG-PROV;SORG-NAZ;RFIS-ID-PAESE;RFIS-PIVA;RFIS-DENOM;RFIS-NOME;RFIS-COGNOME
				//RBNGPP56A14L551I;E;12;IT;4151300235;4151300235;ADAMI MONICA ALIMENTARI;;;TD01 Fattura;
				//31072017;1527;31072017;64,36;6,44;10;;;;I;;;;;;;;;;;;;;;;;
				sb.append("RBNGPP56A14L551I");sb.append(";");
				sb.append("E");sb.append(";");
				sb.append(fattura.getData().getMonth() + 1);sb.append(";");
				sb.append("IT");sb.append(";");
				sb.append(cliente.getPiva() != null ? cliente.getPiva() : cliente.getCodiceFiscale());sb.append(";");
				sb.append(cliente.getCodiceFiscale() != null ? cliente.getCodiceFiscale() : "");sb.append(";");
				sb.append(cliente.getRs().replaceAll(";", " ").toUpperCase());
				if(cliente.getRs2() != null && cliente.getRs2().trim().length() > 0)
					sb.append(" " + cliente.getRs2().replaceAll(";", " ").toUpperCase());
				sb.append(";");
				sb.append(";");
				sb.append(";");
				sb.append("TD01 Fattura");sb.append(";");
				sb.append(sdf.format(fattura.getData()));sb.append(";");
				sb.append(fattura.getNumeroProgressivo());sb.append(";");
				sb.append(sdf.format(fattura.getData()));sb.append(";");				
				sb.append(df.format(imponibili.get(aliquota)[0]));sb.append(";");
				sb.append(df.format(imponibili.get(aliquota)[1]));sb.append(";");
				sb.append(df.format(aliquota.doubleValue()));sb.append(";");
				sb.append(";");
				sb.append(";");
				sb.append(";");
				sb.append("");sb.append(";");
				sb.append(";;;;;;;;;;;;;;;;");
				sb.append("\n");
			}
		}
		
		return sb.toString();
	}

}
