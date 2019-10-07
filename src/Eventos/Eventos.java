package Eventos;

import Estructuras.ListaEnlazada;
import Objetos.Archivo;
import javafx.event.ActionEvent;
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
     * @param e              Evento de mouse al hacer clic en botón "Escoger Archivo"
     * @param escogerArchivo Variable para abrir la ventana para escoger el archivo a agregar
     * @param lista          Lista enlazada en donde se almacenan los archivos
     * @param primaryStage   Ventana principal del programa
     * @throws FileNotFoundException Excepción lanzada en caso de error
     */
    public static void agregarEnBiblioteca(javafx.event.ActionEvent e, FileChooser escogerArchivo, ListaEnlazada<Archivo> lista, Stage primaryStage) {
        try {
            File file = escogerArchivo.showOpenDialog(primaryStage);
            lista.InsertarFinal(new Archivo(file));
            //TextFields.bindAutoCompletion(input, palabrasPosibles);
            FileInputStream input = new FileInputStream(
                    //Direccion Daniel: "/Users/daniel/IdeaProjects/Proyecto-2-Text-Finder/src/Imagenes/texto.png"
                    "/Users/daniel/IdeaProjects/Proyecto-2-Text-Finder/src/Imagenes/texto.png");
            Image image = new Image(input, 80, 60, true, true);
            ImageView imageView = new ImageView(image);

            Label label = new Label("  " + file.getName().toUpperCase(), imageView);
            Label labelSeparacion = new Label("  ");

            vbox.getChildren().addAll(label, labelSeparacion);
            //}
        } catch (Exception ignored) {

            alert.setTitle(" Precaución ");
            alert.setHeaderText(null);
            alert.setContentText("Error al leer el archivo");
            alert.showAndWait();

        }
    }

    /**
     * Método que muestra el archivo en el área de texto de la ventana principal
     *
     * @param event Evento de mouse al hacer clic en botón "Búsqueda"
     * @param lista Lista enlazada donde se almacenan los archivos
     * @throws IOException Excepción lanzada en caso de error
     */
    public static void mostrarArchivo(ActionEvent event, ListaEnlazada<Archivo> lista, Stage primaryStage) throws IOException {

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

            int posX = 30, posY = 40, cont = 0;
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
                if (!input.getText().equals("")) {
                    abrirArchivo(lista.Obtener(i), input,areaDeTexto);
                    textos.getChildren().addAll(areaDeTexto, new Label("              "));
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

    private static void abrirArchivo(Archivo x, TextField area,TextArea textArea) {
        if (x.getArbolPalabras().contains(area.getText())){
            textArea.appendText(x.Texto);
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
