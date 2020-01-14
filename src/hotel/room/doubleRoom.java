package hotel.room;

import java.util.ArrayList;

public class doubleRoom extends room {

    public doubleRoom(ArrayList<String> facilitys) {
        this.price = 160.25;
        this.capacity = 2;
        this.size = 78.34;
        this.description = "Doubleroom";
        this.facilitys = facilitys;
    }

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

    public ArrayList<String> getFacilitys() {
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
}
