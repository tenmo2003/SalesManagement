package salesmanagement.salesmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class SalesManagement extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        AppController.getAppController(stage).run();
    }
    public static void main(String[] args) {
        launch();
    }
}