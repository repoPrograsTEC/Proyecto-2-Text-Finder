package Eventos;

import Estructuras.ListaEnlazada;
import Objetos.Archivo;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

/**
 * Clase eventos en donde se agrupan todos los eventos a realizar en la aplicación
 */
public class Eventos {
    /**
     * Ventana de tipo alert para mostrar algún mensaje de error en la ejecución
     */
    private static Alert alert = new Alert(Alert.AlertType.WARNING);
    /**
     * Variable para almacenar el archivo agregado
     */
    private static Scanner x;
    /**
     * Método que agrega un archivo a la biblioteca
     *
     * @param e Evento de mouse al hacer clic en botón "Escoger Archivo"
     * @param escogerArchivo Variable para abrir la ventana para escoger el archivo a agregar
     * @param lista Lista enlazada en donde se almacenan los archivos
     * @param primaryStage Ventana principal del programa
     * @throws FileNotFoundException Excepción lanzada en caso de error
     */
    public static void agregarEnBiblioteca(javafx.event.ActionEvent e, FileChooser escogerArchivo, ListaEnlazada<File> lista,
                                           Stage primaryStage) throws FileNotFoundException {
        try {
            File file = escogerArchivo.showOpenDialog(primaryStage);
            System.out.println(" Archivo escogido: " + file);
            if (file!=null) {
                lista.InsertarFinal(file);
                for (int i = 0; i < lista.getLargo(); i++) {
                    System.out.println(" Nodo: " + lista.Obtener(i));
                    busquedaSugerida(lista.Obtener(i), palabrasPosibles);
                }
                System.out.println(" ");
                TextFields.bindAutoCompletion(input, palabrasPosibles);
                FileInputStream input = new FileInputStream(
                        //Direccion Daniel: "/Users/daniel/IdeaProjects/Proyecto-2-Text-Finder/src/Imagenes/texto.png"
                        "/Users/daniel/IdeaProjects/Proyecto-2-Text-Finder/src/Imagenes/texto.png");
                Image image = new Image(input, 80, 60, true, true);
                ImageView imageView = new ImageView(image);

                Label label = new Label("  " + file.getName().toUpperCase(), imageView);
                Label labelSeparacion = new Label("  ");

                vbox.getChildren().addAll(label, labelSeparacion);
            }
        } catch (Exception ignored){

            alert.setTitle(" Precaución ");
            alert.setHeaderText(null);
            alert.setContentText("Error al leer el archivo");
            alert.showAndWait();

        }
    }

    /**
     * Método para agregar las palbaras del archivo a la barra de búsqueda sugerida
     *
     * @param archivo Es el archivo del cual se leen las palabras
     * @param palabrasPosibles Colección de palabras que se muestran como búsqueda sugerida
     */
    private static void busquedaSugerida(File archivo, String[] palabrasPosibles){
        FileReader fr = null;
        BufferedReader br = null;

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            System.out.println("Leyendo el contenido del archivo");
            String linea;
            int pos = 0;
            while((linea = br.readLine()) != null) {
                //System.out.println(linea);
                String[] palabrasLinea = linea.split(" ");
                for (String s : palabrasLinea) {
                    palabrasPosibles[pos] = s;
                    pos++;
                }
            }
        }
        catch(Exception e){
            alert.setTitle(" Precaución ");
            alert.setHeaderText(null);
            alert.setContentText(" No se lograron agregar las palabras a la barra de búsqueda sugerida ");
            alert.showAndWait();
        }finally{
            /*
             En el finally cerramos el fichero, para asegurarnos
             que se cierra tanto si todo va bien como si salta
             una excepcion.
            */
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

    /**
     * Método que muestra el archivo en el área de texto de la ventana principal
     *
     * @param event Evento de mouse al hacer clic en botón "Búsqueda"
     * @param lista Lista enlazada donde se almacenan los archivos
     * @throws IOException Excepción lanzada en caso de error
     */
    public static void mostrarArchivo (javafx.event.ActionEvent event, ListaEnlazada lista,
                                       Stage primaryStage) throws IOException {

        if (lista.getLargo() != 0) {
            primaryStage.setIconified(true);
            Group grupo2 = new Group();

            Stage stage1 = new Stage();

            Button atras = new Button(" Atrás ");
            atras.setOnAction(e -> {
                primaryStage.setIconified(false);
                stage1.close();
            });

            int columna = 0;
            int fila = 0;
            GridPane gridPane = new GridPane();
            gridPane.setLayoutX(20);
            gridPane.setLayoutY(20);
            gridPane.setHgap(18);
            gridPane.setMargin(atras, new Insets(20d, 20d, 20d, 20d));
            gridPane.add(atras, columna, fila);
            fila++;
            fila++;

            int posX = 30;
            int posY = 40;
            int cont = 0;
            HBox textos = new HBox();
            textos.setLayoutX(posX);
            textos.setLayoutY(posY);

            // Ciclo para agregar los archivos con ocurrencias
            ScrollPane scrollPane = new ScrollPane();
            for (int i = 0; i < lista.getLargo(); i++) {
                TextArea areaDeTexto = new TextArea();
                areaDeTexto.setEditable(false);
                areaDeTexto.setPrefSize(500d, 230d);
                areaDeTexto.setLayoutX(posX);
                areaDeTexto.setLayoutY(posY);
                areaDeTexto.setStyle("-fx-background-color: #1d178f;");
                areaDeTexto.setBorder(new Border(new BorderStroke(
                        Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                if (!input.getText().equals("")){
                    abrirArchivo(lista, areaDeTexto, cont);
                    textos.getChildren().addAll(areaDeTexto, new Label ("              "));
                    posX += 600;
                    cont++;
                }
            }
            // Contenedor para archivos con ocurrencias
            scrollPane.setContent(textos);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setMaxSize(1100d, 250d);
            scrollPane.setPrefWidth(1100d);
            scrollPane.setPrefHeight(250d);
            gridPane.setMargin(scrollPane, new Insets(20d, 20d, 20d, 20d));
            gridPane.add(scrollPane, columna, fila);

            Scene scene = new Scene(grupo2, 1300, 450);

            grupo2.getChildren().add(gridPane);
            stage1.setScene(scene);
            stage1.setTitle(" Ocurrencias encontradas ");
            stage1.setX(100d);
            stage1.setY(100d);
            stage1.setResizable(false);
            stage1.show();
        }
    }

    /**
     * Método que obtiene el archivo seleccionado para luego abrirlo
     *
     * @param lista Lista enlazada en donde se almacenan los archivos
     * @param area Área de texto en donde se visualiza el texto elegido
     * @throws FileNotFoundException Excepción lanzada en caso de error
     */
    private static void abrirArchivo(ListaEnlazada lista, TextArea area, int cont) throws FileNotFoundException {
        try{
            x = new Scanner(new File(String.valueOf(lista.Obtener(cont))));
            leerArchivo(x, area);
        } catch (Exception e){
            alert.setTitle(" Error ");
            alert.setHeaderText(null);
            alert.setContentText(" Error al abrir el archivo #1 ");
            alert.showAndWait();
        }
    }

    /**
     * Método que lee el contenido del archivo seleccionado
     *
     * @param x Archivo seleccionado para agregar a la biblioteca
     * @param area Área de texto en donde se visualiza el texto elegido
     */
    private static void leerArchivo(Scanner x, TextArea area){
        while(x.hasNext()){
            area.appendText(x.nextLine() + "\n");
        }
        try {
            x.close();
        } catch (Exception e3){
            alert.setTitle(" Precaución ");
            alert.setHeaderText(null);
            alert.setContentText(" El archivo no se cerró correctamente ");
            alert.showAndWait();
        }
    }




    public static Color colorAleatorio() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r, g, b);
    }

    private static void leerArchivo(ListaEnlazada<File> lista, TextArea area) throws IOException {
        area.clear();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(lista.Obtener(0)));
            if(br.readLine() != null) {
                String line;
                while((line = br.readLine()) != null) {
                    String [] x = line.split(",");
                    for(String i : x) {
                        System.out.println(i);
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
}
