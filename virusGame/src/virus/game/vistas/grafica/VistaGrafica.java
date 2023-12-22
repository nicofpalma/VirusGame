package virus.game.vistas.grafica;

import virus.game.controladores.Controlador;
import virus.game.vistas.AccionVista;
import virus.game.vistas.IVista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VistaGrafica extends JFrame implements IVista {
    private AccionVista accionVista;
    private Controlador controlador;
    private JPanel panelCartasJugador;

    public VistaGrafica(){
        setTitle("Virus - Vista Grafica");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 900);
        setLocationRelativeTo(null);
        setVisible(true);

        panelCartasJugador = new JPanel();
        add(panelCartasJugador, BorderLayout.SOUTH);



        panelCartasJugador.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e ){
                switch (accionVista){
                    case NUEVO_JUGADOR: {
                        ingresarNuevoJugador();
                        System.out.println("Caso de nuevo jugador");
                        break;
                    }
                    case ESPERAR_TURNO: {
                        System.out.println("Caso de esperar turno");
                        break;
                    }
                    case TURNO_JUGADOR:{
                        System.out.println("Caso de turno jugador");
                        break;
                    }
                    case DESCARTAR_CARTAS:{
                        System.out.println("Caso de descartar cartas");
                        break;
                    }
                    case GAME_OVER:{
                        System.out.println("Caso de game over");
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
        });
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
    public void ingresarNuevoJugador() {
        String nombre = "Jugador 1";
        controlador.nuevoJugador(nombre);
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
