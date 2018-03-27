package stampe;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import dao.DataAccessObject;
import dao.Fatture;
import dao.Ivas;
import vo.Fattura;
import vo.Iva;

public class PrintVendite extends PrintPDF {

	private static final long serialVersionUID = 1L;

	private Date dataDal = null;
	private Date dataAl = null;
	
	private Collection listaTotali = null;

	public Date getDataAl() {
		return dataAl;
	}

	public void setDataAl(Date dataAl) {
		this.dataAl = dataAl;
	}

	public Date getDataDal() {
		return dataDal;
	}

	public void setDataDal(Date dataDal) {
		this.dataDal = dataDal;
	}

	public String execute() { //STAMPA LISTA TOTALI VENDITE
		try {
			HashMap<Integer, RigaVenditeCommercianti> map = new HashMap<Integer, RigaVenditeCommercianti>();
			listaTotali = new LinkedList<RigaVenditeCommercianti>();
			
			Fatture fatture = new Fatture();
			fatture.setOrderByCliente();
			Collection colFatture = fatture.getFatture(dataDal, dataAl);
			Iterator itr = colFatture.iterator();
			while (itr.hasNext()) {
				Fattura fattura = (Fattura) itr.next();	
				fattura.calcolaTotali();
				if (map.containsKey(fattura.getIdCliente().intValue())) {
					//Aggiungi totali alla riga attuale
					RigaVenditeCommercianti riga = map.get(fattura.getIdCliente().intValue());
					riga.setTotaleFattura(riga.getTotaleFattura().add(fattura.getTotaleFattura()));

					//CALCOLO IMPONIBILI
					fattura.calcolaTotali();
					HashMap<BigDecimal, BigDecimal[]> imponibili = fattura.getImponibili();
					
					//IVA AL 4
					if (imponibili.containsKey(riga.getIva1())) { //IVA AL 4
						riga.setImponibile1(riga.getImponibile1().add(imponibili.get(riga.getIva1())[0]));
						riga.setImposta1(riga.getImposta1().add(imponibili.get(riga.getIva1())[1]));
					}
					
					//IVA AL 10
					if (imponibili.containsKey(riga.getIva2())) {
						riga.setImponibile2(riga.getImponibile2().add(imponibili.get(riga.getIva2())[0]));
						riga.setImposta2(riga.getImposta2().add(imponibili.get(riga.getIva2())[1]));
					}
					
					//IVA AL 20
					if (imponibili.containsKey(riga.getIva2())) {
						riga.setImponibile3(riga.getImponibile3().add(imponibili.get(riga.getIva3())[0]));
						riga.setImposta3(riga.getImposta3().add(imponibili.get(riga.getIva3())[1]));
					}

				} else {
					//Crea riga per cliente
					RigaVenditeCommercianti riga = new RigaVenditeCommercianti();
					riga.setCliente(fattura.getCliente());
					riga.setTotaleFattura(fattura.getTotaleFattura());
					
					//CALCOLO IMPONIBILI
					HashMap<BigDecimal, BigDecimal[]> imponibili = fattura.getImponibili();
					Ivas dao = new Ivas();
					dao.setOrderByDescrizione(DataAccessObject.ORDER_ASC);
					Collection aliquote = dao.getElements();
					
					int index = 1;
					for(Object obj : aliquote)
					{
						Iva iva = (Iva)obj;
						if(iva.getValore().floatValue() < 4) continue;
						
						BigDecimal imponibile = new BigDecimal(0);
						BigDecimal imposta = new BigDecimal(0);
						
						if(imponibili.containsKey(iva.getValore()))
						{
							imponibile = imponibili.get(iva.getValore())[0];
							imposta = imponibili.get(iva.getValore())[1];
						}
						
						if(index == 1)
						{
							riga.setIva1(iva.getValore());
							riga.setImponibile1(imponibile);
							riga.setImposta1(imposta);
							
						}
						else if(index == 2)
						{
							riga.setIva2(iva.getValore());
							riga.setImponibile2(imponibile);
							riga.setImposta2(imposta);
						}
						else if(index == 3)
						{
							riga.setIva3(iva.getValore());
							riga.setImponibile3(imponibile);
							riga.setImposta3(imposta);
						}
						
						if(index == 3) break;
						
						index++;
					}
										
					map.put(fattura.getIdCliente().intValue(), riga);
					listaTotali.add(riga);
				}
			}
			
		} catch (Exception e) {
			stampaErrore("PrintVendite.execute()", e);
			return ERROR;
		}
		return SUCCESS;
	}

	public Collection getListaTotali() {
		return listaTotali;
	}

	public void setListaTotali(Collection listaTotali) {
		this.listaTotali = listaTotali;
	}
}
