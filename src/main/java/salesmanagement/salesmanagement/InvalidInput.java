package salesmanagement.salesmanagement;

import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class InvalidInput {
    private static String title;
    private static String content;

    public static void throwNotification(ErrorCode code, Stage stage) {
        switch (code) {
            case INVALID_LOGIN:
                title = "Login notification";
                content = "Invalid username or password!";
                break;
            case INVALID_EMAIL:
                title = "Signup notification";
                content = "Invalid email!";
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
