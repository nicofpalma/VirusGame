package virus.game.modelos.organos;

import virus.game.modelos.Color;

import java.io.Serializable;

public class Corazon extends Organo implements Serializable {

    // Constructor de corazon
    public Corazon(){
        // Los corazones siempre son rojos
        super(Color.ROJO, "Corazón");
    }
}
