package controller;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class ViewFactory {
    private FXMLLoader adminLoader;
    private FXMLLoader userLoader;
    private FXMLLoader loginLoader;

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
        LoginController loginController = new LoginController(componentFactory.getAuthenticationService(), adminLoader, userLoader);
        loginLoader.setController(loginController);
        loginLoader.load();
        UserController userController = new UserController(loginLoader);
        AdminController adminController = new AdminController(loginLoader);
        userLoader.setController(userController);
        adminLoader.setController(adminController);
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
