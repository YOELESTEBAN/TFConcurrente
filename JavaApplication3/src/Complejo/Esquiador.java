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
public class Esquiador extends Thread {

    private String nombre; //Nombre del Esquiador
    private CaidaRapida complejo; //Monitor del complejo
    private int tipoDeMedio;
    private int menu;

    public Esquiador(String nombre, CaidaRapida complejo, int tipoDeMedio) {
        this.nombre = nombre;
        this.complejo = complejo;
        this.tipoDeMedio = tipoDeMedio;
        this.menu=0;
    }

    public void setTipoDeMedio(int tipoDeMedio) {
        this.tipoDeMedio = tipoDeMedio;
    }
    
    public void setMenu(int tipoMenu){
        this.menu=tipoMenu;
    }

    public int getTipoDeMedio() {
        return tipoDeMedio;
    }
    
    public int getMenu() {
        return menu;
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
                complejo.usarMedio(tipoDeMedio);

            }
            this.esperar(50); //Simula un tiempo entre salir del puente y volver a entrar
            this.cambiaMedioRandom();
        } catch (InterruptedException ex) {
            System.out.println("ERROR SEVERO en Auto " + nombre);
        }

    }

}
