/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yoni
 */
public class Confiteria {

    private int cantPersonas;
    private boolean mostrador1;
    private boolean mostrador2;
    private boolean mostradorPostre;
    private boolean caja;
    private Semaphore sCaja;
    private Semaphore sSalida;

    public Confiteria() {
        this.cantPersonas = 0;
        this.mostrador1 = true; //al ocuparse pasan a false
        this.mostrador2 = true;
        this.mostradorPostre = true;
        this.caja = true;
        sCaja = new Semaphore(1);
    }

    public void comprarMenu(Esquiador esq) {

        try {
            sCaja.acquire();
            while (caja) {
                if (cantPersonas <= 100) {
                    caja = false; //Ocupa la caja
                    cantPersonas++;
                    esq.esperar(3);//Simula el tiempo que demora en comprar el menu
                    esq.setMenu((int) (Math.random() * 2) + 1);
                } else {
                    System.out.println(esq.getId() + " quiso entrar a la confitería pero esta llena, espera.");
                    esq.wait();//Al salir alguien hace notify
                }
            }
            caja = true;
            sCaja.release();

        } catch (InterruptedException ex) {
            System.err.println("Error en Confiteria comprarMenu.");
        }
    }

    public void saleConfiteria(Esquiador esq) {

        try {
            sSalida.acquire();
            cantPersonas--;
            notifyAll();
            sSalida.release();
        } catch (InterruptedException ex) {
            System.err.println("Error en Confiteria saleConfiteria.");
        }
    }

}
