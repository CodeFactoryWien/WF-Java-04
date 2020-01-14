package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage1) throws Exception{

        stage1.setTitle("Hotel Managing Software");
        stage1.setScene(FXMLLoader.load(getClass().getResource("/view/main.fxml")));
        stage1.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
