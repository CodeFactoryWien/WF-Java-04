package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import sample.Main;

public class MainController {
    @FXML
    private Button logoutButton;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab adminTab;

    static boolean userIsAdmin;


    void start() throws Exception {
        Stage S = new Stage();
        S.setTitle("hotel Managing Software");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/main.fxml")));
        S.show();
    }

    @FXML
    private void initialize(){
        if(MainController.userIsAdmin){
            System.out.println("Admin logged in.");
        } else {
            tabPane.getTabs().remove(adminTab);
            System.out.println("User logged in.");
        }
    }

    public void logout() throws Exception {
        MainController.userIsAdmin = false;
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
