package Estructuras;

class Nodo {
    Nodo(String element) {
        this.element = element;
    }
    String element;
    Nodo right;
    Nodo left;
}

public class BST {
    Nodo root = null;
    boolean isEmpty(){
        return root == null;
    }

    public boolean contains (String e){
        return this.contains (e, root);
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
/*
    public void insert (int e){
        root = this.insert  (e, this.root);
    }

    private Nodo insert (int e, Nodo current){
        if (current == null){
            return new Nodo(e);
        } else if (e < current.left.element ){
            current.left = insert (e, current.left);
        } else if (e > current.right.element) {
            current.right = insert (e, current.right);
        }
        return current;
    }
 */

}
