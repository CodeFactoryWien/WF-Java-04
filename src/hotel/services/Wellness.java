package hotel.services;

public class Wellness {
    int wellnessID;
    String wellnessName;
    String wellnessDescription;
    Double wellnessPrice;

    public Wellness(int wellnessID, String wellnessName, String wellnessDescription, Double wellnessPrice) {
        this.wellnessID = wellnessID;
        this.wellnessName = wellnessName;
        this.wellnessDescription = wellnessDescription;
        this.wellnessPrice = wellnessPrice;
    }

    @Override
    public String toString() {
        return wellnessName + " -- " + wellnessPrice + "€";
    }

    public int getWellnessID() {
        return wellnessID;
    }
}
