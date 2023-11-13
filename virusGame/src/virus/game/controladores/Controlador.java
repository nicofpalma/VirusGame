package virus.game.controladores;

import virus.game.modelos.*;
import virus.game.observer.IObservador;
import virus.game.vistas.AccionVista;
import virus.game.vistas.IVista;

public class Controlador implements IObservador {
    private Juego modelo;
    private IVista vista;
    private Jugador jugador;
    private Jugador rival;

    public Controlador(Juego juego, IVista vista){
        this.modelo = juego;
        this.vista = vista;
        this.vista.setControlador(this);
        this.modelo.agregarObservador(this);
        this.jugador = null;
        this.rival = null;
        //modelo.iniciarJuego();
    }

    public void accionarCarta(){

    }

    public void descartarCartas(){

    }

    public int getCantidadDeCartasEnMazo(){
        return modelo.getMazo().cantidadDeCartasEnMazo();
    }

    public int getCantidadDeCartasEnMazoDeDescartes(){
        return modelo.getMazoDeDescarte().cantidadDeCartasEnMazo();
    }

    /* Agrega un nuevo jugador y tambien lo agrega al modelo */
    public void nuevoJugador(String nombre){
        this.jugador = new Jugador(nombre);
        modelo.agregarJugador(jugador);
        modelo.iniciarJuego();
    }

    /* Devuelve el jugador del controlador */
    public Jugador getJugador() {
        return jugador;
    }

    /* Obtiene el rival del jugador actual */
    public Jugador getRival(){
        if(rival == null){
            rival = modelo.getRival(jugador);
        }
        return rival;
    }

    /* Devuelve el Cuerpo del jugador */
    public Cuerpo getCuerpoJugador(){
        return getJugador().getCuerpoJugador();
    }

    /* Obtiene el cuerpo del jugador en un String concatenado */
    public String getCuerpoJugadorToString(){
        return getCuerpoJugador().toString();
    }

    /* Devuelve el cuerpo del rival */
    public Cuerpo getCuerpoRival(){
        return getRival().getCuerpoJugador();
    }

    /* Devuelve el cuerpo del rival en un String concatenado */
    public String getCuerpoRivalToString(){
        return getCuerpoRival().toString();
    }

    @Override
    public void actualizar(Object accion) {
        if(accion instanceof AccionModelo){
            Jugador jugadorActual = this.modelo.getTurnoJugador();

            switch ((AccionModelo) accion){
                case INICIAR_JUEGO:
                {
                    vista.mostrarMesa();
                    break;
                }
                case NUEVO_JUGADOR:
                {
                    vista.setAccionVista(AccionVista.NUEVO_JUGADOR);
                    break;
                }
            }
        }
    }


}
