package virus.game.modelos.medicinas;

import virus.game.modelos.Color;

public class MedicinaHueso extends Medicina{

    // Constructo de la medicina del hueso
    public MedicinaHueso(){
        // El color de la medicina del hueso siempre es amarillo
        super(Color.AMARILLO, "/view/img/medicinaHueso.png");
    }
}
