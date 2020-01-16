package controller;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class MainController {

    void start() throws Exception {
        Stage S = new Stage();
        S.setTitle("Hotel Managing Software");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/main.fxml")));
        S.show();
    }

    // Create new Guest //
    public void call_createGuestController() throws Exception {
       CreateGuestController C = new CreateGuestController();
       C.start();
    }

    // Create new Room //
    public void call_createRoomController() throws Exception {
        CreateRoomController C = new CreateRoomController();
        C.start();
    }

    // Create new Booking //
    public void call_createBookingController() throws Exception {
        CreateBookingController C = new CreateBookingController();
        C.start();
    }

    // Test Details //
    public void call_detailsController() throws Exception {
        DetailsController C = new DetailsController();
        C.start();
    }
}
