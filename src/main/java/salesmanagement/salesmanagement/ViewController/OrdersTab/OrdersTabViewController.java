package salesmanagement.salesmanagement.ViewController.OrdersTab;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.tableview2.FilteredTableView;
import salesmanagement.salesmanagement.SalesComponent.Order;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.ViewController.TabView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class OrdersTabViewController extends TabView implements OrdersTabController {

    @FXML
    private TableColumn<?, ?> commentsColumn;

    @FXML
    private TableColumn<?, ?> customerColumn;

    @FXML
    private TableColumn<?, ?> employeeColumn;

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private TableColumn<?, ?> orderDateColumn;

    @FXML
    private TableColumn<?, ?> orderNumberColumn;

    @FXML
    private FilteredTableView<Order> ordersTable;

    @FXML
    private StackPane root;

    @FXML
    private TableColumn<?, ?> typeColumn;

    @FXML
    private TableColumn<?, ?> valueColumn;

    private OrdersFilterViewController ordersFilterViewController;
    private SortedList<Order> sortedAndFilteredOrders;
    private OrdersExportViewController ordersExportViewController;
    private OrderInfoViewController orderInfoViewController;

    @FXML
    public void addNewOrder() {
        orderInfoViewController.show();
    }

    @FXML
    public void applyFilter() {
        ordersFilterViewController.show();
    }

    @FXML
    public void exportOrdersList() {
        ordersExportViewController.show();
    }

    @Override
    protected void figureShow() {
        super.figureShow();
        runTask(() -> {
            List<Order> orders = new ArrayList<>();

            String query = "SELECT * FROM orders LEFT JOIN customers ON orders.customerNumber = customers.customerNumber INNER JOIN employees ON employees.employeeNumber = orders.created_by;";
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            try {
                while (resultSet.next()) {
                    orders.add(new Order(resultSet));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ordersFilterViewController.setFilteredList(new FilteredList<>(FXCollections.observableArrayList(orders)));
            sortedAndFilteredOrders = new SortedList<>(ordersFilterViewController.getFilteredList());
        }, () -> {
            ordersTable.setItems(sortedAndFilteredOrders);
            sortedAndFilteredOrders.comparatorProperty().bind(ordersTable.comparatorProperty());
            isShowing = false;
        }, loadingIndicator, null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/orders-tab/orders-filter-view.fxml"));
            loader.load();
            ordersFilterViewController = loader.getController();
            root.getChildren().add(ordersFilterViewController.getRoot());
            ordersFilterViewController.setParentController(this);

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/orders-tab/orders-export-view.fxml"));
            loader.load();
            ordersExportViewController = loader.getController();
            root.getChildren().add(ordersExportViewController.getRoot());
            ordersExportViewController.setParentController(this);

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/orders-tab/order-info-view.fxml"));
            loader.load();
            orderInfoViewController = loader.getController();
            root.getChildren().add(orderInfoViewController.getRoot());
            orderInfoViewController.setParentController(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        commentsColumn.setCellValueFactory(new PropertyValueFactory<>("comments"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerNumber"));

        ordersTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                orderInfoViewController.show(ordersTable.getSelectionModel().getSelectedItem());
            }
        });
    }
}
