package virus.game.vistas;

import virus.game.controladores.Controlador;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface IVista extends Serializable {
    void setControlador(Controlador controlador);

    void mostrarCartasManoJugador();

    void vistaInicial();

    void setAccionVista(AccionVista accion);

    void mostrarCartasManoRival() throws RemoteException;

    void mostrarCuerpoRival() throws RemoteException;

    void mostrarCuerpoJugador();

    void mostrarCantidadDeCartasEnMazo() throws RemoteException;

    void mostrarCantidadDeCartasEnMazoDeDescartes() throws RemoteException;

    void mostrarMesa() throws RemoteException;

    void avisarEsperaALosDemasJugadores();

    void avisarTurno() throws RemoteException;

    void jugadorRealizaUnaAccion() throws RemoteException;

    void accionIncorrecta();

    AccionVista getAccionVista();

    void elegirCartasADescartar() throws RemoteException;

    void avisarQueElJugadorGano() throws RemoteException;

    void avisarQueElJugadorPerdio() throws RemoteException;
}
