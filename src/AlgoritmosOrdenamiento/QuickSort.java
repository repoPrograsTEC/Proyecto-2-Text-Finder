package AlgoritmosOrdenamiento;

import Estructuras.ListaEnlazada;
import Objetos.Archivo;

public class QuickSort {

    public static void quickSort(ListaEnlazada<Archivo> l, int low, int high) {
        if (l.getLargo() == 0){
            return;
        }
        if (low >= high)
            return;
        // escoger el pivote
        int middle = low + (high - low) / 2;
        //int pivot = arr[middle];
        String pivote = l.Obtener(middle).Nombre;

        // left < pivot y right > pivot
        int i = low, j = high;
        while (i <= j) {
            int comparationMenor=l.Obtener(i).Nombre.compareTo(pivote);
            while (l.Obtener(i).Nombre.compareTo(pivote)>0) {
                i++;
            }

            int comparationMayor=l.Obtener(j).Nombre.compareTo(pivote);
            while (l.Obtener(j).Nombre.compareTo(pivote)<0) {
                j--;
            }
            if (i <= j) {
                Archivo temp = l.Obtener(i);
                Archivo temp2= l.Obtener(j);
                l.eliminar(i);
                l.Insertar(i,temp2);
                l.eliminar(j);
                l.Insertar(j,temp);
                i++;
                j--;
            }
        }
        // Recursividad para hacer quick en los lados del pivote
        if (low < j)
            quickSort(l, low, j);

        if (high > i)
            quickSort(l, i, high);
    }
}