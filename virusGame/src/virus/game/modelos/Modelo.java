package virus.game.modelos;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;
import virus.game.modelos.cartas.Medicina;
import virus.game.modelos.cartas.Organo;
import virus.game.modelos.cartas.Virus;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

public class Modelo extends ObservableRemoto implements IModelo, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Mazo mazo;
    private Mazo mazoDeDescarte;
    private ArrayList<Jugador> jugadores;
    private Jugador ganador;
    private Jugador turnoJugador;

    public Modelo(){
        this.jugadores = new ArrayList<Jugador>();
        this.mazo = new Mazo(false);
        this.mazoDeDescarte = new Mazo(true);
        this.ganador = null;
        this.turnoJugador = null;
    }

    @Override
    public void iniciarJuego() throws RemoteException {
        // Inicia el juego si ya hay 2 jugadores
        if(jugadores.size() >= 2) this.notificarObservadores(AccionModelo.INICIAR_JUEGO);
        else {
            // Notifica que espere el registro si no hay 2 jugadores
            this.notificarObservadores(AccionModelo.ESPERAR_REGISTRO);
        }
    }

    @Override
    public boolean realizarAccionDeCarta(int idJugador, int numCarta) throws RemoteException {
        Jugador jugador = getJugadorPorId(idJugador);
        // Obtiene el índice de la carta que pidio jugar el usuario
        // Es numCarta - 1 para acceder al índice del array de la mano del usuario
        Carta cartaJugada = jugador.getMano().get(numCarta - 1);
        boolean sePudoJugarUnaCarta = false;

        if(cartaJugada instanceof Organo){
            boolean sePudoJugarOrgano = jugarCarta(jugador, (Organo) cartaJugada);
            if(sePudoJugarOrgano){
                // Le doy 1 carta nueva si se pudo jugar el organo
                darCartasFaltantesJugador(jugador, 1);
                sePudoJugarUnaCarta = true;
            }
        } else {
            if(cartaJugada instanceof Virus){
                boolean sePudoJugarVirus = jugarCarta(jugador, (Virus) cartaJugada);
                if(sePudoJugarVirus){
                    // Le doy 1 carta nueva si se pudo jugar el virus
                    darCartasFaltantesJugador(jugador, 1);
                    sePudoJugarUnaCarta = true;
                }
            } else {
                if(cartaJugada instanceof Medicina){
                    boolean sePudoJugarMedicina = jugarCarta(jugador, (Medicina) cartaJugada);
                    if(sePudoJugarMedicina){
                        // Le doy 1 carta nueva si se pudo jugar la medicina
                        darCartasFaltantesJugador(jugador, 1);
                        sePudoJugarUnaCarta = true;
                    }
                }
            }
        }
        // Notifico si se pudo jugar la carta en cuestión
        return sePudoJugarUnaCarta;
    }

    @Override
    public boolean jugarCarta(Jugador jugador, Organo organo) throws RemoteException {
        boolean organoAgregado = jugador.getCuerpoJugador().agregarOrganoAlCuerpo(organo);
        if (organoAgregado){
            jugador.eliminarCartaDeLaMano(organo);
        }

        // Retorna true si se pudo jugar
        return organoAgregado;
    }

    /* Intenta aplicar una infeccion
    * Devuelve falso si es inmune o no existe el organo */
    @Override
    public boolean jugarCarta(Jugador jugador, Virus virus) throws RemoteException {
        Cuerpo cuerpoRival = getRival(jugador.getNumeroDeJugador()).getCuerpoJugador();
        Organo organoAfectado = cuerpoRival.infectarOrgano(virus);
        if(organoAfectado != null){
            if(organoAfectado.esInmune()){
                return false;
            }
        } else {
            return false;
        }

        if(organoAfectado.estaExtirpado()){
            // Elimina el organo del cuerpo si está para extirpar (con dos infecciones)
            extirparOrganoDelCuerpo(getRival(jugador.getNumeroDeJugador()), organoAfectado);
        } else {
            // Si tiene 1 infeccion y 1 medicina, elimino ambas y las envio a descartes
            if(organoAfectado.getInfecciones().size() == 1 && organoAfectado.getMedicinas().size() == 1){
                mazoDeDescarte.agregarCarta(organoAfectado.getMedicinas().get(0));
                mazoDeDescarte.agregarCarta(organoAfectado.getInfecciones().get(0));
                cuerpoRival.eliminarInfeccionYMedicina(organoAfectado);
            }
        }

        jugador.eliminarCartaDeLaMano(virus);

        // Retorna verdadero si el organo fue infectado.
        return true;
    }

    /* Busca un organo compatible para curar
    * Se fija si el organo no es inmune
    * En ese caso, lo cura. Devuelve true.
    * Si ya es inmune, no lo cura. Devuelve falso.
    * Si no existe, devuelve falso. */
    @Override
    public boolean jugarCarta(Jugador jugador, Medicina medicina) throws RemoteException {
        Organo organoACurar = jugador.getCuerpoJugador().encontrarOrgano(medicina.getColor());
        if(organoACurar != null){
            if(!organoACurar.esInmune()){
                jugador.getCuerpoJugador().curarOrgano(medicina);

                // Si es inmune, no hago nada. Las medicinas quedan ahí
                if(!organoACurar.esInmune()){
                    if(organoACurar.getInfecciones().size() == 1 && organoACurar.getMedicinas().size() == 1){
                        mazoDeDescarte.agregarCarta(organoACurar.getMedicinas().get(0));
                        mazoDeDescarte.agregarCarta(organoACurar.getInfecciones().get(0));
                        jugador.getCuerpoJugador().eliminarInfeccionYMedicina(organoACurar);
                    }
                }
                jugador.eliminarCartaDeLaMano(medicina);
                return true;
            }
            return false;
        }
        return false;
    }

    /* Elimina el organo del cuerpo y lo coloca en el mazo de descartes
     * Reinicia su estado
     * Coloca sus medicinas e infecciones en el mazo de descartes
     * */
    @Override
    public void extirparOrganoDelCuerpo(Jugador jugador, Organo organo) throws RemoteException {
        Organo organoExtirpado = jugador.getCuerpoJugador().extirparOrgano(organo);
        Virus[] infeccionesExtraidas = organoExtirpado.extraerInfecciones();
        Medicina[] medicinasExtraidas = organoExtirpado.extraerMedicinas();
        for (int i = 0; i < medicinasExtraidas.length; i++) {
            mazoDeDescarte.agregarCarta(medicinasExtraidas[i]);
        }
        for (int i = 0; i < infeccionesExtraidas.length; i++) {
            mazoDeDescarte.agregarCarta(infeccionesExtraidas[i]);
        }
        organoExtirpado.reiniciarOrgano();
        mazoDeDescarte.agregarCarta(organoExtirpado);
    }


    /* Retorna el jugador rival  */
    @Override
    public Jugador getRival(int idJugador) throws RemoteException {
        if(getJugadorPorId(idJugador) != jugadores.get(0)){
            return jugadores.get(0);
        } else {
            return jugadores.get(1);
        }
    }

    /* Agrego un nuevo jugador */
    @Override
    public void agregarJugador(Jugador jugador) throws RemoteException {
        System.out.println("Agregando jugador: " + jugador.getNombre());
        dar3CartasJugador(jugador);
        jugadores.add(jugador);
        System.out.println(jugador);
    }

    @Override
    public int agregarJugador(String nombre) throws RemoteException {
        Jugador nuevoJugador = new Jugador(nombre);
        dar3CartasJugador(nuevoJugador);
        jugadores.add(nuevoJugador);
        return nuevoJugador.getNumeroDeJugador();
    }

    @Override
    public void descartarCartaManoJugador(int idJugador, int[] indicesDeCartas) throws RemoteException {
        Jugador jugador = getJugadorPorId(idJugador);
        // Ordeno el array de forma ascendente
        Arrays.sort(indicesDeCartas);

        // Hago un ciclo que arranque por el ultimo elemento
        for (int i = indicesDeCartas.length - 1; i >= 0; i--) {
            Carta cartaADescartar = jugador.getMano().get(indicesDeCartas[i] - 1);
            jugador.eliminarCartaDeLaMano(cartaADescartar);
            agregarCartaAMazoDeDescartes(cartaADescartar);
        }
        darCartasFaltantesJugador(jugador, indicesDeCartas.length);
    }

    @Override
    public ArrayList<Carta> getManoJugador(int idJugador) throws RemoteException {
        return getJugadorPorId(idJugador).getMano();
    }

    @Override
    public void agregarCartaAMazoDeDescartes(Carta carta){
        mazoDeDescarte.agregarCarta(carta);
    }

    @Override
    public void dar3CartasJugador(Jugador jugador) throws RemoteException {
        for (int i = 0; i < 3; i++) {
            jugador.recibirCarta(mazo.dar1Carta());
        }
    }

    @Override
    public void dar1CartaJugador(Jugador jugador) throws RemoteException{
        jugador.recibirCarta(mazo.dar1Carta());
    }

    /* Metodo que permite intecambiar el mazo de descartes con el mazo principal
    *  Cuando el mazo está vacío */
    @Override
    public void intercambiarMazos() throws RemoteException {
        if(mazo.getCartas().isEmpty()){
            for (int i = 0; i < mazoDeDescarte.getCartas().size(); i++) {
                // Envío cada carta del mazo de descarte, al mazo original.
                // Esto pasa cuando el mazo está vacío unicamente.
                mazo.getCartas().add(mazoDeDescarte.getCartas().get(i));
            }
            mazo.mezclarMazo();
            mazoDeDescarte.vaciarMazo();
        }
    }

    @Override
    public void darCartasFaltantesJugador(Jugador jugador, int cantidad) throws RemoteException {
        for (int i = 0; i < cantidad; i++) {
             Carta cartaParaDar = mazo.dar1Carta();
             if(cartaParaDar == null){
                 intercambiarMazos();
                 cantidad++;
             } else {
                 jugador.recibirCarta(cartaParaDar);
             }

        }
    }

    // Eliminar
    /*@Override
    public void notificarCambio(Object objeto){
        for(IObservador observador : this.observadores){
            observador.actualizar(objeto);
        }
    }*/

    /*@Override
    public void agregarObservador(IObservador observador){
        this.observadores.add(observador);
    }*/


    /* Para terminar el turno de un jugador y dárselo al siguiente */
    @Override
    public Jugador cambiarTurnoJugador() throws RemoteException {
        // Si el turno de jugador está en nulo, se le da el turno al primer jugador. Esto pasa solo al empezar el juego.
        if(turnoJugador == null){
            turnoJugador = jugadores.get(0);
        } else {
            if(turnoJugador.equals(jugadores.get(0))){
                turnoJugador = jugadores.get(1);
            } else {
                turnoJugador = jugadores.get(0);
            }
            //turnoJugador = jugadores.get(1);
            notificarObservadores(AccionModelo.INICIO_NUEVO_TURNO);
        }


        return turnoJugador;
    }

    // Controlar si algun jugador está en condición de ser ganador
    @Override
    public void controlarGanador() throws RemoteException {
        for (int i = 0; i < jugadores.size(); i++) {
            // Si el jugador tiene 4 organos, compruebo si ganó
            Cuerpo cuerpoJugador = jugadores.get(i).getCuerpoJugador();
            if(cuerpoJugador.getOrganos().size() == 4){
                boolean tieneAlgunaInfeccion = false;

                // Ciclo sobre el cuerpo del jugador
                for (int j = 0; j < 4; j++) {
                    // Si tiene alguna infección, corto el ciclo
                    if(!cuerpoJugador.getOrganos().get(j).getInfecciones().isEmpty()){
                        tieneAlgunaInfeccion = true;
                        break;
                    }
                }

                // Si no tiene ninguna infección, entonces ese jugador es el ganador :)
                // Descomentar esto luego
                if(!tieneAlgunaInfeccion){
                    this.ganador = jugadores.get(i);
                    jugadores.get(i).setGanador();
                //    String nombreGanador = ganador.getNombre();
                //    String nombrePerdedor = getRival(ganador).getNombre();
                //    SerializadorDeGanadores serializadorDeGanadores = new SerializadorDeGanadores(nombreGanador, nombrePerdedor);
                    if(ganador.getNumeroDeJugador() == 1){
                        notificarObservadores(AccionModelo.GANO_JUGADOR_1);
                    } else {
                        notificarObservadores(AccionModelo.GANO_JUGADOR_2);
                    }
                }
            }
        }
        if(ganador == null){
            cambiarTurnoJugador();
        }
    }

    @Override
    public int cantidadDeCartasEnMazo() throws RemoteException {
        return getMazo().getCartas().size();
    }

    @Override
    public int cantidadDeCartasEnMazoDeDescartes() throws RemoteException {
        return getMazoDeDescarte().getCartas().size();
    }


    @Override
    public Mazo getMazoDeDescarte() throws RemoteException {
        return mazoDeDescarte;
    }

    @Override
    public Mazo getMazo() throws RemoteException {
        return mazo;
    }

    @Override
    public Jugador getGanador() throws RemoteException {
        return ganador;
    }

    @Override
    public Jugador getTurnoJugador() throws RemoteException {
        return turnoJugador;
    }

    // Solucion al problema del alising con RMI
    @Override
    public Jugador getJugadorPorId(int idJugador) throws RemoteException{
        if(idJugador == -1){
            return null;
        } else {
            return idJugador == jugadores.get(0).getNumeroDeJugador()
                    ? jugadores.get(0)
                    : jugadores.get(1);
        }
    }

    /*public ArrayList<IObservador> getObservadores() {
        return observadores;
    }*/

    @Override
    public ArrayList<Jugador> getJugadores() throws RemoteException {
        return jugadores;
    }

}
