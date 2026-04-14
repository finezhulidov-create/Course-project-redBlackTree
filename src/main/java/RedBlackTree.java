import org.w3c.dom.Node;

import java.util.TreeMap;
import java.util.TreeSet;

public class RedBlackTree<T extends Comparable<T>> {
    private static final boolean RED = true;
    private static final boolean BLACK= false;

    private class Node{
        T data;
        Node left, right, parent;
        boolean color;

        public Node(T data) {
            this.data = data;
            this.left = NIL;
            this.right = NIL;
            this.color = RED; // новый узел всегда красный!
        }
    }
        private  final Node NIL = new Node(null);
        private Node root;
        public RedBlackTree(){
            root=NIL;
            NIL.color = BLACK;// корень - черного цвета
        }
        /**
     * Вставляет новый элемент в красно-чёрное дерево.
     *          node(red)
         *    /          \
         *  NIL(black) NIL(black) - изначальное состояние
     * Процесс вставки состоит из двух этапов:
     * 1. Стандартная вставка как в бинарное дерево поиска (BST).
     * 2. Восстановление свойств красно-чёрного дерева с помощью fixInsert.
     *
     * Шаги метода:
     * - Создаётся новый узел z с переданными данными. Цвет нового узла — красный.
     * - Начиная с корня, идёт поиск подходящего места для вставки:
     *   - x — текущий узел, с которого начинается поиск (изначально — root).
     *   - y — родитель текущего узла x (изначально — NIL).
     * - Пока x не станет NIL, сравниваем значение z с x:
     *   - Если z.data < x.data, переходим в левое поддерево.
     *   - Иначе — в правое поддерево.
     * - После выхода из цикла, y указывает на родителя для нового узла z.
     * - Присоединяем z как левого или правого потомка y в зависимости от сравнения значений.
     * - Если y — NIL, значит дерево было пустым, и z становится новым корнем.
     * - Устанавливаем цвет корня в чёрный (по свойству красно-чёрного дерева).
     * - Вызываем fixInsert(z), чтобы восстановить свойства красно-чёрного дерева,
     *   так как вставка красного узла может нарушить правило о двух подряд идущих красных узлах.
     *
     * @param data данные, которые необходимо вставить в дерево
     * @throws ClassCastException если тип T не реализует Comparable
     */
    public void insert(T data){
           Node z = new Node(data); // новый узел всегда красный
           Node x = root; // текущий узел, с которого начинается поиск
           Node y = NIL; // родитель текущего узла

           while (x != NIL){
               y = x;
               x = z.data.compareTo(x.data) < 0 ? x.left : x.right ;//сравнение
           }
           z.parent = y;
           if(y == NIL){
               root = z;
           } else if (z.data.compareTo(y.data) < 0) {
               y.left = z;
           } else {
               y.right = z;
           }
           fixInsert(z);

        }
        /**Свойства красно-чёрного дерева
         Красно-чёрное дерево — это самобалансирующееся двоичное дерево поиска (BST),
         которое поддерживает логарифмическую высоту благодаря следующим 5 свойствам:

       * 1 Каждый узел — либо красный, либо чёрный.
       * 2 Корень дерева — всегда чёрный.
       * 3 Все листья (NIL) — чёрные.
         (Это технические "нулевые" узлы, на которые указывают настоящие листья.)
       * 4 У красного узла оба ребёнка — чёрные.
         (Никакие два красных узла не могут быть подряд: нет двух красных подряд по вертикали.)
       * 5 Любой путь от узла до его простых листьев (NIL) содержит одинаковое количество чёрных узлов.
         Это называется чёрная высота (black height).

         Нарушение этих свойств = дерево больше не КЧД → нужно балансировать.

         Участники в fixInsert
         Мы смотрим на 4 узла:

         z - Только что вставленный узел (красный)
         z.parent - Его отец
         z.parent.parent - Дед (grandparent)
         y - "Дядя" — брат отца (может быть слева или справа)

         Дед существует, потому что если z.parent — красный, он не может быть корнем (корень чёрный).
         * */

    private void fixInsert(Node z) {
        while (z.parent.color == RED){ //пока цвет родительского узла красный балансируем дерево
           if (z.parent == z.parent.parent.left) {
               var y = z.parent.parent.right;

               if (y.color == RED){
                   //случай 1 дядя красный   перекрашиваем g red p black y black
                   //       [G] — чёрный
                   //      /   \
                   //    [P]   [y] — красный
                   //   красный
                   //     |
                   //     z — красный
                   z.parent.color = BLACK;
                   z.parent.parent.color = RED;
                   y.color = BLACK;
                   z = z.parent.parent;
               } else {
                   if (z == z.parent.right){ //если z правый ребенок
                       //случай 2 z — правый ребёнок, а P — левый
                       //       [G]
                       //      /   \
                       //    [P]   [y] — чёрный
                       //   красный
                       //      \
                       //      [z] — красный
                       z = z.parent;
                       leftRotate(z);
                       // После поворота:
                       //       [G]
                       //      /   \
                       //     ?    [z] — красный
                       //         /
                       //       [P] — красный
                   }
                   //случай 3 z - левый ребенок левый ребёнок (или стал им после поворота)
                   // надо сделать правый поворот вокруг G и перекрасить Р черный G красный
                        //       [G]
                        //      /   \
                        //     [z]
                        //    /   \
                        //  [P]   ?
                        // красный
                        z.parent.color = BLACK;
                        z.parent.parent.color = RED;
                        rightRotate(z.parent.parent);
                        //После поворота:
                   //       [P] — чёрный
                   //      /   \
                   //    [z]   [G] — красный
                   //           \
                   //           [y] — чёрный

               }
           } else {
               Node y = z.parent.parent.left;
               if (y.color == RED){
                   z.parent.color = BLACK;
                   y.color = BLACK;
                   z.parent.parent.color = RED;
                   z = z.parent.parent;
               } else {
                   if (z == z.parent.left){
                       z = z.parent;
                       rightRotate(z.parent);
                   }
                   z.parent.color = BLACK;
                   z.parent.parent.color = RED;
                   leftRotate(z.parent.parent);
               }
           }

        }
        root.color = BLACK;
    }

    private void rightRotate(Node g) {
        // до поворота
        //      [G]           ← дед (grandparent)
        //      /
        //    [P]              ← отец (parent), левый ребёнок G
        //   /
        // [z]                 ← z, левый ребёнок P
        Node p = g.left; //отец
        g.left = p.right; //правое поддерево теперь слева от z
        if (p.right != NIL){
            p.right.parent = g;
        }
        p.parent = g.parent; //p занимает место g
        if (g == root){
            root = p;
        } else if (g == g.left) {
            g.parent.left = p;
        } else {
            g.parent.right = p;
        }
        p.right = g; // g становится правым ребенком p
        g.parent = p; // обновляем родителя g
        //После поворота:
        //      [P]
        //     /   \
        //   [z]   [G]
    }

    private void leftRotate(Node g) {
        //Ситуация до поворота:
        //    [G]
        //      \
        //       [P]
        //         \
        //         [z]
        Node p = g.right;
        g.right = p.left;
        if (p.left != NIL){
            p.left.parent = g;
        }
        p.parent = g.parent;
        if (g == root){
            root = p;
        } else if (g == g.left) {
            g.parent.left = p;
        } else {
            g.parent.right = p;
        }
        p.left =g;
        g.parent =p;
        //после поворота
        //      [P]
        //     /   \
        //   [G]   [z]
    }





    //отладочные методы
    /**
 * Выводит дерево в виде инфиксного обхода (лево-корень-право) с указанием цветов узлов.
 * Полезно для отладки структуры и цветов после вставок.
 */
public void printInOrder() {
    System.out.print("In-order traversal: ");
    printInOrder(root);
    System.out.println();
}

private void printInOrder(Node node) {
    if (node != NIL) {
        printInOrder(node.left);
        String color = (node.color == RED) ? "R" : "B";
        System.out.print(node.data + "(" + color + ") ");
        printInOrder(node.right);
    }
}

/**
 * Выводит дерево "как есть" в формате: родитель -> [левый, правый]
 * Показывает структуру и связи между узлами. Полезно для визуализации балансировки.
 */
public void printTree() {
    System.out.println("Tree structure (parent -> [left, right]):");
    if (root == NIL) {
        System.out.println("(empty)");
    } else {
        printTree(root, "", true);
    }
}

private void printTree(Node node, String prefix, boolean isLast) {
    if (node != NIL) {
        String color = (node.color == RED) ? "R" : "B";
        System.out.println(prefix + (isLast ? "└── " : "├── ") + node.data + "(" + color + ")");

        String newPrefix = prefix + (isLast ? "    " : "│   ");
        boolean hasLeft = node.left != NIL;
        boolean hasRight = node.right != NIL;

        if (hasLeft && hasRight) {
            printTree(node.left, newPrefix, false);
            printTree(node.right, newPrefix, true);
        } else {
            if (hasLeft) {
                printTree(node.left, newPrefix, true);
            }
            if (hasRight) {
                printTree(node.right, newPrefix, true);
            }
        }
    }
}
}
