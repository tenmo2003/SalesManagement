package salesmanagement.salesmanagement;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.burningwave.core.assembler.StaticComponentContainer;
import salesmanagement.salesmanagement.SceneController.LoginScene;
import salesmanagement.salesmanagement.SceneController.MainScene;
import salesmanagement.salesmanagement.Utils.ImageController;
import salesmanagement.salesmanagement.Utils.SQLConnection;

import java.io.IOException;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;


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

    public synchronized void run() {
        StaticComponentContainer.JVMInfo.getVersion();
        //Load login scene.
        FXMLLoader loginFXMLLoader = new FXMLLoader(SalesManagement.class.getResource("fxml-scene/login-scene.fxml"));
        try {
            this.loginScene = new Scene(loginFXMLLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        LoginScene loginScene = loginFXMLLoader.getController();

        //Load main scene.
        FXMLLoader mainFXMLLoader = new FXMLLoader(SalesManagement.class.getResource("fxml-scene/main-scene.fxml"));
        try {
            this.mainScene = new Scene(mainFXMLLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        MainScene mainScene = mainFXMLLoader.getController();
        mainScene.setScene(this.mainScene);



        //Set up stage config.

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Sales Management");
        stage.setScene(this.loginScene);
        this.loginScene.setFill(Color.TRANSPARENT);
        stage.getIcons().add(ImageController.getImage("app_icon.png"));
        stage.show();

        var password = "lbfj2nEwsHTelFcZAqLU";
        var user = "udomuzbs3hfulslz";
        var url = "jdbc:mysql://b7kidpocyxjnjhwdw73i-mysql.services.clever-cloud.com:3306/b7kidpocyxjnjhwdw73i";
        sqlConnection = new SQLConnection(url, user, password);
        loginScene.setSqlConnection(sqlConnection, stage);
        mainScene.setSqlConnection(sqlConnection, stage);

        // Set up SQL Connection for scene controllers.
        runTask(() -> {
            sqlConnection.connectServer();
        }, null, loginScene.getProgressIndicator(), loginScene.getLoginPane());
        mainScene.loginDataListener.start();

//        AnimationTimer notifyInternetConnection = new AnimationTimer() {
//            @Override
//            public void handle(long l) {
//                if(sqlConnection.isReconnecting()){
//                    sqlConnection.setReconnecting(false);
//                    Platform.runLater(() -> NotificationSystem.throwNotification(NotificationCode.NETWORK_ERROR, stage));
//                }
//            }
//        };
//        notifyInternetConnection.start();
        // Create timer to update the date/time every frame.
       /* AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                LocalDateTime dateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = dateTime.format(formatter);
                mainScene.setTimeDateText(formattedDateTime);
            }
        };
        timer.start();*/
    }
}