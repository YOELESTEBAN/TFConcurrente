/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import javax.swing.JTextArea;

/**
 *
 * @author Latitude
 */
public class Instructor extends Thread { 

    private CaidaRapida complejo; //Clase principal
    private GestionaClase monitor;// Monitor que gestiona las clases
    private boolean daClaseSky;//Variable Booleana que define si da clase de Sky
    private boolean daClaseSnow;//Variable Booleana que define si da clase de Snow
    private JTextArea salidaT;//Salida de texto en Interfaz

    public Instructor(String nombre, CaidaRapida comp, GestionaClase mon, JTextArea salidaT) {
        complejo = comp;
        daClaseSky = false;
        daClaseSnow = false;
        monitor = mon;
        this.setName(nombre);
        this.salidaT = salidaT;
    }

    public void setDaClaseSky(boolean b) {//Setea si el instructor da clase de Sky
        this.daClaseSky = b;
    }

    public void setDaClaseSnow(boolean b) {//Setea si el instructor da clase de Snow
        this.daClaseSnow = b;
    }

    @Override
    public void run() {
        try {
            salidaT.append(this.getName() + " entra a la cabina.\n");
            while (complejo.estaAbierto()) {//Mientras el complejo esta abierto, el instructor esta corriendo
                synchronized (this) {
                    salidaT.append(this.getName() + " espera.\n");
                    System.out.println(Thread.currentThread().getName() + " duerme");
                    this.wait();//Duerme mientras espera que se forme un grupo
                }
                if (this.daClaseSky) {//Si da clase de Sky
                    System.out.println(Thread.currentThread().getName() + " se despierta para dar la clase.");
                    monitor.darClaseSky(this);//Da la clase, despierta a los esquiadores
                }
                if (this.daClaseSnow) {//Si da clase de Snow
                    System.out.println(Thread.currentThread().getName() + " se despierta para dar la clase.");
                    monitor.darClaseSnow(this);//Da la clase, despierta a los esquiadores
                }
            }
            salidaT.append(this.getName() + " se va.\n");
        } catch (InterruptedException e) {
            System.out.println("Error en el run de Instructor" + Thread.currentThread().getName());
        }
    }
}
