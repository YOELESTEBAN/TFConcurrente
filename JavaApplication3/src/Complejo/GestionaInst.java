/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import javax.swing.JTextArea;

/**
 *
 * @author Latitude
 */
public class GestionaInst {

    private int contAlumnosSky;//Contador de alumnos que quieren tomar clase de Sky
    private int contAlumnosSnow;//Contador de alumnos que quieren tomar clase de Snow
    private int cantInst;//Contador de instructores de Sky o Snow disponibles para dar clase
    private ListaEspera colaSky; //Lista de espera de Sky
    private ListaEspera colaSnowClase; //Lista de espera de clase de Snow
    private ListaEspera colaSnowEntrada; //Lista de espera de entrada a Snow
    private long horaLimiteSky; //Guarda hasta que hora pueden esperar para formar el grupo para la clase de Sky
    private long horaLimiteSnow; //Guarda hasta que hora pueden esperar para formar el grupo para la clase de Snow
    private boolean seCumplioTiempoSky; //Boolean que marca si se cumplio Tiempo limite del equipo Sky
    private boolean seCumplioTiempoSnow; //Boolean que marca si se cumplio Tiempo limite del equipo Snow
    private boolean capitanSky; //Boolean que marca si esta ocupado el lugar de capitanSky 
    private boolean capitanSnow; //Boolean que marca si esta ocupado el lugar de capitanSnow 
    private JTextArea salidaT;//Salida de texto en Interfaz

    public GestionaInst(JTextArea salidaT) {
        contAlumnosSky = 0;
        contAlumnosSnow = 0;
        cantInst = 5;
        horaLimiteSky = 0;
        horaLimiteSnow = 0;
        seCumplioTiempoSky = false;
        seCumplioTiempoSnow = false;
        this.salidaT = salidaT;
        colaSky = new ListaEspera();
        colaSnowClase = new ListaEspera();
        colaSnowEntrada = new ListaEspera();
        capitanSky = false;
        capitanSnow = false;
        this.salidaT.append("Hay " + cantInst + " instructores disponibles.\n");
    }

    public synchronized void tomarClaseSky(Esquiador esq) {
        try {
            System.out.println("**********" + esq.getNombre() + " quiere tomar una clase de sky.");
            salidaT.append(esq.getNombre() + " quiere tomar una clase de sky.\n");
            contAlumnosSky++; //Aumenta el contador de alumnos de Sky esperando
            colaSky.ingresar(esq); //Se almacena en una lista
            System.out.println("**********" + esq.getNombre() + " ingreso a lista.");
            if (!capitanSky) { //Es el primer esquiador esperando
                capitanSky = true; //toma el lugar de Capitan
                seCumplioTiempoSky = false; //Como comienza el conteo, se pone en falso
                horaLimiteSky = System.currentTimeMillis() + 15000; //Se asigna horario limite
                salidaT.append(Thread.currentThread().toString() + " espera para tomar la clase de sky.\n");
                while ((contAlumnosSky < 4 || cantInst < 1) && !seCumplioTiempoSky) { //Mientras no se junten los alumnos, el instructor y no se cumpla el tiempo límite, esperan
                    wait(500); //Solo el primer esquiador que espera hasta 500 milisegundos
                    if (horaLimiteSky < System.currentTimeMillis()) { //Cuando se despierta comprueba si se cumplió el tiempo límite
                        seCumplioTiempoSky = true; //Si se cumplió el tiempo se cambia el valor de la variable booleana seCumplioTiempoSky
                    }
                }
                if (seCumplioTiempoSky) { //Si salió del bucle porque se cumplió el tiempo
                    salidaT.append("Se cumplió el tiempo límite de espera para tomar la clase de sky.\n");
                    contAlumnosSky--; //Se retira y resta 1 a la cantidad de alumnos
                    while (colaSky.getTamaño() > 0) {//Si se cumplio el tiempo, el primer esquiador despierta a todo el grupo
                        (colaSky.retirar()).notify();
                    }
                    capitanSky = false;//Deja de ser el capitan para que otro lo tome
                } else { //Se completó el grupo
                    salidaT.append(Thread.currentThread().toString() + " va a tomar la clase de sky.\n");
                    cantInst--; //El primer esquiador resta 1 instructor a la cantidad de instructores disponibles
                    salidaT.append("Hay " + cantInst + " instructores disponibles.\n");
                    for (int aux = 1; aux < 4; aux++) {//El primer esquiador despierta a todo el grupo para tomar la clase
                        (colaSky.retirar()).notify();
                    }
                    capitanSky = false;//Deja de ser el capitan para que otro lo tome
                    esq.wait(2000); //Toma la clase
                    salidaT.append(Thread.currentThread().toString() + " termino de tomar la clase de sky.\n");
                    cantInst++; //Devuelve el instructor para que este disponible
                    salidaT.append("Hay " + cantInst + " instructores disponibles.\n");
                    contAlumnosSky--; //Se retira y resta 1 a la cantidad de alumnos
                }
            } else { //Si no es el primer esquiador
                salidaT.append(Thread.currentThread().toString() + " espera para tomar la clase de sky.\n");
                esq.wait();
                if (!seCumplioTiempoSky) { //Se armo grupo
                    salidaT.append(Thread.currentThread().toString() + " va a tomar la clase de sky.\n");
                    esq.wait(2000); //Toma la clase
                    salidaT.append(Thread.currentThread().toString() + " termino de tomar la clase de sky.\n");
                    contAlumnosSky--; //Se retira y resta 1 a la cantidad de alumnos
                } else {//Si se cumplio el tiempo
                    contAlumnosSky--; //Se retira y resta 1 a la cantidad de alumnos
                    salidaT.append(Thread.currentThread().toString() + " se va porque no se pudo armar el grupo de sky.\n");
                }
            }
        } catch (InterruptedException ex) {
            System.out.println("ERROR SEVERO en tomarClaseSky " + Thread.currentThread().toString());
        }

    }

    public synchronized void tomarClaseSnow(Esquiador esq) {
        try {
            int aux;//variable auxiliar que se usa de contador
            System.out.println("**********" + Thread.currentThread().toString() + " quiere tomar una clase de snow.");
            if (contAlumnosSnow >= 4) {//Aca esperan cuando se completa 1 grupo
                System.out.println("1**********" + Thread.currentThread().toString() + "ya hay un grupo, espero");
                colaSnowEntrada.ingresar(esq); //Se almacena en una lista
                wait();
            }
            System.out.println("2**********" + Thread.currentThread().toString() + "salio del grupo de espera");
            contAlumnosSnow++; //Aumenta el contador de alumnos de Snow 
            //comprobar si es el alumno multiplo de 4 (en ese caso esperar aca) y cuando se va tiene q despertar al siguiente
            if (!capitanSnow) { //Es el primer esquiador esperando
                capitanSnow = true; //toma el lugar de Capitan
                //contAlumnosSnow=1;
                seCumplioTiempoSnow = false; //Como comienza el conteo, se pone en falso
                horaLimiteSnow = System.currentTimeMillis() + 15000; //Se asigna horario limite
                salidaT.append(Thread.currentThread().toString() + " es el capitan y espera para tomar la clase de snow.\n");
                aux = 0;
                while (colaSnowEntrada.getTamaño() > 0 && aux < 3) {//Despierta a lo sumo a otros 3 esquiadores que esten esperando
                    System.out.println("3**********" + Thread.currentThread().toString() + "despierta a 1 que este esperando, colaSnow tamaño= " + colaSnowClase.getTamaño() + ".");
                    (colaSnowEntrada.retirar()).notify();
                    aux++;
                }
                while ((contAlumnosSnow < 4 || cantInst < 1) && !seCumplioTiempoSnow) { //Mientras no se junten los alumnos, el instructor y no se cumpla el tiempo límite, esperan
                    wait(500); //Solo el primer esquiador se despierta cada 1 segundo
                    if (horaLimiteSnow < System.currentTimeMillis()) { //Cuando se despierta comprueba si se cumplió el tiempo límite
                        seCumplioTiempoSnow = true; //Si se cumplió el tiempo se cambia el valor de la variable booleana seCumplioTiempoSnow
                    }
                }
                System.out.println("3.5**" + Thread.currentThread().toString() + " contAlumnosSnow: " + contAlumnosSnow + " cantInst= " + cantInst + " Se cumplio tiempo: " + seCumplioTiempoSnow);
                if (seCumplioTiempoSnow) { //Si salió del bucle porque se cumplió el tiempo
                    salidaT.append("Se cumplió el tiempo límite de espera para tomar la clase de snow.\n");
                    /*while (colaSnow.getTamaño() > 0) {//Si se cumplio el tiempo, el primer esquiador despierta a todo el grupo
                        (colaSnow.retirar()).notify();
                    }*/
                    aux = 0;
                    while (colaSnowClase.getTamaño() > 0 && aux < 3) {//Despierta a lo sumo a otros 3 esquiadores que esten esperando para tomar la clase
                        System.out.println("4**********" + Thread.currentThread().toString() + "despierta a 1 que este esperando, colaSnow tamaño= " + colaSnowClase.getTamaño() + ".");
                        (colaSnowClase.retirar()).notify();
                    }
                    capitanSnow = false;//Deja de ser el capitan para que otro lo tome
                    contAlumnosSnow--; //Se retira y resta 1 a la cantidad de alumnos
                    salidaT.append(Thread.currentThread().toString() + " se va porque no se pudo armar el grupo de snow.\n");
                    if (colaSnowEntrada.getTamaño() > 0) {//Solo despierta 1 que este esperando para que tome capitanSnow
                        System.out.println("5**********" + Thread.currentThread().toString() + "despierta a 1 que este esperando, colaSnow tamaño= " + colaSnowClase.getTamaño() + ".");
                        (colaSnowClase.retirar()).notify();
                    }
                } else { //Se completó el grupo
                    salidaT.append(Thread.currentThread().toString() + " toma la clase de snow.\n");
                    cantInst--; //El primer esquiador resta 1 instructor a la cantidad de instructores disponibles
                    salidaT.append("Hay " + cantInst + " instructores disponibles.\n");
                    for (aux = 0; aux < 3; aux++) {//El primer esquiador despierta al resto del grupo para tomar la clase
                        System.out.println("6**********" + Thread.currentThread().toString() + " colaSnow tamaño= " + colaSnowClase.getTamaño() + ".");
                        (colaSnowClase.retirar()).notify();
                    }
                    esq.esperar(2); //Toma la clase
                    salidaT.append(Thread.currentThread().toString() + " termino la clase de snow.\n");
                    for (aux = 0; aux < 3; aux++) {//El primer esquiador despierta al resto del grupo para terminar la clase
                        System.out.println("6.5**********" + Thread.currentThread().toString() + " colaSnow tamaño= " + colaSnowClase.getTamaño() + ".");
                        (colaSnowClase.retirar()).notify();
                    }
                    cantInst++; //Devuelve el instructor para que este disponible
                    salidaT.append("Hay " + cantInst + " instructores disponibles.\n");
                    capitanSnow = false;//Deja de ser el capitan para que otro lo tome
                    contAlumnosSnow--; //Se retira y resta 1 a la cantidad de alumnos
                    if (colaSnowEntrada.getTamaño() > 0) {//Solo despierta 1 que este esperando para que tome capitanSnow
                        System.out.println("7**********" + Thread.currentThread().toString() + "despierta a 1 que este esperando, colaSnow tamaño= " + colaSnowClase.getTamaño() + ".");
                        (colaSnowEntrada.retirar()).notify();
                    }
                }
            } else { //Si no es el primer esquiador
                colaSnowClase.ingresar(esq); //Se almacena en una lista
                salidaT.append(Thread.currentThread().toString() + " espera para tomar la clase de snow.\n");
                synchronized (esq){ esq.wait();} //ver blockes synchronized o clase locks
                if (!seCumplioTiempoSnow) { //Se armo grupo
                    salidaT.append(Thread.currentThread().toString() + " toma la clase de snow.\n");
                    wait(); //Toma la clase
                    salidaT.append(Thread.currentThread().toString() + " termino la clase de snow.\n");
                    contAlumnosSnow--; //Se retira y resta 1 a la cantidad de alumnos
                } else {//Si se cumplio el tiempo
                    contAlumnosSnow--; //Se retira y resta 1 a la cantidad de alumnos
                    salidaT.append(Thread.currentThread().toString() + " se va porque no se pudo armar el grupo de snow.\n");
                }
            }
        } catch (InterruptedException ex) {
            System.out.println("ERROR SEVERO en tomarClaseSnow " + Thread.currentThread().toString());
        }
    }

}
