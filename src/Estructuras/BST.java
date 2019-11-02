package Estructuras;

/**
 * Clase BST que crea árboles binarios
 */
public class BST {
    /**
     * Clase Nodo que almacena los valores de las palabras
     */
    public static class Nodo {
        String element;
        Nodo right;
        Nodo left;
        int Fila;

        /**
         * Constructor clase Nodo
         * @param element Palabra del texto
         */
        Nodo(String element, int fila) {
            this.element = element;
            this.Fila=fila;
        }

        /**
         * Método que retorna la palabra del nodo
         * @return Palabra almacenada en el nodo
         */
        public String getElement() {
            return element;
        }

        public int getFila() {
            return Fila;
        }
    }

    /**
     * Variable para almacenar la cabeza del árbol
     */
    private Nodo root = null;

    /**
     * Método que indica si el árbol se encuentra vacío
     * @return Si el árbol está vacío o no
     */
    boolean isEmpty(){
        return root == null;
    }

    /**
     * Método que returna un boolean si se contiene la palabra indicada en el texto seleccionado
     * @param e Palabra a buscar
     * @return Si se contiene la palabra en el texto o no
     */
    public boolean contains (String e){

        return this.containsAux (e.toLowerCase(), root);
    }

    /**
     * Método auxiliar del método contains
     * @param e Palabra a buscar
     * @param current Nodo actual en donde se busca la coincidencia
     * @return Si la palabra se encontró o no
     */
    private boolean containsAux (String e, Nodo current){
        if (current == null) {
            return false;
        }
        int cmp = e.compareTo(current.element);
        if (cmp < 0) {
            return containsAux(e,current.left);
        } else if (cmp > 0) {
            return containsAux(e,current.right);
        } else {
            return true;
        }
    }

    /**
     * Método para obtener una palabra específica
     * @param e String a obtener
     * @return String encontrado
     */
    public Nodo Obtener(String e){
        return ObtenerAux(e, root);
    }

    /**
     * Método auxiliar para obtener la palabra específica
     * @param e String a obtener
     * @param current Nodo actual
     * @return Nodo actual en el que se encuentra la palabra
     */
    private Nodo ObtenerAux(String e, Nodo current){
        int cmp = e.compareTo(current.element);
        if (cmp < 0) {
            return ObtenerAux(e,current.left);
        } else if (cmp > 0) {
            return ObtenerAux(e,current.right);
        } else {
            return current;
        }
    }

    /**
     * Método que retorna el nodo menor del árbol
     * @return Nodo menor
     */
    public Nodo findMin (){
        return this.findMin (this.root);
    }

    /**
     * Método que retorna el nodo menor del árbol
     * @param current Nodo en donde se busca actualmente
     * @return Nodo menor del árbol
     */
    private Nodo findMin (Nodo current){
        if (current == null){
            return null;
        } else if (current.left == null){
            return current;
        } else {
            return findMin(current.left);
        }
    }

    /**
     * Método que inserta un nodo en el árbol
     * @param e Palabra a agregar
     */
    public void insert (String e, int Fila){
        root = this.insertAux(e.toLowerCase(), this.root, Fila);
    }

    /**
     * Método auxiliar del método insert que agrega un nodo al árbol
     * @param e Palabra a agregar
     * @param current Nodo actual
     * @return Nodo agregado al árbol
     */
    private Nodo insertAux (String e, Nodo current, int fila){
        if (current == null){
            return new Nodo(e, fila);
        } else if (e.compareTo(current.element) < 0){
            current.left = insertAux (e, current.left, fila);
        } else if (e.compareTo(current.element) > 0) {
            current.right = insertAux (e, current.right, fila);
        }
        return current;
    }
}