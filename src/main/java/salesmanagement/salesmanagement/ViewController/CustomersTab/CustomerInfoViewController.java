package salesmanagement.salesmanagement.ViewController.CustomersTab;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import salesmanagement.salesmanagement.SalesComponent.Customer;
import salesmanagement.salesmanagement.SalesComponent.SalesComponent;
import salesmanagement.salesmanagement.Utils.NotificationCode;
import salesmanagement.salesmanagement.Utils.NotificationSystem;
import salesmanagement.salesmanagement.ViewController.InfoViewController;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class CustomerInfoViewController extends InfoViewController<Customer> implements CustomersTabController {
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


    protected void show(Customer customer) {
        super.show();
        super.selectedSalesComponent = customer;
        if (customer.isNewUser()) {
            addButton.setVisible(true);
        } else saveButton.setVisible(true);
        if (customer.isNewUser()) customerNumberTextField.setText("");
        else customerNumberTextField.setText(Integer.toString(customer.getCustomerNumber()));
        addressTextField.setText(customer.getAddress());
        contactTextField.setText(customer.getContact());
        rankComboBox.setValue(customer.getRank());
        nameTextField.setText(customer.getName());
    }

    @FXML
    public void save() {
        runTask(() -> {
                    String query = String.format("UPDATE customers SET customerName = '%s', phone = '%s', addressLine = '%s' WHERE customerNumber = %d",
                            nameTextField.getText(), contactTextField.getText(), addressTextField.getText(), selectedSalesComponent.getCustomerNumber());
                    sqlConnection.updateQuery(query);
                }, () -> {
                    close();
                    parentController.show();
                    NotificationSystem.throwNotification(NotificationCode.SUCCEED_ADD_CUSTOMER, stage);
                },
                loadingIndicator, null);
    }

    @FXML
    public void add() {
        runTask(() -> {
                    close();
                    String query = String.format("insert into customers(customerName, phone, addressLine) values ('%s', '%s', '%s')",
                            nameTextField.getText(), contactTextField.getText(), addressTextField.getText());
                    sqlConnection.updateQuery(query);
                }, () -> {
                    parentController.show();
                    NotificationSystem.throwNotification(NotificationCode.SUCCEED_ADD_CUSTOMER, stage);
                },
                loadingIndicator, null);
    }
}
