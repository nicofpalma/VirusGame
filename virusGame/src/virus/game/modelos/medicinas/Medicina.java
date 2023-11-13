package virus.game.modelos.medicinas;

import virus.game.modelos.Carta;
import virus.game.modelos.Color;

public abstract class Medicina extends Carta {

    // Constructor de la medicina
    public Medicina(Color color, String rutaImagen){
        super(color, rutaImagen);
    }

}
