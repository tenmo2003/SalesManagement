package salesmanagement.salesmanagement;

import javafx.application.Application;
import javafx.stage.Stage;

public class SalesManagement extends Application {
    @Override
    public void start(Stage stage) {
        AppController.getAppController(stage).run();
    }
    public static void main(String[] args) {
        launch();
    }
}