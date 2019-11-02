package AlgoritmosOrdenamiento;

import Estructuras.ListaEnlazada;
import Objetos.Archivo;
import java.util.Arrays;

/**
 * Clase QuickSort que sirve para ordenar la búsqueda de archivos
 */
public class QuickSort {

    public static void quickSort(ListaEnlazada<Archivo> lista) {

        if (lista == null || lista.getLargo() == 0) {
            return;
        }

        String [] array = new String [lista.getLargo()];
        for (int i = 0; i < lista.getLargo(); i++){
            array[i] = lista.Obtener(i).Nombre.toLowerCase();
        }

        quicksort(array,0, array.length - 1);
        Reconocer(lista,array);

    }

    private static void quicksort(String[] names, int lowerIndex, int higherIndex) {
        int i = lowerIndex;
        int j = higherIndex;
        String pivot = names[lowerIndex + (higherIndex - lowerIndex) / 2];

        while (i <= j) {
            while (names[i].compareToIgnoreCase(pivot) < 0) {
                i++;
            }

            while (names[j].compareToIgnoreCase(pivot) > 0) {
                j--;
            }

            if (i <= j) {
                exchangeNames(names, i, j);
                i++;
                j--;
            }
        }
        //call quickSort recursively
        if (lowerIndex < j) {
            quicksort(names,lowerIndex, j);
        }
        if (i < higherIndex) {
            quicksort(names, i, higherIndex);
        }
    }

    private static void exchangeNames(String[] names ,int i, int j) {
        String temp = names[i];
        names[i] = names[j];
        names[j] = temp;
    }

    private static void Reconocer(ListaEnlazada<Archivo> lista, String[] arr){
        int num = 0;
        for (String i : arr){
            for (int x = 0; x < lista.getLargo(); x++){
                String palabras = lista.Obtener(x).Nombre.toLowerCase();
                if (palabras.equals(i)){
                    Archivo ordenar = lista.Obtener(x);
                    lista.eliminar(x);
                    lista.Insertar(num,ordenar);
                    break;
                }
            }
            num++;
        }
    }


}