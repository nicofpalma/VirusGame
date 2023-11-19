package virus.game.modelos;

import javax.swing.*;
import java.awt.*;

public abstract class Carta {
    // Color de la carta
    private final Color color;

    // Nombre de la carta
    private final String nombre;

    // Constructor de la carta
    public Carta(Color color, String nombre){
        this.color = color;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "[" + this.getNombre() + "]";
    }

    // Getter del color
    public Color getColor() {
        return color;
    }

    public String getNombre() {
        return nombre;
    }
}
