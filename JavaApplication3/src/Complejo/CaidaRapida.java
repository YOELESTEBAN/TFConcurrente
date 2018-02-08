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
public class CaidaRapida {//Monitor de medios

    private ArrayList medios;//Arreglo que almacena los medios
    private ArrayList aerosillas;//Arreglo que almacena las aerosillas de cada medio
    private JTextArea salidaT;//Salida de texto en Interfaz

    public CaidaRapida() {
        int i;
        Medio m;
        Aerosilla aero;
        medios = new ArrayList(5);
        aerosillas = new ArrayList(5);
        for (i = 1; i < 5; i++) {
            m = new Medio(i);
            aero = new Aerosilla(i, m);//Crea la aerosilla del medio que corresponde
            medios.add(m);
            aerosillas.add(aero);
        }
    }

    public void entrarMedio(int tipo) {
        System.out.println(Thread.currentThread().toString() + " quiere entrar al medio " + tipo);
        Medio m = (Medio) (medios.get(tipo - 1));
        m.usarMedio();
    }

    public void startAerosillas() {
        int i;
        Aerosilla aero;
        for (i = 1; i < 5; i++) {
            aero = (Aerosilla) (this.aerosillas.get(i - 1));
            aero.start();
        }
    }
}
