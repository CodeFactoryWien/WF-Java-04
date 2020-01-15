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

    public static Room firstFreeRoom(String roomType, Date start, Date end){
        try{
            String query = "SELECT * FROM " +
                    "(SELECT * FROM rooms INNER JOIN roomType ON rooms.fk_roomTypeID = roomType.roomTypeID " +
                    "WHERE roomTypeName = \"" + roomType + "\") AS room " +
                    "LEFT JOIN bookings ON bookings.fk_roomID = room.roomID " +
                    "WHERE ((bookingFrom >'" + end +"' OR bookingUntil < '" + start + "') OR bookingFrom IS NULL)";
            System.out.println(query);
            ResultSet rs = Database.getData(query);
            System.out.println(rs.first());
            if(rs.first()){
                System.out.println(rs.getString("roomTypeName"));
                return new Room(rs.getInt("roomID"),rs.getString("roomTypeName"),rs.getDouble("roomTypePrice"),
                        rs.getInt("roomTypeCapacity"),rs.getDouble("roomSize"),
                        "",rs.getString("roomTypeFacilites"));
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

    public String getFacilities() {
        return facilities;
    }

    public String toString(){
        return "ID " + id +" " + type + " " + price + "€ ";
    }
}