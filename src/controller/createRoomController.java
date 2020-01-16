package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class createRoomController {

    private Stage S;

    // Room Fields //
    @FXML
    private ChoiceBox roomType;
    @FXML
    private ChoiceBox roomCapacity;
    @FXML
    private TextField roomPrice;
    @FXML
    private TextField roomSize;
    @FXML
    private TextField roomFacilitys;
    @FXML
    private Button cancel;

    // Start Controller Method //
    void start() throws Exception {
        Stage S = new Stage();
        S.setTitle("Hotel Managing Software");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/createRoom.fxml")));
        S.show();
    }

    // Create new Room //
    public void sendRoomData () throws Exception {
    }

    // Close Window(Stage) //
    public void exit() {
        S = (Stage) cancel.getScene().getWindow();
        S.close();
    }
}
