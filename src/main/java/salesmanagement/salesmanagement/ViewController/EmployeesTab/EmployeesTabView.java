package salesmanagement.salesmanagement.ViewController.EmployeesTab;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.tableview2.FilteredTableView;

import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesComponent.SalesComponent;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.ViewController.TabView;
import salesmanagement.salesmanagement.ViewController.ViewController;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class EmployeesTabView extends TabView implements EmployeesTab {
    @FXML
    private TableColumn<?, ?> accessibilityColumn;

    @FXML
    private TableColumn<?, ?> emailColumn;

    @FXML
    private TableColumn<?, ?> employeeNumberColumn;

    @FXML
    private TableColumn<?, ?> employeeStatusColumn;

    @FXML
    private FilteredTableView<SalesComponent> employeesTable;

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TableColumn<?, ?> phoneColumn;

    ViewController employeesExportView;
    EmployeesFilterView employeesFilterView;
    EmployeeInfoView employeeInfoView;
    SortedList<SalesComponent> sortedAndFilteredEmployees;

    Employee loggedInUser;
    @FXML
    JFXButton addEmployee;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/employees-tab/employees-export-view.fxml"));
            loader.load();
            employeesExportView = loader.getController();
            root.getChildren().add(employeesExportView.getRoot());
            employeesExportView.setParentController(this);

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/employees-tab/employees-filter-view.fxml"));
            loader.load();
            employeesFilterView = loader.getController();
            root.getChildren().add(employeesFilterView.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/employees-tab/employee-info-view.fxml"));
            loader.load();
            employeeInfoView = loader.getController();
            root.getChildren().add(employeeInfoView.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }

        employeeNumberColumn.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        employeeStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statusLabel"));
        accessibilityColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));

        employeesTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                Employee selected = (Employee) employeesTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    employeeInfoView.show(selected);
                }
            }
        });
    }

    public void setLoggedInUser(Employee loggedInUser) {
        this.loggedInUser = loggedInUser;
        if (loggedInUser.getJobTitle().equals("Employee")) {
            addEmployee.setVisible(false);
        } else {
            addEmployee.setVisible(true);
        }
        employeeInfoView.setLoggedInUser(loggedInUser);
    }

    @Override
    protected void figureShow() {
        super.figureShow();
        employeeInfoView.close();
        if (!tableFigured) {
            tableFigured = true;
            double tableWidth = employeesTable.getWidth() - 2;
            employeeNumberColumn.setMinWidth(0.15 * tableWidth);
            nameColumn.setMinWidth(0.25 * tableWidth);
            phoneColumn.setMinWidth(0.2 * tableWidth);
            emailColumn.setMinWidth(0.2 * tableWidth);
            accessibilityColumn.setMinWidth(0.1 * tableWidth);
            employeeStatusColumn.setMinWidth(0.1 * tableWidth);
        }

        runTask(() -> {
            String activity = "UPDATE employees\n" +
                    "SET status = \n" +
                    "CASE \n" +
                    "WHEN COALESCE(lastWorkingDate, joiningDate) <= DATE_ADD(NOW(), INTERVAL -1 MONTH) \n" +
                    "THEN 'INACTIVE'\n" +
                    "ELSE 'ACTIVE'\n" +
                    "END";
            try {
                sqlConnection.updateQuery(activity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            List<Employee> employees = new ArrayList<>();
            String query = "SELECT * FROM employees";
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            try {
                while (resultSet.next()) {
                    employees.add(new Employee(resultSet));
                }
            } catch (SQLException ignored) {

            }
            employeesFilterView.setFilteredList(new FilteredList<>(FXCollections.observableArrayList(employees)));
            sortedAndFilteredEmployees = new SortedList<>(employeesFilterView.getFilteredList());
        }, () -> {
            employeesTable.setItems(sortedAndFilteredEmployees);
            sortedAndFilteredEmployees.comparatorProperty().bind(employeesTable.comparatorProperty());
            isShowing = false;
        }, loadingIndicator, null);
    }

    @FXML
    void addEmployeeFilter() {
        employeesFilterView.show();
    }

    @FXML
    void addNewEmployee() {
        employeeInfoView.show(new Employee());
    }

    @FXML
    void openExportEmployeesBox() {
        employeesExportView.show();
    }
}
