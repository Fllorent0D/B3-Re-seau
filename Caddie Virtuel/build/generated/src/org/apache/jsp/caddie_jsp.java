package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.ResultSet;
import java.sql.Statement;
import com.floca.BeanDBAccess.BeanDBAccessMysql;

public final class caddie_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
 if(session.getAttribute("login") == null)
   {
       session.setAttribute("errorLogin", "Vous devez etre connecté pour accéder à cette page.");
       response.sendRedirect(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ "/WebCaddieVirtuel");
   }
   BeanDBAccessMysql bd = new BeanDBAccessMysql("localhost", "3306", "applicweb", "", "societe");
   bd.connect();
   ResultSet rs = bd.executeQuery("SELECT * FROM produits");
   ResultSet count = bd.executeQuery("SELECT count(1) FROM caddies where client = "+ session.getAttribute("id"));
   count.next();
   bd.disconnect();

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html lang=\"en\">\n");
      out.write("  <head>\n");
      out.write("    <meta charset=\"utf-8\">\n");
      out.write("    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n");
      out.write("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n");
      out.write("    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->\n");
      out.write("    <meta name=\"description\" content=\"\">\n");
      out.write("    <meta name=\"author\" content=\"\">\n");
      out.write("    <link rel=\"icon\" href=\"../../favicon.ico\">\n");
      out.write("\n");
      out.write("    <title>Caddie</title>\n");
      out.write("\n");
      out.write("    <!-- Bootstrap core CSS -->\n");
      out.write("    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.4/css/bootstrap.min.css\" integrity=\"sha384-2hfp1SzUoho7/TsGGGDaFdsuuDL0LX2hnUp6VkX3CUQ2K4K+xjboZdsXyp4oUHZj\" crossorigin=\"anonymous\">   <!-- Custom styles for this template -->\n");
      out.write("\n");
      out.write("    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css\">   <!-- Custom styles for this template -->\n");
      out.write("\n");
      out.write("  </head>\n");
      out.write("\n");
      out.write("  <body style=\"padding-top:80px\">\n");
      out.write("\n");
      out.write("    <nav class=\"navbar navbar-fixed-top navbar-dark bg-inverse\">\n");
      out.write("      <a class=\"navbar-brand\" href=\"#\">Caddie Virtuel</a>\n");
      out.write("      <ul class=\"nav navbar-nav\">\n");
      out.write("        <li class=\"nav-item active\">\n");
      out.write("          <a class=\"nav-link\" href=\"#\">Déconnexion</a>\n");
      out.write("        </li>\n");
      out.write("       \n");
      out.write("      </ul>\n");
      out.write("      <ul class=\"nav nav-pills pull-xs-right\">\n");
      out.write("            <li class=\"nav-item\">\n");
      out.write("              <a class=\"nav-link active\" href=\"pay.jsp\">");
      out.print( count.getInt(1) );
      out.write(" <span class=\"fa fa-shopping-cart\" aria-hidden=\"true\"></span></a>\n");
      out.write("            </li>\n");
      out.write("            \n");
      out.write("          </ul>\n");
      out.write("    </nav>\n");
      out.write("\n");
      out.write("    <div class=\"container\">\n");
      out.write("     \n");
      out.write("        ");
 while (rs.next()) { 
      out.write("\n");
      out.write("        <div class=\"col-md-4\">\n");
      out.write("            <div class=\"card\">\n");
      out.write("                <img class=\"card-img-top\" src=\"");
      out.print( rs.getString("img") );
      out.write("\" alt=\"Card image cap\" height=\"180\" width=\"318\">\n");
      out.write("                <div class=\"card-block\">\n");
      out.write("                  <h4 class=\"card-title\">");
      out.print( rs.getString("nom") );
      out.write("</h4>\n");
      out.write("                  <p class=\"card-text\">");
      out.print( rs.getString("description") );
      out.write("</p>\n");
      out.write("                  ");
  
                    if(rs.getInt("stock") > 0){
                  
      out.write("\n");
      out.write("                  <a href=\"#\" class=\"btn btn-primary\">Ajouter au panier</a>\n");
      out.write("                  ");
 } else {
      out.write("\n");
      out.write("                  <p class=\"text-danger\">Produit indisponible pour le moment</p>\n");
      out.write("                  ");
 } 
      out.write("\n");
      out.write("\n");
      out.write("                </div>\n");
      out.write("              </div>\n");
      out.write("        </div>\n");
      out.write("        ");
 } 
      out.write("\n");
      out.write("\n");
      out.write("    </div><!-- /.container -->\n");
      out.write("\n");
      out.write("\n");
      out.write("  </body>\n");
      out.write("</html>\n");
      out.write("\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
