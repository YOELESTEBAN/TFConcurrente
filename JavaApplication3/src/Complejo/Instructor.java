/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import java.util.ArrayList;

/**
 *
 * @author Latitude
 */
public class Instructor extends Thread { //Hilo

    private boolean esDeSky;
    private int id;
    private ArrayList espera;

    public Instructor(int id) {
        this.id = id;
        esDeSky = false;
        espera=new ArrayList(4);
    }

    public boolean getEsDeSky() {
        return esDeSky;
    }

    public void setEsDeSky(boolean es) {//Es el primero que espera
        this.esDeSky = es;
        
    }
    
    public void insertarEspera(Esquiador e){
        espera.add(e);
        if (espera.size()==4){
            this.notify();
        }
    }
    public void vaciarEspera(){
        for (int aux=0;aux<4;aux++){
            espera.remove(aux);
        }
    }
    public boolean estaLibre() {
        return (espera.isEmpty());
    }
    

    @Override
    public void run() {
        try {
            synchronized (this) {
                wait();
            }
            //empezarContar();
        } catch (InterruptedException ex) {
            System.out.println("ERROR SEVERO en runInstructor " + Thread.currentThread().toString());
        }
    }

    
}
