package model;

import model.medicinas.MedicinaCerebro;
import model.medicinas.MedicinaCorazon;
import model.medicinas.MedicinaEstomago;
import model.medicinas.MedicinaHueso;
import model.organos.Cerebro;
import model.organos.Corazon;
import model.organos.Estomago;
import model.organos.Hueso;
import model.virus.VirusCerebro;
import model.virus.VirusCorazon;
import model.virus.VirusEstomago;
import model.virus.VirusHueso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
    // Lista de cartas que guarda el mazo
    private List<Carta> cartas;

    // Constructor del mazo
    public Mazo(){
        cartas = new ArrayList<>();
        // Los 20 organos
        insertarCartasDeOrganos();

        // Las 16 medicinas
        insertarCartasDeMedicinas();

        // Los 16 virus
        insertarCartasDeVirus();

        // Mezclar el mazo
        mezclarMazo();
    }

    public void mezclarMazo(){
        // Mezclo el mazo utilizando la clase Collections, que tiene un metodo shuffle que permite randomizar las posiciones
        Collections.shuffle(cartas);
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
}
