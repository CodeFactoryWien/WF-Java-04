package hotel.services;

public class Minibar {
    private int mbID;
    private String mbItem;
    private String mbDescription;
    private double mbPrice;

    public Minibar(int mbID, String mbItem, String mbDescription, double mbPrice) {
        this.mbID = mbID;
        this.mbItem = mbItem;
        this.mbDescription = mbDescription;
        this.mbPrice = mbPrice;
    }

    @Override
    public String toString() {
        return  mbID + " " +
                mbItem + " " +
                mbPrice + "€";
    }

    public int getMbID() {
        return mbID;
    }

    public String getMbItem() {
        return mbItem;
    }
}
