package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import model.Account;
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

    public AccountController(FXMLLoader userLoader, AccountService accountService) {
        this.userLoader = userLoader;
        this.accountService = accountService;
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
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Saving account unsuccessful");
                alert.setContentText(saveNotification.getFormattedErrors());
                alert.showAndWait();
            } else {
                if (!saveNotification.getResult()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Saving account unsuccessful");
                    alert.setContentText("Account was not added successful, please try again later.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Account added successfully");
                    refreshView();
                    alert.showAndWait();
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Operation unsuccessful");
            alert.setContentText("Please select a type for the account");
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteAccountHandler(ActionEvent e) {
        if (accountComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Delete unsuccessful");
            alert.setContentText("Please select an account.");
            alert.showAndWait();
        }
        else {
            boolean isSuccessful = accountService.removeAccount(accountComboBox.getValue().getId());
            if(isSuccessful){
                refreshView();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Account deleted successfully");
                refreshView();
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Delete unsuccessful");
                alert.setContentText("Account was not deleted successful, please try again later.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void updateAccountHandler(ActionEvent e){
        if(accountComboBox.getValue() != null) {
            Notification<Boolean> saveNotification = accountService
                    .updateAccount(accountComboBox.getValue().getId(), typeComboBox.getValue(), Integer.parseInt(amountText.getText()), creationDatePicker.getValue());
            if (saveNotification.hasErrors()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Updating account unsuccessful");
                alert.setContentText(saveNotification.getFormattedErrors());
                alert.showAndWait();
            } else {
                if (!saveNotification.getResult()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Updating account unsuccessful");
                    alert.setContentText("Account was not updated successful, please try again later.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Account updated successfully");
                    refreshView();
                    alert.showAndWait();
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Operation unsuccessful");
            alert.setContentText("Please select an account");
            alert.showAndWait();
        }
    }

    @FXML
    private void backHandler(ActionEvent e){
        Scene scene = backButton.getScene();
        scene.setRoot(userLoader.getRoot());
    }


}
