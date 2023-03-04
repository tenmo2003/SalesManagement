package salesmanagement.salesmanagement.scenecontrollers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import salesmanagement.salesmanagement.SQLConnection;

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
    public void setProgressIndicatorStatus(Task<Void> databaseConnectionTask) {
        progressIndicator.visibleProperty().bind(databaseConnectionTask.runningProperty());
        username.disableProperty().bind(databaseConnectionTask.runningProperty());
        password.disableProperty().bind(databaseConnectionTask.runningProperty());
    }


    int loggerID = -1;

    public int getLoggerID() {
        return loggerID;
    }

    @FXML
    public void checkAccount() {
        String password = this.password.getCharacters().toString();
        String username = this.username.getText();

        Task<Void> checkAccountTask = new Task<>() {
            @Override
            protected Void call() throws SQLException {
                String query = "select employeeNumber, username, password from accounts where username = '" + username + "' and password = '" + password+"'";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                if(resultSet.next()) {
                    loggerID =  resultSet.getInt("employeeNumber");
                }
                return null;
            }
        };
        new Thread(checkAccountTask).start();
        setProgressIndicatorStatus(checkAccountTask);
    }
}
