package salesmanagement.salesmanagement.ViewController.EmployeesTab;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.ViewController.FilterViewController;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeesFilterViewController extends FilterViewController<Employee> implements EmployeesTabController {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        accessibilityComboBox.setItems(FXCollections.observableArrayList("HR", "Manager", "Employee", "Admin"));
        statusComboBox.setItems(FXCollections.observableArrayList("ACTIVE", "INACTIVE"));
    }

    @FXML
    public void applyFilter() {
        filteredList.setPredicate(employee -> {
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
    public void clearFilter() {
        emailTextField.setText("");
        nameTextField.setText("");
        phoneTextField.setText("");
        accessibilityComboBox.setValue(null);
        statusComboBox.setValue(null);
    }

}
