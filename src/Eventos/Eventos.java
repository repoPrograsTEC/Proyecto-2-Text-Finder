package Eventos;

import AplicacionMain.Busqueda;
import Estructuras.BST;
import Estructuras.ListaEnlazada;
import Objetos.Archivo;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static AplicacionMain.Main.vbox;

/**
 * Clase eventos en donde se agrupan todos los eventos a realizar en la aplicación
 */
public class Eventos {
    /**
     * Variable para documentos duplicados
     */
    static int a = 1;

    /**
     * Ventana de tipo alert para mostrar algún mensaje de error en la ejecución
     */
    private final static Alert alert = new Alert(Alert.AlertType.WARNING);

    /**
     * Método que agrega un archivo a la biblioteca
     *
     * @param escogerArchivo Variable para abrir la ventana para escoger el archivo a agregar
     * @param lista Lista enlazada en donde se almacenan los archivos
     * @param area Área de texto en donde se visualiza el archivo seleccionado
     * @param primaryStage Ventana principal del programa
     */
    public static void agregarEnBiblioteca(FileChooser escogerArchivo, ListaEnlazada<Archivo> lista,
                                           TextArea area, Stage primaryStage, int numero){

        List<File> files = escogerArchivo.showOpenMultipleDialog(primaryStage);
        try {
            for (File file : files) {
                Archivo temp = new Archivo(file, file.getName(), numero);
                for (int cont = 0; cont < lista.getLargo(); cont++) {
                    if (temp.getNombre().equals(lista.Obtener(cont).getNombre())) {
                        String[] string = temp.getNombre().split("\\.");
                        string[0] = string[0] + "(" + a + ")" + ".";
                        String nombre = string[0] + string[1];
                        temp.setNombre(nombre);
                        a++;
                        //break;
                    }
                }
                lista.InsertarFinal(temp);

                FileInputStream input = new FileInputStream(
                        //Direccion Daniel: "/Users/daniel/IdeaProjects/Proyecto-2-Text-Finder/src/Imagenes/texto.png"
                        //Dirección Esteban: "C:/Users/Personal/IdeaProjects/Proyecto #2/src/Imagenes/texto.png"
                        "/Users/daniel/IdeaProjects/Proyecto-2-Text-Finder/src/Imagenes/texto.png");
                Image image = new Image(input, 100, 80, true, true);
                ImageView imageView = new ImageView(image);

                Label label = new Label("  " + temp.Nombre.toUpperCase(), imageView);
                Label labelSeparacion = new Label("  ");

                label.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent evento) {
                        movimientoDetectado(evento, imageView, temp.Nombre);
                    }
                });

                vbox.getChildren().add(label);

            }

            area.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent evento) {
                    evento.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
            });

            area.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent evento) {
                    for (int num = 0; num < lista.getLargo(); num++) {
                        if (evento.getDragboard().getString().equals(lista.Obtener(num).Nombre)) {
                            try {
                                soltar(evento, lista.Obtener(num), area);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        } catch (NullPointerException | IOException ignored) {
            alert.setTitle(" Precaución ");
            alert.setHeaderText(null);
            alert.setContentText("Error al agregar o leer el archivo");
            alert.showAndWait();
        } finally {
            a = 1;
        }
    }

    /**
     * Método para actualizar la biblioteca
     * @param listaEnlazada Lista de archivos
     */
    public static void actualizarBiblioteca(FileChooser escogerArchivo, ListaEnlazada<Archivo> listaEnlazada,
                                            TextArea area, Stage primaryStage, int numero) {
        try {
            String[] listaArchivos = new String[listaEnlazada.getLargo()];
            if (listaEnlazada.getLargo() == 0){
                throw new ArrayIndexOutOfBoundsException(" Tamaño no aceptable ");
            } else {
                for (int i = 0; i < listaArchivos.length; i++) {
                    listaArchivos[i] = listaEnlazada.Obtener(i).getNombre();
                }

                ChoiceDialog<String> archivoParaActualizar = new ChoiceDialog<>(" Seleccione un archivo ", listaArchivos);

                archivoParaActualizar.setHeaderText(null);
                archivoParaActualizar.setContentText("  Escoger archivo para actualizar:                      ");
                archivoParaActualizar.setResizable(true);
                archivoParaActualizar.getDialogPane().setPrefWidth(650);
                archivoParaActualizar.showAndWait();

                int a = 0;
                for (int i = 0; i < listaArchivos.length; i++) {
                    if (archivoParaActualizar.getSelectedItem().equals(listaArchivos[i])) {
                        agregarEnBibliotecaActualizado(escogerArchivo, listaEnlazada, area, primaryStage, i);
                        a++;
                    }
                }
                if (a == 0){
                    Alert alert1 = new Alert(Alert.AlertType.WARNING);
                    alert1.setTitle(" Precaución ");
                    alert1.setHeaderText(null);
                    alert1.setContentText(" No se escogió algún archivo para actualizar de la biblioteca ");
                    alert1.setResizable(true);
                    alert1.getDialogPane().setPrefWidth(500);
                    alert1.showAndWait();
                }
            }

        } catch (Exception e) {
            Alert alert1 = new Alert(Alert.AlertType.WARNING);
            alert1.setTitle(" Precaución ");
            alert1.setHeaderText(null);
            alert1.setContentText(" No se pueden actualizar archivos porque la biblioteca está vacía ");
            alert1.setResizable(true);
            alert1.getDialogPane().setPrefWidth(550);
            alert1.showAndWait();
        }
    }

    /**
     * Método que agrega un archivo actualizado a la biblioteca
     *
     * @param escogerArchivo Variable para abrir la ventana para escoger el archivo a agregar
     * @param lista Lista enlazada en donde se almacenan los archivos
     * @param area Área de texto en donde se visualiza el archivo seleccionado
     * @param primaryStage Ventana principal del programa
     */
    private static void agregarEnBibliotecaActualizado(FileChooser escogerArchivo, ListaEnlazada<Archivo> lista,
                                                       TextArea area, Stage primaryStage, int numero){
        try {
            File file = escogerArchivo.showOpenDialog(primaryStage);
            Archivo temp = new Archivo(file, file.getName(), numero);
            for (int cont = 0; cont < lista.getLargo(); cont++) {
                if (temp.getNombre().equals(lista.Obtener(cont).getNombre())) {
                    String[] string = temp.getNombre().split("\\.");
                    string[0] = string[0] + "(" + a + ")" + ".";
                    String nombre = string[0] + string[1];
                    temp.setNombre(nombre);
                    a++;
                    //break;
                }
            }

            lista.Insertar(numero, temp);
            lista.eliminar(numero);

            FileInputStream input = new FileInputStream(
                    //Direccion Daniel: "/Users/daniel/IdeaProjects/Proyecto-2-Text-Finder/src/Imagenes/texto.png"
                    //Dirección Esteban: "C:/Users/Personal/IdeaProjects/Proyecto #2/src/Imagenes/texto.png"
                    "C:/Users/Personal/IdeaProjects/Proyecto #2/src/Imagenes/texto.png");
            Image image = new Image(input, 100, 80, true, true);
            ImageView imageView = new ImageView(image);

            Label label = new Label("  " + temp.Nombre.toUpperCase(), imageView);
            Label labelSeparacion = new Label("  ");

            label.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent evento) {
                    movimientoDetectado(evento, imageView, temp.Nombre);
                }
            });

            vbox.getChildren().set(numero, label);


            area.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent evento) {
                    evento.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
            });

            area.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent evento) {
                    for (int num = 0; num < lista.getLargo(); num++) {
                        if (evento.getDragboard().getString().equals(lista.Obtener(num).Nombre)) {
                            try {
                                soltar(evento, lista.Obtener(num), area);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

        } catch (NullPointerException | IOException ignored) {
            alert.setTitle(" Precaución ");
            alert.setHeaderText(null);
            alert.setContentText("Error al agregar o leer el archivo");
            alert.showAndWait();
        } finally {
            a = 1;
        }
    }

    /**
     * Método que elimina un archivo de la biblioteca
     */
    public static void eliminarDeBiblioteca(ListaEnlazada<Archivo> listaEnlazada) {
        try {
            String[] listaArchivos = new String[listaEnlazada.getLargo()];
            if (listaEnlazada.getLargo() == 0){
                throw new ArrayIndexOutOfBoundsException(" Tamaño no aceptable ");
            } else {
                for (int i = 0; i < listaArchivos.length; i++) {
                    listaArchivos[i] = listaEnlazada.Obtener(i).getNombre();
                }

                ChoiceDialog<String> archivoPorEliminar = new ChoiceDialog<>(" Seleccione un archivo ", listaArchivos);

                archivoPorEliminar.setHeaderText(null);
                archivoPorEliminar.setContentText("  Escoger archivo a eliminar:                      ");
                archivoPorEliminar.setResizable(true);
                archivoPorEliminar.getDialogPane().setPrefWidth(600);
                archivoPorEliminar.showAndWait();

                int a = 0;
                for (int i = 0; i < listaArchivos.length; i++) {
                    if (archivoPorEliminar.getSelectedItem().equals(listaArchivos[i])) {
                        listaEnlazada.eliminar(i);
                        vbox.getChildren().remove(i);
                        a++;
                    }
                }
                if (a == 0){
                    Alert alert1 = new Alert(Alert.AlertType.WARNING);
                    alert1.setTitle(" Precaución ");
                    alert1.setHeaderText(null);
                    alert1.setContentText(" No se escogió algún archivo para eliminar de la biblioteca ");
                    alert1.setResizable(true);
                    alert1.getDialogPane().setPrefWidth(500);
                    alert1.showAndWait();
                }
            }

        } catch (Exception e) {
            Alert alert1 = new Alert(Alert.AlertType.WARNING);
            alert1.setTitle(" Precaución ");
            alert1.setHeaderText(null);
            alert1.setContentText(" No se pueden eliminar archivos porque la biblioteca está vacía ");
            alert1.setResizable(true);
            alert1.getDialogPane().setPrefWidth(550);
            alert1.showAndWait();
        }
    }





    // REVISAR
    /**
     * Método para indizar los archivos en la biblioteca
     * @param listaArchivos Lista enlazada de archivos
     */
    public static void indizar(ListaEnlazada<Archivo> listaArchivos) {
        try {
            for (int i = 0; i < listaArchivos.getLargo(); i++) {
                Archivo temp = listaArchivos.Obtener(i);
                temp.Asignar(temp.getURL().getName());
                System.out.println("Largo ListaArboles desde método indizar: " + Archivo.ListaArboles.getLargo());
                //Archivo.Asignar(listaArchivos.Obtener(i).getURL().getName());
            }

            Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
            confirmacion.setTitle(" Confirmación ");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText(" El o los archivos se han indizado correctamente");
            confirmacion.showAndWait();
        } catch (Exception ignored){

        }
    }








    /**
     * Método que ejecuta la acción de detectar el drag & drop
     * @param e Evento del mouse
     * @param imageView Imagen del archivo
     * @param Name Nombre del archivo
     */
    private static void movimientoDetectado(MouseEvent e, ImageView imageView, String Name) {
        Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(Name);
        db.setContent(content);
        e.consume();
    }

    /**
     * Método que agrega el texto seleccionado al área de texto de la ventana principal
     * @param e Evento del Mouse
     * @param x Archivo selecionado
     * @param textArea Área de texto principal
     * @throws IOException Excepción si el archivo no es válido
     */
    private static void soltar(DragEvent e, Archivo x, TextArea textArea) throws IOException {
        textArea.clear();
        String archivoDocx = x.getNombre().substring(x.getNombre().length() - 4, x.getNombre().length());
        String tipoArchivo = x.getNombre().substring(x.getNombre().length() - 3, x.getNombre().length());

        if (archivoDocx.equals("docx")) {
            // LO QUE SE AGREGA AL ÁREA DE TEXTO ES UN ARCHIVO .DOCX
            try {
                FileInputStream fis = new FileInputStream(x.getURL().getAbsolutePath());
                XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
                XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
                textArea.appendText(extractor.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // LO QUE SE AGREGA ES UN ARCHIVO .PDF
        } else if (tipoArchivo.equals("pdf")){
            PDFTextStripper tStripper = new PDFTextStripper();
            tStripper.setStartPage(1);
            tStripper.setEndPage(3);
            PDDocument document = PDDocument.load(x.getURL());
            document.getClass();
            String content = null;

            if (!document.isEncrypted()) {
                String pdfFileInText = tStripper.getText(document);
                String[] lines = pdfFileInText.split("\\r\\n\\r\\n");
                for (String line : lines) {
                    textArea.appendText(line);
                    content += line;
                }
            }

            assert content != null;

        }else {
            // LO QUE SE AGREGA AL ÁREA DE TEXTO ES UN ARCHIVO .TXT
            textArea.appendText(x.Texto);
        }
    }

    /**
     * Método que muestra el archivo en el área de texto de la ventana secundaria (coincidencias)
     *
     * @param lista Lista enlazada donde se almacenan los archivos
     */
    public static void mostrarArchivo(ListaEnlazada<Archivo> lista, Stage primaryStage) throws IOException {
        Busqueda.ventana(lista, primaryStage);
    }

    /**
     * Método para abrir el archivo seleccionado y agregarlo al área de texto
     * @param x Archivo seleccionado
     * @param area Barra de búsqueda
     * @param textArea Área de texto de la ventana secundaria
     */
    public static void abrirArchivo(Archivo x, TextField area, TextArea textArea) throws IOException {
        String palabra = (Archivo.limpiar(area.getText().toLowerCase()));
        if (x.getArbolPalabras().contains(palabra)) {

            BST.Nodo nodo = x.getArbolPalabras().Obtener(Archivo.limpiar(palabra));
            // CASO EN QUE EL ARCHIVO ES .DOCX
            if (x.getURL().getName().charAt(x.getURL().getName().length() - 1) == 'x') {
                try {

                    FileInputStream fis = new FileInputStream(x.getURL().getAbsolutePath());
                    XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
                    XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
                    textArea.appendText(extractor.getText());
                    Tooltip tooltip2 = new Tooltip("    Archivo :    " + x.getURL().getName());
                    tooltip2.setFont(Font.font("Cambria", 18));
                    textArea.setTooltip(tooltip2);
                    textArea.setEditable(false);


                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            int[] inter = Archivo.find(extractor.getText(), area.getText().toLowerCase());
                            textArea.selectRange(inter[0],inter[1]);
                        }
                    });

                    textArea.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (!textArea.getSelectedText().isEmpty()) {
                                textArea.deselect();
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        int[] inter = Archivo.find(x.listaLineas.Obtener(nodo.getFila()), area.getText().toLowerCase());
                                        textArea.selectRange(inter[0],inter[1]);
                                    }
                                });
                            }
                        }
                    });

                    textArea.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (textArea.getSelectedText().isEmpty()) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        int[] inter = Archivo.find(extractor.getText(), area.getText().toLowerCase());
                                        textArea.selectRange(inter[0],inter[1]);
                                    }
                                });
                            }
                        }
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }else {
                textArea.appendText(x.listaLineas.Obtener(nodo.getFila()));
                Tooltip tooltip2 = new Tooltip("    Archivo :    " + x.getURL().getName());
                tooltip2.setFont(Font.font("Cambria", 18));
                textArea.setTooltip(tooltip2);
                textArea.setEditable(false);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        int[] inter = Archivo.find(x.listaLineas.Obtener(nodo.getFila()), area.getText().toLowerCase());
                        textArea.selectRange(inter[0],inter[1]);
                    }
                });

                textArea.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (!textArea.getSelectedText().isEmpty()) {
                            textArea.deselect();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    int[] inter = Archivo.find(x.listaLineas.Obtener(nodo.getFila()), area.getText().toLowerCase());
                                    textArea.selectRange(inter[0],inter[1]);
                                }
                            });
                        }
                    }
                });

                textArea.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (textArea.getSelectedText().isEmpty()) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    int[] inter = Archivo.find(x.listaLineas.Obtener(nodo.getFila()), area.getText().toLowerCase());
                                    textArea.selectRange(inter[0],inter[1]);
                                }
                            });
                        }
                    }
                });
            }
        } else{
            textArea.setText(null);
        }

    }

    /**
     * Método para abrir el archivo seleccionado desde la computadora
     * @param file Archivo a abrir
     */
    public static void abrirThread(File file){
        if (!Desktop.isDesktopSupported()) {
            System.out.println("Desktop not supported");
            return;
        }

        if (!Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            System.out.println("File opening not supported");
            return;
        }

        final Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    System.err.println(e.toString());
                }
                return null;
            }
        };

        final Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }


    public static void verArchivoCompleto(ListaEnlazada<Archivo> lista,
                                          Stage primaryStage,
                                          Stage primaryStage2,
                                          String nombreArchivo){
        try{
            primaryStage2.setIconified(true);

            Stage stage2 = new Stage();

            BorderPane borderPane = new BorderPane();
            borderPane.setStyle("-fx-background-color: #ffe4b3");

            TextArea areaDeTexto2 = new TextArea();
            areaDeTexto2.setEditable(false);
            areaDeTexto2.setPrefSize(750d, 500d);
            areaDeTexto2.setMaxSize(750d,500d);
            areaDeTexto2.setLayoutX(200);
            areaDeTexto2.setLayoutY(90);
            areaDeTexto2.setWrapText(true);
            areaDeTexto2.setStyle("-fx-font-size: 1.5em; " +
                    "-fx-control-inner-background:#000000;" +
                    "-fx-font-family: Cambria;" +
                    "-fx-highlight-fill: #FF8933;" +
                    "-fx-highlight-text-fill: #000000;" +
                    "-fx-text-fill: #FFFFFF;");
            areaDeTexto2.setBorder(new Border(new BorderStroke(
                    Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            for (int i = 0; i < lista.getLargo(); i++) {
                if (lista.Obtener(i).getNombre().equals(nombreArchivo)) {
                    if (lista.Obtener(i).getNombre().charAt(lista.Obtener(i).getNombre().length() - 1) == 'x') {
                        FileInputStream fis = new FileInputStream(lista.Obtener(i).getURL().getAbsolutePath());
                        XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
                        XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
                        areaDeTexto2.appendText(extractor.getText());
                    } else {
                        areaDeTexto2.setText(lista.Obtener(i).getTexto());
                    }
                }
            }


            areaDeTexto2.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent e) {
                    ScaleTransition st = new ScaleTransition(Duration.millis(1000), areaDeTexto2);
                    st.setByX(1.1f);
                    st.setByY(1.1f);
                    st.setToX(1.2);
                    st.setToY(1.2);
                    st.setCycleCount((int) 1f);
                    st.setAutoReverse(false);
                    if (areaDeTexto2.getWidth() < 800d && areaDeTexto2.getHeight() < 550d){
                        st.play();
                    } else {
                        st.stop();
                    }
                }
            });
            areaDeTexto2.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent e) {
                    ScaleTransition st = new ScaleTransition(Duration.millis(1000), areaDeTexto2);
                    st.setByX(-1.1f);
                    st.setByY(-1.1f);
                    st.setToX(1);
                    st.setToY(1);
                    st.setCycleCount((int) 1f);
                    st.setAutoReverse(true);
                    if (areaDeTexto2.getWidth() >= 750d && areaDeTexto2.getHeight() >= 500d){
                        st.play();
                    } else {
                        st.stop();
                    }
                }
            });


            ToolBar toolBar2 = new ToolBar();
            toolBar2.setCursor(Cursor.HAND);
            toolBar2.setMaxSize(120d, 20d);
            toolBar2.setPrefSize(120d, 20d);
            toolBar2.setStyle("-fx-background-color: white;" +
                    "-fx-background-radius: 30;" +
                    "-fx-border-radius: 30; " +
                    "-fx-border-width:5;" +
                    "-fx-border-color: #000000;");
            Label label2 = new Label("     Atrás ");
            label2.setFont(Font.font("Cambria", 21));

            label2.setOnMousePressed(event -> {
                primaryStage.setIconified(false);
                stage2.close();
                //input.clear();
            });

            toolBar2.getItems().addAll(label2);
            BorderPane.setMargin(toolBar2, new Insets(20d, 20d, 10d, 40d));
            borderPane.setTop(toolBar2);
            BorderPane.setMargin(areaDeTexto2, new Insets(10d, 20d, 20d, 40d));
            borderPane.setCenter(areaDeTexto2);

            Scene scene2 = new Scene(borderPane, 1200, 750);

            stage2.setScene(scene2);
            stage2.setTitle(" Ocurrencias encontradas ");
            stage2.setX(100d);
            stage2.setY(100d);
            stage2.setResizable(false);
            stage2.show();

        } catch (Exception e){
            e.printStackTrace();
            /*
            Alert alert2 = new Alert(Alert.AlertType.WARNING);
            alert2.setTitle(" Precaución ");
            alert2.setHeaderText(null);
            alert2.setContentText("No se puede realizar la búsqueda porque " +
                   "no se han indizado los archivos de la biblioteca");
            alert2.showAndWait();
            */
        }
    }


    /**
     * Método para resaltar la ocurrencia en el texto a buscar
     * @param doc Documento docx
     * @param palabraBuscar String a buscar
     * @throws IOException Si el archivo no existe o no es válido
     */
    private static void find_replace_in_DOCX(XWPFDocument doc, String palabraBuscar) throws IOException{
        for (XWPFParagraph p : doc.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null && text.contains(palabraBuscar)) {
                        Text t2 = new Text();
                        t2.setStyle("-fx-fill: RED;-fx-font-weight:normal;");
                        t2.setText(palabraBuscar);
                        text = text.replace(palabraBuscar, palabraBuscar.toUpperCase());
                        r.setText(text, 0);
                    }
                }
            }
        }
        /*
         * for (XWPFTable tbl : doc.getTables()) { for (XWPFTableRow row :
         * tbl.getRows()) { for (XWPFTableCell cell : row.getTableCells()) {
         * for (XWPFParagraph p : cell.getParagraphs()) { for (XWPFRun r :
         * p.getRuns()) { String text = r.getText(0); if
         * (text.contains(“needle”)) { text = text.replace(“key”,
         * “kkk”); r.setText(text); } } } } } }
         */
        //doc.write(new FileOutputStream(“d:\\output.docx”));

    }

    /**
     * Método para reemplazar un caracter en el texto
     * @param doc Documento docx
     * @param findText Palabra a buscar
     * @param replaceText Palabra a reemplazar
     * @return String reemplazado
     */
    private static HWPFDocument replaceText(HWPFDocument doc, String findText, String replaceText) {
        Range r = doc.getRange();
        for (int i = 0; i < r.numSections(); ++i) {
            Section s = r.getSection(i);
            for (int j = 0; j < s.numParagraphs(); j++) {
                Paragraph p = s.getParagraph(j);
                for (int k = 0; k < p.numCharacterRuns(); k++) {
                    CharacterRun run = p.getCharacterRun(k);
                    String text = run.text();
                    if (text.contains(findText)) {
                        run.replaceText(findText, replaceText);
                    }
                }
            }
        }
        return doc;
    }

    /**
     * Método para abrir un documento de tipo docx
     * @param file Nombre del archivo
     * @return Documento docx parseado
     * @throws Exception Si el archivo no existe o no es válido, genera excepción
     */
    private  HWPFDocument openDocument(String file) throws Exception {
        URL res = getClass().getClassLoader().getResource(file);
        HWPFDocument document = null;
        if (res != null) {
            document = new HWPFDocument(new POIFSFileSystem(new File(res.getPath())));
        }
        return document;
    }

    /**
     * Método para escribir en un documento tipo docx
     * @param doc Documento docx
     * @param file Texto del documento
     */
    private void saveDocument(HWPFDocument doc, String file) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            doc.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

