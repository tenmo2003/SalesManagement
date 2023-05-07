package salesmanagement.salesmanagement.Utils;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.SceneController.NotificationSceneController;

import java.io.IOException;


public class NotificationSystem {
    private static String title;
    private static String content;
    public static Stage previousNotification;

    public static void throwNotification(NotificationCode code, Stage stage) {
        switch (code) {
            case INVALID_INPUTS -> {
                title = "Error notification";
                content = "Please check all input fields again.";
            }
            case INVALID_LOGIN -> {
                title = "Login notification";
                content = "Invalid username or password!";
            }
            case INVALID_EMAIL -> {
                title = "Signup notification";
                content = "Invalid email!";
            }
            case NETWORK_ERROR -> {
                title = "Network notification";
                content = "Please check your network connection again!";
            }
            case INVALID_LOGIN_INFO -> {
                title = "Login notification";
                content = "Invalid username or email!";
            }
            case INVALID_SECURITY_CODE -> {
                title = "Login notification";
                content = "Invalid security code!";
            }
            case SUCCEED_VERIFY_MAIL -> {
                title = "Login notification";
                content = "Verify Mail Successfully!";
            }
            case SUCCEED_RESET_PASSWORD -> {
                title = "Login notification";
                content = "Reset Password Successfully!";
            }
            case SUCCEED_ADD_NEW_EMPLOYEE -> {
                title = "Action Result Notification";
                content = "Added New Employee Successfully!";
            }
            case ERROR_EXPORTING -> {
                title = "Action Result Notification";
                content = "Cannot export data!";
            }
            case SUCCEED_EXPORTING -> {
                title = "Action Result Notification";
                content = "Export data successfully!";
            }
            case SUCCEED_ADD_CUSTOMER -> {
                title = "Action Result Notification";
                content = "Added New Customer Successfully!";
            }
            case SUCCEED_CREATE_ORDER -> {
                title = "Action Result Notification";
                content = "Order Created Successfully!";
            }
            case SUCCEED_EDIT_ORDER -> {
                title = "Action Result Notification";
                content = "Order Saved Successfully!";
            }
            case SUCCEED_DELETE_ORDER -> {
                title = "Action Result Notification";
                content = "Order Removed Successfully!";
            }
            case SUCCEED_CREATE_PRODUCT -> {
                title = "Action Result Notification";
                content = "Product Added Successfully!";
            }
            case SUCCEED_EDIT_PRODUCT -> {
                title = "Action Result Notification";
                content = "Product Saved Successfully!";
            }
            case SUCCEED_DELETE_PRODUCT -> {
                title = "Action Result Notification";
                content = "Product Removed Successfully!";
            }
            case SUCCEED_DELETE_CUSTOMER -> {
                title = "Action Result Notification";
                content = "Customer Removed Successfully!";
            }
            case SUCCEED_SAVE_INFO -> {
                title = "Action Result Notification";
                content = "Saved Successfully!";
            }
            case NOT_AUTHORIZED -> {
                title = "Action Result Notification";
                content = "You do not have permission to perform this action!";
            }
        }

        Scene notificationScene = null;
        FXMLLoader loginFXMLLoader = new FXMLLoader(SalesManagement.class.getResource("fxml-scene/notification-scene.fxml"));
        try {
            notificationScene = new Scene(loginFXMLLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        notificationScene.setFill(Color.TRANSPARENT);
        NotificationSceneController notificationSceneController = loginFXMLLoader.getController();
        notificationSceneController.init(title, content, code);

        Stage notificationStage = new Stage();
        notificationStage.initStyle(StageStyle.TRANSPARENT);
        notificationStage.initOwner(stage);

        // Hide notification after 1.5s.
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(1.5),
                event -> {
                    if (notificationStage.isShowing()) {
                        double endX = Screen.getPrimary().getVisualBounds().getWidth();
                        double startX = endX - notificationStage.getWidth();
                        long duration = 500000000L;

                        AnimationTimer animationTimer = new AnimationTimer() {
                            long startTime = 0;
                            final double distance = endX - startX;
                            final double speed = distance / (double) duration;

                            @Override
                            public void start() {
                                super.start();
                                startTime = System.nanoTime();
                            }

                            @Override
                            public void handle(long now) {
                                long elapsed = now - startTime;
                                double newX = startX + speed * elapsed;
                                notificationStage.setX(newX);
                                if (newX >= endX) {
                                    notificationStage.close();
                                    stop();
                                }
                            }
                        };
                        animationTimer.start();
                    }
                }));

        notificationStage.setScene(notificationScene);
        notificationStage.setX(Screen.getPrimary().getVisualBounds().getWidth());
        notificationStage.setY(Screen.getPrimary().getVisualBounds().getHeight() - 120);
        notificationStage.show();

        // Close previous notification if alive.
        if (previousNotification != null) {
            if (previousNotification.isShowing()) {
               previousNotification.close();
            }
            Timeline wait = new Timeline(new KeyFrame(
                    Duration.seconds(0.2),
                    event -> notificationSceneController.show()));
            wait.play();
        } else notificationSceneController.show();


        // Appearing animation.
        double startX = Screen.getPrimary().getVisualBounds().getWidth();
        double endX = startX - notificationStage.getWidth();
        long duration = 500000000L; // 0.5s

        AnimationTimer animationTimer = new AnimationTimer() {
            long startTime = 0;
            final double distance = endX - startX;
            final double speed = distance / (double) duration;

            @Override
            public void start() {
                super.start();
                startTime = System.nanoTime();
            }

            @Override
            public void handle(long now) {
                long elapsed = now - startTime;
                double newX = startX + speed * elapsed;
                notificationStage.setX(newX);
                if (newX <= endX) {
                    stop();
                }
            }
        };
        animationTimer.start();

        previousNotification = notificationStage;
        timeline.play();
    }
}