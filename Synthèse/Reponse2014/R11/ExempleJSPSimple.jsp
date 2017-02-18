<%@ page language="java" %>
<%@page contentType="text/html"%> <HTML>
<HEAD>
<TITLE>Bonjour a repetition</TITLE>
<BODY>
*** Accueil dans le monde des JSP *** <p>
<% int nbre = Integer.parseInt(request.getParameter("nombre")); for (int i=0; i<nbre; i++)
{ %>
Bonjour ! <BR>
<% }
%> </P>
Cela nous fait <%=nbre %> fois ... </BODY>
</HTML>