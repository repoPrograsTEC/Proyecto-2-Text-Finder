package AlgoritmosOrdenamiento;

import Estructuras.ListaEnlazada;
import Objetos.Archivo;

import java.util.Arrays;

/**
 * Clase RadixSort que sirve para ordenar la búsqueda de archivos
 */
public class RadixSort {
    /**
     * Método para ordenar los elementos del array
     * @param lista lista de archivos para ordenar.
     */
    public static void radixsort(ListaEnlazada<Archivo> lista) {
        int [] arr = new int[lista.getLargo()];
        for (int i = 0; i < lista.getLargo(); i++){
            arr[i] = lista.Obtener(i).Palabras;
        }
        int m = getMax(arr, arr.length);
        for (int exp = 1; m/exp > 0; exp *= 10){
            countSort(arr, arr.length, exp);}

        Reconocer(lista,arr);

    }

    /**
     * Método para contar los elementos ordenados
     * @param arr Array de enteros
     * @param n Largo del array
     * @param exp Base numérica para realizar división y ordenar los elementos
     */
    private static void countSort(int[] arr, int n, int exp) {
        int[] output = new int[n]; // output array
        int i;
        int[] count = new int[10];
        Arrays.fill(count,0);

        for (i = 0; i < n; i++)
            count[ (arr[i]/exp)%10 ]++;

        for (i = 1; i < 10; i++)
            count[i] += count[i - 1];

        for (i = n - 1; i >= 0; i--)
        {
            output[count[ (arr[i]/exp)%10 ] - 1] = arr[i];
            count[ (arr[i]/exp)%10 ]--;
        }
        for (i = 0; i < n; i++)
            arr[i] = output[i];
    }

    /**
     * Método para reconocer cual archivo tiene mayor tamaño
     * @param lista Lista enlazada de archivos
     * @param arr Array de enteros con las cantidades de palabras de los archivos
     */
    private static void Reconocer(ListaEnlazada<Archivo> lista, int[] arr){
        int num = 0;
        for (int i : arr){
            for (int x = 0; x < lista.getLargo(); x++){
                int palabras = lista.Obtener(x).Palabras;
                if (palabras == i){
                    Archivo ordenar = lista.Obtener(x);
                    lista.eliminar(x);
                    lista.Insertar(num,ordenar);
                    break;
                }
            }
            num++;
        }
    }

    /**
     * Método para obtener el valor máximo del array
     * @param arr Array de enteros
     * @param n Largo del array
     * @return Valor máximo del array
     */
    private static int getMax(int[] arr, int n) {
        int mx = arr[0];
        for (int i = 1; i < n; i++)
            if (arr[i] > mx)
                mx = arr[i];
        return mx;
    }

}
