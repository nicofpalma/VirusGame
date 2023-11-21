package virus.game.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SerializadorDeGanadores {
    private static final Serializador serializador = new Serializador("tablas.dat");

    public SerializadorDeGanadores(String nombreGanador, String nombrePerdedor){
        // Simplemente guarda algunos datos sobre el ganador
        // Guardo la fecha
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaFormateada = fechaHoraActual.format(formato);

        String datosAGuardar = nombreGanador + " le ganó a " + nombrePerdedor + " con fecha y hora: " + fechaFormateada;
        serializador.addOneObject(datosAGuardar);
    }

    public SerializadorDeGanadores(){

    }

    public String generarStringDeGanadores(){
        Object[] registros = serializador.readObjects();
        String registrosString = "";
        for (int i = 0; i < registros.length; i++) {
            registrosString += registros[i].toString() + "\n";
        }
        if(registrosString.equals("")){
            registrosString = "No hay partidas jugadas todavía.";
        }

        return registrosString;
    }
}
