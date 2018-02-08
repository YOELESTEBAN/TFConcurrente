/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Yoni
 */
public class CaidaRapida {
    
    private ArrayList medios;
    private ArrayList aerosillas;
    
    public CaidaRapida (){
        int i;
        Medio m;
        Aerosilla aero;
        medios= new ArrayList(5);
        aerosillas= new ArrayList(5);
        for (i=1;i<5;i++){
            m= new Medio(i);
            aero= new Aerosilla(i,m);
            medios.add(m);
            aerosillas.add(aero);
        }
    }
    
    public void entrarMedio (int tipo){
        System.out.println(Thread.currentThread().toString() + " quiere entrar al medio "+ tipo);
        Medio m= (Medio) (medios.get(tipo-1));
        m.usarMedio();
        
    }
    public void startAerosillas(){
        int i;
        Aerosilla aero;
        for (i=1;i<5;i++){
            aero= (Aerosilla)(this.aerosillas.get(i-1));
            aero.start();
        }
    }
    /* private Semaphore medio1;
    private Semaphore medio2;
    private Semaphore medio3;
    private Semaphore medio4;
    

    public CaidaRapida(){
        this.medio1= new Semaphore(1);
        this.medio2= new Semaphore(2);
        this.medio3= new Semaphore(3);
        this.medio4= new Semaphore(4);
    }
    public void usarMedio(int tipoDeMedio) {
         switch (tipoDeMedio) {
            case 1:  medio1.acquire();
                     break;
    }
    */
}
