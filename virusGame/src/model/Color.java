package model;

// Colores de las cartas
public enum Color {
    ROJO("Rojo"), VERDE("Verde"), AZUL("Azul"), AMARILLO("Amarillo"), MULTICOLOR("Multicolor");

    // Campo del nombre del color
    private final String nombre;

    // Constructor del color
    Color(String nombre) {
        this.nombre = nombre;
    }

    // getter del nombre
    public String getNombre() {
        return nombre;
    }
}
