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
    private Confiteria conf;
    private int tipoDeMedio;
    private int menu;
    private boolean postre;

    public Esquiador(String nombre, int tipoDeMedio, Confiteria conf) {
        this.nombre = nombre;
        this.tipoDeMedio = tipoDeMedio;
        this.conf = conf;
        this.menu = 0;
        this.postre=false;
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
                System.out.println(this.getNombre()+" empieza.");
                /*complejo.usarMedio(tipoDeMedio);
            this.esperar(5); //Simula un tiempo entre salir del puente y volver a entrar
            this.cambiaMedioRandom();*/
                
                if (conf.comprarMenu(this)) {
                    conf.retiraMenu(this);
                    if (this.getPostre()){
                        conf.retiraPostre(this);
                    }
                    System.out.println(this.getNombre()+ " come.");
                    this.esperar(5);
                    conf.saleConfiteria(this);
                }
                System.out.println(this.getNombre()+ " se va de confitería, espera un toque y vuelve a comenzar.");
                this.esperar(5);

            }
        } catch (InterruptedException ex) {
            System.err.println("Error en runEsquiador.");
        }

    }

    public void setPostre(boolean i) {
        this.postre=i;
    }
    
    public boolean getPostre (){
        return this.postre;
    }

}
