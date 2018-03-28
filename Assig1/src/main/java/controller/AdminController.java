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
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

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


    public AdminController(){
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        LoginController loginController = new LoginController(controller.ComponentFactory.instance().getAuthenticationService());
        loader.setController(loginController);
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Parent sceneMain = null;
        try {
            sceneMain = loader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Scene scene = new Scene(sceneMain);
        stage.setScene(scene);
        stage.setTitle("User screen");
        stage.show();
    }
}