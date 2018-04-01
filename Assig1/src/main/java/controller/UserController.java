package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
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

    public UserController(FXMLLoader loginLoader, FXMLLoader accountLoader, FXMLLoader transferLoader, FXMLLoader billsLoader, ClientService clientService) {
        this.loginLoader = loginLoader;
        this.accountLoader = accountLoader;
        this.transferLoader = transferLoader;
        this.billsLoader = billsLoader;
        this.clientService = clientService;
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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Saving unsuccessful");
            alert.setContentText(saveNotification.getFormattedErrors());
            alert.showAndWait();
        } else {
            if (!saveNotification.getResult()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Saving unsuccessful");
                alert.setContentText("Client was not added successful, please try again later.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Client added successfully");
                refreshView();
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void updateClientHandler(ActionEvent e){
        if(clientComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Update unsuccessful");
            alert.setContentText("Please select a client.");
            alert.showAndWait();
        }
        else {
            Notification<Boolean> updateNotification = clientService
                    .update(Long.parseLong(clientIdText.getText()),clientNameText.getText(), cardNbText.getText(), pncText.getText(), addressText.getText());
            if (updateNotification.hasErrors()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Updating unsuccessful");
                alert.setContentText(updateNotification.getFormattedErrors());
                alert.showAndWait();
            } else {
                if (updateNotification.getResult()) {
                    refreshView();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Client updated successfully");
                    refreshView();
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Update unsuccessful");
                    alert.setContentText("Client was not updated successful, please try again later.");
                    alert.showAndWait();
                }
            }

        }
    }

    private void showUnselectedWarning(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Warning!");
        alert.setContentText("Please select a client for this operation");
        alert.showAndWait();
    }

    @FXML
    private void accountsHandler(ActionEvent e){
        if(clientComboBox.getValue() == null)
        {
            showUnselectedWarning();
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
        if(clientComboBox.getValue() == null)
        {
            showUnselectedWarning();
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
        if(clientComboBox.getValue() == null)
        {
            showUnselectedWarning();
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
