package controller;

import database.Database;
import hotel.services.Minibar;
import hotel.services.Movie;
import hotel.services.ServicesList;
import hotel.services.Wellness;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class DetailsController{
    private Stage S;
    private int bookingID  ;
    private int fixPriceMovie, fixPriceWellness, fixPriceMinibar =0;
    private int serviceIdMovie, serviceIdWellness,serviceIdMinibar = 0;
    private int servicesID = 0;
    private boolean showMovie = true;
    private boolean showWellness = true;
    private boolean showMinibar = true;
    private ArrayList<Boolean> showArr = new ArrayList<>();

    @FXML
    private Label lblRoomNr, lblGuestName, lblDepature, lblAmount;
    @FXML
    private ChoiceBox choiceMovie,choiceWellness,choiceMinibar;
    @FXML
    private Button btn_add_movie, btn_add_wellness, btn_add_minibar, btn_deleteService ,btn_checkout;
    @FXML
    private CheckBox cb_movie, cb_wellness, cb_minibar;
    @FXML
    private ListView serviceListView;

    public DetailsController(){}

    void start() throws Exception {
        Stage S = new Stage();
        S.setTitle("Showing Details for Selection");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/details.fxml")));
        S.setResizable(false);
        S.show();
    }

    @FXML
    public void initialize(){
        try {
            bookingID = MainController.getBookingID();
            System.out.println("bookingID in initialize " + bookingID);
            populateListService();

            populateMoviesChoice();
            populateWellnessChoice();
            populateMinibarChoice();

            setServiceAmount();
            setGuestDetails();

            showArr.add(showMovie);
            showArr.add(showWellness);
            showArr.add(showMinibar);
            // init Buttons
            btn_add_movie.setOnAction(event -> {
                try {
                    addService("movie",serviceIdMovie,fixPriceMovie);
                    populateListService();
                    setServiceAmount();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            btn_add_wellness.setOnAction(event -> {
                try {
                    addService("wellness",serviceIdWellness,fixPriceWellness);
                    populateListService();
                    setServiceAmount();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            btn_add_minibar.setOnAction(event -> {
                try {
                    addService("minibar",serviceIdMinibar,fixPriceMinibar);
                    populateListService();
                    setServiceAmount();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            btn_deleteService.setOnAction(event -> {
                try {
                    deleteService();
                    populateListService();
                    setServiceAmount();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            });
            btn_checkout.setOnAction(event -> {
                try {
                    System.out.println("checkout");
                    call_invoiceController();
                    Stage stage = (Stage) btn_checkout.getScene().getWindow();
                    stage.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            //init Choiceboxes
            cb_movie.selectedProperty().addListener((observable, oldValue, newValue) -> showMovie = !newValue );
            cb_wellness.selectedProperty().addListener((observable, oldValue, newValue) -> showWellness = !newValue);
            cb_minibar.selectedProperty().addListener((observable, oldValue, newValue) -> showMinibar = !newValue);

        }catch (Exception e){
            System.err.println("Exception in initialize ");
        }
    }
    // add Service to a booking
    public void addService(String serviceType, int serviceID, int fixPrice)throws Exception{
        if (serviceID!=0 && fixPrice!=0) {
            java.util.Date date=new java.util.Date();
            java.sql.Date sqlDate=new java.sql.Date(date.getTime());
            System.out.println(bookingID+" "+ serviceID+" "+fixPrice);
            PreparedStatement preparedStatement =
                    Database.c.prepareStatement("INSERT INTO services(fk_bookingID, serviceType, serviceDate, fk_serviceID, fixPrice) \n" +
                            "VALUES ('" + bookingID + "', '"+serviceType+"', '" + sqlDate +"', '" + serviceID + "','"+fixPrice+"' )");
            preparedStatement.executeUpdate();

            fixPrice=0;
            btn_add_minibar.setText("select item to add");
            btn_add_wellness.setText("select item to add");
            btn_add_movie.setText("select item to add");
        }
    }
    // delete a service from a booking
    public void deleteService()throws Exception{
        if (servicesID!=0){
            PreparedStatement preparedStatement =
                    Database.c.prepareStatement("DELETE FROM services WHERE servicesID = " + servicesID );
            preparedStatement.executeUpdate();
            btn_deleteService.setText("select item for deleting");
        }
    }
    // calculate and show the amount of all services
    public void setServiceAmount()throws Exception{
        System.out.println("bookingID in setServiceAmount" + bookingID);

        PreparedStatement preparedStatement =
                Database.c.prepareStatement("SELECT SUM(fixPrice) \n" +
                        " FROM services \n" +
                        " WHERE fk_bookingID="+bookingID);
        ResultSet rsPrice = preparedStatement.executeQuery();
        int index=1;
        while (rsPrice.next()){
            int servicePrice = rsPrice.getInt(index);
            System.out.println(servicePrice);
            lblAmount.setText(String.valueOf((double)servicePrice/100) + " €");
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
            lblDepature.setText(String.valueOf(bookingUntil));
        }
    }

    public void populateListService() throws  Exception {
        System.out.println("bookingID in populate List: "+bookingID);
        String tableS;
        String tableMovie="serv_movies.movieName";
        String tableWellness="serv_wellness.wellnessName";
        String tableMinibar="serv_minibar.mbItem";
        tableS = tableMovie + tableWellness + tableMinibar;
        PreparedStatement preparedStatement =
                Database.c.prepareStatement("SELECT servicesID, serviceType, serviceDate, coalesce(serv_movies.movieName, serv_wellness.wellnessName, serv_minibar.mbItem) as serviceName, fk_serviceID \n" +
                        " FROM services  \n" +
                        " LEFT OUTER JOIN serv_movies ON (services.fk_serviceID = serv_movies.movieID AND services.serviceType = 'movie') \n" +
                        " LEFT OUTER JOIN serv_wellness ON (services.fk_serviceID = serv_wellness.wellnessID AND services.serviceType = 'wellness') \n" +
                        " LEFT OUTER JOIN serv_minibar ON (services.fk_serviceID = serv_minibar.mbID AND services.serviceType = 'minibar') \n" +
                        " WHERE fk_bookingID="+bookingID);
        ResultSet rsServicesList = preparedStatement.executeQuery();

        ObservableList<ServicesList> servicesList = FXCollections.observableArrayList();
        while (rsServicesList.next()){
            int i = rsServicesList.getInt("servicesID");
            String serviceType = rsServicesList.getString("serviceType");
            Date serviceDate = rsServicesList.getDate("serviceDate");
            String serviceName = rsServicesList.getString("serviceName");
            servicesList.add(new ServicesList(i,bookingID,serviceType,serviceDate,serviceName));
            System.out.println(i);
        }
        serviceListView.setItems(servicesList);
        serviceListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    btn_deleteService.setText("delete " + servicesList.get(newValue.intValue()).getServicesID()+ " "+
                            servicesList.get(newValue.intValue()).getServiceType() + " " +
                            servicesList.get(newValue.intValue()).getServiceName() + " from " +
                            servicesList.get(newValue.intValue()).getServiceDate());
                    servicesID = servicesList.get(newValue.intValue()).getServicesID();
                }catch (Exception e) {
                    System.out.println("Error in eventhandler servicelistview");
                }
            }
        });
    }

    public void populateMoviesChoice() throws Exception {
        PreparedStatement preparedStatement =
        Database.c.prepareStatement("SELECT * FROM serv_movies");
        ResultSet rsMovies = preparedStatement.executeQuery();
        ObservableList<Movie> movieList = FXCollections.observableArrayList();

        while (rsMovies.next()) {
            int i = rsMovies.getInt("movieID");
            String movieName = rsMovies.getString("movieName");
            String movieDescription = rsMovies.getString("movieDescription");
            int moviePrice = rsMovies.getInt("moviePrice");
            int movieSeen = rsMovies.getInt("movieSeen");
            movieList.add(new Movie(i,movieName, movieDescription, moviePrice, movieSeen));
        }
        choiceMovie.setItems(movieList);
        choiceMovie.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    btn_add_movie.setText("add movie "  + movieList.get(newValue.intValue()).getMovieID());
                    serviceIdMovie = movieList.get(newValue.intValue()).getMovieID();
                    fixPriceMovie = movieList.get(newValue.intValue()).getMoviePrice();
                } catch (Exception e) {
                    System.out.println("Error");
                }
            }
        });
        choiceMovie.getSelectionModel().selectFirst();
    }

    public void populateWellnessChoice() throws Exception {
        PreparedStatement preparedStatement =
                Database.c.prepareStatement("SELECT * FROM serv_wellness");
        ResultSet rsWellness = preparedStatement.executeQuery();
        ObservableList<Wellness> wellnessList = FXCollections.observableArrayList();

        while (rsWellness.next()) {
            int wellnessID = rsWellness.getInt("wellnessID");
            String wellnessName = rsWellness.getString("wellnessName");
            String wellnessDescription = rsWellness.getString("wellnessDescription");
            int wellnessPrice = rsWellness.getInt("wellnessPrice");
            wellnessList.add(new Wellness(wellnessID, wellnessName, wellnessDescription, wellnessPrice));
        }
        choiceWellness.setItems(wellnessList);
        // changelistener
        choiceWellness.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    btn_add_wellness.setText("add wellness service: " + wellnessList.get(newValue.intValue()).getWellnessID());
                    serviceIdWellness=wellnessList.get(newValue.intValue()).getWellnessID();
                    fixPriceWellness=wellnessList.get(newValue.intValue()).getWellnessPrice();
                } catch (Exception e) {
                    System.out.println("Error changelistener wellnesslist");
                }
            }
        });
        choiceWellness.getSelectionModel().selectFirst();
    }

    public  void populateMinibarChoice()throws Exception{
        PreparedStatement ps =
                Database.c.prepareStatement("SELECT * FROM serv_minibar");
        ResultSet rsMinibar = ps.executeQuery();
        ObservableList<Minibar> minibarList = FXCollections.observableArrayList();

        while (rsMinibar.next()){
            int mbID = rsMinibar.getInt("mbID");
            String mbItem = rsMinibar.getString("mbItem");
            String mbItemDesc = rsMinibar.getString("mbItemDescription");
            int mbPrice = rsMinibar.getInt("mbPrice");
            minibarList.add(new Minibar(mbID,mbItem,mbItemDesc,mbPrice));
        }
        choiceMinibar.setItems(minibarList);
        choiceMinibar.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    btn_add_minibar.setText("add minibar Item " + minibarList.get(newValue.intValue()).getMbID());
                    serviceIdMinibar=minibarList.get(newValue.intValue()).getMbID();
                    fixPriceMinibar=minibarList.get(newValue.intValue()).getMbPrice();

                }catch (Exception e){
                    System.out.println("Error changelistener minibarlist");
                }
            }
        });
        choiceMinibar.getSelectionModel().selectFirst();
    }

    private void call_invoiceController()  throws Exception {
        try {
            CreateInvoiceController C = new CreateInvoiceController();
            C.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}