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
    private Confiteria conf;
    private GestionaInst monInst;
    private GestionaMedio monMedios;
    private int cantEsquiadores;
    private JTextArea textoEsquiadores;
    private JTextArea textoM1;
    private JTextArea textoM2;
    private JTextArea textoM3;
    private JTextArea textoM4;

    public CaidaRapida(JTextArea textoConfiteria, JTextArea textoClases, JTextArea textoEsq, JTextArea textoM1, JTextArea textoM2, JTextArea textoM3, JTextArea textoM4) {
        conf = new Confiteria(textoConfiteria);
        monInst = new GestionaInst(textoClases);
        this.textoEsquiadores = textoEsq;
        cantEsquiadores = 0;
        this.textoM1 = textoM1;
        this.textoM2 = textoM2;
        this.textoM3 = textoM3;
        this.textoM4 = textoM4;
    }

    public void iniciaMedios(CaidaRapida comp) {
        this.monMedios = new GestionaMedio(comp, textoM1, textoM2, textoM3, textoM4);
        monMedios.startMedios();
    }

    public void abrirComplejo() {
        estaAbierto = true;
    }

    public void cerrarComplejo() {
        estaAbierto = false;
    }

    public boolean estaAbierto() {
        return this.estaAbierto;
    }

    public void entraEsquiador(CaidaRapida comp, String nombre) {
        Esquiador aux = new Esquiador(nombre, conf, monInst, monMedios, textoEsquiadores, comp);
        cantEsquiadores++;
        aux.start();
    }

    public void saleEsquiador() {
        cantEsquiadores--;
        if (cantEsquiadores == 0) {
            textoEsquiadores.append("El complejo esta CERRADO.\n");
        }
    }

    public String getEstadistica() {
        return this.monMedios.getEstadistica();
    }

}
