<%-- 
    Document   : index
    Created on : 11 oct. 2016, 15:57:56
    Author     : Florent Cardoen
--%>

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

    <title>Starter Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.4/css/bootstrap.min.css" integrity="sha384-2hfp1SzUoho7/TsGGGDaFdsuuDL0LX2hnUp6VkX3CUQ2K4K+xjboZdsXyp4oUHZj" crossorigin="anonymous">   <!-- Custom styles for this template -->
    <% String s = "Florent"; %>
  </head>

  <body style="padding-top:80px">

    <nav class="navbar navbar-fixed-top navbar-dark bg-inverse">
      <a class="navbar-brand" href="#">Caddie Virtuel</a>
      
    </nav>

    <div class="container">

    <h2>Veuillez vous connecter</h2>
    <% if(session.getAttribute("errorLogin") != null){ out.println("<div class=\"alert alert-danger\">"+ session.getAttribute("errorLogin") +"</div>"); session.setAttribute("errorLogin", null);} %>

    <form action="login" method="POST">
        <div class="form-group row">
          <label for="inputEmail3" class="col-sm-2 col-form-label">Login</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" name="login" placeholder="Login">
          </div>
        </div>
        <div class="form-group row">
          <label for="inputPassword3" class="col-sm-2 col-form-label">Mot de passe</label>
          <div class="col-sm-10">
            <input type="password" class="form-control" name="motdepasse" placeholder="Password">
          </div>
        </div>
        
          
        <div class="form-group row">
         
          <div class="col-sm-10">
            <div class="form-check">
              <label class="form-check-label">
                <input class="form-check-input" name="nouveauclient" type="checkbox"> Je suis un nouveau client
              </label>
            </div>
          </div>
        </div>
        <div class="form-group row">
          <div class="offset-sm-2 col-sm-10">
            <button type="submit" class="btn btn-primary">Commencer</button>
          </div>
        </div>
      </form>

    </div><!-- /.container -->


  </body>
</html>

