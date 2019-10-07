package Objetos;

import Estructuras.ListaEnlazada;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

public class Archivo {
    private ListaEnlazada<String> ListPalabras=new ListaEnlazada<>();
    private int Palabras;
    private LocalDateTime Date;
    private File URL;

    public Archivo(File URL) {
        this.URL = URL;
        setDate();
        setPalabras();
    }

    private void setDate(){
        this.Date=LocalDateTime.now();
    }
    private void setPalabras(){
        try {
            String linea;
            FileReader fr = new FileReader (URL);
            BufferedReader br = new BufferedReader(fr);
            int i,a=0,inicio=0;
            while((linea=br.readLine())!=null) {
                for(i=0;i<linea.length();i++){
                    if(i==0){
                        if(linea.charAt(i)!=' '){
                            inicio=i;
                            a++;}
                    }else{
                        if(linea.charAt(i-1)==' '){
                            if(linea.charAt(i)!=' ') {
                                ListPalabras.InsertarFinal(linea.substring(inicio,i));
                                inicio=i;
                                a++;}}
                    }
                }
                ListPalabras.InsertarFinal(linea.substring(inicio,i));
                inicio=0;
            }
            this.Palabras=a;
            fr.close();
        }
        catch(IOException a){
            System.out.println(a);
        }
    }
}
