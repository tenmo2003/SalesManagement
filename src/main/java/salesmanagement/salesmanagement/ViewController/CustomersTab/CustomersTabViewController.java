package salesmanagement.salesmanagement.ViewController.CustomersTab;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.tableview2.FilteredTableView;
import salesmanagement.salesmanagement.SalesComponent.Customer;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.ViewController.ViewController;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    private CustomerInfoViewController customerInfoViewController;
private CustomersFilterViewController customersFilterViewController;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/customers-tab/customer-info-view.fxml"));
            loader.load();
            customerInfoViewController = loader.getController();
            root.getChildren().add(customerInfoViewController.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/customers-tab/customers-filter-view.fxml"));
            loader.load();
            customersFilterViewController = loader.getController();
            root.getChildren().add(customersFilterViewController.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }

        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));

        customersTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                Customer selected = customersTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    customerInfoViewController.show(selected);
                }
            }
        });
    }

    @FXML
    void addFilter(MouseEvent event) {
        customersFilterViewController.show();
    }

    @FXML
    void addNewCustomer(MouseEvent event) {
        customerInfoViewController.show(new Customer());
    }

    @FXML
    void openExportCustomersBox(MouseEvent event) {

    }

    SortedList<Customer> sortedAndFilteredCustomers;

    @Override
    public void show() {
        super.show();
        runTask(() -> {
            try {
                String query = "SELECT * FROM customers WHERE customerNumber != 6";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                List<Customer> customers = new ArrayList<>();
                while (resultSet.next()) {
                    customers.add(new Customer(resultSet));
                }
                customersFilterViewController.setFilteredCustomers(new FilteredList<>(FXCollections.observableArrayList(customers), p -> true));
                sortedAndFilteredCustomers = new SortedList<>(customersFilterViewController.getFilteredCustomers());
                customersTable.setItems(sortedAndFilteredCustomers);
                sortedAndFilteredCustomers.comparatorProperty().bind(customersTable.comparatorProperty());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, null, loadingIndicator, null);
    }
}
