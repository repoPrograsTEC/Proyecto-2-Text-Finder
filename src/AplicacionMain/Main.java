package AplicacionMain;

import Estructuras.ListaEnlazada;
import Eventos.Eventos;
import Objetos.Archivo;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Clase Main que contiene la interfaz gráfica de la aplicación
 */
public class Main extends Application {
    /**
     * Variable para crear la lista que almacena los archivos
     */
    public static ListaEnlazada<Archivo> ListaArchivo;
    /**
     * Variable grupo que almacena los componentes a agregar en la ventana principal
     */
    private Group grupo = new Group();
    /**
     * Variable para crear la barra de búsqueda
     */
    public static TextField input = new TextField();
    /**
     * Contenedor para conectores dentro del contenedor Vbox
     */
    public static ScrollPane scrollpane = new ScrollPane();
    /**
     * Contenedor para los elementos que se agregan al scrollpane
     */
    public static VBox vbox = new VBox();
    /**
     * Contenedor para los elementos que se agregan en el centro de la ventana
     */
    public static Pane root = new Pane();
    /**
     * Contenedor de  la ventana principal
     */
    public BorderPane borderPane;
    /**
     * Contenedor para visualizar el texto deseado
     */
    private TextArea areaDeTexto = new TextArea();
    /**
     * Contador para el número de archivo
     */
    private static int numero = 0;

    @Override
    public void start(Stage primaryStage) {
        ListaArchivo = new ListaEnlazada<>();
        final FileChooser escogerArchivo = new FileChooser();
        escogerArchivo.setTitle(" Escoger archivo para agregar a la biblioteca ");
        escogerArchivo.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(" Txt ", "*.txt"),
                new FileChooser.ExtensionFilter(" Docx ", "*.docx"),
                new FileChooser.ExtensionFilter(" Pdf ", "*.pdf"),
                new FileChooser.ExtensionFilter(" Todos ", "*.txt", "*.docx","*.pdf"));

        Button abrirArchivo = new Button("       Agregar       ");
        abrirArchivo.setOnAction(e -> {
                Eventos.agregarEnBiblioteca(escogerArchivo, ListaArchivo, areaDeTexto, primaryStage, numero);
        });
        Button actualizar = new Button("      Actualizar     ");
        actualizar.setOnAction(e -> {
                    Eventos.actualizarBiblioteca(escogerArchivo, ListaArchivo, areaDeTexto, primaryStage, numero);
                });
        Button eliminar = new Button("       Eliminar       ");
        eliminar.setOnAction(e -> {
                Eventos.eliminarDeBiblioteca(ListaArchivo);
        });

        /*
        Button indizar = new Button("       Indizar       ");
        indizar.setOnAction(e -> {
            Eventos.indizar(ListaArchivo);
        });
        */

        Image image = new Image("Imagenes/buscar2.jpg", 50, 30, true, false);
        ImageView imageView = new ImageView(image);

        Button buscar = new Button("", imageView);
        buscar.setPrefSize(50d,30d);
        buscar.setCursor(Cursor.HAND);
        Tooltip tooltip = new Tooltip(" Buscar Texto ");
        tooltip.setFont(Font.font("Cambria", 18));
        buscar.setTooltip(tooltip);
        buscar.setOnAction(e -> {
            try {
                Eventos.mostrarArchivo(ListaArchivo, primaryStage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        scrollpane.setMaxSize(210d, 650d);
        scrollpane.setPrefWidth(210d);
        scrollpane.setPrefHeight(650d);
        scrollpane.setContent(vbox);
        scrollpane.setBackground(Background.EMPTY);
        scrollpane.setStyle("-fx-border-color: black");

        input.setLayoutX(200);
        input.setPrefWidth(650);
        input.setPrefHeight(40d);
        input.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; " +
                "-fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00; ");
        buscar.setLayoutX(900);

        areaDeTexto.setEditable(false);
        areaDeTexto.setPrefSize(750d, 500d);
        areaDeTexto.setLayoutX(200);
        areaDeTexto.setLayoutY(120);
        areaDeTexto.setStyle(" -fx-font-size: 1.5em; -fx-control-inner-background:#000000; -fx-font-family: Cambria;" +
                " -fx-highlight-fill: #FF8933 ; -fx-highlight-text-fill: #000000; -fx-text-fill: #FFFFFF; ");
        areaDeTexto.setBorder(new Border(new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        areaDeTexto.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                //areaDeTexto.setScaleX(1.2);
                //areaDeTexto.setScaleY(1.2);
                ScaleTransition st = new ScaleTransition(Duration.millis(1000), areaDeTexto);
                st.setByX(1.1f);
                st.setByY(1.1f);
                st.setToX(1.2);
                st.setToY(1.2);
                st.setCycleCount((int) 1f);
                st.setAutoReverse(false);
                if (areaDeTexto.getWidth() < 800d && areaDeTexto.getHeight() < 550d){
                    st.play();
                } else {
                    st.stop();
                }
            }
        });
        areaDeTexto.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                //areaDeTexto.setScaleX(1);
                //areaDeTexto.setScaleY(1);
                ScaleTransition st = new ScaleTransition(Duration.millis(1000), areaDeTexto);
                st.setByX(-1.1f);
                st.setByY(-1.1f);
                st.setToX(1);
                st.setToY(1);
                st.setCycleCount((int) 1f);
                st.setAutoReverse(true);
                if (areaDeTexto.getWidth() >= 750d && areaDeTexto.getHeight() >= 500d){
                    st.play();
                } else {
                    st.stop();
                }
            }
        });

        root.getChildren().add(areaDeTexto);

        grupo.getChildren().addAll(input, buscar);

        ToolBar toolBar = new ToolBar();
        toolBar.setCursor(Cursor.HAND);
        toolBar.setMaxSize(220d, 30d);
        toolBar.setPrefSize(220d,30d);
        toolBar.setStyle("-fx-background-color: white; -fx-background-radius: 30; -fx-border-radius: 30; " +
                "-fx-border-width:5; -fx-border-color: #000000;");

        Label label = new Label("         Biblioteca : ");
        label.setFont(Font.font("Cambria", 21));
        Tooltip tooltip2 = new Tooltip(" Administrar Biblioteca de Archivos ");
        tooltip2.setFont(Font.font("Cambria", 18));
        label.setTooltip(tooltip2);
        Label separador = new Label("    ");

        toolBar.getItems().addAll(label, abrirArchivo, new Separator(),
                                actualizar, new Separator(), eliminar);
                                //new Separator(), indizar

        borderPane = new BorderPane(grupo);
        borderPane.setBackground(Background.EMPTY);
        BorderPane.setMargin(scrollpane, new Insets(30d,10d,5d,40d));
        borderPane.setLeft(scrollpane);
        BorderPane.setMargin(toolBar, new Insets(5d,20d,40d,35d));
        borderPane.setBottom(toolBar);
        BorderPane.setMargin(root, new Insets(30d,20d,20d,20d));
        borderPane.setCenter(root);
        borderPane.setStyle("-fx-background-color: #ffe4b3");
        root.getChildren().add(grupo);

        Scene escena = new Scene(borderPane, 1500, 800);

        primaryStage.setTitle(" Buscador de texto ");
        primaryStage.setScene(escena);
        primaryStage.show();
    }

    /**
     * Método main que ejecuta la aplicación
     * @param args - Argumentos ha ejecutar.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

