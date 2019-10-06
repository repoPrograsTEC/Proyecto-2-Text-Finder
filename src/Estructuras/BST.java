package Estructuras;



class Nodo {
    Nodo(int element) {
        this.element = element;
    }

    int element;
    Nodo right;
    Nodo left;
}

public class BST {
    Nodo root = null;
    boolean isEmpty(){
        return root == null;
    }

    boolean contains (int e){
        return this.contains (e, root);
    }

    private boolean contains (int e, Nodo current){
        if (current == null){
            return false;
        } else if (e < current.element){
            return contains (e, current.left);
        } else if (e > current.element){
            return contains (e, current.right);
        } else{
            return true;
        }
    }

    public Nodo findMin (){
        return this.findMin (this.root);
    }

    public Nodo findMin (Nodo current){
        if (current == null){
            return null;
        } else if (current.left == null){
            return current;
        } else {
            return findMin(current.left);
        }
    }

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

}
