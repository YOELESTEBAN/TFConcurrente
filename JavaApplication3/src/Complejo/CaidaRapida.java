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

    private boolean estaAbierto;//Boolean que se utiliza de bandera para determinar si esta o no abierto el complejo
    private long horaCierre;//Se usa para setear la hora de cierre
    private Confiteria conf;//Instancia de Confitería
    private GestionaClase monInst; //Instancia de monitor de clases
    private GestionaMedio monMedios;//Instancia de monitor de medios
    private int cantEsquiadores; //Contador de esquiadores
    private JTextArea textoEsquiadores;//Salida de texto a interfaz
    private JTextArea textoM1;//Salida de texto a interfaz
    private JTextArea textoM2;//Salida de texto a interfaz
    private JTextArea textoM3;//Salida de texto a interfaz
    private JTextArea textoM4;//Salida de texto a interfaz
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

    public void abrirComplejo(JTextArea textoClases, CaidaRapida comp) { //Metodo que abre el complejo
        estaAbierto = true;
        horaCierre = System.currentTimeMillis() + 420000; //Suma 7 minutos al horario actual que es lo que dura la simulación (420000 milisegundos = 7 minutos reales = 7 horas simulación)
        this.monMedios = new GestionaMedio(comp, textoM1, textoM2, textoM3, textoM4);//Crea el monitor de medios
        monMedios.startMedios();//Le da inicio al run de los medios
        monInst = new GestionaClase(textoClases, comp);//Crea el monitor de las clases
        monInst.startInstructores();//Le da inicio al run de los instructores
    }

    public void cerrarComplejo() {//Cierra el complejo
        estaAbierto = false;
        monInst.cerrar();//Cierra las clases
        synchronized (this) {
            notifyAll();//Notifica a todos el cambio
        }
    }

    public boolean estaAbierto() {//Comprueba si esta abierto el complejo y devuelve el resultado
        boolean aux = true;
        if (System.currentTimeMillis() > horaCierre) {
            aux = false;
            this.estaAbierto = false;
            this.cerrarComplejo();
        }
        return (aux && this.estaAbierto);
    }

    public void entraEsquiador(CaidaRapida comp, String nombre) { //Metodo para agregar esquiadores y llevar el contador
        Esquiador aux = new Esquiador(nombre, conf, monInst, monMedios, textoEsquiadores, comp);
        cantEsquiadores++;
        aux.start();
    }

    public void saleEsquiador() { //Cada vez que sale un esquiador resta el contador, cuando no hay ningun esquiador avisa para habilitar botón de estadísticas
        cantEsquiadores--;
        if (cantEsquiadores == 0) {
            monMedios.cerrarMedios();
            textoEsquiadores.append("El complejo esta CERRADO.\n");
            interfaz.habilitarEstadística(); //Avisa a la interfaz que tiene que habilitar el boton de estadística
            interfaz.cerroComplejo(); //Avisa a la interfaz que tiene que cambiar el estado de los botones "Cerrar Complejo" y "Agregar Esquiador"
        }

    }

    public String getEstadistica() { //Metodo para obtener estadísticas de los medios
        return this.monMedios.getEstadistica();
    }

}
