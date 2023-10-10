package model.organos;

import model.Carta;
import model.Color;
import model.medicinas.Medicina;
import model.virus.Virus;

public abstract class Organo extends Carta{
    // Cuando está infectado con solo 1 virus
    private Virus infeccion;

    // Cuando está vacunado con solo una medicina
    private Medicina vacuna;

    // Cuando ya se le aplicó dos medicinas, se vuelve inmune.
    private boolean inmune;

    // Constructor que setea solo el color, el resto lo deja nulo.
    public Organo(Color color){
        super(color);
        this.infeccion = null;
        this.vacuna = null;
        this.inmune = false;
    }

    // Getter vacuna
    public Medicina getVacuna() {
        return vacuna;
    }

    // Getter infeccion
    public Virus getInfeccion() {
        return infeccion;
    }

    // Setter infeccion
    public void setInfeccion(Virus infeccion) {
        this.infeccion = infeccion;
    }

    // Setter vacuna
    public void setVacuna(Medicina vacuna) {
        this.vacuna = vacuna;
    }
}
