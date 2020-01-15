package controller;

import hotel.Guest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class createGuestController {

    private Stage S;

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

    public void start() throws IOException {
        Stage S = new Stage();
        S.setTitle("Hotel Managing Software");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/createGuest.fxml")));
        S.show();
    }

    public void sendGuestData() throws Exception {
        S.setTitle("TTDSFDF");
    }

    public void exit() {
        S = (Stage) cancel.getScene().getWindow();
        S.close();

    public void sendGuestData (){

        System.out.println(firstName.getText());
        System.out.println(lastName.getText());
        System.out.println(birthDate.getValue());
        System.out.println(address.getText());
        System.out.println(zipCode.getText());
        System.out.println(country.getText());
        System.out.println(phone.getText());
        System.out.println(eMail.getText());
        System.out.println(passportNumber.getText());

        Guest G = new Guest(1, firstName.getText(), lastName.getText(), birthDate.getValue(), address.getText(), Integer.parseInt(zipCode.getText()),
                country.getText(), phone.getText(), eMail.getText(), passportNumber.getText());
    }
}
