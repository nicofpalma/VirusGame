package model.medicinas;

import model.Color;

public class MedicinaCorazon extends Medicina{

    // Constructo de la medicina del corazon
    public MedicinaCorazon(){
        // Las medicinas del corazon siempre son rojas
        super(Color.ROJO);
    }
}
