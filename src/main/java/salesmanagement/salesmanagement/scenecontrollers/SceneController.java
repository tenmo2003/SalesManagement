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

    /**
     * This runTask() function is a useful utility method that allows for the execution
     * of a background task while updating the UI with progress and disabling certain
     * UI elements. It is well-designed and easy to use, with the parameters clearly
     * indicating what functionality will be provided by the method. The code is concise
     * and uses JavaFX 's Task class to handle background processing and UI updates.
     * Overall, this function promotes good coding practices and can help improve
     * the user experience in JavaFX applications.
     *
     * @author ThanhAn
     * @since 1.3
     */
    static public void runTask(Runnable taskFunction, Runnable finishFunction, ProgressIndicator progressIndicator, Node bannedArea) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                taskFunction.run();
                return null;
            }
        };
        if (progressIndicator != null) {
            progressIndicator.visibleProperty().bind(task.runningProperty());
        }
        if (bannedArea != null) {
            bannedArea.disableProperty().bind(task.runningProperty());
        }
        if (finishFunction != null) {
            task.setOnSucceeded(workerStateEvent -> {
                finishFunction.run();
            });
        }
        task.setOnFailed(e -> task.getException().printStackTrace());
        new Thread(task).start();
    }

}
