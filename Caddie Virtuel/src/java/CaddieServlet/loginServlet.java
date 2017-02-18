/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CaddieServlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.floca.BeanDBAccess.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
/**
 *
 * @author Florent Cardoen
 */
public class loginServlet extends HttpServlet {

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
        //response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        
        //PrintWriter out = response.getWriter();
            /* TODO output your page here. You may use following sample code. */
            if(request.getParameterMap().containsKey("login"))
            {
                if(request.getParameter("motdepasse").isEmpty())
                {
                    session.setAttribute("errorLogin", "Le mot de passe ne peut pas être vide.");
                    response.sendRedirect(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ "/WebCaddieVirtuel");
    
                }
                BeanDBAccessOracle bd;

                
                    bd = new BeanDBAccessOracle("localhost", "1521", "shop", "oracle");
                    bd.connect();
                    
                    //Nouveau client
                    if(request.getParameter("nouveauclient") != null)
                    {
                        HashMap<String, String> ajoutU = new HashMap<>();
                        ajoutU.put("PASSWORD", request.getParameter("motdepasse"));
                        ajoutU.put("LOGIN", request.getParameter("login"));
                        try {
                            bd.ecriture("clients", ajoutU);
                            //session.setAttribute("login", id);
                            
                            //response.sendRedirect(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ "/WebCaddieVirtuel/caddie.jsp");

                            
                        } catch (SQLException ex) {
                            Logger.getLogger(loginServlet.class.getName()).log(Level.SEVERE, null, ex);

                            session.setAttribute("errorLogin", "L'utilisateur existe déjà");
                            response.sendRedirect(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ "/WebCaddieVirtuel");
                             
                        }
                    }
                    
                    ResultSet rse;

                    rse = bd.executeQuery("Select id_client from clients where login = '"+request.getParameter("login")+"' and password = '"+request.getParameter("motdepasse")+"'");                       
                    try { 
                        rse.last();
                        int count = rse.getRow();
                        if(count == 1) 
                        {
                            session.setAttribute("login", rse.getInt("id_client"));
                            response.sendRedirect(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ "/WebCaddieVirtuel/caddie.jsp");
                            //RequestDispatcher dd = request.getRequestDispatcher("caddie.jsp");
                            //dd.forward(request, response);
                        }
                        else
                        {
                            session.setAttribute("errorLogin", "Login ou mot de passe incorrect");
                            response.sendRedirect(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ "/WebCaddieVirtuel");

                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(loginServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   

                    
                
                
            }
            else
            {
                    session.setAttribute("errorLogin", "Veuillez remplir les champs de connexion");
                    response.sendRedirect(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ "/WebCaddieVirtuel");
      
            }
              

            
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
