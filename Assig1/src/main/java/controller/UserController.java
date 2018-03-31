package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Client;
import model.User;
import service.user.AuthenticationService;
import service.user.UserService;
import sun.awt.ComponentFactory;

import javax.swing.*;
import java.io.IOException;
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

    public UserController(FXMLLoader loginLoader){
        this.loginLoader = loginLoader;
    }

    @FXML
    private void clientSelectionHandler(ActionEvent e){

    }

    @FXML
    private void addClientHandler(ActionEvent e){

    }

    @FXML
    private void updateClientHandler(ActionEvent e){

    }

    @FXML
    private void accountsHandler(ActionEvent e){

    }

    @FXML
    private void utilityBillsHandler(ActionEvent e){

    }

    @FXML
    private void transferMoneyHandler(ActionEvent e){

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
