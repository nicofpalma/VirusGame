package virus.game.vistas.consola;

import virus.game.controladores.Controlador;
import virus.game.vistas.AccionVista;
import virus.game.vistas.IVista;

import javax.swing.*;
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
                    default: {
                        break;
                    }
                }
            }
        });

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
            controlador.descartarCartasJugador(indicesCartasADescartar);
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
        mostrarTextoEnNuevaLinea("Juego de cartas VIRUS");
        mostrarTextoEnNuevaLinea("--- Inicio del juego ---");
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
        borrarTexto();
        mostrarCantidadDeCartasEnMazo();
        mostrarTextoEnMismaLinea("   ||   ");
        mostrarCantidadDeCartasEnMazoDeDescartes();

        //mostrarCartasManoRival();
        mostrarCuerpoRival();
        separadorLinea();
        mostrarTextoEnNuevaLinea("");
        mostrarTextoEnNuevaLinea("");
        mostrarCuerpoJugador();
        separadorLinea();
        mostrarTextoEnNuevaLinea("");
        mostrarTextoEnNuevaLinea("");

        separadorLinea();
        separadorLinea();
        mostrarElegirCarta();
        mostrarCartasManoJugador();
        separadorLinea();
        separadorLinea();
        avisarTurno();
    }

    public void mostrarElegirCarta(){
        if(controlador.esSuTurno()){
            mostrarTextoEnNuevaLinea("Elige una carta (1, 2 o 3)  ||  Escribe 0 (cero) para descartar hasta 3 cartas");
        }
    }

    @Override
    public void setAccionVista(AccionVista accion){
        this.accionVista = accion;
    }

    @Override
    public void mostrarCantidadDeCartasEnMazo(){
        mostrarTextoEnMismaLinea("Cantidad de cartas en MAZO: " + controlador.getCantidadDeCartasEnMazo());
    }

    @Override
    public void mostrarCantidadDeCartasEnMazoDeDescartes(){
        mostrarTextoEnNuevaLinea("Cantidad de cartas en MAZO DE DESCARTES: " + controlador.getCantidadDeCartasEnMazoDeDescartes());

    }

    @Override
    public void mostrarCuerpoJugador(){
        separadorLinea();
        mostrarTextoEnNuevaLinea("CUERPO DE " + controlador.getJugador().getNombre() + " (TÚ)");
        String cuerpoJugador = controlador.getCuerpoJugadorToString();
        if(!cuerpoJugador.isEmpty()){
            mostrarTextoEnNuevaLinea(cuerpoJugador);
        } else {
            mostrarTextoEnNuevaLinea("*** CUERPO VACÍO ***");
        }
    }


    @Override
    public void mostrarCartasManoJugador() {
        mostrarTextoEnNuevaLinea("TU MANO:");
        mostrarTextoEnNuevaLinea(controlador.getJugador().verCartasMano());
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
        mostrarTextoEnNuevaLinea("CUERPO DE " + controlador.getRival().getNombre() + " (RIVAL)");
        String cuerpoRival = controlador.getCuerpoRivalToString();
        if (!cuerpoRival.isEmpty()) {
            mostrarTextoEnNuevaLinea(cuerpoRival);
        } else {
            mostrarTextoEnNuevaLinea("*** CUERPO VACÍO ***");
        }
    }

    private void ingresarNuevoJugador(){
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
