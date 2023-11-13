package virus.game.vistas.consola;

import virus.game.controladores.Controlador;
import virus.game.vistas.AccionVista;
import virus.game.vistas.IVista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                        mostrarTextoEnNuevaLinea("Espere a que los jugadores estén listos...");
                        break;
                    }
                    default: {
                        break;
                    }
                    //case MOSTRAR_MESA:{   // Muestra la mesa y permite jugar una carta
                    //    mostrarMesa();
                    //    break;
                    //}
                }
            }
        });

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
    }

    public void mostrarElegirCarta(){
        mostrarTextoEnNuevaLinea("Elige una carta (1, 2 o 3)  ||  Escribe 0 (cero) para descartar hasta 3 cartas");
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
        avisarEsperaALosDemasJugadores();
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



}
