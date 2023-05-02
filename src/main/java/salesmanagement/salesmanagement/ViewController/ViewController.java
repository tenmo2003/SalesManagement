package salesmanagement.salesmanagement.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import salesmanagement.salesmanagement.Utils.SQLConnection;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class ViewController implements Initializable {
    @FXML
    protected StackPane root;

    protected static SQLConnection sqlConnection;
    protected Stage stage;
    protected boolean isShowing = false;

    protected ViewController parentController;
    protected ProgressIndicator loadingIndicator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.setVisible(false);
    }

    public static void setSqlConnection(SQLConnection sqlConnection) {
        if (ViewController.sqlConnection == null)
            ViewController.sqlConnection = sqlConnection;
    }

    public void setParentController(ViewController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void close() {
        root.setVisible(false);
    }

    @FXML
    public void show() {
        if (stage == null) stage = (Stage) root.getScene().getWindow();
        root.setVisible(true);
    }

    public StackPane getRoot() {
        return root;
    }
}
