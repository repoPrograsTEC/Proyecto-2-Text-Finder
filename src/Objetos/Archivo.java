package Objetos;

import Estructuras.BST;
import Estructuras.ListaEnlazada;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

public class Archivo {
    private static final ListaEnlazada<BST> ListaArboles=new ListaEnlazada<>();
    private BST ArbolPalabras= new BST();
    private int Palabras;
    private LocalDateTime Date;
    private File URL;
    public String Texto;

    public Archivo(File URL) {
        this.URL = URL;
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
            int i, a = 0, inicio = 0;
            while((linea = br.readLine()) != null) {
                Texto+=linea+"\n";
                for(i = 0; i < linea.length(); i++){
                    if(i == 0){
                        if(linea.charAt(i) != ' '){
                            inicio = i;
                            a++;}
                    }else{
                        if(linea.charAt(i-1) == ' '){
                            if(linea.charAt(i) != ' ') {
                                ArbolPalabras.insert(limpiar(linea.substring(inicio,i)));
                                inicio = i;
                                a++;} } }
                }
                ArbolPalabras.insert(limpiar(linea.substring(inicio,i)));
                inicio = 0;
            }
            ListaArboles.InsertarFinal(ArbolPalabras);
            this.Palabras = a;
            fr.close();
        }
        catch(IOException a){
            System.out.println(a);
        }
    }

    public File getURL() {
        return URL;
    }

    public BST getArbolPalabras() {
        return ArbolPalabras;
    }

    public String limpiar(String s){
        String a=s.replaceAll("[^a-zA-Z0-9]","");
        return a;
    }
}
