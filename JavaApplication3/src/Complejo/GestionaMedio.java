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
 * @author Latitude
 */
public class GestionaMedio {

    private ArrayList medios;//Arreglo que almacena los medios
    private JTextArea textoM1;//Salida de texto en Interfaz para medio1
    private JTextArea textoM2;//Salida de texto en Interfaz para medio2
    private JTextArea textoM3;//Salida de texto en Interfaz para medio3
    private JTextArea textoM4;//Salida de texto en Interfaz para medio4

    public GestionaMedio(CaidaRapida comp, JTextArea textoM1, JTextArea textoM2, JTextArea textoM3, JTextArea textoM4) {
        Medio m;
        medios = new ArrayList(5);
        m = new Medio(1, comp, textoM1);
        medios.add(m);
        m = new Medio(2, comp, textoM2);
        medios.add(m);
        m = new Medio(3, comp, textoM3);
        medios.add(m);
        m = new Medio(4, comp, textoM4);
        medios.add(m);
    }

    public synchronized void entrarMedio(int tipo) {
        System.out.println(Thread.currentThread().toString() + " quiere entrar al medio " + tipo);
        Medio m = (Medio) (medios.get(tipo - 1));
        m.usarMedio();
    }

    public void startMedios() {
        int i;
        Medio m;
        for (i = 1; i < 5; i++) {
            m = (Medio) (this.medios.get(i - 1));
            m.start();
        }
    }

    public String getEstadistica() {
        String est = "Cantidad de veces que se uso cada medio:\n";
        int aux, cant;
        Medio m;
        for (aux = 0; aux < 4; aux++) {
            System.out.println("aux: " + aux);
            System.out.println("medios tiene: " + this.medios.size());
            m = (Medio) (this.medios.get(aux));
            System.out.println("Medio: " + m.getName());
            cant = m.sacarCuenta();
            est = est + "Medio " + (aux + 1) + ": " + cant + " veces.\n";
        }
        return est;
    }

}
