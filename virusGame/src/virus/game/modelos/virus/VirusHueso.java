package virus.game.modelos.virus;

import virus.game.modelos.Color;

public class VirusHueso extends Virus{
    // Constructor del virus del hueso
    public VirusHueso(){
        // El color del virus del hueso siempre es amarillo
        super(Color.AMARILLO, "/view/img/virusHueso.png");
    }
}
