/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import compta.bimap.ServerBIMAP;
import compta.payp.ServerPAYP;
import dismap.ServerDISMAP;
import payp2.ServerPAYP2;

/**
 *
 * @author bastin
 */
public class Test extends Thread {
    public void run()
    {
        new ServerDISMAP(null).start();
        new ServerBIMAP(null).start();
        new ServerPAYP(null).start();
        new ServerPAYP2(null).start();
    }
    
    public static void main(String[] args) {
        new Test().start();
    }
}
