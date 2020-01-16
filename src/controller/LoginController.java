package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {
    private Stage S;
    String presetUsername = "user";
    String presetPassword = "user";

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    //checking for username and pass


    public void login() throws Exception {
        String user = username.getText();
        String pass = password.getText();
        if(presetUsername.equals(user) && presetPassword.equals(pass)){
            MainController M = new MainController();
            M.start();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error occurred!");
            alert.setHeaderText("Incorrect Username or Password.");
            alert.setContentText("The Username or Password you entered is incorrect!");
            alert.showAndWait();
        }
    }
}
