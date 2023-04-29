package salesmanagement.salesmanagement.ViewController.EmployeesTab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.ViewController.ViewController;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeesFilterViewController extends ViewController {

    @FXML
    private ComboBox<String> accessibilityComboBox;

    @FXML
    private TextField emailTextField;

    @FXML
    private VBox employeeFilterBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private ComboBox<String> statusComboBox;

    private FilteredList<Employee> filteredEmployees;


    List<Employee> employees;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        accessibilityComboBox.setItems(FXCollections.observableArrayList("HR", "Manager", "Employee", "Admin"));
        statusComboBox.setItems(FXCollections.observableArrayList("ACTIVE", "INACTIVE"));
    }

    public FilteredList<Employee> getFilteredEmployees() {
        return filteredEmployees;
    }

    public void setFilteredEmployees(FilteredList<Employee> filteredEmployees) {
        this.filteredEmployees = filteredEmployees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @FXML
    public void applyFilter() {
        filteredEmployees.setPredicate(employee -> {
            boolean nameMatch = employee.getFullName().toLowerCase().contains(nameTextField.getText().toLowerCase());
            boolean emailMatch = employee.getEmail().toLowerCase().contains(emailTextField.getText().toLowerCase());
            boolean phoneMatch = employee.getPhone().toLowerCase().contains(phoneTextField.getText().toLowerCase());
            boolean statusMatch = employee.getStatus().equals(statusComboBox.getValue());
            if (statusComboBox.getValue() == null) statusMatch = true;
            boolean accessibilityMatch = employee.getJobTitle().equals(accessibilityComboBox.getValue());
            if (accessibilityComboBox.getValue() == null) accessibilityMatch = true;
            return nameMatch && emailMatch && phoneMatch && statusMatch && accessibilityMatch;
        });
        close();
    }

    @FXML
    void clearFilter(MouseEvent event) {
        emailTextField.setText("");
        nameTextField.setText("");
        phoneTextField.setText("");
        accessibilityComboBox.setValue(null);
        statusComboBox.setValue(null);
    }

}
