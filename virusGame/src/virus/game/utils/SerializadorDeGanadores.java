package virus.game.utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SerializadorDeGanadores {
    private static final Serializador serializador = new Serializador("tablasGanadores.dat");

    public SerializadorDeGanadores(String nombreGanador, String nombrePerdedor){
        // Simplemente guarda algunos datos sobre el ganador
        // Guardo la fecha
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaFormateada = fechaHoraActual.format(formato);

        String datosAGuardar = nombreGanador + " le ganó a " + nombrePerdedor + " con fecha y hora: " + fechaFormateada;

        File archivo = new File("tablasGanadores.dat");
        if(archivo.exists() && !archivo.isDirectory()){
            // Agrega una nueva línea si ya existía
            serializador.addOneObject(datosAGuardar);
        } else {
            // Escribe el archivo de 0 si no existía
            serializador.writeOneObject(datosAGuardar);
        }
    }

    public SerializadorDeGanadores(){

    }

    public String generarStringDeGanadores(){
        Object[] registros = serializador.readObjects();
        StringBuilder registrosString = new StringBuilder();

        for (int i = registros.length - 1; i >= 0; i--) {
            registrosString.append(registros[i].toString()).append("\n");
            registrosString.append("-----------------------------------------------------------------------------------\n");
        }

        if(registrosString.isEmpty()){
            registrosString = new StringBuilder("No hay partidas jugadas todavía.");
        }

        return registrosString.toString();
    }

}
