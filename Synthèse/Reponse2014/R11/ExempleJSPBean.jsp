<%@page language="java" %> 
<%@page contentType="text/html"%> 
<%@page pageEncoding="UTF-8"%>
<jsp:useBean id="thePreferences" scope="page" class="Preferences.PreferencesWeb" /> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html> 
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>JSP Page with bean PreferencesWeb - Yeahh ... </title> 
	</head>
 <body bgcolor="<jsp:getProperty name="thePreferences" property="couleurFond" />" > 
 	You know what ? I'm happy
	<p>(Pierre Rapsat, folder de son dernier album)
	<HR>
	<p>Le caddie virtuel sera calculé en : <jsp:getProperty name="thePreferences" property="uniteMonetaire" />
 </body> 
</html>
