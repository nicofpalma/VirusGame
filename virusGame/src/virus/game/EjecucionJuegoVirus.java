package virus.game;

import virus.game.controladores.Controlador;
import virus.game.modelos.Juego;
import virus.game.vistas.consola.VistaConsola;

import javax.swing.*;

public class EjecucionJuegoVirus {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VistaConsola vista1 = new VistaConsola();
                VistaConsola vista2 = new VistaConsola();

                Juego modelo = new Juego();
                Controlador controlador1 = new Controlador(modelo, vista1);
                Controlador controlador2 = new Controlador(modelo, vista2);
                vista1.vistaInicial();
                vista2.vistaInicial();

            }
        });



    }
}
