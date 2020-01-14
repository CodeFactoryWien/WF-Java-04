package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class createRoomController {

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

    void createRoom() throws Exception {

        Stage stage2 = new Stage();
        stage2.setTitle("Hotel Managing Software");
        stage2.setScene(FXMLLoader.load(getClass().getResource("/view/createRoom.fxml")));
        stage2.show();
    }

    public void sendRoomData () throws Exception {
        System.out.println(roomType.getValue());
        System.out.println(roomCapacity.getValue());
        System.out.println(roomPrice.getText());
        System.out.println(roomSize.getText());
        System.out.println(roomFacilitys.getText());
    }
}
