package salesmanagement.salesmanagement.ViewController.CustomersTab;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import salesmanagement.salesmanagement.SalesComponent.Customer;
import salesmanagement.salesmanagement.ViewController.ViewController;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CustomersFilterViewController extends ViewController {

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField contactTextField;

    @FXML
    private TextField customerNumberTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private ComboBox<String> rankComboBox;

    @FXML
    private StackPane root;
    private FilteredList<Customer> filteredCustomers;
    public FilteredList<Customer> getFilteredCustomers() {
        return filteredCustomers;
    }

    public void setFilteredCustomers(FilteredList<Customer> filteredCustomers) {
        this.filteredCustomers = filteredCustomers;
    }

    @FXML
    void applyFilter(MouseEvent event) {
        filteredCustomers.setPredicate(customer -> {
            boolean nameMatch = customer.getName().toLowerCase().contains(nameTextField.getText().toLowerCase());
            boolean addressMatch = customer.getAddress().toLowerCase().contains(addressTextField.getText().toLowerCase());
            boolean phoneMatch = customer.getContact().toLowerCase().contains(contactTextField.getText().toLowerCase());
            boolean rankMatch = customer.getRank().equals(rankComboBox.getValue());
            if (rankComboBox.getValue() == null) rankMatch = true;
            return nameMatch && addressMatch && phoneMatch && rankMatch;
        });
        close();
    }

    @FXML
    void clearFilter(MouseEvent event) {
        nameTextField.setText("");
        addressTextField.setText("");
        contactTextField.setText("");
        rankComboBox.setValue(null);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        rankComboBox.setItems(FXCollections.observableList(Arrays.asList("Emerald", "Membership")));
    }
}
