package salesmanagement.salesmanagement.scenecontrollers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import salesmanagement.salesmanagement.ErrorCode;
import salesmanagement.salesmanagement.InvalidInput;
import salesmanagement.salesmanagement.SalesManagement;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginSceneController extends SceneController implements Initializable {
    @FXML
    JFXTextField username;
    @FXML
    JFXPasswordField password;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
                String query = "select accountID, username, password from accounts where username = '" + username + "' and password = '" + password + "'";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                if (resultSet.next()) {
                    loggerID = resultSet.getInt("accountID");
                    return true;
                }
                return false;
            }
        };

        checkAccountTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                InvalidInput.throwNotification(ErrorCode.INVALID_LOGIN, stage);
            }
        });

        new Thread(checkAccountTask).start();
        setProgressIndicatorStatus(checkAccountTask, loginPane);
    }

    public void setProgressIndicatorStatus(Task<?> databaseConnectionTask) {
        super.setProgressIndicatorStatus(databaseConnectionTask, loginPane);
    }
}
