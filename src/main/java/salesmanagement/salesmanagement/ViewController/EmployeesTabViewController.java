package salesmanagement.salesmanagement.ViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.controlsfx.control.tableview2.FilteredTableView;
import salesmanagement.salesmanagement.ImageController;
import salesmanagement.salesmanagement.SalesComponent.Action;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesManagement;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;
import static salesmanagement.salesmanagement.Utils.getAllNodes;

public class EmployeesTabViewController extends ViewController {
    @FXML
    private TableColumn<?, ?> accessibilityColumn;

    @FXML
    private TableColumn<?, ?> emailColumn;

    @FXML
    private TableColumn<?, ?> employeeNumberColumn;

    @FXML
    private TableColumn<?, ?> employeeStatusColumn;

    @FXML
    private FilteredTableView<Employee> employeesTable;

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TableColumn<?, ?> phoneColumn;
    EmployeesExportViewController employeesExportViewController;
    EmployeesFilterViewController employeesFilterViewController;
    EmployeeInfoViewController employeeInfoViewController;
    SortedList<Employee> sortedAndFilteredEmployees;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/employees-export-view.fxml"));
            loader.load();
            employeesExportViewController = loader.getController();
            root.getChildren().add(employeesExportViewController.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/employees-filter-view.fxml"));
            loader.load();
            employeesFilterViewController = loader.getController();
            root.getChildren().add(employeesFilterViewController.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/employee-info-view.fxml"));
            loader.load();
            employeeInfoViewController = loader.getController();
            root.getChildren().add(employeeInfoViewController.getRoot());
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
                Employee selected = employeesTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    employeeInfoViewController.show(selected);
                };
            }
        });
    }

    boolean employeesTableConfigured = false;

    @Override
    public void show() {
        super.show();

        if (!employeesTableConfigured) {
            employeesTableConfigured = true;
            double tableWidth = employeesTable.getWidth()-2;
            employeeNumberColumn.setMinWidth(0.15 * tableWidth);
            nameColumn.setMinWidth(0.25 * tableWidth);
            phoneColumn.setMinWidth(0.2 * tableWidth);
            emailColumn.setMinWidth(0.2 * tableWidth);
            accessibilityColumn.setMinWidth(0.1 * tableWidth);
            employeeStatusColumn.setMinWidth(0.1 * tableWidth);
        }

        ArrayList<Employee> employees = new ArrayList<>();
        employeesFilterViewController.setEmployees(employees);
        runTask(() -> {
            String query = "SELECT * FROM employees";
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            try {
                while (resultSet.next()) {
                    employees.add(new Employee(resultSet));
                }
            } catch (SQLException ignored) {

            }
            ObservableList<Employee> employeeList = FXCollections.observableArrayList(employees);
            employeesFilterViewController.setFilteredEmployees(new FilteredList<>(employeeList, p -> true));
            sortedAndFilteredEmployees = new SortedList<>(employeesFilterViewController.getFilteredEmployees());
        }, () -> {
            employeesTable.setItems(sortedAndFilteredEmployees);
            sortedAndFilteredEmployees.comparatorProperty().bind(employeesTable.comparatorProperty());
        }, loadingIndicator, null);
    }

    @FXML
    void addEmployeeFilter(MouseEvent event) {
        employeesFilterViewController.show();
    }

    @FXML
    void addNewEmployee(MouseEvent event) {
        employeeInfoViewController.show(new Employee());
    }

    @FXML
    void openExportEmployeesBox(MouseEvent event) {
        employeesExportViewController.show();
    }
}
