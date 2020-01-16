package controller;

import hotel.Guest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateGuestController {

    private Stage S;

    // Guest Fields //
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
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
    @FXML
    private Button cancel;

    // Start Controller Method //
    public void start() throws IOException {
        Stage S = new Stage();
        S.setTitle("Hotel Managing Software");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/createGuest.fxml")));
        S.show();
    }

    // New Guest (Not found in Database) create //
    public void sendGuestData() throws Exception {
        Guest G = new Guest(1, lastName.getText(), firstName.getText(), birthDate.getValue(), address.getText(),
                Integer.parseInt(zipCode.getText()), country.getText(), phone.getText(), eMail.getText(), passportNumber.getText());
    }

    // Close Window(Stage) //
    public void exit() {
        S = (Stage) cancel.getScene().getWindow();
        S.close();
    }
}
