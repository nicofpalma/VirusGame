package virus.game.utils;

import virus.game.modelos.Modelo;

import java.io.File;
import java.rmi.RemoteException;

public class SerializadorPartida {
    private static final Serializador serializador = new Serializador("partida.dat");

    public SerializadorPartida(Object modeloParaGuardar){
        serializador.writeOneObject(modeloParaGuardar);
    }

    public SerializadorPartida(){

    }

    public boolean hayPartidaGuardada() throws RemoteException {
        // Retorna true si existe una partida guardada
        return new File("partida.dat").exists();
    }

    public void borrarPartidaGuardada(){
        File archivoPartida = new File("partida.dat");

        if(archivoPartida.exists()){
            if(archivoPartida.delete()){
                System.out.println("Partida borrada con éxito.");
            } else {
                System.out.println("No se pudo borrar la partida");
            }
        } else {
            System.out.println("No existe ninguna partida guardada");
        }
    }

    public String getNombreDeJugadoresEnPartidaGuardada() throws RemoteException {
        Modelo modelo = (Modelo) serializador.readObjects()[0];
        return modelo.getJugadores().get(0).getNombre() +
                " vs " +
                modelo.getJugadores().get(1).getNombre();
    }

    public Object cargarPartidaGuardada(){
        return serializador.readObjects()[0];
    }
}
