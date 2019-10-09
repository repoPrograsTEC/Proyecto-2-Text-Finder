package AlgoritmosOrdenamiento;

import Estructuras.ListaEnlazada;
import Objetos.Archivo;

public class BubbleSort {
    public static void bubbleSort(ListaEnlazada<Archivo> l) {
        int n = l.getLargo();
        Archivo temp;
        for(int i=0; i < n; i++){
            for(int j=1; j < (n-i); j++){
                if(l.Obtener(j-1).Date.compareTo(l.Obtener(j).Date)>0){
                    //cambiar archivos
                    Archivo temp2=l.Obtener(j);
                    temp = l.Obtener(j-1);
                    l.eliminar(j-1);
                    l.Insertar(j-1,temp2);
                    l.eliminar(j);
                    l.Insertar(j,temp);
                }
            }
        }
    }
}