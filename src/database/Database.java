package database;

import hotel.Guest;
import hotel.Room;


import java.sql.*;
import java.time.ZoneId;

public class Database {

    public static Connection c;

    public static void openConnection() throws Exception {
        if (c == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/wf_java_04", "root", "");
        }
    }

    // Send data TO Database
    private static void setData(PreparedStatement sql) throws Exception {
        sql.executeUpdate();
    }

    // Get Data From Database
    private static ResultSet getData(PreparedStatement preparedStatement) throws Exception {
        return preparedStatement.executeQuery();
    }

    private static Room firstFreeRoom(String roomType, Date start, Date end){
        try{
            PreparedStatement preparedStatement = c.prepareStatement(
                    "SELECT * FROM (SELECT * FROM rooms " +
                            "INNER JOIN roomType ON rooms.fk_roomTypeID = roomType.roomTypeID WHERE roomTypeName = ?)" +
                            " AS room LEFT JOIN bookings ON bookings.fk_roomID = room.roomID WHERE" +
                            " ((bookingFrom > ? OR bookingUntil < ?) OR bookingFrom IS NULL)");
            preparedStatement.setString(1, roomType);
            preparedStatement.setDate(2, end);
            preparedStatement.setDate(3,start);

            ResultSet rs = Database.getData(preparedStatement);
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
        try{
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM guests WHERE email = ?");
            preparedStatement.setString(1,email);
            ResultSet rs = getData(preparedStatement);

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
        try{
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM guests WHERE guestID = ?");
            preparedStatement.setInt(1,guestID);
            ResultSet rs = Database.getData(preparedStatement);
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

    public static void insertNewGuest(Guest guest){

        if( getGuest(guest.getId())== null) {
            try {
                PreparedStatement preparedStatement = c.prepareStatement("INSERT INTO guests (firstName, " +
                        "lastName, birthDate, address, zipCode, country, phoneNumber, email, passportNr, fk_customerID) " +
                        "VALUES (?,?,?,?,?,?,?,?,?, 1)");
                preparedStatement.setString(1, guest.getFirstName());
                preparedStatement.setString(2, guest.getLastName());
                preparedStatement.setDate(
                        3, new Date(Date.from(guest.getBirthDate().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()));
                preparedStatement.setString(4, guest.getAddress());
                preparedStatement.setInt(5, guest.getZipCode());
                preparedStatement.setString(6, guest.getCountry());
                preparedStatement.setString(7, guest.getPhone());
                preparedStatement.setString(8, guest.getEmail());
                preparedStatement.setInt(9, Integer.parseInt(guest.getPassportNumber()));

                setData(preparedStatement);
            } catch (Exception e) {
                System.out.println("Exception preparing statement");
            }
        }else{
            System.out.println("Guest already in Database");
        }
    }
}