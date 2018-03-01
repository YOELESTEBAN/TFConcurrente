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
public class Esquiador extends Thread { //Hilo esquiador

    private CaidaRapida complejo;
    private String nombre; //Nombre del Esquiador
    private Confiteria conf; //Confitería del complejo
    private GestionaClase monInst; //Monitor de instructores
    private GestionaMedio monMedio; //Monitor de medios de elevación
    private int tipoMedio; //Indica tipo de medio (1,2,3 o 4)
    private int menu; //Indica menu que se va a servir en la confitería
    private boolean postre; //Indica si compra postre en la confitería
    private boolean claseSky; //Indica si toma clase de Sky (en false toma clase de Snow)
    private int turnoSky; //Indica el turno que se le asigna para la clase de Sky
    private boolean enClase; //Indica si esta tomando una clase
    private Random r; //Random que se usa para variar la clase que toma (sky o snow)
    private JTextArea salidaT; //Salida de texto en Interfaz

    public Esquiador(String nombre, Confiteria conf, GestionaClase monInst, GestionaMedio m, JTextArea salida, CaidaRapida comp) {
        this.complejo = comp;
        this.nombre = nombre;
        this.tipoMedio = 1;//Este valor no es relevante porque luego se llama al metodo cambiaRandom()
        this.conf = conf;
        this.menu = 0; //Este valor no es relevante porque luego se llama al metodo cambiaRandom()
        this.postre = false; //Este valor no es relevante porque luego se llama al metodo cambiaRandom()
        this.monInst = monInst;
        this.claseSky = true;//Este valor no es relevante porque luego se llama al metodo cambiaRandom()
        this.monMedio = m;
        this.turnoSky = 0;
        this.enClase = false;
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

    public void setEnClase(Boolean b) {
        this.enClase = b;
    }

    public boolean getEnClase() {
        return this.enClase;
    }

    public int getTipoDeMedio() {
        return tipoMedio;
    }

    public boolean getClaseSky() {
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

    private void cambiaRandom() {//Este método cambia los valores correspondientes al menu, postre, la clase que toma el esquiador y que tipo de medio utiliza
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

    public void setTurnoSky(int turno) {
        this.turnoSky = turno;
    }

    public int getTurnoSky() {
        return this.turnoSky;
    }

    @Override
    public void run() {
        try {
            int aux;
            salidaT.append(this.getNombre() + " entra al complejo.\n");
            while (complejo.estaAbierto()) {
            this.cambiaRandom();//Este método cambia el menu, postre, el medio a utilizar, y la clase que puede tomar
            System.out.println(this.getNombre() + " empieza.");
            aux = (int) (Math.random() * 3) + 1;//Genera un random entre 1 y 3 para ir cambiando la acción del esquiador
            System.out.println(this.nombre + " elije opcion: " + aux);
            switch (aux) {
                case 1:
                    System.out.println(this.getNombre() + " va a la CONFITERÍA.");
                    if (conf.comprarMenu(this)) {//Si logra entrar a la confitería y compra menu
                        conf.retiraMenu(this);//Como logró entrar a la confitería y compro el menu, lo retira
                        if (this.getPostre()) {//Si el esquiador compro postre
                            conf.retiraPostre(this);//Retira el postre
                        }
                        System.out.println(this.getNombre() + " come.");
                        conf.come(this);//Come en la confitería
                        conf.saleConfiteria(this);//Al terminar de comer sale de la confitería
                    }
                    System.out.println(this.getNombre() + " se va de confitería.");
                    break;

                case 2:
                    if (claseSky) {
                        System.out.println(this.getNombre() + " va a tomarClaseSky en RUN Esquiador.");
                        monInst.tomarClaseSky(this);
                    } else {
                        System.out.println(this.getNombre() + " va a tomarClaseSnow en RUN Esquiador.");
                        monInst.tomarClaseSnow(this);
                    }
                    break;

                case 3:
                    System.out.println(this.getNombre() + " va a SUBIR A UN MEDIO.");
                    monMedio.entrarMedio(this, tipoMedio);
                    switch (tipoMedio) {
                        case 1:
                            this.esperar(10);
                            break;//Simula cuanto demora en subir y bajar del medio 1
                        case 2:
                            this.esperar(8);
                            break;//Simula cuanto demora en subir y bajar del medio 2
                        case 3:
                            this.esperar(6);
                            break;//Simula cuanto demora en subir y bajar del medio 3
                        case 4:
                            this.esperar(4);
                            break;//Simula cuanto demora en subir y bajar del medio 4
                        default:
                            System.out.println(this.getNombre() + " eligió un medio inválido");
                            salidaT.append(this.getNombre() + " eligió un medio inválido.\n");
                    }
                    monMedio.termino(this, tipoMedio);
                    break;
                default:
                    System.out.println(this.getNombre() + " eligió hacer una acción inválida");
                    salidaT.append(this.getNombre() + " eligió hacer una acción inválida.\n");
            }
            this.esperar(5);//Simula el tiempo que transcurre entre que termina una actividad y comienza otra
            }
            salidaT.append(this.getNombre() + " sale del complejo.\n");
            System.out.println(this.getNombre() + " se va.");
            complejo.saleEsquiador();//Sale el esquiador del complejo
        } catch (InterruptedException ex) {
            System.err.println("Error en runEsquiador.");
        }

    }

}
