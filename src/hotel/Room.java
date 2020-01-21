package hotel;

import database.Database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Room {
    private int id;
    private String type;
    private double price;
    private int capacity;
    private double size;
    private String description;
    private String facilities;

    public Room(int id,String type, double price, int capacity, double size, String description, String facilities) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.capacity = capacity;
        this.size = size;
        this.description = description;
        this.facilities = facilities;
    }

    public Room(ResultSet resultSet){
        try {
            id = resultSet.getInt("roomID");
            type = resultSet.getString("roomTypeName");
            price = resultSet.getDouble("roomTypePrice");
            capacity = resultSet.getInt("roomTypeCapacity");
            size = resultSet.getDouble("roomSize");
            facilities = resultSet.getString("roomTypeFacilities");
        }catch (Exception e){
            System.out.println("Problem creating Room from this ResultSet");
        }
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    public String getFacilities() {
        return facilities;
    }

    public String toString(){
        return "ID " + id +" " + type + " " + price + "€ ";
    }

}