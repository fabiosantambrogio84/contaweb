package dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import vo.Property;
import vo.Configurazione;

public class Settings extends DataAccessObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5753860313873250358L;

	static public final String NORMAL = "NORMAL";
//	static public final int WARNING = 1;
//	static public final int ERROR = 2;
	static public final String DB_UPDATE_REQUEST = "DB_UPDATE_REQUEST";
	static public final String UNITIALIZED = "UNITIALIZED";
	static public final String UPDATE_REQUEST = "UPDATE_REQUEST";
	private static String stato = UNITIALIZED;
	
	static private Settings settings = null;
	static private Map<String, String> map = null;
	//static private Property property = null;
	static private Configurazione configurazione = null;
	
	private Settings() {
		this.elementClass = Configurazione.class;
		// stato = UNITIALIZED;
	}

//	protected void setDefaultCriteria() {
//		getQueryByCriteria().addOrderByDescending("elemento");
//	}
	
	private static String loadSettings() {
		map = new HashMap<>();
		String result = ERROR;
		//CARICO GLI ELEMENTI
		try {
			stato = UPDATE_REQUEST;
			Collection<?> elementi = new Settings().getElements();
			if (elementi == null | elementi.size() == 0) {
				Properties properties = Properties.getInstance();
				elementi = properties.getElements();
				if (elementi == null)
					return ERROR;
				Iterator<?> itr = elementi.iterator();
				while (itr.hasNext()) {
					Property prop = (Property) itr.next();
					map.put(prop.getKey(), prop.getValue());
				}
				result = SUCCESS;
				stato = UPDATE_REQUEST;
			} else {
				Iterator<?> itr = elementi.iterator();
				while (itr.hasNext()) {
					configurazione = (Configurazione) itr.next();
					map.put(configurazione.getElemento(), configurazione.getValue());
				}
				result = SUCCESS;
				stato = NORMAL;
		    }
		} catch (Exception e) {
			if(stato == UPDATE_REQUEST) {
				stato = DB_UPDATE_REQUEST;
				Properties properties = Properties.getInstance();
				Collection<?> elementi;
				try {
					elementi = properties.getElements();
					if (elementi == null)
						return ERROR;
					Iterator<?> itr = elementi.iterator();
					while (itr.hasNext()) {
						Property prop = (Property) itr.next();
						map.put(prop.getKey(), prop.getValue());
					}
					result = SUCCESS;
				} catch (DataAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					result = ERROR;
				}
			}
			return result;
		}
		return result;
	}
	
	public static Settings getInstance() {
		if (settings == null) {
			settings = new Settings();
			loadSettings();
		}
		return settings;
	}
	
	public String getValue(String elemento) {
		if (elemento == "PasswordCompleto")
			return "1357";
		if (elemento == "PasswordNormale")
			return "1111";
		if (elemento == "PasswordLimitato")
			return "0000";
		String tmp = null;
		tmp = (String) map.get(elemento);
		return tmp;
	}

	// @elemento = elemento; value = value; return previous value;
	public String setValue(String elemento, String value) {
		try {
			String tmp = null;
			tmp = (String) map.put(elemento, value);
			return tmp;
		} catch (Exception e) {
            stampaErrore("Settings.setValue(" + elemento + ", " + value + "); ",e);
            return null;
        }
	}

	public String updateDB() throws DataAccessException {
		String result = ERROR;
		String tmp = null;
		Configurazione conf = new Configurazione();
		Property prop = null;
		Collection<?> elementi = null;
		if (stato == UPDATE_REQUEST) {
			elementi = Properties.getInstance().getElements();
			if (elementi == null | elementi.size() == 0)
				return ERROR;
			Iterator<?> itr = elementi.iterator();
			while (itr.hasNext()) {
				// conf = (Configurazione) itr.next();
				prop = (Property) itr.next();
				// conf.setId(-1);
				conf.setElemento(prop.getKey());
				conf.setValue(prop.getValue());
				tmp = store(conf); 
				if (tmp != SUCCESS)
					return ERROR;
			}
			result = SUCCESS;
			stato = NORMAL;
		}
		return result;
	}
	
	public String store(Configurazione value) throws DataAccessException {
		return super.store(value);
	}
	
	public String store(Configurazione[] value) throws DataAccessException {
		return super.store(value);
	}

	public String getStato() {
		return stato;
	}

}
