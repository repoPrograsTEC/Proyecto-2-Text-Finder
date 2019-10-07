package AplicacionMain;

import Estructuras.BST;

public class Prueba {

    public static void main(String [] arg){

        BST Hola=new BST();
        Hola.insert("C");
        Hola.insert("A");
        Hola.insert("V");
        Hola.insert("K");

        System.out.println(Hola.contains("V"));
        System.out.println(Hola.findMin().getElement());


        System.out.println("B".compareTo("A"));
        System.out.println("A".compareTo("B"));
    }

}
