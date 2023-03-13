package salesmanagement.salesmanagement.scenecontrollers;

import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import salesmanagement.salesmanagement.SalesComponent.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainSceneController extends SceneController  {
    @FXML
    Text usernameText;
    @FXML
    Text timeDateText;

    public void setUsernameText(String usernameText) {
        this.usernameText.setText(usernameText);
    }

    public void setTimeDateText(String timeDateText) {
        this.timeDateText.setText(timeDateText);
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
    Text postTime;
    @FXML
    JFXTextArea contents;

    public void uploadNotificationText() {
        String query = "SELECT * FROM notifications  " +
                "WHERE notificationID = (SELECT MAX(notificationID) " +
                "FROM notifications)";
        ResultSet resultSet = sqlConnection.getDataQuery(query);
        try {
            if (resultSet.next()) {
                contents.setText(resultSet.getString("contents"));
                notificationTitle.setText(resultSet.getString("title"));
                int authorID = Integer.parseInt(resultSet.getString("employeeNumber"));
                Employee employee = new Employee(sqlConnection, authorID);
                author.setText("Posted by " + employee.getFullName() + ".");
                postTime.setText("Published " + resultSet.getString("postTime") + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    ArrayList<Employee> employees;

    @FXML
    void selectEmployeesOperationTab() {
        String query = "select * from employees";
        employees = new ArrayList<>();
        ResultSet resultSet = sqlConnection.getDataQuery(query);
        try {
            while (resultSet.next()) {
                employees.add(new Employee(resultSet));
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

    }
}
