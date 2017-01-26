import java.io.*;

//source code from https://rosettacode.org/wiki/AVL_tree#Java
public class AVLTree implements java.io.Serializable{

    private Node root;

    private class Node implements  java.io.Serializable{
        private int key;
        private long position;
        private int balance;
        private Node left, right, parent;

        Node(int k, Node p,long pos) {
            key = k;
            position = pos;
            parent = p;
        }
    }

    public boolean insert(int key,long position) {
        if (root == null)
            root = new Node(key, null,position);
        else {
            Node n = root;
            Node parent;
            while (true) {
                if (n.key == key)
                    return false;

                parent = n;

                boolean goLeft = n.key > key;
                n = goLeft ? n.left : n.right;

                if (n == null) {
                    if (goLeft) {
                        parent.left = new Node(key, parent,position);
                    } else {
                        parent.right = new Node(key, parent,position);
                    }
                    rebalance(parent);
                    break;
                }
            }
        }
        return true;
    }

    private long searchAux(Node node,int key) {
        if(node == null)
            return -1;
        else if(node.key == key)
            return node.position;
        else if (node.key > key)
            return searchAux(node.left,key);
        else
            return searchAux(node.right,key);
    }
    public long search(int key){
        return searchAux(root,key);
    }

    public void saveTree(){
        try
        {
            FileOutputStream fileOut = new FileOutputStream("tree.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in tree.ser");
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }
    public static AVLTree openTree(){
        AVLTree tree = null;
        try
        {
            FileInputStream fileIn = new FileInputStream("tree.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            tree = (AVLTree) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException i)
        {
            i.printStackTrace();
        }catch(ClassNotFoundException c)
        {
            System.out.println("AVLTree class not found");
            c.printStackTrace();
        }
        return tree;
    }

    private void rebalance(Node n) {
        setBalance(n);

        if (n.balance == -2) {
            if (height(n.left.left) >= height(n.left.right))
                n = rotateRight(n);
            else
                n = rotateLeftThenRight(n);

        } else if (n.balance == 2) {
            if (height(n.right.right) >= height(n.right.left))
                n = rotateLeft(n);
            else
                n = rotateRightThenLeft(n);
        }

        if (n.parent != null) {
            rebalance(n.parent);
        } else {
            root = n;
        }
    }

    private Node rotateLeft(Node a) {

        Node b = a.right;
        b.parent = a.parent;

        a.right = b.left;

        if (a.right != null)
            a.right.parent = a;

        b.left = a;
        a.parent = b;

        if (b.parent != null) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else {
                b.parent.left = b;
            }
        }

        setBalance(a, b);

        return b;
    }

    private Node rotateRight(Node a) {

        Node b = a.left;
        b.parent = a.parent;

        a.left = b.right;

        if (a.left != null)
            a.left.parent = a;

        b.right = a;
        a.parent = b;

        if (b.parent != null) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else {
                b.parent.left = b;
            }
        }

        setBalance(a, b);

        return b;
    }

    private Node rotateLeftThenRight(Node n) {
        n.left = rotateLeft(n.left);
        return rotateRight(n);
    }

    private Node rotateRightThenLeft(Node n) {
        n.right = rotateRight(n.right);
        return rotateLeft(n);
    }

    private int height(Node n) {
        if (n == null)
            return -1;
        return 1 + Math.max(height(n.left), height(n.right));
    }

    private void setBalance(Node... nodes) {
        for (Node n : nodes)
            n.balance = height(n.right) - height(n.left);
    }

    public void printBalance() {
        printBalance(root);
    }

    private void printBalance(Node n) {
        if (n != null) {
            printBalance(n.left);
            System.out.printf("%s ", n.balance);
            printBalance(n.right);
        }
    }

}