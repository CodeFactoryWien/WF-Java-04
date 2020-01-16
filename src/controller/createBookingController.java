package controller;

import Hotel.Guest;
import database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class createBookingController {

    private Stage S;

    // Room, Price Fields //
    @FXML
    private ChoiceBox roomType;
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

        S = new Stage();
        S.setTitle("Hotel Managing Software");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/createBooking.fxml")));
        S.show();
    }

    // Fill the Data from Database and calculate //
    public void fillDataBox() {

        freeRooms.setText("20");
        pricePerDay.setText("50");
        totalPrice.setText("300");
    }

    // Close Window(Stage) //
    public void exit() {
        S = (Stage) cancel.getScene().getWindow();
        S.close();
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
}
