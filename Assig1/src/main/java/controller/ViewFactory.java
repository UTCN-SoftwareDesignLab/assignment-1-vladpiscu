package controller;

import javafx.fxml.FXMLLoader;
import model.Activity;
import service.ReportWriter;

import java.io.IOException;

public class ViewFactory {
    private FXMLLoader adminLoader;
    private FXMLLoader userLoader;
    private FXMLLoader loginLoader;
    private FXMLLoader accountLoader;
    private FXMLLoader transferLoader;
    private FXMLLoader billsLoader;
    private FXMLLoader reportLoader;

    private static ViewFactory instance;

    public static ViewFactory instance() throws IOException {
        if(instance == null){
            instance = new ViewFactory();
        }
        return instance;
    }

    private ViewFactory() throws IOException {
        ComponentFactory componentFactory = ComponentFactory.instance();
        Activity activity = new Activity();
        adminLoader = new FXMLLoader(getClass().getResource("/AdminView.fxml"));
        userLoader = new FXMLLoader(getClass().getResource("/UserView.fxml"));
        loginLoader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        accountLoader = new FXMLLoader(getClass().getResource("/AccountView.fxml"));
        transferLoader = new FXMLLoader(getClass().getResource("/TransferView.fxml"));
        billsLoader = new FXMLLoader(getClass().getResource("/BillsView.fxml"));
        reportLoader = new FXMLLoader(getClass().getResource("/ReportView.fxml"));
        LoginController loginController = new LoginController(componentFactory.getAuthenticationService(), adminLoader, userLoader, activity);
        loginLoader.setController(loginController);
        loginLoader.load();
        UserController userController = new UserController(loginLoader, accountLoader, transferLoader, billsLoader,componentFactory.getClientService(), activity);
        AdminController adminController = new AdminController(loginLoader, reportLoader, componentFactory.getUserService(), componentFactory.getRightsRolesService(), activity);
        AccountController accountController = new AccountController(userLoader, componentFactory.getAccountService(), activity);
        TransferController transferController = new TransferController(userLoader, componentFactory.getAccountService(), activity);
        BillsController billsController = new BillsController(userLoader, componentFactory.getAccountService(), activity);
        ReportController reportController = new ReportController(componentFactory.getActivityService(), activity, adminLoader, new ReportWriter());
        userLoader.setController(userController);
        adminLoader.setController(adminController);
        accountLoader.setController(accountController);
        transferLoader.setController(transferController);
        billsLoader.setController(billsController);
        reportLoader.setController(reportController);
        accountLoader.load();
        transferLoader.load();
        billsLoader.load();
        userLoader.load();
        adminLoader.load();
        reportLoader.load();
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
