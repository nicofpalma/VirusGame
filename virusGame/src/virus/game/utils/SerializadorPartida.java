package virus.game.utils;

import virus.game.modelos.Modelo;

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
        return serializador.readObjects().length > 0;
    }

    public String nombreJugadores() throws RemoteException {
        Modelo modelo = (Modelo) serializador.readObjects()[0];
        return modelo.getJugadores().get(0).getNombre() +
                " vs " +
                modelo.getJugadores().get(1).getNombre();
    }

    public Object cargarPartidaGuardada(){
        return serializador.readObjects()[0];
    }
}
