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
import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import repository.security.RightsRolesRepository;
import service.security.RightsRolesService;
import service.user.UserService;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private ComboBox<Role> roleComboBox;
    @FXML
    private ComboBox<User> userComboBox;

    private FXMLLoader loginLoader;
    private UserService userService;
    private RightsRolesService rightsRolesService;


    public AdminController(FXMLLoader loginLoader, UserService userService, RightsRolesService rightsRolesService){
        this.loginLoader = loginLoader;
        this.userService = userService;
        this.rightsRolesService = rightsRolesService;
    }

    private void refreshView(){
        populateUserBox();
        roleComboBox.setValue(null);
        userIdText.clear();
        usernameText.clear();
        passwordText.clear();
    }

    @FXML
    private void addUserHandler(ActionEvent e){
        List<Role> roles = new ArrayList<>();
        if(roleComboBox.getValue() != null) {
            roles.add(roleComboBox.getValue());

            Notification<Boolean> registerNotification = userService.save(usernameText.getText(), passwordText.getText(), roles);
            if (registerNotification.hasErrors()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Operation unsuccessful");
                alert.setContentText(registerNotification.getFormattedErrors());
                alert.showAndWait();
            } else {
                if (!registerNotification.getResult()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Operation unsuccessful");
                    alert.setContentText("User was not added successful, please try again later.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("User added successfully");
                    refreshView();
                    alert.showAndWait();
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Operation unsuccessful");
            alert.setContentText("Please select a role for the new user");
            alert.showAndWait();
        }
    }

    @FXML
    private void updateUserHandler(ActionEvent e){
        if(userComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Update unsuccessful");
            alert.setContentText("Please select a user.");
            alert.showAndWait();
        }
        else {
            List<Role> roles = new ArrayList<>();
            roles.add(roleComboBox.getValue());
            String password;
            boolean updatePassword;
            if(passwordText.getText().compareTo("")==0){
                password = userComboBox.getValue().getPassword();
                updatePassword = false;
            }
            else{
                password = passwordText.getText();
                updatePassword = true;
            }
            Boolean isSuccessful = userService.update(Long.parseLong(userIdText.getText()),
                    userComboBox.getValue().getUsername(), password, roles, updatePassword).getResult();
            if(isSuccessful){
                refreshView();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("User updated successfully");
                refreshView();
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Update unsuccessful");
                alert.setContentText("User was not updated successful, please try again later.");
                alert.showAndWait();
            }

        }
    }

    @FXML
    private void deleteUserHandler(ActionEvent e){
        if(userComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Delete unsuccessful");
            alert.setContentText("Please select a user.");
            alert.showAndWait();
        }
        else {
            boolean isSuccessful = userService.removeUser(Long.parseLong(userIdText.getText()));
            if(isSuccessful){
                refreshView();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("User deleted successfully");
                refreshView();
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Delete unsuccessful");
                alert.setContentText("User was not deleted successful, please try again later.");
                alert.showAndWait();
            }

        }
    }

    @FXML
    private void reportHandler(ActionEvent e){

    }

    @FXML
    private void userSelectionHandler(){
        if(userComboBox.getValue() != null) {
            userIdText.setText(String.valueOf(userComboBox.getValue().getId()));
            usernameText.setText(userComboBox.getValue().getUsername());
            roleComboBox.setValue(userComboBox.getValue().getRoles().get(0));
        }
    }

    public void populateUserBox(){
        userComboBox.getItems().clear();
        List<User> users = userService.findAll();
        userComboBox.getItems().addAll(users);
    }

    public void populateRoleComboBox(){
        roleComboBox.getItems().addAll(rightsRolesService.findAllRoles());
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
