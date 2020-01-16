package sample;

import database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage1) throws Exception{

        stage1.setTitle("hotel Managing Software");
        stage1.setScene(FXMLLoader.load(getClass().getResource("/view/login.fxml")));
        stage1.show();
    }

    public static void main(String[] args) {
        try {
            Database.openConnection();
        } catch (Exception e) {
            System.out.println("cannot connect");
        }
        launch(args);
    }
}