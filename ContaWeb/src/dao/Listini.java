package dao;

import vo.*;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import java.util.*;

import java.math.*;

public class Listini extends DataAccessObject {
	
	public Listini() {
		this.elementClass = Listino.class;
	}
	
	public Listino find(Integer idListino) throws DataAccessException {
		Listino listino = new Listino();
		listino.setId(idListino);
		return (Listino)find(listino);
	}
	
	
	public void setOrderByDescrizione(int order) {
		useDefaultCriteria = false;
		if (order == ORDER_DESC)
			getQueryByCriteria().addOrderByDescending("descrizione");
		else
			getQueryByCriteria().addOrderByAscending("descrizione");
	}
	
	@SuppressWarnings("unchecked")
	public Vector getListiniByPrezzi() throws DataAccessException {
		PersistenceBroker broker = null;
		Vector results = null;
		try {
			broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			results = new Vector();

			Collection listini = getElements();
			Iterator itr = listini.iterator();
			while (itr.hasNext()) {
				Listino listino = (Listino)itr.next();
				Prezzo prezzo = new Prezzo();
				prezzo.setListino(listino);
				prezzo.setPrezzo(new BigDecimal(0));
				results.add(prezzo);
			}

			broker.close();
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage());
		} finally {
			if (broker != null)
				broker.close();
		}
	
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public Vector getListiniByPrezzi(Vector prezzi) throws DataAccessException {
		Vector newPrezzi = getListiniByPrezzi();
		for(int i=0;i<newPrezzi.size(); ++i) {
			Prezzo newPrezzo = (Prezzo) newPrezzi.get(i);
			boolean trovato = false;
			for(int j=0;j<prezzi.size() && !trovato; ++j) {
				Prezzo oldPrezzo = (Prezzo) prezzi.get(j);
				if (newPrezzo.getIdListino() == oldPrezzo.getIdListino()) {
					trovato = true;
					newPrezzi.set(i, oldPrezzo);
				}
			}
		}

		return newPrezzi;
	}
	
	@SuppressWarnings("unchecked")
	public Vector getListiniAssociatiByCliente(Cliente cliente) throws DataAccessException {
		Collection fornitori = new Fornitori().getElements();
		
		Vector vector = new Vector();
		Iterator itr = fornitori.iterator();
		while (itr.hasNext()) {
			Fornitore fornitore = (Fornitore) itr.next();
			ListinoAssociato la = new ListinoAssociato();
			la.setFornitore(fornitore);
			la.setCliente(cliente);
			//TODO: LISTINO NULLO. IMPOSTARE LISTINO DI DEFAULT?
			vector.add(la);
		}
		
		//CONTROLLO ESISTANO LISTINO GIA' PRESENTI
		elementClass = ListinoAssociato.class;		
		getCriteria().addColumnEqualTo("idCliente", cliente.getId());
		Collection listiniEsistenti = getElements();
		Iterator itrListiniEsistenti = listiniEsistenti.iterator();
		
		boolean trovato = false;
		while (itrListiniEsistenti.hasNext()) {
			ListinoAssociato la = (ListinoAssociato) itrListiniEsistenti.next();
			trovato = false;
			for(int i=0; i<vector.size() && !trovato; ++i) {
				ListinoAssociato la1 = (ListinoAssociato) vector.get(i);
				if (la.getFornitore().getId().equals(la1.getFornitore().getId())) {
					trovato = true;
					vector.set(i, la);
				}
			}
		}
		return vector;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Cliente> getClientiByListino(Integer idFornitore, Integer idListino, String rs,
			Integer startRow, Integer endRow) 
			throws DataAccessException {
		
		List<Cliente> clienti = new ArrayList<Cliente>();
		HashMap<Integer, Cliente> cache = new HashMap<Integer, Cliente>();
		
		//CONTROLLO ESISTANO LISTINO GIA' PRESENTI
		elementClass = ListinoAssociato.class;		
		if(idFornitore !=null) getCriteria().addColumnEqualTo("idFornitore", idFornitore);
		if(idListino !=null) getCriteria().addColumnEqualTo("idListino", idListino);
		getCriteria().addPrefetchedRelationship("cliente");
		getCriteria().addOrderByAscending("cliente.rs");
		
		Collection listini = getElements();
		Iterator itrListiniEsistenti = listini.iterator();
		
		Integer rowNum = 0;
		while (itrListiniEsistenti.hasNext()) {
			ListinoAssociato la = (ListinoAssociato) itrListiniEsistenti.next();
			if(!cache.containsKey(la.getIdCliente()))
			{
				if(rs != null && rs.trim() != "" && la.getCliente().getRs().toLowerCase()
						.indexOf(rs.toLowerCase()) < 0)
					continue;
				
				cache.put(la.getIdCliente(), la.getCliente());
				
				rowNum++;
				if(rowNum < startRow) continue;
				if(rowNum >= endRow) break;
				
				clienti.add(la.getCliente());
			}
		}
		
		return clienti;
	}
	
	@SuppressWarnings("unchecked")
	public Integer getCountClientiByListino(Integer idFornitore, Integer idListino, String rs) 
			throws DataAccessException {
		
		HashMap<Integer, Cliente> cache = new HashMap<Integer, Cliente>();
		
		//CONTROLLO ESISTANO LISTINO GIA' PRESENTI
		elementClass = ListinoAssociato.class;		
		if(idFornitore !=null) getCriteria().addColumnEqualTo("idFornitore", idFornitore);
		if(idListino !=null) getCriteria().addColumnEqualTo("idListino", idListino);
		getCriteria().addPrefetchedRelationship("cliente");
		
		Collection listini = getElements();
		Iterator itrListiniEsistenti = listini.iterator();
		
		while (itrListiniEsistenti.hasNext()) {
			ListinoAssociato la = (ListinoAssociato) itrListiniEsistenti.next();
			if(rs != null && rs.trim() != "" && la.getCliente().getRs().toLowerCase().indexOf(rs.toLowerCase()) < 0)
				continue;
			
			if(!cache.containsKey(la.getIdCliente()))
				cache.put(la.getIdCliente(), la.getCliente());				
		}
		
		return cache.size();
	}


	public PrezzoConSconto getPrezzoConSconto(Articolo articolo, Cliente cliente, Date data) {
		PrezzoConSconto ps = null;
		try {
			if (cliente == null)
				return ps;
			if (articolo == null)
				return ps;
			
			//RICAVO IL PREZZO DI LISTINO ASSOCIATO AL CLIENTE
			Iterator itr = cliente.getListiniAssociati().iterator();
			Integer idListino = null;
			while (itr.hasNext() && idListino == null) {
				ListinoAssociato la = (ListinoAssociato) itr.next();
				if (la.getIdFornitore() == articolo.getIdFornitore())
					idListino = la.getIdListino();
			}
			
			//SE NON ESISTE RICAVO IL PREZZO DEL LISTINO DI DEFAULT
			if (idListino == null)
				idListino = 2;

			//RICAVO IL PREZZO SENZA SCONTO
			Prezzo prezzo = null;
			itr = articolo.getPrezzi().iterator();
			while (itr.hasNext() && prezzo == null) {
				Prezzo prezzoTemp = (Prezzo) itr.next();
				if (prezzoTemp.getIdListino() == idListino)
					prezzo = prezzoTemp;
			}
			
			ps = new PrezzoConSconto();
			ps.setArticolo(articolo);
			ps.setIdArticolo(prezzo.getIdArticolo());
			ps.setIdListino(prezzo.getIdListino());
			ps.setListino(prezzo.getListino());
			ps.setPrezzo(prezzo.getPrezzo());
			ps.setSconto(new BigDecimal(0));

			if (data == null)
				return ps;
			Sconto sconto = new Sconti().find(cliente, articolo.getFornitore(), articolo, data);
			if (sconto != null)
				ps.setSconto(sconto.getSconto());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ps;
	}
}
