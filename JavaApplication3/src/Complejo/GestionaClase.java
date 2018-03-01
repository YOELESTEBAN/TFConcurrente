/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Complejo;

import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JTextArea;

/**
 *
 * @author Latitude
 */
public class GestionaClase {

    private CaidaRapida complejo;//Clase principal
    private int contAlumnosSky;//Contador de alumnos que quieren tomar clase de Sky
    private int contAlumnosSnow;//Contador de alumnos que quieren tomar clase de Snow
    private int cantInst;//Contador de instructores de Sky o Snow disponibles para dar clase
    private long horaLimiteSky; //Guarda hasta que hora pueden esperar para formar el grupo para la clase de Sky
    private long horaLimiteSnow; //Guarda hasta que hora pueden esperar para formar el grupo para la clase de Snow
    private LinkedBlockingQueue colaSky; //Lista de espera de Sky
    private LinkedBlockingQueue colaSnow; //Lista de espera de Snow
    private boolean seCumplioTiempoSky; //Boolean que marca si se cumplio Tiempo limite del equipo Sky
    private boolean seCumplioTiempoSnow; //Boolean que marca si se cumplio Tiempo limite del equipo Snow
    private ListaEspera listaInst;//Lista que almacena los instructores disponibles
    private JTextArea salidaT;//Salida de texto en Interfaz

    public GestionaClase(JTextArea salidaT, CaidaRapida comp) {
        cantInst = 5;
        contAlumnosSky = 0;
        contAlumnosSnow = 0;
        seCumplioTiempoSky = false;
        seCumplioTiempoSnow = false;
        colaSky = new LinkedBlockingQueue();
        colaSnow = new LinkedBlockingQueue();
        this.salidaT = salidaT;
        complejo = comp;
        listaInst = new ListaEspera();
    }

    public synchronized void startInstructores() {
        System.out.println(Thread.currentThread().toString() + " start instructores");
        Instructor inst;
        for (int aux = 0; aux < 5; aux++) {
            inst = new Instructor("In" + aux, complejo, this, salidaT);
            listaInst.ingresar(inst);
            inst.start();
        }
    }

    public synchronized void tomarClaseSky(Esquiador esq) {
        try {
            boolean banderaSky = false;
            if (complejo.estaAbierto()) {//Si esta abierto
                System.out.println(esq.getNombre() + " esta abierto, agrego el esquiador a la cola y al contAlumnosSky");
                colaSky.add(esq);
                contAlumnosSky++;
                salidaT.append(esq.getNombre() + " quiere tomar una clase de Sky.\n");
                if (contAlumnosSky >= 4) {//Si hay 4 o mas alumnos esperando
                    System.out.println(esq.getNombre() + " hay 4 o mas alumnos.");
                    if ((contAlumnosSky % 4) == 0) {//Si es el 4to alumno
                        System.out.println(esq.getNombre() + " es el 4to.");
                        if (cantInst > 0) {//Si hay instructor disponible
                            salidaT.append(esq.getNombre() + " espera la clase de Sky.\n");
                            System.out.println(esq.getNombre() + " hay 1 inst disponible");
                            Instructor aux = (Instructor) listaInst.retirar(); //Recupera y saca del frente
                            cantInst--;
                            System.out.println(esq.getNombre() + " resto 1 inst, quedan: " + cantInst + " y llamo al instructor.");
                            llamarInstructorSky(aux);
                            System.out.println(esq.getNombre() + " espera al inst");
                            while (!esq.getEnClase()) {
                                this.wait();
                            }
                            System.out.println(esq.getNombre() + " despierta para tomar la clase en MONITOR");
                            while (esq.getEnClase()) { //Cuando entra en la clase
                                this.wait();
                            }
                        } else {//Si no hay Instructor espero
                            salidaT.append(esq.getNombre() + " espera que se libere un instructor.\n");
                            while (!seCumplioTiempoSky && cantInst < 1) {
                                System.out.println(esq.getNombre() + " comprueba porq no hay instructor.");
                                this.wait(500);
                            }
                            if (seCumplioTiempoSky) { //Si se cumplió el tiempo limite
                                salidaT.append(esq.getNombre() + " se va porque se cumplio el tiempo de espera.\n");
                                System.out.println(esq.getNombre() + "  se cumplio tiempo, no pudo tomar la clase entonces borra de cola y contAlumnosSky");
                                colaSky.remove(esq);
                                contAlumnosSky--;
                            } else { //Salió del while porque hay un instructor disponible
                                salidaT.append(esq.getNombre() + " espera la clase de Sky.\n");
                                System.out.println(esq.getNombre() + " hay 1 inst disponible");
                                Instructor aux = (Instructor) listaInst.retirar(); //Recupera y saca del frente
                                cantInst--;
                                System.out.println(esq.getNombre() + " resto 1 inst, quedan: " + cantInst + " y llamo al instructor.");
                                llamarInstructorSky(aux);
                                while (!esq.getEnClase()) {
                                    this.wait();
                                }
                                System.out.println(esq.getNombre() + " despierta para tomar la clase en MONITOR");
                                while (esq.getEnClase()) { //Cuando entra en la clase
                                    this.wait();
                                }
                            }
                        }
                    } else { //Si no es el 4to quiere decir que ya hay un grupo formado 
                        if (cantInst > 0) { //Si hay instructores disponibles espera
                            salidaT.append(esq.getNombre() + " espera la clase de Sky.\n");
                            long j = horaLimiteSky - System.currentTimeMillis();
                            while (!esq.getEnClase() && !seCumplioTiempoSky) {
                                System.out.println(esq.getNombre() + " espera por entrar a clase o se cumpla tiempo");
                                this.wait(j);//A lo sumo espera j milisegundos
                                if (esq.getEnClase()) {
                                    System.out.println(esq.getNombre() + " va a tomar la clase");
                                    banderaSky = true;
                                }
                            }
                            if (!banderaSky) { //Si el esquiador no pudo tomar su clase
                                salidaT.append(esq.getNombre() + " se va porque no se formo grupo.\n");
                                System.out.println(esq.getNombre() + " no pudo tomar la clase entonces borra de cola y contAlumnosSky");
                                colaSky.remove(esq);
                                contAlumnosSky--;
                                if (complejo.estaAbierto()) {
                                    System.out.println(esq.getNombre() + " no pudo tomar clase por falta de grupo");
                                } else {
                                    System.out.println(esq.getNombre() + " no pudo tomar clase por el horario");
                                }
                            } else {
                                System.out.println(esq.getNombre() + " duerme porque va a tomar la clase EN MONITOR");
                                while (esq.getEnClase()) { //Cuando entra en la clase
                                    this.wait();
                                }
                            }
                        } else {//Si no hay instructores disponibles y hay un grupo esperando uno, se va
                            salidaT.append(esq.getNombre() + " se va porque no hay instructor.\n");
                            System.out.println(esq.getNombre() + " no pudo tomar la clase entonces borra de cola y contAlumnosSky");
                            colaSky.remove(esq);
                            contAlumnosSky--;
                            System.out.println(esq.getNombre() + " no pudo tomar clase por falta de instructor y grupo");
                        }
                    }
                } else {//Si hay menos de 4
                    if (contAlumnosSky == 1) {//Si es el primero
                        salidaT.append(esq.getNombre() + " espera la clase de Sky.\n");
                        horaLimiteSky = System.currentTimeMillis() + 20000; //Se asigna horario limite de espera
                        seCumplioTiempoSky = false; //Se setea como falso el boolean
                        while (!esq.getEnClase() && !seCumplioTiempoSky) { //Mientras no se arme el grupo y no se cumpla el tiempo límite, esperan
                            System.out.println(esq.getNombre() + " comprueba.");
                            this.wait(500); //El primer esquiador se despierta cada 500 milisegundos ya que se encarga de setear la variable seCumplioTiempoSky
                            if (horaLimiteSky < System.currentTimeMillis()) { //Cuando se despierta comprueba si se cumplió el tiempo límite
                                System.out.println(esq.getNombre() + " detecto que se cumplió el tiempo de espera.");
                                seCumplioTiempoSky = true; //Si se cumplió el tiempo se cambia el valor de la variable booleana seCumplioTiempoSky
                            }
                        }
                        if (esq.getEnClase()) {
                            System.out.println(esq.getNombre() + " va a tomar la clase");
                            banderaSky = true;
                        }
                        if (seCumplioTiempoSky) {//Si se cumplio el tiempo notifica
                            notifyAll();
                        }

                    } else {
                        salidaT.append(esq.getNombre() + " espera la clase de Sky.\n");
                        long k = horaLimiteSky - System.currentTimeMillis();
                        while (!esq.getEnClase() && !seCumplioTiempoSky) {
                            System.out.println(esq.getNombre() + " espera por entrar a clase o se cumpla tiempo");
                            this.wait(k);//A lo sumo espera k milisegundos
                            if (esq.getEnClase()) {
                                System.out.println(esq.getNombre() + " va a tomar la clase");
                                banderaSky = true;
                            }
                        }
                    }
                    if (!banderaSky) { //Si el esquiador no pudo tomar su clase
                        salidaT.append(esq.getNombre() + " se va porque no se formo grupo.\n");
                        System.out.println(esq.getNombre() + " no pudo tomar la clase entonces borra de cola y contAlumnosSky");
                        colaSky.remove(esq);
                        contAlumnosSky--;
                        if (complejo.estaAbierto()) {
                            System.out.println(esq.getNombre() + " no pudo tomar clase por falta de grupo-----------------------------------------------");
                        } else {
                            System.out.println(esq.getNombre() + " no pudo tomar clase por el horario");
                        }
                    } else {
                        System.out.println(esq.getNombre() + " duerme porque va a tomar la clase EN MONITOR");
                        while (esq.getEnClase()) { //Cuando entra en la clase
                            this.wait();
                        }
                    }
                }
            }//Si cerró el complejo
            System.out.println(esq.getNombre() + " sale de tomarClaseSky.");
        } catch (InterruptedException ex) {
            System.out.println("ERROR SEVERO en tomarClaseSky " + Thread.currentThread().toString());
        }
    }

    public void darClaseSky(Instructor inst) {
        try {
            boolean banderaSky;
            System.out.println(inst.getName() + " va a dar clase en monitor.");
            if (contAlumnosSky >= 4 && complejo.estaAbierto()) {
                System.out.println(inst.getName() + " mientras hay alumnos y esta abierto");
                ListaEspera salidaSky = new ListaEspera();
                Esquiador aux;
                banderaSky = false;
                synchronized (this) {
                    if (contAlumnosSky >= 4) {
                        System.out.println(inst.getName() + " si hay mas de 4 alumnos.");
                        contAlumnosSky = contAlumnosSky - 4;
                        salidaT.append(inst.getName() + " da clase a: ");
                        for (int i = 1; i < 5; i++) {
                            aux = (Esquiador) colaSky.take();
                            aux.setEnClase(true);
                            System.out.println(aux.getNombre() + " va a tomar la clase en monitor.");
                            salidaSky.ingresar(aux);
                            salidaT.append(aux.getNombre() + " ");
                        }
                        salidaT.append(".\n");
                        notifyAll();//Despierta a todos los esquiadores
                        banderaSky = true;
                    }
                }
                if (banderaSky) {
                    System.out.println(inst.getName() + " esta dando la clase.");
                    Thread.sleep(2000);//Simula duración de la clase
                    synchronized (this) {
                        salidaT.append(inst.getName() + " termina la clase de: ");
                        while (salidaSky.getTamaño() != 0) {
                            System.out.println(inst.getName() + " mientras hay alumnos en lista.");
                            aux = (Esquiador) salidaSky.retirar();
                            aux.setEnClase(false);
                            salidaT.append(aux.getNombre() + " ");
                            System.out.println(aux.getNombre() + " termino su clase");
                        }
                        System.out.println(inst.getName() + " despierta a todos los esq");
                        salidaT.append(inst.getName() + ".\n");
                        this.notifyAll();
                    }

                }
            }
            synchronized (this) {
                cantInst++;
                System.out.println(inst.getName() + " aumenta contador inst a " + cantInst + " y se va");
                inst.setDaClaseSky(false);
                listaInst.ingresar(inst); //Lo ingreso al final de la cola
                salidaT.append(inst.getName() + " esta disponible.\n");
            }
        } catch (InterruptedException ex) {
            System.out.println("ERROR SEVERO en darClase " + Thread.currentThread().toString());
        }
        System.out.println(inst.getName() + " sale de darClase.");
    }

    public void llamarInstructorSky(Instructor inst) { //Despierta al instructor para dar la clase
        System.out.println(inst.getName() + " va a ser despertado.");
        synchronized (inst) {
            System.out.println(inst.getName() + " setea dar clase y despierta");
            inst.setDaClaseSky(true);
            inst.notify();
        }
        System.out.println(inst.getName() + " sale de llamarInstructor");
    }

    public synchronized void tomarClaseSnow(Esquiador esq) {
        try {
            boolean banderaSnow = false;
            if (complejo.estaAbierto()) {//Si esta abierto
                System.out.println(esq.getNombre() + " esta abierto, agrego el esquiador a la cola y al contAlumnosSnow");
                colaSnow.add(esq);
                contAlumnosSnow++;
                salidaT.append(esq.getNombre() + " quiere tomar una clase de Snow.\n");
                if (contAlumnosSnow >= 4) {//Si hay 4 o mas alumnos esperando
                    System.out.println(esq.getNombre() + " hay 4 o mas alumnos.");
                    if ((contAlumnosSnow % 4) == 0) {//Si es el 4to alumno
                        System.out.println(esq.getNombre() + " es el 4to.");
                        if (cantInst > 0) {//Si hay instructor disponible
                            salidaT.append(esq.getNombre() + " espera la clase de Snow.\n");
                            System.out.println(esq.getNombre() + " hay 1 inst disponible");
                            Instructor aux = (Instructor) listaInst.retirar(); //Recupera y saca del frente
                            cantInst--;
                            System.out.println(esq.getNombre() + " resto 1 inst, quedan: " + cantInst + " y llamo al instructor.");
                            llamarInstructorSnow(aux);
                            System.out.println(esq.getNombre() + " espera al inst");
                            while (!esq.getEnClase()) {
                                this.wait();
                            }
                            System.out.println(esq.getNombre() + " despierta para tomar la clase en MONITOR");
                            while (esq.getEnClase()) { //Cuando entra en la clase
                                this.wait();
                            }
                        } else {//Si no hay Instructor espero
                            salidaT.append(esq.getNombre() + " espera que se libere un instructor.\n");
                            while (!seCumplioTiempoSnow && cantInst < 1) {
                                System.out.println(esq.getNombre() + " comprueba porq no hay instructor.");
                                this.wait(500);
                            }
                            if (seCumplioTiempoSnow) { //Si se cumplió el tiempo limite
                                salidaT.append(esq.getNombre() + " se va porque se cumplio el tiempo de espera.\n");
                                System.out.println(esq.getNombre() + "  se cumplio tiempo, no pudo tomar la clase entonces borra de cola y contAlumnosSnow");
                                colaSnow.remove(esq);
                                contAlumnosSnow--;
                            } else { //Salió del while porque hay un instructor disponible
                                salidaT.append(esq.getNombre() + " espera la clase de Snow.\n");
                                System.out.println(esq.getNombre() + " hay 1 inst disponible");
                                Instructor aux = (Instructor) listaInst.retirar(); //Recupera y saca del frente
                                cantInst--;
                                System.out.println(esq.getNombre() + " resto 1 inst, quedan: " + cantInst + " y llamo al instructor.");
                                llamarInstructorSnow(aux);
                                while (!esq.getEnClase()) {
                                    this.wait();
                                }
                                System.out.println(esq.getNombre() + " despierta para tomar la clase en MONITOR");
                                while (esq.getEnClase()) { //Cuando entra en la clase
                                    this.wait();
                                }
                            }
                        }
                    } else { //Si no es el 4to quiere decir que ya hay un grupo formado 
                        if (cantInst > 0) { //Si hay instructores disponibles espera
                            salidaT.append(esq.getNombre() + " espera la clase de Snow.\n");
                            long j = horaLimiteSnow - System.currentTimeMillis();
                            while (!esq.getEnClase() && !seCumplioTiempoSnow) {
                                System.out.println(esq.getNombre() + " espera por entrar a clase o se cumpla tiempo");
                                this.wait(j);//A lo sumo espera j milisegundos
                                if (esq.getEnClase()) {
                                    System.out.println(esq.getNombre() + " va a tomar la clase");
                                    banderaSnow = true;
                                }
                            }
                            if (!banderaSnow) { //Si el esquiador no pudo tomar su clase
                                salidaT.append(esq.getNombre() + " se va porque no se formo grupo.\n");
                                System.out.println(esq.getNombre() + " no pudo tomar la clase entonces borra de cola y contAlumnosSnow");
                                colaSnow.remove(esq);
                                contAlumnosSnow--;
                                if (complejo.estaAbierto()) {
                                    System.out.println(esq.getNombre() + " no pudo tomar clase por falta de grupo");
                                } else {
                                    System.out.println(esq.getNombre() + " no pudo tomar clase por el horario");
                                }
                            } else {
                                System.out.println(esq.getNombre() + " duerme porque va a tomar la clase EN MONITOR");
                                while (esq.getEnClase()) { //Cuando entra en la clase
                                    this.wait();
                                }
                            }
                        } else {//Si no hay instructores disponibles y hay un grupo esperando uno, se va
                            salidaT.append(esq.getNombre() + " se va porque no hay instructor.\n");
                            System.out.println(esq.getNombre() + " no pudo tomar la clase entonces borra de cola y contAlumnosSnow");
                            colaSnow.remove(esq);
                            contAlumnosSnow--;
                            System.out.println(esq.getNombre() + " no pudo tomar clase por falta de instructor y grupo");
                        }
                    }
                } else {//Si hay menos de 4
                    if (contAlumnosSnow == 1) {//Si es el primero
                        salidaT.append(esq.getNombre() + " espera la clase de Snow.\n");
                        horaLimiteSnow = System.currentTimeMillis() + 20000; //Se asigna horario limite de espera
                        seCumplioTiempoSnow = false; //Se setea como falso el boolean
                        while (!esq.getEnClase() && !seCumplioTiempoSnow) { //Mientras no se arme el grupo y no se cumpla el tiempo límite, esperan
                            System.out.println(esq.getNombre() + " comprueba.");
                            this.wait(500); //El primer esquiador se despierta cada 500 milisegundos ya que se encarga de setear la variable seCumplioTiempoSnow
                            if (horaLimiteSnow < System.currentTimeMillis()) { //Cuando se despierta comprueba si se cumplió el tiempo límite
                                System.out.println(esq.getNombre() + " detecto que se cumplió el tiempo de espera.");
                                seCumplioTiempoSnow = true; //Si se cumplió el tiempo se cambia el valor de la variable booleana seCumplioTiempoSnow
                            }
                        }
                        if (esq.getEnClase()) {
                            System.out.println(esq.getNombre() + " va a tomar la clase");
                            banderaSnow = true;
                        }
                        if (seCumplioTiempoSnow) {//Si se cumplio el tiempo notifica
                            notifyAll();
                        }

                    } else {
                        salidaT.append(esq.getNombre() + " espera la clase de Snow.\n");
                        long k = horaLimiteSnow - System.currentTimeMillis();
                        while (!esq.getEnClase() && !seCumplioTiempoSnow) {
                            System.out.println(esq.getNombre() + " espera por entrar a clase o se cumpla tiempo");
                            this.wait(k);//A lo sumo espera k milisegundos
                            if (esq.getEnClase()) {
                                System.out.println(esq.getNombre() + " va a tomar la clase");
                                banderaSnow = true;
                            }
                        }
                    }
                    if (!banderaSnow) { //Si el esquiador no pudo tomar su clase
                        salidaT.append(esq.getNombre() + " se va porque no se formo grupo.\n");
                        System.out.println(esq.getNombre() + " no pudo tomar la clase entonces borra de cola y contAlumnosSnow");
                        colaSnow.remove(esq);
                        contAlumnosSnow--;
                        if (complejo.estaAbierto()) {
                            System.out.println(esq.getNombre() + " no pudo tomar clase por falta de grupo-----------------------------------------------");
                        } else {
                            System.out.println(esq.getNombre() + " no pudo tomar clase por el horario");
                        }
                    } else {
                        System.out.println(esq.getNombre() + " duerme porque va a tomar la clase EN MONITOR");
                        while (esq.getEnClase()) { //Cuando entra en la clase
                            this.wait();
                        }
                    }
                }
            }//Si cerró el complejo
            System.out.println(esq.getNombre() + " sale de tomarClaseSnow.");
        } catch (InterruptedException ex) {
            System.out.println("ERROR SEVERO en tomarClaseSnow " + Thread.currentThread().toString());
        }
    }

    public void darClaseSnow(Instructor inst) {
        try {
            boolean banderaSnow;
            System.out.println(inst.getName() + " va a dar clase en monitor.");
            if (contAlumnosSnow >= 4 && complejo.estaAbierto()) {
                System.out.println(inst.getName() + " mientras hay alumnos y esta abierto");
                ListaEspera salidaSnow = new ListaEspera();
                Esquiador aux;
                banderaSnow = false;
                synchronized (this) {
                    if (contAlumnosSnow >= 4) {
                        System.out.println(inst.getName() + " si hay mas de 4 alumnos.");
                        contAlumnosSnow = contAlumnosSnow - 4;
                        salidaT.append(inst.getName() + " da clase a: ");
                        for (int i = 1; i < 5; i++) {
                            aux = (Esquiador) colaSnow.take();
                            aux.setEnClase(true);
                            System.out.println(aux.getNombre() + " va a tomar la clase en monitor.");
                            salidaSnow.ingresar(aux);
                            salidaT.append(aux.getNombre() + " ");
                        }
                        salidaT.append(".\n");
                        notifyAll();//Despierta a todos los esquiadores
                        banderaSnow = true;
                    }
                }
                if (banderaSnow) {
                    System.out.println(inst.getName() + " esta dando la clase.");
                    Thread.sleep(2000);//Simula duración de la clase
                    synchronized (this) {
                        salidaT.append(inst.getName() + " termina la clase de: ");
                        while (salidaSnow.getTamaño() != 0) {
                            System.out.println(inst.getName() + " mientras hay alumnos en lista.");
                            aux = (Esquiador) salidaSnow.retirar();
                            aux.setEnClase(false);
                            salidaT.append(aux.getNombre() + " ");
                            System.out.println(aux.getNombre() + " termino su clase");
                        }
                        System.out.println(inst.getName() + " despierta a todos los esq");
                        salidaT.append(inst.getName() + ".\n");
                        this.notifyAll();
                    }

                }
            }
            synchronized (this) {
                cantInst++;
                System.out.println(inst.getName() + " aumenta contador inst a " + cantInst + " y se va");
                inst.setDaClaseSnow(false);
                listaInst.ingresar(inst); //Lo ingreso al final de la cola
                salidaT.append(inst.getName() + " esta disponible.\n");
            }
        } catch (InterruptedException ex) {
            System.out.println("ERROR SEVERO en darClase " + Thread.currentThread().toString());
        }
        System.out.println(inst.getName() + " sale de darClase.");
    }

    public void llamarInstructorSnow(Instructor inst) { //Despierta al instructor para dar la clase
        System.out.println(inst.getName() + " va a ser despertado.");
        synchronized (inst) {
            System.out.println(inst.getName() + " setea dar clase y despierta");
            inst.setDaClaseSnow(true);
            inst.notify();
        }
        System.out.println(inst.getName() + " sale de llamarInstructor");
    }

    public void cerrar() {
        synchronized (this) {
            this.notifyAll();  //en el caso de que haya quedado algun esquiador esperando y se haya cumplido la hora
        }
        while (listaInst.getTamaño() > 0) {//Despierta a los instructores para que se retiren
            Instructor aux = (Instructor) listaInst.retirar();
            aux.setDaClaseSky(false);
            aux.setDaClaseSnow(false);
            synchronized (aux) {
                aux.notify();
            }
        }

    }

}
