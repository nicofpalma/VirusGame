package virus.game.controladores;

import virus.game.modelos.AccionModelo;
import virus.game.modelos.Cuerpo;
import virus.game.modelos.Juego;
import virus.game.modelos.Jugador;
import virus.game.observer.IObservador;
import virus.game.utils.SerializadorDeGanadores;
import virus.game.vistas.AccionVista;
import virus.game.vistas.IVista;

public class Controlador implements IObservador {
    private Juego modelo;
    private IVista vista;
    private Jugador jugador;
    private Jugador rival;
    private AccionModelo accionModelo;

    public Controlador(Juego juego, IVista vista){
        this.modelo = juego;
        this.vista = vista;
        this.vista.setControlador(this);
        this.modelo.agregarObservador(this);
        this.jugador = null;
        this.rival = null;
        //modelo.iniciarJuego();
    }

    public boolean accionarCarta(int numCarta){
        // Llama al método del modelo realizarAccionDeCarta
        // Este método contiene todas las acciones posibles que puede realizar
        // El jugador, y detecta qué tipo de carta es jugada.
        return modelo.realizarAccionDeCarta(this.jugador, numCarta);
    }

    // Se ejecuta cuando el jugador quiere descartar cartas
    public void descartarCartas(int[] indicesDeCartas){
        modelo.descartarCartaManoJugador(jugador, indicesDeCartas);
    }

    public int getCantidadDeCartasEnMazo(){
        return modelo.cantidadDeCartasEnMazo();
    }

    public int getCantidadDeCartasEnMazoDeDescartes(){
        return modelo.cantidadDeCartasEnMazoDeDescartes();
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
            switch ((AccionModelo) accion){
                case INICIAR_JUEGO:
                {
                    avisarCambioDeTurno();
                    vista.mostrarMesa();
                    break;
                }
                case ESPERAR_REGISTRO:{
                    if(this.jugador != null){
                        vista.avisarEsperaALosDemasJugadores();
                    }
                    break;
                }
                case INICIO_NUEVO_TURNO:{
                    vista.mostrarMesa();
                    avisarCambioDeTurno();
                    break;
                }
                case GAME_OVER:{
                    Jugador jugadorGanador = modelo.getGanador();
                    vista.mostrarMesa();
                    if(jugadorGanador.equals(this.jugador)){
                        vista.avisarQueElJugadorGano();
                    } else {
                        vista.avisarQueElJugadorPerdio();
                    }
                    vista.setAccionVista(AccionVista.GAME_OVER);
                    break;
                }
            }
        }
    }

    public Jugador getTurnoJugador(){
        return modelo.getTurnoJugador();
    }

    // Devuelve true si es el turno del jugador
    public boolean esSuTurno(){
        return modelo.getTurnoJugador().equals(this.jugador);
    }

    // Controla el fin de turno, otorgandole el turno a otro jugador.
    // También controla si hay un ganador
    public void finDeTurno(){
        modelo.controlarGanador();
        if (modelo.getGanador() == null) {
            modelo.cambiarTurnoJugador();
        }
    }

    public Jugador getJugadorGanador(){
        return modelo.getGanador();
    }

    // Le otorga el control al jugador en la vista o le dice que espere su turno
    private void avisarCambioDeTurno(){
        // Sucede cuando es el primer turno
        if (modelo.getTurnoJugador() == null){
            modelo.cambiarTurnoJugador();
        }

        // Avisa de quien es el turno
        if(modelo.getTurnoJugador().equals(this.jugador)){
            vista.setAccionVista(AccionVista.TURNO_JUGADOR);
        } else {
            vista.setAccionVista(AccionVista.ESPERAR_TURNO);
        }
    }

    public String buscarTablaDeGanadores(){
        SerializadorDeGanadores serializadorDeGanadores = new SerializadorDeGanadores();
        return serializadorDeGanadores.generarStringDeGanadores();
    }

}
