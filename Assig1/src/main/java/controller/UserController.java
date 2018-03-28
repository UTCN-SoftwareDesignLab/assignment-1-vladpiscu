package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.user.AuthenticationService;
import sun.awt.ComponentFactory;

import javax.swing.*;
import java.io.IOException;

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

    public UserController(){
    }

    @FXML
    private void logoutHandler(ActionEvent e){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Logout successful");
        alert.showAndWait();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        LoginController loginController = new LoginController(controller.ComponentFactory.instance().getAuthenticationService());
        loader.setController(loginController);
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Parent sceneMain = null;
        try {
            sceneMain = loader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Scene scene = new Scene(sceneMain);
        stage.setScene(scene);
        stage.setTitle("User screen");
        stage.show();
    }
}
