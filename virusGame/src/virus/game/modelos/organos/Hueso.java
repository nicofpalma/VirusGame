package virus.game.modelos.organos;

import virus.game.modelos.Color;

import java.io.Serializable;

public class Hueso extends Organo implements Serializable {
    // Constructor de Hueso
    public Hueso(){
        // Los huesos son siempre amarillos
        super(Color.AMARILLO, "Hueso");
    }
}
