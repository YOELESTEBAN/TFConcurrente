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
        this.cantPersonas = 98;
        this.mostrador1 = true; //al ocuparse pasan a false
        this.mostrador2 = true;
        this.mostradorPostre = true;
        this.caja = true;
        sCaja = new Semaphore(1);
        sSalida = new Semaphore(1);
    }

    public boolean comprarMenu(Esquiador esq) {
        int aux = 0;

        try {
            System.out.println(esq.getNombre() + " llega a la caja y pide el semáforo.");
            sCaja.acquire();
            System.out.println(esq.getNombre() + " obtiene el semáforo.");
            if (cantPersonas < 100) {
                System.out.println(esq.getNombre() + " hay menos de 100 personas.");
                cantPersonas++;
                System.out.println(esq.getNombre() + " suma persona a la confitería y quedan " + cantPersonas);
                esq.esperar(3);//Simula el tiempo que demora en comprar el menu
                esq.setMenu((int) (Math.random() * 2) + 1);
                System.out.println(esq.getNombre() + " compra el menu " + esq.getMenu() + ".");
                System.out.println(esq.getNombre() + " libera la caja (semaforo) y sale.");
                sCaja.release();
                aux = 1;
            } else {
                System.out.println(esq.getNombre() + " quiso entrar a la confitería pero esta llena.");
                aux = 0;
            }

        } catch (InterruptedException ex) {
            System.err.println("Error en Confiteria comprarMenu.");
        }
        if (aux == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void saleConfiteria(Esquiador esq) {

        try {
            System.out.println(esq.getNombre() + " se quiere ir y pide el semaforo.");
            sSalida.acquire();
            cantPersonas--;
            System.out.println(esq.getNombre() + " resta persona a la confitería y quedan " + cantPersonas);
//            notifyAll();
            System.out.println(esq.getNombre() + " libera el semáforo.");
            sSalida.release();
        } catch (InterruptedException ex) {
            System.err.println("Error en Confiteria saleConfiteria.");
        }
    }

}
