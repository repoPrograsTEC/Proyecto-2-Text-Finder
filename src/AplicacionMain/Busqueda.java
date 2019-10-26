package AplicacionMain;

import AlgoritmosOrdenamiento.BubbleSort;
import AlgoritmosOrdenamiento.QuickSort;
import AlgoritmosOrdenamiento.RadixSort;
import Estructuras.ListaEnlazada;
import Eventos.Eventos;
import Objetos.Archivo;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;

import static AplicacionMain.Main.input;

/**
 * Clase Búsqueda que contiene la ventana de coincidencias de palabras
 */
public class Busqueda {
    /**
     * Método ventana que muestra una ventana en donde se visualizan las coincidencias de palabras en la biblioteca
     * @param lista Lista que contiene los archivos de la biblioteca
     * @param primaryStage Escenario de la ventana
     */
    public static void ventana(ListaEnlazada<Archivo> lista, Stage primaryStage) throws IOException {

        if (lista.getLargo() != 0) {
            primaryStage.setIconified(true);

            Stage stage1 = new Stage();

            Button atras = new Button(" Atrás ");
            atras.setOnAction(e -> {
                primaryStage.setIconified(false);
                stage1.close();
                input.clear();
            });

            int columna = 0;
            int fila = 0;
            GridPane gridPane = new GridPane();
            //gridPane.setLayoutX(20);
            //gridPane.setLayoutY(20);
            gridPane.setHgap(18);
            GridPane.setMargin(atras, new Insets(20d, 20d, 20d, 40d));
            gridPane.add(atras, columna, fila);
            fila++;
            fila++;

            int posX = 30, posY = 40;
            HBox textos = new HBox();
            HBox botones= new HBox();
            botones.setSpacing(500);
            textos.setSpacing(50);
            textos.setLayoutX(posX);
            textos.setLayoutY(posY);

            // Ciclo para agregar los archivos con ocurrencias
            ScrollPane scrollPane = new ScrollPane();
            for (int i = 0; i < lista.getLargo(); i++) {
                TextArea areaDeTexto = new TextArea();
                areaDeTexto.setEditable(false);
                areaDeTexto.setPrefSize(500d, 170d);
                areaDeTexto.setLayoutX(posX);
                areaDeTexto.setLayoutY(posY);
                areaDeTexto.setWrapText(true);
                areaDeTexto.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #d64dff; -fx-highlight-text-fill: #000000; -fx-text-fill: #7d00ff; ");
                areaDeTexto.setBorder(new Border(new BorderStroke(
                        Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));


                Button boton=new Button(lista.Obtener(i).getNombre());
                int finalI = i;
                boton.setOnAction(event -> Eventos.abrirThread(lista.Obtener(finalI).getURL()));

                if (!input.getText().equals("")) {
                    Eventos.abrirArchivo(lista.Obtener(i), input, areaDeTexto);
                    if (areaDeTexto.getText() != null) {
                        textos.getChildren().addAll(areaDeTexto);
                        botones.getChildren().addAll(boton);
                        posX += 600;

                    }
                }
            }
            // Contenedor para archivos con ocurrencias
            VBox contenido=new VBox(textos,botones);
            scrollPane.setContent(contenido);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setMaxSize(1100d, 250d);
            scrollPane.setPrefWidth(1100d);
            scrollPane.setPrefHeight(250d);
            GridPane.setMargin(scrollPane, new Insets(20d, 20d, 20d, 40d));
            gridPane.add(scrollPane, columna, fila);
            fila++;
            fila++;

            ToolBar toolBar = new ToolBar();
            toolBar.setCursor(Cursor.HAND);
            toolBar.setMaxSize(180d, 20d);
            toolBar.setPrefSize(180d,20d);
            toolBar.setStyle("-fx-background-color: white; -fx-background-radius: 30; -fx-border-radius: 30; " +
                    "-fx-border-width:5; -fx-border-color: #000000;");
            Label label = new Label(" Ordenar por : ");
            label.setFont(Font.font("Cambria", 21));
            Label separador = new Label("    ");
            Button button = new Button("   Nombre   ");
            button.setOnAction(event -> {
                QuickSort.quickSort(lista,0,lista.getLargo()-1);
                for (int x=0;x<lista.getLargo();x++){
                    System.out.println(lista.Obtener(x).Nombre);
                }
                try {
                    stage1.close();
                    ventana(lista,primaryStage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Button button1 = new Button("     Fecha     ");
            button1.setOnAction(event -> {
                BubbleSort.bubbleSort(lista);
                try {
                    stage1.close();
                    ventana(lista,primaryStage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Button button2 = new Button("   Tamaño   ");
            button2.setOnAction(event -> {
                RadixSort.radixsort(lista);
                try {
                    stage1.close();
                    ventana(lista,primaryStage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            toolBar.getItems().addAll(label, button, new Separator(), button1, new Separator(), button2);
            GridPane.setMargin(toolBar, new Insets(20d, 20d, 20d, 920d));
            gridPane.add(toolBar, 0, 0);
            gridPane.setStyle("-fx-background-color: #7771ff");
            Scene scene = new Scene(gridPane, 1200, 500);

            stage1.setScene(scene);
            stage1.setTitle(" Ocurrencias encontradas ");
            stage1.setX(100d);
            stage1.setY(100d);
            stage1.setResizable(false);
            stage1.show();
        }

    }

}
