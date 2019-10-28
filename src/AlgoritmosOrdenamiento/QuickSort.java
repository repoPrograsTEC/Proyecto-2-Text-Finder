package AlgoritmosOrdenamiento;

import Estructuras.ListaEnlazada;
import Objetos.Archivo;

/**
 * Clase QuickSort que sirve para ordenar la búsqueda de archivos
 */
public class QuickSort {

    /**
     * Método que ordena los archivos de la lista enlazada
     * @param listaEnlazada lista enlazada de archivos
     * @param low Variable para comparar y escoger el pivote
     * @param high Variable para comparar y escoger el pivote
     */
    public static void quickSort(ListaEnlazada<Archivo> listaEnlazada, int low, int high) {
        if (listaEnlazada.getLargo() == 0){
            return;
        }
        if (low >= high)
            return;
        // escoger el pivote
        int middle = low + (high - low) / 2;
        //int pivot = arr[middle];
        String pivote = listaEnlazada.Obtener(middle).Nombre;

        // left < pivot y right > pivot
        int i = low, j = high;
        while (i <= j) {
            while (listaEnlazada.Obtener(i).Nombre.compareTo(pivote) > 0) {
                i++;
            }

            while (listaEnlazada.Obtener(j).Nombre.compareTo(pivote) < 0) {
                j--;
            }
            if (i <= j) {
                Archivo temp = listaEnlazada.Obtener(i);
                Archivo temp2= listaEnlazada.Obtener(j);
                listaEnlazada.eliminar(i);
                listaEnlazada.eliminar(j);
                listaEnlazada.Insertar(i,temp2);
                listaEnlazada.Insertar(j,temp);
                i++;
                j--;
            }
        }
        // Recursividad para hacer quick en los lados del pivote
        if (low < j)
            quickSort(listaEnlazada, low, j);
        if (high > i)
            quickSort(listaEnlazada, i, high);
    }
}