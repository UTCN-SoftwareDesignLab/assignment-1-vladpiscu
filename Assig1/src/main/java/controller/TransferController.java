package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Account;
import model.Client;
import service.account.AccountService;

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

    @FXML
    private void transferMoneyHandler(ActionEvent e){

    }

    @FXML
    private void backHandler(ActionEvent e){
        Scene scene = backButton.getScene();
        scene.setRoot(userLoader.getRoot());
    }
}
