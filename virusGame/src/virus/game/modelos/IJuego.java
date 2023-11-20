package virus.game.modelos;

import virus.game.modelos.medicinas.Medicina;
import virus.game.modelos.organos.Organo;
import virus.game.modelos.virus.Virus;
import virus.game.rmimvc.observer.IObservableRemoto;
import virus.game.rmimvc.observer.IObservadorRemoto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IJuego extends IObservableRemoto {
    void iniciarJuego() throws RemoteException;

    boolean jugarCarta(Jugador jugador, Organo organo) throws RemoteException;

    boolean jugarCarta(Jugador jugador, Virus virus) throws RemoteException;

    boolean jugarCarta(Jugador jugador, Medicina medicina) throws RemoteException;

    void extirparOrganoDelCuerpo(Jugador jugador, Organo organo) throws RemoteException;

    Jugador getRival(Jugador jugador) throws RemoteException;

     void agregarJugador(Jugador jugador) throws RemoteException;
     void descartarCartaManoJugador(Jugador jugador, Carta carta) throws RemoteException;

     void intercambiarMazos() throws RemoteException;

    void darCartasFaltantesJugador(Jugador jugador, int cantidad) throws RemoteException;

    Jugador cambiarTurnoJugador() throws RemoteException;

    void controlarGanador() throws RemoteException;

    Mazo getMazoDeDescarte() throws RemoteException;

    Mazo getMazo() throws RemoteException;

    Jugador getGanador() throws RemoteException;

    Jugador getTurnoJugador() throws RemoteException;

     ArrayList<IObservadorRemoto> getObservadores() throws RemoteException;

    public ArrayList<Jugador> getJugadores() throws RemoteException;
}
