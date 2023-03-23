package salesmanagement.salesmanagement;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class NotificationSystem {
    private static String title;
    private static String content;

    public static void throwNotification(NotificationCode code, Stage stage) {
        switch (code) {
            case INVALID_LOGIN:
                title = "Login notification";
                content = "Invalid username or password!";
                break;
            case INVALID_EMAIL:
                title = "Signup notification";
                content = "Invalid email!";
                break;
            case NETWORK_ERROR:
                title = "Network notification";
                content = "Please check your network connection again!";
                break;
        }
        Notifications notificationBuilder = Notifications.create()
                .title(title)
                .text(content)
                .owner(stage)
                .position(Pos.TOP_CENTER).hideAfter(Duration.seconds(1));
        notificationBuilder.show();
    }
}