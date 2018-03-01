/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import javax.swing.JTextArea;

/**
 *
 * @author Yoni
 */
public class CaidaRapida {

    private boolean estaAbierto;
    private long horaCierre;
    private Confiteria conf;
    private GestionaClase monInst;
    private GestionaMedio monMedios;
    private int cantEsquiadores;
    private JTextArea textoEsquiadores;
    private JTextArea textoM1;
    private JTextArea textoM2;
    private JTextArea textoM3;
    private JTextArea textoM4;
    private Interfaz interfaz; //Se utiliza para avisar los cambios necesarios al cerrar el complejo por cumplimiento de horario

    public CaidaRapida(JTextArea textoConfiteria, JTextArea textoEsq, JTextArea textoM1, JTextArea textoM2, JTextArea textoM3, JTextArea textoM4, Interfaz interfaz) {
        conf = new Confiteria(textoConfiteria);
        this.textoEsquiadores = textoEsq;
        cantEsquiadores = 0;
        this.textoM1 = textoM1;
        this.textoM2 = textoM2;
        this.textoM3 = textoM3;
        this.textoM4 = textoM4;
        this.interfaz = interfaz;
    }

    public void abrirComplejo(JTextArea textoClases, CaidaRapida comp) {
        estaAbierto = true;
        horaCierre = System.currentTimeMillis() + 420000; //Suma 7 minutos al horario actual que es lo que dura la simulación (420000 milisegundos = 7 minutos reales = 7 horas simulación)
        this.monMedios = new GestionaMedio(comp, textoM1, textoM2, textoM3, textoM4);
        monMedios.startMedios();
        monInst = new GestionaClase(textoClases, comp);
        monInst.startInstructores();
    }

    public void cerrarComplejo() {
        estaAbierto = false;
        monInst.cerrar();
        synchronized (this) {
            notifyAll();
        }
    }

    public boolean estaAbierto() {//Comprueba si esta abierto el complejo
        boolean aux = true;
        if (System.currentTimeMillis() > horaCierre) {
            aux = false;
            this.estaAbierto = false;
            this.cerrarComplejo();
        }
        return (aux && this.estaAbierto);
    }

    public void entraEsquiador(CaidaRapida comp, String nombre) {
        Esquiador aux = new Esquiador(nombre, conf, monInst, monMedios, textoEsquiadores, comp);
        cantEsquiadores++;
        aux.start();
    }

    public void saleEsquiador() {
        cantEsquiadores--;
        if (cantEsquiadores == 0) {
            monMedios.cerrarMedios();
            textoEsquiadores.append("El complejo esta CERRADO.\n");
            interfaz.habilitarEstadística(); //Avisa a la interfaz que tiene que habilitar el boton de estadística
            interfaz.cerroComplejo(); //Avisa a la interfaz que tiene que cambiar el estado de los botones "Cerrar Complejo" y "Agregar Esquiador"
        }

    }

    public String getEstadistica() {
        return this.monMedios.getEstadistica();
    }

}
