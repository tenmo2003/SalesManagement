package salesmanagement.salesmanagement.ViewController.CustomersTab;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import salesmanagement.salesmanagement.SalesComponent.Customer;
import salesmanagement.salesmanagement.ViewController.OrdersTab.OrderInfoView;
import salesmanagement.salesmanagement.ViewController.SearchView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class CustomerSearchView extends SearchView<Customer> {
    @FXML
    private TableColumn<?, ?> SSNColumn;
    @FXML
    private TextField SSNTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField contactTextField;
    @FXML
    private TableColumn<?, ?> customerNameColumn;
    @FXML
    private TableColumn<?, ?> customerNumberColumn;
    @FXML
    private TableColumn<?, ?> contactColumn;
    @FXML
    private TextField customerNameTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        customerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("customerNumber"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        SSNColumn.setCellValueFactory(new PropertyValueFactory<>("SSN"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));

        searchTable.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                ((OrderInfoView)parentController).setSearchedCustomer(searchTable.getSelectionModel().getSelectedItem());
                close();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        if (!tableFigured) {
            tableFigured = true;
            double tableWidth = searchTable.getWidth();
            customerNumberColumn.setMinWidth(0.1 * tableWidth);
            customerNameColumn.setMinWidth(0.3 * tableWidth);
            contactColumn.setMinWidth(0.3 * tableWidth);
            SSNColumn.setMinWidth(0.3 * tableWidth);
        }
        search();
    }

    @FXML
    public void search() {
        runTask(() -> {
            String query = "select * from customers";
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            List<Customer> customerList = new ArrayList<>();
            try {
                while (resultSet.next()) {
                    customerList.add(new Customer(resultSet));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            searchList = new FilteredList<>(FXCollections.observableArrayList(customerList));
            searchList.setPredicate(customer -> {
                boolean nameMatch = customer.getName().toLowerCase().contains(customerNameTextField.getText());
                boolean contactMatch = customer.getContact().toLowerCase().contains(contactTextField.getText());
                boolean SSNMatch = String.valueOf(customer.getSSN()).contains(SSNTextField.getText());
                boolean addressMatch = customer.getAddress().toLowerCase().contains(addressTextField.getText());
                return contactMatch && nameMatch && SSNMatch && addressMatch;
            });
        }, () -> searchTable.setItems(searchList), loadingIndicator, null);

    }
}