package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class createBookingController {

    private Stage bookingStage;

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


    public void createBooking() throws Exception {

        bookingStage = new Stage();
        bookingStage.setTitle("Hotel Managing Software");
        bookingStage.setScene(FXMLLoader.load(getClass().getResource("/view/createBooking.fxml")));
        bookingStage.show();
    }

    public void fillDataBox() {

        freeRooms.setText("20");
        pricePerDay.setText("50");
        totalPrice.setText("300");
    }

    public void next() throws Exception {
        createGuestController C = new createGuestController();
                C.createGuest();
    }

    public void close() {
    }


}
