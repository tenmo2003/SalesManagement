package salesmanagement.salesmanagement;

import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.scenecontrollers.LoginSceneController;
import salesmanagement.salesmanagement.scenecontrollers.MainSceneController;
import salesmanagement.salesmanagement.scenecontrollers.SignupSceneController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @since 1.0
 */
public class AppController {
    private static AppController appController = null;
    private final Stage stage;
    private Scene loginScene;
    private Scene signupScene;
    private Scene mainScene;
    private SQLConnection sqlConnection;
    private Employee user;

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

    void start() {
        final String url = "jdbc:mysql://bjeoeejo9jrw0qoibqmj-mysql.services.clever-cloud.com:3306/bjeoeejo9jrw0qoibqmj";
        final String user = "ugxxkw9sh32lhroy";
        final String password = "QtXTyK7jzCyWztQv80TM";
        sqlConnection = new SQLConnection();
        sqlConnection.logInSQLServer(url, user, password);
    }

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

        //Load login scene.
        FXMLLoader signupFXMLLoader = new FXMLLoader(SalesManagement.class.getResource("signup_scene.fxml"));
        try {
            signupScene = new Scene(signupFXMLLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
       // SignupSceneController signupSceneController = signupFXMLLoader.getController();

        //Load main scene.
        FXMLLoader mainFXMLLoader = new FXMLLoader(SalesManagement.class.getResource("main_scene.fxml"));
        try {
            mainScene = new Scene(mainFXMLLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        MainSceneController mainSceneController = mainFXMLLoader.getController();

        //Set up stage config.
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Sales Management");
        stage.setScene(loginScene);
        stage.getIcons().add(new Image("/app_icon.jpg"));
        stage.show();

        loginEventController(loginSceneController,mainSceneController);

        //Timer waits for user to login successfully and changes scene.
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (loginSceneController.getLoggerID() > 0) {
                    user = new Employee(sqlConnection, loginSceneController.getLoggerID());
                    mainSceneController.setUsernameText(user.getFullName());
                    mainSceneController.uploadNotificationText();

                    stage.setScene(mainScene);
                    stage.setX((Screen.getPrimary().getVisualBounds().getWidth() - stage.getWidth()) / 2);
                    stage.setY((Screen.getPrimary().getVisualBounds().getHeight() - stage.getHeight()) / 2);
                    stop();
                }
            }
        };
        animationTimer.start();

        // Create timer to update the date/time every frame.
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                LocalDateTime dateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = dateTime.format(formatter);
                mainSceneController.setTimeDateText(formattedDateTime);
            }
        };
        // Start the AnimationTimer
        timer.start();
    }
    private void loginEventController(LoginSceneController loginSceneController, MainSceneController mainSceneController) {
        //Create thread for run progressIndicator while load db connection.
        Task<Void> databaseConnectionTask = new Task<>() {
            @Override
            protected Void call() {
                start();
                loginSceneController.setSqlConnection(sqlConnection);
                mainSceneController.setSqlConnection(sqlConnection);
                return null;
            }
        };
        new Thread(databaseConnectionTask).start();
        loginSceneController.setProgressIndicatorStatus(databaseConnectionTask);

    }
}
