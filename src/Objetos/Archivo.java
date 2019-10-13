package Objetos;

import Estructuras.BST;
import Estructuras.ListaEnlazada;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

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

        if (!document.isEncrypted()) {

            String pdfFileInText = tStripper.getText(document);

            String[] lines = pdfFileInText.split("\\r\\n\\r\\n");
            int i,inicio=0,a=0;
            for (String linea : lines) {
                Texto+=linea+"\n";
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
                ArbolPalabras.insert(limpiar(linea.substring(inicio,i)));
                inicio = 0;

            }
            ListaArboles.InsertarFinal(ArbolPalabras);
            this.Palabras = a;
        }
        document.close();
    }

    private void readDocxFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
        XWPFDocument document = new XWPFDocument(fis);
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        int i,inicio=0,a=0;
        for (XWPFParagraph linea : paragraphs) {
            System.out.println(linea.getParagraphText());
            Texto+=linea.getParagraphText()+"\n";
            System.out.println(Texto);
            Texto = Texto.substring(4, linea.getParagraphText().length());
            for (i = 0; i < linea.getParagraphText().length(); i++) {
                if (i == 0) {
                    if (linea.getParagraphText().charAt(i) != ' ') {
                        inicio = i;
                        a++;
                    }
                } else {
                    if (linea.getParagraphText().charAt(i - 1) == ' ') {
                        if (linea.getParagraphText().charAt(i) != ' ') {
                            ArbolPalabras.insert(limpiar(linea.getParagraphText().substring(inicio, i)));
                            inicio = i;
                            a++;
                        }
                    }
                }
            }
            ArbolPalabras.insert(limpiar(linea.getParagraphText().substring(inicio,i)));
            inicio = 0;
        }
        ListaArboles.InsertarFinal(ArbolPalabras);
        this.Palabras = a;
        fis.close();

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

    public static String limpiar(String s){
        return s.replaceAll("[^a-zA-Z0-9]", "");
    }
}
