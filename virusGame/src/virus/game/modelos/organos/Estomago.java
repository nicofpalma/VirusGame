package virus.game.modelos.organos;

import virus.game.modelos.Color;

import java.io.Serializable;

public class Estomago extends Organo implements Serializable {

    // Constructor de estomago
    public Estomago(){
        // Los estomagos siempre son verdes
        super(Color.VERDE, "Estómago");
    }
}
