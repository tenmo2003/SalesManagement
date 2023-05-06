package salesmanagement.salesmanagement.ViewController.CustomersTab;

import com.jfoenix.controls.JFXButton;
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
import salesmanagement.salesmanagement.SalesComponent.SalesComponent;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.ViewController.TabView;
import salesmanagement.salesmanagement.ViewController.UserRight;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class CustomersTabView extends TabView implements CustomersTab {
    private CustomerInfoView customerInfoView;
    private CustomersExportView customersExportView;
    private CustomersFilterView customersFilterView;

    @FXML
    private TableColumn<?, ?> addressColumn;

    @FXML
    private TableColumn<?, ?> contactColumn;

    @FXML
    private TableColumn<?, ?> customerNameColumn;

    @FXML
    private FilteredTableView<SalesComponent> customersTable;

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private TableColumn<?, ?> rankColumn;
    @FXML
    private JFXButton addButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/customers-tab/customer-info-view.fxml"));
            loader.load();
            customerInfoView = loader.getController();
            root.getChildren().add(customerInfoView.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/customers-tab/customers-export-view.fxml"));
            loader.load();
            customersExportView = loader.getController();
            root.getChildren().add(customersExportView.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/customers-tab/customers-filter-view.fxml"));
            loader.load();
            customersFilterView = loader.getController();
            root.getChildren().add(customersFilterView.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }

        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));

        customersTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                Customer selected = (Customer) customersTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    customerInfoView.show(selected);
                }
            }
        });

        customerInfoView.setParentController(this);
        customersExportView.setParentController(this);
    }

    @FXML
    void applyFilter() {
        customersFilterView.show();
    }

    @FXML
    void add() {
        customerInfoView.show();
    }

    @FXML
    void export() {
        customersExportView.show();
    }

    SortedList<SalesComponent> sortedAndFilteredCustomers;

    @Override
    public void figureShow() {
        super.figureShow();
        runTask(() -> {
            try {
                String query = "SELECT * FROM customers WHERE customerNumber != 6";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                List<Customer> customers = new ArrayList<>();
                while (resultSet.next()) {
                    customers.add(new Customer(resultSet));
                }
                customersFilterView.setFilteredList(new FilteredList<>(FXCollections.observableArrayList(customers), p -> true));
                sortedAndFilteredCustomers = new SortedList<>(customersFilterView.getFilteredList());
                customersTable.setItems(sortedAndFilteredCustomers);
                sortedAndFilteredCustomers.comparatorProperty().bind(customersTable.comparatorProperty());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, () -> {
            isShowing = false;
        }, loadingIndicator, null);
    }
}
