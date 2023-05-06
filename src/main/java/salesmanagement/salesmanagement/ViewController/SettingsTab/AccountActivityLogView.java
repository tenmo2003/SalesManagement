package salesmanagement.salesmanagement.ViewController.SettingsTab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.tableview2.FilteredTableView;
import salesmanagement.salesmanagement.SalesComponent.Action;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.ViewController.TabView;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class AccountActivityLogView extends TabView implements SettingsTab {
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
    @FXML
    private Label userIDLabel;

    boolean actionsTableConfigured = false;
    Employee user;
    AccountActivityLogFilterView accountActivityLogFilterView;
    SortedList<Action> sortedAndFilteredActions;

    public AccountActivityLogView setUser(Employee user) {
        this.user = user;
        return this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/settings-tab/account-activity-log-filter-view.fxml"));
            loader.load();
            accountActivityLogFilterView = loader.getController();
            root.getChildren().add(accountActivityLogFilterView.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        actionIDColumn.setCellValueFactory(new PropertyValueFactory<>("actionID"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
    }

    @Override
    protected void figureShow() {
        super.figureShow();
        if (!actionsTableConfigured) {
            actionsTableConfigured = true;
            double actionsTableWidth = actionsTable.getWidth();
            descriptionColumn.setMinWidth(0.5 * actionsTableWidth);
            timeColumn.setMinWidth(0.2 * actionsTableWidth);
            actionIDColumn.setMinWidth(0.1 * actionsTableWidth);
            resultColumn.setMinWidth(0.2 * actionsTableWidth);
        }
        userIDLabel.setText(String.format("Account %d", user.getEmployeeNumber()));
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
            accountActivityLogFilterView.setActionFilteredList(new FilteredList<>(actionObservableList));
            sortedAndFilteredActions = new SortedList<>(accountActivityLogFilterView.getActionFilteredList());
        }, () -> {
            actionsTable.setItems(sortedAndFilteredActions);
            isShowing = false;
        }, loadingIndicator, null);
    }

    @FXML
    public void addFilter() {
        accountActivityLogFilterView.show();
    }
}
