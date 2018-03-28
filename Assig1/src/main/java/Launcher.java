import controller.ComponentFactory;
import controller.LoginController;
import controller.UserController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException{
        ComponentFactory componentFactory = ComponentFactory.instance();
        UserController userController = new UserController();
        LoginController loginController = new LoginController(componentFactory.getAuthenticationService());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        loader.setController(loginController);
        Parent sceneMain = loader.load();
        Scene scene = new Scene(sceneMain);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login screen");
        primaryStage.show();
    }
}
