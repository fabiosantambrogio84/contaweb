package utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import stampemgr.StampeMgr;
import vo.Cliente;
import vo.DettaglioNotaAccredito;
import vo.NotaAccredito;
import vo.Pagamento;

public class ENoteCreditoHelper {

	private static final Logger logger = Logger.getLogger(ENoteCreditoHelper.class);
	
	private static final String PAESE = "IT";
	
	private static final String CODICE_FISCALE = "RBNGPP56A14L551I";
	
	private static final String PARTITA_IVA = "02471520243";
	
	private static final String FORMATO_TRASMISSIONE = "FPR12";
	
	private static final String DEFAULT_CODICE_DESTINATARIO = "0000000";
	
	private static final String TELEFONO = "0456550993";
	
	private static final String EMAIL = "INFO@URBANIALIMENTARI.COM";
	
	private static final String RAGIONE_SOCIALE = "AZIENDA COMMERCIALE URBANI GIUSEPPE";
	
	private static final String REGIME_FISCALE = "RF01";
	
	private static final String SEDE_INDIRIZZO = "VIA 11 SETTEMBRE";
	
	private static final String SEDE_NUMERO_CIVICO = "17";
	
	private static final String SEDE_CAP = "37035";
	
	private static final String SEDE_COMUNE = "SAN GIOVANNI ILARIONE";
	
	private static final String SEDE_PROVINCIA = "VR";
	
	private static final String SEDE_NAZIONE = "IT";
	
	private static final String TIPO_DOCUMENTO = "TD04";
	
	private static final String DIVISA = "EUR";
	
	private static final String COND_PAGAMENTO_COMPLETO = "TP02";
	
	private static final String FILE_SEPARATOR = "/";
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private String basePath;
	
	private Map<Integer, Cliente> clienti;
		
	private Map<Integer, List<NotaAccredito>> noteAccredito;
	
	private Base64 base64;
	
	public ENoteCreditoHelper(){
		this.base64 = new Base64();
	}
	
	public String getBasePath(){
		return basePath;
	}
	
	public void setBasePath(String basePath){
		this.basePath = basePath;
	}
	
	public Map<Integer, Cliente> getClienti(){
		return clienti;
	}
	
	public void setClienti(Map<Integer, Cliente> clienti){
		this.clienti = clienti;
	}
	
	public Map<Integer, List<NotaAccredito>> getNoteAccredito(){
		return noteAccredito;
	}
	
	public void setNoteAccredito(Map<Integer, List<NotaAccredito>> noteAccredito){
		this.noteAccredito = noteAccredito;
	}
	
	@SuppressWarnings("unchecked")
	public Integer createXml() throws Exception{
		Integer idEsportazione = null;
		Connection conn = null;
		
		try {
			/* Ottengo l'oggetto per collegarmi al db */
			DbUtils dbUtils = DbUtils.getInstance();
			
			/* Connessione al db */
			conn = dbUtils.getConnection();
			
			/* Recupero l'id dell'esportazione */
			idEsportazione = dbUtils.getSequenceNextVal(conn, "seq_e_fatturazione");
			
			/* Creo la cartella dell'esportazione */
			String directoryPath = basePath + FILE_SEPARATOR + idEsportazione;
			EFattureUtils.checkAndCreateDirectory(directoryPath);
			
			/* Creo un file xml per ogni cliente e note di credito */
			for(Map.Entry<Integer, Cliente> clientiEntry : clienti.entrySet()){
				
				/* Recupero i dati del cliente */
				Integer idCliente = clientiEntry.getKey();
				Cliente cliente = clientiEntry.getValue();
				
				/* Recupero le note di credito associate al cliente*/
				List<NotaAccredito> notaCredito = noteAccredito.get(idCliente);
				if(notaCredito != null && !notaCredito.isEmpty()){
					for(int i=0; i<notaCredito.size(); i++){
						
						/* Creo il nome del file */
						String fileName = PAESE + CODICE_FISCALE + "_"; 
						
						/* Recupero il progressivo del file */
						Integer idProgressivo = dbUtils.getSequenceNextVal(conn, "seq_e_fatturazione_file");
						
						/* Aggiorno il nome del file */
						fileName = fileName + StringUtils.leftPad(String.valueOf(idProgressivo), 5, '0')+".xml";
										
						StringWriter stringWriter = new StringWriter();
						XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
						XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);
						
						xMLStreamWriter.writeStartElement("p", "FatturaElettronica", "");
						xMLStreamWriter.writeAttribute("xmlns", "", "ds", "http://www.w3.org/2000/09/xmldsig#");
						xMLStreamWriter.writeAttribute("xmlns", "", "p", "http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2");
						xMLStreamWriter.writeAttribute("xmlns", "", "xsi", "http://www.w3.org/2001/XMLSchema-instance");
						xMLStreamWriter.writeAttribute("versione", "FPR12");
						xMLStreamWriter.writeAttribute("xsi", "", "schemaLocation", "http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2 http://www.fatturapa.gov.it/export/fatturazione/sdi/fatturapa/v1.2/Schema_del_file_xml_FatturaPA_versione_1.2.xsd");
									
						/* Creo il nodo 'FatturaElettronicaHeader' */
						xMLStreamWriter.writeStartElement("FatturaElettronicaHeader");
						
						/* Creo il nodo 'DatiTrasmissione' */
						createNodeDatiTrasmissione(xMLStreamWriter, cliente, idProgressivo);
						
						/* Creo il nodo 'CedentePrestatore' */
						createNodeCedentePrestatore(xMLStreamWriter);
						
						/* Creo il nodo 'CessionarioCommittente' */
						createNodeCessionarioCommittente(xMLStreamWriter, cliente);
						
						/* Chiudo il nodo 'FatturaElettronicaHeader'*/
						xMLStreamWriter.writeEndElement();
						
						/* Recupero la nota di credito */
						NotaAccredito notaAccredito = notaCredito.get(i);
						
						/* Recupero i dettagli della nota di credito */
						List<DettaglioNotaAccredito> dettagli = notaAccredito.getDettagliAccredito();
						
						xMLStreamWriter.writeStartElement("FatturaElettronicaBody");
					
						createNodeBodyDatiGenerali(xMLStreamWriter, notaAccredito);
						createNodeBodyDatiBeniServizi(xMLStreamWriter, notaAccredito, dettagli);
						createNodeBodyDatiPagamento(xMLStreamWriter, notaAccredito, dettagli);
						createNodeBodyAllegati(xMLStreamWriter, notaAccredito);
						
						/* Chiudo il nodo 'FatturaElettronicaBody'*/
						xMLStreamWriter.writeEndElement();
						
						xMLStreamWriter.writeEndElement();
						
						xMLStreamWriter.flush();
						xMLStreamWriter.close();
						
						String xmlString = stringWriter.getBuffer().toString();
						stringWriter.close();

						xmlString = transformToPrettyPrint(xmlString);
						//System.out.println(transformToPrettyPrint(xmlString));
						
						File f = new File(directoryPath+FILE_SEPARATOR+fileName);
						f.setReadable(true, false);
						f.setWritable(true, false);
						try (PrintWriter out = new PrintWriter(f)) {
							out.println(xmlString);
						}
					}
					
				}
			}
		} catch (Exception e) {
			logger.error("Errore nella creazione del file xml", e);
	        throw e;
	    } finally{
	    	if(conn != null){
	    		conn.close();
	    	}
	    }
		
		return idEsportazione;
	}
	
	public String createZip(String folderPath) throws Exception{
		long maxSize = 5000000;
		long currentSize = 0;
		int index = 1;
		Connection conn = null;
		FileOutputStream fos = null;
		ZipOutputStream zipOut = null;
		FileInputStream fis = null;
		
		String resultFileName = "";
		try{
			/* Ottengo l'oggetto per collegarmi al db */
			DbUtils dbUtils = DbUtils.getInstance();
			
			/* Connessione al db */
			conn = dbUtils.getConnection();
			
			/* Creo la mappa contenente i files da inserire negli zip */
			Map<Integer, List<File>> filesGrouped = new LinkedHashMap<Integer, List<File>>();
			filesGrouped.put(index, new ArrayList<File>());
			
			/* Popolo la mappa iterando sui files xml */
			File folder = new File(folderPath);
			if(folder.exists() && folder.isDirectory()){
				File[] files = folder.listFiles();
				if(files != null && files.length > 0 ){
					for(int i=0; i<files.length; i++){
						File file = files[i];
						long fileSize = file.length();
						if((currentSize + fileSize) < maxSize){
							currentSize = currentSize + fileSize;
							
							List<File> filesByIndex = filesGrouped.get(index);
							if(filesByIndex == null){
								filesByIndex = new ArrayList<File>();
							}
							filesByIndex.add(file);
							filesGrouped.put(index, filesByIndex);
							
						} else{
							currentSize = fileSize;
							index = index + 1;
							List<File> filesByIndex = new ArrayList<File>();
							filesByIndex.add(file);
							filesGrouped.put(index, filesByIndex);
						}
					}
				}
			}
			
			/* Creo un file zip per ogni chiave della mappa */
			for(Map.Entry<Integer, List<File>> entry : filesGrouped.entrySet()){
				
				/* Recupero il progressivo dello zip */
				Integer numProgrZip = dbUtils.getSequenceNextVal(conn, "seq_e_fatturazione_file_zip");
				
				/* Creo il nome del file zip */
				String fileName = PAESE + CODICE_FISCALE + "_" + StringUtils.leftPad(String.valueOf(numProgrZip), 5, '0')+".zip";
				
				/* Recupero la lista dei file da inserire nel file zip */		
				List<File> filesToZip = entry.getValue();
				
				/* Creo il file zip */
				File f = new File(folderPath + "/" + fileName);
				f.setReadable(true, false);
				f.setWritable(true, false);
				fos = new FileOutputStream(f);
		        zipOut = new ZipOutputStream(fos);
		        for (File fileToZip : filesToZip) {
		            fis = new FileInputStream(fileToZip);
		            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
		            zipOut.putNextEntry(zipEntry);
		 
		            byte[] bytes = new byte[1024];
		            int length;
		            while((length = fis.read(bytes)) >= 0) {
		                zipOut.write(bytes, 0, length);
		            }
		            fis.close();
		        }
		        zipOut.close();
		        fos.close();
		        
		        /* Elimino i file xml inseriti nel file zip */
		        EFattureUtils.removeFiles(filesToZip, new ArrayList<String>());
			}
			
			/* Creo il file zip contenente tutti i file zip */
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("yyyyMMdd_HHmmss");
			
			String fileName = "export_note_credito_elettroniche_"+sdf.format(new Date(System.currentTimeMillis()))+".zip";
			resultFileName = folderPath + "/" + fileName;
			
			List<String> fileNamesToExclude = new ArrayList<String>();
			fileNamesToExclude.add(fileName);
			
			File f = new File(resultFileName);
			f.setReadable(true, false);
			f.setWritable(true, false);
			
			fos = new FileOutputStream(f);
	        zipOut = new ZipOutputStream(fos);
			
			File[] filesZip = folder.listFiles();
			if(filesZip != null && filesZip.length > 0){
				for(int i=0; i<filesZip.length; i++){
					File file = filesZip[i];
					String filePath = file.getAbsolutePath(); 
					String fileToZipName = file.getName();
					if(filePath.endsWith("zip") && !fileToZipName.startsWith("export_note_credito_elettroniche")){
						fis = new FileInputStream(file);
			            ZipEntry zipEntry = new ZipEntry(fileToZipName);
			            zipOut.putNextEntry(zipEntry);
			 
			            byte[] bytes = new byte[1024];
			            int length;
			            while((length = fis.read(bytes)) >= 0) {
			                zipOut.write(bytes, 0, length);
			            }
			            fis.close();
					}
				}
				/* Elimino i file zip */
				EFattureUtils.removeFiles(new ArrayList<File>(Arrays.asList(filesZip)), fileNamesToExclude);
			}
			zipOut.close();
	        fos.close();
			
		} catch(Exception e){
			logger.error("Errore nella creazione del file zip", e);
	        throw e;
		} finally{
			if(conn != null){
				conn.close();
			}
			if(fos != null){
				fos.close();
			}
			if(zipOut != null){
				zipOut.close();
			}
			if(fis != null){
				fis.close();
			}
		}
		return resultFileName;
	}
	
	private void createNodeDatiTrasmissione(XMLStreamWriter xMLStreamWriter, Cliente cliente, Integer numProgr) throws Exception{
		xMLStreamWriter.writeStartElement("DatiTrasmissione");
		
		/* Creo il nodo 'IdTrasmittente' */
		xMLStreamWriter.writeStartElement("IdTrasmittente");
		
		/* Creo il nodo 'IdPaese' */
		xMLStreamWriter.writeStartElement("IdPaese");
		xMLStreamWriter.writeCharacters(PAESE);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'IdCodice' */
		xMLStreamWriter.writeStartElement("IdCodice");
		xMLStreamWriter.writeCharacters(CODICE_FISCALE);
		xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'IdTrasmittente' */
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'ProgressivoInvio' */
		String numProg_s = String.valueOf(numProgr);
		xMLStreamWriter.writeStartElement("ProgressivoInvio");
		xMLStreamWriter.writeCharacters(StringUtils.leftPad(numProg_s, 5, '0'));
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'FormatoTrasmissione' */
		xMLStreamWriter.writeStartElement("FormatoTrasmissione");
		xMLStreamWriter.writeCharacters(FORMATO_TRASMISSIONE);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'CodiceDestinatario' */
		xMLStreamWriter.writeStartElement("CodiceDestinatario");
		xMLStreamWriter.writeCharacters(DEFAULT_CODICE_DESTINATARIO);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'ContattiTrasmittente' */
//		xMLStreamWriter.writeStartElement("ContattiTrasmittente");
//		
//		/* Creo il nodo 'Telefono' */
//		xMLStreamWriter.writeStartElement("Telefono");
//		xMLStreamWriter.writeCharacters(TELEFONO);
//		xMLStreamWriter.writeEndElement();
//		
//		/* Creo il nodo 'Email' */
//		xMLStreamWriter.writeStartElement("Email");
//		xMLStreamWriter.writeCharacters(EMAIL);
//		xMLStreamWriter.writeEndElement();
//		
//		/* Chiudo il nodo 'ContattiTrasmittente' */
//		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'PECDestinatario' */
		String email = cliente.getEmailPec();
		if(email != null && !email.equals("")){
			xMLStreamWriter.writeStartElement("PECDestinatario");
			xMLStreamWriter.writeCharacters(email);
			xMLStreamWriter.writeEndElement();
		}
		
		xMLStreamWriter.writeEndElement();
	}
	
	private void createNodeCedentePrestatore(XMLStreamWriter xMLStreamWriter) throws Exception{
		xMLStreamWriter.writeStartElement("CedentePrestatore");
		
		/* Creo il nodo 'DatiAnagrafici' */
		xMLStreamWriter.writeStartElement("DatiAnagrafici");
		
		/* Creo il nodo 'IdFiscaleIVA' */
		xMLStreamWriter.writeStartElement("IdFiscaleIVA");
		
		/* Creo il nodo 'IdPaese' */
		xMLStreamWriter.writeStartElement("IdPaese");
		xMLStreamWriter.writeCharacters(PAESE);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'IdCodice' */
		xMLStreamWriter.writeStartElement("IdCodice");
		xMLStreamWriter.writeCharacters(PARTITA_IVA);
		xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'IdFiscaleIVA' */
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'CodiceFiscale' */
		xMLStreamWriter.writeStartElement("CodiceFiscale");
		xMLStreamWriter.writeCharacters(CODICE_FISCALE);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Anagrafica' */
		xMLStreamWriter.writeStartElement("Anagrafica");
		
		/* Creo il nodo 'Denominazione' */
		xMLStreamWriter.writeStartElement("Denominazione");
		xMLStreamWriter.writeCharacters(RAGIONE_SOCIALE);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Nome' */
		//xMLStreamWriter.writeStartElement("Nome");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Cognome' */
		//xMLStreamWriter.writeStartElement("Cognome");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Titolo' */
		//xMLStreamWriter.writeStartElement("Titolo");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'CodEORI' */
		//xMLStreamWriter.writeStartElement("CodEORI");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'Anagrafica' */
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'AlboProfessionale' */
		//xMLStreamWriter.writeStartElement("AlboProfessionale");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'ProvinciaAlbo' */
		//xMLStreamWriter.writeStartElement("ProvinciaAlbo");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'NumeroIscrizioneAlbo' */
		//xMLStreamWriter.writeStartElement("NumeroIscrizioneAlbo");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'DataIscrizioneAlbo' */
		//xMLStreamWriter.writeStartElement("DataIscrizioneAlbo");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'RegimeFiscale' */
		xMLStreamWriter.writeStartElement("RegimeFiscale");
		xMLStreamWriter.writeCharacters(REGIME_FISCALE);
		xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'DatiAnagrafici' */
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Sede' */
		xMLStreamWriter.writeStartElement("Sede");
		
		/* Creo il nodo 'Indirizzo' */
		xMLStreamWriter.writeStartElement("Indirizzo");
		xMLStreamWriter.writeCharacters(SEDE_INDIRIZZO);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'NumeroCivico' */
		xMLStreamWriter.writeStartElement("NumeroCivico");
		xMLStreamWriter.writeCharacters(SEDE_NUMERO_CIVICO);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'CAP' */
		xMLStreamWriter.writeStartElement("CAP");
		xMLStreamWriter.writeCharacters(SEDE_CAP);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Comune' */
		xMLStreamWriter.writeStartElement("Comune");
		xMLStreamWriter.writeCharacters(SEDE_COMUNE);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Provincia' */
		xMLStreamWriter.writeStartElement("Provincia");
		xMLStreamWriter.writeCharacters(SEDE_PROVINCIA);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Nazione' */
		xMLStreamWriter.writeStartElement("Nazione");
		xMLStreamWriter.writeCharacters(SEDE_NAZIONE);
		xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'Sede' */
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Contatti' */
		xMLStreamWriter.writeStartElement("Contatti");
		
		/* Creo il nodo 'Telefono' */
		xMLStreamWriter.writeStartElement("Telefono");
		xMLStreamWriter.writeCharacters(TELEFONO);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Email' */
		xMLStreamWriter.writeStartElement("Email");
		xMLStreamWriter.writeCharacters(EMAIL);
		xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'Contatti' */
		xMLStreamWriter.writeEndElement();
		
		xMLStreamWriter.writeEndElement();
	}
	
	private void createNodeCessionarioCommittente(XMLStreamWriter xMLStreamWriter, Cliente cliente) throws Exception{
		xMLStreamWriter.writeStartElement("CessionarioCommittente");
		
		/* Creo il nodo 'DatiAnagrafici' */
		xMLStreamWriter.writeStartElement("DatiAnagrafici");
		
		/* Creo il nodo 'IdFiscaleIVA' */
		String partitaIva = cliente.getPiva();
		if(partitaIva != null && !partitaIva.equals("")){
			xMLStreamWriter.writeStartElement("IdFiscaleIVA");
			
			/* Creo il nodo 'IdPaese' */
			xMLStreamWriter.writeStartElement("IdPaese");
			xMLStreamWriter.writeCharacters(PAESE);
			xMLStreamWriter.writeEndElement();
			
			/* Creo il nodo 'IdCodice' */
			xMLStreamWriter.writeStartElement("IdCodice");
			xMLStreamWriter.writeCharacters(cliente.getPiva());
			xMLStreamWriter.writeEndElement();
			
			/* Chiudo il nodo 'IdFiscaleIVA' */
			xMLStreamWriter.writeEndElement();
		}
		
		/* Creo il nodo 'CodiceFiscale' */
		xMLStreamWriter.writeStartElement("CodiceFiscale");
		xMLStreamWriter.writeCharacters(cliente.getCodiceFiscale());
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Anagrafica' */
		xMLStreamWriter.writeStartElement("Anagrafica");
		
		/* Creo il nodo 'Denominazione' */
		String ragioneSociale = cliente.getRs();
		if(ragioneSociale == null || ragioneSociale.equals("")){
			ragioneSociale = cliente.getRs2();
		}
		if(ragioneSociale == null || ragioneSociale.equals("")){
			ragioneSociale = cliente.getNome() + " " + cliente.getCognome();
		}
		xMLStreamWriter.writeStartElement("Denominazione");
		xMLStreamWriter.writeCharacters(ragioneSociale);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Nome' */
		//xMLStreamWriter.writeStartElement("Nome");
		//xMLStreamWriter.writeCharacters(cliente.getNome());
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Cognome' */
		//xMLStreamWriter.writeStartElement("Cognome");
		//xMLStreamWriter.writeCharacters(cliente.getCognome());
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Titolo' */
		//xMLStreamWriter.writeStartElement("Titolo");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'CodEORI' */
		//xMLStreamWriter.writeStartElement("CodEORI");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'Anagrafica' */
		xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'DatiAnagrafici' */
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Sede' */
		xMLStreamWriter.writeStartElement("Sede");
		
		/* Creo il nodo 'Indirizzo' */
		xMLStreamWriter.writeStartElement("Indirizzo");
		xMLStreamWriter.writeCharacters(cliente.getIndirizzo());
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'CAP' */
		xMLStreamWriter.writeStartElement("CAP");
		xMLStreamWriter.writeCharacters(cliente.getCap());
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Comune' */
		xMLStreamWriter.writeStartElement("Comune");
		xMLStreamWriter.writeCharacters(cliente.getLocalita());
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Provincia' */
		xMLStreamWriter.writeStartElement("Provincia");
		xMLStreamWriter.writeCharacters(cliente.getProv());
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Nazione' */
		xMLStreamWriter.writeStartElement("Nazione");
		xMLStreamWriter.writeCharacters(PAESE);
		xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'Sede' */
		xMLStreamWriter.writeEndElement();
				
		xMLStreamWriter.writeEndElement();
	}

	private void createNodeBodyDatiGenerali(XMLStreamWriter xMLStreamWriter, NotaAccredito notaCredito) throws Exception{
		xMLStreamWriter.writeStartElement("DatiGenerali");
	
		/* Creo il nodo 'DatiGeneraliDocumento' */
		xMLStreamWriter.writeStartElement("DatiGeneraliDocumento");
		
		/* Creo il nodo 'TipoDocumento' */
		xMLStreamWriter.writeStartElement("TipoDocumento");
		xMLStreamWriter.writeCharacters(TIPO_DOCUMENTO);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Divisa' */
		xMLStreamWriter.writeStartElement("Divisa");
		xMLStreamWriter.writeCharacters(DIVISA);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Data' */
		Date dataNotaCredito = (Date) notaCredito.getData();
		String dataNotaCredito_s = "";
		if(dataNotaCredito != null){
			dataNotaCredito_s = sdf.format(dataNotaCredito);
		}
		xMLStreamWriter.writeStartElement("Data");
		xMLStreamWriter.writeCharacters(dataNotaCredito_s);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Numero' */
		Integer numeroProgr = notaCredito.getNumeroProgressivo();
		String numero = "";
		if(numeroProgr != null && !numeroProgr.equals("")){
			numero = String.valueOf(numeroProgr);
		}
		xMLStreamWriter.writeStartElement("Numero");
		xMLStreamWriter.writeCharacters(numero);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'DatiRitenuta' */
		//xMLStreamWriter.writeStartElement("DatiRitenuta");
		
		/* Creo il nodo 'TipoRitenuta' */
		//xMLStreamWriter.writeStartElement("TipoRitenuta");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'ImportoRitenuta' */
		//xMLStreamWriter.writeStartElement("ImportoRitenuta");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'AliquotaRitenuta' */
		//xMLStreamWriter.writeStartElement("AliquotaRitenuta");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'CausalePagamento' */
		//xMLStreamWriter.writeStartElement("CausalePagamento");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'DatiRitenuta' */
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'DatiBollo' */
		//xMLStreamWriter.writeStartElement("DatiBollo");
		
		/* Creo il nodo 'BolloVirtuale' */
		//xMLStreamWriter.writeStartElement("BolloVirtuale");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'ImportoBollo' */
		//xMLStreamWriter.writeStartElement("ImportoBollo");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'DatiBollo' */
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'DatiCassaPrevidenziale' */
		//xMLStreamWriter.writeStartElement("DatiCassaPrevidenziale");
		
		/* Creo il nodo 'TipoCassa' */
		//xMLStreamWriter.writeStartElement("TipoCassa");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'AlCassa' */
		//xMLStreamWriter.writeStartElement("AlCassa");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'ImportoContributoCassa' */
		//xMLStreamWriter.writeStartElement("ImportoContributoCassa");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'ImponibileCassa' */
		//xMLStreamWriter.writeStartElement("ImponibileCassa");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'AliquotaIVA' */
		//xMLStreamWriter.writeStartElement("AliquotaIVA");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Ritenuta' */
		//xMLStreamWriter.writeStartElement("Ritenuta");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Natura' */
		//xMLStreamWriter.writeStartElement("Natura");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'RiferimentoAmministrazione' */
		//xMLStreamWriter.writeStartElement("RiferimentoAmministrazione");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'DatiCassaPrevidenziale' */
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'ScontoMaggiorazione' */
		//xMLStreamWriter.writeStartElement("ScontoMaggiorazione");
		
		/* Creo il nodo 'Tipo' */
		//xMLStreamWriter.writeStartElement("Tipo");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Percentuale' */
		//xMLStreamWriter.writeStartElement("Percentuale");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Importo' */
		//xMLStreamWriter.writeStartElement("Importo");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'ScontoMaggiorazione' */
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'ImportoTotaleDocumento' */
		BigDecimal totaleNotaCredito = notaCredito.getTotale();
		String totaleNotaCredito_s = "";
		if(totaleNotaCredito != null){
			totaleNotaCredito_s = totaleNotaCredito.setScale(2).toPlainString();
			xMLStreamWriter.writeStartElement("ImportoTotaleDocumento");
			xMLStreamWriter.writeCharacters(totaleNotaCredito_s);
			xMLStreamWriter.writeEndElement();
		}
		
		/* Creo il nodo 'Arrotondamento' */
		//xMLStreamWriter.writeStartElement("Arrotondamento");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Causale' */
		/* Se la lunghezza è maggiore di 200 devo creare un nuovo nodo contenente i successivi 200 caratteri */
		String causale = notaCredito.getCausale();
		if(causale != null && !causale.isEmpty()){
			int casualeLength = causale.length();
			if(casualeLength < 200){
				xMLStreamWriter.writeStartElement("Causale");
				xMLStreamWriter.writeCharacters(causale);
				xMLStreamWriter.writeEndElement();
			}else{
				String[] tokens = Iterables.toArray(Splitter.fixedLength(4).split(causale), String.class);
				if(tokens != null && tokens.length > 0){
					for(int j=0; j<tokens.length; j++){
						xMLStreamWriter.writeStartElement("Causale");
						xMLStreamWriter.writeCharacters(tokens[j]);
						xMLStreamWriter.writeEndElement();
					}
				}
			}
		} else{
			//xMLStreamWriter.writeStartElement("Causale");
			//xMLStreamWriter.writeCharacters("");
			//xMLStreamWriter.writeEndElement();
		}
				
		/* Creo il nodo 'Art73' */
		//xMLStreamWriter.writeStartElement("Art73");
		//xMLStreamWriter.writeCharacters("");
		//xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'DatiGeneraliDocumento' */
		xMLStreamWriter.writeEndElement();
		
		xMLStreamWriter.writeEndElement();
	}
		
	private void createNodeBodyDatiBeniServizi(XMLStreamWriter xMLStreamWriter, NotaAccredito notaCredito, List<DettaglioNotaAccredito> dettagliNotaCredito) throws Exception{
		xMLStreamWriter.writeStartElement("DatiBeniServizi");
		
		int numLineaIndex = 1;
		
		/* Creo i nodi 'DettaglioLinee' */
		if(dettagliNotaCredito != null && !dettagliNotaCredito.isEmpty()){
					
			for(DettaglioNotaAccredito dettaglioNotaCredito :  dettagliNotaCredito){
				/* Creo il nodo 'DettaglioLinee' */
				xMLStreamWriter.writeStartElement("DettaglioLinee");
				
				/* Creo il nodo 'NumeroLinea' */
				xMLStreamWriter.writeStartElement("NumeroLinea");
				xMLStreamWriter.writeCharacters(String.valueOf(numLineaIndex));
				xMLStreamWriter.writeEndElement();
				
				/* Creo il nodo 'Descrizione' */
				xMLStreamWriter.writeStartElement("Descrizione");
				xMLStreamWriter.writeCharacters(dettaglioNotaCredito.getDescrizioneArticolo());
				xMLStreamWriter.writeEndElement();
				
				/* Creo il nodo 'Quantita' */
				xMLStreamWriter.writeStartElement("Quantita");
				BigDecimal quantita = dettaglioNotaCredito.getQta();
				String quantita_s = "";
				if(quantita != null){
					quantita_s = quantita.toString();
				}
				xMLStreamWriter.writeCharacters(quantita_s);
				xMLStreamWriter.writeEndElement();
				
				/* Creo il nodo 'UnitaMisura' */
				xMLStreamWriter.writeStartElement("UnitaMisura");
				xMLStreamWriter.writeCharacters(dettaglioNotaCredito.getUm());
				xMLStreamWriter.writeEndElement();
				
				/* Creo il nodo 'PrezzoUnitario' */
				xMLStreamWriter.writeStartElement("PrezzoUnitario");
				BigDecimal prezzo = dettaglioNotaCredito.getPrezzo();
				String prezzo_s = "";
				if(prezzo != null){
					prezzo_s = prezzo.setScale(2).toPlainString();
				}
				xMLStreamWriter.writeCharacters(prezzo_s);
				xMLStreamWriter.writeEndElement();
				
				/* Creo il nodo 'PrezzoTotale' */
				xMLStreamWriter.writeStartElement("PrezzoTotale");
				BigDecimal prezzoTotale = dettaglioNotaCredito.getTotale();
				String prezzoTotale_s = "";
				if(prezzoTotale != null){
					prezzoTotale_s = prezzoTotale.setScale(2).toPlainString();
				}
				xMLStreamWriter.writeCharacters(prezzoTotale_s);
				xMLStreamWriter.writeEndElement();
				
				/* Creo il nodo 'AliquotaIVA' */
				xMLStreamWriter.writeStartElement("AliquotaIVA");
				Integer iva = dettaglioNotaCredito.getIva();
				String iva_s = "";
				if(iva != null){
					iva_s= String.valueOf(iva);
				}
				xMLStreamWriter.writeCharacters(iva_s);
				xMLStreamWriter.writeEndElement();
				
				/* Chiudo il nodo 'DettaglioLinee' */
				xMLStreamWriter.writeEndElement();
				
				numLineaIndex = numLineaIndex + 1;
			}
		}
		
		/* Creo il nodo 'DatiRiepilogo' */
		xMLStreamWriter.writeStartElement("DatiRiepilogo");
		
		/* Creo il nodo 'AliquotaIVA' */
		xMLStreamWriter.writeStartElement("AliquotaIVA");
		xMLStreamWriter.writeCharacters("0.00");
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'ImponibileImporto' */
		xMLStreamWriter.writeStartElement("ImponibileImporto");
		String imp_s = "";
		if(notaCredito.getTotaleImponibile() != null){
			imp_s = notaCredito.getTotaleImponibile().toString();
		}
		xMLStreamWriter.writeCharacters(imp_s);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'Imposta' */
		xMLStreamWriter.writeStartElement("Imposta");
		String imposta_s = "";
		if(notaCredito.getTotaleImposta() != null){
			imposta_s = notaCredito.getTotaleImposta().toString();
		}
		xMLStreamWriter.writeCharacters(imposta_s);
		xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'DatiRiepilogo' */
		xMLStreamWriter.writeEndElement();
				
		xMLStreamWriter.writeEndElement();
	}
	
	private void createNodeBodyDatiPagamento(XMLStreamWriter xMLStreamWriter, NotaAccredito notaCredito, List<DettaglioNotaAccredito> dettagliNotaCredito) throws Exception{
		xMLStreamWriter.writeStartElement("DatiPagamento");
		
		/* Creo il nodo 'CondizioniPagamento' */
		xMLStreamWriter.writeStartElement("CondizioniPagamento");
		xMLStreamWriter.writeCharacters(COND_PAGAMENTO_COMPLETO);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'DettaglioPagamento' */
		xMLStreamWriter.writeStartElement("DettaglioPagamento");
		
		/* Creo il nodo 'ModalitaPagamento' */
		String modalitaPagamento = "";
		Cliente cliente = notaCredito.getCliente();
		if(cliente != null){
			Pagamento pagamento = cliente.getPagamento();
			String descrPagamento = pagamento.getDescrizione();
			if(descrPagamento != null && !descrPagamento.equals("")){
				descrPagamento = descrPagamento.toLowerCase();
				if(descrPagamento.contains("contanti")){
					modalitaPagamento = "MP01";
				} else if(descrPagamento.contains("bonifico")){
					modalitaPagamento = "MP05";
				} else if(descrPagamento.contains("ricevuta bancaria")){
					modalitaPagamento = "MP12";
				}
			}
		}
		xMLStreamWriter.writeStartElement("ModalitaPagamento");
		xMLStreamWriter.writeCharacters(modalitaPagamento);
		xMLStreamWriter.writeEndElement();
		
		/* Creo il nodo 'ImportoPagamento' */
		BigDecimal totaleNotaCredito = new BigDecimal(0);
		if(dettagliNotaCredito != null && !dettagliNotaCredito.isEmpty()){
			for(DettaglioNotaAccredito dettaglio : dettagliNotaCredito){
				totaleNotaCredito = totaleNotaCredito.add(dettaglio.getTotale());
			}
		}
		
		String totaleNotaCredito_s = "";
		if(totaleNotaCredito != null){
			totaleNotaCredito_s = totaleNotaCredito.setScale(2).toPlainString();
		}
		xMLStreamWriter.writeStartElement("ImportoPagamento");
		xMLStreamWriter.writeCharacters(totaleNotaCredito_s);
		xMLStreamWriter.writeEndElement();
		
		/* Chiudo il nodo 'DettaglioPagamento' */
		xMLStreamWriter.writeEndElement();
				
		xMLStreamWriter.writeEndElement();
	}
	
	private void createNodeBodyAllegati(XMLStreamWriter xMLStreamWriter, NotaAccredito notaCredito) throws Exception{
		ByteArrayOutputStream baos = null;
        ZipOutputStream zos = null;
		
        try{
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("yy");
			
			xMLStreamWriter.writeStartElement("Allegati");
			
			/* Creo il nome dell'allegato */
			String nomeAttachment = "nota_credito_"+notaCredito.getNumeroProgressivo() + "/" + sdf.format(notaCredito.getData())+".pdf";
			
			/* Creo il nodo 'NomeAttachment' */
			xMLStreamWriter.writeStartElement("NomeAttachment");
			xMLStreamWriter.writeCharacters(nomeAttachment + ".zip");
			xMLStreamWriter.writeEndElement();
			
			/* Creo il nodo 'AlgoritmoCompressione' */
			xMLStreamWriter.writeStartElement("AlgoritmoCompressione");
			xMLStreamWriter.writeCharacters("ZIP");
			xMLStreamWriter.writeEndElement();
			
			/* Creo il nodo 'FormatoAttachment' */
			xMLStreamWriter.writeStartElement("FormatoAttachment");
			xMLStreamWriter.writeCharacters("PDF");
			xMLStreamWriter.writeEndElement();
			
			/* Creo il pdf della nota di credito */
			try{
				byte[] pdfBytes = StampeMgr.getInstance().richiediPDFDocumento(notaCredito);
				
				/* Creo lo zip contenente il pdf */
				baos = new ByteArrayOutputStream();
		        zos = new ZipOutputStream(baos);
		        ZipEntry entry = new ZipEntry(nomeAttachment);
		        zos.putNextEntry(entry);
		        zos.write(pdfBytes);
		        zos.closeEntry();
		                
				/* File pdf in base64 */
				String encodedPdf = new String(base64.encode(baos.toByteArray()));
				
				/* Creo il nodo 'Attachment' */
				xMLStreamWriter.writeStartElement("Attachment");
				xMLStreamWriter.writeCharacters(encodedPdf);
				xMLStreamWriter.writeEndElement();
				
			} catch(Exception e){
				logger.error("Errore nella creazione del documento pdf per la nota di credito '" + notaCredito.getId()+"'", e);
			}
			
			xMLStreamWriter.writeEndElement();
			
			zos.close();
			baos.close();
			
		} catch(Exception e){
			logger.error("Errore nella creazione del tag 'Allegati' per la nota di credito '" + notaCredito.getId()+"'", e);
		} finally{
			if(zos != null){
				zos.close();
			}
			if(baos != null){
				baos.close();
			}
		}
	}	
	
	private String transformToPrettyPrint(String xml) throws Exception{
	    Transformer t = TransformerFactory.newInstance().newTransformer();
	    t.setOutputProperty(OutputKeys.INDENT, "yes");
	    t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	    Writer out = new StringWriter();
	    t.transform(new StreamSource(new StringReader(xml)), new StreamResult(out));
	    return out.toString();
	}
		
}
