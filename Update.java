
public class Update implements Comparable<Update> {
    private boolean isRelavent;
    private long timeStamp;
    private Float priceDifference;

    // Constructor
    public Update(long timeStamp,Float priceDifference) {
        this.isRelavent = true;
        this.timeStamp = timeStamp;
        this.priceDifference = priceDifference;
    }

    // Getters
    public boolean isRelavent() {
        return isRelavent;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Float getPriceDifference() {return priceDifference;}

    // Setters
    public void setRelavent(boolean isRelavent) {
        this.isRelavent = isRelavent;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    // equals method (based on timeStamp)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Update other = (Update) obj;
        return this.timeStamp == other.timeStamp;
    }

    // compareTo method (based on timeStamp)
    @Override
    public int compareTo(Update other) {
        return Long.compare(this.timeStamp, other.timeStamp);
    }

    // Optional: toString method for easy debugging
    @Override
    public String toString() {
        return "Update{" +
                "isRelavent=" + isRelavent +
                ", timeStamp=" + timeStamp +
                '}';
    }
}