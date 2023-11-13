package virus.game.modelos;

import virus.game.modelos.medicinas.Medicina;
import virus.game.modelos.organos.Organo;
import virus.game.modelos.virus.Virus;
import virus.game.observer.IObservable;
import virus.game.observer.IObservador;

import java.util.ArrayList;

public class Juego implements IObservable {
    private Mazo mazo;
    private Mazo mazoDeDescarte;
    private ArrayList<Jugador> jugadores;
    private Jugador ganador;
    private Jugador turnoJugador;
    private ArrayList<IObservador> observadores;

    public Juego(){
        this.jugadores = new ArrayList<Jugador>();
        this.observadores = new ArrayList<IObservador>();
        this.mazo = new Mazo(false);
        this.mazoDeDescarte = new Mazo(true);
        this.ganador = null;
        this.turnoJugador = null;
    }

    public void iniciarJuego(){
        if(jugadores.size() >= 2) this.notificarCambio(AccionModelo.INICIAR_JUEGO);
    }

    public boolean jugarCarta(Jugador jugador, Organo organo){
        return jugador.getCuerpoJugador().agregarOrganoAlCuerpo(organo);
    }

    /* Intenta aplicar una infeccion
    * Devuelve falso si es inmune o no existe el organo */
    public boolean jugarCarta(Jugador jugador, Virus virus){
        Cuerpo cuerpoRival = getRival(jugador).getCuerpoJugador();
        Organo organoAfectado = cuerpoRival.infectarOrgano(virus);
        if(organoAfectado != null){
            if(organoAfectado.esInmune()){
                return false;
            }
        } else {
            return false;
        }

        // Elimina el organo del cuerpo si está para extirpar (con dos infecciones)
        if(organoAfectado.estaExtirpado()){
            extirparOrganoDelCuerpo(jugador, organoAfectado);
        }
        // Retorna verdadero si el organo fue infectado.
        return true;
    }

    public boolean jugarCarta(Jugador jugador, Medicina medicina){

        return false;
    }

    /* Elimina el organo del cuerpo y lo coloca en el mazo de descartes
     * Reinicia su estado
     * Coloca sus medicinas e infecciones en el mazo de descartes
     * */
    public void extirparOrganoDelCuerpo(Jugador jugador, Organo organo){
        Organo organoExtirpado = jugador.getCuerpoJugador().extirparOrgano(organo);
        Virus[] infeccionesExtraidas = organoExtirpado.extraerInfecciones();
        Medicina[] medicinasExtraidas = organoExtirpado.extraerMedicinas();
        for (int i = 0; i < infeccionesExtraidas.length; i++) {
            mazoDeDescarte.agregarCarta(medicinasExtraidas[i]);
        }
        for (int i = 0; i < infeccionesExtraidas.length; i++) {
            mazoDeDescarte.agregarCarta(infeccionesExtraidas[i]);
        }
        organoExtirpado.reiniciarOrgano();
        mazoDeDescarte.agregarCarta(organoExtirpado);
    }


    /* Retorna el jugador rival, retorna nulo si no lo encuentra (controlar este caso) */
    public Jugador getRival(Jugador jugador){
        Jugador jugadorRival = null;
        for (int i = 0; i < jugadores.size(); i++) {
            jugadorRival = jugadores.get(i);
            if(!jugadorRival.equals(jugador)){
                return jugadorRival;
            }
        }
        return jugadorRival;
    }

    /* Agrego un nuevo jugador */
    public void agregarJugador(Jugador jugador){
        dar3CartasJugador(jugador);
        jugadores.add(jugador);
    }

    /* Le da 3 cartas al jugador, se usa cuando se inicia un nuevo juego */
    private void dar3CartasJugador(Jugador jugador){
        for (int i = 0; i < 3; i++) {
            jugador.recibirCarta(mazo.dar1Carta());
        }
    }

    private void agregarCartaAlMazoDeDescartes(Carta carta){
        mazoDeDescarte.agregarCarta(carta);
    }

    private void dar1CartaJugador(Jugador jugador){

    }

    private void darCartasFaltantesJugador(Jugador jugador){
    }

    @Override
    public void notificarCambio(Object objeto){
        for(IObservador observador : this.observadores){
            observador.actualizar(objeto);
        }
    }

    @Override
    public void agregarObservador(IObservador observador){
        this.observadores.add(observador);
    }

    /* Para terminar el turno de un jugador y dárselo al siguiente */
    public void cambiarTurnoJugador(){
        // Si el turno de jugador está en nulo, se le da el turno al primer jugador. Esto pasa solo al empezar el juego.
        if(turnoJugador == null){
            turnoJugador = jugadores.get(0);
        } else {
            // Cicla entre los jugadores que hay en el juego. Si son 2 jugadores, siempre ciclará entre el primero y el segundo.
            int indiceActual = jugadores.indexOf(turnoJugador);
            int indiceSiguiente = (indiceActual + 1) % jugadores.size();
            turnoJugador = jugadores.get(indiceSiguiente);
        }
    }

    public Mazo getMazoDeDescarte() {
        return mazoDeDescarte;
    }

    public Mazo getMazo() {
        return mazo;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public Jugador getTurnoJugador() {
        return turnoJugador;
    }

    public ArrayList<IObservador> getObservadores() {
        return observadores;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

}
