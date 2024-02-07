package virus.game.modelos;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import virus.game.modelos.cartas.Medicina;
import virus.game.modelos.cartas.Organo;
import virus.game.modelos.cartas.Tratamiento;
import virus.game.modelos.cartas.Virus;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IModelo extends IObservableRemoto {
    void reiniciarJuego() throws RemoteException;

    void setRevancha(int idJugador) throws RemoteException;

    boolean seJuegaRevancha() throws RemoteException;

    void iniciarJuego() throws RemoteException;

    boolean realizarAccionDeCarta(int idJugador, int numCarta) throws RemoteException;

    boolean jugarCarta(Jugador jugador, Tratamiento tratamiento) throws RemoteException;

    boolean jugarCarta(Jugador jugador, Organo organo) throws RemoteException;

    /* Intenta aplicar una infeccion
     * Devuelve falso si es inmune o no existe el organo */
    boolean jugarCarta(Jugador jugador, Virus virus) throws RemoteException;

    /* Busca un organo compatible para curar
     * Se fija si el organo no es inmune
     * En ese caso, lo cura. Devuelve true.
     * Si ya es inmune, no lo cura. Devuelve falso.
     * Si no existe, devuelve falso. */
    boolean jugarCarta(Jugador jugador, Medicina medicina) throws RemoteException;

    /* Elimina el organo del cuerpo y lo coloca en el mazo de descartes
     * Reinicia su estado
     * Coloca sus medicinas e infecciones en el mazo de descartes
     * */
    void extirparOrganoDelCuerpo(Jugador jugador, Organo organo) throws RemoteException;


    /* Retorna el jugador rival, retorna nulo si no lo encuentra (controlar este caso) */
    Jugador getRival(int idJugador) throws RemoteException;

    /* Agrego un nuevo jugador */
    /*
    void agregarJugador(Jugador jugador) throws RemoteException;
*/

    /* Le da 3 cartas al jugador, se usa cuando se inicia un nuevo juego */

    void dar3CartasJugador(Jugador jugador) throws RemoteException;

    ArrayList<Carta> getManoJugador(int idJugador) throws RemoteException;

    void agregarCartaAMazoDeDescartes(Carta carta) throws RemoteException;

    int agregarJugador(String nombre) throws RemoteException;

    void descartarCartaManoJugador(int idJugador, int[] indicesDeCartas) throws RemoteException;

    /* Metodo que permite intecambiar el mazo de descartes con el mazo principal
     *  Cuando el mazo está vacío */
    void intercambiarMazos() throws RemoteException;

    void dar1CartaJugador(Jugador jugador) throws RemoteException;

    void darCartasFaltantesJugador(Jugador jugador, int cantidad) throws RemoteException;


    /* Para terminar el turno de un jugador y dárselo al siguiente */
    Jugador cambiarTurnoJugador(boolean accionadoPorCarta) throws RemoteException;

    // Controlar si algun jugador está en condición de ser ganador
    void controlarGanador() throws RemoteException;

    boolean seCargoLaPartida()  throws RemoteException;

    boolean cargarPartida() throws RemoteException;

    void guardarPartida() throws RemoteException;

    int cantidadDeCartasEnMazo() throws RemoteException;

    int cantidadDeCartasEnMazoDeDescartes() throws RemoteException;

    Mazo getMazoDeDescarte() throws RemoteException;

    Mazo getMazo() throws RemoteException;

    Jugador getGanador() throws RemoteException;

    Jugador getTurnoJugador() throws RemoteException;

    // Solucion al problema del alising con RMI
    Jugador getJugadorPorId(int idJugador) throws RemoteException;

    ArrayList<Jugador> getJugadores() throws RemoteException;
}
