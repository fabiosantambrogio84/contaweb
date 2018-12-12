package utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DbUtils {

	private static final Logger logger = Logger.getLogger(DbUtils.class);
	
	private static DbUtils instance;
	
	private Properties properties;
	
	public DbUtils() throws Exception{
		if(properties == null){
	    	InputStream is = null;
	    	try{
	    		properties = new Properties();
		    	
		    	is = this.getClass().getClassLoader().getResourceAsStream("db.properties");
		    	properties.load(is);
	    	} catch(Exception e){
	    		logger.error("Errore nel caricamento delle proprietà di connessione al db", e);
	    		throw e;
	    	} finally{
	    		if(is != null){
	    			try{
	    				is.close();
	    			} catch(Exception e){
	    				logger.error("Errore nella chiusura dello stream di lettura delle proprietà di connessione al db", e);
	    			}
	    		}
	    	}
		}
	}
	
	public static DbUtils getInstance() throws Exception{
	    if(instance == null){
	        instance = new DbUtils();
	    }
	    return instance;
	}
	
	public Connection getConnection(){
		Connection connection = null;
		try{
			String url = properties.getProperty("url");
			
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://"+url, properties);
			
		} catch(Exception e){
			e.printStackTrace();
			logger.error("Errore nella creazione della connessione al db", e);
		}
		return connection;
	}
	
	public void closeConnection(Connection connection){
		try{
			if(connection != null){
				connection.close();
			}
		} catch(Exception e){
			logger.error("Errore nella chiusura della connessione al db", e);
		}
	}
	
	public Integer getSequenceNextVal(Connection connection, String sequence){
		Integer nextVal = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "SELECT nextval('"+sequence+"') as SEQ from dual;";
		try{
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
            while (rs.next()){
            	nextVal = rs.getInt("SEQ");            	
            }
		}catch(Exception e){
			logger.error("Errore nel recupero del next val per la sequence '"+sequence+"'", e);
		} finally{
			try{
				if(ps != null){
					ps.close();
				}
				if(rs != null){
					rs.close();
				}
			} catch(Exception e){
				logger.error("Errore nella chiusura del prepared statement per il recupero del next val per la sequence '"+sequence+"'", e);
			}
			
		}
		return nextVal;
	}
	
	public static void main(String[] args) throws Exception {
		DbUtils dbUtils = DbUtils.getInstance();
		
		Connection conn = dbUtils.getConnection();
		Integer nextVal = dbUtils.getSequenceNextVal(conn, "seq_e_fatturazione");
		System.out.println("-> "+nextVal);
		
		dbUtils.closeConnection(conn);
	}
}
