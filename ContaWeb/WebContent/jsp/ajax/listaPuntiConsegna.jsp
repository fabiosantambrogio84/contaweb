<%@ taglib prefix="s" uri="/struts-tags" %>

<s:select labelposition="left" label="Punto Consegna" list="listPuntiConsegna" listKey="id" listValue="nomeLocalita"
value="%{idPuntoConsegna}" required="true" name="idPuntoConsegna" cssClass="testo" onchange="dojo.event.topic.publish('/calcolaStatistiche');"/>