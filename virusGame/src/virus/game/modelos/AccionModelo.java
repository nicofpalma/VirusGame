package virus.game.modelos;

import java.io.Serializable;

public enum AccionModelo implements Serializable {
    INICIAR_JUEGO,
    NUEVO_JUGADOR,
    ESPERAR_TURNO,
    ESPERAR_REGISTRO,
    INICIO_NUEVO_TURNO,
    GAME_OVER
}
