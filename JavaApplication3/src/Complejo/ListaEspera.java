/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import java.util.ArrayList;

/**
 *
 * @author Latitude
 */
public class ListaEspera {
    private ArrayList arreglo;
    private int posIni;
    private int posFin;
    
    public ListaEspera(){
        arreglo= new ArrayList();
        posIni=0;
        posFin=0;
    }
    
    public void ingresar(Esquiador e){
        arreglo.add(posFin,e);
        posFin++;
    }
    public Esquiador retirar(){
        Esquiador aux= (Esquiador) arreglo.get(posIni);
        posIni++;
        return aux;
    }
    public int getTamaño(){
        return (posFin-posIni);
    }
    
}
