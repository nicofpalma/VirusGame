package virus.game.modelos;

import virus.game.modelos.medicinas.MedicinaCerebro;
import virus.game.modelos.medicinas.MedicinaCorazon;
import virus.game.modelos.medicinas.MedicinaEstomago;
import virus.game.modelos.medicinas.MedicinaHueso;
import virus.game.modelos.organos.Cerebro;
import virus.game.modelos.organos.Corazon;
import virus.game.modelos.organos.Estomago;
import virus.game.modelos.organos.Hueso;
import virus.game.modelos.virus.VirusCerebro;
import virus.game.modelos.virus.VirusCorazon;
import virus.game.modelos.virus.VirusEstomago;
import virus.game.modelos.virus.VirusHueso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
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

    private void insertarCartasDeVirus(){
        // 4 cartas de cada virus
        for (int i = 0; i < 4; i++) {
            cartas.add(new VirusCorazon());
            cartas.add(new VirusEstomago());
            cartas.add(new VirusCerebro());
            cartas.add(new VirusHueso());
        }
    }

    private void insertarCartasDeMedicinas(){
        // 4 cartas de cada medicina
        for (int i = 0; i < 4; i++) {
            cartas.add(new MedicinaCorazon());
            cartas.add(new MedicinaEstomago());
            cartas.add(new MedicinaCerebro());
            cartas.add(new MedicinaHueso());
        }
    }

    private void insertarCartasDeOrganos(){
        // 5 cartas de cada organo
        for (int i = 0; i < 5; i++) {
            cartas.add(new Cerebro());
            cartas.add(new Estomago());
            cartas.add(new Hueso());
            cartas.add(new Corazon());
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

    public int cantidadDeCartasEnMazo(){
        return cartas.size();
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
