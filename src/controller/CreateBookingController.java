package controller;

import hotel.Guest;
import database.Database;
import hotel.Room;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CreateBookingController {

    private Stage bookStage;

    // Important fields //
    private int totalcount, bookedroomscount, freeroomcount;
    private int selectedRoomPrice;
    private LocalDate checkInDate, checkOutDate;
    private ObservableList<String> roomtypeslist;
    private Guest selected_item;
    private boolean nonullfound;

    // Room and date fields //
    @FXML
    private ChoiceBox<String> roomType;
    @FXML
    private DatePicker checkIn, checkOut;
    @FXML
    private Label freeRooms, pricePerDay, totalPrice;

    // Vbox of guest fields //
    @FXML
    private VBox column2;

    // Guest fields//
    @FXML
    private TextField lastName, firstName, address, zipCode, country, phoneNumber, email, passportNr;
    @FXML
    private DatePicker birthDate;
    @FXML
    private ListView listViewFoundGuest;

    // Cancel and booking button //
    @FXML
    private Button cancel, booking;

    // Init method //
    public void initialize() {
        // Database search //
        lastName.setOnKeyTyped(e -> databaseSearch(lastName));
        firstName.setOnKeyTyped(e -> databaseSearch(firstName));
        address.setOnKeyTyped(e -> databaseSearch(address));
        zipCode.setOnKeyTyped(e -> databaseSearch(zipCode));
        country.setOnKeyTyped(e -> databaseSearch(country));
        phoneNumber.setOnKeyTyped(e -> databaseSearch(phoneNumber));
        email.setOnKeyTyped(e -> databaseSearch(email));
        passportNr.setOnKeyTyped(e -> databaseSearch(passportNr));

        // CheckInput call on key typed //
        zipCode.setOnKeyTyped(e -> checkInputInteger(zipCode));
        phoneNumber.setOnKeyTyped(e -> checkInputInteger(phoneNumber));
        passportNr.setOnKeyTyped(e -> checkInputInteger(passportNr));

        // Listview select call //
        listViewFoundGuest.setOnMouseClicked(e -> getSelectedObj());

        // Booking call //
        booking.setOnMouseClicked(e -> addBooking());

        // Fill choicebox on init with roomtypes from database //
        fillRoomTypes();

        // Listener for choicebox -> call bookaroom //
        roomType.getSelectionModel().selectedItemProperty().addListener((observableValue, d, t1) -> {
                bookaroom();
        });

        // Datepicker call -> bookaroom //
        checkIn.setOnAction(e -> bookaroom());
        checkOut.setOnAction(e -> bookaroom());

        // Datepicker editable set false //
        checkIn.setEditable(false);
        checkOut.setEditable(false);

        // close method on button click //
        cancel.setOnMouseClicked(e -> close());
    }

    // Start controller method //
    public void start() throws Exception {
        bookStage = new Stage();
        bookStage.setTitle("hotel Managing Software");
        bookStage.setScene(FXMLLoader.load(getClass().getResource("/view/createBooking.fxml")));
        bookStage.show();
    }

    // Method is everytime called when choicebox, checkIn or checkOut is fired //
    private void bookaroom() {
            // All fields in column1 must be filled //
            if (roomType.getValue() != null) {
                // Total room count of selected room type //
                totalSelectedRoomCount();
                // Price per day of selected roomtype //
                fillPricePerDay();
            }
                if (checkIn.getValue() != null && checkOut.getValue() != null) {
                    // CheckIn or CheckOut cannot be in the past //
                    if (checkIn.getValue().isBefore(LocalDate.now()) || checkOut.getValue().isBefore(LocalDate.now())) {
                        column2.setDisable(true);
                        totalPrice.setText("/////");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information!");
                        alert.setHeaderText("CheckIn or CheckOut date cannot be set in the past");
                        alert.showAndWait();
                    } else {
                        // CheckIn must be set before CheckOut //
                        if (checkIn.getValue().isBefore(checkOut.getValue())) {
                            column2.setDisable(false);
                        } else {
                            column2.setDisable(true);
                            totalPrice.setText("/////");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information!");
                            alert.setHeaderText("CheckIn must be set before CheckOut");
                            alert.showAndWait();
                        }

                        try {
                            if (checkIn.getValue() != null) {
                                checkInDate = checkIn.getValue();
                            }
                            if (checkOut.getValue() != null) {
                                checkOutDate = checkOut.getValue();
                            }

                            int days = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);

                            if (column2.isDisable()) {
                                totalPrice.setText("/////");
                            } else {
                                totalPrice.setText(String.valueOf(selectedRoomPrice * days));
                            }
                        } catch (Exception ignored) {
                        }
                    }
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
    }

    // Fill total count of free rooms from database based on selected roomtype//
    private void totalSelectedRoomCount() {

        try {
            PreparedStatement P1 =
                    Database.c.prepareStatement("SELECT COUNT(*) AS roomCount FROM rooms " +
                            "JOIN roomtype ON fk_roomTypeID = roomTypeID " +
                            "where roomTypeName = ?");
            P1.setString(1, roomType.getValue());

            ResultSet R1 = P1.executeQuery();

            if (R1.first()) {
                totalcount = R1.getInt("roomCount");
            }

            PreparedStatement P2 =
                    Database.c.prepareStatement("SELECT COUNT(*) AS bookedRoomsCount FROM rooms " +
                            "JOIN roomtype ON fk_roomTypeID = roomTypeID " +
                            "JOIN bookings ON fk_roomID = roomID " +
                            "WHERE roomTypeName = ? " +
                            "AND bookingUntil >= ? " +
                            "AND roomID >= 0 ");
            P2.setString(1, roomType.getValue());
            P2.setDate(2, Date.valueOf(LocalDate.now()));

            ResultSet R2 = P2.executeQuery();

            if (R2.first()) {
                bookedroomscount = R2.getInt("bookedRoomsCount");
            }

        } catch (Exception e) {
            System.out.println("Error room count call not possible");
        }
        freeroomcount = totalcount-bookedroomscount;
        freeRooms.setText(String.valueOf(freeroomcount));
    }

    // Fill roomPrice from selected roomType from database //
    private void fillPricePerDay() {

        String selectedRoomType = roomType.getSelectionModel().getSelectedItem();

        try {
            PreparedStatement preparedStatement =
                    Database.c.prepareStatement("Select roomTypePrice FROM roomType WHERE roomTypeName = ?");
            preparedStatement.setString(1, selectedRoomType);

            ResultSet R = preparedStatement.executeQuery();

            if (R.first()) {
                selectedRoomPrice = (R.getInt("roomTypePrice")/100);
            }
        } catch (Exception e) {
            System.out.println("Error fillpriceperday not working");
        }

        pricePerDay.setText(String.valueOf(selectedRoomPrice));
    }

    // Close Window(Stage) //
    private void close() {
        bookStage = (Stage) cancel.getScene().getWindow();
        bookStage.close();
    }

    // New guest (not found in database) create //
    private void sendGuestDataToDatabase() {
        try {
            PreparedStatement P = Database.c.prepareStatement("INSERT INTO guests (firstName, lastName, birthDate, " +
                    "address, zipCode, country, phoneNumber, email, passportNr, fk_customerID)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, 7)");
            P.setString(1, firstName.getText());
            P.setString(2, lastName.getText());
            P.setDate(3, Date.valueOf(birthDate.getValue()));
            P.setString(4, address.getText());
            P.setInt(5, Integer.parseInt(zipCode.getText()));
            P.setString(6, country.getText());
            P.setString(7, phoneNumber.getText());
            P.setString(8, email.getText());
            P.setString(9, passportNr.getText());


            P.executeUpdate();
        } catch (Exception e) {
            System.out.println("New guest create function error");
        }
    }

    // Everytime a character is typed in a field in column2 the method is called //
    private void databaseSearch(TextField obj) {
        if (selected_item != null) {
            selected_item = null;
        }
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

    // Get data from the selected entry //
    private void getSelectedObj() {
        selected_item = (Guest) listViewFoundGuest.getSelectionModel().getSelectedItem();

        try {
            PreparedStatement P = Database.c.prepareStatement("SELECT * FROM guests WHERE guests.guestID = ?");
           P.setInt(1, selected_item.getId());

            ResultSet R = P.executeQuery();

            if (R.first()) {
                firstName.setText(R.getString("firstName"));
                lastName.setText(R.getString("lastName"));
                birthDate.setValue(R.getDate("birthDate").toLocalDate());
                address.setText(R.getString("address"));
                zipCode.setText(String.valueOf(R.getInt("zipCode")));
                country.setText(R.getString("country"));
                phoneNumber.setText(R.getString("phoneNumber"));
                email.setText(R.getString("email"));
                passportNr.setText(R.getString("passportNr"));
            }
        } catch (Exception e) {
            System.out.println("selected entry cannot be obtained");
        }
    }

    // for ZIPCode, phonenumber and passport field so only numbers can be typed in //
    private void checkInputInteger(TextField obj) {
        ////
        obj.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[0-9P+]*")){
                obj.setText(oldValue);
            }
        });
    }

    // Loop over vbox (column2) and if any child is null or empty alert is fired //
    private void vboxFieldLoop() {
        nonullfound = true;
        for (Node child : column2.getChildren()) {
            if (child instanceof TextField) {
                TextField T = (TextField) child;
                if (T.getText().equals("")) {
                    nonullfound = false;
                    break;
                }
            } else if (child instanceof DatePicker) {
                DatePicker D = (DatePicker) child;
                if (D.getValue() == null) {
                    nonullfound = false;
                    break;
                }
            }
        }
    }

    // Add booking function //
    private void addBooking() {
        vboxFieldLoop();
        // if vboxFieldLoop found a null value in fields //
        if (nonullfound == false) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information!");
            alert.setHeaderText("All fields are required");
            alert.showAndWait();
            // If the user try to brake the code and fill all fields and after that set the date to past //
        } else if (column2.isDisabled() == true) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information!");
            alert.setHeaderText("Booking is not possible ;-)");
            alert.showAndWait();
            // both alerts are not fired //
        } else {
            if (nonullfound == true) {
                if (freeroomcount != 0) {
                    // If user picked ID is found in database //
                    int guestID1 = 0;
                    if (selected_item != null) {
                        try {
                            PreparedStatement P = Database.c.prepareStatement("SELECT guestID FROM guests WHERE " +
                                    "guestID = " + selected_item.getId());
                            ResultSet R = P.executeQuery();

                            if (R.first()) {
                                guestID1 = R.getInt("guestID");
                            }

                            if (guestID1 != selected_item.getId()) {
                                sendGuestDataToDatabase();
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    // If user is not picked method called //
                    if (selected_item == null) {
                        sendGuestDataToDatabase();
                    }

                    // To get the correct ID from the created guest or the selected guest so it can be added to booking //
                    String guestID2 = null;
                    try {
                        PreparedStatement P = Database.c.prepareStatement("SELECT guestID FROM guests " +
                                "WHERE guests.firstName = ? AND guests.lastName = ? AND guests.birthDate = ? " +
                                "AND guests.address = ?");
                        P.setString(1, firstName.getText());
                        P.setString(2, lastName.getText());
                        P.setDate(3, Date.valueOf(birthDate.getValue()));
                        P.setString(4, address.getText());

                        ResultSet R = P.executeQuery();

                        if (R.first()) {
                            guestID2 = R.getString("guestID");
                        }
                    } catch (Exception e) {
                        System.out.println("guest add not working");
                    }

                    Room R = Database.firstFreeRoom(roomType.getValue(), checkIn.getValue(), checkOut.getValue());

                    // Create booking //
                    try {
                        PreparedStatement preparedStatement =
                                Database.c.prepareStatement("INSERT INTO bookings (fk_roomID, fk_guestID, fk_customerID, " +
                                        "openAmount, bookingFrom, bookingUntil, bookingCanceled, checkedIn) " +
                                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
                        assert R != null;
                        assert guestID2 != null;
                        preparedStatement.setInt(1, R.getId());
                        preparedStatement.setInt(2, Integer.parseInt(guestID2));
                        preparedStatement.setInt(3, 5);
                        preparedStatement.setInt(4, selectedRoomPrice);
                        preparedStatement.setDate(5, Date.valueOf(checkIn.getValue()));
                        preparedStatement.setDate(6, Date.valueOf(checkOut.getValue()));
                        preparedStatement.setDate(7, null);
                        preparedStatement.setDate(8, null);

                        preparedStatement.executeUpdate();
                        MainController.updateTables();
                    } catch (SQLException e) {
                        System.out.println("Error booking cannot be created");
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information!");
                    alert.setHeaderText("Booking created!");
                    alert.showAndWait();
                    close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information!");
                    alert.setHeaderText("No free room for your selected roomtype available!");
                    alert.showAndWait();
                }
            }
        }
    }
}
