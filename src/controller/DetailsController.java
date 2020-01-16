package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DetailsController{
    private Stage S;


    @FXML

    // Start Controller Method //
    void start() throws Exception {
        Stage S = new Stage();
        S.setTitle("Showing Details for Selection");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/details.fxml")));
        S.show();
    }
}