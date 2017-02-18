/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CaddieServlet;

import com.floca.BeanDBAccess.BeanDBAccessMysql;
import com.floca.BeanDBAccess.BeanDBAccessOracle;
import java.io.IOException;
import java.io.PrintWriter;
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
public class caddieServlet extends HttpServlet {

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
            throws ServletException, IOException{
        HttpSession session = request.getSession(true);

        response.setContentType("text/html;charset=UTF-8");
        if(request.getParameter("product") == null)
              response.sendRedirect(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ "/WebCaddieVirtuel/caddie.jsp");
    
           int produit = Integer.parseInt(request.getParameter("product"));
           Integer client = (Integer) session.getAttribute("login");
           
           int quantite = 1;
           
           
            try {
                BeanDBAccessOracle bd = new BeanDBAccessOracle("localhost", "3306", "shop", "shop");

                bd.connect();
                
                ResultSet rs = bd.executeQuery("select numero_serie\n" +
                        "from appareils ap " +
                        "INNER JOIN ETATS_SITUATION et ON ap.etat_situation = et.id_situation " +
                        "where et.description IN ('WA', 'SA') " +
                        "and type_precis = "+produit+
                        " and  ROWNUM = 1 ");
                rs.next();
                bd.executeUpdate("INSERT INTO caddies (client, numero_serie, date_reservation) values ('"+client.shortValue()+"', '"+rs.getString("numero_serie")+"', NOW())");


            } catch (SQLException | ClassNotFoundException ex) {
               
                Logger.getLogger(caddieServlet.class.getName()).log(Level.SEVERE, null, ex);
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
