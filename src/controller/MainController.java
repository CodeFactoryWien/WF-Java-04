package controller;

import database.Database;
import hotel.Booking;
import hotel.Guest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class MainController {
    @FXML
    private Button logoutButton;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab adminTab;

    static boolean userIsAdmin;

    private ObservableList<String> roomtypeslist;

    @FXML
    private TableView<Booking> tableOccupiedRooms;
    @FXML
    private TableColumn<Booking, Integer> columnRoomNr;
    @FXML
    private TableColumn<Booking, String> columnGuestName, columnArrival, columnDeparture;

    @FXML
    private TableView<Booking> tableBookings;
    @FXML
    private TableColumn<Booking, String> columnBookingGuest, columnBookingArrival, columnBookingDeparture, columnBookingStatus;

    @FXML
    private Button buttonStorno;
    @FXML
    private Button btnCheckIn;

    @FXML
    private CheckBox checkBoxShowAll;
    @FXML
    private DatePicker dateFrom, dateUntil, birthDate;

    public ObservableList<Booking> occupiedRooms, bookings;

    public static int bookingID;

    @FXML
    private ChoiceBox<String> roomType, roomType1;

    @FXML
    private CheckBox billingCheck;
    @FXML
    private TextField roomTypePrice, roomTypeSize, compName, firstName, lastName,
            address, zipCode, country, phoneNumber, email, passportNr;

    @FXML
    private ListView<Guest> listViewFoundGuest;

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
        fillRoomTypes();
        updateTableOccupied();
        updateTableBookings();
        initializeTableOccupied();
        initializeTableBookings();
        birthDate.setEditable(false);
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

        // Database search //
        lastName.setOnKeyTyped(e -> databaseSearch(lastName));
        firstName.setOnKeyTyped(e -> databaseSearch(firstName));
        address.setOnKeyTyped(e -> databaseSearch(address));
        zipCode.setOnKeyTyped(e -> databaseSearch(zipCode));
        country.setOnKeyTyped(e -> databaseSearch(country));
        phoneNumber.setOnKeyTyped(e -> databaseSearch(phoneNumber));
        email.setOnKeyTyped(e -> databaseSearch(email));
        passportNr.setOnKeyTyped(e -> databaseSearch(passportNr));
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
        if(billCheck) {
            Guest guest = new Guest(1,
                    lastName.getText(),
                    firstName.getText(),
                    birthDate.getValue(),
                    address.getText(),
                    Integer.parseInt(zipCode.getText()),
                    country.getText(),
                    phoneNumber.getText(),
                    email.getText(),
                    passportNr.getText());
            Database.insertNewGuest(guest);
        }
        else {
            Guest guest = new Guest(1,
                    lastName.getText(),
                    firstName.getText(),
                    birthDate.getValue(),
                    address.getText(),
                    Integer.parseInt(zipCode.getText()),
                    country.getText(),
                    phoneNumber.getText(),
                    email.getText(),
                    passportNr.getText());
            Database.insertNewCustomer(compName.getText(), guest);
        }
    }

    public void editGuestData(){
        if(billCheck) {
            Guest guest = new Guest(1,
                    lastName.getText(),
                    firstName.getText(),
                    birthDate.getValue(),
                    address.getText(),
                    Integer.parseInt(zipCode.getText()),
                    country.getText(),
                    phoneNumber.getText(),
                    email.getText(),
                    passportNr.getText());
            Database.insertNewGuest(guest);
        }
        else {
            Guest guest = new Guest(1,
                    lastName.getText(),
                    firstName.getText(),
                    birthDate.getValue(),
                    address.getText(),
                    Integer.parseInt(zipCode.getText()),
                    country.getText(),
                    phoneNumber.getText(),
                    email.getText(),
                    passportNr.getText());
            Database.insertNewCustomer(compName.getText(), guest);
        }
    }

    public void loadRoomTypePrice(){
       roomTypePrice.setText(Database.getRoomTypePrice(roomType1.getValue()));
    }

    public void sendNewRoomTypePrice(){
        System.out.println(roomType1.getValue());
        String input = roomTypePrice.getText().replaceAll("[€,]","");
        String inpEuro = input.substring(0, input.length() - 1);
        Database.setNewRoomTypePrice(roomType1.getValue(), inpEuro);
    }

    public void sendNewRoomCreation(){
        System.out.println(roomType.getValue());
        System.out.println(roomTypeSize.getText());
        Database.createNewRoom(roomType1.getValue(), roomTypeSize.getText());
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

    // Everytime a character is typed in a field //
    private void databaseSearch(TextField obj) {
        if (obj.getText() != null) {
            String userInput = obj.getText();
            String objID = obj.getId();

            ObservableList<Guest> O = FXCollections.observableArrayList();

            ResultSet R;
            try {
                PreparedStatement P = Database.c.prepareStatement("SELECT * FROM guests WHERE " + objID + " LIKE ?");
                P.setString(1, userInput + "%");
                R = P.executeQuery();

                while (R.next()) {
                    Guest G = new Guest(R);
                    O.add(G);
                }

            } catch (Exception e) {
                System.out.println("Error database search not possible");
            }

            listViewFoundGuest.setItems(O);
        }
    }

    // Fill roomtypes from database called from when choicebox shown //
    private void fillRoomTypes() {
        try {
            PreparedStatement preparedStatement =
                    Database.c.prepareStatement("SELECT roomTypeName FROM roomtype");

            ResultSet R = preparedStatement.executeQuery();

            roomtypeslist = FXCollections.observableArrayList();

            while (R.next()) {
                String typeName = R.getString("roomTypeName");
                roomtypeslist.add(typeName);
            }
        } catch (Exception e) {
            System.out.println("Error fillroomtypes not working");
        }
        roomType.setItems(roomtypeslist);
        roomType1.setItems(roomtypeslist);
    }
}
