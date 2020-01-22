package controller;

import javafx.fxml.FXML;
import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import hotel.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import sample.Main;

public class MainController {
    @FXML
    private Button logoutButton;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab adminTab;

    static boolean userIsAdmin;


    @FXML
    private TableView<Booking> tableOccupiedRooms;
    @FXML
    private TableColumn<Booking, Integer> columnRoomNr;
    @FXML
    private TableColumn<Booking, String> columnGuestName;
    @FXML
    private TableColumn<Booking, String> columnArrival;
    @FXML
    private TableColumn<Booking, String> columnDeparture;

    @FXML
    private TableView<Booking> tableBookings;
    @FXML
    private TableColumn<Booking, String> columnBookingGuest;
    @FXML
    private TableColumn<Booking, String> columnBookingArrival;
    @FXML
    private TableColumn<Booking, String> columnBookingDeparture;
    @FXML
    private TableColumn<Booking, String> columnBookingStatus;

    @FXML
    private Button buttonStorno;

    @FXML
    private CheckBox checkBoxShowAll;
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateUntil;

    public ObservableList<Booking> occupiedRooms;
    public ObservableList<Booking> bookings;
    public static int bookingID;

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
    private TextField compName;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private DatePicker birthDate;
    @FXML
    private TextField address;
    @FXML
    private TextField zipCode;
    @FXML
    private TextField country;
    @FXML
    private TextField phone;
    @FXML
    private TextField eMail;
    @FXML
    private TextField passportNumber;
    @FXML
    private Button addGuest;
    @FXML
    private Button editGuest;

    public void start() throws Exception {
        Stage S = new Stage();
        S.setTitle("Hotel Managing Software");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/main.fxml")));
        S.show();
    }

    @FXML
    public void initialize(){
        dateFrom.setValue(LocalDate.now());
        dateUntil.setValue(LocalDate.now().plusDays(7));
        updateTableOccupied();
        updateTableBookings();
        initializeTableOccupied();
        initializeTableBookings();
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

    // Details //
    public void call_detailsController() throws Exception {
        bookingID = tableOccupiedRooms.getSelectionModel().getSelectedItem().getBookingId();
        DetailsController C = new DetailsController();
        C.start();
    }
    public static int getBookingID(){
        return bookingID;
    }
    // Create Invoice //
    public void call_invoiceController()throws Exception{
        CreateInvoiceController C = new CreateInvoiceController();
        C.start();
    }




    private void initializeTableOccupied(){
        columnRoomNr.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        columnGuestName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnArrival.setCellValueFactory(new PropertyValueFactory<>("arrivalProperty"));
        columnDeparture.setCellValueFactory(new PropertyValueFactory<>("departureProperty"));
    }

    private void initializeTableBookings(){
        columnBookingGuest.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnBookingArrival.setCellValueFactory(new PropertyValueFactory<>("arrivalProperty"));
        columnBookingDeparture.setCellValueFactory(new PropertyValueFactory<>("departureProperty"));
        columnBookingStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void updateTableBookings(){
        if(checkBoxShowAll.isSelected()){
            bookings = FXCollections.observableArrayList(getAllBookingsFromTo(
                    new Date(Date.from(dateFrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()),
                    new Date(Date.from(dateUntil.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime())
            ));
        } else {
            bookings = FXCollections.observableArrayList(getOpenBookingsFromTo(
                    new Date(Date.from(dateFrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()),
                    new Date(Date.from(dateUntil.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime())
            ));
        }
        tableBookings.setItems(bookings);
        tableBookings.refresh();
    }

    public void updateTableOccupied(){
        Date today = new Date(new java.util.Date().getTime());
        occupiedRooms = FXCollections.observableArrayList(getBookingsFromTo(today));
        tableOccupiedRooms.setItems(occupiedRooms);
        tableOccupiedRooms.refresh();
    }

    public void toggleShowAll(){
        if(checkBoxShowAll.isSelected()){
            buttonStorno.setDisable(true);
        }else{
            buttonStorno.setDisable(false);
        }
        updateTableBookings();
    }

    private ArrayList<Booking> getOpenBookingsFromTo(Date start, Date end){
        ArrayList<Booking> bookings = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = Database.c.prepareStatement("SELECT * FROM (bookings INNER JOIN guests " +
                    "ON fk_guestID = guestID) INNER JOIN (rooms INNER JOIN roomtype ON roomTypeID = fk_roomTypeID) " +
                    "ON fk_roomID = roomID WHERE ((checkedIn IS NULL OR checkedIn = '0000-00-00') AND (bookingCanceled IS NULL OR bookingCanceled = '0000-00-00'))" +
                    " AND (bookingFrom >= ? AND bookingFrom <= ?)");
            preparedStatement.setDate(1, start);
            preparedStatement.setDate(2, end);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                bookings.add(new Booking(resultSet));
            }
        }catch(Exception e){
            System.err.println("problem requesting data from DB");
        }
        return bookings;
    }

    private ArrayList<Booking> getAllBookingsFromTo(Date start, Date end){
        ArrayList<Booking> bookings = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = Database.c.prepareStatement("SELECT * FROM (bookings INNER JOIN guests " +
                    "ON fk_guestID = guestID) INNER JOIN (rooms INNER JOIN roomtype ON roomTypeID = fk_roomTypeID) " +
                    "ON fk_roomID = roomID WHERE bookingFrom >= ? AND bookingFrom <= ?");
            preparedStatement.setDate(1, start);
            preparedStatement.setDate(2, end);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                bookings.add(new Booking(resultSet));
            }
        }catch(Exception e){
            System.err.println("problem requesting data from DB");
        }
        return bookings;
    }

    private ArrayList<Booking> getBookingsFromTo(Date today){
        ArrayList<Booking> bookings = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = Database.c.prepareStatement("SELECT * FROM (bookings INNER JOIN guests " +
                    "ON fk_guestID = guestID) INNER JOIN (rooms INNER JOIN roomtype ON roomTypeID = fk_roomTypeID) " +
                    "ON fk_roomID = roomID WHERE (bookingCanceled IS NULL OR bookingCanceled = '0000-00-00') AND(bookingFrom <= ? AND bookingUntil >= ?) AND " +
                    " checkedIn <= ?");
            preparedStatement.setDate(1, today);
            preparedStatement.setDate(2, today);
            preparedStatement.setDate(3, today);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                bookings.add(new Booking(resultSet));
            }
        }catch(Exception e){
            System.err.println("problem requesting data from DB");
        }
        return bookings;
    }

    public void checkOut() throws Exception {
        call_invoiceController();

        int bookingId = tableOccupiedRooms.getSelectionModel().getSelectedItem().getBookingId();
            try {
                /*
            Date today = new Date(new java.util.Date().getTime());
            PreparedStatement preparedStatement = Database.c.prepareStatement("UPDATE bookings SET bookingUntil = ?, " +
                    "bookingCanceled = ? WHERE bookingID = ?");
            preparedStatement.setDate(1, today);
            preparedStatement.setDate(2,today);
            preparedStatement.setInt(3, bookingId);
            preparedStatement.executeUpdate();
                 */
        }catch (Exception e){
            System.err.println("Problem updating SQL table");
        }
        updateTableOccupied();
    }

    public void cancelBooking(){
        int bookingId = tableBookings.getSelectionModel().getSelectedItem().getBookingId();
        try {
            Date today = new Date(new java.util.Date().getTime());
            PreparedStatement preparedStatement = Database.c.prepareStatement("UPDATE bookings SET bookingUntil = ?, " +
                    "bookingCanceled = ? WHERE bookingID = ?");
            preparedStatement.setDate(1, today);
            preparedStatement.setDate(2,today);
            preparedStatement.setInt(3, bookingId);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.err.println("Problem updating SQL table");
        }
        updateTableBookings();
    }

    public void checkIn(){
        int bookingId = tableBookings.getSelectionModel().getSelectedItem().getBookingId();
        try {
            Date today = new Date(new java.util.Date().getTime());
            PreparedStatement preparedStatement = Database.c.prepareStatement("UPDATE bookings SET checkedIn = ?, bookingFrom = ? "+
                    " WHERE bookingID = ?");
            preparedStatement.setDate(1,today);
            preparedStatement.setDate(2,today);
            preparedStatement.setInt(3,bookingId);
            preparedStatement.executeUpdate();

            updateTableOccupied();

        }catch (Exception e){
            System.out.println("Kann nicht einchecken.");
        }
        updateTableBookings();
    }
    public void sendRoomData () throws Exception {
        System.out.println("No room creation possible yet.");
    }

    public void sendGuestData(){
        Guest guest = new Guest(1, lastName.getText(), firstName.getText(), birthDate.getValue(), address.getText(),
                Integer.parseInt(zipCode.getText()), country.getText(), phone.getText(), eMail.getText(), passportNumber.getText());
        Database.insertNewGuest(guest);
    }
}
