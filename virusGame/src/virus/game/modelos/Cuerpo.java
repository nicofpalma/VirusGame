package virus.game.modelos;

import virus.game.modelos.medicinas.Medicina;
import virus.game.modelos.organos.Organo;
import virus.game.modelos.virus.Virus;

import java.util.ArrayList;

public class Cuerpo {
    private ArrayList<Organo> cuerpo;

    public Cuerpo(){
        this.cuerpo = new ArrayList<Organo>();
    }

    public ArrayList<Organo> getCuerpo() {
        return cuerpo;
    }

    @Override
    public String toString() {
        String cuerpoString = "";
        for (int i = 0; i < cuerpo.size(); i++) {
            cuerpoString += cuerpo.get(i).toString() + " ";
        }
        return cuerpoString;
    }

    public int getCantidadDeOrganosEnElCuerpo(){
        return cuerpo.size();
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
        cuerpo.remove(organoExtirpado);
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
        for (int i = 0; i < cuerpo.size(); i++) {
            if(this.cuerpo.get(i).getColor().equals(color)){
                return this.cuerpo.get(i);
            }
        }
        return null;
    }

    /* Agrega una carta al cuerpo del jugador, verifica que el organo no exista */
    public boolean agregarOrganoAlCuerpo(Organo organo){
        if(cuerpo.size() < 4){ // Verificación que el cuerpo tenga menos de 4 cartas (es la condición de que el jugador gane)
            for (int i = 0; i < cuerpo.size(); i++) {
                // Verifica que el organo no exista en el cuerpo, lo comparo con el color.
                // Si existe, retorno falso y no lo agrego
                if(cuerpo.get(i).getColor().equals(organo.getColor())){
                    return false;
                }
            }
            cuerpo.add(organo);      // Si no existe, lo agrego.
        } else {
            return false;
        }
        return true;
    }
}
