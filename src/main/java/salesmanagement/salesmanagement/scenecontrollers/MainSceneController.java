package salesmanagement.salesmanagement.scenecontrollers;

import com.jfoenix.controls.JFXButton;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import salesmanagement.salesmanagement.SalesComponent.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainSceneController extends SceneController {
    @FXML
    Text usernameText;

    public void setUsernameText(String usernameText) {
        this.usernameText.setText(usernameText);
    }

    @FXML
    TabPane tabPane;
    @FXML
    private Tab employeesOperationTab;
    @FXML
    private Tab createOrderTab;
    @FXML
    private Tab homeTab;
    @FXML
    private Tab settingTab;
    @FXML
    private Tab productsOperationTab;

    @FXML
    void goToCreateOrderTab() {
        tabPane.getSelectionModel().select(createOrderTab);
        statusIcon.setImage(new Image("/create_order_icon.png"));
    }

    @FXML
    void goToEmployeesOperationTab() {
        tabPane.getSelectionModel().select(employeesOperationTab);
    }

    @FXML
    void goToHomeTab() {
        tabPane.getSelectionModel().select(homeTab);
        statusIcon.setImage(new Image("/home_icon.png"));
    }

    @FXML
    void goToProductsOperationTab() {
        tabPane.getSelectionModel().select(productsOperationTab);
    }

    @FXML
    void goToSettingTab() {
        tabPane.getSelectionModel().select(settingTab);
    }

    @FXML
    Text author;
    @FXML
    Text notificationTitle;
    @FXML
    Text publishedDate;
    @FXML
    Text content;
    @FXML
    HBox contentBox;

    private void uploadNotificationText() {
        runTask(() -> {
            String query = "SELECT * FROM notifications  " +
                    "WHERE notificationID = (SELECT MAX(notificationID) " +
                    "FROM notifications)";
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            try {
                if (resultSet.next()) {
                    content.wrappingWidthProperty().bind(contentBox.widthProperty().multiply(0.9));
                    content.setText(resultSet.getString("content"));
                    notificationTitle.setText(resultSet.getString("title"));
                    int authorID = Integer.parseInt(resultSet.getString("employeeNumber"));
                    Employee employee = new Employee(authorID);
                    author.setText("\t\tPosted by " + employee.getFullName() + ".");
                    publishedDate.setText("\t\tPublished " + resultSet.getString("publishedDate") + ".");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, progressIndicator, homeTab.getTabPane());

    }

    @FXML
    ImageView statusIcon;

    /**
     * Handle employee management tab.
     */
    @FXML
    TableView<Employee> employeeTable;
    @FXML
    private TableColumn<?, ?> email;
    @FXML
    private TableColumn<Employee, Integer> employeeNumber;
    @FXML
    private TableColumn<?, ?> firstName;
    @FXML
    private TableColumn<?, ?> jobTitle;
    @FXML
    private TableColumn<?, ?> lastName;
    @FXML
    private TableColumn<?, ?> officeCode;
    @FXML
    private TableColumn<?, ?> operation;
    @FXML
    private TableColumn<?, ?> reportsTo;
    @FXML
    AnchorPane employeeOperationPane;
    ArrayList<Employee> employees;

    @FXML
    void selectEmployeesOperationTab() {
        employeeTable.setSelectionModel(null);
        Task<Void> getEmployeeRecordsTask = new Task<>() {
            @Override
            protected Void call() {
                String query = "select * from employees";
                employees = new ArrayList<>();
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                try {
                    while (resultSet.next()) {
                        employees.add(new Employee(resultSet, MainSceneController.this));
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                employeeNumber.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
                lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                email.setCellValueFactory(new PropertyValueFactory<>("email"));
                officeCode.setCellValueFactory(new PropertyValueFactory<>("officeCode"));
                reportsTo.setCellValueFactory(new PropertyValueFactory<>("reportsTo"));
                jobTitle.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
                operation.setCellValueFactory(new PropertyValueFactory<>("operation"));

                ObservableList<Employee> employeeList = FXCollections.observableArrayList(employees);
                employeeTable.setItems(employeeList);
                return null;
            }
        };
        new Thread(getEmployeeRecordsTask).start();
        setProgressIndicatorStatus(getEmployeeRecordsTask, employeeOperationPane);
    }


    @FXML
    SplitPane firstSplitPane;
    @FXML
    SplitPane secondSplitPane;
    @FXML
    SplitPane thirdSplitPane;
    @FXML
    VBox employeeTabBox;
    @FXML
    HBox statusIconBox;

    public void initialSetup() {

        user = new Employee(sqlConnection, loggerID);

        usernameText.setText(user.getFullName());

        firstSplitPane.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
        ((AnchorPane) firstSplitPane.getItems().get(0)).setMinHeight(0.05 * Screen.getPrimary().getVisualBounds().getHeight());
        ((AnchorPane) firstSplitPane.getItems().get(1)).setMinHeight(0.95 * Screen.getPrimary().getVisualBounds().getHeight());
        secondSplitPane.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());
        ((AnchorPane) secondSplitPane.getItems().get(0)).setMinWidth(0.1667 * Screen.getPrimary().getVisualBounds().getWidth());
        ((AnchorPane) secondSplitPane.getItems().get(1)).setMinWidth(0.8333 * Screen.getPrimary().getVisualBounds().getWidth());
        thirdSplitPane.setMaxWidth(0.8333 * Screen.getPrimary().getVisualBounds().getWidth());
        ((VBox) thirdSplitPane.getItems().get(0)).setMinWidth(0.75 * thirdSplitPane.getMaxWidth());
        ((VBox) thirdSplitPane.getItems().get(1)).setMinWidth(0.25 * thirdSplitPane.getMaxWidth());

        statusIconBox.setPrefWidth(0.1667 * Screen.getPrimary().getVisualBounds().getWidth());

        double tableWidth = employeeTabBox.getWidth() * 0.95;
        System.out.println(employeeTabBox.getWidth());
        employeeTable.setMaxWidth(tableWidth);
        employeeNumber.setMinWidth(0.1 * tableWidth);
        firstName.setMinWidth(0.125 * tableWidth);
        lastName.setMinWidth(0.125 * tableWidth);
        email.setMinWidth(0.15 * tableWidth);
        officeCode.setMinWidth(0.125 * tableWidth);
        reportsTo.setMinWidth(0.125 * tableWidth);
        jobTitle.setMinWidth(0.125 * tableWidth);
        operation.setMinWidth(0.125 * tableWidth);
    }

    private Employee user;

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public static boolean haveJustOpened = false;
    public static int loggerID = -1;
    public static boolean haveChangeInEmployeesTab = false;
    public static boolean haveChangeInOrdersTab = false;
    public static boolean haveChangeInHomeTab = false;
    public static boolean haveChangeInSettingTab = false;
    public static boolean haveChangeInProductTab = false;
    public AnimationTimer loginDataListener = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if (MainSceneController.loggerID > 0) {
                reloadFlagListener.start();
                stop();
            }
        }
    };
    private AnimationTimer reloadFlagListener = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if (haveJustOpened) {
                stage.setScene(MainSceneController.this.scene);
                stage.hide();
                initialSetup();
                stage.setX(0);
                stage.setY(0);
                stage.show();
                uploadNotificationText();
                haveJustOpened = false;
            }
            if (haveChangeInEmployeesTab) {
                selectEmployeesOperationTab();
                haveChangeInEmployeesTab = false;
            }
            if (haveChangeInHomeTab) {

            }
        }
    };

}
