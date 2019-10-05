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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {

    private Desktop desktop = Desktop.getDesktop();
    public ListaEnlazada lista = new ListaEnlazada();
    public static String [] palabrasPosibles = new String[5000];
    public Group grupo = new Group();
    public static TextField input = new TextField();
    ScrollPane scrollpane = new ScrollPane();
    public static VBox vbox = new VBox();
    public static Pane root = new Pane();

    @Override
    public void start(Stage primaryStage) throws Exception{

        final FileChooser escogerArchivo = new FileChooser();
        escogerArchivo.setTitle(" Escoger archivo para agregar a la biblioteca ");
        escogerArchivo.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt"),
                new FileChooser.ExtensionFilter("DOCX", "*.docx"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"));

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

        Image image = new Image("Imagenes/buscar2.jpg", 50, 30, true, false);
        ImageView imageView = new ImageView(image);

        final Button buscar = new Button("", imageView);
        buscar.setPrefSize(50d,30d);
        buscar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                try {
                    Eventos.mostrarArchivo(e, lista);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


        /**
         * Contenedor para conectores dentro del contenedor Vbox
         */
        scrollpane.setMaxSize(210d, 650d);
        scrollpane.setPrefWidth(210d);
        scrollpane.setPrefHeight(650d);
        scrollpane.setCursor(Cursor.HAND);
        scrollpane.setContent(vbox);
        scrollpane.setBackground(Background.EMPTY);
        scrollpane.setStyle("-fx-border-color: black");

        input.setLayoutX(200);
        input.setPrefWidth(500);
        input.setPrefHeight(40d);
        buscar.setLayoutX(750);

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

        primaryStage.setTitle(" Buscador de texto ");
        primaryStage.setScene(new Scene(borderPane, 1500, 800));
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}

