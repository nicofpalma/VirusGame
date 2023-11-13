package virus.game.modelos.virus;

import virus.game.modelos.Carta;
import virus.game.modelos.Color;

public abstract class Virus extends Carta {
    // Constructor del virus
    public Virus(Color color, String rutaImagen){
        super(color, rutaImagen);
    }
}
