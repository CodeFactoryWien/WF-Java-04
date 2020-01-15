package hotel;

public class Room {
    private String type;
    private double price;
    private int capacity;
    private double size;
    private String description;
    private String facilities;

    public Room(String type, double price, int capacity, double size, String description, String facilities) {
        this.type = type;
        this.price = price;
        this.capacity = capacity;
        this.size = size;
        this.description = description;
        this.facilities = facilities;
    }
}