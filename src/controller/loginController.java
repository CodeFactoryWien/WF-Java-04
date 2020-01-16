package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class loginController {
    private Stage S;
    String presetUsername = "user";
    String presetPassword = "user";

    @FXML
    private TextField username;
    @FXML
    private TextField password;

    //checking for username and pass


    public void login(MouseEvent mouseEvent) throws Exception {
        String user = username.getText();
        String pass = password.getText();
        if(presetUsername.equals(user) && presetPassword.equals(pass)){
            MainController M = new MainController();
            M.start();
        }
        else {
            System.out.println("Wrong Password.");
        }
        System.out.println(user);
        System.out.println(pass);
        System.out.println(presetUsername);
        System.out.println(presetPassword);
    }
}
