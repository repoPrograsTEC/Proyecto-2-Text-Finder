package Objetos;

import Estructuras.BST;
import Estructuras.ListaEnlazada;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

public class Archivo {
    private static final ListaEnlazada<BST> ListaArboles = new ListaEnlazada<>();
    private final BST ArbolPalabras = new BST();
    public int Palabras, numArchivo;
    public LocalDateTime Date;
    private File URL;
    public String Texto, Nombre;

    public Archivo(File URL, String Nombre, int numArchivo) {
        this.Nombre=Nombre;
        this.URL = URL;
        this.numArchivo = numArchivo;
        setDate();
        setPalabras();
    }

    private void setDate(){
        this.Date = LocalDateTime.now();
    }

    private void setPalabras(){
        try {
            String linea;
            FileReader fr = new FileReader (URL);
            BufferedReader br = new BufferedReader(fr);
            int i = 0, a = 0, inicio = 0;
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
                ArbolPalabras.insert(limpiar(linea.substring(inicio, i)));
                inicio = 0;
            }
            ListaArboles.InsertarFinal(ArbolPalabras);
            this.Palabras = a;
            fr.close();
        }
        catch(IOException ignored){
        }
    }

    public String getNombre() {
        return Nombre;
    }

    public File getURL() {
        return URL;
    }

    public BST getArbolPalabras() {
        return ArbolPalabras;
    }

    public static String limpiar(String s){
        return s.replaceAll("[^a-zA-Z0-9]", "");
    }
}
