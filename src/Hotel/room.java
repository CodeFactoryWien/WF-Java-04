package Hotel;

public class room {
    private String type;
    private double price;
    private int capacity;
    private double size;
    private String description;
    private String facilitys;

    public room(String type, double price, int capacity, double size, String description, String facilitys) {
        this.type = type;
        this.price = price;
        this.capacity = capacity;
        this.size = size;
        this.description = description;
        this.facilitys = facilitys;
    }
}