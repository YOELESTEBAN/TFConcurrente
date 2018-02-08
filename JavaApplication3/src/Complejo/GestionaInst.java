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
public class GestionaInst { //Monitor de instructores
    //Instructores hilos

    int contAlumnosSky; //Contador de alumnos que quieren tomar clase de Sky
    int contAlumnosSnow; //Contador de alumnos que quieren tomar clase de Snow
    int contInst; //Contador de instructores de Sky o Snow
    int cuentaIniciaClaseSky; //Contador de alumnos que inicia la clase de Sky (para restar instructores cada 4 alumnos que inician la clase)
    int cuentaIniciaClaseSnow; //Contador de alumnos que inicia la clase de Snow(para restar instructores cada 4 alumnos que inician la clase)
    int cuentaTerminaClaseSky; //Contador de alumnos que se van (para liberar instructores cada 4 alumnos que terminan la clase)
    int cuentaTerminaClaseSnow; //Contador de alumnos que se van (para liberar instructores cada 4 alumnos que terminan la clase)

    public GestionaInst() {
        contAlumnosSky = 0;
        contAlumnosSnow = 0;
        contInst = 5;
        cuentaIniciaClaseSky = 0;
        cuentaIniciaClaseSnow = 0;
        cuentaTerminaClaseSky = 0;
        cuentaTerminaClaseSnow = 0;
    }

    public synchronized void iniciarClase(boolean claseSky) {
        try {
            if (claseSky) { //Comprueba que sea clase de sky la que quiere tomar
                System.out.println("**********" + Thread.currentThread().toString() + " quiere tomar una clase de sky.");
                contAlumnosSky++; //Aumenta el contador de alumnos que quieren tomar clase de sky
                notifyAll(); //Notifica a todos el cambio de la variable contAlumnosSky
                while (contAlumnosSky != 4 || contInst == 0) { //Verifica si hay 4 alumnos y 1 instructor para comenzar la clase, en caso de que no espera.
                    this.wait();
                }
                System.out.println("**********" + Thread.currentThread().toString() + " toma la clase de sky");
                cuentaIniciaClaseSky++; //Aumenta el contador de cuantos toman la clase, al llegar a 4 se resta 1 instructor
                if (cuentaIniciaClaseSky == 4) {
                    contInst--;
                }
                //Toma la clase

            } else {
                System.out.println("**********" + Thread.currentThread().toString() + " quiere tomar una clase de snow.");
                contAlumnosSnow++;
                notifyAll(); //Notifica a todos el cambio de la variable contAlumnosSnow
                while (contAlumnosSnow != 4 || contInst == 0) { //Verifica si hay 4 alumnos y 1 instructor para comenzar la clase, en caso de que no espera.
                    this.wait();
                }
                System.out.println("**********" + Thread.currentThread().toString() + " toma la clase de snow");
                cuentaIniciaClaseSnow++;//Aumenta el contador de cuantos toman la clase, al llegar a 4 se resta 1 instructor
                if (cuentaIniciaClaseSnow == 4) {
                    contInst--;
                }
                //Toma la clase
            }
        } catch (InterruptedException ex) {
            System.out.println("ERROR SEVERO en iniciarClase " + Thread.currentThread().toString());
        }

    }

    public synchronized void terminarClase(boolean claseSky) {
        if (claseSky) { //Comprueba que tipo de clase termina
            contAlumnosSky--; //Resta el alumno que termina la clase
            cuentaTerminaClaseSky++;//Aumenta el contador de alumnos que terminan la clase de Sky
            if (cuentaTerminaClaseSky == 4) { //Verifica si son 4 los que terminaron la clase de Sky (1 grupo)
                contInst++; //Si 4 terminaron la clase, liberan 1 instructor
                cuentaTerminaClaseSky = 0; //Resetea el contador de alumnos que terminan la clase
                notifyAll(); //Notifica a todos el cambio de la variable contInst
            }
        } else {
            contAlumnosSnow--;//Resta el alumno que termina la clase
            cuentaTerminaClaseSnow++;//Aumenta el contador de alumnos que terminan la clase de Snow
            if (cuentaTerminaClaseSnow == 4) { //Verifica si son 4 los que terminaron la clase de Snow (1 grupo)
                contInst++; //Si 4 terminaron la clase, liberan 1 instructor
                cuentaTerminaClaseSnow = 0; //Resetea el contador de alumnos que terminan la clase
                notifyAll(); //Notifica a todos el cambio de la variable contInst
            }
        }

        System.out.println("**********" + Thread.currentThread().toString() + " termina la clase.");
        notifyAll();
    }

}
