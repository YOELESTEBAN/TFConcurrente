/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

/**
 *
 * @author Yoni
 */
public class Test {
    
    public static void main(String[] args) {
        CaidaRapida complejo= new CaidaRapida ();
        Confiteria conf= new Confiteria();
        Esquiador e1= new Esquiador ("e1", complejo,1,conf);
        Esquiador e2= new Esquiador ("e2", complejo,1,conf);
        Esquiador e3= new Esquiador ("e3", complejo,1,conf);
        Esquiador e4= new Esquiador ("e4", complejo,1,conf);
        e1.start();
        e2.start();
        e3.start();
        e4.start();
        
    }
    
}
