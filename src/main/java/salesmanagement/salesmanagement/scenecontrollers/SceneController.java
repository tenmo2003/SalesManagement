package salesmanagement.salesmanagement.scenecontrollers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
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

    public Stage getStage() {
        return stage;
    }

    @FXML
    protected void closeStage() {
        if (sqlConnection != null)
            sqlConnection.addClosingWork();
        System.exit(0);
    }
    protected Scene scene;

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @FXML
    ProgressIndicator progressIndicator;

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public void setProgressIndicatorStatus(Task<?> databaseConnectionTask, Node bannedArea) {
        progressIndicator.visibleProperty().bind(databaseConnectionTask.runningProperty());
        bannedArea.disableProperty().bind(databaseConnectionTask.runningProperty());
    }
    public void showProgressIndicator(Node bannedPane) {
        progressIndicator.setVisible(true);
        bannedPane.setDisable(true);
    }

    public void hideProgressIndicator(Node bannedPane) {
        progressIndicator.setVisible(false);
        bannedPane.setDisable(false);
    }
    static public void runTask(Runnable taskFunction, ProgressIndicator progressIndicator, Node bannedArea) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                taskFunction.run();
                return null;
            }
        };
        progressIndicator.visibleProperty().bind(task.runningProperty());
        bannedArea.disableProperty().bind(task.runningProperty());
        task.setOnSucceeded(workerStateEvent -> {
            //do sth;
        });
        new Thread(task).start();
    }
}
