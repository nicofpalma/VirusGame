package virus.game.modelos.cartas;

import virus.game.modelos.Carta;
import virus.game.modelos.Color;

import java.util.ArrayList;

public final class Organo extends Carta{
    // Solo pueden guardar 2 infecciones o vacunas como máximo
    private ArrayList<Virus> infecciones;
    private ArrayList<Medicina> medicinas;

    // Cuando ya se le aplicó dos medicinas, se vuelve inmune.
    private boolean inmune;
    private boolean extirpado;

    // Constructor que setea solo el color, el resto lo deja nulo.
    public Organo(Color color, String nombre, String rutaImagen){
        super(color, nombre, rutaImagen);
        this.infecciones = new ArrayList<Virus>(2);
        this.medicinas = new ArrayList<Medicina>(2);
        this.inmune = false;
        this.extirpado = false;
    }

    @Override
    public String toString() {
        String nombreOrgano = "[" + this.getNombre();
        if(!infecciones.isEmpty()){
            nombreOrgano += " -INFECTADO";
        }
        if(!medicinas.isEmpty()){
            nombreOrgano += " -VACUNADO";
        }
        if(esInmune()){
            nombreOrgano += " -INMUNE";
        }
        nombreOrgano += "]";
        return nombreOrgano;
    }

    /* Reinicia sus atributos cuando se le coloca en el mazo de descartes */
    public void reiniciarOrgano(){
        infecciones = new ArrayList<Virus>();
        medicinas = new ArrayList<Medicina>();
        inmune = false;
        extirpado = false;
    }

    public void eliminarMedicinas(){
        this.medicinas = new ArrayList<Medicina>(2);
    }

    public void eliminarInfecciones(){
        this.infecciones = new ArrayList<Virus>(2);
    }

    public Medicina[] extraerMedicinas(){
        Medicina[] medicinasExtraidas = new Medicina[2];
        for (int i = 0; i < medicinas.size(); i++) {
            medicinasExtraidas[i] = medicinas.get(i);
        }
        return medicinasExtraidas;
    }

    public Virus[] extraerInfecciones(){
        Virus[] infeccionesExraidas = new Virus[2];
        for (int i = 0; i < infecciones.size(); i++) {
            infeccionesExraidas[i] = infecciones.get(i);
        }
        return infeccionesExraidas;
    }

    // Getter medicinas
    public ArrayList<Medicina> getMedicinas() {
        return medicinas;
    }

    // Getter infecciones
    public ArrayList<Virus> getInfecciones() {
        return infecciones;
    }

    // Elimina la infeccion y la medicina cuando tiene 1 infeccion y 1 medicina
    public void cancelarInfeccionYMedicina(){
        this.medicinas = new ArrayList<Medicina>(2);
        this.infecciones = new ArrayList<Virus>(2);
    }

    /* Agrega una infeccion si el organo no es inmune. Si tiene 2 infecciones, extirpado se pone en true */
    public void agregarInfeccion(Virus infeccion) {
        if(!inmune) {
            this.infecciones.add(infeccion);
            if (this.infecciones.size() >= 2) extirpado = true;
        }
    }

    /* Agrega una vacuna si el organo no es inmune. Si tiene 2 vacunas, se vuelve inmune. */
    public void agregarMedicina(Medicina medicina) {
        if(!inmune){
            this.medicinas.add(medicina);
            if(this.medicinas.size() >= 2) inmune = true;
        }
    }

    public boolean estaExtirpado() {
        return extirpado;
    }

    public boolean esInmune(){
        return inmune;
    }
}
