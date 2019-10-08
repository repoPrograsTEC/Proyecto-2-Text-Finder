package AlgoritmosOrdenamiento;

import Estructuras.ListaEnlazada;

import java.util.Arrays;

public class QuickSort {
    public static void main(String[] args) {
        int[] x = {9, 2, 4, 7, 3, 7, 10};
        System.out.println(Arrays.toString(x));

        int low = 0;
        int high = x.length - 1;

        quickSort(x,new ListaEnlazada(), low, high);
        System.out.println(Arrays.toString(x));
    }
    public static void quickSort(int[] arr, ListaEnlazada<String> l, int low, int high) {
        if (l.getLargo() == 0){
            return;
        }
        if (low >= high)
            return;
        // escoger el pivote
        int middle = low + (high - low) / 2;
        String pivote = l.Obtener(middle);
        int pivot = arr[middle];

        // left < pivot y right > pivot
        int i = low, j = high;
        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }

            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }
        // Recursividad para hacer quick en los lados del pivote
        if (low < j)
            quickSort(arr,l, low, j);

        if (high > i)
            quickSort(arr,l, i, high);
    }
}