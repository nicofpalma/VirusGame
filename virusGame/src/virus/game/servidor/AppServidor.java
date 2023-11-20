package virus.game.servidor;

import java.util.ArrayList;
import java.rmi.RemoteException;

import virus.game.modelos.Juego;
import virus.game.rmimvc.ServidorRMI;
import virus.game.rmimvc.RMIMVCException;
import virus.game.rmimvc.Util;
import virus.game.rmimvc.servidor.*;

import javax.swing.*;


public class AppServidor {
    public static Juego modelo;

    public static void main (String[] args){
        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchará peticiones el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchará peticiones el servidor", "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );
        AppServidor.modelo = new Juego();
        Servidor servidor = new Servidor(ip, Integer.parseInt(port));
        try {
            servidor.iniciar(modelo);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (RMIMVCException e) {
            e.printStackTrace();
        }
    }

    public static Juego getModelo() {
        return modelo;
    }
}
