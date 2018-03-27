package dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import vo.Property;
// import vo.Configurazione;

public class Properties extends DataAccessObject {
	
	private static final long serialVersionUID = 6541869612711264445L;
	
	static private Properties settings = null;
	static private Map<String, String> map = null;
	static private Property property = null;
	// static private Configurazione configurazione = null;
	
	private Properties() {
		this.elementClass = Property.class;
	}

//	protected void setDefaultCriteria() {
//		getQueryByCriteria().addOrderByDescending("key");
//	}
	
	@SuppressWarnings("unchecked")
	private static void loadSettings() {
		map = new HashMap();
		//CARICO GLI ELEMENTI
		try {
			Collection keys = new Properties().getElements();
			Iterator itr = keys.iterator();
			while (itr.hasNext()) {
				Property prop = (Property) itr.next();
				map.put(prop.getKey(), prop.getValue());
			}
		} catch (Exception e) {
		}
	}
	
	public static Properties getInstance() {
		if (settings == null) {
			settings = new Properties();
			loadSettings();
		}
		return settings;
	}
	
	public String getValue(String key) {
		String tmp = null;
		tmp = (String) map.get(key);
		return tmp;
	}

	// @key = key; value = value; return previous value;
	public String setValue(String key, String value) {
		try {
			String tmp = null;
			tmp = (String) map.put(key, value);
			return tmp;
		} catch (Exception e) {
            stampaErrore("Settings.setValue(" + key + ", " + value + "); ",e);
            return null;
        }
	}

	// Questo store non può funzionare a causa della scelta degli elementi in tabella (key, value). 
	// La parola "key" è riservata è genera un'eccezione nel JDBC. 
//	public String store(Property value) throws DataAccessException {
//		return super.store(value);
//	}
	
}
