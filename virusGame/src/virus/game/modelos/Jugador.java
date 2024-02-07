package virus.game.modelos;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String nombre;
    private static int cantidadJugadores;
    private int numeroDeJugador;
    private ArrayList<Carta> mano;
    private Cuerpo cuerpoJugador;
    public Jugador(String nombre){
        this.nombre = nombre;
        ++Jugador.cantidadJugadores;
        numeroDeJugador = cantidadJugadores;
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

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public void setCuerpoJugador(Cuerpo nuevoCuerpoJugador){
        this.cuerpoJugador = nuevoCuerpoJugador;
    }

    // Reinicia los atributos del jugador
    public void reiniciarJugador(){
        mano = new ArrayList<Carta>(3);
        cuerpoJugador = new Cuerpo();

    }


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

    public void eliminarCartaDeLaMano(Carta carta){
        this.mano.remove(carta);
    }

    public void setMano(ArrayList<Carta> mano) {
        this.mano = mano;
    }

    public Cuerpo getCuerpoJugador() {
        return cuerpoJugador;
    }

}
