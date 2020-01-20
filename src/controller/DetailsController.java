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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DetailsController{
    private Stage S;

    @FXML
    private Label lblRoomNr;
    @FXML
    private Label lblGuestName;
    @FXML
    private Label lblDepature;
    @FXML
    private Label lblAmount;
    @FXML
    private ChoiceBox choiceMovie;
    @FXML
    private ChoiceBox choiceWellness;
    @FXML
    private ChoiceBox choiceMinibar;
    @FXML
    private Button btn_add_movie;
    @FXML
    private Button btn_add_wellness;
    @FXML
    private Button btn_add_minibar;
    @FXML
    private Button btn_deleteService;
    @FXML
    private CheckBox cb_movie;
    @FXML
    private CheckBox cb_wellness;
    @FXML
    private CheckBox cb_minibar;
    @FXML
    private ListView serviceListView;

    //during development:
    private int bookingID = 3;
    private int serviceID = 0;
    private int servicesID = 0;

    void start() throws Exception {
        Stage S = new Stage();
        S.setTitle("Showing Details for Selection");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/details.fxml")));
        S.show();
    }

    @FXML
    public void initialize(){
        try {
            populateMoviesChoice();
            populateWellnessChoice();
            populateMinibarChoice();

            populateListService();
            setServiceAmount();
            setGuestDetails();

            btn_add_movie.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        addService("movie");
                        populateListService();
                        setServiceAmount();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            btn_add_wellness.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        addService("wellness");
                        populateListService();
                        setServiceAmount();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            btn_add_minibar.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        addService("minibar");
                        populateListService();
                        setServiceAmount();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            btn_deleteService.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        deleteService();
                        populateListService();
                        setServiceAmount();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            System.err.println("Exception in initialize ");
        }
    }

    public void addService(String serviceType)throws Exception{
        if (serviceID!=0) {
            java.util.Date date=new java.util.Date();
            java.sql.Date sqlDate=new java.sql.Date(date.getTime());
            PreparedStatement preparedStatement =
                    Database.c.prepareStatement("INSERT INTO services(fk_bookingID, serviceType, serviceDate, fk_serviceID) \n" +
                            "VALUES ('" + bookingID + "', '"+serviceType+"', '" + sqlDate +"', '" + serviceID + "' )");
            preparedStatement.executeUpdate();
            servicesID=0;
            btn_add_minibar.setText("select item to add");
            btn_add_wellness.setText("select item to add");
            btn_add_movie.setText("select item to add");
        }
    }

    public void deleteService()throws Exception{
        if (servicesID!=0){
            PreparedStatement preparedStatement =
                    Database.c.prepareStatement("DELETE FROM services WHERE servicesID = " + servicesID );
            preparedStatement.executeUpdate();
            btn_deleteService.setText("select item for deleting");
        }
    }

    public void setServiceAmount()throws Exception{
        PreparedStatement preparedStatement =
                Database.c.prepareStatement("SELECT SUM(coalesce(serv_movies.moviePrice, serv_wellness.wellnessPrice, serv_minibar.mbPrice,0)) as servicePrice \n" +
                        " FROM services \n" +
                        " LEFT OUTER JOIN serv_movies ON services.fk_serviceID = serv_movies.movieID AND services.serviceType='movie' \n" +
                        " LEFT OUTER JOIN serv_wellness ON services.fk_serviceID = serv_wellness.wellnessID AND services.serviceType = 'wellness' \n" +
                        " LEFT OUTER JOIN serv_minibar ON services.fk_serviceID = serv_minibar.mbID AND services.serviceType = 'minibar'  \n" +
                        " WHERE fk_bookingID="+bookingID);
        ResultSet rsPrice = preparedStatement.executeQuery();
        int index=1;
        while (rsPrice.next()){
            double servicePrice = rsPrice.getInt(index);
            System.out.println(servicePrice);
            lblAmount.setText(String.format("%1.2f €", servicePrice/100));
        }
    }

    public void setGuestDetails()throws Exception{
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
                    serviceID = movieList.get(newValue.intValue()).getMovieID();
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
                    serviceID=wellnessList.get(newValue.intValue()).getWellnessID();
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
                    serviceID=minibarList.get(newValue.intValue()).getMbID();

                }catch (Exception e){
                    System.out.println("Error changelistener minibarlist");
                }
            }
        });
        choiceMinibar.getSelectionModel().selectFirst();
    }
}