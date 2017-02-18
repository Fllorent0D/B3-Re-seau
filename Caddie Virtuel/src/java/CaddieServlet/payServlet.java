/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CaddieServlet;

import com.floca.BeanDBAccess.BeanDBAccessOracle;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Florent Cardoen
 */
public class payServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                HttpSession session = request.getSession(true);
            short primaryKey = 0;
            
            if(!request.getParameterMap().containsKey("card-number"))
              response.sendRedirect(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ "/WebCaddieVirtuel/pay.jsp");
            
            BeanDBAccessOracle bd;
     
            bd = new BeanDBAccessOracle("localhost", "1521", "shop", "oracle");
            bd.connect();
            bd.setAutoCommit(false);
            PreparedStatement prepareStatement;
        try {
            prepareStatement = bd.getConnection().prepareStatement("insert into factures (id_client, date_facturation, type_achat) values ("+ session.getAttribute("login")+", sysdate, 'SA')", new String[] { "ID_FACTURE" });
            prepareStatement.executeUpdate();

            ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
            
            if (null != generatedKeys && generatedKeys.next()) {
                 primaryKey = generatedKeys.getShort(1);
            }
            System.out.println(primaryKey);
            bd.executeUpdate("insert into items_facture (id_facture, numero_serie, prix) " +
                            "select "+ primaryKey +", re.numero_serie, ap.prix_vente_effectif " +
                            "from reservations re " +
                            "inner join appareils ap on re.NUMERO_SERIE = ap.NUMERO_SERIE " +
                            "where re.ID_CLIENT = "+ session.getAttribute("login"));
            
            bd.executeUpdate("UPDATE appareils ap " +
                            "set etat_situation = (select et.id_situation " +
                            "                      from etats_situation et" +
                            "                      where et.Description = 'Vendu Web') " +
                            "where ap.numero_serie IN (select it.numero_serie  " +
                            "                       from items_facture it " +
                            "                       where it.id_facture = "+ primaryKey +")");
            
            bd.executeUpdate("delete from reservations where id_client = "+ session.getAttribute("login"));
            
            bd.commit();

        } catch (SQLException ex) {
            Logger.getLogger(payServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

                
            
           
            
        response.sendRedirect(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ "/WebCaddieVirtuel/caddie.jsp");
    
            
            
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
