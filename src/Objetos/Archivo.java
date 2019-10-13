package Objetos;

import Estructuras.BST;
import Estructuras.ListaEnlazada;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.time.LocalDateTime;

public class Archivo {
    private static final ListaEnlazada<BST> ListaArboles = new ListaEnlazada<>();
    private final BST ArbolPalabras = new BST();
    public int Palabras, numArchivo;
    public LocalDateTime Date;
    private File URL;
    public String Texto, Nombre;

    public Archivo(File URL, String Nombre, int numArchivo) throws IOException {
        this.Nombre=Nombre;
        this.URL = URL;
        this.numArchivo = numArchivo;
        setDate();
        System.out.println(URL.getName());
        Asignar(URL.getName());
    }

    private void Asignar(String URL) throws IOException {
        if (URL.charAt(URL.length()-1)=='x'){readDocxFile(this.URL);}
        if (URL.charAt(URL.length()-1)=='f'){readPDFFile(this.URL);}
        else{readTXTFile();}
    }

    private void setDate(){
        this.Date = LocalDateTime.now();
    }

    private void readPDFFile(File file) throws IOException {
        PDFTextStripper tStripper = new PDFTextStripper();
        tStripper.setStartPage(1);
        tStripper.setEndPage(3);
        PDDocument document = PDDocument.load(file);
        document.getClass();

        String content = null;
        if (!document.isEncrypted()) {
            String pdfFileInText = tStripper.getText(document);
            String[] lines = pdfFileInText.split("\\r\\n\\r\\n");
            for (String line : lines) {
                System.out.println(line);
                content += line;
            }
        }

        assert content != null;
        System.out.println(content.trim());
    }

    private void readDocxFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            XWPFDocument document = new XWPFDocument(OPCPackage.open(fis));
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            System.out.println(extractor.getText());
            //List<XWPFParagraph> paragraphs = document.getParagraphs();
            //for(int i = 0; i < paragraphs.size(); i++){
            //    System.out.println(paragraphs.get(i).getParagraphText());
            //}
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readTXTFile(){
        try {
            String linea;
            FileReader fr = new FileReader (URL);
            BufferedReader br = new BufferedReader(fr);
            int i=0, a = 0, inicio = 0;
            while((linea = br.readLine()) != null) {
                if (a == 0){
                    Texto += linea + "\n";
                    Texto = Texto.substring(4, linea.length());
                    for (i = 0; i < linea.length(); i++) {
                        if (i == 0) {
                            if (linea.charAt(i) != ' ') {
                                inicio = i;
                                a++;
                            }
                        } else {
                            if (linea.charAt(i - 1) == ' ') {
                                if (linea.charAt(i) != ' ') {
                                    ArbolPalabras.insert(limpiar(linea.substring(inicio, i)));
                                    inicio = i;
                                    a++;
                                }
                            }
                        }
                    }
                } else {
                    Texto += linea + "\n";
                    for (i = 0; i < linea.length(); i++) {
                        if (i == 0) {
                            if (linea.charAt(i) != ' ') {
                                inicio = i;
                                a++;
                            }
                        } else {
                            if (linea.charAt(i - 1) == ' ') {
                                if (linea.charAt(i) != ' ') {
                                    ArbolPalabras.insert(limpiar(linea.substring(inicio, i)));
                                    inicio = i;
                                    a++;
                                }
                            }
                        }
                    }
                }
                ArbolPalabras.insert(limpiar(linea.substring(inicio,i)));
                inicio = 0;
            }
            ListaArboles.InsertarFinal(ArbolPalabras);
            this.Palabras = a;
            fr.close();
        }
        catch(IOException ignored){
        }
    }

    public BST getArbolPalabras() {
        return ArbolPalabras;
    }

    public int getPalabras() {
        return Palabras;
    }

    public int getNumArchivo() {
        return numArchivo;
    }

    public File getURL() {
        return URL;
    }

    public String getTexto() {
        return Texto;
    }

    public String getNombre() {
        return Nombre;
    }

    public static String limpiar(String s){
        return s.replaceAll("[^a-zA-Z0-9]", "");
    }
}
