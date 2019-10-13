package AlgoritmosOrdenamiento;

import java.util.*;

/**
 * Clase RadixSort que sirve para ordenar la búsqueda de archivos
 */
class RadixSort {

    /**
     * Método para obtener el valor máximo del array
     * @param arr Array de enteros
     * @param n Largo del array
     * @return Valor máximo del array
     */
    static int getMax(int arr[], int n) {
        int mx = arr[0];
        for (int i = 1; i < n; i++)
            if (arr[i] > mx)
                mx = arr[i];
        return mx;
    }

    // A function to do counting sort of arr[] according to
    // the digit represented by exp.
    /**
     * Método para contar los elementos ordenados
     * @param arr Array de enteros
     * @param n Largo del array
     * @param exp Base numérica para realizar división y ordenar los elementos
     */
    static void countSort(int arr[], int n, int exp) {
        int output[] = new int[n]; // output array
        int i;
        int count[] = new int[10];
        Arrays.fill(count,0);

        // Store count of occurrences in count[]
        for (i = 0; i < n; i++)
            count[ (arr[i]/exp)%10 ]++;

        // Change count[i] so that count[i] now contains
        // actual position of this digit in output[]
        for (i = 1; i < 10; i++)
            count[i] += count[i - 1];

        // Build the output array
        for (i = n - 1; i >= 0; i--)
        {
            output[count[ (arr[i]/exp)%10 ] - 1] = arr[i];
            count[ (arr[i]/exp)%10 ]--;
        }

        // Copy the output array to arr[], so that arr[] now
        // contains sorted numbers according to curent digit
        for (i = 0; i < n; i++)
            arr[i] = output[i];
    }

    // The main function to that sorts arr[] of size n using
    // RadixSort Sort
    /**
     * Método para ordenar los elementos del array
     * @param arr Array de enteros
     */
    static void radixsort(int arr[]) {
        // Find the maximum number to know number of digits
        int m = getMax(arr, arr.length);

        // Do counting sort for every digit. Note that instead
        // of passing digit number, exp is passed. exp is 10^i
        // where i is current digit number
        for (int exp = 1; m/exp > 0; exp *= 10)
            countSort(arr, arr.length, exp);
    }

    // A utility function to print an array
    /**
     * Método para imprimir el array
     * @param arr Array de enteros
     * @param n Largo del array
     */
    static void print(int arr[], int n) {
        for (int i = 0; i < n; i++)
            System.out.print(arr[i]+" ");
    }

    /*
    // PRUEBA DEL ALGORITMO DE ORDENAMIENTO
    public static void main (String[] args)
    {
        int arr[] = {170, 45, 75, 90, 802, 24, 2, 66};
        int n = arr.length;
        radixsort(arr);
        print(arr, n);
    }

     */
}
