package controller;

import database.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

public class AdminController {
    @FXML
    private Button logoutButton;
    @FXML
    private Button updateUserButton;
    @FXML
    private Button deleteUserButton;
    @FXML
    private Button addUserButton;
    @FXML
    private Button reportButton;
    @FXML
    private TextField userIdText;
    @FXML
    private TextField usernameText;
    @FXML
    private TextField passwordText;
    @FXML
    private ComboBox roleComboBox;
    @FXML
    private ComboBox<User> userComboBox;

    private FXMLLoader loginLoader;


    public AdminController(FXMLLoader loginLoader){
        this.loginLoader = loginLoader;
    }

    @FXML
    private void addUserHandler(ActionEvent e){

    }

    @FXML
    private void userSelectionHandler(){
        userIdText.setText(String.valueOf(userComboBox.getValue().getId()));
        usernameText.setText(userComboBox.getValue().getUsername());
        roleComboBox.setValue(userComboBox.getValue().getRoles().get(0));
    }

    public void populateUserBox(){
    }

    public void populateRoleComboBox(){
        roleComboBox.getItems().addAll(Constants.Roles.ROLES);
    }

    @FXML
    private void logoutHandler(ActionEvent e){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Logout successful");
        alert.showAndWait();

        Scene scene = logoutButton.getScene();
        scene.setRoot(loginLoader.getRoot());
    }
}
