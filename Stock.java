

public class Stock implements Comparable<Stock> {
    private String stockID;
    private float currentPrice;
    private int MstockID;
    private long firstTimestamp;
    private TwoThreeTree<Long, Update> updateList;


    public Stock(String stockID, long timestamp, float Price) {
        this.stockID = stockID;
        this.currentPrice = 0;
        this.MstockID = 0;
        this.firstTimestamp = timestamp; //Saving the first update
        this.updateList = new TwoThreeTree<>();
        this.Update(timestamp,Price);
    }
    public void Update(long timestamp, Float priceDifference) {
        MstockID++;
        this.currentPrice+= priceDifference;
        this.currentPrice = Math.round(this.currentPrice * 100.0f) / 100.0f; // Ensuring two decimal places

        //Creating a new update and adding if to the list of updates
        Update newUpdate = new Update(timestamp,priceDifference);
        Node<Long, Update> newUpdateNode = new Node<>(timestamp,newUpdate);
        updateList.insert(newUpdateNode);
    }
    public void UpdateNotRelavent(Long timestamp){
        MstockID--;
        //Finding node to remove
        Node <Long, Update> NodeToRemove = updateList.search(updateList.getRoot(),timestamp);
        Update updateToRemove = NodeToRemove.getValue();

        //Remove it from updateList
        updateList.delete(NodeToRemove);
        updateToRemove.setRelavent(false);
        this.currentPrice -= updateToRemove.getPriceDifference();
    }

    public Node<Long, Update> findUpdate(long timestamp){
        return updateList.search(updateList.getRoot(),timestamp);
    }


    public String getStockID() {
        return stockID;
    }

    // Getters and setters
    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public float getPrice() {
        return currentPrice;
    }

    public void setPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getMstockID() {
        return MstockID;
    }

    public void setMstockID(int MstockID) {
        this.MstockID = MstockID;
    }

    public TwoThreeTree<Long, Update> getUpdateList() {
        return updateList;
    }

    public void setUpdateList(TwoThreeTree<Long, Update> updateList) {
        this.updateList = updateList;
    }

    public long getFirstTimestamp() {
        return firstTimestamp;
    }

    public void setFirstTimestamp(int firstTimestamp) {this.firstTimestamp = firstTimestamp;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;  // If it's the same object, return true.
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;  // If the object is null or not the same class, return false.
        }
        Stock other = (Stock) obj;  // Cast the object to Stock type.
        return this.stockID.equals(other.getStockID());  // Compare the stockID of both objects.
    }

    @Override
    public int compareTo(Stock other) {
        if (other == null) {
            throw new NullPointerException("The object to compare to is null.");  // Throw exception if compared object is null.
        }
        return this.stockID.compareTo(other.getStockID());  // Compare the stockID of both objects.
    }
}


