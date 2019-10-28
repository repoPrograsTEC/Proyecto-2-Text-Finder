package AlgoritmosOrdenamiento;

import Estructuras.ListaEnlazada;
import Objetos.Archivo;

/**
 * Clase BubbleSort que sirve para ordenar la búsqueda de archivos
 */
public class BubbleSort {

    /**
     * Método que ordena los archivos de la lista enlazada
     * @param listaEnlazada Lista enlazada de archivos de la biblioteca
     */
    public static void bubbleSort(ListaEnlazada<Archivo> listaEnlazada) {
        int n = listaEnlazada.getLargo();
        Archivo temp;
        for(int i = 0; i < n; i++){
            for(int j = 1; j < (n-i); j++){
                if(listaEnlazada.Obtener(j-1).Date.compareTo(listaEnlazada.Obtener(j).Date)>0){
                    //cambiar archivos
                    Archivo temp2=listaEnlazada.Obtener(j);
                    temp = listaEnlazada.Obtener(j-1);
                    listaEnlazada.eliminar(j-1);
                    listaEnlazada.Insertar(j-1,temp2);
                    listaEnlazada.eliminar(j);
                    listaEnlazada.Insertar(j,temp);
                }
            }
        }
    }
}