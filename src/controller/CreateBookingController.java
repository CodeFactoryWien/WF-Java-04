package controller;

import exceptions.CheckInDateMissing_Exception;
import exceptions.CheckOutDateMissing_Exception;
import exceptions.RoomTypeMissing_Exception;
import hotel.Guest;
import database.Database;
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

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLData;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class CreateBookingController {

    private Stage bookStage;

    // Room, Price Fields //
    private int count;
    private java.util.Date checkInDate;
    private java.util.Date checkOutDate;
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
    @FXML
    private Button cancel;

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
    private TextField phone;
    @FXML
    private TextField eMail;
    @FXML
    private TextField passportNumber;

    // Start Controller Method //
    public void start() throws Exception {
        bookStage = new Stage();
        bookStage.setTitle("hotel Managing Software");
        bookStage.setScene(FXMLLoader.load(getClass().getResource("/view/createBooking.fxml")));
        bookStage.show();
    }

    public void column1main() throws Exception {

        fillTotalCount();

        try {
            fillPrice();
            getCheckInDate();
            getCheckOutDate();
            calcDays();
            fillTotalPrice();

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
        // add a listener
        roomType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            // if the item of the list is changed
            public void changed(ObservableValue ov, Number value, Number new_value)
            {
                try {
                    column1main();
                } catch (Exception e) {
                    System.out.println("Error");
                }
            }
        });
    }

    // Fill total count of free rooms from Database //
    public void fillTotalCount() throws Exception {
        PreparedStatement preparedStatement =
                Database.c.prepareStatement("SELECT COUNT(*) AS total FROM rooms");

        ResultSet R = preparedStatement.executeQuery();

        while(R.next()) {
            count = R.getInt("total");
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

    // Fill Total Price calc with the days between 2 dates //
    public void fillTotalPrice() {
        totalPrice.setText(String.valueOf(selectedRoomPrice*days));
    }

    // #####FILL METHODS ENDS HERE##### //



    // #####DATE METHODS START HERE##### //

    // Get CheckIN Date //
    public java.util.Date getCheckInDate() {
        try {
            if (checkIn.getValue() == null) {
                throw new CheckInDateMissing_Exception("Missing checkIn date pls select or type a correct date");
            }
            LocalDate ld = checkIn.getValue();
            Calendar c = Calendar.getInstance();
            c.set(ld.getYear(), ld.getMonthValue() - 1, ld.getDayOfMonth());
            checkInDate = c.getTime();

        } catch (CheckInDateMissing_Exception e) {
            System.out.println(e.getMessage());
        }
        return checkInDate;
    }

    // Get CheckOut Date //
    public java.util.Date getCheckOutDate() {
        try {
            if (checkOut.getValue() == null) {
                throw new CheckOutDateMissing_Exception("Missing checkOut date pls select or type a correct date");
            }
            LocalDate ld = checkOut.getValue();
            Calendar c = Calendar.getInstance();
            c.set(ld.getYear(), ld.getMonthValue() - 1, ld.getDayOfMonth());
            checkOutDate = c.getTime();

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return checkOutDate;
    }

    // Calc Days between Dates //
    private void calcDays() {
        try {
            days = (int) ChronoUnit.DAYS.between(checkInDate.toInstant(), checkOutDate.toInstant());
        } catch (Exception e) {
            System.out.println("cannot calculate days");
        }
    }
    // #####DATE METHODS ENDS HERE##### //


    // Close Window(Stage) //
    public void exit() {
        bookStage = (Stage) cancel.getScene().getWindow();
        bookStage.close();
    }

    // New Guest (Not found in Database) create //
    public void sendGuestDataToDatabase() {
        Guest G = new Guest(1, lastName.getText(), firstName.getText(), birthDate.getValue(), address.getText(),
                Integer.parseInt(zipCode.getText()), country.getText(), phone.getText(), eMail.getText(), passportNumber.getText());
    }

    // Everytime a character is typed method is called //
    public void databaseSearch() {
        //Database Querry insert//
    }

    public void addBooking() {
        Guest G = new Guest(1, lastName.getText(), firstName.getText(), birthDate.getValue(), address.getText(),
                Integer.parseInt(zipCode.getText()), country.getText(), phone.getText(), eMail.getText(), passportNumber.getText());

        Database.firstFreeRoom(roomType.getSelectionModel().toString(), Date.valueOf(checkIn.getValue()),
                Date.valueOf(checkOut.getValue()));
    }
}
