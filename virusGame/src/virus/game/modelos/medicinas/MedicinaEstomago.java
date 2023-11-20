package virus.game.modelos.medicinas;

import virus.game.modelos.Color;

import java.io.Serializable;

public class MedicinaEstomago extends Medicina implements Serializable {

    // Constructor de la medicina del estomago
    public MedicinaEstomago(){
        // La medicina del estomago es siempre verde
        super(Color.VERDE, "Medicina del estómago");
    }
}
