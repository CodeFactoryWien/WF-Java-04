package controller;

import hotel.Guest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class createGuestController {

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


    void createGuest() throws Exception {

        Stage stage2 = new Stage();
        stage2.setTitle("Hotel Managing Software");
        stage2.setScene(FXMLLoader.load(getClass().getResource("/view/createGuest.fxml")));
        stage2.show();
    }

    public void sendGuestData () throws Exception {

        System.out.println(firstName.getText());
        System.out.println(lastName.getText());
        System.out.println(birthDate.getValue());
        System.out.println(address.getText());
        System.out.println(zipCode.getText());
        System.out.println(country.getText());
        System.out.println(phone.getText());
        System.out.println(eMail.getText());
        System.out.println(passportNumber.getText());

        guest G = new guest(1, firstName.getText(), lastName.getText(), birthDate.getValue(), address.getText(), Integer.parseInt(zipCode.getText()),
                country.getText(), phone.getText(), eMail.getText(), Integer.parseInt(passportNumber.getText()));
    }
}
