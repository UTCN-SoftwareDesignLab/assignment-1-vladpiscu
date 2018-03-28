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
    private TableColumn firstColumn;
    @FXML
    private Button logoutButton;
    @FXML
    private Button updateUserButton;
    @FXML
    private Button deleteUserButton;
    @FXML
    private Button addUserButton;
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
    private Button reportButton;
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
    private TextField userIdText;
    @FXML
    private TextField usernameText;
    @FXML
    private TextField passwordText;
    @FXML
    private ComboBox roleComboBox;
    public UserController(){
    }

    public void setPageForRole(String role){
        boolean userType = false;
        if(role.compareTo(ADMINISTRATOR) == 0)
            userType = true;

        //ADMINISTRATOR
        updateUserButton.setVisible(userType);
        deleteUserButton.setVisible(userType);
        addUserButton.setVisible(userType);
        userIdText.setVisible(userType);
        usernameText.setVisible(userType);
        passwordText.setVisible(userType);
        roleComboBox.setVisible(userType);
        reportButton.setVisible(userType);

        //EMPLOYEE
        updateClientButton.setVisible(!userType);
        addClientButton.setVisible(!userType);
        accountsButton.setVisible(!userType);
        utilityBillsButton.setVisible(!userType);
        transferMoneyButton.setVisible(!userType);
        clientIdText.setVisible(!userType);
        clientNameText.setVisible(!userType);
        cardNbText.setVisible((!userType));
        pncText.setVisible(!userType);
        addressText.setVisible(!userType);
    }

    @FXML
    private void addUserHandler(ActionEvent e){
        firstColumn.setVisible(false);
        updateUserButton.setVisible(false);
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
