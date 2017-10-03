/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Yoni
 */
public class Test {
    
    public static void main(String[] args) throws InterruptedException {
       
        Confiteria conf= new Confiteria();
        ArrayList esqVector= new ArrayList (10);
        Random r= new Random ();
        
        int aux;
        for (aux=0; aux<5; aux++){
            String nombre="E"+aux;
            esqVector.add(new Esquiador (nombre, 1,conf));
        }
        for (aux=0; aux<5; aux++){
            ((Esquiador)esqVector.get(aux)).start();
            Thread.sleep(r.nextInt(1000));
        }
        
    }
    
}
/*for (int i=0; i<20;i++){
            System.out.println (r.nextInt(6)+1 );
            System.out.println (r.nextBoolean());
        }*/