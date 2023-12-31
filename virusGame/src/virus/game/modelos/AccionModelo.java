package virus.game.modelos;

import java.io.Serializable;

public enum AccionModelo implements Serializable {
    INICIAR_JUEGO,
    ESPERAR_REGISTRO,
    INICIO_NUEVO_TURNO,
    GAME_OVER,
    GANO_JUGADOR_1,
    GANO_JUGADOR_2
}
