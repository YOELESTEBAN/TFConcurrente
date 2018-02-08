/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import java.util.concurrent.Semaphore;
import javax.swing.JTextArea;

/**
 *
 * @author Yoni
 */
public class Confiteria {

    private int cantPersonas;//Contador de personas en confitería
    private Semaphore sCaja;
    private Semaphore sCantPersonas;
    private Semaphore sMostrador1;
    private Semaphore sMostrador2;
    private Semaphore sPostre;
    private JTextArea salidaT;//Salida de texto en Interfaz

    public Confiteria(JTextArea salidaT) {
        this.cantPersonas = 0;
        sCaja = new Semaphore(1);
        sCantPersonas = new Semaphore(1);
        sMostrador1 = new Semaphore(1);
        sMostrador2 = new Semaphore(1);
        sPostre = new Semaphore(1);
        this.salidaT = salidaT;
    }

    public boolean comprarMenu(Esquiador esq) {
        boolean aux = false;

        try {
            System.out.println(esq.getNombre() + " llega a la caja y pide el semáforo.");
            sCaja.acquire();
            System.out.println(esq.getNombre() + " obtiene el semáforo.");
            if (cantPersonas < 100) {
                System.out.println(esq.getNombre() + " hay menos de 100 personas.");
                salidaT.append(esq.getNombre() + " llega a la caja y pide un menu.\n");
                sCantPersonas.acquire();
                cantPersonas++;
                System.out.println(esq.getNombre() + " suma persona a la confitería y hay " + cantPersonas);
                sCantPersonas.release();
                esq.esperar(3);//Simula el tiempo que demora en comprar el menu
                System.out.println(esq.getNombre() + " compra el menu " + esq.getMenu() + " y postre en " + esq.getPostre() + ".");
                salidaT.append(esq.getNombre() + " compra el menu " + esq.getMenu() + " y postre en " + esq.getPostre() + ".\n");
                System.out.println(esq.getNombre() + " libera la caja (semaforo) y sale.");
                aux = true;
            } else {
                salidaT.append(esq.getNombre() + " quiso entrar pero estaba llena.\n");
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
                salidaT.append(esq.getNombre() + " quiere retirar el menu 1.\n");
                sMostrador1.acquire();
                System.out.println(esq.getNombre() + " obtiene el semaforo de mostrador 1 y retira el menu.");
                salidaT.append(esq.getNombre() + " retira el menu 1.\n");
                esq.esperar(1);//Simula el tiempo que demora en retirar el menu
                System.out.println(esq.getNombre() + " libera el semaforo de mostrador 1 y se va.");
                sMostrador1.release();
            } else {
                System.out.println(esq.getNombre() + " va a retirar el menu 2 y solicita el semaforo.");
                salidaT.append(esq.getNombre() + " quiere retirar el menu 2.\n");
                sMostrador2.acquire();
                System.out.println(esq.getNombre() + " obtiene el semaforo de mostrador 2 y retira el menu.");
                salidaT.append(esq.getNombre() + " retira el menu 2.\n");
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
            salidaT.append(esq.getNombre() + " quiere retirar el postre.\n");
            sPostre.acquire();
            salidaT.append(esq.getNombre() + " retira el postre.\n");
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
            salidaT.append(esq.getNombre() + " se retira y quedan " + cantPersonas + " personas en la Confitería.\n");
            System.out.println(esq.getNombre() + " resta persona a la confitería y quedan " + cantPersonas);
            System.out.println(esq.getNombre() + " libera el semáforo.");
            sCantPersonas.release();
        } catch (InterruptedException ex) {
            System.err.println("Error en Confiteria saleConfiteria.");
        }
    }

}
