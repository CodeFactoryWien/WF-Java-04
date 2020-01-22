package controller;

import database.Database;
import hotel.InvoiceTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

public class CreateInvoiceController {
    @FXML
    private Label lblRoomNr, lblGuestName, lblQuantNights, lblQuantServices, lblTotal, lblTax;
    @FXML
    private Button btnEditGuest, btnCreateInvoice, btnCancel;
    @FXML
    private TableView<InvoiceTable> tableInvoice;
    @FXML
    private TableColumn<InvoiceTable,Integer>columnID, columnQuant, columnPPS,columnPrice;
    @FXML
    private TableColumn<InvoiceTable,Date>columnDate;
    @FXML
    private TableColumn<InvoiceTable,String>columnType, columnName;


    private int bookingID;
    private Date today;
    private int nights;
    private int total;
    private ObservableList<InvoiceTable> servicesInvoiceList;


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
            getAndProvideData();
            initInvoiceTable();
            btnCreateInvoice.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    try {
                        generateInvoice();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            btnCancel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    clsoe();
                }
            });

        }catch (Exception e){
            System.err.println(" Exception in initialize ");
        }
    }

    public void getAndProvideData()throws Exception{
        try {
            PreparedStatement preparedStatementBooking =
                    Database.c.prepareStatement("SELECT fk_roomID, firstName, lastName, checkedIn, openAmount, roomtype.roomTypeName \n" +
                            " FROM bookings\n" +
                            " INNER JOIN guests ON bookings.fk_guestID = guests.guestID\n" +
                            " INNER JOIN rooms on bookings.fk_roomID = rooms.roomID\n" +
                            " INNER JOIN roomtype ON rooms.fk_roomTypeID = roomtype.roomTypeID\n" +
                            " WHERE bookingID = " + bookingID);
            ResultSet rsInvoiceBookings = preparedStatementBooking.executeQuery();
            servicesInvoiceList = FXCollections.observableArrayList();
            while (rsInvoiceBookings.next()) {
                int roomID = rsInvoiceBookings.getInt("fk_roomID");
                String firstName = rsInvoiceBookings.getString("firstName");
                String lastName = rsInvoiceBookings.getString("lastName");
                Date arrival = rsInvoiceBookings.getDate("checkedIn");
                int ppn = rsInvoiceBookings.getInt("openAmount");
                String roomType = rsInvoiceBookings.getString("roomTypeName");
                Date date1 = arrival;
                Date date2 = today;
                long diff = date2.getTime() - date1.getTime();
                long lngNights = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                nights = new BigDecimal(lngNights).intValueExact();
                System.out.println(nights + " "+ ppn);
                servicesInvoiceList.add(new InvoiceTable(roomID,arrival.toString(),nights,"nights ",roomType,ppn,nights*ppn));

                lblRoomNr.setText(String.valueOf(roomID));
                lblGuestName.setText(firstName + " " + lastName);
                lblQuantNights.setText(String.valueOf(nights));
                total=total+(nights*ppn);
            }
            PreparedStatement preparedStatementServices =
                    Database.c.prepareStatement("SELECT servicesID, serviceType, serviceDate, \n" +
                            " coalesce(serv_movies.moviePrice, serv_wellness.wellnessPrice, serv_minibar.mbPrice) as pps, \n" +
                            " COUNT(coalesce(serv_movies.movieName, serv_wellness.wellnessName, serv_minibar.mbItem)) as quant,\n" +
                            " coalesce(serv_movies.movieName, serv_wellness.wellnessName, serv_minibar.mbItem) AS serviceName \n" +
                            " FROM services \n" +
                            " LEFT OUTER JOIN serv_movies ON (services.fk_serviceID = serv_movies.movieID AND services.serviceType = 'movie')\n" +
                            " LEFT OUTER JOIN serv_wellness ON (services.fk_serviceID = serv_wellness.wellnessID AND services.serviceType = 'wellness')\n" +
                            " LEFT OUTER JOIN serv_minibar ON (services.fk_serviceID = serv_minibar.mbID AND services.serviceType = 'minibar')\n" +
                            " WHERE fk_bookingID="+bookingID+"\n" +
                            " GROUP BY serv_minibar.mbItem, serv_movies.movieName, serv_wellness.wellnessName\n" +
                            " HAVING COUNT(*) > 0");
            ResultSet rsInvoiceServices = preparedStatementServices.executeQuery();

            while (rsInvoiceServices.next()){
                int i = rsInvoiceServices.getInt("servicesID");
                String serviceType = rsInvoiceServices.getString("serviceType");
                String serviceDate = rsInvoiceServices.getDate("serviceDate").toString();
                int servicePPS = rsInvoiceServices.getInt("pps");
                int serviceQuant = rsInvoiceServices.getInt("quant");
                String serviceName = rsInvoiceServices.getString("serviceName");
                servicesInvoiceList.add(new InvoiceTable(i,serviceDate,serviceQuant,serviceType,serviceName,servicePPS,servicePPS*serviceQuant));
                total = total+(servicePPS*serviceQuant);
            }
            lblTotal.setText(String.valueOf(total));
            lblTax.setText(String.valueOf(total-(total*0.9)));
        }catch (Exception e){
            System.out.println("getdata exption");
        }
    }


    public void initInvoiceTable()throws Exception{
        try {
            columnID.setCellValueFactory(new PropertyValueFactory<>("servicesID"));
            columnDate.setCellValueFactory(new PropertyValueFactory<>("serviceDate"));
            columnQuant.setCellValueFactory(new PropertyValueFactory<>("quant"));
            columnType.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
            columnName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
            columnPPS.setCellValueFactory(new PropertyValueFactory<>("pps"));
            columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

            tableInvoice.setItems(servicesInvoiceList);

        }catch (Exception e){
            System.out.println("Befüllen der Rechnungstabelle nicht möglich");
            e.printStackTrace();
        }
    }

    public void generateInvoice()throws Exception{
        try {
            btnCreateInvoice.setText(" ---- ");
        }catch (Exception e){
            System.out.println(" ..... ");
        }
    }
    @FXML
    private void clsoe() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

}
