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

public class BillsController {
    @FXML
    private ComboBox<Account> sendingAccount;
    @FXML
    private TextField billCodeText;
    @FXML
    private TextField amountText;
    @FXML
    private Button payBillButton;
    @FXML
    private Button backButton;

    private Long clientId;
    private FXMLLoader userLoader;
    private AccountService accountService;
    private Activity activity;

    public BillsController(FXMLLoader userLoader, AccountService accountService, Activity activity) {
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

    private void refreshView(){
        populateSendingAccountBox();
        billCodeText.clear();
        amountText.clear();
    }

    @FXML
    private void payBillHandler(ActionEvent e){
        int amount;
        if(amountText.getText().compareTo("") == 0)
            amount = 0;
        else
            amount = Integer.parseInt(amountText.getText());
        if(amount > 0 && sendingAccount.getValue() != null && billCodeText.getText().compareTo("") != 0) {
            Notification<Boolean> transferNotification = accountService
                    .payBills(sendingAccount.getValue().getId(), sendingAccount.getValue().getType(), sendingAccount.getValue().getAmount(),
                            sendingAccount.getValue().getCreationDate().toLocalDate(), amount);
            if (transferNotification.hasErrors()) {
                showAlert(Alert.AlertType.WARNING, "Paying bill unsuccessful", transferNotification.getFormattedErrors());
            } else {
                if (!transferNotification.getResult()) {
                    showAlert(Alert.AlertType.WARNING, "Paying bill unsuccessful", "The paying was not processed correctly, please try again later.");
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Paying bill successfully", "");
                    activity.setOperationAndTimeStamp("Bill paid from the account with id " + sendingAccount.getValue().getId() + " of the client with id " + clientId, Date.valueOf(LocalDate.now()));
                    refreshView();
                }
            }
        }
        else{
            showAlert(Alert.AlertType.WARNING, "Please select an account, type a bill code and put an amount greater than 0", "");
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
