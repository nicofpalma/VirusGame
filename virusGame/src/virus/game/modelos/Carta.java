package virus.game.modelos;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.Serial;
import java.io.Serializable;

public abstract class Carta implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    // Color de la carta
    private final Color color;

    // Nombre de la carta
    private final String nombre;

    private final ImageIcon imagen;

    // Constructor de la carta
    public Carta(Color color, String nombre, String nombreImagen){
        this.color = color;
        this.nombre = nombre;
        this.imagen = cargarImagen(nombreImagen);
    }

    // Se encarga de buscar la imágen para cargarla, cuando se le pasa la ruta en el constructor.
    public ImageIcon cargarImagen(String nombreImagen){
        int ancho = 100;
        int alto = 140;
        Image imagen = new ImageIcon("./src/virus/game/modelos/cartas/img/" + nombreImagen).getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
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
