package salesmanagement.salesmanagement.ViewController.CustomersTab;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import salesmanagement.salesmanagement.SalesComponent.Customer;
import salesmanagement.salesmanagement.Utils.NotificationCode;
import salesmanagement.salesmanagement.Utils.NotificationSystem;
import salesmanagement.salesmanagement.ViewController.InfoViewController;

import java.sql.ResultSet;
import java.sql.SQLException;

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
    private TextField SSNTextField;
    @FXML
    private ComboBox<String> rankComboBox;

    @Override
    public void show() {
        super.show();
        addButton.setVisible(true);
    }

    public void show(Customer customer) {
        super.show();
        super.selectedSalesComponent = customer;
        saveButton.setVisible(true);
        customerNumberTextField.setText(Integer.toString(customer.getCustomerNumber()));
        SSNTextField.setText(String.valueOf(customer.getSSN()));
        addressTextField.setText(customer.getAddress());
        contactTextField.setText(customer.getContact());
        rankComboBox.setValue(customer.getRank());
        nameTextField.setText(customer.getName());
    }

    @Override
    public void close() {
        super.close();
        resetData();
    }

    protected void resetData() {
        customerNumberTextField.setText("");
        SSNTextField.setText("");
        addressTextField.setText("");
        contactTextField.setText("");
        rankComboBox.setValue(null);
        nameTextField.setText("");
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
                parentController.getLoadingIndicator(), null);
    }

    @FXML
    public void add() {
        runTask(() -> {
                    close();
                    String query = String.format("insert into customers(customerName, phone, addressLine, customerSSN) values ('%s', '%s', '%s', '%s')",
                            nameTextField.getText(), contactTextField.getText(), addressTextField.getText(), SSNTextField.getText());
                    sqlConnection.updateQuery(query);
                    query = "select customerNumber from customers where customerSSN = " + SSNTextField.getText();
                    ResultSet resultSet = sqlConnection.getDataQuery(query);
                    try {
                        while (resultSet.next()) {
                            customerNumberTextField.setText(String.valueOf(resultSet.getInt("customerNumber")));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }, () -> {
                    parentController.show();
                    NotificationSystem.throwNotification(NotificationCode.SUCCEED_ADD_CUSTOMER, stage);
                },
                parentController.getLoadingIndicator(), null);
    }
}
