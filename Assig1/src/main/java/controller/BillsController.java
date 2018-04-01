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

    public BillsController(FXMLLoader userLoader, AccountService accountService) {
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
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Paying bill unsuccessful");
                alert.setContentText(transferNotification.getFormattedErrors());
                alert.showAndWait();
            } else {
                if (!transferNotification.getResult()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Paying bill unsuccessful");
                    alert.setContentText("The paying was not processed correctly, please try again later.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Paying bill successfully");
                    alert.showAndWait();
                    refreshView();
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Please select an account, type a bill code and put an amount greater than 0");
            alert.showAndWait();
            refreshView();
        }
    }

    @FXML
    private void backHandler(ActionEvent e){
        Scene scene = backButton.getScene();
        scene.setRoot(userLoader.getRoot());
    }
}
