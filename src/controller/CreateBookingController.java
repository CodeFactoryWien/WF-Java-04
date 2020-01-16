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

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
        bookStage.setTitle("hotel Managing Software");
        bookStage.setScene(FXMLLoader.load(getClass().getResource("/view/createBooking.fxml")));
        bookStage.show();
    }

    // Fill roomTypes from Database //
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
    private void fillTotalCount() throws Exception {
        PreparedStatement preparedStatement =
                Database.c.prepareStatement("SELECT COUNT(*) AS total FROM rooms");

        ResultSet R = preparedStatement.executeQuery();

        while(R.next()) {
            count = R.getInt("total");
        }
        freeRooms.setText(String.valueOf(count));
    }

    // Fill roomPrice from selected roomType from Database //
    private void fillPrice() throws Exception {
        PreparedStatement preparedStatement =
                Database.c.prepareStatement("Select roomTypePrice FROM roomType WHERE roomTypeID = '1'");

        ResultSet R = preparedStatement.executeQuery();

        double selected_roomPrice = 0;

        if (R.first()) {
            selected_roomPrice = R.getDouble("roomTypePrice");
        }

        pricePerDay.setText(String.valueOf(selected_roomPrice));
        totalPrice.setText(String.valueOf(selected_roomPrice*7));
    }

    // Call Fill Methods //
    public void fillDataBox() throws Exception {

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

        Database.firstFreeRoom(roomType.getSelectionModel().toString(), Date.valueOf(checkIn.getValue()),
                Date.valueOf(checkOut.getValue()));
    }
}
