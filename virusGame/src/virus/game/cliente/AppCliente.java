package virus.game.cliente;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import virus.game.controladores.Controlador;
import virus.game.servidor.AppServidor;
import virus.game.vistas.IVista;
import virus.game.vistas.consola.VistaConsola;
import virus.game.rmimvc.RMIMVCException;
import virus.game.rmimvc.Util;
import virus.game.rmimvc.cliente.Cliente;

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
        IVista vista = new VistaConsola();
        Controlador controlador = new Controlador(AppServidor.getModelo(), vista);
        Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
        vista.vistaInicial();
        try {
            c.iniciar(controlador);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RMIMVCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
