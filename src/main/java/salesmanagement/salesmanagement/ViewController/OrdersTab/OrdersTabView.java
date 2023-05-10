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
import javafx.scene.control.TableView;
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

public class OrdersTabView extends TabView implements OrdersTab {

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
    private TableView<Order> ordersTable;

    @FXML
    private StackPane root;

    @FXML
    private TableColumn<?, ?> typeColumn;

    @FXML
    private TableColumn<?, ?> valueColumn;

    private OrdersFilterView ordersFilterView;
    private SortedList<Order> sortedAndFilteredOrders;
    private OrdersExportView ordersExportView;
    private OrderInfoView orderInfoView;

    @FXML
    public void addNewOrder() {
        orderInfoView.show();
    }

    @FXML
    public void applyFilter() {
        ordersFilterView.show();
    }

    @FXML
    public void exportOrdersList() {
        ordersExportView.show();
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

            ordersFilterView.setFilteredList(new FilteredList<>(FXCollections.observableArrayList(orders)));
            sortedAndFilteredOrders = new SortedList<>(ordersFilterView.getFilteredList());
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
            ordersFilterView = loader.getController();
            root.getChildren().add(ordersFilterView.getRoot());
            ordersFilterView.setParentController(this);

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/orders-tab/orders-export-view.fxml"));
            loader.load();
            ordersExportView = loader.getController();
            root.getChildren().add(ordersExportView.getRoot());
            ordersExportView.setParentController(this);

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/orders-tab/order-info-view.fxml"));
            loader.load();
            orderInfoView = loader.getController();
            root.getChildren().add(orderInfoView.getRoot());
            orderInfoView.setParentController(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        commentsColumn.setCellValueFactory(new PropertyValueFactory<>("comments"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        ordersTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                orderInfoView.show(ordersTable.getSelectionModel().getSelectedItem());
            }
        });
    }
}
