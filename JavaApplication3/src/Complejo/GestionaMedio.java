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
public class GestionaMedio { //Monitor de los medios

    private ArrayList medios;//Arreglo que almacena los medios
    private JTextArea textoM1;//Salida de texto en Interfaz para medio1
    private JTextArea textoM2;//Salida de texto en Interfaz para medio2
    private JTextArea textoM3;//Salida de texto en Interfaz para medio3
    private JTextArea textoM4;//Salida de texto en Interfaz para medio4

    public GestionaMedio(CaidaRapida comp, JTextArea textoM1, JTextArea textoM2, JTextArea textoM3, JTextArea textoM4) {
        this.textoM1 = textoM1;
        this.textoM2 = textoM2;
        this.textoM3 = textoM3;
        this.textoM4 = textoM4;
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

    public void entrarMedio(Esquiador esq, int tipo) {//Metodo para usar medio correspondiente por esquiador
        switch (tipo) {
            case 1:
                textoM1.append(esq.getNombre() + " quiere entrar.\n");
                break;
            case 2:
                textoM2.append(esq.getNombre() + " quiere entrar.\n");
                break;
            case 3:
                textoM3.append(esq.getNombre() + " quiere entrar.\n");
                break;
            case 4:
                textoM4.append(esq.getNombre() + " quiere entrar.\n");
                break;
        }
        synchronized (esq) {
            System.out.println(Thread.currentThread().toString() + " quiere entrar al medio " + tipo);
            Medio m = (Medio) (medios.get(tipo - 1));
            m.usarMedio();
        }
    }

    public synchronized void startMedios() { //Inicia los medios
        int i;
        Medio m;
        for (i = 1; i < 5; i++) {
            m = (Medio) (this.medios.get(i - 1));
            m.start();
        }
    }

    public synchronized String getEstadistica() { //Saca la cuenta de cuanto se uso cada medio
        String est = "Cantidad de veces que se uso cada medio:\n";
        int aux, cant;
        Medio m;
        for (aux = 0; aux < 4; aux++) {
            m = (Medio) (this.medios.get(aux));
            cant = m.sacarCuenta();
            est = est + "Medio " + (aux + 1) + ": " + cant + " veces.\n";
        }
        return est;
    }

    public synchronized void cerrarMedios() { //Cierra los medios
        int i;
        Medio m;
        for (i = 1; i < 5; i++) {
            m = (Medio) (this.medios.get(i - 1));
            m.cerrar();
        }
    }

    public synchronized void termino(Esquiador esq, int tipo) {
        switch (tipo) {
            case 1:
                textoM1.append(esq.getNombre() + " terminó de bajar.\n");
                break;
            case 2:
                textoM2.append(esq.getNombre() + " terminó de bajar.\n");
                break;
            case 3:
                textoM3.append(esq.getNombre() + " terminó de bajar.\n");
                break;
            case 4:
                textoM4.append(esq.getNombre() + " terminó de bajar.\n");
                break;
        }
    }

}
