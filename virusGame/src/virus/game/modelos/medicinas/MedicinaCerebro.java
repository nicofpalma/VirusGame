package virus.game.modelos.medicinas;

import virus.game.modelos.Color;

import java.io.Serializable;

public class MedicinaCerebro extends Medicina implements Serializable {

    // Constructor de la medicina del cerebro
    public MedicinaCerebro(){
        // La medicina del cerebro siempre es azul
        super(Color.AZUL, "Medicina del cerebro");
    }
}
