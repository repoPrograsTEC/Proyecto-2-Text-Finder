package Eventos;

import ListaEnlazada.ListaEnlazada;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.*;
import java.util.Random;

import static AplicacionMain.Main.*;

public class Eventos {
    public static void indizarArchivo(javafx.event.ActionEvent e,
                                      FileChooser escogerArchivo,
                                      ListaEnlazada lista,
                                      Stage primaryStage) throws FileNotFoundException {

        File file = escogerArchivo.showOpenDialog(primaryStage);
        System.out.println(" Archivo escogido: " + file);
        lista.InsertarFinal(file);
        for (int i = 0; i < lista.getLargo(); i++){
            System.out.println(" Nodo: " + lista.Obtener(i));
            leer(lista.Obtener(i), palabrasPosibles);
        }
        System.out.println(" ");
        TextFields.bindAutoCompletion(input, palabrasPosibles);

        FileInputStream input = new FileInputStream(
                "C:/Users/Personal/IdeaProjects/Proyecto #2/src/Imagenes/texto.png");
        Image image = new Image(input, 80, 60, true, true);
        ImageView imageView = new ImageView(image);

        Label label = new Label("  " + file.getName().toUpperCase(), imageView);
        Label labelSeparacion = new Label ("  ");

        vbox.getChildren().addAll(label, labelSeparacion);
    }

    public static void mostrarArchivo (javafx.event.ActionEvent e, ListaEnlazada lista){

    }
    public static void escribir() {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("archivo.txt");
            pw = new PrintWriter(fichero);

            System.out.println("Escribiendo en el archivo.txt");
            for (int i = 0; i < 10; i++)
                pw.println("Linea " + i);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para
                // asegurarnos que se cierra el fichero.
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void leer(File archivo, String[] palabrasPosibles){
        FileReader fr = null;
        BufferedReader br = null;

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            System.out.println("Leyendo el contendio del archivo.txt");
            String linea;
            int pos = 0;
            while((linea = br.readLine()) != null) {
                //System.out.println(linea);
                String[] palabrasLinea = linea.split(" ");
                for (int i = 0; i < palabrasLinea.length; i++) {
                    palabrasPosibles[pos] = palabrasLinea[i];
                    pos++;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try{
                if( null != fr ){
                    fr.close();
                    System.out.println(" Archivo leído correctamente ");
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }

    public static Color colorAleatorio() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r, g, b);
    }

}
