package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainController {
    @FXML
    private Button logoutButton;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab adminTab;

    boolean userIsAdmin;

    void start() throws Exception {
        Stage S = new Stage();
        S.setTitle("hotel Managing Software");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/main.fxml")));
        S.show();
    }

    @FXML
    private void initialize(){
        if(!userIsAdmin){
            tabPane.getTabs().remove(adminTab);
        }
    }

    public void logout() throws Exception {
        LoginController L = new LoginController();
        L.start();
        Stage S = (Stage) logoutButton.getScene().getWindow();
        S.close();
    }

    public void setAdminStatus(){
        userIsAdmin = true;
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
