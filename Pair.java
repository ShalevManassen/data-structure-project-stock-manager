

public class Pair<F extends Comparable<F>, S extends Comparable<S>> implements Comparable<Pair<F, S>> {
    private F first;
    private S second;

    // Constructor
    public Pair(F first, S second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Both elements of the pair must be non-null.");
        }
        this.first = first;
        this.second = second;
    }

    // Getters
    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    // Setters
    public void setFirst(F first) {
        if (first == null) {
            throw new IllegalArgumentException("First element must be non-null.");
        }
        this.first = first;
    }

    public void setSecond(S second) {
        if (second == null) {
            throw new IllegalArgumentException("Second element must be non-null.");
        }
        this.second = second;
    }

    // compareTo Method
    @Override
    public int compareTo(Pair<F, S> other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare to a null pair.");
        }
        int firstComparison = this.first.compareTo(other.first);
        if (firstComparison != 0) {
            return firstComparison;
        }
        return this.second.compareTo(other.second);
    }

    // equals Method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Pair<?, ?> other = (Pair<?, ?>) obj;
        return this.first.equals(other.first) && this.second.equals(other.second);
    }

    // hashCode Method
    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }

    // toString Method (Optional, for debugging purposes)
    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
