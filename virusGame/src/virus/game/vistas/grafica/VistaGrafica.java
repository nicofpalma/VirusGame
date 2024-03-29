package virus.game.vistas.grafica;

import virus.game.controladores.Controlador;
import virus.game.modelos.Carta;
import virus.game.modelos.cartas.Organo;
import virus.game.utils.GuiConstants;
import virus.game.vistas.AccionVista;
import virus.game.vistas.IVista;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class VistaGrafica extends JFrame implements IVista, Serializable {
    @Serial
    private static final long serialVersionUID = -1109397795690209052L;
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
    private JButton botonCancelarDescarte;
    private JButton botonJugar;
    private JPanel panelTextoTurno;
    private JLabel textoTurno;
    private JLayeredPane capas;
    private JLabel cartaSeleccionada = null;
    private ArrayList<JLabel> cartasSeleccionadasParaDescartar;
    private JLabel textoNombreJugador;
    private JLabel textoNombreRival;


    /**
     * Constructor de la vista gráfica
     * */
    public VistaGrafica() {
        setTitle("Virus - El juego de cartas más contagioso");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(GuiConstants.ANCHO_VENTANA, GuiConstants.ALTO_VENTANA);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

        // Colores del tooltip, texto que se muestra al poner el mouse arriba de una carta por ejemplo
        UIManager.put("ToolTip.background", Color.BLACK);
        UIManager.put("ToolTip.foreground", Color.WHITE);
        Border border = BorderFactory.createLineBorder(GuiConstants.COLOR_VIOLETA); // Borde violeta del tooltip
        UIManager.put("ToolTip.border", border);

        campoNombre = new JTextField();
        campoNombre.setColumns(15);
        botonConfirmar = new JButton("Confirmar");

        panelIngresoNombre = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIngresoNombre.add(new JLabel("Nombre: "));
        panelIngresoNombre.add(campoNombre);
        panelIngresoNombre.add(botonConfirmar);

        JLabel labelEspacio = new JLabel("                                  ");
        panelIngresoNombre.add(labelEspacio);

        JButton botonCargarPartida = new JButton("Cargar partida");
        panelIngresoNombre.add(botonCargarPartida);
        botonCargarPartida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(controlador.hayPartidaGuardada()){
                    int respuesta = JOptionPane.showConfirmDialog(
                            null,
                            "¿Desea reanudar la partida de " + controlador.nombreJugadoresEnPartidaGuardada() + "?", "Reanudar partida",
                            JOptionPane.YES_NO_OPTION
                    );

                    if(respuesta == JOptionPane.YES_OPTION){
                        controlador.cargarPartidaGuardada();
                        mostrarMesa();
                    }
                } else {
                    textoBienvenida.append("No hay ninguna partida para reanudar.\n");
                }

            }
        });

        JButton botonReglas = new JButton("Ver reglas");
        panelIngresoNombre.add(botonReglas);

        botonReglas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarReglas();
            }
        });

        JButton botonGanadores = new JButton("Ver ganadores");
        panelIngresoNombre.add(botonGanadores);

        botonGanadores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarTablaDeGanadores();
            }
        });

        add(panelIngresoNombre, BorderLayout.SOUTH);

        botonConfirmar.addActionListener(e -> {
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
            panelManoJugador.setSize(GuiConstants.SIZE_MANO_JUGADOR_X, GuiConstants.SIZE_MANO_JUGADOR_Y);
            panelManoJugador.setVisible(true);
            panelManoJugador.setOpaque(false);

            // Lo agrego a las capas, en la capa de PALLETE_LAYER, que es la capa superior al fondo.
            capas.add(panelManoJugador, JLayeredPane.PALETTE_LAYER);
        } else {
            panelManoJugador.removeAll();
        }

        panelManoJugador.setLocation(GuiConstants.LOC_MANO_JUGADOR_X, GuiConstants.LOC_MANO_JUGADOR_Y);
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

            // Hover con el nombre de la carta
            // Se usa <html></html> para poder usar un salto de línea (<br>), ya que "\n" no funciona en el tooltip.
            labelCarta.setToolTipText("<html>" + carta + "<br>" + carta.getDescripcion() + "</html>");

            // Eventos del mouse para las cartas de la mano
            labelCarta.addMouseListener(new MouseAdapter() {
                // Guarda la ubicacion en Y cuando se pasa el mouse encima de la carta, para poder volver a su posicion original cuando se saca el mouse
                private int ubicacionY;
                @Override
                public void mouseEntered(MouseEvent e){
                    labelCarta.setLocation(labelCarta.getLocation().x, labelCarta.getLocation().y - 50);
                    ubicacionY = labelCarta.getLocation().y;
                    labelCarta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));   // Cambia al cursor de la mano para indicar que es clickeable
                }

                @Override
                public void mouseExited(MouseEvent e){
                    labelCarta.setLocation(labelCarta.getLocation().x, ubicacionY + 50);
                    labelCarta.setCursor(Cursor.getDefaultCursor());    // Cambia al cursor por default al salir
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    int espacio = GuiConstants.ESPACIO_BORDE_SELECCION_CARTA;
                    switch (accionVista){
                        case ESPERAR_TURNO:{
                            mostrarTextoInformativo("No es tu turno, espera...");
                            break;
                        }
                        case TURNO_JUGADOR:{
                            // 3 casos.
                            // 1 -> Deselecciona la carta actual si ya estaba seleccionada
                            // 2 -> Deselecciona la carta anterior si se selecciona una nueva carta
                            // 3 -> Selecciona la carta actual.

                            if(cartaSeleccionada != null && cartaSeleccionada.equals(labelCarta)){
                                // Deselecciona la carta actual
                                cartaSeleccionada = null;
                                botonJugar.setVisible(false);
                                labelCarta.setBorder(null);
                                botonDescartar.setVisible(true);

                                mostrarTextoInformativo("Juega una carta haciendo click en ella, o descarta si no tienes opciones");
                            } else {
                                if(cartaSeleccionada != null){
                                    // Deselecciona la carta anterior
                                    cartaSeleccionada.setBorder(null);
                                }

                                // Selecciona la carta actual
                                cartaSeleccionada = labelCarta;

                                // Creo un borde blanco para indicar que la carta está seleccionada
                                labelCarta.setBorder(BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(Color.white, 3),
                                        new EmptyBorder(espacio, espacio, espacio, espacio)));

                                botonJugar.setVisible(true);
                                botonDescartar.setVisible(false);

                                mostrarTextoInformativo("Carta seleccionada: " + carta);
                            }
                            break;
                        }
                        case DESCARTAR_CARTAS:{
                            if(labelCarta.getBorder() != null){
                                // Elimina el borde, deselecciona la carta
                                labelCarta.setBorder(null);
                                cartasSeleccionadasParaDescartar.remove(labelCarta);
                            } else {
                                if(cartasSeleccionadasParaDescartar == null) cartasSeleccionadasParaDescartar = new ArrayList<>(3);
                                // Selecciona la carta, creo un borde externo para indicar que la carta está seleccionada
                                labelCarta.setBorder(BorderFactory.createCompoundBorder(
                                                BorderFactory.createLineBorder(Color.white, 3),
                                                new EmptyBorder(espacio, espacio, espacio, espacio)));

                                cartasSeleccionadasParaDescartar.add(labelCarta);
                            }
                            break;
                        }
                        case GAME_OVER:{
                            mostrarTextoInformativo("La partida ya terminó...");
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

    @Override
    public void mostrarTextoInformativo(String texto){
        if(panelTextoInformativo == null){
            panelTextoInformativo = new JPanel();
            panelTextoInformativo.setSize(GuiConstants.SIZE_TEXTO_INFORMATIVO_X, GuiConstants.SIZE_TEXTO_INFORMATIVO_Y);
            panelTextoInformativo.setVisible(true);
            panelTextoInformativo.setOpaque(false);
            capas.add(panelTextoInformativo, JLayeredPane.POPUP_LAYER);
        } else {
            panelTextoInformativo.removeAll();
        }

        JLabel labelTexto = new JLabel(texto);
        labelTexto.setForeground(Color.white);

        panelTextoInformativo.add(labelTexto, BorderLayout.CENTER);
        panelTextoInformativo.setLocation(GuiConstants.LOC_TEXTO_INFORMATIVO_X, GuiConstants.LOC_TEXTO_INFORMATIVO_Y);
        panelTextoInformativo.revalidate();
        panelTextoInformativo.repaint();
    }

    @Override
    public void mostrarBienvenida(){
        textoBienvenida = new JTextArea("¡Bienvenido a Virus, el juego de cartas más contagioso!\n Ingresa tu nombre en el input debajo: \n");
        textoBienvenida.setBackground(Color.BLACK);
        textoBienvenida.setForeground(Color.WHITE);
        textoBienvenida.setEditable(false);
        textoBienvenida.setLineWrap(true);
        textoBienvenida.setWrapStyleWord(true);

        add(textoBienvenida, BorderLayout.CENTER);
    }


    @Override
    public void ingresarNuevoJugador() {
        if(controlador.getJugador() == null){
            String nombre = campoNombre.getText().trim().toUpperCase();
            if(!nombre.isEmpty()){
                controlador.nuevoJugador(nombre);
                campoNombre.setText("");
            } else {
                textoBienvenida.setText("");
                textoBienvenida.append("No puedes dejar el nombre vacío.\n");
            }
        } else {
            textoBienvenida.setText("");
            textoBienvenida.append("Ya ingresó su nombre, ahora espera a tu rival.\n");
        }


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
                panelCuerpoRival.setSize(GuiConstants.SIZE_PANEL_CUERPO_X, GuiConstants.SIZE_PANEL_CUERPO_Y);
                panelCuerpoRival.setVisible(true);
                panelCuerpoRival.setOpaque(false);
                panelCuerpoRival.setLocation(GuiConstants.LOC_PANEL_CUERPO_X, GuiConstants.LOC_PANEL_CUERPO_RIVAL_Y);
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

                // Hover con nombre y estado de la carta
                labelOrgano.setToolTipText(organo.toString());
                panelDelCuerpo.add(labelOrgano);

                if(organo.estaInfectado()){
                    Image imagenInfeccion = organo.getInfecciones().get(0).getImagen()
                            .getImage()
                            .getScaledInstance(GuiConstants.SIZE_IMG_INFECCION_MEDICINA_X, GuiConstants.SIZE_IMG_INFECCION_MEDICINA_Y, Image.SCALE_SMOOTH);

                    JLabel labelInfeccion = new JLabel(new ImageIcon(imagenInfeccion));
                    labelInfeccion.setVisible(true);
                    labelInfeccion.setOpaque(false);
                    labelInfeccion.setAlignmentY(2.0f);

                    labelOrgano.add(labelInfeccion);
                } else {
                    if(organo.esInmune()){
                        // Si es inmune, muestro ambas medicinas
                        JPanel panelMedicinas = new JPanel();
                        panelMedicinas.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                        panelMedicinas.setOpaque(false);

                        Image imagenMedicina = organo.getMedicinas().get(0).getImagen()
                                .getImage()
                                .getScaledInstance(GuiConstants.SIZE_IMGS_INMUNE_X, GuiConstants.SIZE_IMGS_INMUNE_Y, Image.SCALE_SMOOTH);

                        JLabel labelMedicina1 = new JLabel(new ImageIcon(imagenMedicina));
                        JLabel labelMedicina2 = new JLabel(new ImageIcon(imagenMedicina));
                        labelMedicina1.setOpaque(false);
                        labelMedicina2.setOpaque(false);

                        panelMedicinas.add(labelMedicina1);
                        panelMedicinas.add(labelMedicina2);

                        labelOrgano.setLayout(new BorderLayout());
                        labelOrgano.add(panelMedicinas, BorderLayout.SOUTH);
                    } else {
                        if(organo.tieneMedicina()){
                            Image imagenMedicina = organo.getMedicinas().get(0).getImagen()
                                    .getImage()
                                    .getScaledInstance(GuiConstants.SIZE_IMG_INFECCION_MEDICINA_X, GuiConstants.SIZE_IMG_INFECCION_MEDICINA_Y, Image.SCALE_SMOOTH);
                            JLabel labelMedicina = new JLabel(new ImageIcon(imagenMedicina));
                            labelMedicina.setVisible(true);
                            labelMedicina.setOpaque(false);
                            labelMedicina.setAlignmentY(2.0f);

                            labelOrgano.add(labelMedicina);
                        }
                    }

                }
            }
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
                panelCuerpoJugador.setSize(GuiConstants.SIZE_PANEL_CUERPO_X, GuiConstants.SIZE_PANEL_CUERPO_Y);
                panelCuerpoJugador.setVisible(true);
                panelCuerpoJugador.setOpaque(false);
                panelCuerpoJugador.setLocation(GuiConstants.LOC_PANEL_CUERPO_X, GuiConstants.LOC_PANEL_CUERPO_JUGADOR_Y);
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

                labelOrgano.setToolTipText(organo.toString());
                panelDelCuerpo.add(labelOrgano);

                if(organo.estaInfectado()) {
                    Image imagenInfeccion = organo.getInfecciones().get(0).getImagen().
                            getImage().
                            getScaledInstance(GuiConstants.SIZE_IMG_INFECCION_MEDICINA_X, GuiConstants.SIZE_IMG_INFECCION_MEDICINA_Y, Image.SCALE_SMOOTH);

                    JLabel labelInfeccion = new JLabel(new ImageIcon(imagenInfeccion));
                    labelInfeccion.setVisible(true);
                    labelInfeccion.setOpaque(false);
                    labelInfeccion.setAlignmentY(2.0f);

                    labelOrgano.add(labelInfeccion);
                } else {
                    if(organo.esInmune()){
                        // Si es inmune, muestro ambas medicinas
                        JPanel panelMedicinas = new JPanel();
                        panelMedicinas.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                        panelMedicinas.setOpaque(false);

                        Image imagenMedicina = organo.getMedicinas().get(0).getImagen()
                                .getImage()
                                .getScaledInstance(GuiConstants.SIZE_IMGS_INMUNE_X, GuiConstants.SIZE_IMGS_INMUNE_Y, Image.SCALE_SMOOTH);

                        JLabel labelMedicina1 = new JLabel(new ImageIcon(imagenMedicina));
                        JLabel labelMedicina2 = new JLabel(new ImageIcon(imagenMedicina));
                        labelMedicina1.setOpaque(false);
                        labelMedicina2.setOpaque(false);

                        panelMedicinas.add(labelMedicina1);
                        panelMedicinas.add(labelMedicina2);

                        labelOrgano.setLayout(new BorderLayout());
                        labelOrgano.add(panelMedicinas, BorderLayout.SOUTH);
                    } else {
                        if(organo.tieneMedicina()){
                            // Agrego la carta de medicina sobre la carta del organo
                            Image imagenMedicina = organo.getMedicinas().get(0).getImagen()
                                    .getImage()
                                    .getScaledInstance(GuiConstants.SIZE_IMG_INFECCION_MEDICINA_X, GuiConstants.SIZE_IMG_INFECCION_MEDICINA_Y, Image.SCALE_SMOOTH);
                            JLabel labelMedicina = new JLabel(new ImageIcon(imagenMedicina));
                            labelMedicina.setVisible(true);
                            labelMedicina.setOpaque(false);
                            labelMedicina.setAlignmentY(2.0f);

                            labelOrgano.add(labelMedicina);
                        }
                    }

                }
            }
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

    @Override
    public void iniciarVistaYControlarEventos(){
        // Agrego capas para poder manejar varios elementos simultaneos
        capas = new JLayeredPane();
        capas.setSize(GuiConstants.ANCHO_VENTANA, GuiConstants.ALTO_VENTANA);

        // Agrego una imágen de fondo
        ImageIcon background = new ImageIcon("./src/virus/game/modelos/cartas/img/fondo3.jpg");
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());

        capas.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
        capas.setVisible(true);
        add(capas);

        // Creacion de los botones para jugar
        botonDescartar = new JButton();
        botonDescartar.setText("Descartar");
        botonDescartar.setSize(GuiConstants.SIZE_BTN_X, GuiConstants.SIZE_BTN_Y);
        botonDescartar.setVisible(false);
        botonDescartar.setLocation(GuiConstants.LOC_BTN_DESCARTAR_X, GuiConstants.LOC_BTN_Y);
        botonDescartar.setBackground(Color.WHITE);
        botonDescartar.setForeground(Color.BLACK);

        botonCancelarDescarte = new JButton();
        botonCancelarDescarte.setText("Cancelar");
        botonCancelarDescarte.setSize(GuiConstants.SIZE_BTN_X, GuiConstants.SIZE_BTN_Y);
        botonCancelarDescarte.setVisible(false);
        botonCancelarDescarte.setLocation(GuiConstants.LOC_BTN_DERECHA_X, GuiConstants.LOC_BTN_Y);
        botonCancelarDescarte.setBackground(Color.WHITE);
        botonCancelarDescarte.setForeground(Color.BLACK);

        // Muestra el nombre del jugador y del rival
        textoNombreJugador = new JLabel(controlador.getJugador().getNombre());
        textoNombreJugador.setForeground(Color.WHITE);
        textoNombreJugador.setSize(GuiConstants.SIZE_NOMBRE_JUGADOR_X, GuiConstants.SIZE_NOMBRE_JUGADOR_Y);
        textoNombreJugador.setOpaque(false);
        textoNombreJugador.setLocation(GuiConstants.LOC_NOMBRE_JUGADOR_X, GuiConstants.LOC_NOMBRE_JUGADOR_Y);
        textoNombreJugador.setVisible(true);
        textoNombreJugador.setFont(new Font(getFont().getName(), Font.BOLD, getFont().getSize()));
        textoNombreJugador.setToolTipText("Tú");

        textoNombreRival = new JLabel(controlador.getRival().getNombre());
        textoNombreRival.setForeground(Color.WHITE);
        textoNombreRival.setSize(GuiConstants.SIZE_NOMBRE_JUGADOR_X, GuiConstants.SIZE_NOMBRE_JUGADOR_Y);
        textoNombreRival.setOpaque(false);
        textoNombreRival.setLocation(GuiConstants.LOC_NOMBRE_JUGADOR_X, GuiConstants.LOC_NOMBRE_RIVAL_Y);
        textoNombreRival.setVisible(true);
        textoNombreRival.setFont(new Font(getFont().getName(), Font.BOLD, getFont().getSize()));
        textoNombreRival.setToolTipText("Rival");


        botonDescartar.addActionListener(e -> {
            switch (accionVista){
                case TURNO_JUGADOR:{
                    // Cuando el jugador hace click en el botón Descartar
                    mostrarTextoInformativo("Seleccione las cartas a descartar");
                    botonDescartar.setText("Confirmar");
                    setAccionVista(AccionVista.DESCARTAR_CARTAS);
                    botonCancelarDescarte.setVisible(true);

                    // Si había alguna carta seleccionada, la deselecciona
                    if(cartaSeleccionada != null) {
                        cartaSeleccionada.setBorder(null);
                        cartaSeleccionada = null;
                        botonJugar.setVisible(false);
                    }
                    break;
                }
                case DESCARTAR_CARTAS:{
                    // Se acciona cuando se hace click en el botón "Confirmar"
                    // Controla que si no hay ninguna carta seleccionada, no permito descartar
                    boolean noHayCartasSeleccionadas = cartasSeleccionadasParaDescartar == null || cartasSeleccionadasParaDescartar.isEmpty();
                    if(noHayCartasSeleccionadas){
                        // Si no hay cartas seleccionadas, lo informo y no descarto
                        mostrarTextoInformativo("Debe seleccionar al menos una carta para descartar");
                    } else {
                        int[] indicesDeCartasADescartar = new int[cartasSeleccionadasParaDescartar.size()];
                        for (int i = 0; i < cartasSeleccionadasParaDescartar.size(); i++) {
                            indicesDeCartasADescartar[i] = Integer.parseInt(cartasSeleccionadasParaDescartar.get(i).getName()); // El label de la carta contiene de nombre el numero de carta que le corresponde.
                        }
                        controlador.descartarCartas(indicesDeCartasADescartar); // Descarto las cartas seleccionadas
                        cartasSeleccionadasParaDescartar = null;        // Elimino el array
                        mostrarTextoInformativo("");
                        controlador.finDeTurno();
                        botonCancelarDescarte.setVisible(false);
                        botonDescartar.setText("Descartar");
                        setAccionVista(AccionVista.ESPERAR_TURNO);
                    }
                    break;
                }
            }
        });

        // Acciones del boton para cancelar el descarte
        botonCancelarDescarte.addActionListener(e -> {
            if(cartasSeleccionadasParaDescartar != null) {
                for (JLabel labelCarta : cartasSeleccionadasParaDescartar) {
                    labelCarta.setBorder(null);
                }
                cartasSeleccionadasParaDescartar = null;
            }
            botonDescartar.setText("Descartar");
            setAccionVista(AccionVista.TURNO_JUGADOR);
            botonCancelarDescarte.setVisible(false);
            mostrarTextoInformativo(GuiConstants.TXT_JUEGA_UNA_CARTA);
        });

        botonJugar = new JButton();
        botonJugar.setText("Jugar");
        botonJugar.setSize(GuiConstants.SIZE_BTN_X, GuiConstants.SIZE_BTN_Y);
        botonJugar.setVisible(false);
        botonJugar.setLocation(GuiConstants.LOC_BTN_DERECHA_X, GuiConstants.LOC_BTN_Y);
        botonJugar.setBackground(Color.WHITE);
        botonJugar.setForeground(Color.BLACK);

        botonJugar.addActionListener(e -> {
            // Esto acciona todo lo relativo a las cartas, menos descartar
            jugadorRealizaUnaAccion();
        });

        capas.add(botonJugar, JLayeredPane.POPUP_LAYER);
        capas.add(botonDescartar, JLayeredPane.POPUP_LAYER);
        capas.add(botonCancelarDescarte, JLayeredPane.POPUP_LAYER);
        capas.add(textoNombreJugador, JLayeredPane.DRAG_LAYER);
        capas.add(textoNombreRival, JLayeredPane.DRAG_LAYER);
    }

    // Muestra el boton para descartar cartas solo cuando es su turno
    @Override
    public void mostrarBotonDescartar() {
        botonDescartar.setVisible(controlador.esSuTurno());
    }

    @Override
    public void avisarEsperaALosDemasJugadores() {
        textoBienvenida.append("Espere a que tu rival esté listo...\n");
    }

    // Muestra el mazo
    public void mostrarMazo(){
        // Si el panel del mazo todavía no se creó, se crea aquí por primera vez
        if(panelMazo == null){
            panelMazo = new JPanel();
            panelMazo.setSize(GuiConstants.SIZE_MAZO_X, GuiConstants.SIZE_MAZO_Y);
            panelMazo.setOpaque(false);
            panelMazo.setVisible(true);
            panelMazo.setLocation(GuiConstants.LOC_MAZO_X , GuiConstants.LOC_MAZO_Y);
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

        JLabel labelCantidadDeCartasEnMazo = new JLabel();
        labelCantidadDeCartasEnMazo.setText(cantidadDeCartasEnMazo + " cartas");
        labelCantidadDeCartasEnMazo.setForeground(Color.white);

        labelCantidadDeCartasEnMazo.setBorder(BorderFactory.createEmptyBorder(0, 12, 0,0));
        Font fuenteGlobal = labelCantidadDeCartasEnMazo.getFont();
        labelCantidadDeCartasEnMazo.setFont(new Font(fuenteGlobal.getName(), Font.BOLD, fuenteGlobal.getSize() + 2));

        panelCartasDelMazo.add(labelCantidadDeCartasEnMazo);
        labelCantidadDeCartasEnMazo.setVisible(false);

        // Obtengo la imágen del dorso de las cartas
        Image imagenDorso = new ImageIcon("./src/virus/game/modelos/cartas/img/dorso.png")
                .getImage()
                .getScaledInstance(GuiConstants.SIZE_IMG_MAZO_X, GuiConstants.SIZE_IMG_MAZO_Y, Image.SCALE_SMOOTH);

        if(cantidadDeCartasEnMazo == 1) cantidadDeCartasEnMazo = 2; // Para que se muestre cuando queda solo 1 carta
        for (int i = (cantidadDeCartasEnMazo / 2) - 1; i >= 0; i--) {
            // Creo la imagen del dorso de las cartas
            // De modo que cada carta esté una abajo de otra
            JLabel labelDorso = new JLabel(new ImageIcon(imagenDorso));
            labelDorso.setBorder(BorderFactory.createEmptyBorder(0, i, 0, 0));

            panelCartasDelMazo.add(labelDorso);

            if(i > 0){
                panelCartasDelMazo.add(Box.createRigidArea(new Dimension(0,-109)));
            }
        }

        panelMazo.add(panelCartasDelMazo);
        panelMazo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                labelCantidadDeCartasEnMazo.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                labelCantidadDeCartasEnMazo.setVisible(false);
            }
        });
    }

    // Muestra el mazo de descartes
    @Override
    public void mostrarMazoDeDescartes(){
        ArrayList<Carta> cartasDelMazoDeDescartes = controlador.getCartasDelMazoDeDescartes();

        if(!cartasDelMazoDeDescartes.isEmpty()) {
            if(panelMazoDeDescartes == null){
                panelMazoDeDescartes = new JPanel();
                panelMazoDeDescartes.setSize(GuiConstants.SIZE_MAZO_X, GuiConstants.SIZE_MAZO_DESCARTES_Y);
                panelMazoDeDescartes.setVisible(true);
                panelMazoDeDescartes.setOpaque(false);
                panelMazoDeDescartes.setLocation(GuiConstants.LOC_MAZO_X, GuiConstants.LOC_MAZO_DESCARTES_Y);
                capas.add(panelMazoDeDescartes, JLayeredPane.PALETTE_LAYER);
            } else {
                panelMazoDeDescartes.removeAll();
            }

            JPanel panelCartasDelMazoDeDescartes = new JPanel();
            panelCartasDelMazoDeDescartes.setOpaque(false);
            panelCartasDelMazoDeDescartes.setVisible(true);
            panelCartasDelMazoDeDescartes.setLayout(new BoxLayout(panelCartasDelMazoDeDescartes, BoxLayout.Y_AXIS));

            int cantidadDeCartas = cartasDelMazoDeDescartes.size();

            // Toma las últimas 3 cartas, es decir, las que se acaban de descartar únicamente
            int cantCartasAMostrar = Math.max(cantidadDeCartas - 3, 0);

            // Bucle para mostrar las últimas 3
            for (int i = cantidadDeCartas - 1; i >= cantCartasAMostrar; i--) {
                Image imagenCarta = cartasDelMazoDeDescartes.get(i).getImagen()
                        .getImage()
                        .getScaledInstance(GuiConstants.SIZE_IMG_MAZO_X, GuiConstants.SIZE_IMG_MAZO_Y, Image.SCALE_SMOOTH);
                JLabel labelCarta = new JLabel(new ImageIcon(imagenCarta));

                //labelCarta.setToolTipText(cartasDelMazoDeDescartes.get(i).toString() + " (Descartada)");
                labelCarta.setToolTipText("<html>" + cartasDelMazoDeDescartes.get(i).toString() + " (Descartada) <br>"
                        + cartasDelMazoDeDescartes.get(i).getDescripcion() + "</html>");

                panelCartasDelMazoDeDescartes.add(labelCarta);
                if(i > cantCartasAMostrar){
                    // Mueve las cartas de más abajo, de forma vertical para abajo.
                    panelCartasDelMazoDeDescartes.add(Box.createRigidArea(new Dimension(0, -60)));
                }
            }
            panelMazoDeDescartes.add(panelCartasDelMazoDeDescartes);
        } else {
            if(panelMazoDeDescartes!=null) panelMazoDeDescartes.removeAll();
        }
    }

    @Override
    public void avisarTurno() {
        // Crea el panel de texto que avisa de quien es el turno
        // También, al final avisa quién es el ganador
        if(panelTextoTurno == null){
            panelTextoTurno = new JPanel();
            panelTextoTurno.setSize(GuiConstants.SIZE_TEXTO_TURNO_X, GuiConstants.SIZE_TEXTO_TURNO_Y);
            panelTextoTurno.setVisible(true);
            panelTextoTurno.setOpaque(false);
            panelTextoTurno.setLocation(GuiConstants.LOC_TEXTO_TURNO_X, GuiConstants.LOC_TEXTO_TURNO_Y);
            textoTurno = new JLabel();
            textoTurno.setForeground(Color.white);
            panelTextoTurno.add(textoTurno);
            capas.add(panelTextoTurno, JLayeredPane.POPUP_LAYER);
        }

        if(controlador.esSuTurno()){
            textoTurno.setText("*** TU TURNO ***");
            mostrarTextoInformativo(GuiConstants.TXT_JUEGA_UNA_CARTA);
        } else {
            textoTurno.setText("*** TURNO DE " + controlador.getTurnoJugador().getNombre() + " ***");
            mostrarTextoInformativo(GuiConstants.TXT_ESPERA_QUE_EL_RIVAL_JUEGUE);
        }
    }

    @Override
    public void jugadorRealizaUnaAccion() {
        int numCarta = Integer.parseInt(cartaSeleccionada.getName());
        if(controlador.accionarCarta(numCarta)){
            controlador.finDeTurno();
            botonJugar.setVisible(false);
        } else {
            //mostrarMesa();
            mostrarTextoInformativo(GuiConstants.TXT_NO_PUEDES_JUGAR_CARTA);
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
        textoTurno.setText("*** ¡" + textoNombreJugador.getText() + ", ERES EL GANADOR, FELICITACIONES! ***");
        botonDescartar.setVisible(false);
        mostrarTextoInformativo("Has logrado un gran desempeño y ganaste la partida.");
        revanchaYSalir();
    }

    @Override
    public void revanchaYSalir(){
        // Botón salir
        JButton botonSalir = new JButton("Salir");
        botonSalir.setVisible(true);
        botonSalir.setSize(GuiConstants.SIZE_BTN_X, GuiConstants.SIZE_BTN_Y);
        botonSalir.setLocation(GuiConstants.LOC_BTN_DESCARTAR_X, GuiConstants.LOC_BTN_Y);
        capas.add(botonSalir, JLayeredPane.POPUP_LAYER);

        botonSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cierra el juego por completo. (aunque el servidor sí seguirá corriendo)
                dispose();
                System.exit(0);
            }
        });

        // Botón revancha
        JButton botonVolverAJugar = new JButton("Revancha");
        botonVolverAJugar.setVisible(true);
        botonVolverAJugar.setSize(GuiConstants.SIZE_BTN_X + 10, GuiConstants.SIZE_BTN_Y);
        botonVolverAJugar.setLocation(GuiConstants.LOC_BTN_DERECHA_X, GuiConstants.LOC_BTN_Y);
        capas.add(botonVolverAJugar, JLayeredPane.POPUP_LAYER);

        botonVolverAJugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonVolverAJugar.setVisible(false);
                mostrarTextoInformativo("Espera a que tu rival decida si quiere jugar revancha...");
                controlador.jugarRevancha();
                panelCuerpoJugador.removeAll();
                panelCuerpoRival.removeAll();
                capas.revalidate();
                capas.repaint();

                botonSalir.setVisible(false);
            }
        });
    }

    @Override
    public void avisarQueElJugadorPerdio() {
        textoTurno.setText("*** " + textoNombreJugador.getText() + ", HAS PERDIDO. " + textoNombreRival.getText() + " ES EL GANADOR. ***");
        mostrarTextoInformativo("¡Te deseamos más suerte la próxima vez!");
        revanchaYSalir();
    }

    @Override
    public void mostrarTablaDeGanadores() {
        textoBienvenida.setText("");
        textoBienvenida.append(controlador.buscarTablaDeGanadores());
    }

    @Override
    public void mostrarReglas() {
        textoBienvenida.setText("");
        textoBienvenida.append("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        textoBienvenida.append("  OBJETIVO Y REGLAS DEL JUEGO\n");
        textoBienvenida.append("  --> PARA GANAR: Tener en tu cuerpo (tu mesa) las 4 cartas de órganos diferentes, sin ninguna infección.\n");
        textoBienvenida.append("  --> ORGANOS: Cerebro, Corazón, Estómago y Hueso\n");
        textoBienvenida.append("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        textoBienvenida.append("  --> VIRUS: Los hay para cada órgano. Sirven para infectar los órganos del rival e impedir que éste gane.\n");
        textoBienvenida.append("  --> Si se aplican 2 virus sobre un órgano, éste se extirpará del cuerpo (el órgano se enviará a descartes, junto con los virus aplicados).\n");
        textoBienvenida.append("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        textoBienvenida.append("  --> MEDICINAS: Las hay para cada órgano. Sirven para vacunar tus órganos, mantenerlos a salvo.\n");
        textoBienvenida.append("  --> Si se aplican 2 medicinas sobre un órgano, éste se volverá inmune a cualquier virus (verás dos cartas de medicina sobre el órgano).\n");
        textoBienvenida.append("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        textoBienvenida.append(" --> Si se aplica un virus sobre un órgano que está vacunado, éste eliminará esa vacuna (tanto el virus como la medicina se enviarán a descartes).\n");
        textoBienvenida.append(" --> Si se aplica una medicina sobre un órgano que está infectado, éste eliminará ese virus (tanto la medicina como el virus se enviarán a descartes).\n");
        textoBienvenida.append("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        textoBienvenida.append(" --> DESCARTAR CARTAS: Durante tu turno, presiona el botón 'Descartar' en la parte inferior izquierda de la pantalla, haz click sobre las cartas que quieres descartar y presiona el botón 'Confirmar'.\n");
        textoBienvenida.append(" --> En cada turno tendrás 3 cartas en la mano. Podrás jugar una, o descartar hasta las 3 cartas.\n");
        textoBienvenida.append(" --> Habrá ocasiones en las que no podrás jugar ninguna carta, así que te verás obligado a descartar.\n");
        textoBienvenida.append("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        textoBienvenida.append(" --> TIPS \n");
        textoBienvenida.append(" --> Para jugar una carta, haz click en ella y luego en el botón 'Jugar' en la esquina inferior derecha. Si no se puede jugar esa carta, habrá un texto informativo en la parte superior que te lo indicará.\n");
        textoBienvenida.append(" --> Para ver qué tipo de carta es alguna de las que se muestra en pantalla, deja el mouse posado sobre ella y te indicará su nombre, y si es un órgano, si está infectado, inmune o vacunado.\n");
        textoBienvenida.append(" --> En la esquina inferior izquierda podrás ver las últimas 3 cartas enviadas a descartes.\n");
        textoBienvenida.append(" --> En la esquina superior derecha podrás ver cuántas cartas quedan en el mazo si te posas sobre él. No te preocupes, cuando el mazo se quede sin cartas, se reciclará con el mazo de descartes.\n");

        textoBienvenida.append("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
    }
}
