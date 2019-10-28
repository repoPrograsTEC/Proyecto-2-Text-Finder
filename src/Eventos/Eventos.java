package Eventos;

import AplicacionMain.Busqueda;
import Estructuras.BST;
import Estructuras.ListaEnlazada;
import Objetos.Archivo;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
                lista.InsertarFinal(temp);

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
     * Método que elimina un archivo de la biblioteca
     */
    public static void eliminarDeBiblioteca(ListaEnlazada<Archivo> listaEnlazada) {
        String[] listaArchivos = new String[listaEnlazada.getLargo()];
        for (int i = 0; i < listaArchivos.length; i++) {
            listaArchivos[i] = listaEnlazada.Obtener(i).getURL().getName();
        }

        ChoiceDialog d = new ChoiceDialog(listaArchivos[0], listaArchivos);

        d.setHeaderText(null);
        d.setContentText("  Escoger archivo a eliminar:  ");
        d.showAndWait();

        System.out.println(d.getSelectedItem());
        for (int i = 0; i < listaArchivos.length; i++) {
            if (d.getSelectedItem().equals(listaArchivos[i])){
                listaEnlazada.eliminar(i);
            }
        }

        //Main.scrollpane.getContent();
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
    public static void abrirArchivo(Archivo x, TextField area, TextArea textArea){
        String palabra=(Archivo.limpiar(area.getText().toLowerCase()));
        if (x.getArbolPalabras().contains(palabra)) {

            BST.Nodo nodo = x.getArbolPalabras().Obtener(Archivo.limpiar(palabra));
            // CASO EN QUE EL ARCHIVO ES .DOCX
            if (x.getURL().getName().charAt(x.getURL().getName().length() - 1) == 'x') {
                try {
                    /*
                    FileInputStream fis = new FileInputStream(x.getURL().getAbsolutePath());
                    XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
                    XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
                    textArea.appendText(extractor.getText());
                    */
                    x.Texto = x.Texto.toLowerCase();
                    FileInputStream fis = new FileInputStream(x.getURL().getAbsolutePath());
                    XWPFDocument doc = new XWPFDocument(OPCPackage.open(fis));
                    if (doc != null) {
                        find_replace_in_DOCX(doc, area.getText());
                        //textArea.appendText("          Archivo:  " + x.getURL().getName() + "\n");
                        textArea.appendText(new XWPFWordExtractor(doc).getText());
                        Tooltip tooltip2 = new Tooltip("    Archivo :    " + x.getURL().getName());
                        tooltip2.setFont(Font.font("Cambria", 18));
                        textArea.setTooltip(tooltip2);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                textArea.appendText(x.listaLineas.Obtener(nodo.getFila()));

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        int[] inter = Archivo.find(x.listaLineas.Obtener(nodo.getFila()), area.getText().toLowerCase());
                        textArea.selectRange(inter[0],inter[1]);
                    }
                });
            }
        } else{
            textArea.setText(null);
        }

    }

    /**
     * Método para abrir el archivo seleeccionado
     * @param file Archivo a abrir
     */
    public static void abrirThread (File file){
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

