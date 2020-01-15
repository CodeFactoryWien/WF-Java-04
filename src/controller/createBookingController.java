package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class createBookingController {

    private Stage S;

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
    @FXML
    private Button next;


    public void start() throws Exception {

        S = new Stage();
        S.setTitle("Hotel Managing Software");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/createBooking.fxml")));
        S.show();
    }

    public void fillDataBox() {

        freeRooms.setText("20");
        pricePerDay.setText("50");
        totalPrice.setText("300");
    }

    public void next() throws Exception {
        S = (Stage) next.getScene().getWindow();
        S.setScene(FXMLLoader.load(getClass().getResource("/view/createGuest.fxml")));
    }

    public void exit() {
        S = (Stage) cancel.getScene().getWindow();
        S.close();
    }


}
