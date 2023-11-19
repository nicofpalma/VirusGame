package virus.game.vistas;

import virus.game.controladores.Controlador;

public interface IVista {
    void setControlador(Controlador controlador);

    void mostrarCartasManoJugador();

    void vistaInicial();

    void setAccionVista(AccionVista accion);

    void mostrarCartasManoRival();

    void mostrarCuerpoRival();

    void mostrarCuerpoJugador();

    void mostrarCantidadDeCartasEnMazo();

    void mostrarCantidadDeCartasEnMazoDeDescartes();

    void mostrarMesa();

    void avisarEsperaALosDemasJugadores();

    void avisarTurno();

    void jugadorRealizaUnaAccion();

    void accionIncorrecta();

    AccionVista getAccionVista();

    void elegirCartasADescartar();
}
