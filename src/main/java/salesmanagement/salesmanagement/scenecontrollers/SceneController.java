package salesmanagement.salesmanagement.scenecontrollers;

import javafx.fxml.FXML;
import salesmanagement.salesmanagement.SQLConnection;

public class SceneController {
    SQLConnection sqlConnection;

    public void setSqlConnection(SQLConnection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }

    @FXML
    protected void closeStage() {
        System.exit(0);
    }
}
