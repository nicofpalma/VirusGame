package virus.game.modelos;

import virus.game.modelos.cartas.Medicina;
import virus.game.modelos.cartas.Organo;
import virus.game.modelos.cartas.Tratamiento;
import virus.game.modelos.cartas.Virus;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // Lista de cartas que guarda el mazo
    private ArrayList<Carta> cartas;

    // Constructor del mazo
    public Mazo(boolean vacio) {
        cartas = new ArrayList<>();
        if (!vacio) {
            // Los 20 organos
            insertarCartasDeOrganos();

            // Las 16 medicinas
            insertarCartasDeMedicinas();

            // Los 16 virus
            insertarCartasDeVirus();

            // Cartas de tratamientos (n)
            insertarCartasDeTratamientos();

            // Mezclar el mazo
            mezclarMazo();
        }
    }

    public void mezclarMazo() {
        // Mezclo el mazo utilizando la clase Collections, que tiene un metodo shuffle que permite randomizar las posiciones
        Collections.shuffle(cartas);
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    /* Metodo para dar 1 carta */
    public Carta dar1Carta(){
        Carta cartaParaDar = null;

        // Agregar la comprobacion para llenar el mazo en caso de que se vacíe.
        if(!cartas.isEmpty()){
            cartaParaDar = cartas.get(cartas.size() - 1);
            cartas.remove(cartas.size() - 1);
        }
        return cartaParaDar;
    }


    private void insertarCartasDeTratamientos(){
        for (int i = 0; i < 3; i++) {
            // Error médico
            if(i == 0){
                cartas.add(new Tratamiento(
                        Color.INCOLORO, "Error médico", "errorMedico.png",
                        1, "Intercambia tu cuerpo con el cuerpo del rival."));

                cartas.add(new Tratamiento(
                        Color.INCOLORO, "Guante de látex", "guanteDeLatex.png",
                        3, "Descarta la mano del rival y le hace perder un turno. Puedes realizar otra acción este turno."
                ));
            }

            // Contagio
            if(i < 2) cartas.add(new Tratamiento(Color.INCOLORO,
                    "Contagio", "contagio.png",
                    2, "Envía todos las infecciones que se puedan, a los órganos del rival."));

        }
    }

    private void insertarCartasDeVirus(){
        // 4 cartas de cada virus
        for (int i = 0; i < 4; i++) {
            cartas.add(new Virus(Color.ROJO, "Virus del corazón", "virusCorazon.png", "Infecta al corazón del rival."));
            cartas.add(new Virus(Color.VERDE, "Virus del estómago", "virusEstomago.png", "Infecta al estómago del rival."));
            cartas.add(new Virus(Color.AZUL, "Virus del cerebro", "virusCerebro.png", "Infecta al cerebro del rival"));
            cartas.add(new Virus(Color.AMARILLO, "Virus del hueso", "virusHueso.png", "Infecta al hueso del rival"));
        }
    }

    private void insertarCartasDeMedicinas(){
        // 4 cartas de cada medicina
        for (int i = 0; i < 4; i++) {
            cartas.add(new Medicina(Color.ROJO, "Medicina del corazón", "medicinaCorazon.png", "Medicina para tu corazón"));
            cartas.add(new Medicina(Color.VERDE, "Medicina del estómago", "medicinaEstomago.png", "Medicina para tu estómago"));
            cartas.add(new Medicina(Color.AZUL, "Medicina del cerebro", "medicinaCerebro.png", "Medicina para tu cerebro"));
            cartas.add(new Medicina(Color.AMARILLO, "Medicina del hueso", "medicinaHueso.png", "Medicina para tu hueso"));
        }
    }

    private void insertarCartasDeOrganos(){
        // 5 cartas de cada organo
        for (int i = 0; i < 5; i++) {
            cartas.add(new Organo(Color.AZUL, "Cerebro", "cerebro.png", "Órgano para el cuerpo"));
            cartas.add(new Organo(Color.VERDE, "Estómago", "estomago.png", "Órgano para el cuerpo"));
            cartas.add(new Organo(Color.AMARILLO, "Hueso", "hueso.png", "Órgano para el cuerpo"));
            cartas.add(new Organo(Color.ROJO, "Corazón", "corazon.png", "Órgano para el cuerpo"));
        }
    }

    /* Metodo que llena el mazo a partir de otro
    * Comunmente usado cuando el mazo está vacío, se le envía el de descartes.*/
    public void llenarMazo(Mazo mazo){
        this.cartas.addAll(mazo.cartas); // Uso addAll de collection que hace lo mismo
    }

    /* Vacia el mazo por completo */
    public void vaciarMazo(){
        cartas.clear();
    }

    public void agregarCarta(Carta carta){
        cartas.add(carta);
    }

    public void eliminarCarta(Carta carta){
        cartas.remove(carta);
    }

    public boolean estaVacio(){
        return cartas.isEmpty();
    }
}
