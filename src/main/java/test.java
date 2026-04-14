public class test {


    public static void main(String[] args) {
        RedBlackTree<Integer> rbt = new RedBlackTree<>();
        rbt.insert(10);
        rbt.insert(5);
        rbt.insert(3);
        rbt.printInOrder();
        rbt.printTree();
    }
}
