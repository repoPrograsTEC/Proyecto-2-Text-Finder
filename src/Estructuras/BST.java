package Estructuras;

public class BST {

    public static class Nodo {
        Nodo(String element) {
            this.element = element;
        }
        String element;
        Nodo right;
        Nodo left;

        public String getElement() {
            return element;
        }
    }

    private Nodo root = null;
    boolean isEmpty(){
        return root == null;
    }

    public boolean contains (String e){
        return this.contains (e.toLowerCase(), root);
    }
    private boolean contains (String e, Nodo current){
        if (current == null) {
            return false;
        }
        int cmp = e.compareTo(current.element);
        if (cmp < 0) {
            return contains(e,current.left);
        } else if (cmp > 0) {
            return contains(e,current.right);
        } else {
            return true;
        }
    }

    public Nodo findMin (){
        return this.findMin (this.root);
    }

    private Nodo findMin (Nodo current){
        if (current == null){
            return null;
        } else if (current.left == null){
            return current;
        } else {
            return findMin(current.left);
        }
    }

    public void insert (String e){
        root = this.insert  (e.toLowerCase(), this.root);
    }
    private Nodo insert (String e, Nodo current){
        if (current == null){
            return new Nodo(e);
        } else if (e.compareTo(current.element) < 0){
            current.left = insert (e, current.left);
        } else if (e.compareTo(current.element) > 0) {
            current.right = insert (e, current.right);
        }
        return current;
    }

    public void Eliminar(int n){

    }

}