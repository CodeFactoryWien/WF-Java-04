package database;

import hotel.Guest;
import hotel.Room;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Database {

    public static Connection c;

    public static Connection getConnection() throws Exception {
        if (c == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/wf_java04_hotel", "root", "");
        }
        return c;
    }

    // Send data TO Database
    private static void setData(String sql) throws Exception {
        Database.getConnection().createStatement().executeUpdate(sql);
    }

    // Get Data From Database
    private static ResultSet getData(String sql) throws Exception {
        return Database.getConnection().createStatement().executeQuery(sql);
    }

    private static Room firstFreeRoom(String roomType, Date start, Date end){
        String query = "SELECT * FROM " +
                "(SELECT * FROM rooms INNER JOIN roomType ON rooms.fk_roomTypeID = roomType.roomTypeID " +
                "WHERE roomTypeName = \"" + roomType + "\") AS room " +
                "LEFT JOIN bookings ON bookings.fk_roomID = room.roomID " +
                "WHERE ((bookingFrom >'" + end +"' OR bookingUntil < '" + start + "') OR bookingFrom IS NULL)";
        System.out.println(query);
        try{
            ResultSet rs = Database.getData(query);
            System.out.println(rs.first());
            if(rs.first()){
                System.out.println(rs.getString("roomTypeName"));
                return new Room(rs.getInt("roomID"),rs.getString("roomTypeName"),rs.getDouble("roomTypePrice"),
                        rs.getInt("roomTypeCapacity"),rs.getDouble("roomSize"),
                        "",rs.getString("roomTypeFacilites"));
            }
        }catch(Exception e){
            System.err.println("SQL Query Error");
        }
        return null;
    }

    public static Guest getGuest(String email){
        String query = "SELECT * FROM guests WHERE email = " + email;
        try{
            ResultSet rs = Database.getData(query);
            if(rs.first()){
                return new Guest(rs.getInt("guestID"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getDate("birthDate").toLocalDate(),
                        rs.getString("address"), rs.getInt("zipCode"),
                        rs.getString("country"), rs.getString("phoneNumber"),
                        rs.getString("email"),Integer.toString(rs.getInt("passportNr")));
            }
            //TODO: Password implementation for Data-Privacy
        }catch(Exception e){
            System.err.println("SQL Query Error");
        }
        return null;
    }
    public static Guest getGuest(int guestID){
        String query = "SELECT * FROM guests WHERE guestID = " + guestID;
        try{
            ResultSet rs = Database.getData(query);
            if(rs.first()){
                return new Guest(rs.getInt("guestID"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getDate("birthDate").toLocalDate(),
                        rs.getString("address"), rs.getInt("zipCode"),
                        rs.getString("country"), rs.getString("phoneNumber"),
                        rs.getString("email"),Integer.toString(rs.getInt("passportNr")));
            }
        }catch(Exception e){
            System.err.println("SQL Query Error");
        }
        return null;
    }
}