package controller;

import javafx.fxml.FXML;
import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
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
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateUntil;

    public ObservableList<Booking> occupiedRooms;
    public ObservableList<Booking> bookings;

    public void start() throws Exception {
        Stage S = new Stage();
        S.setTitle("hotel Managing Software");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/main.fxml")));
        S.show();
    }

    @FXML
    public void initialize(){
        Date today = new Date(new java.util.Date().getTime());
        occupiedRooms = FXCollections.observableArrayList(getBookingsFromTo(today));
        dateFrom.setValue(LocalDate.now());
        dateUntil.setValue(LocalDate.now().plusDays(7));
        bookings = FXCollections.observableArrayList(getBookingsFromTo(
                new Date(Date.from(dateFrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()),
                new Date(Date.from(dateUntil.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime())
        ));
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

    // Test Details //
    public void call_detailsController() throws Exception {
        DetailsController C = new DetailsController();
        C.start();
    }

    private void initializeTableOccupied(){
        columnRoomNr.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        columnGuestName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnArrival.setCellValueFactory(new PropertyValueFactory<>("arrivalProperty"));
        columnDeparture.setCellValueFactory(new PropertyValueFactory<>("departureProperty"));

        tableOccupiedRooms.setItems(occupiedRooms);
    }

    private void initializeTableBookings(){
        columnBookingGuest.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnBookingArrival.setCellValueFactory(new PropertyValueFactory<>("arrivalProperty"));
        columnBookingDeparture.setCellValueFactory(new PropertyValueFactory<>("departureProperty"));

        tableBookings.setItems(bookings);
    }

    public void updateTableBookings(){
        bookings = FXCollections.observableArrayList(getBookingsFromTo(
                new Date(Date.from(dateFrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()),
                new Date(Date.from(dateUntil.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime())
        ));
        tableBookings.refresh();
    }

    public void updateTableOccupied(){
        Date today = new Date(new java.util.Date().getTime());
        occupiedRooms = FXCollections.observableArrayList(getBookingsFromTo(today,today));
        tableOccupiedRooms.refresh();
    }

    private ArrayList<Booking> getBookingsFromTo(Date start, Date end){
        ArrayList<Booking> bookings = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = Database.c.prepareStatement("SELECT * FROM (bookings INNER JOIN guests " +
                    "ON fk_guestID = guestID) INNER JOIN (rooms INNER JOIN roomtype ON roomTypeID = fk_roomTypeID) " +
                    "ON fk_roomID = roomID WHERE (bookingCanceled IS NULL) AND((? >= bookingFrom AND ? <= bookingUntil)OR " +
                    "(? >= bookingFrom AND ? <= bookingUntil) OR (? <= bookingFrom AND ? >= bookingUntil))");
            preparedStatement.setDate(1, start);
            preparedStatement.setDate(2, start);
            preparedStatement.setDate(3, end);
            preparedStatement.setDate(4, end);
            preparedStatement.setDate(5, start);
            preparedStatement.setDate(6, end);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Guest tempGuest = new Guest(resultSet.getInt("guestID"), resultSet.getString("firstname"),
                        resultSet.getString("lastName"), resultSet.getDate("birthDate").toLocalDate(),
                        resultSet.getString("address"),resultSet.getInt("zipCode"),
                        resultSet.getString("country"), resultSet.getString("phoneNumber"),
                        resultSet.getString("email"), resultSet.getString("passportNr"));
                Room tempRoom = new Room(resultSet.getInt("roomID"),resultSet.getString("roomTypeName"),
                        resultSet.getDouble("roomTypePrice"), resultSet.getInt("roomTypeCapacity"),
                        resultSet.getDouble("roomSize"),"",
                        resultSet.getString("roomTypeFacilities"));
                Booking tempBooking =new Booking(resultSet.getInt("bookingID"), tempGuest, tempRoom,
                        resultSet.getDate("bookingFrom"), resultSet.getDate("bookingUntil"));

                bookings.add(tempBooking);
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
                    "ON fk_roomID = roomID WHERE (bookingCanceled IS NULL) AND(bookingFrom <= ? AND bookingUntil >= ?)");
            preparedStatement.setDate(1, today);
            preparedStatement.setDate(2, today);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Guest tempGuest = new Guest(resultSet.getInt("guestID"), resultSet.getString("firstname"),
                        resultSet.getString("lastName"), resultSet.getDate("birthDate").toLocalDate(),
                        resultSet.getString("address"),resultSet.getInt("zipCode"),
                        resultSet.getString("country"), resultSet.getString("phoneNumber"),
                        resultSet.getString("email"), resultSet.getString("passportNr"));
                Room tempRoom = new Room(resultSet.getInt("roomID"),resultSet.getString("roomTypeName"),
                        resultSet.getDouble("roomTypePrice"), resultSet.getInt("roomTypeCapacity"),
                        resultSet.getDouble("roomSize"),"",
                        resultSet.getString("roomTypeFacilities"));
                Booking tempBooking =new Booking(resultSet.getInt("bookingID"), tempGuest, tempRoom,
                        resultSet.getDate("bookingFrom"), resultSet.getDate("bookingUntil"));

                bookings.add(tempBooking);
            }
        }catch(Exception e){
            System.err.println("problem requesting data from DB");
        }
        return bookings;
    }
}
