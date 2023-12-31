package virus.game.vistas;

import java.io.Serializable;

public enum AccionVista implements Serializable {
    NUEVO_JUGADOR,
    ESPERAR_TURNO,
    TURNO_JUGADOR,
    JUGAR_CARTA,
    DESCARTAR_CARTAS,
    GAME_OVER
}
