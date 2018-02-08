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
public class Aerosilla extends Thread {

    private int id;
    private Medio medio;

    public Aerosilla(int id, Medio m) {
        this.id = id;
        this.medio = m;
    }

    public void run() {
        try {
            Thread.sleep(1000);
            while (true) {
                this.medio.entraAerosilla();
                Thread.sleep(100);//Duerme el hilo por 0.1 segundo
                this.medio.saleAerosilla();
                Thread.sleep(5 * 1000);//Duerme el hilo por 5 segundos
            }
        } catch (InterruptedException ex) {
            System.err.println("Error en runAerosilla.");
        }
    }

}
