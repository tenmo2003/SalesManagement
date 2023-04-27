package salesmanagement.salesmanagement.ViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import org.controlsfx.control.tableview2.FilteredTableView;
import salesmanagement.salesmanagement.SalesComponent.Action;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesManagement;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class AccountActivityLogController extends ViewController {
    @FXML
    private FilteredTableView<Action> actionsTable;
    @FXML
    private TableColumn<?, ?> descriptionColumn;
    @FXML
    private TableColumn<?, ?> timeColumn;
    @FXML
    private TableColumn<?, ?> actionIDColumn;
    @FXML
    private TableColumn<?, ?> resultColumn;
    @FXML
    private ProgressIndicator loadingIndicator;

    boolean actionsTableConfigured = false;
    Employee user;
    AccountActivityLogFilterViewController accountActivityLogFilterViewController;
    SortedList<Action> sortedAndFilteredActions;

    public void setUser(Employee user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/account-activity-log-filter-view.fxml"));
            loader.load();
            accountActivityLogFilterViewController = loader.getController();
            root.getChildren().add(accountActivityLogFilterViewController.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        actionIDColumn.setCellValueFactory(new PropertyValueFactory<>("actionID"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
    }

    @Override
    public void show() {
        super.show();
        if (!actionsTableConfigured) {
            actionsTableConfigured = true;
            double actionsTableWidth = actionsTable.getWidth();
            descriptionColumn.setMinWidth(0.5 * actionsTableWidth);
            timeColumn.setMinWidth(0.2 * actionsTableWidth);
            actionIDColumn.setMinWidth(0.1 * actionsTableWidth);
            resultColumn.setMinWidth(0.2 * actionsTableWidth);
        }
        List<Action> actions = new ArrayList<>();
        runTask(() -> {
            String query = "SELECT * FROM activityLog WHERE userID = " + user.getEmployeeNumber();
            ResultSet actionsSet = sqlConnection.getDataQuery(query);
            try {
                while (actionsSet.next()) {
                    actions.add(new Action(actionsSet));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            ObservableList<Action> actionObservableList = FXCollections.observableArrayList(actions);
            accountActivityLogFilterViewController.setActionFilteredList(new FilteredList<>(actionObservableList));
            sortedAndFilteredActions = new SortedList<>(accountActivityLogFilterViewController.getActionFilteredList());
        }, () -> {
            actionsTable.setItems(sortedAndFilteredActions);
        }, loadingIndicator, null);
    }

    @FXML
    public void addFilter() {
        accountActivityLogFilterViewController.show();
    }
}
