package model;

public abstract class Carta {
    // Color de la carta
    private final Color color;

    // Constructor de la carta
    public Carta(Color color){
        this.color = color;
    }

    // Getter del color
    public Color getColor() {
        return color;
    }
}
