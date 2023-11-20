package virus.game.modelos.medicinas;

import virus.game.modelos.Color;

import java.io.Serializable;

public class MedicinaHueso extends Medicina implements Serializable {

    // Constructo de la medicina del hueso
    public MedicinaHueso(){
        // El color de la medicina del hueso siempre es amarillo
        super(Color.AMARILLO, "Medicina del hueso");
    }
}
