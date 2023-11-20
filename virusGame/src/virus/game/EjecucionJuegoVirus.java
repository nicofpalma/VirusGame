package virus.game;

import virus.game.controladores.Controlador;
import virus.game.modelos.IJuego;
import virus.game.modelos.Juego;
import virus.game.vistas.consola.VistaConsola;

import javax.swing.*;
import java.rmi.RemoteException;

public class EjecucionJuegoVirus {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            // Genero las vistas de consola
            VistaConsola vista1 = new VistaConsola();
            VistaConsola vista2 = new VistaConsola();

            // Creo el modelo del juego y lo asigno a cada controlador, con cada vista
            IJuego modelo = new Juego();
            try {
                Controlador controlador1 = new Controlador(modelo, vista1);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            try {
                Controlador controlador2 = new Controlador(modelo, vista2);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

            // Estas vistas iniciales dan inicio al ciclo del juego, primero se pide un nombre de jugador, y luego empieza el juego.
            vista1.vistaInicial();
            vista2.vistaInicial();
        });
    }
}
