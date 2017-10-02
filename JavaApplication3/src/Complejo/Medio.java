/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import java.util.ArrayList;


/**
 *
 * @author Yoni
 */
public class Medio {
    
    private ArrayList molinetes; //Vector que contiene los molinetes de este medio
    private int tipo; //Tipo de medio (entre 1 y 4)
    private boolean disponible; //Cuando disponible=true entonces pueden subir 

    public Medio(int tipo) {
        
        if (tipo>0 && tipo<5){ //Verifica que la cantidad de molinetes del medio sea correcta
            int aux=0; //Entero auxiliar utilizado para el ID del molinete a crear
            molinetes= new ArrayList (tipo); //Se declara el vector que contiene los molinetes de este medio
            while (aux<tipo){
                insertarMolinete(aux); //Crea los molinetes y los va insertando en el vector
                aux++;
            }
        this.tipo = tipo; //Tipo de medio (de 1 a 4) OJOOOOOOOOOOO (verificar que no se haya creado ya, si o si 4 medios tiene q haber)
        }
        
    }

    public int getTipo() {
        return tipo;
    }

    private void insertarMolinete(int id) {
        Molinete nuevo= new Molinete (id); //Crea el molinete con contador 0 y el id que ingresa al metodo
        this.molinetes.add(nuevo); //Inserta el molinete en el vector de este medio
        
    }
    
    public void entrarMedio(){
        if (disponible){
            
        }
    }
    
    private synchronized void usarMedio(){
        
    }
    
    public int sacarCuenta (){ //Saca el conteo final de la cantidad que esquiadores que usaron este medio
        int total=0; //Se usa para llevar la cuenta
        Molinete auxM; //Se usa temporalmente para recuperar cada molinete del Arreglo
        for (int i=0; i<molinetes.size(); i++){
            auxM= (Molinete) molinetes.get(i);
            total= total+ auxM.getContador();
        }   
        return total;
    }
    
    
    
}
