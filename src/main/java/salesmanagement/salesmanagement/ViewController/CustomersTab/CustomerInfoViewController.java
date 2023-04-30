package salesmanagement.salesmanagement.ViewController.CustomersTab;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import salesmanagement.salesmanagement.SalesComponent.Customer;
import salesmanagement.salesmanagement.Utils.NotificationCode;
import salesmanagement.salesmanagement.Utils.NotificationSystem;
import salesmanagement.salesmanagement.ViewController.ViewController;

import java.net.URL;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class CustomerInfoViewController extends ViewController {
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
    private JFXButton addButton;
    @FXML
    private JFXButton saveButton;
    private Customer customer;
    @FXML
    private ProgressIndicator loadingIndicator;
    @FXML
    private VBox customerInfoBox;
    private CustomersTabViewController tab;
    protected void show(Customer customer) {
        super.show();
        this.customer = customer;
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
    void saveCustomerInfo() {
        String query = String.format("UPDATE customers SET customerName = '%s', phone = '%s', addressLine = '%s' WHERE customerNumber = %d",
                nameTextField.getText(), contactTextField.getText(), addressTextField.getText(), customer.getCustomerNumber());
        sqlConnection.updateQuery(query);
    }

    @FXML
    void addNewCustomer() {
        runTask(() -> {
                    String query = String.format("insert into customers(customerName, phone, addressLine) values ('%s', '%s', '%s')",
                            nameTextField.getText(), contactTextField.getText(), addressTextField.getText());
                    sqlConnection.updateQuery(query);
                }, () -> {
                    close();
                    tab.show();
                    NotificationSystem.throwNotification(NotificationCode.SUCCEED_ADD_CUSTOMER, stage);
                },
                loadingIndicator,null);
    }

    @Override
    public void close() {
        super.close();
        addButton.setVisible(false);
        saveButton.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }
}
