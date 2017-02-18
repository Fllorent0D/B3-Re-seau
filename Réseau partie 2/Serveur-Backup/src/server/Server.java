/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import dismap.ServerDISMAP;
import redmmp.ServerREDMMP;

/**
 *
 * @author bastin
 */
public class Server {
    
    public Server() {
        new ServerDISMAP(null).start();
        new ServerREDMMP(null).start();
        //new ServerINDEP(null).start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Server();
    }
    
}
