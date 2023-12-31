package virus.game.modelos;

import virus.game.modelos.cartas.Organo;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Jugador implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String nombre;
    private static int cantidadJugadores;
    private int numeroDeJugador;
    private boolean ganador;
    private ArrayList<Carta> mano;
    private Cuerpo cuerpoJugador;
    public Jugador(String nombre){
        this.nombre = nombre;
        ++Jugador.cantidadJugadores;
        numeroDeJugador = cantidadJugadores;
        this.ganador = false;
        this.mano = new ArrayList<Carta>(3);
        this.cuerpoJugador = new Cuerpo();
    }
    public String getNombre() {
        return nombre;
    }
    public int getNumeroDeJugador() {return numeroDeJugador;}
    public static int getCantidadJugadores() {
        return cantidadJugadores;
    }
    public boolean esGanador() {
        return ganador;
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    /*

    // Override de "equals" para solucionar el problema de aliasing con RMI
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Jugador jugador = (Jugador) obj;

        return Objects.equals(nombre, jugador.nombre) &&
                Objects.equals(numeroDeJugador, jugador.numeroDeJugador);
    } */


    /* Retorna las cartas de la mano en un string concatenado */
    public String verCartasManoString(){
        String cartas = "";
        for (int i = 0; i < mano.size(); i++) {
            if(i == mano.size() - 1){
                cartas += (Integer.valueOf(i+1)) + ") " + mano.get(i).toString();
            } else {
                cartas += (Integer.valueOf(i+1)) + ") " + mano.get(i).toString() + "          ";
            }
        }
        return cartas;
    }

    /* Metodo para recibir nuevas cartas del mazo */
    public boolean recibirCarta(Carta nuevaCarta){
        if(mano.size() == 3){
            return false;
        } else{
            mano.add(nuevaCarta);
            return true;
        }
    }

    public void setGanador(){
        ganador = true;
    }

    public void eliminarCartaDeLaMano(Carta carta){
        this.mano.remove(carta);
    }

    public void setMano(ArrayList<Carta> mano) {
        this.mano = mano;
    }

    public Cuerpo getCuerpoJugador() {
        return cuerpoJugador;
    }

    /* Agrega un organo al cuerpo (mesa) del jugador. Retorna verdadero si lo pudo agregar, falso si no. */
    public boolean agregarOrganoAlCuerpo(Organo organo){
        return cuerpoJugador.agregarOrganoAlCuerpo(organo);
    }
}
