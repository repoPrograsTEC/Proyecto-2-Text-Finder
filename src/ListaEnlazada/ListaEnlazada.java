package ListaEnlazada;

import java.io.File;

/**
 * Clase para crear una lista simple enlazada
 */
public class ListaEnlazada{
    /**
     * Variable para referenciar la cabeza de la lista
     */
    private Nodo cabeza;
    /**
     * Variable para conocer el tamañoo de la lista
     */
    private int largo;

    /**
     * Clase Nodo anidada a la clase ListaEnlazada
     */
    private class Nodo{
        public Nodo siguiente = null;
        public File archivo;

        /**
         * Constructor clase Nodo
         * @param archivo - valor del nodo
         */
        public Nodo(File archivo) {
            this.archivo = archivo;
        }
    }

    /**
     * Método para insertar un nodo al inicio de la lista
     * @param archivo - Nodo a insertar
     */
    public void InsertarInicio(File archivo){
        Nodo nodo = new Nodo(archivo);
        nodo.siguiente = cabeza;
        cabeza = nodo;
        largo++;
    }

    /**
     * Método para insertar un nodo al final de la lista
     * @param archivo - Nodo a insertar
     */
    public void InsertarFinal(File archivo){
        Nodo nodo = new Nodo(archivo);
        if (cabeza == null){
            InsertarInicio(archivo);
        }else{
            Nodo puntero = cabeza;
            while(puntero.siguiente != null){
                puntero = puntero.siguiente;
            }
            puntero.siguiente = nodo;
            largo++;
        }
    }

    /**
     * Método para insertar un valor en una posición específica de la lista
     * @param indice - index de la lista en el que se desea insertar
     * @param archivo - Valor que se insertará
     */
    public void Insertar(int indice, File archivo){
        Nodo nodo = new Nodo(archivo);
        if (cabeza == null || indice == 0){
            this.InsertarInicio(archivo);
        }else{
            Nodo puntero = cabeza;
            int contador = 0;
            while(contador < indice && puntero.siguiente != null){
                puntero = puntero.siguiente;
                contador++;
            }
            nodo.siguiente = puntero.siguiente;
            puntero.siguiente = nodo;
            largo++;
        }
    }

    /**
     * Método para obtener el valor en un índice específico de la lista.
     * @param indice - Numero de posicion a obtener
     * @return Archivo en el índice especificado
     */
    public File Obtener(int indice){
        if(cabeza == null){
            return null;
        }else{
            Nodo puntero = cabeza;
            int contador = 0;
            while (contador < indice && puntero.siguiente != null){
                puntero = puntero.siguiente;
                contador++;
            }
            if(contador != indice){
                return null;
            }else{
                return puntero.archivo;
            }
        }
    }

    /**
     * Método que retorna el tamaño de la lista
     * @return  Variable largo de la lista
     */
    public int getLargo(){
        return largo;
    }

    /**
     * Método para eliminar el primer elemento de la lista
     */
    public void eliminarPrimero(){
        if (cabeza != null) {
            Nodo primer = cabeza;
            cabeza = cabeza.siguiente;
            primer.siguiente = null;
            largo--;
        }
    }

    /**
     * Método para eliminae el nodo al final de la lista
     */
    public void eliminarUltimo(){
        if(cabeza != null){
            if(cabeza.siguiente == null){
                cabeza = null;
            }else{
                Nodo puntero = cabeza;
                while (puntero.siguiente.siguiente != null){
                    puntero = puntero.siguiente;
                }
                puntero.siguiente = null;
                largo--;
            }
        }
    }

    /**
     * Método para eliminar el nodo en una posicion específica de la lista
     * @param indice - Posición del nodo a eliminar.
     */
    public void eliminar(int indice){
        if (cabeza != null){
            if(indice == 0){
                this.eliminarPrimero();
            }else if(indice < largo) {
                Nodo puntero = cabeza;
                int contador = 0;
                while (contador < (indice - 1)) {
                    puntero = puntero.siguiente;
                    contador++;
                }
                Nodo temp = puntero.siguiente;
                puntero.siguiente = temp.siguiente;
                temp.siguiente = null;
                largo--;
            }
        }
    }

}
