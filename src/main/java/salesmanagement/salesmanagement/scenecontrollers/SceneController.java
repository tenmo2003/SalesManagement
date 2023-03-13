package salesmanagement.salesmanagement.scenecontrollers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import salesmanagement.salesmanagement.SQLConnection;

public class SceneController {
    SQLConnection sqlConnection;
    Stage stage;

    public void setSqlConnection(SQLConnection sqlConnection, Stage stage) {
        this.sqlConnection = sqlConnection;
        this.stage = stage;
    }

    @FXML
    protected void closeStage() {
        System.exit(0);
    }
    @FXML
    ProgressIndicator progressIndicator;
    public void setProgressIndicatorStatus(Task<?> databaseConnectionTask, Node bannedArea) {
        progressIndicator.visibleProperty().bind(databaseConnectionTask.runningProperty());
        bannedArea.disableProperty().bind(databaseConnectionTask.runningProperty());
    }

}
