/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yoni
 */
public class Medio {

    private ArrayList molinetes; //Arreglo que contiene los molinetes de este medio
    private int tipo; //Tipo de medio (entre 1 y 4)
    private int molinetesDisp; //Contador de molinetes disponibles
    private boolean pasaAerosilla; //Cuando pasaAerosilla=true entonces pueden subir 
    

    public Medio(int tipo) {
        molinetesDisp = tipo; //Cantidad de molinetes que tiene el medio
        pasaAerosilla = false; //Cada 5 segundos pasa una aerosilla y se vuelve true
        if (tipo > 0 && tipo < 5) { //Verifica que la cantidad de molinetes del medio sea correcta
            molinetes = new ArrayList(tipo); //Se declara el vector que contiene los molinetes de este medio
            for (int i = 0; i < tipo; i++) {
                insertarMolinete(i); //Crea los molinetes y los va insertando en el array
            }
            this.tipo = tipo; //Tipo de medio (de 1 a 4) OJOOOOOOOOOOO (verificar que no se haya creado ya, si o si 4 medios tiene q haber)
        }

    }

    public int getTipo() {
        return tipo;
    }

    private void insertarMolinete(int id) {
        Molinete nuevo = new Molinete(id); //Crea el molinete con contador 0 y el id que ingresa al metodo
        this.molinetes.add(nuevo); //Inserta el molinete en el vector de este medio
    }
    
    public synchronized void entraAerosilla(){
        System.out.println("*********************** Entra AEROSILLA DE MEDIO "+ this.tipo);
        pasaAerosilla=true;
        notifyAll();
    }
    
    public synchronized void saleAerosilla(){
        System.out.println("*********************** SALE AEROSILLA DE MEDIO "+ this.tipo);
        pasaAerosilla=false;
        notifyAll();//Notifica el cambio de pasaAerosilla y molinetesDisp
    }


    public synchronized void usarMedio() {
        try {
            while (molinetesDisp == 0) {//Comprueba si hay lugar para pasar a tomar la aerosilla
                System.out.println(Thread.currentThread().toString() + " espera porque no hay molinetes disponibles en el medio " + this.tipo);
                this.wait();
            }//Hay molinetes disponibles, osea un lugar en la aerosilla que va a pasar
            molinetesDisp--;
            System.out.println(Thread.currentThread().toString() + " pasó el molinete del medio " + this.tipo);
            //notifyAll();
            while (!pasaAerosilla){
                System.out.println(Thread.currentThread().toString() + " espera a que pase la aerosilla en el medio " + this.tipo);
                this.wait();
            }
            molinetesDisp++;
            System.out.println(Thread.currentThread().toString() + " ya subió al medio " + this.tipo);

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

}
