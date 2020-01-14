package hotel.room;

public class suite extends room {
    private double price = 240.22;
    private int capacity = 4;
    private double size = 128.88;
    private String description = "Suite";

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
}
