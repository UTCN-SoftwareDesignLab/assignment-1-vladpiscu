package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Account;
import model.Client;
import model.validation.Notification;
import service.account.AccountService;

import java.time.LocalDate;
import java.util.List;

public class TransferController {

    @FXML
    private ComboBox<Account> sendingAccount;
    @FXML
    private ComboBox<Client> receivingClient;
    @FXML
    private ComboBox<Account> receivingAccount;
    @FXML
    private TextField amountText;
    @FXML
    private Button transferMoneyButton;
    @FXML
    private Button backButton;

    private Long clientId;
    private FXMLLoader userLoader;
    private AccountService accountService;

    public TransferController(FXMLLoader userLoader, AccountService accountService) {
        this.userLoader = userLoader;
        this.accountService = accountService;
    }

    public void setClientId(Long clientId){
        this.clientId = clientId;
    }

    public void populateSendingAccountBox(){
        sendingAccount.getItems().clear();
        sendingAccount.getItems().addAll(accountService.getAccountsForClient(clientId));
    }

    public void populateClientsBox(List<Client> clients){
        receivingClient.getItems().clear();
        receivingClient.getItems().addAll(clients);
    }

    private void populateReceivingAccountBox(Long clientId){
        receivingAccount.getItems().clear();
        if(clientId != null) {
            receivingAccount.getItems().addAll(accountService.getAccountsForClient(clientId));
        }

    }

    private void refreshView(){
        populateSendingAccountBox();
        receivingClient.setValue(null);
        amountText.clear();
    }

    @FXML
    private void clientSelectionHandler(ActionEvent e){
        if(receivingClient.getValue() != null) {
            populateReceivingAccountBox(receivingClient.getValue().getId());
        }
        else{
            populateReceivingAccountBox(null);
        }
    }

    @FXML
    private void transferMoneyHandler(ActionEvent e){
        if(sendingAccount.getValue() != null && receivingAccount.getValue() != null) {
            if(sendingAccount.getValue().equals(receivingAccount.getValue())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Transferring money unsuccessful");
                alert.setContentText("Can't transfer money on the same account!");
                alert.showAndWait();
            }
            else{
                int amount;
                if(amountText.getText().compareTo("") == 0)
                    amount = 0;
                else
                    amount = Integer.parseInt(amountText.getText());
                Notification<Boolean> transferNotification = accountService
                        .transferMoneyBetweenAccounts(sendingAccount.getValue(), receivingAccount.getValue(), amount);
                if (transferNotification.hasErrors()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Transferring money unsuccessful");
                    alert.setContentText(transferNotification.getFormattedErrors());
                    alert.showAndWait();
                } else {
                    if (!transferNotification.getResult()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText("Transferring money unsuccessful");
                        alert.setContentText("The transfer was not processed correctly, please try again later.");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Transfer done successfully");
                        alert.showAndWait();
                        refreshView();
                    }
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Operation unsuccessful");
            alert.setContentText("Please select a sending account and a receiving one");
            alert.showAndWait();
        }
    }

    @FXML
    private void backHandler(ActionEvent e){
        Scene scene = backButton.getScene();
        scene.setRoot(userLoader.getRoot());
    }
}
