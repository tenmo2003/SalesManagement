package salesmanagement.salesmanagement.ViewController.CustomersTab;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import salesmanagement.salesmanagement.SalesComponent.Customer;
import salesmanagement.salesmanagement.ViewController.FilterView;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CustomersFilterView extends FilterView<Customer> implements CustomersTab {

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
    public void applyFilter() {
        filteredList.setPredicate(customer -> {
            boolean customerNumberMatch = (String.valueOf(customer.getCustomerNumber()).contains(customerNumberTextField.getText()));
            boolean nameMatch = customer.getName().toLowerCase().contains(nameTextField.getText().toLowerCase());
            boolean addressMatch = customer.getAddress().toLowerCase().contains(addressTextField.getText().toLowerCase());
            boolean phoneMatch = customer.getContact().toLowerCase().contains(contactTextField.getText().toLowerCase());
            boolean rankMatch = customer.getRank().equals(rankComboBox.getValue());
            if (rankComboBox.getValue() == null) rankMatch = true;
            return nameMatch && addressMatch && phoneMatch && rankMatch && customerNumberMatch;
        });
        close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        rankComboBox.setItems(FXCollections.observableList(Arrays.asList("Emerald", "Membership")));
    }
}
