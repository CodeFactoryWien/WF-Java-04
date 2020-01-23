package database;

import hotel.Guest;
import hotel.Room;
import javafx.scene.control.Alert;


import java.sql.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Locale;

public class Database {

    public static Connection c;

    public static void openConnection() throws Exception {
        if (c == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/wf_java04_hotel", "root", "");
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

    public static Room firstFreeRoom(String roomType, LocalDate start, LocalDate end){
        try{
            PreparedStatement preparedStatement = c.prepareStatement(
                    "SELECT * FROM (SELECT * FROM rooms " +
                            "INNER JOIN roomType ON rooms.fk_roomTypeID = roomType.roomTypeID WHERE roomTypeName = ?)" +
                            " AS room LEFT JOIN bookings ON bookings.fk_roomID = room.roomID WHERE" +
                            " (roomID NOT IN (SELECT fk_roomID FROM bookings " +
                            "WHERE ((bookingFrom >= ? AND bookingFrom <= ?) OR (bookingUntil >= ? AND bookingUntil <= ?))) " +
                            "OR bookingFrom IS NULL)");
            preparedStatement.setString(1, roomType);
            preparedStatement.setDate(2, Date.valueOf(start));
            preparedStatement.setDate(3, Date.valueOf(end));
            preparedStatement.setDate(4, Date.valueOf(start));
            preparedStatement.setDate(5, Date.valueOf(end));
            ResultSet rs = Database.getData(preparedStatement);
            if(rs.first()){
                return new Room(rs.getInt("roomID"),rs.getString("roomTypeName"),rs.getDouble("roomTypePrice"),
                        rs.getInt("roomTypeCapacity"),rs.getDouble("roomSize"),
                        "",rs.getString("roomTypeFacilities"));
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
            System.err.println(e.toString());
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


    public static String checkUserName(String username){
        try{
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM users WHERE userName = ?");
            preparedStatement.setString(1, username);
            ResultSet rs = Database.getData(preparedStatement);
            if(rs.first()){
                if(rs.getString("userName").equals(username)) {
                    return rs.getString("userName");
                }
            }
        }catch(Exception e){
            System.err.println("SQL Query Error");
            System.err.println(e.toString());
        }
        return "0";
    }


    public static String getPasswordHash(String username){
        try{
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM users WHERE userName = ?");
            preparedStatement.setString(1, username);
            ResultSet rs = Database.getData(preparedStatement);
            if(rs.first()){
                return rs.getString("userPassword");
            }
        }catch(Exception e){
            System.err.println("SQL Query Error");
            System.err.println(e.toString());
        }
        return "0";
    }

    public static String getRoomTypePrice(String roomTypeName) {
        try{
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM roomtype WHERE roomTypeName = ?");
            preparedStatement.setString(1, roomTypeName);
            ResultSet rs = Database.getData(preparedStatement);
            if(rs.first()){
                int dbCents = rs.getInt("roomTypePrice");
                NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
                return nf.format(dbCents/100.0);
            }
        }catch(Exception e){
            System.err.println("SQL Query Error");
            System.err.println(e.toString());
        }
        return "";
    }

    public static void setNewRoomTypePrice(String roomTypeName, String roomTypePrice){
        try{
            PreparedStatement preparedStatement = c.prepareStatement("UPDATE `roomtype` SET `roomTypePrice` = ? WHERE `roomtype`.`roomTypeName` = ?");
            preparedStatement.setInt(1, Integer.parseInt(roomTypePrice));
            preparedStatement.setString(2, roomTypeName);
            setData(preparedStatement);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setHeaderText(null);
            alert.setContentText("The price for " + roomTypeName + " has been successfully updated!");

            alert.showAndWait();
        } catch(Exception e){
            System.err.println("SQL Query Error");
            System.err.println(e.toString());
        }
    }

    public static void createNewRoom(String roomTypeName, String roomTypeSize){
        try {
            PreparedStatement preparedStatement = c.prepareStatement("INSERT INTO rooms (roomID, fk_roomTypeID, roomSize) " +
                    "VALUES (NULL,?,?)");
            switch (roomTypeName) {
                case "Single Room":
                    preparedStatement.setString(1, "1");
                    break;
                case "Single Room with Balcony":
                    preparedStatement.setString(1, "2");
                    break;
                case "Double Room":
                    preparedStatement.setString(1, "3");
                    break;
                case "Double Room with Balcony":
                    preparedStatement.setString(1, "4");
                    break;
                case "Suite":
                    preparedStatement.setString(1, "5");
                    break;
                case "Suite with Balcony":
                    preparedStatement.setString(1, "6");
                    break;
                case "Superior Double Room":
                    preparedStatement.setString(1, "7");
                    break;
                case "Superior Double Room with Balcony":
                    preparedStatement.setString(1, "8");
                    break;
                    default:
                        System.out.println("No.");
                    break;
            }
            preparedStatement.setString(2, roomTypeSize);
            setData(preparedStatement);
            System.out.println("New room has been created.");
        }
        catch (Exception e){
            System.err.println("SQL Query Error");
            System.err.println(e.toString());
        }
    }
}