package virus.game.modelos.cartas;

import virus.game.modelos.Carta;
import virus.game.modelos.Color;

import java.io.Serial;
import java.io.Serializable;

public final class Medicina extends Carta implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Constructor de la medicina
    public Medicina(Color color, String nombre, String nombreImagen, String descripcion){
        super(color, nombre, nombreImagen, descripcion);
    }

}
