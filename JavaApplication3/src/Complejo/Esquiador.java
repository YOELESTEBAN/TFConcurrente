/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yoni
 */
public class Esquiador extends Thread {

    private String nombre; //Nombre del Esquiador
    private CaidaRapida complejo; //Monitor del complejo
    private Confiteria conf;
    private int tipoDeMedio;
    private int menu;

    public Esquiador(String nombre, CaidaRapida complejo, int tipoDeMedio, Confiteria conf) {
        this.nombre = nombre;
        this.complejo = complejo;
        this.tipoDeMedio = tipoDeMedio;
        this.conf = conf;
        this.menu = 0;
    }

    public void setTipoDeMedio(int tipoDeMedio) {
        this.tipoDeMedio = tipoDeMedio;
    }

    public void setMenu(int tipoMenu) {
        this.menu = tipoMenu;
    }

    public int getTipoDeMedio() {
        return tipoDeMedio;
    }

    public int getMenu() {
        return menu;
    }

    public String getNombre() {
        return nombre;
    }

    private void cambiaMedioRandom() {
        this.tipoDeMedio = (int) (Math.random() * 4) + 1; //Genera un random entre 1 y 4 para ir cambiando el medio
    }

    @Override
    public String toString() {
        return nombre;
    }

    public void esperar(int tiempo) throws InterruptedException { //Duerme el hilo en segundos
        try {
            Thread.sleep(tiempo * 1000);
        } catch (Exception ex) {
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                /*complejo.usarMedio(tipoDeMedio);
            this.esperar(5); //Simula un tiempo entre salir del puente y volver a entrar
            this.cambiaMedioRandom();*/
                System.out.println(this.getNombre()+" empieza.");
                if (conf.comprarMenu(this)) {
                    this.esperar(5);
                    conf.saleConfiteria(this);
                }
                System.out.println(this.getNombre()+ " se va de confitería, espera un toque y vuelve a comenzar.");
                this.esperar(5);

            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Esquiador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
