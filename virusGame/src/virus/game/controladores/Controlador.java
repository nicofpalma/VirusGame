package virus.game.controladores;

import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import virus.game.modelos.*;
import virus.game.modelos.cartas.Organo;
import virus.game.utils.SerializadorDeGanadores;
import virus.game.vistas.AccionVista;
import virus.game.vistas.IVista;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Controlador implements IControladorRemoto, Serializable {
    @Serial
    private static final long serialVersionUID = -5490212082421746916L;
    IModelo modelo;  // Corroborar si tiene que ser private o no
    IVista vista;   // Corroborar si tiene que ser private o no
    private int idJugador = -1;  // Inicializo en -1 para controlar cuando todavía no existe el jugador del controlador
    private int idRival = -2;   // Inicializo en -2 para controlar cuando todavía no existe un rival
    private AccionModelo accionModelo;

    public <T extends IObservableRemoto> Controlador(IVista vista, T modelo) throws RemoteException {
        //this.modelo = modelo;
        this.vista = vista;
        this.setModeloRemoto(modelo);
        //this.vista.setControlador(this);
        //this.modelo.agregarObservador(this);
        //modelo.iniciarJuego();
    }

    public Controlador(IVista vista){
        this.vista = vista;
    }

    public boolean accionarCarta(int numCarta){
        // Llama al método del modelo realizarAccionDeCarta
        // Este método contiene todas las acciones posibles que puede realizar
        // El jugador, y detecta qué tipo de carta es jugada.
        try {
            return modelo.realizarAccionDeCarta(idJugador, numCarta);
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return false;
    }

    // Se ejecuta cuando el jugador quiere descartar cartas
    public void descartarCartas(int[] indicesDeCartas){
        try{
            modelo.descartarCartaManoJugador(idJugador, indicesDeCartas);
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public int getCantidadDeCartasEnMazo(){
        try {
            return modelo.cantidadDeCartasEnMazo();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return -1;
    }

    public int getCantidadDeCartasEnMazoDeDescartes(){
        try {
            return modelo.cantidadDeCartasEnMazoDeDescartes();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return -1;
    }

    /* Agrega un nuevo jugador y tambien lo agrega al modelo */
    public void nuevoJugador(String nombre){
        try {
            this.idJugador = modelo.agregarJugador(nombre);
            modelo.iniciarJuego();
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public ArrayList<Carta> getManoJugador(){
        try {
            return modelo.getManoJugador(idJugador);
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    /* Devuelve el jugador del controlador */
    public Jugador getJugador() {
        try{
            return modelo.getJugadorPorId(idJugador);
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    /* Obtiene el rival del jugador actual */
    public Jugador getRival(){
        try {
            idRival = modelo.getRival(idJugador).getNumeroDeJugador();
            return modelo.getJugadorPorId(idRival);
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    /* Devuelve el Cuerpo del jugador */
    public Cuerpo getCuerpoJugador(){
        try {
            return modelo.getJugadorPorId(idJugador).getCuerpoJugador();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Carta> getCartasDelMazoDeDescartes(){
        try {
            return (ArrayList<Carta>) modelo.getMazoDeDescarte().getCartas();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Organo> getOrganosDelCuerpoDelJugador(){
        return getCuerpoJugador().getOrganos();
    }

    public ArrayList<Organo> getOrganosDelCuerpoDelRival() {
        return getCuerpoRival().getOrganos();
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

    /*
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
    }*/

    public Jugador getTurnoJugador(){
        try {
            return modelo.getTurnoJugador();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    // Devuelve true si es el turno del jugador
    public boolean esSuTurno(){
        try {
            return modelo.getTurnoJugador().getNumeroDeJugador() == idJugador;
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return false;
    }

    // Controla el fin de turno, otorgandole el turno a otro jugador.
    // También controla si hay un ganador
    public void finDeTurno(){
        try {
            modelo.controlarGanador();
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public Jugador getJugadorGanador(){
        try {
            return modelo.getGanador();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Le otorga el control al jugador en la vista o le dice que espere su turno
    public void avisarCambioDeTurno(){
        try {
            // Sucede cuando es el primer turno
            if (modelo.getTurnoJugador() == null){
                modelo.cambiarTurnoJugador();
            }

            // Avisa de quien es el turno
            if(modelo.getTurnoJugador().getNumeroDeJugador() == idJugador){
                vista.setAccionVista(AccionVista.TURNO_JUGADOR);
            } else {
                vista.setAccionVista(AccionVista.ESPERAR_TURNO);
            }
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public String buscarTablaDeGanadores(){
        SerializadorDeGanadores serializadorDeGanadores = new SerializadorDeGanadores();
        return serializadorDeGanadores.generarStringDeGanadores();
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.modelo = (IModelo) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto modelo, Object cambio)  {
        if(cambio instanceof AccionModelo){
            switch ((AccionModelo) cambio){
                case INICIAR_JUEGO:
                {
                    avisarCambioDeTurno();
                    vista.mostrarMesa();
                    break;
                }
                case ESPERAR_REGISTRO:{
                    if(idRival == -2){
                        vista.avisarEsperaALosDemasJugadores();
                    }
                    break;
                }
                case INICIO_NUEVO_TURNO:{
                    vista.mostrarMesa();
                    avisarCambioDeTurno();
                    break;
                }
                case GANO_JUGADOR_1:{
                    vista.mostrarMesa();
                    if(idJugador == 1){
                        vista.avisarQueElJugadorGano();
                    } else {
                        vista.avisarQueElJugadorPerdio();
                    }
                    vista.setAccionVista(AccionVista.GAME_OVER);
                    break;
                }
                case GANO_JUGADOR_2:{
                    vista.mostrarMesa();
                    if(idJugador == 2){
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
}
