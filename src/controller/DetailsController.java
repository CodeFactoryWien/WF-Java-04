package controller;

import database.Database;
import hotel.services.Movie;
import hotel.services.Wellness;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.awt.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DetailsController{
    private Stage S;

    @FXML
    private Label roomNr;
    @FXML
    private Label guestName;
    @FXML
    private Label depature;
    @FXML
    private Label amount;
    @FXML
    private ChoiceBox choiceMovie;
    @FXML
    private ChoiceBox choiceWellness;
    @FXML
    private ChoiceBox choiceMinibar;

    @FXML
    private Button add_movie;
    @FXML
    private Button add_wellness;

    @FXML
    private ListView listMovie;
    @FXML
    private ListView listWellness;
    @FXML
    private ListView listMinibar;

    //during development:
    private int bookingID = 3;


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
        }catch (Exception e){
            System.err.println("Exception in moviechoice aufruf ");
        }

    }
    //populate Head of Details
    /*
    public void populateDetails()throws Exception{
        PreparedStatement preparedStatement =
                Database.c.prepareStatement("SELECT fk_guestID FROM bookings where bookingID=1");
            // Überschriften erstellen
    }

    public void populateListMovie() throws  Exception{
        PreparedStatement preparedStatement =
                Database.c.prepareStatement("SELECT movieDate, fk_MovieID, serv_movies.movieName \n" +
                        "FROM services\n" +
                        "INNER JOIN serv_movies ON services.fk_movieID = serv_movies.movieID\n" +
                        "WHERE fk_bookingID="+bookingID );
        ResultSet rsMoviesList = preparedStatement.executeQuery();
        ObservableList<ArrayList> seenMoviesList = FXCollections.observableArrayList();

        while (rsMoviesList.next()){
            int i = rsMoviesList.getInt("serviceID");
            Date movieDate = rsMoviesList.getDate("movieDate");
            String movieName = rsMoviesList.getString("movieName");
            seenMoviesList.addAll();
        }
    }
*/

    public void populateMoviesChoice() throws Exception {
        PreparedStatement preparedStatement =
        Database.c.prepareStatement("SELECT * FROM serv_movies");
        ResultSet rsMovies = preparedStatement.executeQuery();
        ObservableList<Movie> movieList = FXCollections.observableArrayList();

        while (rsMovies.next()) {
            int i = rsMovies.getInt("movieID");
            String movieName = rsMovies.getString("movieName");
            String movieDescription = rsMovies.getString("movieDescription");
            Double moviePrice = rsMovies.getDouble("moviePrice");
            int movieSeen = rsMovies.getInt("movieSeen");
            movieList.add(new Movie(i,movieName, movieDescription, moviePrice, movieSeen));
        }
        choiceMovie.setItems(movieList);
        // changelistener
        choiceMovie.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    add_movie.setText("Movie: "  + movieList.get(newValue.intValue()).getMovieID());
                } catch (Exception e) {
                    System.out.println("Error");
                }
            }
        });
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
            Double wellnessPrice = rsWellness.getDouble("wellnessPrice");
            wellnessList.add(new Wellness(wellnessID, wellnessName, wellnessDescription, wellnessPrice));
        }
        choiceWellness.setItems(wellnessList);
        // changelistener
        choiceWellness.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    add_wellness.setText("Wellness: " + wellnessList.get(newValue.intValue()).getWellnessID());
                } catch (Exception e) {
                    System.out.println("Error");
                }
            }
        });
    }
}