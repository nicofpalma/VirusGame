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
    public Mazo(boolean vacio){
        cartas = new ArrayList<>();
        if(!vacio) {
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

    public void mezclarMazo(){
        // Mezclo el mazo utilizando la clase Collections, que tiene un metodo shuffle que permite randomizar las posiciones
        Collections.shuffle(cartas);
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public Carta[] dar3Cartas(){
        Carta[] cartasJugador = new Carta[3];

        // Verifico que haya 3 cartas en el mazo
        if(cartas.size() >= 3){
            for (int i = 0; i < 3; i++) {
                // Tomo las ultimas cartas del mazo, es decir, las que están en la parte superior.
                cartasJugador[i] = cartas.get(cartas.size() - i - 1);
            }
        }

        // Elimino esas cartas tomadas del mazo
        for (int i = 0; i < 3; i++) {
            cartas.remove(cartas.size() - 1);
        }
        return cartasJugador;
    }

    /* Metodo para dar 1 carta */
    public Carta dar1Carta(){
        Carta cartaParaDar = null;

        // Agregar la comprobacion para llenar el mazo en caso de que se vacíe.
        if(cartas.size() >= 1){
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

    public void verMazo(){
        // Recorro el mazo y lo veo, solo para fines de desarrollo
        for (int i = 0; i < cartas.size(); i++) {
            System.out.println(i + ": " + cartas.get(i).getClass().getSimpleName());
        }
    }

    public int cantidadDeCartasEnMazo(){
        return cartas.size();
    }

    public void agregarCarta(Carta carta){
        cartas.add(carta);
    }
}
