package salesmanagement.salesmanagement.ViewController.CustomersTab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.tableview2.FilteredTableView;
import salesmanagement.salesmanagement.SalesComponent.Customer;
import salesmanagement.salesmanagement.ViewController.ViewController;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class CustomersTabViewController extends ViewController {
    @FXML
    private TableColumn<?, ?> addressColumn;

    @FXML
    private TableColumn<?, ?> contactColumn;

    @FXML
    private TableColumn<?, ?> customerNameColumn;

    @FXML
    private FilteredTableView<Customer> customersTable;

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private TableColumn<?, ?> rankColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
    }

    @FXML
    void addFilter(MouseEvent event) {

    }

    @FXML
    void addNewCustomer(MouseEvent event) {

    }

    @FXML
    void openExportCustomersBox(MouseEvent event) {

    }
    FilteredList<Customer> filteredCustomers;
    SortedList<Customer> sortedAndFilteredCustomers;
    @Override
    public void show() {
        super.show();
        runTask(() -> {
            ObservableList<Customer> customers = FXCollections.observableArrayList();
            customersTable.setItems(customers);
            try {
                String query = "SELECT * FROM customers WHERE customerNumber != 6";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                while (resultSet.next()) {
                    Customer customer = new Customer(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5)
                    );
                    customersTable.getItems().add(customer);
                }
                customersTable.refresh();

                filteredCustomers = new FilteredList<>(FXCollections.observableArrayList(customersTable.getItems()), p -> true);
                sortedAndFilteredCustomers = new SortedList<>(filteredCustomers);
                customersTable.setItems(sortedAndFilteredCustomers);
                sortedAndFilteredCustomers.comparatorProperty().bind(customersTable.comparatorProperty());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, null, loadingIndicator, null);
    }
}
