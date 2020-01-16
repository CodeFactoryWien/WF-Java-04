package controller;

import hotel.Guest;
import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CreateBookingController {

    private Stage bookStage;

    // Room, Price Fields //
    private int count;

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
        bookStage.setTitle("Hotel Managing Software");
        bookStage.setScene(FXMLLoader.load(getClass().getResource("/view/createBooking.fxml")));
        bookStage.show();
    }

    private void fillRoomTypes() throws Exception {
        ResultSet R = Database.getData("SELECT roomTypeName FROM roomtype");

        ObservableList<String> O = FXCollections.observableArrayList();

        while (R.next()) {
            String typeName = R.getString("roomTypeName");
            System.out.println(typeName);
            O.add(typeName);
        }
        roomType.setItems(O);
    }

    // Fill total count of free rooms from Database //
    private void fillTotalCount() throws Exception {
        ResultSet R = Database.getData("SELECT COUNT(*) AS total FROM rooms");

        while(R.next()) {
            count = R.getInt("total");
        }
        freeRooms.setText(String.valueOf(count));
    }

    // Fill roomPrice from selected roomType from Database //
    private void fillPrice() throws Exception {
        ResultSet R = Database.getData("Select roomTypePrice FROM roomType WHERE roomTypeID = '1'");

        double selected_roomPrice = 0;

        while (R.next()) {
            selected_roomPrice = R.getDouble("roomTypePrice");
        }

        pricePerDay.setText(String.valueOf(selected_roomPrice));
        totalPrice.setText(String.valueOf(selected_roomPrice*7));
    }

    // Fill the Data from Database and calculate //
    public void fillDataBox() throws Exception {

        fillRoomTypes();

        fillTotalCount();

        fillPrice();
    }

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

        Room.firstFreeRoom(roomType.getSelectionModel().toString(), Date.valueOf(checkIn.getValue()),
                Date.valueOf(checkOut.getValue()));
    }
}
