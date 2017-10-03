/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import java.util.Random;
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
    private Semaphore sCaja;
    private Semaphore sCantPersonas;
    private Semaphore sMostrador1;
    private Semaphore sMostrador2;
    private Semaphore sPostre;

    public Confiteria() {
        this.cantPersonas = 0;
        this.mostrador1 = true; //al ocuparse pasan a false
        this.mostrador2 = true;
        this.mostradorPostre = true;
        sCaja = new Semaphore(1);
        sCantPersonas = new Semaphore(1);
        sMostrador1 = new Semaphore(1);
        sMostrador2 = new Semaphore(1);
        sPostre = new Semaphore(1);
    }

    public boolean comprarMenu(Esquiador esq) {
        boolean aux = false;
        Random r= new Random ();

        try {
            System.out.println(esq.getNombre() + " llega a la caja y pide el semáforo.");
            sCaja.acquire();
            System.out.println(esq.getNombre() + " obtiene el semáforo.");
            if (cantPersonas < 100) {
                System.out.println(esq.getNombre() + " hay menos de 100 personas.");
                sCantPersonas.acquire();
                cantPersonas++;
                System.out.println(esq.getNombre() + " suma persona a la confitería y hay " + cantPersonas);
                sCantPersonas.release();
                esq.esperar(3);//Simula el tiempo que demora en comprar el menu
                esq.setMenu(r.nextInt(2) + 1);
                esq.setPostre(r.nextBoolean());
                System.out.println(esq.getNombre() + " compra el menu " + esq.getMenu() + " y postre en " +esq.getPostre()+".");
                System.out.println(esq.getNombre() + " libera la caja (semaforo) y sale.");
                aux = true;
            } else {
                System.out.println(esq.getNombre() + " quiso entrar a la confitería pero esta llena.");
                aux = false;
            }
            sCaja.release();

        } catch (InterruptedException ex) {
            System.err.println("Error en Confiteria comprarMenu.");
        }

        return aux;

    }

    public void retiraMenu(Esquiador esq) {
        try {
            if (esq.getMenu() == 1) {
                System.out.println(esq.getNombre() + " va a retirar el menu 1 y solicita el semaforo.");
                sMostrador1.acquire();
                System.out.println(esq.getNombre() + " obtiene el semaforo de mostrador 1 y retira el menu.");
                esq.esperar(1);//Simula el tiempo que demora en retirar el menu
                System.out.println(esq.getNombre() + " libera el semaforo de mostrador 1 y se va.");
                sMostrador1.release();
            } else {
                System.out.println(esq.getNombre() + " va a retirar el menu 2 y solicita el semaforo.");
                sMostrador2.acquire();
                System.out.println(esq.getNombre() + " obtiene el semaforo de mostrador 2 y retira el menu.");
                esq.esperar(1);//Simula el tiempo que demora en retirar el menu
                System.out.println(esq.getNombre() + " libera el semaforo de mostrador 2 y se va.");
                sMostrador2.release();
            }
        } catch (InterruptedException ex) {
            System.err.println("Error en Confiteria retiraMenu.");
        }
    }

    public void retiraPostre(Esquiador esq) {
        try {
            System.out.println(esq.getNombre() + " va a retirar el postre y solicita el semaforo.");
            sPostre.acquire();
            esq.esperar(1);//Simula el tiempo que demora en retirar el postre
            System.out.println(esq.getNombre() + " libera el semaforo de Postre y se va.");
            sPostre.release();
        } catch (InterruptedException ex) {
            System.err.println("Error en Confiteria retiraPostre.");
        }
    }

    public void saleConfiteria(Esquiador esq) {

        try {
            System.out.println(esq.getNombre() + " se quiere ir y pide el semaforo.");
            sCantPersonas.acquire();
            cantPersonas--;
            System.out.println(esq.getNombre() + " resta persona a la confitería y quedan " + cantPersonas);
            System.out.println(esq.getNombre() + " libera el semáforo.");
            sCantPersonas.release();
        } catch (InterruptedException ex) {
            System.err.println("Error en Confiteria saleConfiteria.");
        }
    }

}
