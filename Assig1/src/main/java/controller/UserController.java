package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Activity;
import model.Client;
import model.Role;
import model.User;
import model.validation.Notification;
import service.client.ClientService;
import service.user.AuthenticationService;
import service.user.UserService;
import sun.awt.ComponentFactory;

import javax.swing.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Roles.ADMINISTRATOR;

public class UserController {
    @FXML
    private Button logoutButton;
    @FXML
    private Button updateClientButton;
    @FXML
    private Button addClientButton;
    @FXML
    private Button accountsButton;
    @FXML
    private Button utilityBillsButton;
    @FXML
    private Button transferMoneyButton;
    @FXML
    private TextField clientIdText;
    @FXML
    private TextField clientNameText;
    @FXML
    private TextField cardNbText;
    @FXML
    private TextField pncText;
    @FXML
    private TextField addressText;
    @FXML
    private ComboBox<Client> clientComboBox;

    private FXMLLoader loginLoader;
    private FXMLLoader accountLoader;
    private FXMLLoader transferLoader;
    private FXMLLoader billsLoader;
    private ClientService clientService;
    private Activity activity;

    public UserController(FXMLLoader loginLoader, FXMLLoader accountLoader, FXMLLoader transferLoader, FXMLLoader billsLoader, ClientService clientService, Activity activity) {
        this.loginLoader = loginLoader;
        this.accountLoader = accountLoader;
        this.transferLoader = transferLoader;
        this.billsLoader = billsLoader;
        this.clientService = clientService;
        this.activity = activity;
    }

    private void refreshView(){
        populateClientBox();
        clientIdText.clear();
        clientNameText.clear();
        cardNbText.clear();
        pncText.clear();
        addressText.clear();
    }

    public void populateClientBox(){
        clientComboBox.getItems().clear();
        List<Client> clients = clientService.findAll();
        clientComboBox.getItems().addAll(clients);
    }

    @FXML
    private void clientSelectionHandler(ActionEvent e){
        if(clientComboBox.getValue() != null) {
            clientIdText.setText(String.valueOf(clientComboBox.getValue().getId()));
            clientNameText.setText(clientComboBox.getValue().getName());
            cardNbText.setText(clientComboBox.getValue().getCardNb());
            pncText.setText(clientComboBox.getValue().getPnc());
            addressText.setText(clientComboBox.getValue().getAddress());
        }
    }

    @FXML
    private void addClientHandler(ActionEvent e){
        Notification<Boolean> saveNotification = clientService
                .save(clientNameText.getText(), cardNbText.getText(), pncText.getText(), addressText.getText());
        if (saveNotification.hasErrors()) {
            showAlert(Alert.AlertType.WARNING, "Saving unsuccessful", saveNotification.getFormattedErrors());
        } else {
            if (!saveNotification.getResult()) {
                showAlert(Alert.AlertType.WARNING, "Saving unsuccessful", "Client was not added successful, please try again later.");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Client added successfully", "");
                activity.setOperationAndTimeStamp("Client " + clientNameText.getText() + " added", Date.valueOf(LocalDate.now()));
                refreshView();
            }
        }
    }

    @FXML
    private void updateClientHandler(ActionEvent e){
        if(clientComboBox.getValue() == null){
            showAlert(Alert.AlertType.WARNING, "Update unsuccessful", "Please select a client.");
        }
        else {
            Notification<Boolean> updateNotification = clientService
                    .update(Long.parseLong(clientIdText.getText()),clientNameText.getText(), cardNbText.getText(), pncText.getText(), addressText.getText());
            if (updateNotification.hasErrors()) {
                showAlert(Alert.AlertType.WARNING, "Update unsuccessful", updateNotification.getFormattedErrors());
            } else {
                if (updateNotification.getResult()) {
                    showAlert(Alert.AlertType.INFORMATION, "Client updated successfully", "");
                    activity.setOperationAndTimeStamp("Client with id:" + clientComboBox.getValue().getId() + " and name" + clientComboBox.getValue().getName() + " updated", Date.valueOf(LocalDate.now()));
                    refreshView();
                } else {
                    showAlert(Alert.AlertType.WARNING, "Update unsuccessful", "Client was not updated successful, please try again later.");
                }
            }

        }
    }

    @FXML
    private void accountsHandler(ActionEvent e){
        if(clientComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Warning!", "Please select a client for this operation");
        }
        else {
            AccountController accountController = accountLoader.getController();
            accountController.setClientId(clientComboBox.getValue().getId());
            accountController.populateAccountsBox();
            accountController.populateTypeBox();
            Scene scene = accountsButton.getScene();
            scene.setRoot(accountLoader.getRoot());
        }
    }

    @FXML
    private void utilityBillsHandler(ActionEvent e){
        if(clientComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Warning!", "Please select a client for this operation");
        }
        else {
            BillsController billsController = billsLoader.getController();
            billsController.setClientId(clientComboBox.getValue().getId());
            billsController.populateSendingAccountBox();
            Scene scene = utilityBillsButton.getScene();
            scene.setRoot(billsLoader.getRoot());
        }
    }

    @FXML
    private void transferMoneyHandler(ActionEvent e){
        if(clientComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Warning!", "Please select a client for this operation");
        }
        else {
            TransferController transferController = transferLoader.getController();
            transferController.setClientId(clientComboBox.getValue().getId());
            transferController.populateClientsBox(clientComboBox.getItems());
            transferController.populateSendingAccountBox();
            Scene scene = transferMoneyButton.getScene();
            scene.setRoot(transferLoader.getRoot());
        }
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
