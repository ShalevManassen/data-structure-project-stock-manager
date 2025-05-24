public class Node<K extends Comparable<K>, V> implements Comparable<Node<K, V>> {
    private KeyParameters<K> key ;
    private V value;
    private int size = 0;
    private Node<K, V> left;
    private Node<K, V> mid;
    private Node<K, V> right;
    private Node<K, V> parent;
    private Node<K, V> next;
    private Node<K, V> prev;

    // Constructor with key, value, and all children
    public Node(KeyParameters<K> key, V value, Node<K, V> left, Node<K, V> mid, Node<K, V> right, Node<K, V> parent,Node<K, V> next, Node<K, V> prev) {
        this.key = key;
        this.value = value;
        this.left = left;
        this.mid = mid;
        this.right = right;
        this.parent = parent;
        this.next = next;
        this.prev = prev;
    }

    // Constructor with key and value only (no children)
    public Node(K key, V value) {
        KeyParameters<K> newKey = new KeyParameters<>(key);
        this.key = newKey;
        this.value = value;
        this.left = null;
        this.mid = null;
        this.right = null;
        this.parent = null;
        this.next = null;
    }
    public Node(KeyParameters<K> key) {
        this(key, null, null, null, null, null,null,null); // Calling the first constructor with nulls for optional values
    }
    public Node() {
        this(null, null, null, null, null, null,null,null); // Calling the first constructor with nulls for optional values
    }

    public void updateSize() {
        int leftSize = (left != null) ? left.getSize() : 0;
        int midSize = (mid != null) ? mid.getSize() : 0;
        int rightSize = (right != null) ? right.getSize() : 0;
        this.size =  leftSize + midSize + rightSize;
        if (this.getParent() != null) { //Changing node's size might affect the parent's size
            this.getParent().updateSize();
        }
    }

    // Getters and Setters
    public KeyParameters<K> getKey() {
        return key;
    }

    public void setKey(KeyParameters<K> key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Node<K, V> getLeft() {
        return left;
    }

    public void setLeft(Node<K, V> left) {
        this.left = left;
    }

    public Node<K, V> getMid() {
        return mid;
    }

    public void setMid(Node<K, V> mid) {
        this.mid = mid;
    }

    public Node<K, V> getRight() {
        return right;
    }

    public void setRight(Node<K, V> right) {
        this.right = right;
    }

    public Node<K, V> getParent() {
        return parent;
    }

    public void setParent(Node<K, V> parent) {
        this.parent = parent;
    }

    public Node<K, V> getPrev() {
        return prev;
    }

    public void setPrev(Node<K, V> prev) {
        this.prev = prev;
    }

    public Node<K, V> getNext() {
        return next;
    }

    public void setNext(Node<K, V> next) {
        this.next = next;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    // equals Method (compares by key and value)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Same object
        if (obj == null || getClass() != obj.getClass()) return false; // Null or not the same class
        Node<K, V> other = (Node<K, V>) obj; // Cast to Node<K, V>
        return (this.key != null && this.key.equals(other.key)) &&
                (this.value != null && this.value.equals(other.value));
    }

    // compareTo Method (compares by key only)
    @Override
    public int compareTo(Node<K, V> other) {
        if (this.key == null && other.key == null) return 0;
        if (this.key == null) return -1;
        if (other.key == null) return 1;
        return this.key.compareTo(other.getKey());
    }

    // Optional: toString Method for debugging
    @Override
    public String toString() {
        return "Node{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}