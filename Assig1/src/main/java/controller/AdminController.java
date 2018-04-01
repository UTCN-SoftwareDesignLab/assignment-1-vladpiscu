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
import model.Activity;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import repository.security.RightsRolesRepository;
import service.security.RightsRolesService;
import service.user.UserService;

import javax.swing.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
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
    private FXMLLoader reportLoader;
    private UserService userService;
    private RightsRolesService rightsRolesService;
    private Activity activity;


    public AdminController(FXMLLoader loginLoader, FXMLLoader reportLoader, UserService userService, RightsRolesService rightsRolesService, Activity activity){
        this.loginLoader = loginLoader;
        this.userService = userService;
        this.rightsRolesService = rightsRolesService;
        this.activity = activity;
        this.reportLoader = reportLoader;
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
                showAlert(Alert.AlertType.WARNING, "Operation unsuccessful", registerNotification.getFormattedErrors());
            } else {
                if (!registerNotification.getResult()) {
                    showAlert(Alert.AlertType.WARNING, "Operation unsuccessful", "User was not added successful, please try again later.");
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "User added successfully", "");
                    activity.setOperationAndTimeStamp("User " + usernameText.getText() + " added", Date.valueOf(LocalDate.now()));
                    refreshView();
                }
            }
        }
        else{
            showAlert(Alert.AlertType.WARNING, "Operation unsuccessful", "Please select a role for the new user");
        }
    }

    @FXML
    private void updateUserHandler(ActionEvent e){
        if(userComboBox.getValue() == null){
            showAlert(Alert.AlertType.WARNING, "Update unsuccessful", "Please select a user.");
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
            Notification<Boolean> updateNotification = userService.update(Long.parseLong(userIdText.getText()),
                    userComboBox.getValue().getUsername(), password, roles, updatePassword);
            if (updateNotification.hasErrors()) {
                showAlert(Alert.AlertType.WARNING, "Update unsuccessful", updateNotification.getFormattedErrors());
            } else {
                if (updateNotification.getResult()) {
                    showAlert(Alert.AlertType.INFORMATION, "User updated successfully", "");
                    activity.setOperationAndTimeStamp("User " + userComboBox.getValue().getUsername() + " updated", Date.valueOf(LocalDate.now()));
                    refreshView();
                } else {
                    showAlert(Alert.AlertType.WARNING, "Update unsuccessful", "User was not updated successful, please try again later.");
                }
            }

        }
    }

    @FXML
    private void deleteUserHandler(ActionEvent e){
        if(userComboBox.getValue() == null){
            showAlert(Alert.AlertType.WARNING, "Delete unsuccessful", "Please select a user.");
        }
        else {
            boolean isSuccessful = userService.removeUser(Long.parseLong(userIdText.getText()));
            if(isSuccessful){
                showAlert(Alert.AlertType.INFORMATION, "User deleted successfully", "");
                activity.setOperationAndTimeStamp("User " + userComboBox.getValue().getUsername() + " deleted", Date.valueOf(LocalDate.now()));
                refreshView();
            }
            else{
                showAlert(Alert.AlertType.WARNING, "Delete unsuccessful", "User was not deleted successful, please try again later.");
            }

        }
    }

    @FXML
    private void reportHandler(ActionEvent e){
        if(userComboBox.getValue() != null) {
            ReportController reportController = reportLoader.getController();
            reportController.setUserId(userComboBox.getValue().getId());
            Scene scene = reportButton.getScene();
            scene.setRoot(reportLoader.getRoot());
        }
        else{
            showAlert(Alert.AlertType.WARNING, "Operation unavailable", "Please select the user for which the report should be generated.");
        }
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

    private void showAlert(Alert.AlertType alertType, String headerText, String contentText){
        Alert alert;
        if(alertType == Alert.AlertType.WARNING){
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");

        }
        else{
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
        }
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    private void logoutHandler(ActionEvent e){
        showAlert(Alert.AlertType.INFORMATION, "Logout successful", "");
        Scene scene = logoutButton.getScene();
        scene.setRoot(loginLoader.getRoot());
    }
}
