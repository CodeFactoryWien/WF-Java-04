package controller;

import database.Database;
import hotel.services.Movie;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.awt.*;
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
    private ChoiceBox choiseWellness;
    @FXML
    private ChoiceBox choiseMinibar;

    @FXML
    private ListView listMovie;
    @FXML
    private ListView listWellness;
    @FXML
    private ListView listMinibar;


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
        }catch (Exception e){
            System.err.println("Exception in moviechoice");
        }

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
            Double moviePrice = rsMovies.getDouble("moviePrice");
            int movieSeen = rsMovies.getInt("movieSeen");
            movieList.add(new Movie(i,movieName, movieDescription, moviePrice, movieSeen));

        }

        //choiceMovie.getItems().addAll(movieList);
        choiceMovie.setItems(movieList);

        // changelistener
        choiceMovie.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    System.out.println("Choicebox changed"+newValue);;
                } catch (Exception e) {
                    System.out.println("fehler im changeevent");
                }
            }
        });
    }


}