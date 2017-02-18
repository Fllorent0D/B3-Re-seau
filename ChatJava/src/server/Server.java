/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import server_mucop.ServerMUCOP;

/**
 *
 * @author bastin
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ServerMUCOP(6002, 10, null).start();

    }
    
}
