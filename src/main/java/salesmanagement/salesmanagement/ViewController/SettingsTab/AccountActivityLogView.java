package salesmanagement.salesmanagement.ViewController.SettingsTab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import salesmanagement.salesmanagement.SalesComponent.Action;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.Utils.Utils;
import salesmanagement.salesmanagement.ViewController.TabView;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class AccountActivityLogView extends TabView implements SettingsTab {
    @FXML
    private TableView<Action> actionsTable;
    @FXML
    private TableColumn<?, ?> descriptionColumn;
    @FXML
    private TableColumn<Action, LocalDateTime> timeColumn;
    @FXML
    private TableColumn<Action, Integer> actionIDColumn;
    @FXML
    private TableColumn<Action, Label> resultColumn;
    @FXML
    private TableColumn<Action, String> componentModifiedIDColumn;
    @FXML
    private ProgressIndicator loadingIndicator;
    @FXML
    private Label userIDLabel;

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
        componentModifiedIDColumn.setCellValueFactory(new PropertyValueFactory<>("componentModifiedID"));

        Utils.adjustTableColumnWidths(actionsTable, Arrays.asList(0.1, 0.2, 0.3, 0.2, 0.2));

        resultColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Label item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);

                } else {
                    setGraphic(item);
                    setAlignment(Pos.CENTER);

                }
            }
        });

        Utils.setColumnAlignmentCenter(Arrays.asList(componentModifiedIDColumn));
        Utils.setColumnAlignmentCenter(Arrays.asList(resultColumn));


        actionsTable.getSortOrder().add(actionIDColumn);
        actionsTable.sort();
    }

    @Override
    protected void figureShow() {
        super.figureShow();

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
            accountActivityLogFilterView.setFilteredList(new FilteredList<>(actionObservableList));
            sortedAndFilteredActions = new SortedList<>(accountActivityLogFilterView.getFilteredList());
            sortedAndFilteredActions.comparatorProperty().bind(actionsTable.comparatorProperty());
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
