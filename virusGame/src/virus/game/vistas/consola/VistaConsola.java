package virus.game.vistas.consola;

import virus.game.controladores.Controlador;
import virus.game.vistas.AccionVista;
import virus.game.vistas.IVista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class VistaConsola extends JFrame implements IVista {
    private JTextField textoUsuario;
    private JButton botonConfirmar;
    private JScrollPane areaConsola;
    private JTextArea areaTextoConsola;
    private JPanel panel;
    private AccionVista accionVista;
    private Controlador controlador;

    public VistaConsola(){
        setTitle("Virus Game");
        setContentPane(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        areaTextoConsola.setBackground(Color.BLACK);
        areaTextoConsola.setForeground(Color.WHITE);

        botonConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (accionVista){
                    case NUEVO_JUGADOR: {
                        borrarTexto();
                        ingresarNuevoJugador();
                        break;
                    }
                    case ESPERAR_TURNO:{
                        mostrarMesa();
                        mostrarTextoEnNuevaLinea("No es tu turno, espera...");
                        break;
                    }
                    case TURNO_JUGADOR:{
                        jugadorRealizaUnaAccion();
                        break;
                    }
                    case DESCARTAR_CARTAS:{
                        elegirCartasADescartar();
                        break;
                    }
                    case GAME_OVER: {
                        mostrarTextoEnNuevaLinea("El juego ya terminó...");
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        });

        // Para el control de teclas. Lo uso sólo para permitir el uso de enter además de hacer click en el botón
        textoUsuario.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // No necesito programar este método
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Este método sirve para recibir el enter del usuario como si fuese un click
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    botonConfirmar.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // No necesito programar este método
            }
        });
    }

    @Override
    public void avisarQueElJugadorGano(){
        mostrarTextoEnNuevaLinea("");
        separadorLinea();
        mostrarTextoEnNuevaLinea("¡FELICITACIONES " + controlador.getJugadorGanador().getNombre() + ", GANASTE EL JUEGO!");
        separadorLinea();
        mostrarTextoEnNuevaLinea("Para volver a jugar, cierre y vuelve a abrir el juego.");
    }

    @Override
    public void avisarQueElJugadorPerdio(){
        mostrarTextoEnNuevaLinea("");
        separadorLinea();
        mostrarTextoEnNuevaLinea("PERDISTE. LO SENTIMOS, " + controlador.getJugadorGanador().getNombre() + " ES EL GANADOR. TE DESEO MÁS SUERTE LA PRÓXIMA VEZ :)");
        separadorLinea();
        mostrarTextoEnNuevaLinea("Para volver a jugar, cierre y vuelve a abrir el juego.");
    }

    @Override
    public void jugadorRealizaUnaAccion(){
        // Controla que el numero de carta elegida sea 1, 2 o 3. Si es cero, es un descarte.
        // Si no es ninguno, muestro error.
        int numAccion = Integer.parseInt(textoUsuario.getText().trim());
        if(numAccion == 1 || numAccion == 2 || numAccion == 3){
            if (controlador.accionarCarta(numAccion)){
                controlador.finDeTurno();
            } else {
                mostrarMesa();
                noPuedeJugarEsaCarta();
            }
        } else {
            if(numAccion == 0){
                //Descartar cartas
                mostrarTextoEnNuevaLinea("Elige qué cartas descartar (Ejemplo: 1, 2, 3 (separados con coma)): ");
                setAccionVista(AccionVista.DESCARTAR_CARTAS);
            } else {
                mostrarMesa();
                accionIncorrecta();
            }
        }
        borrarInputUsuario();
    }

    @Override
    public void mostrarTablaDeGanadores() {
        separadorLinea();
        mostrarTextoEnNuevaLinea("  PARTIDAS ANTERIORES");
       mostrarTextoEnNuevaLinea(controlador.buscarTablaDeGanadores());
       separadorLinea();
    }

    @Override
    public void mostrarReglas(){
        separadorLinea();
        mostrarTextoEnNuevaLinea("  OBJETIVO Y REGLAS DEL JUEGO");
        mostrarTextoEnNuevaLinea("  --> PARA GANAR: Tener en tu cuerpo (tu mesa) las 4 cartas de órganos diferentes, sin ninguna infección.");
        mostrarTextoEnNuevaLinea("  --> ORGANOS: Cerebro, Corazón, Estómago y Hueso");
        separadorLinea();
        mostrarTextoEnNuevaLinea("  --> VIRUS: Los hay para cada órgano. Sirven para infectar los órganos del rival e impedir que éste gane.");
        mostrarTextoEnNuevaLinea("  --> Si se aplican 2 virus sobre un órgano, éste se extirpará del cuerpo (se eliminará).");
        separadorLinea();
        mostrarTextoEnNuevaLinea("  --> MEDICINAS: Las hay para cada órgano. Sirven para vacunar tus órganos, mantenerlos a salvo.");
        mostrarTextoEnNuevaLinea("  --> Si se aplican 2 medicinas sobre un órgano, éste se volverá inmune a cualquier virus.");
        separadorLinea();
        mostrarTextoEnNuevaLinea(" --> Si se aplica un virus sobre un órgano que está vacunado, éste eliminará esa vacuna.");
        mostrarTextoEnNuevaLinea(" --> Si se aplica una medicina sobre un órgano que está infectado, éste eliminará ese virus.");
        separadorLinea();
        mostrarTextoEnNuevaLinea(" --> DESCARTAR CARTAS: Durante tu turno, presiona '0' para elegir qué cartas quieres descartar.");
        mostrarTextoEnNuevaLinea(" --> En cada turno tendremos 3 cartas en la mano. Podés jugar una, o descartar hasta las 3 cartas.");
        mostrarTextoEnNuevaLinea(" --> Habrá ocasiones en las que no podrás jugar ninguna carta, te verás obligado a descartar :)");
        separadorLinea();
    }

    @Override
    public void elegirCartasADescartar(){
        String indicesSeparadosConComa = textoUsuario.getText().trim();

        // Separo cada indice con coma
        String[] indicesStr = indicesSeparadosConComa.split(",");

        if(indicesStr.length > 3 || indicesStr.length < 1){
            accionIncorrecta();
        } else {
            // pongo cada indice en un array de ints
            int[] indicesCartasADescartar = new int[indicesStr.length];
            for (int i = 0; i < indicesStr.length; i++) {
                indicesCartasADescartar[i] = Integer.parseInt(indicesStr[i].trim());
            }
            controlador.descartarCartas(indicesCartasADescartar);
            controlador.finDeTurno();
        }
        borrarInputUsuario();

    }

    public void noPuedeJugarEsaCarta(){
        mostrarTextoEnNuevaLinea("No puede jugar esa carta, intente jugar otra o descarte si no tiene opciones.");
    }

    @Override
    public void accionIncorrecta(){
        mostrarTextoEnNuevaLinea("Acción incorrecta. Solo puede accionar 1, 2 o 3 para jugar cartas. 0 para descartar...");
    }

    @Override
    public void vistaInicial(){
        borrarTexto();
        mostrarTextoEnNuevaLinea("¡Bienvenido al juego de cartas VIRUS!");
        mostrarReglas();
        separadorLinea();
        mostrarTablaDeGanadores();
        mostrarTextoEnNuevaLinea("Para iniciar, ingrese su nombre a continuación: ");
        this.accionVista = AccionVista.NUEVO_JUGADOR;
    }

    @Override
    public void avisarEsperaALosDemasJugadores(){
        mostrarTextoEnNuevaLinea("Espere a que los demás jugadores estén listos...");
    }

    @Override
    public void avisarTurno(){
        if (controlador.esSuTurno()){
            mostrarTextoEnNuevaLinea("*** TU TURNO ***");
        } else {
            mostrarTextoEnNuevaLinea("*** TURNO DE " + controlador.getTurnoJugador().getNombre() + " ***");
        }
    }

    @Override
    public void mostrarMesa(){
        borrarTexto();      // Limpia la consola
        mostrarCantidadDeCartasEnMazo();  // Cantidad de cartas en mazo
        mostrarTextoEnMismaLinea("   ||   ");
        mostrarCantidadDeCartasEnMazoDeDescartes();  // Cantidad de cartas en mazo descartes
        separadorLinea();
        separadorBarra();

        mostrarCuerpoRival();           // Cuerpo rival
        separadorLinea();
        separadorBarra();
        mostrarCuerpoJugador();         // Cuerpo jugador
        separadorLinea();
        separadorBarra();
        separadorLinea();
        mostrarCartasManoJugador();     // Cartas mano jugador
        separadorLinea();
        mostrarElegirCarta();           // Opciones de acciones
        separadorLinea();
        avisarTurno();                  // Avisar de quien es el turno
    }


    public void mostrarElegirCarta(){
        if(controlador.esSuTurno()){
            mostrarTextoEnNuevaLinea("   Elige una carta (1, 2 o 3)  ||  Escribe 0 (cero) para descartar hasta 3 cartas");
        }
    }

    @Override
    public void setAccionVista(AccionVista accion){
        this.accionVista = accion;
    }

    @Override
    public void mostrarCantidadDeCartasEnMazo(){
        mostrarTextoEnMismaLinea("   Cantidad de cartas en MAZO: " + controlador.getCantidadDeCartasEnMazo());
    }

    @Override
    public void mostrarCantidadDeCartasEnMazoDeDescartes(){
        mostrarTextoEnNuevaLinea("Cantidad de cartas en MAZO DE DESCARTES: " + controlador.getCantidadDeCartasEnMazoDeDescartes());

    }

    @Override
    public void mostrarCuerpoJugador(){
        separadorLinea();
        mostrarTextoEnNuevaLinea("   ----> CUERPO DE " + controlador.getJugador().getNombre() + " (TÚ) <----");
        String cuerpoJugador = controlador.getCuerpoJugadorToString();
        if(!cuerpoJugador.isEmpty()){
            mostrarTextoEnNuevaLinea("   " + cuerpoJugador);
        } else {
            mostrarTextoEnNuevaLinea("   [ CUERPO VACÍO ]  ");
        }
    }


    @Override
    public void mostrarCartasManoJugador() {
        mostrarTextoEnNuevaLinea("   TU MANO:");
        mostrarTextoEnNuevaLinea("   " + controlador.getJugador().verCartasManoString());
    }

    @Override
    public void mostrarCartasManoRival() {
        separadorLinea();
        mostrarTextoEnNuevaLinea("MANO DE " + controlador.getRival().getNombre() + " (RIVAL)");
        mostrarTextoEnNuevaLinea("  [Carta 1]  ||  [Carta 2]  ||  [Carta 3]  ");
    }

    @Override
    public void mostrarCuerpoRival() {
        separadorLinea();
        mostrarTextoEnNuevaLinea("   CUERPO DE " + controlador.getRival().getNombre() + " (RIVAL)");
        String cuerpoRival = controlador.getCuerpoRivalToString();
        if (!cuerpoRival.isEmpty()) {
            mostrarTextoEnNuevaLinea("   " + cuerpoRival);
        } else {
            mostrarTextoEnNuevaLinea("   [ CUERPO VACÍO ]  ");
        }
    }

    @Override
    public void ingresarNuevoJugador(){
        String nombre = textoUsuario.getText().trim();
        controlador.nuevoJugador(nombre);
        borrarInputUsuario();
    }

    private String getNombreJugadorControlador(){
        return controlador.getJugador().getNombre();
    }

    /* Muestra cierto texto en una nueva linea, equivalente a System.out.println() */
    private void mostrarTextoEnNuevaLinea(String texto){
        areaTextoConsola.append(texto + "\n");
    }

    /* Muestra cierto texto en una misma linea, equivalente a System.out.print() */
    private void mostrarTextoEnMismaLinea(String texto){
        areaTextoConsola.append(texto);
    }

    /* Separador de texto con una linea, luego crea una nueva linea vacía */
    private void separadorLinea(){
        for (int i = 0; i < panel.size().width/4; i++) {
            mostrarTextoEnMismaLinea("-");
        }
        mostrarTextoEnNuevaLinea("");
    }

    private void separadorBarra(){
        mostrarTextoEnMismaLinea(" ");
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < panel.size().width/13; i++) {
                mostrarTextoEnMismaLinea(" = ");
            }
            mostrarTextoEnNuevaLinea("");
            mostrarTextoEnMismaLinea(" ");
        }
    }


    /* Limpia toda la consola, elimina el texto */
    private void borrarTexto(){
        areaTextoConsola.setText("");
    }

    private void borrarInputUsuario(){
        textoUsuario.setText("");
    }

    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public AccionVista getAccionVista(){
        return this.accionVista;
    }

}
