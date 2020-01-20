package hotel.services;

public class Wellness {
    int wellnessID;
    String wellnessName;
    String wellnessDescription;
    int wellnessPrice;

    public Wellness(int wellnessID, String wellnessName, String wellnessDescription, int wellnessPrice) {
        this.wellnessID = wellnessID;
        this.wellnessName = wellnessName;
        this.wellnessDescription = wellnessDescription;
        this.wellnessPrice = wellnessPrice;
    }

    @Override
    public String toString() {
        double tmpPrice = Double.parseDouble(String.valueOf(wellnessPrice));
        return /*wellnessID + " "+ */ wellnessName + " -- " + String.format("%1.2f €", tmpPrice/100);
    }

    public int getWellnessID() {
        return wellnessID;
    }
}
