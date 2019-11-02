package AlgoritmosOrdenamiento;

import java.util.Arrays;

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
    /*
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
    */

    private static int array[];

    public static void sort(int[] arr) {

        if (arr == null || arr.length == 0) {
            return;
        }
        array = arr;
        quickSort(0, array.length-1);
    }

    private static void quickSort(int left, int right) {

        int i = left;
        int j = right;
        // find pivot number, we will take it as mid
        int pivot = array[left+(right-left)/2];

        while (i <= j) {
            /**
             * In each iteration, we will increment left until we find element greater than pivot
             * We will decrement right until we find element less than pivot
             */
            while (array[i] < pivot) {
                i++;
            } while (array[j] > pivot) {
                j--;
            }
            if (i <= j) {
                exchange(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (left < j)
            quickSort(left, j);
        if (i < right)
            quickSort(i, right);
    }

    private static void exchange(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String a[]){
        int[] input = {33,21,45,64,55,34,11,8,3,5,1};
        System.out.println("Before Sorting : ");
        System.out.println(Arrays.toString(input));
        sort(input);
        System.out.println("==================");
        System.out.println("After Sorting : ");
        System.out.println(Arrays.toString(array));

    }
}