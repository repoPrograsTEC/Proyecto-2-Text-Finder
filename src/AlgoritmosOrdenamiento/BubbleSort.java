package AlgoritmosOrdenamiento;

import Estructuras.ListaEnlazada;
import Objetos.Archivo;

/**
 * Clase BubbleSort que sirve para ordenar la búsqueda de archivos
 */
public class BubbleSort {

    /**
     * Método que ordena los archivos de la lista enlazada
     * @param l Lista enlazada de archivos de la biblioteca
     */
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