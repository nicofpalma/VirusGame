package virus.game.modelos;

import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;
import virus.game.controladores.Controlador;
import virus.game.vistas.IVista;
import virus.game.vistas.consola.VistaConsola;
import virus.game.vistas.grafica.VistaGrafica;

import javax.swing.*;
import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class AppCliente {
    public static void main(String[] args) {
        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchará peticiones el cliente", "IP del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchará peticiones el cliente", "Puerto del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                9999
        );
        String ipServidor = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la corre el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );
        String portServidor = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que corre el servidor", "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );
        ArrayList<String> vistas = new ArrayList<>(2);
        vistas.add("Vista gráfica");
        vistas.add("Vista consola");

        String tipoDeVista = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la vista con la que desea jugar", "Tipo de vista",
                JOptionPane.QUESTION_MESSAGE,
                null,
                vistas.toArray(),
                vistas.get(0)
        );

        SwingUtilities.invokeLater(()-> {
            IVista vista;
            if(tipoDeVista.equals("Vista gráfica")){
                // Tomo la vista gráfica si la elegió, si no la de consola.
                vista = new VistaGrafica();
            } else {
                vista = new VistaConsola();
            }
            Controlador controlador = new Controlador(vista);
            vista.setControlador(controlador);
            Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
            try {
                c.iniciar(controlador);
                vista.vistaInicial();
            } catch (RMIMVCException e) {
                throw new RuntimeException(e);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }
}


