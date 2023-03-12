package salesmanagement.salesmanagement.scenecontrollers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginSceneController extends SceneController implements Initializable {
    @FXML
    JFXTextField username;
    @FXML
    JFXPasswordField password;
    @FXML
    ProgressIndicator progressIndicator;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setProgressIndicatorStatus(Task<?> databaseConnectionTask) {
        progressIndicator.visibleProperty().bind(databaseConnectionTask.runningProperty());
        loginPane.disableProperty().bind(databaseConnectionTask.runningProperty());
    }


    int loggerID = -1;

    public int getLoggerID() {
        return loggerID;
    }

    @FXML
    AnchorPane loginPane;

    @FXML
    public void checkAccount(Event event) {
        if(event instanceof KeyEvent)
            if(((KeyEvent)event).getCode() != KeyCode.ENTER) return;
        String password = this.password.getCharacters().toString();
        String username = this.username.getText();

        Task<Boolean> checkAccountTask = new Task<>() {
            @Override
            protected Boolean call() throws SQLException {
                String query = "select employeeNumber, username, password from accounts where username = '" + username + "' and password = '" + password + "'";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                if (resultSet.next()) {
                    loggerID = resultSet.getInt("employeeNumber");
                    return true;
                }
                return false;
            }
        };

        checkAccountTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                Notifications notificationBuilder = Notifications.create()
                        .title("Login notification")
                        .text("Invalid username or password!")
                        .owner(this.password.getScene().getWindow())
                        .position(Pos.TOP_CENTER).hideAfter(Duration.seconds(1));
                notificationBuilder.show();
            }
        });

        new Thread(checkAccountTask).start();
        setProgressIndicatorStatus(checkAccountTask);

    }
}
