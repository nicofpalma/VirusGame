package virus.game.modelos.cartas;

import virus.game.modelos.Carta;
import virus.game.modelos.Color;
import java.io.Serial;
import java.io.Serializable;

public class Tratamiento extends Carta implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int id;   // El id es para identificar el tipo de tratamiento en el modelo
    public Tratamiento(Color color, String nombre, String nombreImagen, int id, String descripcion) {
        super(color, nombre, nombreImagen, descripcion);
        this.id = id;
    }
    public int getId() {
        return id;
    }

    @Override
    public String toString(){
        return "Tratamiento: " + super.toString();
    }
}
