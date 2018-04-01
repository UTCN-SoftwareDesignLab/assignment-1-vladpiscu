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
import model.Activity;
import model.Client;
import model.validation.Notification;
import service.account.AccountService;

import java.sql.Date;
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
    private Activity activity;

    public TransferController(FXMLLoader userLoader, AccountService accountService, Activity activity) {
        this.userLoader = userLoader;
        this.accountService = accountService;
        this.activity = activity;
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
                showAlert(Alert.AlertType.WARNING, "Transferring money unsuccessful", "Can't transfer money on the same account!");
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
                    showAlert(Alert.AlertType.WARNING, "Transferring money unsuccessful", transferNotification.getFormattedErrors());
                } else {
                    if (!transferNotification.getResult()) {
                        showAlert(Alert.AlertType.WARNING, "Transferring money unsuccessful", "The transfer was not processed correctly, please try again later.");
                    } else {
                        showAlert(Alert.AlertType.INFORMATION, "Transferring money successfully", "");
                        activity.setOperationAndTimeStamp("Transfer done from the account with id " + sendingAccount.getValue().getId() + " to the account with id " + receivingAccount.getValue().getId(), Date.valueOf(LocalDate.now()));
                        refreshView();
                    }
                }
            }
        }
        else{
            showAlert(Alert.AlertType.WARNING, "Operation unsuccessful", "Please select a sending account and a receiving one");
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
    private void backHandler(ActionEvent e){
        Scene scene = backButton.getScene();
        scene.setRoot(userLoader.getRoot());
    }
}
