package hotel.room;

import java.util.ArrayList;

public class suite extends room {

    public suite(ArrayList<String> facilitys) {
       this.price = 240.22;
        this.capacity = 4;
        this.size = 128.88;
        this.description = "Suite";
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

    public void setFacilitys(ArrayList<String> facilitys) {
        this.facilitys = facilitys;
    }
}
