package virus.game.vistas.grafica;

import virus.game.controladores.Controlador;
import virus.game.vistas.AccionVista;
import virus.game.vistas.IVista;

import javax.swing.*;

public class VistaGrafica extends JFrame implements IVista {


    private AccionVista accionVista;

    private Controlador controlador;

    public VistaGrafica(){
        setTitle("Virus - Vista Grafica");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);



    }


    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void mostrarCartasManoJugador() {

    }

    @Override
    public void vistaInicial() {

    }

    @Override
    public void setAccionVista(AccionVista accion) {

    }

    @Override
    public void mostrarCartasManoRival() {

    }

    @Override
    public void mostrarCuerpoRival() {

    }

    @Override
    public void mostrarCuerpoJugador() {

    }

    @Override
    public void mostrarCantidadDeCartasEnMazo() {

    }

    @Override
    public void mostrarCantidadDeCartasEnMazoDeDescartes() {

    }

    @Override
    public void mostrarMesa() {

    }

    @Override
    public void avisarEsperaALosDemasJugadores() {

    }

    @Override
    public void avisarTurno() {

    }

    @Override
    public void jugadorRealizaUnaAccion() {

    }

    @Override
    public void accionIncorrecta() {

    }

    @Override
    public AccionVista getAccionVista() {
        return null;
    }

    @Override
    public void elegirCartasADescartar() {

    }

    @Override
    public void avisarQueElJugadorGano() {

    }

    @Override
    public void avisarQueElJugadorPerdio() {

    }

    @Override
    public void mostrarTablaDeGanadores() {

    }

    @Override
    public void mostrarReglas() {

    }
}
