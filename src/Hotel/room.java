package hotel;

import database.Database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Room {
    private String type;
    private double price;
    private int capacity;
    private double size;
    private String description;
    private String facilitys;

    public Room(String type, double price, int capacity, double size, String description, String facilitys) {
        this.type = type;
        this.price = price;
        this.capacity = capacity;
        this.size = size;
        this.description = description;
        this.facilitys = facilitys;
    }

    public static Room firstFreeRoom(String roomType, Date start, Date end){
        try{
            String query = "SELECT * FROM (SELECT * FROM rooms INNER JOIN roomTypes ON rooms.fk_roomTypeID = roomTypes.roomTypeID" +
                    "WHERE roomTypeName =" + roomType + ") LEFT JOIN bookings ON" +
                    "bookings.fk_roomID = rooms.roomID WHERE (bookingFrom >" + end +" AND bookingUntil < " + start + ")";
            ResultSet rs = Database.getData(query);
            if(rs.first()){
                return new Room(rs.getString("roomTypeName"),rs.getDouble("roomTypePrice"),
                        rs.getInt("roomTypeCapacity"),rs.getDouble("roomSize"),
                        "",rs.getString("roomTypeFacilities"));
            }
        }catch(Exception e){
            System.err.println(e);
        }
        return null;
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

    public String getFacilitys() {
        return facilitys;
    }
}