
public class StockManager {
    public TwoThreeTree<String, Stock> treeStockID;
    public TwoThreeTree<Pair<Float, KeyParameters<String>>, Stock> treePrice;

    public StockManager() {

    }

    // 1. Initialize the system
    public void initStocks() {
        this.treeStockID = new TwoThreeTree<>();
        this.treePrice = new TwoThreeTree<>();
    }

    // 2. Add a new stock
    public void addStock(String stockId, long timestamp, Float price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Can't initialize stock with price zero or less");
        }
        if (treeStockID.search(treeStockID.getRoot(), stockId) != null) {
            throw new IllegalArgumentException("Stock is already exists");
        }
        Stock newStock = new Stock(stockId, timestamp, price);
        //New keys
        KeyParameters<String> newWrapper = new KeyParameters<>(stockId);
        Pair<Float, KeyParameters<String>> newPair = new Pair<>(price, newWrapper);
        //New nodes
        Node<String, Stock> newStockID = new Node<>(stockId, newStock);
        newStockID.setSize(1);
        Node<Pair<Float, KeyParameters<String>>, Stock> newStockIDPrice = new Node<>(newPair, newStock);
        newStockIDPrice.setSize(1);
        //Inserting them to the tree
        treeStockID.insert(newStockID);
        treePrice.insert(newStockIDPrice);
    }

    // 3. Remove a stock
    public void removeStock(String stockId) {
        Node<String, Stock> NodeToRemove = treeStockID.search(treeStockID.getRoot(), stockId);
        if (NodeToRemove == null) {
            throw new IllegalArgumentException("Stock with ID " + stockId + " does not exist.");
        }
        Stock StockToRemove = NodeToRemove.getValue(); //Finding StockToRemove
        //Creating key to remove for tree price
        Pair<Float, KeyParameters<String>> PairToRemove = new Pair<>(StockToRemove.getPrice(), new KeyParameters<>(stockId));
        //Creating node to remove for tree price
        Node<String, Stock> RemoveID = treeStockID.search(treeStockID.getRoot(), stockId);
        treeStockID.delete(RemoveID);
        Node<Pair<Float, KeyParameters<String>>, Stock> RemovePrice = treePrice.search(treePrice.getRoot(), PairToRemove);
        treePrice.delete(RemovePrice);
    }

    // 4. Update a stock price
    public void updateStock(String stockId, long timestamp, Float priceDifference) {
        Node<String, Stock> NodeToUpdate = treeStockID.search(treeStockID.getRoot(), stockId);
        if (NodeToUpdate == null) {
            throw new IllegalArgumentException("Stock" + stockId + "does not exists");
        }
        if (priceDifference == 0) {
            throw new IllegalArgumentException("Can't update price with difference zero");
        }
        //Finding the stock that needs to be updated
        Stock StockToUpdate = NodeToUpdate.getValue();
        //Creating the key of the stock that needs to be updated
        Pair<Float, KeyParameters<String>> PairToDelete = new Pair<>(StockToUpdate.getPrice(), new KeyParameters<>(stockId));
        //Finding the stock that needs to be updated in tree price
        Node<Pair<Float, KeyParameters<String>>, Stock> nodeToDelete = treePrice.search(treePrice.getRoot(), PairToDelete);
        treePrice.delete(nodeToDelete);

        //Updating the price of the stock log(MstockID)
        StockToUpdate.Update(timestamp, priceDifference);
        //Creating new key and node
        Pair<Float, KeyParameters<String>> PairToUpdate = new Pair<>(StockToUpdate.getPrice(), new KeyParameters<>(stockId));
        Node<Pair<Float, KeyParameters<String>>, Stock> nodeToUpdate = new Node<>(PairToUpdate, StockToUpdate);
        nodeToUpdate.setSize(1);
        treePrice.insert(nodeToUpdate); //Insert according to the new pair key
    }

    // 5. Get the current price of a stock
    public Float getStockPrice(String stockId) {
        //Search for the stock in the tree
        Node<String, Stock> node = treeStockID.search(treeStockID.getRoot(), stockId);
        if (node == null || node.getValue() == null) {
            throw new IllegalArgumentException("Stock with ID " + stockId + " does not exist.");
        }
        return Math.round(node.getValue().getPrice() * 100.0f) / 100.0f;
    }


    // 6. Remove a specific timestamp from a stock's history
    public void removeStockTimestamp(String stockId, long timestamp) {
        Stock stockToChange = treeStockID.search(treeStockID.getRoot(), stockId).getValue();
        if (stockToChange == null) {
            throw new IllegalArgumentException("Stock with ID " + stockId + " does not exist.");
        }
        if (stockToChange.getMstockID() == 1) {
            throw new IllegalArgumentException("Stock with ID " + stockId + " got only one initial update so you cant delete it");
        }
        if (stockToChange.getFirstTimestamp() == timestamp) {
            throw new IllegalArgumentException("Can't remove initial price");
        }
        if (stockToChange.findUpdate(timestamp) == null) {
            throw new IllegalArgumentException("Update with timestamp " + timestamp + " does not exist.");
        }
        //Updating stock's update list log(MstockID)
        stockToChange.UpdateNotRelavent(timestamp);
    }

    // 7. Get the amount of stocks in a given price range
    public int getAmountStocksInPriceRange(Float price1, Float price2) {
        //Creating the keys and nodes of sentinel that will represent the bounds of the range
        Pair<Float, KeyParameters<String>> minSentinel = new Pair<>(price1, new KeyParameters<>(null, false, true));
        Pair<Float, KeyParameters<String>> maxSentinel = new Pair<>(price2, new KeyParameters<>(null, true, false));
        Node<Pair<Float, KeyParameters<String>>, Stock> minSentinelNode = new Node<>(minSentinel, null);
        minSentinelNode.setSize(1);
        Node<Pair<Float, KeyParameters<String>>, Stock> maxSentinelNode = new Node<>(maxSentinel, null);
        maxSentinelNode.setSize(1);

        //visualizeNode(treePrice.getRoot(),null,1);
        //Inserting the sentinel
        treePrice.insert(minSentinelNode);
        treePrice.insert(maxSentinelNode);

        //Finding their order statistic compare to the other leafs
        int min = treePrice.rank(minSentinelNode);
        int max = treePrice.rank(maxSentinelNode);

        //Deleting the sentinels because they are not real leafs
        treePrice.delete(minSentinelNode);
        treePrice.delete(maxSentinelNode);

        return max - min - 1; //the length of the range
    }

    // 8. Get a list of stock IDs within a given price range
    public String[] getStocksInPriceRange(Float price1, Float price2) {
        int K = getAmountStocksInPriceRange(price1, price2);
        String[] StocksInPriceRange = new String[K];

        //Creating sentinel to find the beginning of the range
        Pair<Float, KeyParameters<String>> minSentinel = new Pair<>(price1, new KeyParameters<>(null, false, true));
        Node<Pair<Float, KeyParameters<String>>, Stock> minSentinelNode = new Node<>(minSentinel, null);
        treePrice.insert(minSentinelNode);

        //Find the beginning of the range by finding the minSentinel's successor
        Node<Pair<Float, KeyParameters<String>>, Stock> start = treePrice.successor(minSentinelNode);

        if (K != 0) {
            StocksInPriceRange[0] = start.getKey().getKey().getSecond().getKey();
        }

        int i = 1;
        Node<Pair<Float, KeyParameters<String>>, Stock> curr = start.getNext();
        while (i < K) {
            if (curr != null) {
                StocksInPriceRange[i] = curr.getKey().getKey().getSecond().getKey();
                curr = curr.getNext();
                i++;
            } else {
                break;
            }
        }
        if (i < K) {
            String[] NewStocksInPriceRange = new String[i];
            int j;
            for (j = 0; j < i; j++) {
                NewStocksInPriceRange[j] = StocksInPriceRange[j];
            }
            treePrice.delete(minSentinelNode);
            return NewStocksInPriceRange;
        }
        treePrice.delete(minSentinelNode);
        return StocksInPriceRange;
    }
}