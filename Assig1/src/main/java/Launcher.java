import controller.ComponentFactory;
import controller.LoginController;
import controller.UserController;
import controller.ViewFactory;
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
        ViewFactory viewFactory = ViewFactory.instance();
        FXMLLoader loginLoader = viewFactory.getLoginLoader();
        Parent sceneMain = loginLoader.getRoot();
        Scene scene = new Scene(sceneMain);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login screen");
        primaryStage.show();
    }
}
