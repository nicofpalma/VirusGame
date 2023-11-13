package virus.game.modelos;

import javax.swing.*;
import java.awt.*;

public abstract class Carta {
    // Color de la carta
    private final Color color;
    private Image imagen;

    // Constructor de la carta
    public Carta(Color color, String rutaImagen){
        this.color = color;
        //cargarImagen(rutaImagen);
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getSimpleName() + "]";
    }

    private void cargarImagen(String rutaImagen){
        imagen = new ImageIcon(getClass().getResource(rutaImagen)).getImage();
    }

    public Image getImagen() {
        return imagen;
    }

    // Getter del color
    public Color getColor() {
        return color;
    }
}
