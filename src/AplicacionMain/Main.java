package AplicacionMain;

import Eventos.Eventos;
import ListaEnlazada.ListaEnlazada;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Clase Main que contiene la interfaz gráfica de la aplicación
 */
public class Main extends Application {
    /**
     * Variable para crear la lista que almacena los archivos
     */
    public ListaEnlazada lista = new ListaEnlazada();
    /**
     * Colección para crear la sugerencia de palabras en la barra de búsqueda
     */
    public static String [] palabrasPosibles = new String[5000];
    /**
     * Variable grupo que almacena los componentes a agregar en la ventana principal
     */
    public Group grupo = new Group();
    /**
     * Variable para crear la barra de búsqueda
     */
    public static TextField input = new TextField();
    /**
     * Contenedor para conectores dentro del contenedor Vbox
     */
    ScrollPane scrollpane = new ScrollPane();
    /**
     * Contenedor para los elementos que se agregan al scrollpane
     */
    public static VBox vbox = new VBox();
    /**
     * Contenedor para los elementos que se agregan en el centro de la ventana
     */
    public static Pane root = new Pane();
    /**
     * Botón para buscar coincidencias en la bibilioteca
     */
    public static Button buscar;

    public static Scene escena;

    @Override
    public void start(Stage primaryStage) throws Exception{
        /**
         * Variable para abrir la ventana para escoger archivo
         */
        final FileChooser escogerArchivo = new FileChooser();
        escogerArchivo.setTitle(" Escoger archivo para agregar a la biblioteca ");
        escogerArchivo.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt"),
                new FileChooser.ExtensionFilter("DOCX", "*.docx"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        /**
         *  Botón que abre la ventana para escoger el archivo a agregar
         */
        final Button abrirArchivo = new Button(" Escoger archivo ");
        abrirArchivo.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        try {
                            Eventos.agregarEnBiblioteca(e, escogerArchivo, lista, primaryStage);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

        /**
         * Variable para almacenar la foto para el botón de búsqueda
         */
        Image image = new Image("Imagenes/buscar2.jpg", 50, 30, true, false);
        ImageView imageView = new ImageView(image);

        /**
         * Botón para realizar la búsqueda de coincidencias de palabras
         */
        buscar = new Button("", imageView);
        buscar.setPrefSize(50d,30d);
        buscar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                try {
                    Eventos.mostrarArchivo(e, lista, primaryStage);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        scrollpane.setMaxSize(210d, 650d);
        scrollpane.setPrefWidth(210d);
        scrollpane.setPrefHeight(650d);
        scrollpane.setCursor(Cursor.HAND);
        scrollpane.setContent(vbox);
        scrollpane.setBackground(Background.EMPTY);
        scrollpane.setStyle("-fx-border-color: black");

        input.setLayoutX(200);
        input.setPrefWidth(650);
        input.setPrefHeight(40d);
        buscar.setLayoutX(900);

        TextArea areaDeTexto = new TextArea();
        areaDeTexto.setEditable(false);
        areaDeTexto.setPrefSize(750d, 500d);
        areaDeTexto.setLayoutX(200);
        areaDeTexto.setLayoutY(120);
        areaDeTexto.setStyle("-fx-background-color: #1d178f;");
        areaDeTexto.setBorder(new Border(new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        root.getChildren().add(areaDeTexto);

        grupo.getChildren().addAll(input, buscar);

        /**
         * Contenedor del scrollbar
         */
        BorderPane borderPane = new BorderPane( grupo);
        borderPane.setBackground(Background.EMPTY);
        BorderPane.setMargin(scrollpane, new Insets(30d,10d,5d,40d));
        borderPane.setLeft(scrollpane);
        BorderPane.setMargin(abrirArchivo, new Insets(5d,20d,40d,70d));
        borderPane.setBottom(abrirArchivo);
        BorderPane.setMargin(root, new Insets(30d,20d,20d,20d));
        borderPane.setCenter(root);
        borderPane.setStyle("-fx-background-color: white");
        root.getChildren().add(grupo);

        escena = new Scene(borderPane, 1500, 800);

        primaryStage.setTitle(" Buscador de texto ");
        primaryStage.setScene(escena);
        primaryStage.show();
    }


    /**
     * Método main que ejecuta la aplicación
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}

