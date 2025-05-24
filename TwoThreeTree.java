
public class TwoThreeTree<K extends Comparable<K>, V> {
    private Node<K, V> root;
    private int size; //is needed???

    // Constructor for the tree
    public TwoThreeTree() { //initialize tree with infinite and minusInfinite sentinels
        Node<K, V> leftChild = new Node<>();
        Node<K, V> midChild = new Node<>();
        leftChild.setKey(new KeyParameters<K>(null, false, true));
        midChild.setKey(new KeyParameters<K>(null, true, false));
        KeyParameters<K> rootKey = new KeyParameters<K>(null, true, false);
        root = new Node<>(rootKey, null, leftChild, midChild, null, null, null, null);
        leftChild.setParent(root);
        midChild.setParent(root);
        //size = 0;
    }


    public boolean isLeaf(Node<K, V> vertex) { //If v got no left child then it is a leaf
        return vertex.getLeft() == null;
    }

    // Search method
    public Node<K, V> search(Node<K, V> subRoot, K key) {
        //Wrapping key with KeyParameters so it will be comparable to other keys
        KeyParameters<K> searchKey = new KeyParameters<>(key);
        if (isLeaf(subRoot)) { //Stop condition
            if (searchKey.equals(subRoot.getKey())) {
                return subRoot;
            }
            return null;
        }
        if (searchKey.compareTo(subRoot.getLeft().getKey()) <= 0) {
            //Recursively search in left subTree
            return search(subRoot.getLeft(), key);
        } else if (subRoot.getMid() != null && searchKey.compareTo(subRoot.getMid().getKey()) <= 0) {
            //Recursively search in mid subTree
            return search(subRoot.getMid(), key);
        } else if (subRoot.getRight() != null) {
            //Recursively search in right subTree
            return search(subRoot.getRight(), key);
        } else {
            return null;
        }
    }



    // Successor method
    public Node<K,V> successor(Node<K,V> x) { //O(log(n))
        Node<K,V> z = x.getParent();
        Node<K,V> y;

        // Traverse up the tree to find the correct subtree
        while ((x == z.getRight()) || ((z.getRight() == null) && (x == z.getMid()))) {
            x = z;
            z = z.getParent();
        }

        if (x == z.getLeft()) {
            y = z.getMid();
        } else {
            y = z.getRight();
        }

        if (y == null) {
            return null;
        }

        // Traverse down to find the leftmost node in the right subtree
        while (!isLeaf(y)) {
            y = y.getLeft();
        }
        //In case of reaching to one of the sentinels
        if (y.getKey().isMinusInfinity() || y.getKey().isInfinity()) {
            return null;
        }
        return y;
    }




    public Node<K,V> predecessor(Node<K,V> x) { //O(log(n))
        Node<K,V> z = x.getParent();
        Node<K,V> y;

        // Traverse up the tree to find the correct subtree
        while ((x == z.getLeft()) || ((z.getLeft() == null) && (x == z.getMid()))) {
            x = z;
            z = z.getParent();
        }

        if (x == z.getRight()) {
            y = z.getMid();
        } else {
            y = z.getLeft();
        }

        if (y == null) {
            return null;
        }

        // Traverse down to find the rightmost node in the left subtree
        while (!isLeaf(y)) {
            y = y.getRight() != null ? y.getRight() : y.getMid();
        }
        //In case of reaching to one of the sentinels
        if ( y.getKey().isMinusInfinity() || y.getKey().isInfinity()){
            return null;
        }

        return y;
    }


    public void updateKey(Node<K, V> x) {
        if (x.getLeft() != null) {
            x.setKey(x.getLeft().getKey());
        }
        if (x.getMid() != null) {
            x.setKey(x.getMid().getKey());
        }
        if (x.getRight() != null) {
            x.setKey(x.getRight().getKey());
        }
    }

    public void setChildren(Node<K, V> x, Node<K, V> left, Node<K, V> mid, Node<K, V> right) {
        x.setLeft(left);
        x.setMid(mid);
        x.setRight(right);

        if (left != null) {
            left.setParent(x);
        }
        if (mid != null) {
            mid.setParent(x);
        }
        if (right != null) {
            right.setParent(x);
        }
        updateKey(x);
    }

    public Node<K, V> insertAndSplit(Node<K, V> x, Node<K, V> z) {
        Node<K, V> left = x.getLeft();
        Node<K, V> mid = x.getMid();
        Node<K, V> right = x.getRight();

        if (right == null) { // No right child
            if (z.getKey().compareTo(left.getKey()) < 0) {
                setChildren(x, z, left, mid);
            } else if (mid != null && z.getKey().compareTo(mid.getKey()) < 0) {
                setChildren(x, left, z, mid);
            } else {
                setChildren(x, left, mid, z);
            }
            x.updateSize(); //Amount of children might differ so updateSize is needed
            return null;
        } else { // Split required
            Node<K, V> y = new Node<>(null); // New internal node
            if (z.getKey().compareTo(left.getKey()) < 0) {
                setChildren(x, z, left, null);
                setChildren(y, mid, right, null);
            } else if (mid != null && z.getKey().compareTo(mid.getKey()) < 0) {
                setChildren(x, left, z, null);
                setChildren(y, mid, right, null);
            } else if (z.getKey().compareTo(right.getKey()) < 0) {
                setChildren(x, left, mid, null);
                setChildren(y, z, right, null);
            } else {
                setChildren(x, left, mid, null);
                setChildren(y, right, z, null);
            }
            x.updateSize(); //Amount of children might differ so updateSize is needed
            y.updateSize(); //Amount of children might differ so updateSize is needed
            return y;
        }
    }

    public void insert(Node<K, V> z) {
        Node<K, V> pointerToZ = z;
        Node<K, V> y = this.getRoot();

        // Traverse down to find the appropriate leaf
        while (!isLeaf(y)) {
            if ( z.getKey().compareTo(y.getLeft().getKey()) < 0) {
                y = y.getLeft();
            } else if (y.getMid() != null && z.getKey().compareTo(y.getMid().getKey()) < 0) {
                y = y.getMid();
            } else {
                y = y.getRight();
            }
        }

        Node<K, V> x = y.getParent();
        z = insertAndSplit(x , z);

        // Handle splitting up the tree
        while (x != this.getRoot()) {
            x = x.getParent();
            if (z != null) {
                z = insertAndSplit(x, z);
            } else {
                updateKey(x);
            }
        }

        // If splitting reaches the root
        if (z != null) {
            Node<K, V> w = new Node<>(null); // New root
            setChildren(w, x, z, null);
            w.updateSize(); //Amount of children might differ so updateSize is needed
            this.setRoot(w);
        }
        //Making sure that the new leaf will point to it's next and prev
        Node<K,V> newLeaf = search(this.root, pointerToZ.getKey().getKey());
        if (newLeaf != null) {
            newLeaf.setNext(successor(newLeaf));
            newLeaf.setPrev(predecessor(newLeaf));
        }
        if (newLeaf != null && newLeaf.getPrev() != null) {
            newLeaf.getPrev().setNext(newLeaf);
        }
        if (newLeaf != null && newLeaf.getNext() != null) {
            newLeaf.getNext().setPrev(newLeaf);
        }
    }


    public void delete(Node<K, V> x) {
        Node<K, V> y = x.getParent();

        // Reorganize x as child of the parent and updating his children after delete x
        if (x == y.getLeft()) {
            setChildren(y, y.getMid(), y.getRight(), null);
        } else if (x == y.getMid()) {
            setChildren(y, y.getLeft(), y.getRight(), null);
        } else {
            setChildren(y, y.getLeft(), y.getMid(), null);
        }

        // Handling cases where parent of x remain with only one child
        while (y != null) {
            if (y.getMid() != null) { //Parent of x remain with 2 children no need to borrow or merge
                updateKey(y);
                y = y.getParent();
            } else { //Parent of x remain with 1 child
                if (y != this.root) { //If Parent of x is not the root, call borrowOrMerge
                    y = borrowOrMerge(y);
                } else { // Special case: root underflow
                    this.root = y.getLeft();
                    if (this.root != null) {
                        this.root.setParent(null);
                    }
                    deleteNode(y);
                    return;
                }
            }
        }
        //Making sure that to update next and prev
        if (x != null && x.getKey() != null) {
            //In case of reaching to one of the sentinels
            if (!x.getKey().isInfinity() && !x.getKey().isMinusInfinity() && isLeaf(x)) {
                if (x.getPrev() != null) {
                    x.getPrev().setNext(x.getNext());
                }
                if (x.getNext() != null) {
                    x.getNext().setPrev(x.getPrev());
                }
            }
        }
        deleteNode(x);
    }


    private Node<K, V> borrowOrMerge(Node<K, V> y) {
        Node<K, V> yParent = y.getParent();
        Node<K, V> z = null; //Representing the sibling that with him x will be borrow or merge

        if (y == yParent.getLeft()) { // Borrow or merge with the middle sibling
            z = yParent.getMid();
            if (z != null && z.getRight() != null) { // Borrow from middle sibling
                setChildren(y, y.getLeft(), z.getLeft(), null);
                setChildren(z, z.getMid(), z.getRight(), null);
            } else if (z != null) { // Merge with middle sibling
                setChildren(z, y.getLeft(), z.getLeft(), z.getMid());
                setChildren(yParent, z, yParent.getRight(), null);
                deleteNode(y);
            }
        } else if (y == yParent.getMid()) { // Borrow or merge with the left sibling
            z = yParent.getLeft();
            if (z != null && z.getRight() != null) { // Borrow from left sibling
                setChildren(y, z.getRight(), y.getLeft(), null);
                setChildren(z, z.getLeft(), z.getMid(), null);
            } else if (z != null) { // Merge with left sibling
                setChildren(z, z.getLeft(), z.getMid(), y.getLeft());
                setChildren(yParent, z, yParent.getRight(), null);
                deleteNode(y);
            }
        } else if (y == yParent.getRight()) { // Borrow or merge with the middle sibling
            z = yParent.getMid();
            if (z != null && z.getRight() != null) { // Borrow from middle sibling
                setChildren(y, z.getRight(), y.getLeft(), null);
                setChildren(z, z.getLeft(), z.getMid(), null);
            } else if (z != null) { // Merge with middle sibling
                setChildren(z, z.getLeft(), z.getMid(), y.getLeft());
                setChildren(yParent, yParent.getLeft(), z, null);
                deleteNode(y);
            }
        }

        if (z != null) { //Amount of children might differ so updateSize is needed
            z.updateSize();
            y.updateSize();
            yParent.updateSize();
        }
        return yParent;
    }


    private void deleteNode(Node<K, V> node) {
        if (node == null) return;
        node.setLeft(null);
        node.setMid(null);
        node.setRight(null);
        node.setParent(null);
        node.setKey(null);
        node.setPrev(null);
        node.setNext(null);
    }


    public int rank(Node<K, V> x) { //Get a leaf and needs to determine what his order statistic
        int rank = 1; // Initialize rank as 1
        Node<K, V> y = x.getParent(); // Start with the parent of x

        while (y != null) {
            if (x == y.getMid()) {
                rank += (y.getLeft() != null ? y.getLeft().getSize() : 0);
            } else if (x == y.getRight()) {
                rank += (y.getLeft() != null ? y.getLeft().getSize() : 0) +
                        (y.getMid() != null ? y.getMid().getSize() : 0);
            }
            x = y; // Move up the tree
            y = y.getParent();
        }
        return rank;
    }

    // Getters and Setters
    public Node<K, V> getRoot() {
        return root;
    }

    public void setRoot(Node<K, V> root) {
        this.root = root;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}