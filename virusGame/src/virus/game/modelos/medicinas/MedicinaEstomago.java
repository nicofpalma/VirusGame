package virus.game.modelos.medicinas;

import virus.game.modelos.Color;

public class MedicinaEstomago extends Medicina{

    // Constructor de la medicina del estomago
    public MedicinaEstomago(){
        // La medicina del estomago es siempre verde
        super(Color.VERDE, "/view/img/medicinaEstomago.png");
    }
}
