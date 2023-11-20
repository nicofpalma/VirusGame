package virus.game.modelos.medicinas;

import virus.game.modelos.Color;

import java.io.Serializable;

public class MedicinaCorazon extends Medicina implements Serializable {

    // Constructo de la medicina del corazon
    public MedicinaCorazon(){
        // Las medicinas del corazon siempre son rojas
        super(Color.ROJO, "Medicina del corazón");
    }
}
