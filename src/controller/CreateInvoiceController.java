package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CreateInvoiceController {
    @FXML
    private Label lblRoomNr;
    @FXML
    private Label lblGuestName;
    @FXML
    private Label lblQuantNights;
    @FXML
    private Label lblQuantServices;
    @FXML
    private Button btnEditGuest;
    @FXML
    private Button btnCreateInvoice;
    @FXML
    private Button btnCancel;



    void start() throws Exception {
        Stage S = new Stage();
        S.setTitle("Showing Details for Selection");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/createInvoice.fxml")));
        S.setResizable(false);
        S.show();
    }

    @FXML
    public void initialize(){
        try {

        }catch (Exception e){
            System.err.println(" Exception in initialize ");
        }
    }
}
