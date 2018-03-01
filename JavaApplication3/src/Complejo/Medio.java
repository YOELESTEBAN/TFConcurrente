/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import java.util.ArrayList;
import javax.swing.JTextArea;

/**
 *
 * @author Yoni
 */
public class Medio extends Thread {

    private CaidaRapida complejo; //Clase principal
    private ArrayList molinetes; //Arreglo que contiene los molinetes de este medio
    private int tipo; //Tipo de medio (entre 1 y 4)
    private int molinetesDisp; //Contador de molinetes disponibles
    private boolean pasaAerosilla; //Cuando pasaAerosilla=true entonces pueden subir 
    private boolean estaAbierto; //Boolean que indica si esta abierto el medio o no
    private JTextArea salidaT; //Salida de texto en Interfaz

    public Medio(int tipo, CaidaRapida comp, JTextArea textoM) {
        this.complejo = comp;
        this.salidaT = textoM;
        this.estaAbierto = true;
        molinetesDisp = tipo; //Cantidad de molinetes que tiene el medio
        pasaAerosilla = false; //Cada ciertos segundos pasa una aerosilla y se vuelve true
        if (tipo > 0 && tipo < 5) { //Verifica que la cantidad de molinetes del medio sea correcta
            molinetes = new ArrayList(tipo); //Se declara el vector que contiene los molinetes de este medio
            for (int i = 0; i < tipo; i++) {
                insertarMolinete(i); //Crea los molinetes y los va insertando en el array
            }
            this.tipo = tipo; //Tipo de medio (de 1 a 4) 
        }
    }

    public void cerrar() { //Se usa para cerrar el medio
        this.estaAbierto = false;
    }

    public int getTipo() {
        return tipo;
    }

    private void insertarMolinete(int id) {
        Molinete nuevo = new Molinete(id); //Crea el molinete con contador 0 y el id que ingresa al metodo
        this.molinetes.add(nuevo); //Inserta el molinete en el vector de este medio
    }

    public synchronized void entraAerosilla() {
        System.out.println("**** Entra AEROSILLA DE MEDIO " + this.tipo + "****");
        salidaT.append("***Entra aerosilla***\n");
        pasaAerosilla = true;
        notifyAll();//Notifica el cambio de pasaAerosilla 
    }

    public synchronized void saleAerosilla() {
        System.out.println("**** Sale AEROSILLA DE MEDIO " + this.tipo + "****");
        salidaT.append("***Sale aerosilla***\n");
        pasaAerosilla = false;
        notifyAll();//Notifica el cambio de pasaAerosilla
    }

    public synchronized void usarMolinete(int i) {
        Molinete aux = (Molinete) molinetes.get(i - 1);
        aux.usarMolinete();
        molinetesDisp--;
    }

    public synchronized void usarMedio() {
        try {
            if (molinetesDisp == 0) {//Si no hay molinetes disponibles
                System.out.println(Thread.currentThread().toString() + " espera porque no hay molinetes disponibles en el medio " + this.tipo);
                salidaT.append(Thread.currentThread().toString() + " espera porque no hay molinetes disponibles.\n");
                while (molinetesDisp == 0) {//Comprueba si hay lugar para pasar a tomar la aerosilla
                    wait();
                }
            }
            //Hay molinetes disponibles, osea un lugar en la aerosilla que va a pasar
            usarMolinete(molinetesDisp);
            System.out.println(Thread.currentThread().toString() + " pasó el molinete del medio " + this.tipo);
            salidaT.append(Thread.currentThread().toString() + " pasó el molinete.\n");
            if (!pasaAerosilla) {//Si la Aerosilla no esta pasando
                System.out.println(Thread.currentThread().toString() + " espera a que pase la aerosilla en el medio " + this.tipo);
                salidaT.append(Thread.currentThread().toString() + " espera la aerosilla.\n");
                while (!pasaAerosilla) {
                    wait();
                }
            }
            molinetesDisp++;
            System.out.println(Thread.currentThread().toString() + " ya subió al medio " + this.tipo);
            salidaT.append(Thread.currentThread().toString() + " esta subiendo y baja esquiando.\n");
        } catch (InterruptedException ex) {
            System.out.println("ERROR SEVERO en usarMedio tipo" + this.tipo + " " + Thread.currentThread().toString());
        }

    }

    public int sacarCuenta() { //Saca el conteo final de la cantidad que esquiadores que usaron este medio
        int total = 0; //Se usa para llevar la cuenta
        Molinete auxM; //Se usa temporalmente para recuperar cada molinete del Arreglo
        for (int i = 0; i < molinetes.size(); i++) {
            auxM = (Molinete) molinetes.get(i);
            total = total + auxM.getContador();
        }
        return total;
    }

    @Override
    public void run() {
        try {
            while (this.estaAbierto) {//Mientras esta abierto esta vivo el hilo
                this.entraAerosilla();
                Thread.sleep(50);//Duerme el hilo por 50 milisegundos (tiempo que tardan en acomodarse los esquiadores en aerosilla)
                this.saleAerosilla();
                switch (tipo) {//Duerme el hilo una cantidad de tiempo, dependiendo el tipo (hasta que pase otra aerosilla)
                    case 4:
                        Thread.sleep(5000);//El medio 4 tiene menos aerosillas porque cada una tiene mas lugares
                        break;
                    case 3:
                        Thread.sleep(4000);
                        break;
                    case 2:
                        Thread.sleep(2500);
                        break;
                    case 1:
                        Thread.sleep(1500);
                        break;
                }
            }
        } catch (InterruptedException ex) {
            System.err.println("Error en runAerosilla.");
        }
    }

}
