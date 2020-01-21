package controller;

import hotel.Guest;
import database.Database;
import hotel.Room;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CreateBookingController {

    private Stage bookStage;

    // Room, price fields //
    private int count;
    private double selectedRoomPrice;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int days;
    private ObservableList<String> roomtypeslist;

    // Guest Object //
    private Guest guest;

    @FXML
    private ChoiceBox<String> roomType;
    @FXML
    private DatePicker checkIn;
    @FXML
    private DatePicker checkOut;
    @FXML
    private Label freeRooms;
    @FXML
    private Label pricePerDay;
    @FXML
    private Label totalPrice;

    // Vbox of guest fields //
    @FXML
    private VBox column2;

    // Guest fields//
    @FXML
    private TextField lastName;
    @FXML
    private TextField firstName;
    @FXML
    private DatePicker birthDate;
    @FXML
    private TextField address;
    @FXML
    private TextField zipCode;
    @FXML
    private TextField country;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField email;
    @FXML
    private TextField passportNr;
    @FXML
    private ListView listViewFoundGuest;

    // Cancel and booking button //
    @FXML
    private Button cancel;
    @FXML
    private Button booking;

    // Init method //
    public void initialize() throws Exception {
        // Database search //
        lastName.setOnKeyTyped(e -> databaseSearch(lastName));
        firstName.setOnKeyTyped(e -> databaseSearch(firstName));
        address.setOnKeyTyped(e -> databaseSearch(address));
        zipCode.setOnKeyTyped(e -> databaseSearch(zipCode));
        country.setOnKeyTyped(e -> databaseSearch(country));
        phoneNumber.setOnKeyTyped(e -> databaseSearch(phoneNumber));
        email.setOnKeyTyped(e -> databaseSearch(email));
        passportNr.setOnKeyTyped(e -> databaseSearch(passportNr));

        // Booking call //
        booking.setOnMouseClicked(e -> addBooking());

        // Fill choicebox on init with roomtypes from database //
        fillRoomTypes();

        // Listener for choicebox -> call //
        roomType.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            try {
                bookaroom();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Datepicker call -> bookaroom //
        checkIn.setOnAction(e -> bookaroom());
        checkOut.setOnAction(e -> bookaroom());
        // Datepicker editable set false //
        checkIn.setEditable(false);
        checkOut.setEditable(false);

        // close method on button click //
        cancel.setOnMouseClicked(e -> close());
    }

    // Start controller method //
    public void start() throws Exception {
        bookStage = new Stage();
        bookStage.setTitle("hotel Managing Software");
        bookStage.setScene(FXMLLoader.load(getClass().getResource("/view/createBooking.fxml")));
        bookStage.show();
    }

    // Method called when choicebox, checkIn or checkOut is fired //
    public void bookaroom() {

        if (roomType.getValue() != null && checkIn.getValue() != null && checkOut.getValue() != null) {
            if (checkIn.getValue().isBefore(checkOut.getValue())) {
                column2.setDisable(false);
            } else {
                column2.setDisable(true);
            }
        }
        try {
            totalselectedroomcount();
            fillpriceperday();
            if (checkIn.getValue() != null) {
                checkInDate = checkIn.getValue();
            }
            if (checkOut.getValue() != null) {
                checkOutDate = checkOut.getValue();
            }

            days = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);

            if (column2.isDisable()) {
                totalPrice.setText("///");
            } else {
                totalPrice.setText(String.valueOf(selectedRoomPrice * days));
            }
            } catch (Exception ignored) {
        }
    }

    // #####FILL METHODS START HERE##### //

    // Fill roomtypes from database called from when choicebox shown //
    public void fillRoomTypes() {
        try {
            PreparedStatement preparedStatement =
                    Database.c.prepareStatement("SELECT roomTypeName FROM roomtype");

            ResultSet R = preparedStatement.executeQuery();

            roomtypeslist = FXCollections.observableArrayList();

            while (R.next()) {
                String typeName = R.getString("roomTypeName");
                roomtypeslist.add(typeName);
            }
        } catch (Exception e) {
            System.out.println("Error fillroomtypes not working");
        }
        roomType.setItems(roomtypeslist);
    }

    // Fill total count of free rooms from database based on selected roomtype//
    public void totalselectedroomcount() {

        try {
            PreparedStatement preparedStatement =
                    Database.c.prepareStatement("SELECT COUNT(*) AS roomCount FROM rooms " +
                            "JOIN roomtype ON fk_roomTypeID = roomTypeID " +
                            "where roomTypeName = ?");
            preparedStatement.setString(1, roomType.getValue());

            ResultSet R = preparedStatement.executeQuery();

            while (R.next()) {
                count = R.getInt("roomCount");
            }
        } catch (Exception e) {
            System.out.println("Error room count call not possible");
        }
        freeRooms.setText(String.valueOf(count));
    }

    // Fill roomPrice from selected roomType from database //
    public void fillpriceperday() {

        String selectedRoomType = roomType.getSelectionModel().getSelectedItem();

        try {
            PreparedStatement preparedStatement =
                    Database.c.prepareStatement("Select roomTypePrice FROM roomType WHERE roomTypeName = ?");
            preparedStatement.setString(1, selectedRoomType);

            ResultSet R = preparedStatement.executeQuery();

            if (R.first()) {
                selectedRoomPrice = R.getDouble("roomTypePrice");
            }
        } catch (Exception e) {
            System.out.println("Error fillpriceperday not working");
        }

        pricePerDay.setText(String.valueOf(selectedRoomPrice));
    }

    // #####FILL METHODS ENDS HERE##### //

    // Close Window(Stage) //
    public void close() {
        bookStage = (Stage) cancel.getScene().getWindow();
        bookStage.close();
    }

    // #####Guest create / search in database and create booking methods ##### //

    // New guest (not found in database) create //
    public void sendGuestDataToDatabase() {
        guest = new Guest(1, firstName.getText(), lastName.getText(), birthDate.getValue(), address.getText(),
                Integer.parseInt(zipCode.getText()), country.getText(), phoneNumber.getText(),
                email.getText(), passportNr.getText());

        try {
            PreparedStatement P = Database.c.prepareStatement("INSERT INTO guests (firstName, lastName, birthDate, " +
                    "address, zipCode, country, phoneNumber, email, passportNr, fk_customerID)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, 7)");
            P.setString(1, guest.getFirstName());
            P.setString(2, guest.getLastName());
            P.setDate(3, Date.valueOf(guest.getBirthDate()));
            P.setString(4, guest.getAddress());
            P.setInt(5, guest.getZipCode());
            P.setString(6, guest.getCountry());
            P.setString(7, guest.getPhone());
            P.setString(8, guest.getEmail());
            P.setString(9, guest.getPassportNumber());


            P.executeUpdate();
        } catch (Exception e) {
            System.out.println("New guest create function error");
        }
    }

    // Everytime a character is typed method is called //
    public void databaseSearch(TextField obj) {
        if (obj.getText() != null) {
            String userInput = obj.getText();

            String objID = obj.getId();

            ObservableList<String> O = FXCollections.observableArrayList();

            ResultSet R;
            try {
                PreparedStatement P = Database.c.prepareStatement("SELECT lastName, firstName, address FROM guests WHERE " + objID + " LIKE ?");
                P.setString(1, userInput + "%");
                R = P.executeQuery();

                while (R.next()) {
                    String S1 = R.getString("lastName");
                    String S2 = R.getString("firstName");
                    String S3 = R.getString("address");

                    O.add(S1.concat(" " + S2 + " ").concat(S3));
                }
            } catch (Exception e) {
                System.out.println("Error database search not possible");
            }

            listViewFoundGuest.setItems(O);
        }
    }

    // Add booking function //
    public void addBooking() {

        sendGuestDataToDatabase();

        String guestID = null;

        try {
            PreparedStatement P = Database.c.prepareStatement("SELECT guests.guestID FROM guests " +
                    "WHERE guests.firstName = ? AND guests.lastName = ? AND guests.birthDate = ? " +
                            "AND guests.address = ?");
            P.setString(1, guest.getFirstName());
            P.setString(2, guest.getLastName());
            P.setDate(3, Date.valueOf(guest.getBirthDate()));
            P.setString(4, guest.getAddress());

            ResultSet R = P.executeQuery();

            while (R.next()) {
                guestID = R.getString("guestID");
            }

        } catch (Exception e) {
            System.out.println("Get guest not working");
        }

        Room R = Database.firstFreeRoom(roomType.getValue(), checkIn.getValue(), checkOut.getValue());

        LocalDate ld3 = LocalDate.parse("2017-05-22");

        try {
            PreparedStatement preparedStatement =
                    Database.c.prepareStatement("INSERT INTO bookings (fk_roomID, fk_guestID, fk_customerID, openAmount, bookingFrom, bookingUntil, bookingCanceled, checkedIn) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            preparedStatement.setInt(1, R.getId());
            preparedStatement.setInt(2, Integer.parseInt(guestID));
            preparedStatement.setInt(3, 5);
            preparedStatement.setInt(4, (int) (selectedRoomPrice));
            preparedStatement.setDate(5, Date.valueOf(checkIn.getValue()));
            preparedStatement.setDate(6, Date.valueOf(checkOut.getValue()));
            preparedStatement.setDate(7, Date.valueOf(ld3));
            preparedStatement.setInt(8, 8);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error booking cannot be created");
        }
    }
}
