package controller;

import javafx.fxml.FXML;
import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import hotel.*;

import java.io.PrintWriter;
import java.io.StringWriter;
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
    private Button btnCheckIn;

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
    private ChoiceBox roomType1;
    @FXML
    private TextField roomTypePrice;
    @FXML
    private TextField roomTypeSize;
    @FXML
    private CheckBox billingCheck;
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
    boolean billCheck;

    private static MainController controller;

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
        roomType1.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            try {
                loadRoomTypePrice();
            } catch (Exception e) {
                showError(e);
            }
        });
        billingCheck.selectedProperty().addListener((observableValue, s, t1) -> {
            try {
                billingCheck();
            } catch (Exception e) {
                showError(e);
            }
        });
        controller = this;
    }


    public void logout() throws Exception {
        MainController.userIsAdmin = false;
        LoginController L = new LoginController();
        L.start();
        Stage S = (Stage) logoutButton.getScene().getWindow();
        S.close();
    }

    void showError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error alert");
        alert.setHeaderText(e.getMessage());
        VBox dialogPaneContent = new VBox();
        Label label = new Label("Stack Trace:");
        String stackTrace = this.getStackTrace(e);
        TextArea textArea = new TextArea();
        textArea.setText(stackTrace);
        dialogPaneContent.getChildren().addAll(label, textArea);
        alert.getDialogPane().setContent(dialogPaneContent);
        alert.showAndWait();
    }

    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return  sw.toString();
    }

    public void setAdminStatus(){
        userIsAdmin = true;
    }

    // Create new Booking //
    public void call_createBookingController(){
        try {
            CreateBookingController C = new CreateBookingController();
            C.start();
        } catch (Exception e) {
            showError(e);
        }
    }

    // Details //
    public void call_detailsController(){
        try {
        bookingID = tableOccupiedRooms.getSelectionModel().getSelectedItem().getBookingId();
        DetailsController C = new DetailsController();
        C.start();
        } catch (Exception e) {
            showError(e);
        }
    }
    // Create Invoice //
    public void call_invoiceController(){
        try {
        bookingID = tableOccupiedRooms.getSelectionModel().getSelectedItem().getBookingId();
        CreateInvoiceController C = new CreateInvoiceController();
        C.start();
        } catch (Exception e) {
            showError(e);
        }
    }
    public static int getBookingID() {
        return bookingID;
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

    public static void updateTables(){
        controller.updateTableBookings();
        controller.updateTableOccupied();
    }

    public void toggleShowAll(){
        if(checkBoxShowAll.isSelected()){
            buttonStorno.setDisable(true);
            btnCheckIn.setDisable(true);
        }else{
            buttonStorno.setDisable(false);
            btnCheckIn.setDisable(false);
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

    public void cancelBooking(){
        int bookingId = tableBookings.getSelectionModel().getSelectedItem().getBookingId();
        try {
            Date today = new Date(new java.util.Date().getTime());
            PreparedStatement preparedStatement = Database.c.prepareStatement("UPDATE bookings SET bookingUntil = ?, " +
                    "bookingCanceled = ?, fk_roomID = 0 WHERE bookingID = ?");
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

    public void sendGuestData(){
        Guest guest = new Guest(1, lastName.getText(), firstName.getText(), birthDate.getValue(), address.getText(),
                Integer.parseInt(zipCode.getText()), country.getText(), phone.getText(), eMail.getText(), passportNumber.getText());
        Database.insertNewGuest(guest);
    }

    public void loadRoomTypePrice(){
        try {
            roomTypePrice.setText(Double.toString(Database.getRoomTypePrice(roomType1.getValue().toString())));
        }catch(NullPointerException e){
            System.out.println("No price found for Roomtype");
        }
    }

    public void sendNewRoomTypePrice(){
        System.out.println(roomType1.getValue().toString());
        System.out.println(roomTypePrice.getText());
        Database.setNewRoomTypePrice(roomType1.getValue().toString(), roomTypePrice.getText());
    }

    public void sendNewRoomCreation(){
        System.out.println(roomType.getValue().toString());
        System.out.println(roomTypeSize.getText());
        Database.createNewRoom(roomType1.getValue().toString(), roomTypeSize.getText());
    }

    public void billingCheck(){
        if(compName.isDisabled()){
            compName.setDisable(false);
            billCheck = false;
        } else {
            compName.setDisable(true);
            billCheck = true;
        }
    }
}
