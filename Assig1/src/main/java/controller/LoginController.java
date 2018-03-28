package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import model.validation.Notification;
import repository.user.AuthenticationException;
import service.user.AuthenticationService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginController {
    private AuthenticationService authenticationService;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button loginButton;

    public LoginController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    public void LoginHandler(){
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        Notification<String> loginNotification = null;
        try {
            loginNotification = authenticationService.login(username, password);
        } catch (AuthenticationException e1) {
            e1.printStackTrace();
        }

        if (loginNotification != null) {
            if (loginNotification.hasErrors()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Login unsuccessful");
                alert.setContentText("Wrong username or password!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Login successful");
                alert.showAndWait();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserView.fxml"));
                UserController userController = new UserController();
                loader.setController(userController);
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Parent sceneMain = null;
                try {
                    sceneMain = loader.load();
                    userController.setPageForRole(loginNotification.getResult());
                    Scene scene = new Scene(sceneMain);
                    stage.setScene(scene);
                    stage.setTitle("User screen");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
