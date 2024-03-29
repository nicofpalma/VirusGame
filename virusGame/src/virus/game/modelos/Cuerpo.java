package virus.game.modelos;

import virus.game.modelos.cartas.Medicina;
import virus.game.modelos.cartas.Organo;
import virus.game.modelos.cartas.Virus;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Cuerpo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private ArrayList<Organo> organos;

    public Cuerpo(){
        this.organos = new ArrayList<Organo>();
    }

    public ArrayList<Organo> getOrganos() {
        return organos;
    }

    @Override
    public String toString() {
        String cuerpoString = "";
        for (int i = 0; i < organos.size(); i++) {
            cuerpoString += organos.get(i).toString() + "      ";
        }
        return cuerpoString;
    }

    /*
    *  Intenta infectar un organo buscado por el color.
    *  Devuelve el organo cuando lo infecta.
    *  Tambien lo devuelve si no lo pudo infectar porque es inmune.
    *  Retorna null si el organo a infectar no existe.
    * */
    public Organo infectarOrgano(Virus virus){
        Organo organoAfectado;
        organoAfectado = encontrarOrgano(virus.getColor());
        if(organoAfectado != null){
            organoAfectado.agregarInfeccion(virus);
            return organoAfectado;
        }
        return null; // Devuelve null si no existe en el cuerpo
    }

    public Organo extirparOrgano(Organo organo){
        Organo organoExtirpado = encontrarOrgano(organo.getColor());
        organos.remove(organoExtirpado);
        return organoExtirpado;
    }

    public void eliminarInfeccionYMedicina(Organo organo){
        Organo organoAfectado = encontrarOrgano(organo.getColor());
        organoAfectado.eliminarMedicinas();
        organoAfectado.eliminarInfecciones();
    }

    public void eliminarMedicinas(Organo organo){
        Organo organoAEliminarMedicinas = encontrarOrgano(organo.getColor());
        organoAEliminarMedicinas.eliminarMedicinas();
    }


    public Organo curarOrgano(Medicina medicina){
        Organo organoCurado;
        organoCurado = encontrarOrgano(medicina.getColor());
        if(organoCurado != null){
            // Devuelve el organo curado (al que se le aplico la medicina)
            // Tambien lo devuelve si no lo pudo curar porque ya es inmune (esto no pasaría nunca).
            organoCurado.agregarMedicina(medicina);
            return organoCurado;
        }
        return null; // Devuelve null si no existe en el cuerpo
    }

    /* Encuentra un organo a través de un color.
    * Los colores de los organos coinciden con los colores de la vacuna o virus de dicho organo
    */
    public Organo encontrarOrgano(Color color){
        for (int i = 0; i < organos.size(); i++) {
            if(this.organos.get(i).getColor().equals(color)){
                return this.organos.get(i);
            }
        }
        return null;
    }

    /* Agrega una carta al cuerpo del jugador, verifica que el organo no exista */
    public boolean agregarOrganoAlCuerpo(Organo organo){
        if(organos.size() < 4){ // Verificación que el cuerpo tenga menos de 4 cartas (es la condición de que el jugador gane)
            for (int i = 0; i < organos.size(); i++) {
                // Verifica que el organo no exista en el cuerpo, lo comparo con el color.
                // Si existe, retorno falso y no lo agrego
                if(organos.get(i).getColor().equals(organo.getColor())){
                    return false;
                }
            }
            organos.add(organo);      // Si no existe, lo agrego.
        } else {
            return false;
        }
        return true;
    }
}
