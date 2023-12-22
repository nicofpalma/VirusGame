package virus.game.vistas.grafica;

import virus.game.controladores.Controlador;
import virus.game.modelos.Carta;
import virus.game.vistas.AccionVista;
import virus.game.vistas.IVista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class VistaGrafica extends JFrame implements IVista {
    private AccionVista accionVista;
    private Controlador controlador;
    private JPanel panelCartasJugador;
    private JTextArea textoBienvenida;
    private JTextField campoNombre;

    private JPanel panelIngresoNombre;
    private JButton botonConfirmar;

    public VistaGrafica(){
        setTitle("Virus - Vista Grafica");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        campoNombre = new JTextField();
        campoNombre.setColumns(15);
        botonConfirmar = new JButton("Confirmar");

        panelIngresoNombre = new JPanel();
        panelIngresoNombre.add(new JLabel("Nombre: "));
        panelIngresoNombre.add(campoNombre);
        panelIngresoNombre.add(botonConfirmar);

        add(panelIngresoNombre, BorderLayout.SOUTH);
        botonConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (accionVista){
                    case NUEVO_JUGADOR: {
                        ingresarNuevoJugador();
                        break;
                    }
                    case ESPERAR_TURNO:{
                        break;
                    }
                }
            }
        });

    }

    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void mostrarCartasManoJugador() {
        if(panelCartasJugador == null){
            panelCartasJugador = new JPanel();
            panelCartasJugador.setVisible(true);
            add(panelCartasJugador, BorderLayout.SOUTH);


        }
        // Recorro la mano del jugador, y por cada carta, obtengo su imagen y la agrego al panel de la mano para mostrarlas
        ArrayList<Carta> mano = controlador.getManoJugador();

        JPanel panelDeLaMano = new JPanel();
        panelDeLaMano.setLayout(new FlowLayout());

        for (int i = 0; i < mano.size(); i++) {
            ImageIcon imagenCarta = mano.get(i).getImagen();
            JLabel labelCarta = new JLabel(imagenCarta);

            labelCarta.setAlignmentX(CENTER_ALIGNMENT);
            labelCarta.setToolTipText(mano.get(i).getNombre());  // muestra el nombre de la carta al posicionar el mouse encima
            panelDeLaMano.add(labelCarta);
        }

        // Borro la visualizacion de las cartas que había anteriormente
        if(panelCartasJugador != null){
            panelCartasJugador.removeAll();
        }

        // Muestro las nuevas cartas
        panelCartasJugador.add(panelDeLaMano);

        revalidate();
        repaint();
    }

    @Override
    public void vistaInicial() {
        //mostrarMensaje("¡Bienvenido a Virus!");
        mostrarBienvenida();
        this.accionVista = AccionVista.NUEVO_JUGADOR;

    }

    private void mostrarBienvenida(){
        textoBienvenida = new JTextArea("¡Bienvenido a Virus Game!");
        textoBienvenida.setEditable(false);
        textoBienvenida.setLineWrap(true);
        textoBienvenida.setWrapStyleWord(true);

        add(textoBienvenida, BorderLayout.CENTER);

        textoBienvenida.append("---------------------------------------------------------------------\n");
        textoBienvenida.append("  OBJETIVO Y REGLAS DEL JUEGO\n");
        textoBienvenida.append("  --> PARA GANAR: Tener en tu cuerpo (tu mesa) las 4 cartas de órganos diferentes, sin ninguna infección.\n");
        textoBienvenida.append("  --> ORGANOS: Cerebro, Corazón, Estómago y Hueso\n");
        textoBienvenida.append("---------------------------------------------------------------------\n");
        textoBienvenida.append("  --> VIRUS: Los hay para cada órgano. Sirven para infectar los órganos del rival e impedir que éste gane.\n");
        textoBienvenida.append("  --> Si se aplican 2 virus sobre un órgano, éste se extirpará del cuerpo (se eliminará).\n");
        textoBienvenida.append("---------------------------------------------------------------------\n");

        textoBienvenida.append("  --> MEDICINAS: Las hay para cada órgano. Sirven para vacunar tus órganos, mantenerlos a salvo.\n");
        textoBienvenida.append("  --> Si se aplican 2 medicinas sobre un órgano, éste se volverá inmune a cualquier virus.\n");
        textoBienvenida.append("---------------------------------------------------------------------\n");

        textoBienvenida.append(" --> Si se aplica un virus sobre un órgano que está vacunado, éste eliminará esa vacuna.\n");
        textoBienvenida.append(" --> Si se aplica una medicina sobre un órgano que está infectado, éste eliminará ese virus.\n");
        textoBienvenida.append("---------------------------------------------------------------------\n");
        textoBienvenida.append(" --> DESCARTAR CARTAS: Durante tu turno, presiona '0' para elegir qué cartas quieres descartar.\n");
        textoBienvenida.append(" --> En cada turno tendremos 3 cartas en la mano. Podés jugar una, o descartar hasta las 3 cartas.\n");
        textoBienvenida.append(" --> Habrá ocasiones en las que no podrás jugar ninguna carta, te verás obligado a descartar :)\n");
        textoBienvenida.append("---------------------------------------------------------------------\n");

        textoBienvenida.append("Para iniciar, ingrese su nombre en el input que está debajo: \n");
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    @Override
    public void ingresarNuevoJugador() {
        String nombre = campoNombre.getText().trim();
        controlador.nuevoJugador(nombre);
        campoNombre.setText("");
    }

    @Override
    public void setAccionVista(AccionVista accion) {
        this.accionVista = accion;
    }

    @Override
    public void mostrarCartasManoRival() {

    }

    @Override
    public void mostrarCuerpoRival() {

    }

    @Override
    public void mostrarCuerpoJugador() {

    }

    @Override
    public void mostrarCantidadDeCartasEnMazo() {

    }

    @Override
    public void mostrarCantidadDeCartasEnMazoDeDescartes() {

    }

    @Override
    public void mostrarMesa() {
        System.out.println("Entré a mostrar mesa");
        // Elimina todo el panel de ingreso de nombre, para poder empezar a jugar y mostrar las cartas.
        if(panelIngresoNombre != null){
            getContentPane().remove(campoNombre);
            getContentPane().remove(botonConfirmar);
            getContentPane().remove(textoBienvenida);
            getContentPane().remove(panelIngresoNombre);
            revalidate();
            repaint();
        }
        mostrarCartasManoJugador();

    }

    @Override
    public void avisarEsperaALosDemasJugadores() {
        textoBienvenida.append("Espere a que los demás jugadores estén listos...\n");
    }

    @Override
    public void avisarTurno() {
        if(controlador.esSuTurno()){
            System.out.println("Es tu turno");
        } else
        {
            System.out.println("NO es tu turno");
        }
    }

    @Override
    public void jugadorRealizaUnaAccion() {

    }

    @Override
    public void accionIncorrecta() {

    }

    @Override
    public AccionVista getAccionVista() {
        return this.accionVista;
    }

    @Override
    public void elegirCartasADescartar() {

    }

    @Override
    public void avisarQueElJugadorGano() {

    }

    @Override
    public void avisarQueElJugadorPerdio() {

    }

    @Override
    public void mostrarTablaDeGanadores() {

    }

    @Override
    public void mostrarReglas() {

    }
}
