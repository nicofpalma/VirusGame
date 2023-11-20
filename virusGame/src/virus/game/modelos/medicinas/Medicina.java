package virus.game.modelos.medicinas;

import virus.game.modelos.Carta;
import virus.game.modelos.Color;

import java.io.Serializable;

public abstract class Medicina extends Carta implements Serializable {

    // Constructor de la medicina
    public Medicina(Color color, String nombre){
        super(color, nombre);
    }

}
