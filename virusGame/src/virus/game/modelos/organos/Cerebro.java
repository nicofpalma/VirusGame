package virus.game.modelos.organos;

import virus.game.modelos.Color;

import java.io.Serializable;

public class Cerebro extends Organo implements Serializable {
    // Constructor de cerebro
    public Cerebro(){
        // Los cerebros son siempre azules
        super(Color.AZUL, "Cerebro");
    }
}
