/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import java.util.Random;


/**
 *
 * @author Yoni
 */
public class Esquiador extends Thread {

    private String nombre; //Nombre del Esquiador
    private Confiteria conf;
    private GestionaInst monInst; //Monitor de instructores
    private CaidaRapida monMedio; //Monitor de medios de elevación
    private int tipoMedio;
    private int menu;
    private boolean postre;
    private boolean claseSky;
    private Random r;

    public Esquiador(String nombre, int tipoDeMedio, Confiteria conf, GestionaInst monInst, CaidaRapida m) {
        this.nombre = nombre;
        this.tipoMedio = tipoDeMedio;
        this.conf = conf;
        this.menu = 0;
        this.postre=false;
        this.monInst=monInst;
        this.claseSky=true;
        this.monMedio=m;
    }

    public void setTipoDeMedio(int tipoDeMedio) {
        this.tipoMedio = tipoDeMedio;
    }

    public void setMenu(int tipoMenu) {
        this.menu = tipoMenu;
    }

    public int getTipoDeMedio() {
        return tipoMedio;
    }
    
    public boolean getTipoDeClase() {
        return claseSky;
    }

    public int getMenu() {
        return menu;
    }
     public void setPostre(boolean i) {
        this.postre=i;
    }
    
    public boolean getPostre (){
        return this.postre;
    }

    public String getNombre() {
        return nombre;
    }

    private void cambiaMedioRandom() {
        this.tipoMedio = (int) (Math.random() * 4) + 1; //Genera un random entre 1 y 4 para ir cambiando el medio
    }
    
    private void cambiaClaseRandom() {
        r = new Random();
        this.claseSky = r.nextBoolean();//Genera un random booleano para ir cambiando la clase que tome (sky True, snow False)
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
                this.esperar(5);
                this.cambiaMedioRandom();*/
                
               /* if (conf.comprarMenu(this)) {
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
                
                this.cambiaClaseRandom(); //Cambia la clase para que sea random desde el principio
                monInst.iniciarClase(this.getTipoDeClase());
                this.esperar(5);
                monInst.terminarClase(this.getTipoDeClase());
                this.esperar(5);
                */
                this.cambiaMedioRandom();
                monMedio.entrarMedio(tipoMedio);
                //monMedio.entrarMedio(1);
                this.esperar(5);
            }
        } catch (InterruptedException ex) {
            System.err.println("Error en runEsquiador.");
        }

    }

}
