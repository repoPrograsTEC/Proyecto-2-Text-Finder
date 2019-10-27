package Eventos;

import AplicacionMain.Busqueda;
import Estructuras.ListaEnlazada;
import Objetos.Archivo;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static AplicacionMain.Main.input;
import static AplicacionMain.Main.vbox;

/**
 * Clase eventos en donde se agrupan todos los eventos a realizar en la aplicación
 */
public class Eventos {
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
                                           TextArea area, Stage primaryStage, int numero) throws IOException {

        List<File> files = escogerArchivo.showOpenMultipleDialog(primaryStage);
        try {
            for (int i = 0; i < files.size(); i++) {
                Archivo temp = new Archivo(files.get(i), files.get(i).getName(), numero);
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

                vbox.getChildren().addAll(label, labelSeparacion);
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
            alert.setContentText("Error al leer el archivo");
            alert.showAndWait();
        }
    }

    /**
     * Método que ejecuta la acción de detectar el drag & drop
     * @param e Evento del mouse
     * @param imageView Imagen del archivo
     * @param Name Nombre del archivo
     */
    public static void movimientoDetectado(MouseEvent e, ImageView imageView, String Name) {
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
    public static void soltar(DragEvent e, Archivo x, TextArea textArea) throws IOException {
        textArea.clear();
        String tipoArchivo = x.getNombre().substring(x.getNombre().length()-1);
        if (tipoArchivo.equals("x")) {
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
        } else if (tipoArchivo.equals("f")){
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
    public static TextArea abrirArchivo(Archivo x, TextField area, TextArea textArea) {
        if (x.getArbolPalabras().contains(Archivo.limpiar(area.getText().toLowerCase()))){
            if (x.getURL().getName().charAt(x.getURL().getName().length()-1) == 'x'){
                try {
                    FileInputStream fis = new FileInputStream(x.getURL().getAbsolutePath());
                    XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
                    XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
                    textArea.appendText(extractor.getText());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else{
                textArea.appendText(x.Texto);
            }
        } else{
            textArea.setText(null);
        }
        return textArea;
    }
}
