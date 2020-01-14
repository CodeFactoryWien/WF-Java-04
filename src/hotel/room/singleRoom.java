package hotel.room;

public class singleRoom extends room {
    private double price = 120.22;
    private int capacity = 1;
    private double size = 58.88;
    private String description = "Singleroom";
    private String[] facilitys = {"TV", "Coffee Maschine", "WLAN"};

    //Getter//
    public double getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getSize() { return size; }

    public String getDescription() {
        return description;
    }

    public String[] getFacilitys() {
        return facilitys;
    }

    //Setter//
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFacilitys(String[] facilitys) {
        this.facilitys = facilitys;
    }
}
