package hotel.services;

public class Minibar {
    private int mbID;
    private String mbItem;
    private String mbDescription;
    private int mbPrice;

    public Minibar(int mbID, String mbItem, String mbDescription, int mbPrice) {
        this.mbID = mbID;
        this.mbItem = mbItem;
        this.mbDescription = mbDescription;
        this.mbPrice = mbPrice;
    }

    @Override
    public String toString() {
        double tmpPrice = Double.parseDouble(String.valueOf(mbPrice));

        return  /*mbID + " " +*/
                mbItem + " -- " +
                String.format("%1.2f €", tmpPrice/100) + "€";
    }

    public int getMbID() {
        return mbID;
    }

    public String getMbItem() {
        return mbItem;
    }
}
