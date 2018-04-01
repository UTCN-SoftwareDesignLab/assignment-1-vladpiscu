package controller;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class ViewFactory {
    private FXMLLoader adminLoader;
    private FXMLLoader userLoader;
    private FXMLLoader loginLoader;
    private FXMLLoader accountLoader;
    private FXMLLoader transferLoader;
    private FXMLLoader billsLoader;

    private static ViewFactory instance;

    public static ViewFactory instance() throws IOException {
        if(instance == null){
            instance = new ViewFactory();
        }
        return instance;
    }

    private ViewFactory() throws IOException {
        ComponentFactory componentFactory = ComponentFactory.instance();
        adminLoader = new FXMLLoader(getClass().getResource("/AdminView.fxml"));
        userLoader = new FXMLLoader(getClass().getResource("/UserView.fxml"));
        loginLoader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        accountLoader = new FXMLLoader(getClass().getResource("/AccountView.fxml"));
        transferLoader = new FXMLLoader(getClass().getResource("/TransferView.fxml"));
        billsLoader = new FXMLLoader(getClass().getResource("/BillsView.fxml"));
        LoginController loginController = new LoginController(componentFactory.getAuthenticationService(), adminLoader, userLoader);
        loginLoader.setController(loginController);
        loginLoader.load();
        UserController userController = new UserController(loginLoader, accountLoader, transferLoader, billsLoader,componentFactory.getClientService());
        AdminController adminController = new AdminController(loginLoader, componentFactory.getUserService(), componentFactory.getRightsRolesService());
        AccountController accountController = new AccountController(userLoader, componentFactory.getAccountService());
        TransferController transferController = new TransferController(userLoader, componentFactory.getAccountService());
        BillsController billsController = new BillsController(userLoader, componentFactory.getAccountService());
        userLoader.setController(userController);
        adminLoader.setController(adminController);
        accountLoader.setController(accountController);
        transferLoader.setController(transferController);
        billsLoader.setController(billsController);
        accountLoader.load();
        transferLoader.load();
        billsLoader.load();
        userLoader.load();
        adminLoader.load();
    }

    public FXMLLoader getAdminLoader() {
        return adminLoader;
    }

    public FXMLLoader getUserLoader() {
        return userLoader;
    }

    public FXMLLoader getLoginLoader() {
        return loginLoader;
    }
}
