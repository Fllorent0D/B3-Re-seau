<%-- 
    Document   : caddie
    Created on : 12 oct. 2016, 18:58:17
    Author     : Florent Cardoen
--%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.floca.BeanDBAccess.BeanDBAccessOracle"%>
<% if(session.getAttribute("login") == null)
   {
       session.setAttribute("errorLogin", "Vous devez etre connecté pour accéder à cette page.");
       response.sendRedirect(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ "/WebCaddieVirtuel");
   }
   BeanDBAccessOracle bd = new BeanDBAccessOracle("localhost", "1521", "shop", "oracle");
   bd.connect();
   ResultSet rs = bd.executeQuery("SELECT * FROM type_appareils ta"
           + " WHERE EXISTS("
           + " SELECT * FROM APPAREILS ap"
           + " INNER JOIN ETATS_SITUATION et ON ap.etat_situation = et.id_situation"
           + " where et.description IN ('WA', 'SA')"
           + " and ap.type_precis = ta.ID_TYPE_APPAREIL)");
   ResultSet count = bd.executeQuery("SELECT count(*) as countt FROM reservations where id_client = "+ session.getAttribute("login"));
   count.last();
   bd.disconnect();
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>Caddie</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.4/css/bootstrap.min.css" integrity="sha384-2hfp1SzUoho7/TsGGGDaFdsuuDL0LX2hnUp6VkX3CUQ2K4K+xjboZdsXyp4oUHZj" crossorigin="anonymous">   <!-- Custom styles for this template -->

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css">   <!-- Custom styles for this template -->

  </head>

  <body style="padding-top:80px">

    <nav class="navbar navbar-fixed-top navbar-dark bg-inverse">
      <a class="navbar-brand" href="#">Caddie Virtuel</a>
      <ul class="nav navbar-nav">
        <li class="nav-item active">
          <a class="nav-link" href="#">Déconnexion</a>
        </li>
       
      </ul>
      <ul class="nav nav-pills pull-xs-right">
            <li class="nav-item">
              <a class="nav-link active" href="pay.jsp"><%= count.getInt("countt") %> <span class="fa fa-shopping-cart" aria-hidden="true"></span></a>
            </li>
            
          </ul>
    </nav>

    <div class="container">
 <% if(session.getAttribute("flash") != null){ out.println("<div class=\"alert alert-danger\">"+ session.getAttribute("flash") +"</div>"); session.setAttribute("flash", null);} %>

        <% while (rs.next()) { %>
        <div class="col-md-4">
            <div class="card">
                <div class="card-block">
                  <h4 class="card-title"><%= rs.getString("type") %></h4>
                  <p class="card-text"><%= rs.getString("LIBELLE") %></p>

                  <a href="caddie?product=<%= rs.getInt("id_type_appareil") %>" class="btn btn-primary">Ajouter au panier</a>
                 
                </div>
              </div>
        </div>
        <% } %>

    </div><!-- /.container -->


  </body>
</html>

