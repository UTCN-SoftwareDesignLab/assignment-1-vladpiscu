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

import static database.Constants.Roles.EMPLOYEE;

public class LoginController {
    private AuthenticationService authenticationService;
    private FXMLLoader adminLoader;
    private FXMLLoader userLoader;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button loginButton;

    public LoginController(AuthenticationService authenticationService, FXMLLoader adminLoader, FXMLLoader userLoader){
        this.authenticationService = authenticationService;
        this.adminLoader = adminLoader;
        this.userLoader = userLoader;
    }

    public void loginHandler(){
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        Notification<User> loginNotification = null;
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
                alert.setContentText(loginNotification.getFormattedErrors());
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Login successful");
                alert.showAndWait();
                String role = loginNotification.getResult().getRoles().get(0).getRole();

                Parent sceneMain = null;
                FXMLLoader currentLoader;
                if(role.compareTo(EMPLOYEE)==0)
                    currentLoader = userLoader;
                else {
                    currentLoader = adminLoader;
                    AdminController adminController = currentLoader.getController();
                    adminController.populateUserBox();
                    adminController.populateRoleComboBox();
                }
                usernameTextField.clear();
                passwordTextField.clear();
                Scene scene = loginButton.getScene();
                scene.setRoot(currentLoader.getRoot());

            }
        }
    }
}
