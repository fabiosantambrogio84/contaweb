package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.util.Calendar;
//import java.util.Date;
import java.util.Properties;

import javax.naming.spi.DirStateFactory.Result;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import vo.Articolo;
import vo.DDT;
import vo.Giacenza;
import vo.NotaAccredito;
import vo.OrdineFornitore;
import vo.PuntoConsegna;
import vo.VOElement;

/**
 * This class connects to a database and dumps all the tables and contents out to stdout in the form of
 * a set of SQL executable statements
 */
public class DBUtilities extends DataAccessObject {

    private static String driverClassName = "";
    private static String driverURL = "";
    private static String columnNameQuote = "";
    private static String catalog = "";
    private static File fileAppend = null;
    private static FileOutputStream ofAppend = null;
    private static String schema = "";
    private static String storeInFileMode = "Create";
    private static String tables = "";
    private static int threshold = 128; 
    private static int totalLengh = 0;
    private static String urlTarget = ""; 
    private static String user = "";
    private static String password = "";
    private static String path = "";
    private static Properties props = null; 
    public static final String FilenamePathOnly  = "FilenamePathOnly"; // Restituisce la directory
    public static final String FilenameFileOnly  = "FilenameFileOnly"; // Restituisce il nome del file (completo di data e ora)
    public static final String FilenameFileUptoFileNameOnly = "FilenameFileUptoFileNameOnly"; // Restituisce il nome del file con "*" al posto di data e ora
    public static final String FilenameUptoFile  = "FilenameUptoFile"; // Path completo con "*" al posto di data e ora
    public static final String FilenameUptoYear  = "FilenameUptoYear"; // Path completo con "*" al posto di mese, giorno e ora
    public static final String FilenameUptoMonth = "FilenameUptoMonth"; // Path completo con "*" al posto di giorno e ora
    public static final String FilenameUptoDay   = "FilenameUptoDay"; // Path completo con "*" al posto dell'ora
    public static final String FilenameUptoHour  = "FilenameUptoHour"; // Path completo con "*" al posto dei minuti
    public static final String FilenameEntire    = "FilenameEntire"; // Path completo

    public static String calculateFilename(String backupLocation, String mode) {
    	String tmpFileName = "";
    	int tmpInt = 0;
    	Calendar cal = Calendar.getInstance();
    	
    	tmpFileName = backupLocation + "/ContaWebDB_" + cal.get(cal.YEAR);
    	tmpInt = cal.get(cal.MONTH) + 1; 
    	if (tmpInt < 9) 
    		tmpFileName += "0";
    	tmpFileName += tmpInt; //(cal.get(cal.MONTH) + 1);
    	tmpInt = cal.get(cal.DAY_OF_MONTH); 
    	if (tmpInt < 10)
    		tmpFileName += "0";
    	tmpFileName += tmpInt + "_"; // cal.get(cal.DAY_OF_MONTH) + "_";
    	tmpInt = cal.get(cal.HOUR_OF_DAY);
    	if (tmpInt < 10)
    		tmpFileName += "0"; 
    	tmpFileName += tmpInt; // cal.get(cal.HOUR_OF_DAY);
    	tmpInt = cal.get(cal.MINUTE);
    	if (tmpInt < 10)
    		tmpFileName += "0"; 
    	tmpFileName += tmpInt; // cal.get(cal.MINUTE);

    	switch (mode) {
    	case FilenamePathOnly:
        	tmpFileName = tmpFileName.substring(0, tmpFileName.length() - 13 - 11);
        	return tmpFileName;
    	case FilenameFileOnly:
        	tmpFileName = tmpFileName.substring(tmpFileName.length() - 13 - 11, tmpFileName.length());
        	break;
    	case FilenameFileUptoFileNameOnly:
        	tmpFileName = tmpFileName.substring(tmpFileName.length() - 13 - 10, tmpFileName.length() - 13);
        	break;
    	case FilenameUptoFile:
        	tmpFileName = tmpFileName.substring(0, tmpFileName.length() - 13) + "*" ;
        	break;
    	case FilenameUptoYear:
        	tmpFileName = tmpFileName.substring(0, tmpFileName.length() - 9) + "*" ;
        	break;
    	case FilenameUptoMonth:
        	tmpFileName = tmpFileName.substring(0, tmpFileName.length() - 7) + "*" ;
        	break;
    	case FilenameUptoDay:
        	tmpFileName = tmpFileName.substring(0, tmpFileName.length() - 4) + "*" ;
        	break;
    	case FilenameUptoHour:
        	tmpFileName = tmpFileName.substring(0, tmpFileName.length() - 2) + "*" ;
        	break;
    	case FilenameEntire:
        	// tmpFileName = tmpFileName.substring(0, tmpFileName.length() - 2) + "*" ;
        	break;
    	}
    	tmpFileName += ".msql";
    	return tmpFileName;
    }
    
    /** Retrieve data for connection*/
    private static String retrieveProps(String backupLocation, String mode) {
        // open file repository.xml
        // retrieve data:
        //  - driver
        //  - protocol
        //  - subprotocol
        //  - dbalias
        //  - username
        //  - password
        /*
        File file = null;
		file = new File("repository.xml");
		FileInputStream isf = null;
		if (!file.exists()) 
			return "error";
		if (!file.canRead())
			return "error";
		try {
	        isf = new FileInputStream(file);
		} catch (FileNotFoundException e) {
	        System.err.println("DBUtilities.checkDBData, input file opening error: "+e);
    		return "error";
		}
        */
        driverClassName = "com.mysql.jdbc.Driver";
        driverURL = "jdbc:mysql://localhost:3306/contaweb";
        columnNameQuote = "";
        urlTarget = "_BLANK"; 
        catalog = "";
        schema = "";
        tables = "";
        user = "root";
        // v0.9.9e - mabepi1903
        password = "daniele";
        // Date data = new Date();
        Calendar cal = Calendar.getInstance();
        switch (mode) {
            case "backup":
            	//path = backupLocation + "/ContaWebDB_" + cal.YEAR + cal.MONTH + cal.DAY_OF_MONTH + "_" + cal.HOUR_OF_DAY + cal.MINUTE + ".msql";
            	/* if (cal.get(cal.MONTH)<10) {
            	    path = backupLocation + "/ContaWebDB_" + cal.get(cal.YEAR) + "0" + cal.get(cal.MONTH) + cal.get(cal.DAY_OF_MONTH) + "_" + cal.get(cal.HOUR_OF_DAY) + cal.get(cal.MINUTE) + ".msql";
            	} else {
            	    path = backupLocation + "/ContaWebDB_" + cal.get(cal.YEAR) + cal.get(cal.MONTH) + cal.get(cal.DAY_OF_MONTH) + "_" + cal.get(cal.HOUR_OF_DAY) + cal.get(cal.MINUTE) + ".msql";
            	} */
            	path = calculateFilename(backupLocation, FilenameEntire);

            	/* path = backupLocation + "/ContaWebDB_" + cal.get(cal.YEAR);
            	if (cal.get(cal.MONTH)<10) 
            	    path += "0";
            	path += cal.get(cal.MONTH);
            	if (cal.get(cal.DAY_OF_MONTH)<10)
            		path += "0";
            	path += cal.get(cal.DAY_OF_MONTH) + "_";
            	if (cal.get(cal.HOUR_OF_DAY)<10)
            		path += "0"; 
            	path += cal.get(cal.HOUR_OF_DAY);
            	if (cal.get(cal.MINUTE)<10)
            		path += "0"; 
            	path += cal.get(cal.MINUTE) + ".msql"; */
            break;
            case "retrieve":
            	path = calculateFilename(backupLocation, FilenameFileOnly);
            	// path = backupLocation + "/ContaWebDB_" + cal.YEAR + cal.MONTH + cal.DAY_OF_MONTH + "_" + cal.HOUR_OF_DAY + cal.MINUTE + ".msql";
            break;
            default:
            	path = calculateFilename(backupLocation, FilenameEntire);
            	//path = backupLocation + "/ContaWebDB_" + cal.YEAR + cal.MONTH + cal.DAY_OF_MONTH + "_" + cal.HOUR_OF_DAY + cal.MINUTE + ".msql";
        }
        props = new Properties();
        props.setProperty("driver.class", driverClassName);
        props.setProperty("driver.url", driverURL);
        props.setProperty("columnName.quoteChar", columnNameQuote);
        props.setProperty("target", urlTarget);
        props.setProperty("catalog", catalog );
        props.setProperty("schemaPattern", schema);
        props.setProperty("tableName", tables); //"properties, Configurazioni, fornitori, listini, iva, pagamenti, pagamenti_eseguiti, autisti, agenti, categoria_articoli, destinazione_articoli, articoli, prezzi, destclienti, clienti, movimenti, telefonate, invddt, ddt, note_accredito, invnote_accredito, listiniassociati, sconti, fatture, giacenze, movimentigiacenze, bolleacquisto, invbolleacquisto, ordini, invordini, evasioniordini, ordinifornitori, invordinifornitori, richiesteOrdini");
        props.setProperty("user", user);
        props.setProperty("password", password);
        props.setProperty("path", path);
        
        return "success";
    }

    /** Check data in database 
     * @throws DataAccessException */
    public String checkDBData(VOElement element, String type) throws DataAccessException {
        // Userei una connessione jdbc diretta, usando una variabile d'appoggio, sulla quale
        // caricare tutti i dati, fornendo solo l'id di element.
        // Vado poi a paragonare element con la variabile temporanea e verifico se sono 
        // identici.
        // VOElement tmpElement = null;
        // tmpElement = findWithAllReferences(element, broker)
        PersistenceBroker broker = null;
        // try
        //{
          broker = PersistenceBrokerFactory.defaultPersistenceBroker();  
          // broker.beginTransaction();             
          // broker.commitTransaction();
        //} catch (Exception e) {
          // broker.abortTransaction();
          //throw new DataAccessException(e.getMessage());
        //} finally {
          //if (broker != null) {
            //broker.close();
        //}
        
        String retValue = "error";
        switch (type)
        {
            case "Articolo":
                Articolo articolo = new Articolo();
                articolo.setId(element.getId());
			    articolo = (Articolo) findWithAllReferences(articolo, broker);
                if (articolo == element)
                  retValue = "error";
                break;
            case "DDT":
                DDT ddt = new DDT();
                ddt.setId(element.getId());
			    ddt = (DDT) findWithAllReferences(ddt, broker);
                if (ddt == element)
                  retValue = "error";
                break;
            case "Giacenza":
                Giacenza giacenza = new Giacenza();
                giacenza.setId(element.getId());
			    giacenza = (Giacenza) findWithAllReferences(giacenza, broker);
                if (giacenza == element)
                  retValue = "error";
                break;
            case "NotaAccredito":
                NotaAccredito notaAccredito = new NotaAccredito();
                notaAccredito.setId(element.getId());
			    notaAccredito = (NotaAccredito) findWithAllReferences(notaAccredito, broker);
                if (notaAccredito == element)
                  retValue = "error";
                break;
            case "OrdineFornitore":
                OrdineFornitore ordineFornitore = new OrdineFornitore();
                ordineFornitore.setId(element.getId());
			    ordineFornitore = (OrdineFornitore) findWithAllReferences(ordineFornitore, broker);
                if (ordineFornitore == element)
                  retValue = "error";
                break;
            case "PuntoConsegna":
                PuntoConsegna puntoConsegna = new PuntoConsegna();
                puntoConsegna.setId(element.getId());
			    puntoConsegna = (PuntoConsegna) findWithAllReferences(puntoConsegna, broker);
                if (puntoConsegna == element)
                  retValue = "error";
                break;
        }
        if (broker != null) 
          broker.close();

        return retValue;
    }

    /** Dump the whole database to an SQL string */
    public static String dumpDB(String backupLocation) {
        String retValue = "";
        storeInFileMode = "Create";
        
        if (!"success".equalsIgnoreCase(retrieveProps(backupLocation, "backup")))
            return "error";

        // Default to not having a quote character
        DatabaseMetaData dbMetaData = null;
        Connection dbConn = null;
        try {
            Class.forName(driverClassName);
            dbConn = DriverManager.getConnection(driverURL, props);
            dbMetaData = dbConn.getMetaData();
        }
        catch( Exception e ) {
            System.err.println("DBUtilities.dumpDB() - Unable to connect to database: "+e);
            return "error";
        }

        try {
            StringBuffer result = new StringBuffer();
            ResultSet rs = dbMetaData.getTables(catalog, schema, tables, null);
            if (! rs.next()) {
                System.err.println("Unable to find any tables matching: catalog="+catalog+" schema="+schema+" tables="+tables);
                rs.close();
            } else {
                result.append("\ndrop database if exists contaweb;\n");
                result.append("\ncreate database if not exists contaweb;\n");
                result.append("\n\nuse contaweb;\n\n");

                // Right, we have some tables, so we can go to work.
                // the details we have are
                // TABLE_CAT String => table catalog (may be null)
                // TABLE_SCHEM String => table schema (may be null)
                // TABLE_NAME String => table name
                // TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
                // REMARKS String => explanatory comment on the table
                // TYPE_CAT String => the types catalog (may be null)
                // TYPE_SCHEM String => the types schema (may be null)
                // TYPE_NAME String => type name (may be null)
                // SELF_REFERENCING_COL_NAME String => name of the designated "identifier" column of a typed table (may be null)
                // REF_GENERATION String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)
                // We will ignore the schema and stuff, because people might want to import it somewhere else
                // We will also ignore any tables that aren't of type TABLE for now.
                // We use a do-while because we've already caled rs.next to see if there are any rows
                do {
                    String tableName = rs.getString("TABLE_NAME");
                    String tableType = rs.getString("TABLE_TYPE");
                    if ("TABLE".equalsIgnoreCase(tableType)) {
                        result.append("\n\n-- "+tableName);                // -- table_name
                        result.append("\nCREATE TABLE "+tableName+" (\n"); // CREATE TABLE table_name (
                        ResultSet tableMetaData = dbMetaData.getColumns(null, null, tableName, "%");
                        boolean firstLine = true;
                        while (tableMetaData.next()) {
                            if (firstLine) {
                                firstLine = false;
                            } else {
                                // If we're not the first line, then finish the previous line with a comma
                                result.append(",\n");
                            }
                            String columnName = tableMetaData.getString("COLUMN_NAME");
                            if (columnName.matches("key"))
                            	columnName = "`key`";
                            String columnType = tableMetaData.getString("TYPE_NAME");
                            String[] columnTypeArray = (columnType.split(" "));
                            // WARNING: this may give daft answers for some types on some databases (eg JDBC-ODBC link)
                            int columnSize = tableMetaData.getInt("COLUMN_SIZE");
                            String nullable = tableMetaData.getString("IS_NULLABLE");
                            String nullString = "NULL";
                            if ("NO".equalsIgnoreCase(nullable)) {
                                nullString = "NOT NULL";
                            }
                            // String extra = tableMetaData.getString("EXTRA"); // genera eccezione!
                            switch (columnTypeArray.length) {
                            case 1:
                            	if (columnType.equals("date") | columnType.equals("datetime") | columnType.equals("time") | columnType.equals("tinytext"))
                                    result.append("    "+columnNameQuote+columnName+columnNameQuote+" "+columnType+" "+nullString);
                            	else
                            		result.append("    "+columnNameQuote+columnName+columnNameQuote+" "+columnType+" ("+columnSize+")"+" "+nullString);
                                break;
                            case 2:
                                result.append("    "+columnNameQuote+columnName+columnNameQuote+" "+columnTypeArray[0]+" ("+columnSize+")"+" "+columnTypeArray[1]+" "+nullString);
                                break;
                            default:
                                System.err.println("Column Type contains more than 2 words: " + columnType);
                            	return ERROR;
                            }
                            	
                                                 //    columnName columnType (columnSize) nullString 
                        }
                        tableMetaData.close();

                        // Now we need to put the primary key constraint
                        try {
                            ResultSet primaryKeys = dbMetaData.getPrimaryKeys(catalog, schema, tableName);
                            // What we might get:
                            // TABLE_CAT String => table catalog (may be null)
                            // TABLE_SCHEM String => table schema (may be null)
                            // TABLE_NAME String => table name
                            // COLUMN_NAME String => column name
                            // KEY_SEQ short => sequence number within primary key
                            // PK_NAME String => primary key name (may be null)
                            String primaryKeyName = null;
                            StringBuffer primaryKeyColumns = new StringBuffer();
                            while (primaryKeys.next()) {
                                String thisKeyName = primaryKeys.getString("PK_NAME");
                                if ((thisKeyName != null && primaryKeyName == null)
                                        || (thisKeyName == null && primaryKeyName != null)
                                        || (thisKeyName != null && ! thisKeyName.equals(primaryKeyName))
                                        || (primaryKeyName != null && ! primaryKeyName.equals(thisKeyName))) {
                                    // the keynames aren't the same, so output all that we have so far (if anything)
                                    // and start a new primary key entry
                                    if (primaryKeyColumns.length() > 0) {
                                        // There's something to output
                                        result.append(",\n    PRIMARY KEY");
                                        // if (primaryKeyName != null) { result.append(primaryKeyName); }
                                        result.append("("+primaryKeyColumns.toString()+")");
                                    }
                                    // Start again with the new name
                                    primaryKeyColumns = new StringBuffer();
                                    primaryKeyName = thisKeyName;
                                }
                                // Now append the column
                                if (primaryKeyColumns.length() > 0) {
                                    primaryKeyColumns.append(", ");
                                }
                                primaryKeyColumns.append(primaryKeys.getString("COLUMN_NAME"));
                            }
                            if (primaryKeyColumns.length() > 0) {
                                // There's something to output
                                result.append(",\n    PRIMARY KEY ");
                                // if (primaryKeyName != null) { result.append(primaryKeyName); }
                                if (primaryKeyColumns.toString().matches("key"))
                                    result.append("(`"+primaryKeyColumns.toString()+"`)");
                                else 
                                    result.append("("+primaryKeyColumns.toString()+")");
                            }
                        } catch (SQLException e) {
                            // NB you will get this exception with the JDBC-ODBC link because it says
                            // [Microsoft][ODBC Driver Manager] Driver does not support this function
                            System.err.println("Unable to get primary keys for table "+tableName+" because "+e);
                        }

                        result.append("\n);\n");

                        // Right, we have a table, so we can go and dump it
                        if (dumpTable(dbConn, result, tableName).equalsIgnoreCase(ERROR))
                        	return ERROR;
                        
                        if (result.length()>threshold) {
                        	retValue = storeInFile(props, result, storeInFileMode);
                        	storeInFileMode = "Append";
                        	if (retValue.equalsIgnoreCase(ERROR))
                        		return ERROR;
                        	result.delete(0, result.length()-1);
                        }
                    }
                } while (rs.next());
                if (result.length()>0) {
                	retValue = storeInFile(props, result, storeInFileMode);
                	storeInFileMode = "Append";
                	if (retValue.equalsIgnoreCase(ERROR))
                		return ERROR;
                	result.delete(0, result.length()-1);
                }
                storeInFileMode = "Close";
            	retValue = storeInFile(props, result, storeInFileMode);
            	if (retValue.equalsIgnoreCase(ERROR))
            		return ERROR;
                
                rs.close();
            }
            dbConn.close();
            
            // retValue = storeInFile(props, result);

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            return "error";
        }
    	return retValue;
    }

    private static String storeInFile(Properties props, StringBuffer result, String mode) {
    	switch (mode) {
    		case "Create":
    			totalLengh = 0;
    			String path = props.getProperty("path");
    			fileAppend = new File(path);
    			if (fileAppend.exists()) 
    				return "error";
    		    try {
    		    	fileAppend.createNewFile();
    		  	} catch (IOException e) {
    		        System.err.println("DBUtilities.storeInFile - Append: output file creating error: "+e);
    		  		return "error";
    		  	}

    		    if (!fileAppend.exists())
    		  	    return "error";

    		    if (!fileAppend.canWrite())
    		  	  	return "error";

    		    try {
    		  	    ofAppend = new FileOutputStream(fileAppend);
    		  	} catch (FileNotFoundException e) {
    		  	    System.err.println("DBUtilities.storeInFile - Append: output file opening error: "+e);
    		  		return "error";
    		  	}

    		    mode = "Append";
    		    storeInFileMode = "Append";
    		case "Append":
    			byte[] byteResult = new byte[result.length()];
    			totalLengh = totalLengh + result.length();
    			for (int i=0; i<result.length(); i++)
    			    byteResult[i] = (byte)result.charAt(i);

    		    try {
    		        ofAppend.write(byteResult);
    			} catch (IOException e) {
    			    System.err.println("DBUtilities.storeInFile - Append: output file writing error: "+e);
    			    try {
    			        ofAppend.close();
    			    } catch (IOException e1) {
    			        System.err.println("DBUtilities.storeInFile - Append: output file closing error: "+e1);
    			    }
    			    return "error";
    			}

    		    try {
    			    ofAppend.flush();
    			} catch (IOException e) {
    			    System.err.println("DBUtilities.storeInFile - Append: output file flushing error: "+e);
    			    try {
    				    ofAppend.close();
    				} catch (IOException e1) {
    				    System.err.println("DBUtilities.storeInFile - Append: output file closing error: "+e1);
    				}
    				return "error";
    		    }

    			break;
    		case "Close":
    		    try {
    			    ofAppend.close();
    			} catch (IOException e) {
    			    System.err.println("DBUtilities.storeInFile - Append: output file closing error: "+e);
    			    return "error";
    			}
    			fileAppend.setReadOnly();
  		        if (fileAppend.length() == 0 | fileAppend.length() > totalLengh)
  		            return "error";
  		        return "success";
    		default:
    			return ERROR;
    	}
		return SUCCESS;
    }

    /** Store DB dumped data in file */
    private static String storeInFile(Properties props, StringBuffer result) {
      String path = props.getProperty("path");
      File file = null;
	  file = new File(path);
	  FileOutputStream of = null;
	  if (file.exists()) 
	    return "error";

      try {
	    file.createNewFile();
	  } catch (IOException e) {
        System.err.println("DBUtilities.storeInFile, output file creating error: "+e);
		return "error";
	  }

      if (!file.exists())
	    return "error";

      if (!file.canWrite())
	  	return "error";

      try {
	    of = new FileOutputStream(file);
	  } catch (FileNotFoundException e) {
	    System.err.println("DBUtilities.storeInFile, output file opening error: "+e);
		return "error";
	  }

	  byte[] byteResult = new byte[result.length()];
	  for (int i=0; i<result.length(); i++) 
	    byteResult[i] = (byte)result.charAt(i);

      try {
	    of.write(byteResult);
	  } catch (IOException e) {
	    System.err.println("DBUtilities.storeInFile, output file writing error: "+e);
	    try {
	      of.close();
  	    } catch (IOException e1) {
	      System.err.println("DBUtilities.storeInFile, output file closing error: "+e1);
	    }
	    return "error";
	  }

      try {
		of.flush();
	  } catch (IOException e) {
	    System.err.println("DBUtilities.storeInFile, output file flushing error: "+e);
		try {
		  of.close();
		} catch (IOException e1) {
		  System.err.println("DBUtilities.storeInFile, output file closing error: "+e1);
		}
		return "error";
      }

      try {
	    of.close();
	  } catch (IOException e) {
	    System.err.println("DBUtilities.storeInFile, output file closing error: "+e);
		return "error";
	  }

      file.setReadOnly();
      if (file.length() == 0 | file.length() > result.length())
        return "error";
      return "success";
    }

    /** dump this particular table to the string buffer */
    private static String dumpTable(Connection dbConn, StringBuffer result, String tableName) {
        try {
            // First we output the create table stuff
            PreparedStatement stmt = dbConn.prepareStatement("SELECT * FROM "+tableName);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            String retValue  = "";

            // Now we can output the actual data
            result.append("\n\n-- Data for "+tableName+"\n"); // -- DATA FOR table_name
            while (rs.next()) {
                result.append("INSERT INTO "+tableName+" VALUES ("); // INSERT TO table_name VALUES (
                for (int i=0; i<columnCount; i++) {
                    if (i > 0) {
                        result.append(", ");
                    }
                    Object value = rs.getObject(i+1);
                    if (value == null) {
                        result.append("NULL");
                    } else {
                        String outputValue = value.toString();
                        String replaceOutputValue;

                        if (outputValue.contains("\\")) {
                    		int occurencyIndex = outputValue.indexOf('\\');
                    		replaceOutputValue = outputValue;
                    		outputValue = "";
                    		while (occurencyIndex >= 0) {
                        		outputValue = outputValue + replaceOutputValue.substring(0, occurencyIndex);
                        		replaceOutputValue = (String) replaceOutputValue.subSequence(occurencyIndex + 1, replaceOutputValue.length());
                        		occurencyIndex = replaceOutputValue.indexOf('\\');
                        	}
                    		if (replaceOutputValue.length() > 0)
                        		outputValue = outputValue + replaceOutputValue;
                        }
                        
                        if (outputValue.contains("\"")) {
                    		int occurencyIndex = outputValue.indexOf('\"');
                    		int occurenciesNumber = outputValue.split("\"").length; 
                    		replaceOutputValue = outputValue;
                    		outputValue = "";
                    		for (int occurenciesCounter = 0; occurenciesCounter < occurenciesNumber; occurenciesCounter++) {
                        		outputValue = outputValue + replaceOutputValue.substring(0, occurencyIndex);
                        		outputValue = outputValue + '\\';
                        		outputValue = outputValue + "\"";
                        		replaceOutputValue = (String) replaceOutputValue.subSequence(occurencyIndex + 1, replaceOutputValue.length());
                        		occurencyIndex = replaceOutputValue.indexOf('\"');
                        	}
                    		if (replaceOutputValue.length() > 0)
                        		outputValue = outputValue + replaceOutputValue;
                        }

                        outputValue = "\""+outputValue+"\"";

                        while (outputValue.matches("''"))
                            outputValue = outputValue.replaceAll("\"\"","\"");
                        while (outputValue.matches("\\\\"))
                            outputValue = outputValue.replaceAll("\\\\","\\");
                        result.append(outputValue);  // 'value'
                    }
                    if (result.length()>threshold) {
                    	retValue = storeInFile(props, result, storeInFileMode);
                    	storeInFileMode = "Append";
                    	if (retValue.equalsIgnoreCase(ERROR))
                    		return ERROR;
                    	result.delete(0, result.length() + 1);
                    }
                }
                result.append(");\n");
                if (result.length()>threshold) {
                	retValue = storeInFile(props, result, storeInFileMode);
                	storeInFileMode = "Append";
                	if (retValue.equalsIgnoreCase(ERROR))
                		return ERROR;
                	result.delete(0, result.length()-1);
                }

            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Unable to dump table "+tableName+" because: "+e);
        }
        return SUCCESS;
    }
}