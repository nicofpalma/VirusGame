package virus.game.utils;

import java.io.*;
import java.util.ArrayList;

public class Serializador {
    private String nombreArchivo;

    public Serializador(String nombreArchivo){
        super();
        this.nombreArchivo = nombreArchivo;
    }

    public boolean writeOneObject(Object obj){
        boolean respuesta = false;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo));
            oos.writeObject(obj);
            oos.close();
            respuesta = true;
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return respuesta;
    }

    public boolean addOneObject(Object obj){
        boolean respuesta = false;
        try {
            AddableObjectOutputStream oos = new AddableObjectOutputStream(new FileOutputStream(nombreArchivo, true));
            oos.writeObject(obj);
            oos.close();
            respuesta = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return respuesta;
    }
    public Object[] readObjects() {
        Object[] respuesta;
        ArrayList<Object> listOfObject = new ArrayList<Object>();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombreArchivo));
            Object r = ois.readObject();
            while (r != null){
                listOfObject.add(r);
                r = ois.readObject();
            }
            ois.close();
        } catch (EOFException e ){
            System.out.println("Lectura de " + nombreArchivo + " completado");
        } catch (ClassNotFoundException | IOException e ){
            e.printStackTrace();
        }
        if(!listOfObject.isEmpty()){
            respuesta = new Object[listOfObject.size()];
            int count = 0;
            for(Object o : listOfObject){
                respuesta[count ++] = o;
            }
        } else {
            respuesta = null;
        }
        return respuesta;
    }
}
