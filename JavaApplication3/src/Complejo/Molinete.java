/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

/**
 *
 * @author Yoni
 */
public class Molinete {
    
    private int contador;
    private int id;

    public Molinete(int id) {
        this.id = id;
        this.contador=0;
    }
    
    public void setContador(int contador) {
        this.contador = contador;
    }
    
    
    public int getContador(){
        return contador; 
    }
    
    public synchronized void usarMolinete(){
        contador++;
    }
   
}
