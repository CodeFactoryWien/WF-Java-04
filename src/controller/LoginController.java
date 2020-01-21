package controller;

import database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;

    //checking for username and pass

    public void login() {
        String userInput = username.getText().toLowerCase();
        String passInput = password.getText();

        //check if username is in database, if yes get the hash

        String dbUserName = Database.checkUserName(userInput);
        String dbPasswordHash = Database.getPasswordHash(passInput);
        try {
            switch (dbUserName) {
                case "admin":
                    //hashing user input and comparing with hash on db, else throw invalid data alert
                    if (verifyPassword(passInput, dbPasswordHash, salt)) {
                        MainController M = new MainController();
                        M.setAdminStatus();
                        M.start();
                        Stage S = (Stage) loginButton.getScene().getWindow();
                        S.close();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error occurred!");
                        alert.setHeaderText("Incorrect Username or Password.");
                        alert.setContentText("The Username or Password you entered is incorrect!");
                        alert.showAndWait();
                    }
                    break;
                case "user":
                    if (verifyPassword(passInput, dbPasswordHash, salt)) {
                        MainController M1 = new MainController();
                        M1.start();
                        Stage S1 = (Stage) loginButton.getScene().getWindow();
                        S1.close();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error occurred!");
                        alert.setHeaderText("Incorrect Username or Password.");
                        alert.setContentText("The Username or Password you entered is incorrect!");
                        alert.showAndWait();
                    }
                    break;
                default:
                    System.out.println("I like you. You got to somewhere, where you shouldn't have gone.");
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error alert");
        alert.setHeaderText(e.getMessage());
        VBox dialogPaneContent = new VBox();
        Label label = new Label("Stack Trace:");
        String stackTrace = this.getStackTrace(e);
        TextArea textArea = new TextArea();
        textArea.setText(stackTrace);
        dialogPaneContent.getChildren().addAll(label, textArea);
        // Set content for Dialog Pane
        alert.getDialogPane().setContent(dialogPaneContent);
        alert.showAndWait();
    }

    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String s = sw.toString();
        return s;
    }
    // hash / verify data (credits to steven's brain and #stackoverflow)

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private final String salt = "P9n7OJVzbIPVOfOuAzebkGduLjhu2fCA";

    public static Optional<String> hashPassword (String password, String salt) {

        char[] chars = password.toCharArray();
        byte[] bytes = salt.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

        Arrays.fill(chars, Character.MIN_VALUE);

        try {
            SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            return Optional.of(Base64.getEncoder().encodeToString(securePassword));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.err.println("Exception encountered in hashPassword()");
            return Optional.empty();

        } finally {
            spec.clearPassword();
        }
    }

    public static boolean verifyPassword (String password, String key, String salt) {
        Optional<String> optEncrypted = hashPassword(password, salt);
        if (!optEncrypted.isPresent()) return false;
        return optEncrypted.get().equals(key);
    }

    void start() throws Exception {
        Stage S = new Stage();
        S.setTitle("hotel Managing Software");
        S.setScene(FXMLLoader.load(getClass().getResource("/view/login.fxml")));
        S.show();
    }
}