<%@ taglib prefix="s" uri="/struts-tags" %>

<h3>Statistiche</h3>

<s:url id="getStatistiche" action="getStatisticheOrdine"/>

<s:div errorText="Errore. Riprovare" listenTopics="/calcolaStatistiche" theme="ajax" formId="ordiniForm" href="%{getStatistiche}" loadingText="Caricamento statistiche"/>