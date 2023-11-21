package virus.game.modelos;

import virus.game.modelos.medicinas.Medicina;
import virus.game.modelos.organos.Organo;
import virus.game.modelos.virus.Virus;
import virus.game.observer.IObservable;
import virus.game.observer.IObservador;

import java.util.ArrayList;

public class Juego implements IObservable {
    private Mazo mazo;
    private Mazo mazoDeDescarte;
    private ArrayList<Jugador> jugadores;
    private Jugador ganador;
    private Jugador turnoJugador;
    private ArrayList<IObservador> observadores;

    public Juego(){
        this.jugadores = new ArrayList<Jugador>();
        this.observadores = new ArrayList<IObservador>();
        this.mazo = new Mazo(false);
        this.mazoDeDescarte = new Mazo(true);
        this.ganador = null;
        this.turnoJugador = null;
    }

    public void iniciarJuego(){
        // Inicia el juego si ya hay 2 jugadores
        if(jugadores.size() >= 2) this.notificarCambio(AccionModelo.INICIAR_JUEGO);
        else {
            // Notifica que espere el registro si no hay 2 jugadores
            this.notificarCambio(AccionModelo.ESPERAR_REGISTRO);
        }
    }

    public boolean jugarCarta(Jugador jugador, Organo organo){
        boolean organoAgregado = jugador.getCuerpoJugador().agregarOrganoAlCuerpo(organo);
        if (organoAgregado){
            jugador.eliminarCartaDeLaMano(organo);
        }

        // Retorna true si se pudo jugar
        return organoAgregado;
    }

    /* Intenta aplicar una infeccion
    * Devuelve falso si es inmune o no existe el organo */
    public boolean jugarCarta(Jugador jugador, Virus virus){
        Cuerpo cuerpoRival = getRival(jugador).getCuerpoJugador();
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
            extirparOrganoDelCuerpo(getRival(jugador), organoAfectado);
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
    public boolean jugarCarta(Jugador jugador, Medicina medicina){
        Organo organoACurar = jugador.getCuerpoJugador().encontrarOrgano(medicina.getColor());
        if(organoACurar != null){
            if(!organoACurar.esInmune()){
                jugador.getCuerpoJugador().curarOrgano(medicina);

                // Si se vuelve inmune al aplicar la medicina, la elimino y la agrego al mazo de descartes
                if(organoACurar.esInmune()){
                    Medicina[] medicinasExtraidas = organoACurar.extraerMedicinas();
                    mazoDeDescarte.agregarCarta(medicinasExtraidas[0]);
                    mazoDeDescarte.agregarCarta(medicinasExtraidas[1]);
                    jugador.getCuerpoJugador().eliminarMedicinas(organoACurar);
                } else {
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
    public void extirparOrganoDelCuerpo(Jugador jugador, Organo organo){
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


    /* Retorna el jugador rival, retorna nulo si no lo encuentra (controlar este caso) */
    public Jugador getRival(Jugador jugador){
        Jugador jugadorRival = null;
        for (int i = 0; i < jugadores.size(); i++) {
            jugadorRival = jugadores.get(i);
            if(!jugadorRival.equals(jugador)){
                return jugadorRival;
            }
        }
        return jugadorRival;
    }

    /* Agrego un nuevo jugador */
    public void agregarJugador(Jugador jugador){
        dar3CartasJugador(jugador);
        jugadores.add(jugador);
    }

    /* Le da 3 cartas al jugador, se usa cuando se inicia un nuevo juego */
    private void dar3CartasJugador(Jugador jugador){
        for (int i = 0; i < 3; i++) {
            jugador.recibirCarta(mazo.dar1Carta());
        }
    }

    private void agregarCartaAMazoDeDescartes(Carta carta){
        mazoDeDescarte.agregarCarta(carta);
    }

    public void descartarCartaManoJugador(Jugador jugador, Carta carta){
        jugador.eliminarCartaDeLaMano(carta);
        agregarCartaAMazoDeDescartes(carta);
    }

    /* Metodo que permite intecambiar el mazo de descartes con el mazo principal
    *  Cuando el mazo está vacío */
    public void intercambiarMazos(){
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

    private void dar1CartaJugador(Jugador jugador){
        jugador.recibirCarta(mazo.dar1Carta());
    }

    public void darCartasFaltantesJugador(Jugador jugador, int cantidad){
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

    @Override
    public void notificarCambio(Object objeto){
        for(IObservador observador : this.observadores){
            observador.actualizar(objeto);
        }
    }

    @Override
    public void agregarObservador(IObservador observador){
        this.observadores.add(observador);
    }


    /* Para terminar el turno de un jugador y dárselo al siguiente */
    public Jugador cambiarTurnoJugador(){
        // Si el turno de jugador está en nulo, se le da el turno al primer jugador. Esto pasa solo al empezar el juego.
        if(turnoJugador == null){
            turnoJugador = jugadores.get(0);
        } else {
            // Cicla entre los jugadores que hay en el juego. Si son 2 jugadores, siempre ciclará entre el primero y el segundo.
            int indiceActual = jugadores.indexOf(turnoJugador);
            int indiceSiguiente = (indiceActual + 1) % jugadores.size();
            turnoJugador = jugadores.get(indiceSiguiente);
            notificarCambio(AccionModelo.INICIO_NUEVO_TURNO);
        }


        return turnoJugador;
    }

    // Controlar si algun jugador está en condición de ser ganador
    public void controlarGanador(){
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
                if(!tieneAlgunaInfeccion){
                    this.ganador = jugadores.get(i);
                    notificarCambio(AccionModelo.GAME_OVER);
                }
            }
        }
    }

    public Mazo getMazoDeDescarte() {
        return mazoDeDescarte;
    }

    public Mazo getMazo() {
        return mazo;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public Jugador getTurnoJugador() {
        return turnoJugador;
    }

    public ArrayList<IObservador> getObservadores() {
        return observadores;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

}
