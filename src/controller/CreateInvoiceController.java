package controller;

import database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    private int bookingID;


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
            bookingID = MainController.getBookingID();
            // get guest infos
            // get nights and price
            // gett extras services


        }catch (Exception e){
            System.err.println(" Exception in initialize ");
        }
    }

    public void setGuestDetails()throws Exception{
        System.out.println("bookingID in setGuestDetails" + bookingID);
        PreparedStatement preparedStatement =
                Database.c.prepareStatement("SELECT fk_roomID, firstName, lastName, bookingUntil\n" +
                        "FROM bookings\n" +
                        "INNER JOIN guests ON bookings.fk_guestID = guests.guestID\n" +
                        "WHERE bookingID = "+bookingID);
        ResultSet rsHead = preparedStatement.executeQuery();
        while (rsHead.next()){
            int roomID   = rsHead.getInt("fk_roomID");
            lblRoomNr.setText(String.valueOf(roomID));
            System.out.println("roomid angezeigt");
            String firstName = rsHead.getString("firstName");
            String lastName = rsHead.getString("lastName");
            lblGuestName.setText(firstName+ " " + lastName);
            Date bookingUntil = rsHead.getDate("bookingUntil");
            //lblDepature.setText(String.valueOf(bookingUntil));
        }
    }

}
