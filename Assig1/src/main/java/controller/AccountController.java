package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import model.Account;
import model.Activity;
import model.validation.Notification;
import service.account.AccountService;

import java.sql.Date;
import java.time.LocalDate;

import static database.Constants.AccountTypes.ACCOUNT_TYPES;

public class AccountController {
    @FXML
    private ComboBox<Account> accountComboBox;
    @FXML
    private TextField accountIdText;
    @FXML
    private TextField amountText;
    @FXML
    private DatePicker creationDatePicker;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private Button addAccountButton;
    @FXML
    private Button deleteAccountButton;
    @FXML
    private Button updateAccountButton;
    @FXML
    private Button backButton;

    private Long clientId;
    private FXMLLoader userLoader;
    private AccountService accountService;
    private Activity activity;

    public AccountController(FXMLLoader userLoader, AccountService accountService, Activity activity) {
        this.userLoader = userLoader;
        this.accountService = accountService;
        this.activity = activity;
    }

    public void setClientId(Long clientId){
        this.clientId = clientId;
    }

    private void refreshView(){
        populateAccountsBox();
        populateTypeBox();
        accountIdText.clear();
        amountText.clear();
        creationDatePicker.setValue(null);
        typeComboBox.setValue(null);
    }

    public void populateAccountsBox(){
        accountComboBox.getItems().clear();
        accountComboBox.getItems().addAll(accountService.getAccountsForClient(clientId));
    }

    public void populateTypeBox(){
        typeComboBox.getItems().clear();
        typeComboBox.getItems().addAll(ACCOUNT_TYPES);
    }

    @FXML
    private void accountSelectionHandler(ActionEvent e){
        if(accountComboBox.getValue() != null) {
            accountIdText.setText(String.valueOf(accountComboBox.getValue().getId()));
            amountText.setText(String.valueOf(accountComboBox.getValue().getAmount()));
            creationDatePicker.setValue(accountComboBox.getValue().getCreationDate().toLocalDate());
            typeComboBox.setValue(accountComboBox.getValue().getType());
        }
    }

    @FXML
    private void addAccountHandler(ActionEvent e){
        if(typeComboBox.getValue() != null) {
            int amount;
            if(amountText.getText().compareTo("") == 0)
                amount = 0;
            else
                amount = Integer.parseInt(amountText.getText());
            LocalDate date;
            if(creationDatePicker.getValue() == null)
                date = LocalDate.now();
            else
                date = creationDatePicker.getValue();
            Notification<Boolean> saveNotification = accountService
                    .addAccountForClient(clientId, typeComboBox.getValue(), amount, date);
            if (saveNotification.hasErrors()) {
                showAlert(Alert.AlertType.WARNING, "Saving account unsuccessful", saveNotification.getFormattedErrors());
            } else {
                if (!saveNotification.getResult()) {
                    showAlert(Alert.AlertType.WARNING, "Saving account unsuccessful", "Account was not added successful, please try again later.");
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Account added successfully", "");
                    activity.setOperationAndTimeStamp("Account added for the client with id " + clientId , Date.valueOf(date));
                    refreshView();
                }
            }
        }
        else{
            showAlert(Alert.AlertType.WARNING, "Operation unsuccessful", "Please select a type for the account");
        }
    }

    @FXML
    private void deleteAccountHandler(ActionEvent e) {
        if (accountComboBox.getValue() == null){
            showAlert(Alert.AlertType.WARNING, "Delete unsuccessful", "Please select an account.");
        }
        else {
            boolean isSuccessful = accountService.removeAccount(accountComboBox.getValue().getId());
            if(isSuccessful){
                showAlert(Alert.AlertType.INFORMATION, "Account deleted successfully", "");
                activity.setOperationAndTimeStamp("Account with id " + accountComboBox.getValue().getId() + " of the client with id " + clientId + " deleted", Date.valueOf(LocalDate.now()));
                refreshView();
            }
            else{
                showAlert(Alert.AlertType.WARNING, "Delete unsuccessful", "Account was not deleted successful, please try again later.");
            }
        }
    }

    @FXML
    private void updateAccountHandler(ActionEvent e){
        if(accountComboBox.getValue() != null) {
            Notification<Boolean> saveNotification = accountService
                    .updateAccount(accountComboBox.getValue().getId(), typeComboBox.getValue(), Integer.parseInt(amountText.getText()), creationDatePicker.getValue());
            if (saveNotification.hasErrors()) {
                showAlert(Alert.AlertType.WARNING, "Updating account unsuccessful", saveNotification.getFormattedErrors());
            } else {
                if (!saveNotification.getResult()) {
                    showAlert(Alert.AlertType.WARNING, "Updating account unsuccessful", "Account was not updated successful, please try again later.");
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Account updated successfully", "");
                    activity.setOperationAndTimeStamp("Account with id " + accountComboBox.getValue().getId() + " of the client with id " + clientId + " updated", Date.valueOf(LocalDate.now()));
                    refreshView();
                }
            }
        }
        else{
            showAlert(Alert.AlertType.WARNING, "Operation unsuccessful", "Please select an account");
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
