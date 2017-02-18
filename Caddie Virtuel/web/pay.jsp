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
   ResultSet rs = bd.executeQuery("SELECT ap.prix_vente_effectif as prixvente, ta.type as type, ta.marque as marque, ta.libelle as libelle FROM reservations re "
           + "inner join appareils ap on re.numero_serie = ap.numero_serie " +
           " inner join type_appareils ta on ap.type_precis = ta.id_type_appareil " +
           "where re.id_client = " + session.getAttribute("login"));
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

  </head>

  <body style="padding-top:80px">

    <nav class="navbar navbar-fixed-top navbar-dark bg-inverse">
      <a class="navbar-brand" href="#">Caddie Virtuel</a>
      <ul class="nav navbar-nav">
        <li class="nav-item active">
          <a class="nav-link" href="#">Déconnexion</a>
        </li>
       
      </ul>
    </nav>

    <div class="container">
        <h3>Votre caddie</h3>
        <table class="table">
            <thead class="thead-inverse">
              <tr>
                <th>Type</th>
                <th>Article</th>
                <th>Prix</th>
              </tr>
            </thead>
            <tbody>
               <% while (rs.next()) { %>
                <tr>
 
                    <td><%= rs.getString("type") %></td>
                    <td><%= rs.getString("libelle") %></td>
                    <td><%= rs.getInt("prixvente") %>€</td>
                </tr>
            <% } %>
             
            
            </tbody>
        </table>
        <form class="form-horizontal" action="pay" method="POST" role="form">

          <h3>Payer</h3>
          <div class="form-group row ">
            <label class="col-sm-3 control-label" for="card-holder-name">Titulaire de la carte</label>
            <div class="col-sm-9">
              <input type="text" class="form-control" name="card-holder-name" id="card-holder-name" placeholder="Nom">
            </div>
          </div>
          <div class="form-group row">
            <label class="col-sm-3 control-label" for="card-number">Numéro de la carte</label>
            <div class="col-sm-9">
              <input type="text" class="form-control" name="card-number" id="card-number" placeholder="Numéro">
            </div>
          </div>
          <div class="form-group row">
            <label class="col-sm-3 control-label" for="expiry-month">Date d'expiration</label>
            <div class="col-sm-9">
              <div class="row">
                <div class="col-xs-3">
                  <select class="form-control col-sm-2" name="expiry-month" id="expiry-month">
                    <option>Mois</option>
                    <option value="01">Janvier (01)</option>
                    <option value="02">Fevrier (02)</option>
                    <option value="03">Mars (03)</option>
                    <option value="04">Avril (04)</option>
                    <option value="05">Mai (05)</option>
                    <option value="06">Juin (06)</option>
                    <option value="07">Juilleit (07)</option>
                    <option value="08">Aout (08)</option>
                    <option value="09">Septembre (09)</option>
                    <option value="10">Octobre (10)</option>
                    <option value="11">Novembre (11)</option>
                    <option value="12">Decembre (12)</option>
                  </select>
                </div>
                <div class="col-xs-3">
                  <select class="form-control" name="expiry-year" >
                    <option value="13">2013</option>
                    <option value="14">2014</option>
                    <option value="15">2015</option>
                    <option value="16">2016</option>
                    <option value="17">2017</option>
                    <option value="18">2018</option>
                    <option value="19">2019</option>
                    <option value="20">2020</option>
                    <option value="21">2021</option>
                    <option value="22">2022</option>
                    <option value="23">2023</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
          <div class="form-group row">
            <label class="col-sm-3 control-label" for="cvv">Code de sécurité</label>
            <div class="col-sm-3">
              <input type="text" class="form-control" name="cvv" id="cvv" placeholder="Security Code" >
            </div>
          </div>
          <div class="form-group row">
            <div class="col-sm-offset-3 col-sm-9">
              <input type="submit" class="btn btn-success" value="Payer">
            </div>
          </div>
      </form>
       
    </div><!-- /.container -->


  </body>
</html>

