package virus.game;

import virus.game.controladores.Controlador;
import virus.game.modelos.Juego;
import virus.game.vistas.IVista;
import virus.game.vistas.consola.VistaConsola;

import javax.swing.*;

public class EjecucionJuegoVirus {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            // Genero las vistas de consola
            IVista vista1 = new VistaConsola();
            IVista vista2 = new VistaConsola();

            // Creo el modelo del juego y lo asigno a cada controlador, con cada vista
            Juego modelo = new Juego();
            Controlador controlador1 = new Controlador(modelo, vista1);
            Controlador controlador2 = new Controlador(modelo, vista2);

            // Estas vistas iniciales dan inicio al ciclo del juego, primero se pide un nombre de jugador, y luego empieza el juego.
            vista1.vistaInicial();
            vista2.vistaInicial();
        });
    }
}
