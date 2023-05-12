package salesmanagement.salesmanagement.ViewController.EmployeesTab;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.Utils.Utils;
import salesmanagement.salesmanagement.ViewController.SearchView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class EmployeeSearchView extends SearchView<Employee> {
    @FXML
    private ComboBox<String> accessibilityComboBox;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private TableColumn<Employee, String> employeeNumberColumn;
    @FXML
    private TableColumn<Employee, String> employeeNameColumn;
    @FXML
    private TableColumn<Employee, String> officeColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        employeeNumberColumn.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
        officeColumn.setCellValueFactory(new PropertyValueFactory<>("officeCode"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        Utils.adjustTableColumnWidths(searchTable, Arrays.asList(0.3, 0.5, 0.2));

        statusComboBox.setItems(FXCollections.observableArrayList(Arrays.asList("Active", "Inactive", "All")));
        accessibilityComboBox.setItems(FXCollections.observableArrayList(Arrays.asList("Employee", "HR", "Manager", "All")));
    }

    @Override
    public void show() {
        super.show();
        search();
    }

    @Override
    protected void search() {
        runTask(() -> {
            String query = "select * from employees";
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            List<Employee> employeeList = new ArrayList<>();
            try {
                while (resultSet.next()) {
                    employeeList.add(new Employee(resultSet));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            searchList = new FilteredList<>(FXCollections.observableArrayList(employeeList));
            searchList.setPredicate(employee -> {
                boolean nameMatch = employee.getName().toLowerCase().contains(nameTextField.getText());
                boolean phoneMatch = employee.getPhone().toLowerCase().contains(phoneTextField.getText());
                boolean statusMatch = statusComboBox.getValue().equalsIgnoreCase(employee.getStatus());
                boolean emailMatch = employee.getEmail().toLowerCase().contains(emailTextField.getText().toLowerCase());
                boolean accessibilityMatch = employee.getJobTitle().equalsIgnoreCase(accessibilityComboBox.getValue());
                if (accessibilityComboBox.getValue().equals("All")) accessibilityMatch = true;
                if (statusComboBox.getValue().equals("All")) statusMatch = true;
                return phoneMatch && nameMatch && accessibilityMatch && emailMatch && statusMatch;
            });
        }, () -> searchTable.setItems(searchList), loadingIndicator, null);
    }
}
