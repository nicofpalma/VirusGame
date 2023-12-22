package virus.game.modelos;

import javax.swing.*;
import java.awt.*;

public abstract class Carta {
    // Color de la carta
    private final Color color;

    // Nombre de la carta
    private final String nombre;

    private final ImageIcon imagen;

    // Constructor de la carta
    public Carta(Color color, String nombre, String rutaImagen){
        this.color = color;
        this.nombre = nombre;
        this.imagen = cargarImagen(rutaImagen);
    }

    // Se encarga de buscar la imágen para cargarla, cuando se le pasa la ruta en el constructor.
    public ImageIcon cargarImagen(String rutaImagen){
        Image imagen = new ImageIcon(rutaImagen).getImage();
        return new ImageIcon(imagen);
    }

    public ImageIcon getImagen() {
        return imagen;
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
