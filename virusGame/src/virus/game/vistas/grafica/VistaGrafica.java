package virus.game.vistas.grafica;

import virus.game.controladores.Controlador;
import virus.game.modelos.Carta;
import virus.game.modelos.cartas.Organo;
import virus.game.vistas.AccionVista;
import virus.game.vistas.IVista;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class VistaGrafica extends JFrame implements IVista {
    private AccionVista accionVista;
    private Controlador controlador;
    private JPanel panelManoJugador;
    private JPanel panelCuerpoJugador;
    private JPanel panelCuerpoRival;
    private JPanel panelMazo;
    private JPanel panelMazoDeDescartes;
    private JTextArea textoBienvenida;
    private JTextField campoNombre;
    private JPanel panelIngresoNombre;
    private JButton botonConfirmar;
    private JPanel panelTextoInformativo;
    private JButton botonDescartar;
    private JButton botonJugar;
    private JPanel panelTextoTurno;
    private JLayeredPane capas;
    private JLabel cartaSeleccionada = null;
    private JCheckBox[] checkBoxCartas;
    private JPanel panelDeCheckbox;

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
                    default:
                        break;
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
        if(panelManoJugador == null){
            // Crea el panel de las cartas del jugador (la mano), sólo cuando no está creado todavía
            panelManoJugador = new JPanel();
            panelManoJugador.setSize(350, 250);
            panelManoJugador.setVisible(true);
            panelManoJugador.setOpaque(false);

            // Lo agrego a las capas, en la capa de PALLETE_LAYER, que es la capa superior al fondo.
            capas.add(panelManoJugador, JLayeredPane.PALETTE_LAYER);
        } else {
            panelManoJugador.removeAll();
        }

        panelManoJugador.setLocation(210, 420);
        // Recorro la mano del jugador, y por cada carta, obtengo su imagen y la agrego al panel de la mano para mostrarlas
        ArrayList<Carta> mano = controlador.getManoJugador();

        // Contenedor propio de las cartas de la mano
        JPanel panelDeLaMano = new JPanel();
        panelDeLaMano.setOpaque(false);
        panelDeLaMano.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 50));

        for (int i = 0; i < mano.size(); i++) {
            Carta carta = mano.get(i);
            ImageIcon imagenCarta = carta.getImagen();
            JLabel labelCarta = new JLabel(imagenCarta);
            labelCarta.setOpaque(false);

            // Identificador de carta para poder jugarlas, 1, 2, 3
            labelCarta.setName(String.valueOf(i+1));

            // Hover para mostrar el nombre de la carta
            //labelCarta.setToolTipText(carta.getNombre() + " (" + carta.getClass().getSimpleName() + ")");  // muestra el nombre de la carta al posicionar el mouse encima

            // Eventos del mouse para las cartas de la mano
            labelCarta.addMouseListener(new MouseAdapter() {
                private int ubicacionY;
                @Override
                public void mouseEntered(MouseEvent e){
                    labelCarta.setLocation(labelCarta.getLocation().x, labelCarta.getLocation().y - 50);
                    ubicacionY = labelCarta.getLocation().y;
                    setCursor(Cursor.HAND_CURSOR);
                }

                @Override
                public void mouseExited(MouseEvent e){
                    labelCarta.setLocation(labelCarta.getLocation().x, ubicacionY + 50);
                    setCursor(Cursor.DEFAULT_CURSOR);
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    switch (accionVista){
                        case ESPERAR_TURNO:{
                            mostrarTextoInformativo("No es tu turno, espera...");

                            break;
                        }
                        case TURNO_JUGADOR:{
                            if(cartaSeleccionada != null && cartaSeleccionada.equals(labelCarta)){
                                cartaSeleccionada = null;
                                botonJugar.setVisible(false);
                                labelCarta.setBorder(null);
                                //mostrarTextoInformativo("Juega una carta haciendo click en ella, o descarta si no tienes opciones");
                            } else {
                                if(cartaSeleccionada != null){
                                    // Deselecciona la carta anterior
                                    //cartaSeleccionada.setLocation(cartaSeleccionada.getLocation().x, ubicacionY);
                                    cartaSeleccionada.setBorder(null);
                                }

                                // Selecciona la carta actual
                                cartaSeleccionada = labelCarta;
                                //labelCarta.setLocation(labelCarta.getLocation().x, ubicacionY - 20);
                                //mostrarTextoInformativo("Carta seleccionada : " + cartaSeleccionada.getToolTipText());
                                labelCarta.setBorder(BorderFactory.createLineBorder(Color.white, 2));
                                botonJugar.setVisible(true);
                            }
                            break;
                        }
                        default:{
                            break;
                        }
                    }
                }
            });
            panelDeLaMano.add(labelCarta);
        }

        // Muestro las nuevas cartas
        panelManoJugador.add(panelDeLaMano, BorderLayout.CENTER);
    }

    @Override
    public void vistaInicial() {
        mostrarBienvenida();
        this.accionVista = AccionVista.NUEVO_JUGADOR;
    }

    private void mostrarTextoInformativo(String texto){
        if(panelTextoInformativo == null){
            panelTextoInformativo = new JPanel();
            panelTextoInformativo.setSize(500, 100);
            panelTextoInformativo.setVisible(true);
            panelTextoInformativo.setOpaque(false);
            capas.add(panelTextoInformativo, JLayeredPane.POPUP_LAYER);
        } else {
            panelTextoInformativo.removeAll();
        }

        JLabel labelTexto = new JLabel(texto);
        labelTexto.setForeground(Color.white);

        panelTextoInformativo.add(labelTexto, BorderLayout.CENTER);
        panelTextoInformativo.setLocation(150, 15);
        panelTextoInformativo.revalidate();
        panelTextoInformativo.repaint(300);
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
        // Obtengo los organos del cuerpod del rival
        ArrayList<Organo> organosCuerpoRival = controlador.getOrganosDelCuerpoDelRival();
        if(!organosCuerpoRival.isEmpty()){
            if(panelCuerpoRival == null){
                panelCuerpoRival = new JPanel();
                panelCuerpoRival.setSize(520, 180);
                panelCuerpoRival.setVisible(true);
                panelCuerpoRival.setOpaque(false);
                capas.add(panelCuerpoRival, JLayeredPane.PALETTE_LAYER);
            } else {
                panelCuerpoRival.removeAll();
            }

            JPanel panelDelCuerpo = new JPanel();
            panelDelCuerpo.setOpaque(false);
            panelDelCuerpo.setLayout(new FlowLayout(FlowLayout.CENTER, 30 ,0));

            for(Organo organo : organosCuerpoRival){
                ImageIcon imagenOrgano = organo.getImagen();
                JLabel labelOrgano = new JLabel(imagenOrgano);
                labelOrgano.setLayout(new BoxLayout(labelOrgano, BoxLayout.X_AXIS));

                //labelOrgano.setToolTipText(organo.getNombre() + " (" + organo.getClass().getSimpleName() + " )");
                panelDelCuerpo.add(labelOrgano);

                if(organo.estaInfectado()){
                    Image imagenInfeccion = organo.getInfecciones().get(0).getImagen()
                            .getImage()
                            .getScaledInstance(60, 90, Image.SCALE_SMOOTH);

                    JLabel labelInfeccion = new JLabel(new ImageIcon(imagenInfeccion));
                    labelInfeccion.setVisible(true);
                    labelInfeccion.setOpaque(false);
                    labelInfeccion.setAlignmentY(2.0f);

                    labelOrgano.add(labelInfeccion);
                } else {
                    if(organo.tieneMedicina()){
                        Image imagenMedicina = organo.getMedicinas().get(0).getImagen()
                                .getImage()
                                .getScaledInstance(60, 90, Image.SCALE_SMOOTH);

                        JLabel labelMedicina = new JLabel(new ImageIcon(imagenMedicina));
                        labelMedicina.setVisible(true);
                        labelMedicina.setOpaque(false);
                        labelMedicina.setAlignmentY(2.0f);

                        labelOrgano.add(labelMedicina);
                    }
                }
            }
            panelCuerpoRival.setLocation(160, 35);
            panelCuerpoRival.add(panelDelCuerpo);
        }
    }

    @Override
    public void mostrarCuerpoJugador() {
        // Obtengo los organos del cuerpo del jugador
        ArrayList<Organo> organosCuerpoJugador = controlador.getOrganosDelCuerpoDelJugador();
        if(!organosCuerpoJugador.isEmpty()){
            if(panelCuerpoJugador == null){
                // Creo el panel del cuerpo del jugador si no estaba creado anteriormente
                panelCuerpoJugador = new JPanel();
                panelCuerpoJugador.setSize(520, 180);
                panelCuerpoJugador.setVisible(true);
                panelCuerpoJugador.setOpaque(false);
                capas.add(panelCuerpoJugador, JLayeredPane.PALETTE_LAYER);
            } else {
                panelCuerpoJugador.removeAll();
            }

            // Contenedor propio de los organos del cuerpo
            JPanel panelDelCuerpo = new JPanel();
            panelDelCuerpo.setOpaque(false);

            // Creo un flowlayout para que las cartas se muestren separadas entre si en una misma fila
            panelDelCuerpo.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));

            for(Organo organo : organosCuerpoJugador){
                ImageIcon imagenOrgano = organo.getImagen();
                JLabel labelOrgano = new JLabel(imagenOrgano);
                // Le doy un boxlayout al label del organo, para poder mostrar su infeccion o medicina
                labelOrgano.setLayout(new BoxLayout(labelOrgano, BoxLayout.X_AXIS));

                // Hover para mostrar el nombre del organo
                //labelOrgano.setToolTipText(organo.getNombre() + " (" + organo.getClass().getSimpleName() + " )");
                panelDelCuerpo.add(labelOrgano);

                if(organo.estaInfectado()) {
                    Image imagenInfeccion = organo.getInfecciones().get(0).getImagen().
                            getImage().
                            getScaledInstance(60, 90, Image.SCALE_SMOOTH);

                    JLabel labelInfeccion = new JLabel(new ImageIcon(imagenInfeccion));
                    labelInfeccion.setVisible(true);
                    labelInfeccion.setOpaque(false);
                    labelInfeccion.setAlignmentY(2.0f);

                    labelOrgano.add(labelInfeccion);
                } else {
                    if(organo.tieneMedicina()){
                        // Agregeo la carta de medicina sobre la carta del organo
                        Image imagenMedicina = organo.getMedicinas().get(0).getImagen()
                                .getImage()
                                .getScaledInstance(60, 90, Image.SCALE_SMOOTH);
                        JLabel labelMedicina = new JLabel(new ImageIcon(imagenMedicina));
                        labelMedicina.setVisible(true);
                        labelMedicina.setOpaque(false);
                        labelMedicina.setAlignmentY(2.0f);

                        labelOrgano.add(labelMedicina);
                    }
                }
            }

            panelCuerpoJugador.setLocation(160, 240);
            panelCuerpoJugador.add(panelDelCuerpo);
        }

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
            panelIngresoNombre = null;
            revalidate();
            repaint();

            // Creación de algunos elementos esenciales para la vista gráfica y control de eventos.
            iniciarVistaYControlarEventos();
        }

        mostrarCartasManoJugador();
        mostrarCuerpoJugador();
        mostrarCuerpoRival();
        mostrarMazo();
        mostrarMazoDeDescartes();
        avisarTurno();
        mostrarBotonDescartar();

        // Borra el estado de la vista anterior y lo actualiza con lo nuevo
        capas.revalidate();
        capas.repaint();
    }

    public void iniciarVistaYControlarEventos(){
        // Agrego capas para poder manejar varios elementos simultaneos
        capas = new JLayeredPane();
        //capas.setPreferredSize(new Dimension(800, 600));
        capas.setSize(800, 600);

        // Agrego una imágen de fondo
        ImageIcon background = new ImageIcon("./src/virus/game/modelos/cartas/img/fondo.jpg");
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());

        capas.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
        capas.setVisible(true);
        add(capas);

        // Creacion de los botones para jugar
        botonDescartar = new JButton();
        botonDescartar.setText("Descartar");
        botonDescartar.setSize(100, 30);
        botonDescartar.setVisible(false);
        botonDescartar.setLocation(630, 435);

        botonDescartar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (accionVista){
                    case TURNO_JUGADOR:{
                        mostrarTextoInformativo("Seleccione las cartas a descartar");
                        botonDescartar.setText("Confirmar");
                        panelDeCheckbox.setVisible(true);
                        setAccionVista(AccionVista.DESCARTAR_CARTAS);
                        break;
                    }
                    case DESCARTAR_CARTAS:{
                        // Obtengo los indices de las cartas a descartar según los checkboxes seleccionados
                        ArrayList<Integer> listaDeCartasADescartar = new ArrayList<>();
                        for (int i = 0; i < checkBoxCartas.length; i++) {
                            if(checkBoxCartas[i].isSelected()){
                                // Añado los índices de los checkboxes
                                listaDeCartasADescartar.add(i+1);
                            }
                        }
                        // Transformo los índices a un array común para enviarselos al controlador
                        int[] indicesDeCartasADescartar = new int[listaDeCartasADescartar.size()];
                        for (int i = 0; i < listaDeCartasADescartar.size(); i++) {
                            indicesDeCartasADescartar[i] = listaDeCartasADescartar.get(i);
                        }

                        // Descarto las cartas que pidió descartar el usuario.
                        controlador.descartarCartas(indicesDeCartasADescartar);
                        for (JCheckBox checkBoxCarta : checkBoxCartas) {
                            checkBoxCarta.setSelected(false);
                        }
                        mostrarTextoInformativo("");
                        panelDeCheckbox.setVisible(false);
                        controlador.finDeTurno();

                        botonDescartar.setText("Descartar");
                        break;
                    }
                }
            }
        });

        botonJugar = new JButton();
        botonJugar.setText("Jugar");
        botonJugar.setSize(100, 30);
        botonJugar.setVisible(false);
        botonJugar.setLocation(630, 475);

        botonJugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jugadorRealizaUnaAccion();
            }
        });

        capas.add(botonJugar, JLayeredPane.POPUP_LAYER);
        capas.add(botonDescartar, JLayeredPane.POPUP_LAYER);
    }

    // Muestra el boton para descartar cartas solo cuando es su turno
    // Y los checkboxes asociados para descartar
    public void mostrarBotonDescartar() {
        if(checkBoxCartas == null){
            checkBoxCartas = new JCheckBox[3];
            for (int i = 0; i < checkBoxCartas.length; i++) {
                checkBoxCartas[i] = new JCheckBox();
                checkBoxCartas[i].setVisible(true);
            }
            panelDeCheckbox = new JPanel();
            panelDeCheckbox.setOpaque(false);
            panelDeCheckbox.setLayout(new FlowLayout(FlowLayout.CENTER, 85, 30));
            panelDeCheckbox.add(checkBoxCartas[0], BorderLayout.CENTER);
            panelDeCheckbox.add(checkBoxCartas[1], BorderLayout.CENTER);
            panelDeCheckbox.add(checkBoxCartas[2], BorderLayout.CENTER);
            panelDeCheckbox.setSize(500, 200);
            panelDeCheckbox.setLocation(138, 510);
            panelDeCheckbox.setVisible(false);

            capas.add(panelDeCheckbox, JLayeredPane.POPUP_LAYER);
        }
        botonDescartar.setVisible(controlador.esSuTurno());
    }

    @Override
    public void avisarEsperaALosDemasJugadores() {
        textoBienvenida.append("Espere a que los demás jugadores estén listos...\n");
    }

    // Muestra el mazo
    public void mostrarMazo(){
        // Si el panel del mazo todavía no se creó, se crea aquí por primera vez
        if(panelMazo == null){
            panelMazo = new JPanel();
            panelMazo.setSize(140, 200);
            panelMazo.setOpaque(false);
            panelMazo.setVisible(true);
            capas.add(panelMazo, JLayeredPane.PALETTE_LAYER);
        } else {
            panelMazo.removeAll();
        }

        // Creo un panel dentro del panel del mazo, para mostrar las cartas
        // Uso boxLayout para poder controlar el desplazamiento de cada una de las cartas
        int cantidadDeCartasEnMazo = controlador.getCantidadDeCartasEnMazo();
        JPanel panelCartasDelMazo = new JPanel();
        panelCartasDelMazo.setOpaque(false);
        panelCartasDelMazo.setLayout(new BoxLayout(panelCartasDelMazo, BoxLayout.Y_AXIS));

        JLabel labelTexto = new JLabel();
        labelTexto.setText(cantidadDeCartasEnMazo + " cartas");
        labelTexto.setForeground(Color.white);

        labelTexto.setBorder(BorderFactory.createEmptyBorder(0, 15, 0,0));
        Font fuente = labelTexto.getFont();
        labelTexto.setFont(new Font(fuente.getName(), Font.BOLD, fuente.getSize() + 5));

        panelCartasDelMazo.add(labelTexto);

        // Obtengo la imágen del dorso de las cartas
        Image imagenDorso = new ImageIcon("./src/virus/game/modelos/cartas/img/dorso.png")
                .getImage()
                .getScaledInstance(100, 140, Image.SCALE_SMOOTH);

        if(cantidadDeCartasEnMazo >= 4) cantidadDeCartasEnMazo = cantidadDeCartasEnMazo / 4;
        for (int i = 0; i < cantidadDeCartasEnMazo; i++) {
            // Creo la imagen del dorso de las cartas
            // De modo que cada carta esté una abajo de otra
            JLabel labelDorso = new JLabel(new ImageIcon(imagenDorso));
            labelDorso.setBorder(BorderFactory.createEmptyBorder(0, i, 0, 0));

            panelCartasDelMazo.add(labelDorso);

            if(i < (cantidadDeCartasEnMazo) - 1){
                panelCartasDelMazo.add(Box.createRigidArea(new Dimension(0,-138)));

            }
        }

        //if(panelMazo != null){
        //    panelMazo.removeAll();
        //}

        //panelCartasDelMazo.setToolTipText("Mazo (" + cantidadDeCartasEnMazo + ")");
        panelMazo.setLocation(0 ,200);
        panelMazo.add(panelCartasDelMazo);
    }

    // Muestra el mazo de descartes
    public void mostrarMazoDeDescartes(){
        ArrayList<Carta> cartasDelMazoDeDescartes = controlador.getCartasDelMazoDeDescartes();

        if(!cartasDelMazoDeDescartes.isEmpty()) {
            if(panelMazoDeDescartes == null){
                panelMazoDeDescartes = new JPanel();
                panelMazoDeDescartes.setSize(140, 200);
                panelMazoDeDescartes.setVisible(true);
                panelMazoDeDescartes.setOpaque(false);
                capas.add(panelMazoDeDescartes, JLayeredPane.PALETTE_LAYER);
            } else {
                panelMazoDeDescartes.removeAll();
            }

            JPanel panelCartasDelMazoDeDescartes = new JPanel();
            panelCartasDelMazoDeDescartes.setOpaque(false);
            panelCartasDelMazoDeDescartes.setVisible(true);
            panelCartasDelMazoDeDescartes.setLayout(new BoxLayout(panelCartasDelMazoDeDescartes, BoxLayout.Y_AXIS));

            int cantidadDeCartas = cartasDelMazoDeDescartes.size();
            if(cantidadDeCartas >= 3) cantidadDeCartas = cantidadDeCartas / 3;

            for (int i = 0; i < cantidadDeCartas; i++) {
                    ImageIcon imagenCarta = cartasDelMazoDeDescartes.get(i).getImagen();
                    JLabel labelCarta = new JLabel(imagenCarta);

                    labelCarta.setBorder(BorderFactory.createEmptyBorder(0, i, 0, 0));

                    panelCartasDelMazoDeDescartes.add(labelCarta);
                    if(i < (cantidadDeCartas) - 1){
                        panelCartasDelMazoDeDescartes.add(Box.createRigidArea(new Dimension(0, -138)));
                    }
            }

            //if(panelMazoDeDescartes != null){
            //    panelMazoDeDescartes.removeAll();
            //}

            //panelCartasDelMazoDeDescartes.setToolTipText("Mazo de descartes (" + cartasDelMazoDeDescartes.size() + ")");
            panelMazoDeDescartes.setLocation(0, 400);
            panelMazoDeDescartes.add(panelCartasDelMazoDeDescartes);
        }
    }

    @Override
    public void avisarTurno() {
        if(panelTextoTurno == null){
            panelTextoTurno = new JPanel();
            panelTextoTurno.setSize(200, 100);
            panelTextoTurno.setVisible(true);
            panelTextoTurno.setOpaque(false);
            panelTextoTurno.setLocation(300, 0);
            capas.add(panelTextoTurno, JLayeredPane.POPUP_LAYER);
        } else {
            panelTextoTurno.removeAll();
        }
        JLabel labelTexto = new JLabel();
        labelTexto.setForeground(Color.white);
        panelTextoTurno.add(labelTexto);

        if(controlador.esSuTurno()){
            labelTexto.setText("*** TU TURNO ***");
            mostrarTextoInformativo("Juega una carta haciendo click en ella, o descarta si no tienes opciones");
        } else {
            labelTexto.setText("*** TURNO DE " + controlador.getTurnoJugador().getNombre() + " ***");
            mostrarTextoInformativo("Espera a que tu rival juegue su turno");
        }
    }

    @Override
    public void jugadorRealizaUnaAccion() {
        int numCarta = Integer.parseInt(cartaSeleccionada.getName());
        if(controlador.accionarCarta(numCarta)){
            controlador.finDeTurno();
        } else {
            //mostrarMesa();
            mostrarTextoInformativo("No puedes jugar esa carta, intenta con otra.");
        }
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
