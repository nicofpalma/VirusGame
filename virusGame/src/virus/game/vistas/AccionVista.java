package virus.game.vistas;

import java.io.Serializable;

public enum AccionVista implements Serializable {
    NUEVO_JUGADOR,
    ESPERAR_TURNO,
    TURNO_JUGADOR,
    DESCARTAR_CARTAS,
    GAME_OVER
}
