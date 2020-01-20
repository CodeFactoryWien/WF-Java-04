package controller;

import exceptions.CheckInDateMissing_Exception;
import exceptions.CheckOutDateMissing_Exception;
import exceptions.RoomTypeMissing_Exception;
import hotel.Guest;
import database.Database;
import hotel.Room;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CreateBookingController {

    private Stage bookStage;

    // Room, Price Fields //
    private int count;
    private java.time.LocalDate checkInDate;
    private java.time.LocalDate checkOutDate;
    private double selectedRoomPrice;
    private int days;

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

    // Guest Fields//
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

    // cancel and booking button //

    @FXML
    private Button cancel;
    @FXML
    private Button booking;

    public void initialize() throws Exception {
        lastName.setOnKeyTyped(e -> databaseSearch(lastName));
        firstName.setOnKeyTyped(e -> databaseSearch(firstName));
        address.setOnKeyTyped(e -> databaseSearch(address));
        zipCode.setOnKeyTyped(e -> databaseSearch(zipCode));
        country.setOnKeyTyped(e -> databaseSearch(country));
        phoneNumber.setOnKeyTyped(e -> databaseSearch(phoneNumber));
        email.setOnKeyTyped(e -> databaseSearch(email));
        passportNr.setOnKeyTyped(e -> databaseSearch(passportNr));

        booking.setOnMouseClicked(e -> addBooking());

        fillRoomTypes();

        roomType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try {
                    column1main();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Start Controller Method //
    public void start() throws Exception {
        bookStage = new Stage();
        bookStage.setTitle("hotel Managing Software");
        bookStage.setScene(FXMLLoader.load(getClass().getResource("/view/createBooking.fxml")));
        bookStage.show();
    }

    public void column1main() throws Exception {

        try {
            totalCountOfSelectedRoom();
            fillPrice();
            checkInDate = checkIn.getValue();
            checkOutDate = checkOut.getValue();

            try {
                days = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            } catch (Exception e) {
                System.out.println("cannot calculate days");
            }

            totalPrice.setText(String.valueOf(selectedRoomPrice * days));

        } catch (RoomTypeMissing_Exception | CheckInDateMissing_Exception | CheckOutDateMissing_Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // #####FILL METHODS START HERE##### //

    // Fill roomTypes from Database called from when choiceBox shown //
    public void fillRoomTypes() throws Exception {
        PreparedStatement preparedStatement =
                Database.c.prepareStatement("SELECT roomTypeName FROM roomtype");

        ResultSet R = preparedStatement.executeQuery();

        ObservableList<String> O = FXCollections.observableArrayList();

        while (R.next()) {
            String typeName = R.getString("roomTypeName");
            O.add(typeName);
        }
        roomType.setItems(O);
    }

    // Fill total count of free rooms from Database //
    public void totalCountOfSelectedRoom() throws Exception {

        PreparedStatement preparedStatement =
                Database.c.prepareStatement("SELECT COUNT(*) AS roomCount FROM rooms " +
                        "JOIN roomtype ON fk_roomTypeID = roomTypeID " +
                        "where roomTypeName = ?");
        preparedStatement.setString(1, roomType.getValue());

        ResultSet R = preparedStatement.executeQuery();

        while (R.next()) {
            count = R.getInt("roomCount");
        }
        freeRooms.setText(String.valueOf(count));
    }

    // Fill roomPrice from selected roomType from Database //
    public void fillPrice() throws Exception {

        if (roomType.getSelectionModel().getSelectedItem() == null) {
            throw new RoomTypeMissing_Exception("RoomType is missing pls select one");
        }
        String selectedRoomType = roomType.getSelectionModel().getSelectedItem();

        PreparedStatement preparedStatement =
                Database.c.prepareStatement("Select roomTypePrice FROM roomType WHERE roomTypeName = ?");
        preparedStatement.setString(1, selectedRoomType);

        ResultSet R = preparedStatement.executeQuery();

        if (R.first()) {
            selectedRoomPrice = R.getDouble("roomTypePrice");
        }
        pricePerDay.setText(String.valueOf(selectedRoomPrice));
    }

    // #####FILL METHODS ENDS HERE##### //

    public void exit() {
        bookStage = (Stage) cancel.getScene().getWindow();
        bookStage.close();
    }

    // #####Guest Create / Search in Database Methods##### //
    // Close Window(Stage) //

    // New Guest (Not found in Database) create //
    public void sendGuestDataToDatabase() {
        Guest G = new Guest(1, lastName.getText(), firstName.getText(), birthDate.getValue(), address.getText(),
                Integer.parseInt(zipCode.getText()), country.getText(), phoneNumber.getText(), email.getText(), passportNr.getText());
    }

    // Everytime a character is typed method is called //
    public void databaseSearch(TextField obj) {
        if (obj.getText() != null) {
            String userInput = obj.getText();

            String objID = obj.getId();

            ObservableList<String> O = FXCollections.observableArrayList();

            ResultSet R;
            try {
                PreparedStatement P1 = Database.c.prepareStatement("SELECT lastName, firstName, address FROM guests WHERE " + objID + " LIKE ?");
                P1.setString(1, userInput + "%");
                R = P1.executeQuery();

                while (R.next()) {
                    String S1 = R.getString("lastName");
                    String S2 = R.getString("firstName");
                    String S3 = R.getString("address");

                    O.add(S1.concat(" " + S2 + " ").concat(S3));
                }
            } catch (Exception e) {
                System.out.println("Error");
            }

            listViewFoundGuest.setItems(O);
        }
    }

    public void addBooking() {

        Guest G = new Guest(1, lastName.getText(), firstName.getText(), birthDate.getValue(), address.getText(),
                Integer.parseInt(zipCode.getText()), country.getText(), phoneNumber.getText(), email.getText(), passportNr.getText());

        Room R = Database.firstFreeRoom(roomType.getValue(), checkIn.getValue(), checkOut.getValue());

        LocalDate ld3 = LocalDate.parse("2017-05-22");

        try {
            PreparedStatement preparedStatement =
                    Database.c.prepareStatement("INSERT INTO bookings (fk_roomID, fk_guestID, fk_customerID, openAmount, bookingFrom, bookingUntil, bookingCanceled, checkedIn) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            preparedStatement.setInt(1, R.getId());
            preparedStatement.setInt(2, G.getId());
            preparedStatement.setInt(3, 5);
            preparedStatement.setInt(4, (int) (selectedRoomPrice * days));
            preparedStatement.setDate(5, Date.valueOf(checkIn.getValue()));
            preparedStatement.setDate(6, Date.valueOf(checkOut.getValue()));
            preparedStatement.setDate(7, Date.valueOf(ld3));
            preparedStatement.setInt(8, 8);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error");
        }
    }
}
