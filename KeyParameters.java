
public class KeyParameters<K extends Comparable<K>> implements Comparable<KeyParameters<K>> {
    private K key;
    private boolean isInfinity;
    private boolean isMinusInfinity;

    // Constructors
    public KeyParameters(K key, boolean isInfinity, boolean isMinusInfinity) {
        this.key = key;
        this.isInfinity = isInfinity;
        this.isMinusInfinity = isMinusInfinity;
    }

    public KeyParameters(K key) {
        this.key = key;
        this.isInfinity = false;
        this.isMinusInfinity = false;
    }

    public KeyParameters(){
        this(null,false,false);
    }

    // Getters and Setters
    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public boolean isInfinity() {
        return isInfinity;
    }

    public void setInfinity(boolean infinity) {
        isInfinity = infinity;
    }

    public boolean isMinusInfinity() {
        return isMinusInfinity;
    }

    public void setMinusInfinity(boolean minusInfinity) {
        isMinusInfinity = minusInfinity;
    }

    // compareTo Method
    @Override
    public int compareTo(KeyParameters<K> other) {
        // Handle null keys explicitly
        if (this.key == null && other.key == null) {
            // If both keys are null, compare infinity flags
            if (this.isInfinity && other.isInfinity) return 0;
            if (this.isMinusInfinity && other.isMinusInfinity) return 0;
            if (this.isInfinity) return 1;
            if (this.isMinusInfinity) return -1;
            if (other.isInfinity) return -1;
            if (other.isMinusInfinity) return 1;

            // If neither is infinity, treat them as equal
            return 0;
        }

        if (this.key == null) {
            // If this key is null, it is considered less than any non-null key
            return this.isMinusInfinity ? -1 : 1;
        }

        if (other.key == null) {
            // If the other key is null, it is considered less than this non-null key
            return other.isMinusInfinity ? 1 : -1;
        }

        // Compare actual keys if neither is null
        int keyComparison = this.key.compareTo(other.key);

        if (keyComparison == 0) {
            // Keys are equal, compare infinity flags
            if (this.isInfinity && other.isInfinity) return 0;
            if (this.isMinusInfinity && other.isMinusInfinity) return 0;
            if (this.isInfinity) return 1;
            if (this.isMinusInfinity) return -1;
            if (other.isInfinity) return -1;
            if (other.isMinusInfinity) return 1;
        }

        // Return result of key comparison
        return keyComparison;
    }

    // equals Method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        KeyParameters<K> other = (KeyParameters<K>) obj;

        if (this.isInfinity != other.isInfinity) return false;
        if (this.isMinusInfinity != other.isMinusInfinity) return false;

        // If neither is infinity, compare keys.
        return (this.key != null && this.key.equals(other.key));
    }

    // toString Method
    @Override
    public String toString() {
        if (isInfinity) return "Infinity";
        if (isMinusInfinity) return "-Infinity";
        return "KeyParameters{" + "key=" + key + '}';
    }
}