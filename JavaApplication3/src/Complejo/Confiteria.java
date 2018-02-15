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
    private Semaphore sCaja;//Semáforo de la caja
    private Semaphore sCantPersonas;//Semáforo que proteje la variable sCantPersonas
    private Semaphore sMostrador1;//Semáforo del menu mostrador1
    private Semaphore sMostrador2;//Semáforo del menu mostrador1
    private Semaphore sPostre;//Semáforo del mostrador de postre
    private JTextArea salidaT;//Salida de texto en Interfaz

    public Confiteria(JTextArea salidaT) {
        this.cantPersonas = 0;//Se setea en 0
        sCaja = new Semaphore(1);//Semáforo binario
        sCantPersonas = new Semaphore(1);//Semáforo binario
        sMostrador1 = new Semaphore(1);//Semáforo binario
        sMostrador2 = new Semaphore(1);//Semáforo binario
        sPostre = new Semaphore(1);//Semáforo binario
        this.salidaT = salidaT;//Salida de texto en Interfaz
    }

    public boolean comprarMenu(Esquiador esq) { //En este método el esquiador entra o no a la confitería y compra el menu y postre (si corresponde)
        boolean entro = false; //Variable que se usa para devolver si entro o no a la confitería

        try {
            System.out.println(esq.getNombre() + " llega a la caja y pide el semáforo.");
            sCaja.acquire();//Pide el semáforo de la caja
            System.out.println(esq.getNombre() + " obtiene el semáforo.");
            if (cantPersonas < 100) {
                System.out.println(esq.getNombre() + " hay menos de 100 personas.");
                salidaT.append(esq.getNombre() + " llega a la caja y pide un menu.\n");
                sCantPersonas.acquire();//Pide el semáforo que proteje la variable cantPersonas para sumarle 1
                cantPersonas++;//Incrementa el valor de la variable porque entro un esquiador
                System.out.println(esq.getNombre() + " suma persona a la confitería y hay " + cantPersonas);
                sCantPersonas.release();//Libera el semáforo de la variable sCantPersonas
                esq.esperar(1);//Simula el tiempo que demora en comprar el menu
                System.out.println(esq.getNombre() + " compra el menu " + esq.getMenu() + " y postre en " + esq.getPostre() + ".");
                if (esq.getPostre()) {
                    salidaT.append(esq.getNombre() + " compra el menu " + esq.getMenu() + " y postre.\n");
                } else {
                    salidaT.append(esq.getNombre() + " solo compra el menu " + esq.getMenu() + ".\n");
                }
                System.out.println(esq.getNombre() + " libera la caja (semaforo) y sale.");
                entro = true; //Setea el boolean en true porque pudo entrar a la confitería
            } else {
                salidaT.append(esq.getNombre() + " quiso entrar pero estaba llena.\n");
                System.out.println(esq.getNombre() + " quiso entrar a la confitería pero esta llena.");
                entro = false;//Setea el boolean en falso porque no pudo entrar a la confitería
            }
            sCaja.release(); //Libera el semáforo de la caja

        } catch (InterruptedException ex) {
            System.err.println("Error en Confiteria comprarMenu.");
        }

        return entro; //Devuelve la variable booleana

    }

    public void retiraMenu(Esquiador esq) { //En este método el esquiador retira el menu que compró
        try {
            if (esq.getMenu() == 1) { //Si el menú que compro el esquiador es el 1 entra
                System.out.println(esq.getNombre() + " va a retirar el menu 1 y solicita el semaforo.");
                salidaT.append(esq.getNombre() + " quiere retirar el menu 1.\n");
                sMostrador1.acquire();//Pide el semáforo del mostrador1
                System.out.println(esq.getNombre() + " obtiene el semaforo de mostrador 1 y retira el menu.");
                salidaT.append(esq.getNombre() + " retira el menu 1.\n");
                esq.esperar(1);//Simula el tiempo que demora en retirar el menu
                System.out.println(esq.getNombre() + " libera el semaforo de mostrador 1 y se va.");
                sMostrador1.release();//Libera el semáforo del motrador1
            } else {//Si el menú que compro el esquiador no es el 1
                System.out.println(esq.getNombre() + " va a retirar el menu 2 y solicita el semaforo.");
                salidaT.append(esq.getNombre() + " quiere retirar el menu 2.\n");
                sMostrador2.acquire();//Pide el semáforo del mostrador2
                System.out.println(esq.getNombre() + " obtiene el semaforo de mostrador 2 y retira el menu.");
                salidaT.append(esq.getNombre() + " retira el menu 2.\n");
                esq.esperar(1);//Simula el tiempo que demora en retirar el menu
                System.out.println(esq.getNombre() + " libera el semaforo de mostrador 2 y se va.");
                sMostrador2.release();//Libera el semáforo del motrador2
            }
        } catch (InterruptedException ex) {
            System.err.println("Error en Confiteria retiraMenu.");
        }
    }

    public void retiraPostre(Esquiador esq) {//En este método el esquiador retira el postre que compró
        try {
            System.out.println(esq.getNombre() + " va a retirar el postre y solicita el semaforo.");
            salidaT.append(esq.getNombre() + " quiere retirar el postre.\n");
            sPostre.acquire();//Pide el semáforo del mostrador de postre
            salidaT.append(esq.getNombre() + " retira el postre.\n");
            esq.esperar(1);//Simula el tiempo que demora en retirar el postre
            System.out.println(esq.getNombre() + " libera el semaforo de Postre y se va.");
            sPostre.release();//Libera el semáforo del mostrador de postre
        } catch (InterruptedException ex) {
            System.err.println("Error en Confiteria retiraPostre.");
        }
    }

    public void come(Esquiador esq) {
        try {
            salidaT.append(esq.getNombre() + " come.\n");
            esq.esperar(20);//Duerme el hilo simulando el tiempo que demora en comer
        } catch (InterruptedException ex) {
            System.err.println("Error en Confiteria come.");
        }
    }

    public void saleConfiteria(Esquiador esq) {//En este método el esquiador sale de la confitería
        try {
            System.out.println(esq.getNombre() + " se quiere ir y pide el semaforo.");
            sCantPersonas.acquire();//Pide el semáforo que proteje la variable cantPersonas para restar 1
            cantPersonas--;//Decrementa el valor de la variable porque sale un esquiador
            salidaT.append(esq.getNombre() + " se retira y quedan " + cantPersonas + " personas en la Confitería.\n");
            System.out.println(esq.getNombre() + " resta persona a la confitería y quedan " + cantPersonas);
            System.out.println(esq.getNombre() + " libera el semáforo.");
            sCantPersonas.release();//Libera el semáforo de la variable sCantPersonas
        } catch (InterruptedException ex) {
            System.err.println("Error en Confiteria saleConfiteria.");
        }
    }

}
