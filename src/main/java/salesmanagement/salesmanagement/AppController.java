package salesmanagement.salesmanagement;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import salesmanagement.salesmanagement.scenecontrollers.LoginSceneController;
import salesmanagement.salesmanagement.scenecontrollers.MainSceneController;
import salesmanagement.salesmanagement.scenecontrollers.SceneController;

import java.io.IOException;

/**
 * @since 1.0
 */
public class AppController {
    private static AppController appController = null;
    private final Stage stage;
    private Scene loginScene;
    private Scene mainScene;
    private SQLConnection sqlConnection;

    private AppController(Stage stage) {
        this.stage = stage;
    }

    public static AppController getAppController(Stage stage) {
        synchronized (AppController.class) {
            if (appController == null) {
                appController = new AppController(stage);
            }
        }
        return appController;
    }

    private final String url = "jdbc:mysql://bjeoeejo9jrw0qoibqmj-mysql.services.clever-cloud.com:3306/bjeoeejo9jrw0qoibqmj";
    private final String user = "ugxxkw9sh32lhroy";
    private final String password = "QtXTyK7jzCyWztQv80TM";

    public synchronized void run() {
        //Load login scene.
        FXMLLoader loginFXMLLoader = new FXMLLoader(SalesManagement.class.getResource("login_scene.fxml"));
        try {
            loginScene = new Scene(loginFXMLLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        LoginSceneController loginSceneController = loginFXMLLoader.getController();

        //Load main scene.
        FXMLLoader mainFXMLLoader = new FXMLLoader(SalesManagement.class.getResource("main_scene.fxml"));
        try {
            mainScene = new Scene(mainFXMLLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        MainSceneController mainSceneController = mainFXMLLoader.getController();
        mainSceneController.setScene(mainScene);

        ((AnchorPane) mainScene.getRoot()).setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        ((AnchorPane) mainScene.getRoot()).setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());

        //Set up stage config.
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Sales Management");
        stage.setScene(loginScene);
        stage.getIcons().add(new Image("/app_icon.png"));
        stage.show();

        // Set up SQL Connection for scene controllers.
        SceneController.runTask(() -> {
            sqlConnection = new SQLConnection();
            sqlConnection.logInSQLServer(url, user, password);
            loginSceneController.setSqlConnection(sqlConnection, stage);
            mainSceneController.setSqlConnection(sqlConnection, stage);
        }, loginSceneController.getProgressIndicator(), loginSceneController.getLoginPane());

        // Login event.
        mainSceneController.loginDataListener.start();

        // Create timer to update the date/time every frame.
       /* AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                LocalDateTime dateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = dateTime.format(formatter);
                mainSceneController.setTimeDateText(formattedDateTime);
            }
        };
        timer.start();*/
    }
}