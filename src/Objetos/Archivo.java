package Objetos;

import Estructuras.BST;
import Estructuras.ListaEnlazada;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase Archivo en la que se establecen atributos y métodos para cada documento indizado
 */
public class Archivo {
    /**
     * Variable en la que se guardan los árboles creados de cada archivo
     */
    public static final ListaEnlazada<BST> ListaArboles = new ListaEnlazada<>();
    /**
     * Variable que almacena las palabras del archivo en un árbol binario
     */
    private final BST ArbolPalabras = new BST();
    /**
     * Variable que contiene el número de palabras en el documento
     */
    public int Palabras;
    /**
     * Variable que contiene el número de archivo
     */
    public int numArchivo;
    /**
     * Variable que almacena la fecha en la que se agregó el documento a la biblioteca
     */
    public LocalDateTime Date;
    /**
     * Variable que almacena la ruta del archivo
     */
    private File URL;
    /**
     * Variable que almacena el texto del archivo
     */
    public String Texto;

    public ListaEnlazada<String> listaLineas = new ListaEnlazada<>();
    /**
     * Variable que almacena el texto del archivo
     */
    public String TextoCoincidencias ;
    /**
     * Variable para almacenar el nombre del archivo
     */
    public String Nombre;
    /**
     * Variable para saber si el archivo se elimina de la lista
     */
    public boolean eliminado;

    /**
     * Constructor clase Archivo
     * @param URL Extensión del archivo escogido
     * @param Nombre Nombre del archivo seleccionado
     * @param numArchivo Número de archivo para agregar
     * @throws IOException Excepción si el archivo no es correcto
     */
    public Archivo(File URL, String Nombre, int numArchivo) throws IOException {
        this.Nombre = Nombre;
        this.URL = URL;
        this.numArchivo = numArchivo;
        this.TextoCoincidencias = "";
        this.eliminado = false;
        setDate();
        //System.out.println("Largo ListaArboles desde clase Archivo: " + ListaArboles.getLargo());
        Asignar(URL.getName());
    }

    /**
     * Método que lee el archivo *.pdf
     * @param file Ruta del archivo
     * @throws IOException Excepción si el archivo no es correcto
     */
    private void readPDFFile(File file) throws IOException {
        // ELIMINAR ERROR DE LOGGER AL AGREGAR UN ARCHIVO .PDF
        String[] loggers = { "org.apache.pdfbox.util.PDFStreamEngine",
                "org.apache.pdfbox.pdmodel.font.PDSimpleFont",
                "org.apache.pdfbox.pdmodel.font.PDFont",
                "org.apache.pdfbox.pdmodel.font.FontManager",
                "org.apache.pdfbox.pdfparser.PDFObjectStreamParser",
                "org.apache.fontbox.ttf.PostScriptTable",
                "org.apache.pdfbox.io.ScratchFileBuffer",
                "org.apache.pdfbox.cos.COSDocument"};
        for (String logger : loggers) {
            org.apache.log4j.Logger logpdfengine = org.apache.log4j.Logger.getLogger(logger);
            logpdfengine.setLevel(org.apache.log4j.Level.OFF);
        }


        PDFTextStripper tStripper = new PDFTextStripper();
        tStripper.setStartPage(1);
        tStripper.setEndPage(3);
        PDDocument document = PDDocument.load(file);

        if (!document.isEncrypted()) {

            String pdfFileInText = tStripper.getText(document);

            String[] lines = pdfFileInText.split("\\r\\n\\r\\n");
            int i, inicio = 0, cantidadPalabras = 0, fila = 0;
            for (String linea : lines) {
                Texto += linea + "\n";
                Texto = Texto.substring(4, linea.length());
                listaLineas.InsertarFinal(linea + "\n");
                for (i = 0; i < linea.length(); i++) {
                    if (i == 0) {
                        if (linea.charAt(i) != ' ') {
                            inicio = i;
                            cantidadPalabras++;
                        }
                    } else {
                        if (linea.charAt(i - 1) == ' ') {
                            if (linea.charAt(i) != ' ') {
                                ArbolPalabras.insert(limpiar(linea.substring(inicio, i)), fila);
                                inicio = i;
                                cantidadPalabras++;
                            }
                        }
                    }
                }
                ArbolPalabras.insert(limpiar(linea.substring(inicio, i)), fila);
                inicio = 0;
                fila++;
            }
            ListaArboles.InsertarFinal(ArbolPalabras);
            this.Palabras = cantidadPalabras;
        }
        document.close();

    }

    /**
     * Método que lee el archivo *.docx
     * @param file Ruta del archivo
     * @throws IOException Excepción si el archivo no es correcto
     */
    private void readDocxFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
        XWPFDocument document = new XWPFDocument(fis);
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        int i, inicio = 0, cantidadPalabras = 0, fila = 0;
        for (XWPFParagraph linea : paragraphs) {
            Texto += linea.getText() + "\n";
            Texto = Texto.substring(4, linea.getText().length());
            listaLineas.InsertarFinal(linea+"\n");
            for (i = 0; i < linea.getText().length(); i++) {
                if (i == 0) {
                    if (linea.getText().charAt(i) != ' ') {
                        inicio = i;
                        cantidadPalabras++;
                    }
                } else {
                    if (linea.getText().charAt(i - 1) == ' ') {
                        if (linea.getText().charAt(i) != ' ') {
                            ArbolPalabras.insert(limpiar(linea.getText().substring(inicio, i)),fila);
                            inicio = i;
                            cantidadPalabras++;
                        }
                    }
                }
            }
            ArbolPalabras.insert(limpiar(linea.getParagraphText().substring(inicio,i)),fila);
            inicio = 0;
            fila++;
        }
        ListaArboles.InsertarFinal(ArbolPalabras);
        this.Palabras = cantidadPalabras;
        fis.close();
        document.close();
    }

    /**
     * Método que lee el archivo *.txt
     */
    private void readTXTFile(){
        try {
            String linea;
            FileReader fr = new FileReader (URL);
            BufferedReader br = new BufferedReader(fr);

            int i = 0, a = 0, inicio = 0, fila = 0;
            while((linea = br.readLine()) != null) {
                if (a == 0){
                    Texto += linea + "\n";
                    Texto = Texto.substring(4, Texto.length());
                    listaLineas.InsertarFinal(linea + "\n");

                    for (i = 0; i < linea.length(); i++) {
                        if (i == 0) {
                            if (linea.charAt(i) != ' ') {
                                inicio = i;
                                a++;
                            }
                        } else {
                            if (linea.charAt(i - 1) == ' ') {
                                if (linea.charAt(i) != ' ') {
                                    ArbolPalabras.insert(limpiar(linea.substring(inicio, i)),fila);
                                    inicio = i;
                                    a++;
                                }
                            }
                        }
                    }
                } else {
                    Texto += linea + "\n";
                    listaLineas.InsertarFinal(linea + "\n");
                    for (i = 0; i < linea.length(); i++) {
                        if (i == 0) {
                            if (linea.charAt(i) != ' ') {
                                inicio = i;
                                a++;
                            }
                        } else {
                            if (linea.charAt(i - 1) == ' ') {
                                if (linea.charAt(i) != ' ') {
                                    ArbolPalabras.insert(limpiar(linea.substring(inicio, i)),fila);
                                    inicio = i;
                                    a++;
                                }
                            }
                        }
                    }
                }
                ArbolPalabras.insert(limpiar(linea.substring(inicio,i)),fila);
                inicio = 0;
                fila++;
            }
            ListaArboles.InsertarFinal(ArbolPalabras);
            this.Palabras = a;
            fr.close();
            br.close();
        }
        catch(IOException ignored){
        }
    }





    // REVISAR
    /**
     * Método que lee el archivo *.pdf
     * @param file Ruta del archivo
     * @throws IOException Excepción si el archivo no es correcto
     */
    public static void buscarEnPdf(Archivo file, String palabraParaBuscar) throws IOException {
        // ELIMINAR ERROR DE LOGGER AL AGREGAR UN ARCHIVO .PDF
        String[] loggers = { "org.apache.pdfbox.util.PDFStreamEngine",
                "org.apache.pdfbox.pdmodel.font.PDSimpleFont",
                "org.apache.pdfbox.pdmodel.font.PDFont",
                "org.apache.pdfbox.pdmodel.font.FontManager",
                "org.apache.pdfbox.pdfparser.PDFObjectStreamParser",
                "org.apache.fontbox.ttf.PostScriptTable",
                "org.apache.pdfbox.io.ScratchFileBuffer",
                "org.apache.pdfbox.cos.COSDocument"};
        for (String logger : loggers) {
            org.apache.log4j.Logger logpdfengine = org.apache.log4j.Logger.getLogger(logger);
            logpdfengine.setLevel(org.apache.log4j.Level.OFF);
        }


        PDFTextStripper tStripper = new PDFTextStripper();
        tStripper.setStartPage(1);
        tStripper.setEndPage(3);
        PDDocument document = PDDocument.load(file.getURL());

        if (!document.isEncrypted()) {

            String pdfFileInText = tStripper.getText(document);

            String[] lines = pdfFileInText.split("\\r\\n\\r\\n");
            int i, inicio = 0, cantidadPalabras = 0, fila = 0;
            for (String linea : lines) {
                file.Texto += linea + "\n";
                file.Texto = file.Texto.substring(4, linea.length());
                file.listaLineas.InsertarFinal(linea + "\n");
                for (i = 0; i < linea.length(); i++) {
                    if (i == 0) {
                        if (linea.charAt(i) != ' ') {
                            inicio = i;
                            cantidadPalabras++;
                        }
                    } else {
                        if (linea.charAt(i - 1) == ' ') {
                            if (linea.charAt(i) != ' ') {
                                //ArbolPalabras.insert(limpiar(linea.substring(inicio, i)), fila);
                                String palabraEnTexto = linea.substring(inicio + 1  , i).toLowerCase();
                                if(palabraEnTexto.equals(palabraParaBuscar)){
                                        find(palabraEnTexto,palabraParaBuscar);
                                }
                                inicio = i;
                                cantidadPalabras++;
                            }
                        }
                    }
                }
                //ArbolPalabras.insert(limpiar(linea.substring(inicio, i)), fila);
                inicio = 0;
                fila++;
            }
            //ListaArboles.InsertarFinal(ArbolPalabras);
            //this.Palabras = cantidadPalabras;
        }
        document.close();

    }

    /**
     * Método que lee el archivo *.docx
     * @param file Ruta del archivo
     * @throws IOException Excepción si el archivo no es correcto
     */
    /*public static void readDocxFileCoincidence(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
        XWPFDocument document = new XWPFDocument(fis);
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        int i, inicio = 0, cantidadPalabras = 0, fila = 0;
        for (XWPFParagraph linea : paragraphs) {
            Texto += linea.getText() + "\n";
            Texto = Texto.substring(4, linea.getText().length());
            listaLineas.InsertarFinal(linea+"\n");
            for (i = 0; i < linea.getText().length(); i++) {
                if (i == 0) {
                    if (linea.getText().charAt(i) != ' ') {
                        inicio = i;
                        cantidadPalabras++;
                    }
                } else {
                    if (linea.getText().charAt(i - 1) == ' ') {
                        if (linea.getText().charAt(i) != ' ') {
                            ArbolPalabras.insert(limpiar(linea.getText().substring(inicio, i)),fila);
                            inicio = i;
                            cantidadPalabras++;
                        }
                    }
                }
            }
            ArbolPalabras.insert(limpiar(linea.getParagraphText().substring(inicio,i)),fila);
            inicio = 0;
            fila++;
        }
        ListaArboles.InsertarFinal(ArbolPalabras);
        this.Palabras = cantidadPalabras;
        fis.close();
        document.close();
    }
    */

    /**
     * Método que lee el archivo *.txt
     */
    /*public static void readTXTFileCoincidence(){
        try {
            String linea;
            FileReader fr = new FileReader (URL);
            BufferedReader br = new BufferedReader(fr);

            int i = 0, a = 0, inicio = 0, fila = 0;
            while((linea = br.readLine()) != null) {
                if (a == 0){
                    Texto += linea + "\n";
                    Texto = Texto.substring(4, Texto.length());
                    listaLineas.InsertarFinal(linea + "\n");

                    for (i = 0; i < linea.length(); i++) {
                        if (i == 0) {
                            if (linea.charAt(i) != ' ') {
                                inicio = i;
                                a++;
                            }
                        } else {
                            if (linea.charAt(i - 1) == ' ') {
                                if (linea.charAt(i) != ' ') {
                                    ArbolPalabras.insert(limpiar(linea.substring(inicio, i)),fila);
                                    inicio = i;
                                    a++;
                                }
                            }
                        }
                    }
                } else {
                    Texto += linea + "\n";
                    listaLineas.InsertarFinal(linea + "\n");
                    for (i = 0; i < linea.length(); i++) {
                        if (i == 0) {
                            if (linea.charAt(i) != ' ') {
                                inicio = i;
                                a++;
                            }
                        } else {
                            if (linea.charAt(i - 1) == ' ') {
                                if (linea.charAt(i) != ' ') {
                                    ArbolPalabras.insert(limpiar(linea.substring(inicio, i)),fila);
                                    inicio = i;
                                    a++;
                                }
                            }
                        }
                    }
                }
                ArbolPalabras.insert(limpiar(linea.substring(inicio,i)),fila);
                inicio = 0;
                fila++;
            }
            ListaArboles.InsertarFinal(ArbolPalabras);
            this.Palabras = a;
            fr.close();
            br.close();
        }
        catch(IOException ignored){
        }
    }
    */







    /**
     * Método que asigna cual documento se debe leer
     * @param URL Nombre del archivo
     * @throws IOException Excepción si el archivo no es correcto
     */
    public void Asignar(String URL) throws IOException {
        String nombre = URL.substring(URL.length() - 4 , URL.length());
        if (nombre.equals("docx")) {
            readDocxFile(this.URL);
        } else if (nombre.equals(".pdf")){
            readPDFFile(this.URL);
        } else {
            readTXTFile();
        }
    }

    /**
     * Método para establecer la fecha de ingreso del documento a la biblioteca
     */
    private void setDate(){
        this.Date = LocalDateTime.now();
    }

    /**
     * Método que retorna el árbol binario del archivo deseado
     * @return Árbol binario del archivo
     */
    public BST getArbolPalabras() {
        return ArbolPalabras;
    }

    /**
     * Método que retorna la cantidad de palabras en el documento
     * @return Cantidad de  palabras en el documento
     */
    public int getPalabras() {
        return Palabras;
    }

    /**
     * Método que retorna el número de archivo
     * @return Entero correspondiente al número del archivo
     */
    public int getNumArchivo() {
        return numArchivo;
    }

    /**
     * Método que retorna la ruta del archivo
     * @return Ruta del archivo deseado
     */
    public File getURL() {
        return URL;
    }

    /**
     * Método que retorn el texto del archivo
     * @return Texto del archivo
     */
    public String getTexto() {
        return Texto;
    }

    /**
     * Método que retorna el nombre del archivo
     * @return Nombre del archivo
     */
    public String getNombre() {
        return Nombre;
    }

    /**
     * Método para establecer el nombre del archivo
     * @param nombre String con el nombre del archivo
     */
    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    /**
     * Método que elimina los caracteres innecesarios del texto
     * @param s String a limpiar
     * @return String sin caracteres no deseados
     */
    public static String limpiar(String s){
        return s.replaceAll("[^a-zA-Z0-9]", "");
    }

    /**
     * Método para encontrar una palabra
     * @param texto Texto del archivo
     * @param palabra Palabra a buscar
     * @return Coincidencia de palabra en el texto
     */
    public static int[] find(String texto, String palabra){
        int inicio = 0, fin = 0, cont = 0;
        texto = texto.toLowerCase();
        palabra = palabra.toLowerCase();
        for (int x = 0; x < texto.length(); x++){
            if (texto.charAt(x) == palabra.charAt(0)){
                cont++;
                inicio=x;
            }
            else if(texto.charAt(x) == palabra.charAt(cont)) {
                if (cont == palabra.length() - 1) {
                    fin = x;
                    break;
                } else {
                    cont++;
                }
            } else{
                inicio = 0;
                fin = 0;
                cont = 0;
            }
        }
        return new int[]{inicio, fin+1};
    }
}
