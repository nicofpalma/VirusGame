package virus.game.vistas;

import virus.game.controladores.Controlador;

public interface IVista {
    void setControlador(Controlador controlador);

    void mostrarCartasManoJugador();

    void vistaInicial();

    void mostrarTextoInformativo(String texto);

    void mostrarBienvenida();

    void ingresarNuevoJugador();

    void setAccionVista(AccionVista accion);

    void mostrarCartasManoRival();

    void mostrarCuerpoRival();

    void mostrarCuerpoJugador();

    void mostrarCantidadDeCartasEnMazo();

    void mostrarCantidadDeCartasEnMazoDeDescartes();

    void mostrarMesa();

    void iniciarVistaYControlarEventos();

    // Muestra el boton para descartar cartas solo cuando es su turno
    void mostrarBotonDescartar();

    void avisarEsperaALosDemasJugadores();

    // Muestra el mazo de descartes
    void mostrarMazoDeDescartes();

    void avisarTurno();

    void jugadorRealizaUnaAccion();

    void accionIncorrecta();

    AccionVista getAccionVista();

    void elegirCartasADescartar();

    void avisarQueElJugadorGano();

    void revanchaYSalir();

    void avisarQueElJugadorPerdio();

    void mostrarTablaDeGanadores();

    void mostrarReglas();
}
