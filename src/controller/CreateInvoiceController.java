package controller;

import database.Database;
import hotel.InvoiceTable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

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
    @FXML
    private TableView<InvoiceTable> tableInvoice;
    @FXML
    private TableColumn<InvoiceTable,Integer>columnQuantInvocie;
    @FXML
    private TableColumn<InvoiceTable,String>columnServiceInvoice;
    @FXML
    private TableColumn<InvoiceTable,Integer>columnPricePerSInvoice;
    @FXML
    private TableColumn<InvoiceTable,Integer> columnPriceInvoice;

    private int bookingID;
    private Date today;
    private long nights;


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
            today = new Date(new java.util.Date().getTime());
            getData();
            initInvoiceTable();
            // get guest infos
            // get nights and price
            // gett extras services
        }catch (Exception e){
            System.err.println(" Exception in initialize ");
        }
    }

    public void getData()throws Exception{
        try {
            PreparedStatement preparedStatementBooking =
                    Database.c.prepareStatement("SELECT fk_roomID, firstName, lastName, checkedIn \n" +
                            "FROM bookings\n" +
                            "INNER JOIN guests ON bookings.fk_guestID = guests.guestID\n" +
                            "WHERE bookingID = " + bookingID);
            ResultSet rsHead = preparedStatementBooking.executeQuery();
            while (rsHead.next()) {
                int roomID = rsHead.getInt("fk_roomID");
                lblRoomNr.setText(String.valueOf(roomID));
                System.out.println("roomid angezeigt");
                String firstName = rsHead.getString("firstName");
                String lastName = rsHead.getString("lastName");
                Date arrival = rsHead.getDate("checkedIn");
                Date date1 = arrival;
                Date date2 = today;
                long diff = date2.getTime() - date1.getTime();
                nights = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                lblGuestName.setText(firstName + " " + lastName);
                lblQuantNights.setText(String.valueOf(nights));
            }
            PreparedStatement preparedStatementServices =
                    Database.c.prepareStatement("SELECT servicesID, serviceType, serviceDate, coalesce(serv_movies.movieName, serv_wellness.wellnessName, serv_minibar.mbItem) as serviceName, fk_serviceID \n" +
                            " FROM services  \n" +
                            " LEFT OUTER JOIN serv_movies ON (services.fk_serviceID = serv_movies.movieID AND services.serviceType = 'movie') \n" +
                            " LEFT OUTER JOIN serv_wellness ON (services.fk_serviceID = serv_wellness.wellnessID AND services.serviceType = 'wellness') \n" +
                            " LEFT OUTER JOIN serv_minibar ON (services.fk_serviceID = serv_minibar.mbID AND services.serviceType = 'minibar') \n" +
                            " WHERE fk_bookingID=" + bookingID);
            ResultSet rsInvoiceServices = preparedStatementServices.executeQuery();
            while (rsInvoiceServices.next()){
                int i = rsInvoiceServices.getInt("servicesID");
                String serviceType = rsInvoiceServices.getString("serviceType");
                Date serviceDate = rsInvoiceServices.getDate("serviceDate");
                String serviceName = rsInvoiceServices.getString("serviceName");
            }

        }catch (Exception e){
            System.out.println("getdata exption");
        }
    }


    public void initInvoiceTable()throws Exception{
        try {
                columnQuantInvocie.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
                columnServiceInvoice.setCellValueFactory(new PropertyValueFactory<>("Services"));
                columnPricePerSInvoice.setCellValueFactory(new PropertyValueFactory<>("PPS"));
                columnPriceInvoice.setCellValueFactory(new PropertyValueFactory<>("Price"));

                //tableInvoice.getItems(objekt der rechnugnszeilen)

        }catch (Exception e){
            System.out.println("Befüllen der Rechnungstabelle nicht möglich");
        }
    }

}
