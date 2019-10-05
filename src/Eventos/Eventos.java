package Eventos;

import ListaEnlazada.ListaEnlazada;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

import static AplicacionMain.Main.*;

public class Eventos {
    private static Alert alert = new Alert(Alert.AlertType.WARNING);
    private static Scanner x;


    public static void agregarEnBiblioteca(javafx.event.ActionEvent e,
                                           FileChooser escogerArchivo,
                                           ListaEnlazada lista,
                                           Stage primaryStage) throws FileNotFoundException {
        try {
            File file = escogerArchivo.showOpenDialog(primaryStage);
            System.out.println(" Archivo escogido: " + file);
            boolean vacio;
            if (vacio = file.length() == 0){
                alert.setTitle(" Precaución ");
                alert.setHeaderText(null);
                alert.setContentText(" El archivo se encuentra vacío y no se agregó a la biblioteca ");
                alert.showAndWait();
            } else {
                lista.InsertarFinal(file);
                for (int i = 0; i < lista.getLargo(); i++) {
                    System.out.println(" Nodo: " + lista.Obtener(i));
                    escogerArchivo(lista.Obtener(i), palabrasPosibles);
                }
                System.out.println(" ");
                TextFields.bindAutoCompletion(input, palabrasPosibles);

                FileInputStream input = new FileInputStream(
                        "C:/Users/Personal/IdeaProjects/Proyecto #2/src/Imagenes/texto.png");
                Image image = new Image(input, 80, 60, true, true);
                ImageView imageView = new ImageView(image);

                Label label = new Label("  " + file.getName().toUpperCase(), imageView);
                Label labelSeparacion = new Label("  ");

                vbox.getChildren().addAll(label, labelSeparacion);
            }
        } catch (Exception ignored){

        }
    }

    public static void mostrarArchivo (javafx.event.ActionEvent e, ListaEnlazada lista) throws IOException {
        TextArea areaDeTexto = new TextArea();
        areaDeTexto.setEditable(false);
        areaDeTexto.setPrefSize(750d, 500d);
        areaDeTexto.setLayoutX(200);
        areaDeTexto.setLayoutY(120);
        areaDeTexto.setStyle("-fx-background-color: #1d178f;");
        areaDeTexto.setBorder(new Border(new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        openFile(lista, areaDeTexto);
        root.getChildren().add(areaDeTexto);
    }


    private static void escogerArchivo(File archivo, String[] palabrasPosibles){
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
            alert.setTitle(" Precaución ");
            alert.setHeaderText(null);
            alert.setContentText(" No se agregó ningún archivo a la biblioteca ");
            alert.showAndWait();
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

    private static void leerArchivo(ListaEnlazada lista, TextArea area) throws IOException {
        area.clear();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(lista.Obtener(0)));
            if(br.readLine() != null) {
                String line;
                while((line = br.readLine()) != null) {
                    String [] x = line.split(",");
                    for(String i : x) {
                        Label h = new Label(i);
                        h.setFont(new Font("Arial",15));
                        area.appendText(String.valueOf(h));
                    }
                    area.appendText("\n");
                }
            }
            else {
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Archivo vacío");
                alert.showAndWait();
            }
        }

        catch (NullPointerException e) {
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Error al abrir el archivo!");
            alert.showAndWait();
        }
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

    private static void openFile(ListaEnlazada lista, TextArea area) throws FileNotFoundException {
        //try{
            x = new Scanner(new File(String.valueOf(lista.Obtener(0))));
            readFile(x, area);
        /*} catch (Exception e){
            alert.setTitle(" Error ");
            alert.setHeaderText(null);
            alert.setContentText(" Error al abrir el archivo #1 ");
            alert.showAndWait();
        }*/
    }

    private static void readFile(Scanner x, TextArea area){
        while(x.hasNext()){
            area.appendText(x.nextLine() + "\n");
        }
        closeFile();
    }

    private static void closeFile(){
        x.close();
    }

}
