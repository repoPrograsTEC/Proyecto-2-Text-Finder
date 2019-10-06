package Estructuras;


/**
 * Clase para crear una lista enlazada simple
 */
public class ListaEnlazada<G>{
    /**
     * Variable para referenciar la cabeza de la lista
     */
    private Nodo<G> cabeza;
    /**
     * Variable para conocer el tamaño de la lista
     */
    private int largo;

    /**
     * Clase Nodo anidada a la clase ListaEnlazada
     */
    private class Nodo<G>{
        /**
         * Variable para manejar el puntero next del nodo actual
         */
        public Nodo<G> siguiente = null;
        /**
         * Variable para almacenar el archivo seleccionado
         */
        public G archivo;

        /**
         * Constructor clase Nodo
         * @param archivo - valor del nodo
         */
        public Nodo(G archivo) {
            this.archivo = archivo;
        }
    }

    /**
     * Método para insertar un nodo al inicio de la lista
     * @param archivo - Nodo a insertar
     */
    public void InsertarInicio(G archivo){
        Nodo<G> nodo = new Nodo(archivo);
        nodo.siguiente = cabeza;
        cabeza = nodo;
        largo++;
    }

    /**
     * Método para insertar un nodo al final de la lista
     * @param archivo - Nodo a insertar
     */
    public void InsertarFinal(G archivo){
        Nodo<G> nodo = new Nodo(archivo);
        if (cabeza == null){
            InsertarInicio(archivo);
        }else{
            Nodo<G> puntero = cabeza;
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
    public void Insertar(int indice,G archivo){
        Nodo<G> nodo = new Nodo(archivo);
        if (cabeza == null || indice == 0){
            this.InsertarInicio(archivo);
        }else{
            Nodo<G> puntero = cabeza;
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
    public G Obtener(int indice){
        if(cabeza == null){
            return null;
        }else{
            Nodo<G> puntero = cabeza;
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
            Nodo<G> primer = cabeza;
            cabeza = cabeza.siguiente;
            primer.siguiente = null;
            largo--;
        }
    }

    /**
     * Método para eliminar el nodo al final de la lista
     */
    public void eliminarUltimo(){
        if(cabeza != null){
            if(cabeza.siguiente == null){
                cabeza = null;
            }else{
                Nodo<G> puntero = cabeza;
                while (puntero.siguiente.siguiente != null){
                    puntero = puntero.siguiente;
                }
                puntero.siguiente = null;
                largo--;
            }
        }
    }

    /**
     * Método para eliminar el nodo en una posición específica de la lista
     * @param indice - Posición del nodo a eliminar.
     */
    public void eliminar(int indice){
        if (cabeza != null){
            if(indice == 0){
                this.eliminarPrimero();
            }else if(indice < largo) {
                Nodo<G> puntero = cabeza;
                int contador = 0;
                while (contador < (indice - 1)) {
                    puntero = puntero.siguiente;
                    contador++;
                }
                Nodo<G> temp = puntero.siguiente;
                puntero.siguiente = temp.siguiente;
                temp.siguiente = null;
                largo--;
            }
        }
    }

}
