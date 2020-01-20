package controller;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import hotel.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;


public class MainController {

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

    public ObservableList<Booking> bookings = FXCollections.observableArrayList(getCurrentBookings());

    public void start() throws Exception {
        Stage S = new Stage();
        S.setTitle("hotel Managing Software");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/main.fxml")));
        S.show();
    }

    @FXML
    public void initialize(){
        initializeTable();
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

    private void initializeTable(){
        columnRoomNr.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        columnGuestName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnArrival.setCellValueFactory(new PropertyValueFactory<>("arrivalProperty"));
        columnDeparture.setCellValueFactory(new PropertyValueFactory<>("departureProperty"));

        tableOccupiedRooms.setItems(bookings);
    }

    private ArrayList<Booking> getCurrentBookings(){
        ArrayList<Booking> bookings = new ArrayList<>();
        Date today = new Date(new java.util.Date().getTime());
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
