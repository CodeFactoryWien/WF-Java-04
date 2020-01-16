package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DetailsController{
    private Stage S;

    @FXML
    private Label roomNr;
    @FXML
    private Label guestName;
    @FXML
    private Label depature;
    @FXML
    private Label amount;
    @FXML
    private ChoiceBox choiceMovie;
    @FXML
    private ChoiceBox choiseWellness;
    @FXML
    private ChoiceBox choiseMinibar;

    @FXML
    private ListView listMovie;
    @FXML
    private ListView listWellness;
    @FXML
    private ListView listMinibar;


    // Start Controller Method //
    void start() throws Exception {
        Stage S = new Stage();
        S.setTitle("Showing Details for Selection");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/details.fxml")));
        S.show();
    }



}