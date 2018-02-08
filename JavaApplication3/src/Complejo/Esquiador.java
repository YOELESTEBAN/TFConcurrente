/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import java.util.Random;
import javax.swing.JTextArea;

/**
 *
 * @author Yoni
 */
public class Esquiador extends Thread {

    private String nombre; //Nombre del Esquiador
    private Confiteria conf; //Confitería del complejo
    private GestionaInst monInst; //Monitor de instructores
    private CaidaRapida monMedio; //Monitor de medios de elevación
    private int tipoMedio; //Indica tipo de medio (1,2,3 o 4)
    private int menu; //Indica menu que se va a servir en la confitería
    private boolean postre; //Indica si compra postre en la confitería
    private boolean claseSky; //Indica si toma clase de Sky (en false toma clase de Snow)
    private Random r; //Random que se usa para variar la clase que toma (sky o snow)
    private JTextArea salidaT; //Salida de texto en Interfaz

    public Esquiador(String nombre, int tipoDeMedio, Confiteria conf, GestionaInst monInst, CaidaRapida m, JTextArea salida) {
        this.nombre = nombre;
        this.tipoMedio = tipoDeMedio;
        this.conf = conf;
        this.menu = 0; //Este valor no es relevante porque luego se llama al metodo cambiaRandom()
        this.postre = false; //Este valor no es relevante porque luego se llama al metodo cambiaRandom()
        this.monInst = monInst;
        this.claseSky = true;//Este valor no es relevante porque luego se llama al metodo cambiaRandom()
        this.monMedio = m;
        this.salidaT = salida;
    }

    public void setTipoDeMedio(int tipoDeMedio) {
        this.tipoMedio = tipoDeMedio;
    }

    public void setMenu(int tipoMenu) {
        this.menu = tipoMenu;
    }

    public void setPostre(boolean i) {
        this.postre = i;
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

    public boolean getPostre() {
        return this.postre;
    }

    public String getNombre() {
        return nombre;
    }

    private void cambiaRandom() {
        r = new Random();
        this.claseSky = r.nextBoolean();//Genera un random booleano para ir cambiando la clase que tome (sky True, snow False)
        this.postre = r.nextBoolean();//Genera un random booleano para ir cambiando si compra o no postre.
        this.tipoMedio = (int) (Math.random() * 4) + 1; //Genera un random entre 1 y 4 para ir cambiando el medio
        this.menu = (int) (Math.random() * 2) + 1; //Genera un random entre 1 y 2 para ir cambiando el menu

    }

    private void cambiaMedioRandom() {
        this.tipoMedio = (int) (Math.random() * 4) + 1; //Genera un random entre 1 y 4 para ir cambiando el medio
    }

    private void cambiaClaseRandom() {
        r = new Random();
        this.claseSky = r.nextBoolean();//Genera un random booleano para ir cambiando la clase que tome (sky True, snow False)
    }

    private void cambiaMenuRandom() {
        this.menu = (int) (Math.random() * 2) + 1; //Genera un random entre 1 y 2 para ir cambiando el menu
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
            salidaT.append(this.getNombre() + " entra al complejo.\n");
            while (true) {
                this.cambiaRandom();
                System.out.println(this.getNombre() + " empieza.");

                System.out.println(this.getNombre() + " va a la CONFITERÍA.");
                if (conf.comprarMenu(this)) {
                    conf.retiraMenu(this);
                    if (this.getPostre()) {
                        conf.retiraPostre(this);
                    }
                    System.out.println(this.getNombre() + " come.");
                    this.esperar(5);
                    conf.saleConfiteria(this);
                }
                System.out.println(this.getNombre() + " se va de confitería.");
                this.esperar(5);

                System.out.println(this.getNombre() + " va a TOMAR UNA CLASE.");
                monInst.iniciarClase(this.getTipoDeClase());
                this.esperar(5);
                monInst.terminarClase(this.getTipoDeClase());
                this.esperar(5);
                /*
                
                System.out.println(this.getNombre()+" va a SUBIR A UN MEDIO.");
                monMedio.entrarMedio(tipoMedio);
                this.esperar(5);*/
            }
        } catch (InterruptedException ex) {
            System.err.println("Error en runEsquiador.");
        }

    }

}
