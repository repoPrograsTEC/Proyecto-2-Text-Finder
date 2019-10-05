package AplicacionMain;

import ListaEnlazada.ListaEnlazada;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image ;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.io.*;
import java.util.Random;

public class Main extends Application {

    private Desktop desktop = Desktop.getDesktop();
    public ListaEnlazada lista = new ListaEnlazada();
    public String [] palabrasPosibles = new String[5000];
    public Group grupo = new Group();
    TextField input = new TextField();
    ScrollPane scrollpane = new ScrollPane();
    VBox vbox = new VBox();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane root = new Pane();

        final FileChooser escogerArchivo = new FileChooser();
        escogerArchivo.setTitle(" Escoger archivo para agregar a la biblioteca ");
        escogerArchivo.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt"),
                new FileChooser.ExtensionFilter("DOCX", "*.docx"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        final Button openButton = new Button(" Escoger archivo ");
        openButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = escogerArchivo.showOpenDialog(primaryStage);
                        System.out.println(" Archivo escogido: " + file);
                        lista.InsertarFinal(file);
                        for (int i = 0; i < lista.getLargo(); i++){
                            System.out.println(" Nodo: " + lista.Obtener(i));
                            leer(lista.Obtener(i), palabrasPosibles);
                        }
                        System.out.println(" ");
                        TextFields.bindAutoCompletion(input, palabrasPosibles);
                        Rectangle rectangulo = new Rectangle(
                                70d,70d, Color.BLACK);
                        vbox.getChildren().add(rectangulo);
                    }
                });

        //Creating an image
        Image image = new Image("Imagenes/buscar.jpg");

        //Setting the image view
        ImageView imageView = new ImageView(image);

        final Button buscar = new Button();
        buscar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
            }
        });



        /**
         * Contenedor para conectores dentro del contenedor Vbox
         */
        scrollpane.setMaxSize(88, 485);
        scrollpane.setPrefWidth(88);
        scrollpane.setCursor(Cursor.HAND);
        scrollpane.setContent(vbox);
        scrollpane.setBackground(Background.EMPTY);
        scrollpane.setStyle("-fx-border-color: black");

        input.setLayoutX(300);
        input.setPrefWidth(500);
        buscar.setLayoutX(850);


        grupo.getChildren().addAll(input, buscar);

        /**
         * Contenedor del scrollbar
         */
        BorderPane borderPane = new BorderPane( grupo);
        borderPane.setBackground(Background.EMPTY);
        BorderPane.setMargin(scrollpane, new javafx.geometry.Insets(30d,10d,5d,30d));
        borderPane.setLeft(scrollpane);
        BorderPane.setMargin(openButton, new javafx.geometry.Insets(5d,20d,20d,22d));
        borderPane.setBottom(openButton);
        BorderPane.setMargin(root, new javafx.geometry.Insets(30d,20d,20d,20d));
        borderPane.setCenter(root);
        borderPane.setStyle("-fx-background-color: white");
        root.getChildren().add(grupo);

        primaryStage.setTitle(" Buscador de texto ");
        primaryStage.setScene(new Scene(borderPane, 1200, 600));
        primaryStage.show();
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


    public static void main(String[] args) {
        launch(args);
    }
}

