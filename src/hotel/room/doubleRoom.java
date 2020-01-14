package room;

public class doubleRoom extends room {
    private double price = 160.25;
    private int capacity = 2;
    private double size = 78.34;
    private String description = "Doubleroom";

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
