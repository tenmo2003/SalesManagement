package salesmanagement.salesmanagement.SceneController;

import com.jfoenix.controls.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import salesmanagement.salesmanagement.SalesComponent.*;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.Utils.ImageController;
import salesmanagement.salesmanagement.Utils.NotificationCode;
import salesmanagement.salesmanagement.Utils.NotificationSystem;
import salesmanagement.salesmanagement.ViewController.*;
import salesmanagement.salesmanagement.ViewController.CustomersTab.CustomersTabViewController;
import salesmanagement.salesmanagement.ViewController.DashBoardTab.DashboardTabViewController;
import salesmanagement.salesmanagement.ViewController.EmployeesTab.EmployeesTabViewController;
import salesmanagement.salesmanagement.ViewController.OrdersTab.OrdersTabViewController;
import salesmanagement.salesmanagement.ViewController.ProductsTab.ProductsTabViewController;
import salesmanagement.salesmanagement.ViewController.SettingsTab.SettingTabViewController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainSceneController extends SceneController implements Initializable {
    @FXML
    Text usernameText;
    @FXML
    TabPane tabPane;
    @FXML
    private Tab employeesOperationTab;
    @FXML
    private Tab createOrderTab;
    boolean createOrderInit = false;
    @FXML
    private Tab settingTab;
    @FXML
    private Tab productsOperationTab;
    boolean productsInit = false;
    @FXML
    private Tab ordersTab;
    boolean ordersInit = false;
    @FXML
    private Tab customersTab;
    @FXML
    JFXButton dashBoardTabButton;
    @FXML
    JFXButton ordersTabButton;
    @FXML
    JFXButton newsTabButton;
    @FXML
    JFXButton settingsTabButton;
    @FXML
    JFXButton productsTabButton;
    @FXML
    JFXButton customersTabButton;
    @FXML
    JFXButton employeesTabButton;
    JFXButton currentTabButton;


    SettingTabViewController settingTabViewController;
    EmployeesTabViewController employeesTabViewController;
    CustomersTabViewController customersTabViewController;
    ProductsTabViewController productsTabViewController;
    OrdersTabViewController ordersTabViewController;
    DashboardTabViewController dashboardTabViewController;

    @FXML
    void goToCreateOrderTab() {
        tabPane.getSelectionModel().select(createOrderTab);
    }

    @FXML
    void goToOrdersTab() {
        tabPane.getSelectionModel().select(ordersTab);
        ordersTabViewController.show();

    }

    void goToEditOrderTab() {
        tabPane.getSelectionModel().select(createOrderTab);
    }

    @FXML
    void goToProductsOperationTab() {
        tabPane.getSelectionModel().select(productsOperationTab);
        productsTabViewController.show();
    }

    @FXML
    void goToCustomersTab() {
        tabPane.getSelectionModel().select(customersTab);
        customersTabViewController.show();
    }

    @FXML
    void goToSettingTab() {
        tabPane.getSelectionModel().select(settingTab);
        settingTabViewController.show();
    }

    //region DashBoard Tab
    @FXML
    Tab dashBoardTab;

    @FXML
    private StackPane chartPane1;
    @FXML
    private StackPane chartPane2;

    final int maximumChecked = 2;
    int numChecked = 2;

    @FXML
    JFXCheckBox totalRevenueCheck;
    @FXML
    JFXCheckBox topProductCheck;
    @FXML
    JFXCheckBox topProductLineCheck;
    @FXML
    JFXCheckBox topCustomerCheck;
    @FXML
    JFXCheckBox topStoreCheck;

    public void displayDashBoardTab() {

        tabPane.getSelectionModel().select(dashBoardTab);
        dashboardTabViewController.show();

//        chartPane1.getChildren().clear();
//        chartPane2.getChildren().clear();
//
//        final CategoryAxis totalRevenueAxis = new CategoryAxis();
//        final NumberAxis timeAxis = new NumberAxis();
//        final BarChart<String, Number> totalRevenueChart =
//                new BarChart<>(totalRevenueAxis, timeAxis);
//        chartPane1.getChildren().add(totalRevenueChart);
//        totalRevenueChart.setTitle("Total Revenue");
//
//        String query = "SELECT YEAR(months.month) AS year, " +
//                "MONTH(months.month) AS month, " +
//                "COALESCE(SUM(value), 0) AS revenue " +
//                "FROM ( SELECT DATE_SUB(DATE_FORMAT(NOW(), '%Y-%m-01'), INTERVAL n MONTH) AS month " +
//                "FROM (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) AS numbers ) AS months " +
//                "LEFT JOIN orders " +
//                "ON YEAR(orderDate) = YEAR(months.month) AND MONTH(orderDate) = MONTH(months.month) " +
//                "WHERE months.month >= DATE_SUB(DATE_FORMAT(NOW(), '%Y-%m-01'), INTERVAL 5 MONTH) " +
//                "GROUP BY YEAR(months.month), MONTH(months.month) " +
//                "ORDER BY year ASC, month ASC";
//        ResultSet rs = sqlConnection.getDataQuery(query);
//        try {
//            XYChart.Series<String, Number> series = new XYChart.Series<>();
//            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US); // create a NumberFormat instance for US locale
//            nf.setMaximumFractionDigits(2); // set the maximum number of fraction digits to 2
//            while (rs.next()) {
//                double value = rs.getDouble(3);
//                XYChart.Data<String, Number> data = new XYChart.Data<>(rs.getString(2) + "-" + rs.getString(1), value);
//                Label label = new Label(nf.format(value)); // create a Label with the formatted value
//                label.setGraphic(new Group()); // set an empty graphic to ensure the Label is shown
//                label.setAlignment(Pos.CENTER);
//                data.setNode(label); // set the Label as the node for the Data object
//                series.getData().add(data);
//            }
//            totalRevenueChart.getData().add(series);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        final NumberAxis productRevenueAxis = new NumberAxis();
//        final CategoryAxis productAxis = new CategoryAxis();
//        final BarChart<String, Number> productRevenueChart =
//                new BarChart<>(productAxis, productRevenueAxis);
//        chartPane2.getChildren().add(productRevenueChart);
//
//        productRevenueChart.setTitle("Top Products");
//
//        query = "SELECT SUM(quantityOrdered * priceEach) AS revenue, products.productCode FROM orderdetails " +
//                "RIGHT JOIN products ON orderdetails.productCode = products.productCode " +
//                "GROUP BY products.productCode " +
//                "ORDER BY revenue DESC " +
//                "LIMIT 6";
//        rs = sqlConnection.getDataQuery(query);
//
//        try {
//            XYChart.Series<String, Number> series = new XYChart.Series<>();
//            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US); // create a NumberFormat instance for US locale
//            nf.setMaximumFractionDigits(2); // set the maximum number of fraction digits to 2
//            while (rs.next()) {
//                double value = rs.getDouble(1);
//                XYChart.Data<String, Number> data = new XYChart.Data<>(rs.getString(2), rs.getDouble(1));
//                Label label = new Label(nf.format(value)); // create a Label with the formatted value
//                label.setGraphic(new Group()); // set an empty graphic to ensure the Label is shown
//                label.setAlignment(Pos.CENTER);
//                data.setNode(label); // set the Label as the node for the Data object
//                series.getData().add(data);
//            }
//            productRevenueChart.getData().add(series);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        final NumberAxis productLineRevenueAxis = new NumberAxis();
//        final CategoryAxis productLineAxis = new CategoryAxis();
//        final BarChart<String, Number> productLineRevenueChart =
//                new BarChart<>(productLineAxis, productLineRevenueAxis);
//
//        productLineRevenueChart.setTitle("Top Product Lines");
//
//        query = "SELECT SUM(quantityOrdered * priceEach) AS revenue, products.productLine FROM orderdetails " +
//                "RIGHT JOIN products ON orderdetails.productCode = products.productCode " +
//                "GROUP BY products.productLine " +
//                "ORDER BY revenue DESC " +
//                "LIMIT 6";
//        rs = sqlConnection.getDataQuery(query);
//
//        try {
//            XYChart.Series<String, Number> series = new XYChart.Series<>();
//            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US); // create a NumberFormat instance for US locale
//            nf.setMaximumFractionDigits(2); // set the maximum number of fraction digits to 2
//            while (rs.next()) {
//                double value = rs.getDouble(1);
//                XYChart.Data<String, Number> data = new XYChart.Data<>(rs.getString(2), rs.getDouble(1));
//                Label label = new Label(nf.format(value)); // create a Label with the formatted value
//                label.setGraphic(new Group()); // set an empty graphic to ensure the Label is shown
//                label.setAlignment(Pos.CENTER);
//                data.setNode(label); // set the Label as the node for the Data object
//                series.getData().add(data);
//            }
//            productLineRevenueChart.getData().add(series);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        final NumberAxis customerRevenueAxis = new NumberAxis();
//        final CategoryAxis customerAxis = new CategoryAxis();
//        final BarChart<String, Number> customerRevenueChart =
//                new BarChart<>(customerAxis, customerRevenueAxis);
//
//        customerRevenueChart.setTitle("Top Customers");
//
//        query = "SELECT SUM(orderdetails.quantityOrdered * orderdetails.priceEach) AS revenue, customers.customerName " +
//                "FROM customers " +
//                "INNER JOIN orders ON customers.customerNumber = orders.customerNumber " +
//                "INNER JOIN orderdetails ON orders.orderNumber = orderdetails.orderNumber " +
//                "GROUP BY customers.customerNumber " +
//                "ORDER BY revenue DESC " +
//                "LIMIT 10; ";
//        rs = sqlConnection.getDataQuery(query);
//
//        try {
//            XYChart.Series<String, Number> series = new XYChart.Series<>();
//            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US); // create a NumberFormat instance for US locale
//            nf.setMaximumFractionDigits(2); // set the maximum number of fraction digits to 2
//            while (rs.next()) {
//                double value = rs.getDouble(1);
//                XYChart.Data<String, Number> data = new XYChart.Data<>(rs.getString(2), rs.getDouble(1));
//                Label label = new Label(nf.format(value)); // create a Label with the formatted value
//                label.setGraphic(new Group()); // set an empty graphic to ensure the Label is shown
//                label.setAlignment(Pos.CENTER);
//                data.setNode(label); // set the Label as the node for the Data object
//                series.getData().add(data);
//            }
//            customerRevenueChart.getData().add(series);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        final NumberAxis storeRevenueAxis = new NumberAxis();
//        final CategoryAxis storeAxis = new CategoryAxis();
//        final BarChart<String, Number> storeRevenueChart =
//                new BarChart<>(storeAxis, storeRevenueAxis);
//
//        storeRevenueChart.setTitle("Top Stores");
//
//        query = "SELECT SUM(orderdetails.quantityOrdered * orderdetails.priceEach) AS revenue, offices.addressline " +
//                "FROM orderdetails " +
//                "INNER JOIN orders ON orders.orderNumber = orderdetails.orderNumber " +
//                "INNER JOIN employees ON orders.created_by = employees.employeeNumber " +
//                "RIGHT JOIN offices ON offices.officeCode = employees.officeCode " +
//                "GROUP BY offices.addressline " +
//                "ORDER BY revenue DESC " +
//                "LIMIT 10; ";
//        rs = sqlConnection.getDataQuery(query);
//
//        try {
//            XYChart.Series<String, Number> series = new XYChart.Series<>();
//            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US); // create a NumberFormat instance for US locale
//            nf.setMaximumFractionDigits(2); // set the maximum number of fraction digits to 2
//            while (rs.next()) {
//                double value = rs.getDouble(1);
//                XYChart.Data<String, Number> data = new XYChart.Data<>(rs.getString(2), rs.getDouble(1));
//                Label label = new Label(nf.format(value)); // create a Label with the formatted value
//                label.setGraphic(new Group()); // set an empty graphic to ensure the Label is shown
//                label.setAlignment(Pos.CENTER);
//                data.setNode(label); // set the Label as the node for the Data object
//                series.getData().add(data);
//            }
//            storeRevenueChart.getData().add(series);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        totalRevenueCheck.setSelected(true);
//        totalRevenueCheck.setDisable(false);
//        topProductCheck.setSelected(true);
//        topProductCheck.setDisable(false);
//        topProductLineCheck.setSelected(false);
//        topProductLineCheck.setDisable(true);
//        topCustomerCheck.setSelected(false);
//        topCustomerCheck.setDisable(true);
//        topStoreCheck.setSelected(false);
//        topStoreCheck.setDisable(true);
//
//        totalRevenueCheck.setOnMouseClicked(event -> {
//            updateChecked(totalRevenueCheck);
//            if (totalRevenueCheck.isSelected()) {
//                if (chartPane1.getChildren().isEmpty()) {
//                    chartPane1.getChildren().add(totalRevenueChart);
//                } else if (chartPane2.getChildren().isEmpty()) {
//                    chartPane2.getChildren().add(totalRevenueChart);
//                }
//            } else {
//                if (chartPane1.getChildren().contains(totalRevenueChart)) {
//                    chartPane1.getChildren().clear();
//                } else {
//                    chartPane2.getChildren().clear();
//                }
//            }
//        });
//
//        topProductCheck.setOnMouseClicked(event -> {
//            updateChecked(topProductCheck);
//            if (topProductCheck.isSelected()) {
//                if (chartPane1.getChildren().isEmpty()) {
//                    chartPane1.getChildren().add(productRevenueChart);
//                } else if (chartPane2.getChildren().isEmpty()) {
//                    chartPane2.getChildren().add(productRevenueChart);
//                }
//            } else {
//                if (chartPane1.getChildren().contains(productRevenueChart)) {
//                    chartPane1.getChildren().clear();
//                } else {
//                    chartPane2.getChildren().clear();
//                }
//            }
//        });
//
//        topProductLineCheck.setOnMouseClicked(event -> {
//            updateChecked(topProductLineCheck);
//            if (topProductLineCheck.isSelected()) {
//                if (chartPane1.getChildren().isEmpty()) {
//                    chartPane1.getChildren().add(productLineRevenueChart);
//                } else if (chartPane2.getChildren().isEmpty()) {
//                    chartPane2.getChildren().add(productLineRevenueChart);
//                }
//            } else {
//                if (chartPane1.getChildren().contains(productLineRevenueChart)) {
//                    chartPane1.getChildren().clear();
//                } else {
//                    chartPane2.getChildren().clear();
//                }
//            }
//        });
//
//        topCustomerCheck.setOnMouseClicked(event -> {
//            updateChecked(topCustomerCheck);
//            if (topCustomerCheck.isSelected()) {
//                if (chartPane1.getChildren().isEmpty()) {
//                    chartPane1.getChildren().add(storeRevenueChart);
//                } else if (chartPane2.getChildren().isEmpty()) {
//                    chartPane2.getChildren().add(storeRevenueChart);
//                }
//            } else {
//                if (chartPane1.getChildren().contains(storeRevenueChart)) {
//                    chartPane1.getChildren().clear();
//                } else {
//                    chartPane2.getChildren().clear();
//                }
//            }
//        });
//
//        topStoreCheck.setOnMouseClicked(event -> {
//            updateChecked(topStoreCheck);
//            if (topStoreCheck.isSelected()) {
//                if (chartPane1.getChildren().isEmpty()) {
//                    chartPane1.getChildren().add(storeRevenueChart);
//                } else if (chartPane2.getChildren().isEmpty()) {
//                    chartPane2.getChildren().add(storeRevenueChart);
//                }
//            } else {
//                if (chartPane1.getChildren().contains(storeRevenueChart)) {
//                    chartPane1.getChildren().clear();
//                } else {
//                    chartPane2.getChildren().clear();
//                }
//            }
//        });


    }
    //endregion

    private void updateChecked(JFXCheckBox checkbox) {
        if (checkbox.isSelected()) {
            numChecked++;
            if (numChecked >= maximumChecked) {
                disableUncheckedCheckboxes();
            }
        } else {
            numChecked--;
            enableAllCheckboxes();
        }
    }

    private void disableUncheckedCheckboxes() {
        if (!totalRevenueCheck.isSelected()) {
            totalRevenueCheck.setDisable(true);
        }
        if (!topProductCheck.isSelected()) {
            topProductCheck.setDisable(true);
        }
        if (!topProductLineCheck.isSelected()) {
            topProductLineCheck.setDisable(true);
        }
        if (!topCustomerCheck.isSelected()) {
            topCustomerCheck.setDisable(true);
        }
        if (!topStoreCheck.isSelected()) {
            topStoreCheck.setDisable(true);
        }
    }

    private void enableAllCheckboxes() {
        totalRevenueCheck.setDisable(false);
        topProductCheck.setDisable(false);
        topProductLineCheck.setDisable(false);
        topCustomerCheck.setDisable(false);
        topStoreCheck.setDisable(false);
    }

    //region Employees Tab: list employees, employee's info: details, order, operations.
    @FXML
    void displayEmployeesTab() {
        tabPane.getSelectionModel().select(employeesOperationTab);
        employeesTabViewController.show();
    }



    //endregion
    @FXML
    SplitPane firstSplitPane;
    @FXML
    SplitPane secondSplitPane;

    @FXML
    HBox appName;
    @FXML
    ImageView smallAvatar;

    @Override
    protected void maximumStage(MouseEvent mouseEvent) {

    }

    @FXML
    StackPane menuPane;

    public void initialSetup() {
        user = new Employee(sqlConnection, loggerID);
        settingTabViewController.setUser(user);
        ViewController.setSqlConnection(sqlConnection);

        // Load current UI.

        usernameText.setText(user.getFullName());

        firstSplitPane.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
        ((StackPane) firstSplitPane.getItems().get(0)).setMinHeight(0.06 * Screen.getPrimary().getVisualBounds().getHeight());
        ((SplitPane) firstSplitPane.getItems().get(1)).setMinHeight(0.94 * Screen.getPrimary().getVisualBounds().getHeight());
        secondSplitPane.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());
        ((AnchorPane) secondSplitPane.getItems().get(0)).setMinWidth(0.1667 * Screen.getPrimary().getVisualBounds().getWidth());
        ((TabPane) secondSplitPane.getItems().get(1)).setMinWidth(0.8333 * Screen.getPrimary().getVisualBounds().getWidth());

        Insets hboxMargin = new Insets(0, 0.8333 * Screen.getPrimary().getVisualBounds().getWidth(), 0, 0);
        StackPane.setMargin(appName, hboxMargin);


        Circle clip = new Circle();
        clip.setRadius(35);
        clip.setCenterX(35);
        clip.setCenterY(35);
        smallAvatar.setClip(clip);

        currentTabButton = dashBoardTabButton;


        //TODO: test area
//        ScrollPane scroll = (ScrollPane) detailsInfoBox.getParent().getParent().getParent();
//        Transition down = new Transition() {
//            {
//                setCycleDuration(Duration.INDEFINITE);
//            }
//
//            @Override
//            protected void interpolate(double v) {
//                scroll.setVvalue(scroll.getVvalue() + 0.001);
//            }
//        };
//
//        Transition up = new Transition() {
//            {
//                setCycleDuration(Duration.INDEFINITE);
//            }
//
//            @Override
//            protected void interpolate(double v) {
//                scroll.setVvalue(scroll.getVvalue() - 0.001);
//            }
//        };


        // Load UI for others.
        runTask(() -> {
            // Load small avatar.
            smallAvatar.setImage(ImageController.getImage("avatar_employee_" + user.getEmployeeNumber() + ".png", true));


            // Employees Tab Preparation.


        }, null, null, null);
    }


    private Employee user;
    public static boolean haveJustOpened = false;
    public static int loggerID = -1;
    public static boolean haveChangeInEmployeesTab = false;
    public static boolean haveChangeInOrdersTab = false;
    public static boolean haveChangeInHomeTab = false;
    public static boolean haveChangeInSettingTab = false;
    public static boolean haveChangeInProductTab = false;
    public AnimationTimer loginDataListener = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if (MainSceneController.loggerID > 0) {
                runTask(() -> {
                    Platform.runLater(() -> {
                        stage.setScene(MainSceneController.this.scene);
                        stage.hide();
                        initialSetup();
                        stage.setX(0);
                        stage.setY(0);
                        stage.show();
                        displayDashBoardTab();
                    });

                }, null, null, null);
                stop();
            }
        }
    };
    @FXML
    ComboBox<String> statusInput;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/employees-tab/employees-tab-view.fxml"));
            loader.load();
            employeesTabViewController = loader.getController();
            employeesOperationTab.setContent(employeesTabViewController.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/settings-tab/setting-tab-view.fxml"));
            loader.load();
            settingTabViewController = loader.getController();
            settingTab.setContent(settingTabViewController.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/customers-tab/customers-tab-view.fxml"));
            loader.load();
            customersTabViewController = loader.getController();
            customersTab.setContent(customersTabViewController.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/products-tab/products-tab-view.fxml"));
            loader.load();
            productsTabViewController = loader.getController();
            productsOperationTab.setContent(productsTabViewController.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/orders-tab/orders-tab-view.fxml"));
            loader.load();
            ordersTabViewController = loader.getController();
            ordersTab.setContent(ordersTabViewController.getRoot());

            loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/dashboard-tab/dashboard-tab-view.fxml"));
            loader.load();
            dashboardTabViewController = loader.getController();
            dashBoardTab.setContent(dashboardTabViewController.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        statusInput.getItems().add("Cancelled");
        statusInput.getItems().add("Disputed");
        statusInput.getItems().add("In Process");
        statusInput.getItems().add("On Hold");
        statusInput.getItems().add("Resolved");
        statusInput.getItems().add("Shipped");

        productCodeOD.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        quantityOD.setCellValueFactory(new PropertyValueFactory<>("quantityOrdered"));
        priceEachOD.setCellValueFactory(new PropertyValueFactory<>("priceEach"));
        totalOD.setCellValueFactory(param -> {
            OrderItem orderItem = param.getValue();
            int quantity = orderItem.getQuantityOrdered();
            double price = orderItem.getPriceEach();
            double total = quantity * price;
            return new SimpleStringProperty(String.format("%.2f", total));
        });

        orderDetailsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        orderDetailsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                priceEachInput.setText(String.valueOf(newSelection.getPriceEach()));
                productCodeInput.setText(newSelection.getProductCode());
                quantityInput.setText(String.valueOf(newSelection.getQuantityOrdered()));
            }
        });

//        orderDetailsTable.getItems().addListener((ListChangeListener<OrderItem>) c -> {
//            while (c.next()) {
//                if (c.wasAdded()) {
//                    double addedTotal = c.getAddedSubList().stream()
//                            .mapToDouble(OrderItem::getAmount)
//                            .sum();
//                    totalAmount.set(totalAmount.get() + addedTotal);
//                } else if (c.wasRemoved()) {
//                    double removedTotal = c.getRemoved().stream()
//                            .mapToDouble(OrderItem::getAmount)
//                            .sum();
//                    totalAmount.set(totalAmount.get() - removedTotal);
//                }
//            }
//        });

//        orderDetailsTable.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (!newVal && !removeItemButtonClicked) {
//                orderDetailsTable.getSelectionModel().clearSelection();
//            }
//            removeItemButtonClicked = false;
//        });
//
//        productsTable.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (!newVal && !removeProductButtonClicked) {
//                productsTable.getSelectionModel().clearSelection();
//            }
//            removeProductButtonClicked = false;
//        });
//
//        ordersTable.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (!newVal && !removeOrderButtonClicked) {
//                ordersTable.getSelectionModel().clearSelection();
//            }
//            removeOrderButtonClicked = false;
//        });

        // Add a listener to the text property of the text field
        productCodeInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 2 && newValue.length() > oldValue.length() && suggestionList.getItems().isEmpty()) {

            } else {
                // Update the suggestion list based on the current input
                if (newValue.equals("")) {
                    suggestionList.setVisible(false);
                    suggestionList.setMouseTransparent(true);
                } else {
                    if (newValue.length() > 1) {
                        List<String> suggestions = new ArrayList<>(); // Replace with your own function to retrieve suggestions
                        String sql = "SELECT productCode, sellPrice FROM products WHERE upper(productCode) LIKE upper('" + newValue + "%');";
                        ResultSet rs = sqlConnection.getDataQuery(sql);
                        try {
                            // Add each suggestion to the list
                            while (rs.next()) {
                                suggestions.add(rs.getString("productCode"));
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        if (suggestions.isEmpty() || (!suggestions.isEmpty() && suggestions.get(0).equals(newValue))) {
                            suggestionList.setMouseTransparent(true);
                            suggestionList.setVisible(false);
                            suggestionList.getItems().clear();
                            if (!suggestions.isEmpty() && suggestions.get(0).equals(newValue)) {
                                setPrice();
                            }
                        } else {
                            suggestionList.getItems().setAll(suggestions);
                            suggestionList.getSelectionModel().selectFirst();
                            suggestionList.setMouseTransparent(false);
                            suggestionList.setVisible(true);
                        }
                    }
                }
            }

        });

        // Add an event handler to the suggestion list
        suggestionList.setOnMouseClicked(event -> {
            String selectedValue = suggestionList.getSelectionModel().getSelectedItem();
            if (selectedValue != null) {
                productCodeInput.setText(selectedValue);
                setPrice();
                suggestionList.setVisible(false);
                suggestionList.getItems().clear();
            }
        });

        productCodeInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String selectedValue = suggestionList.getSelectionModel().getSelectedItem();
                if (selectedValue != null) {
                    productCodeInput.setText(selectedValue);
                    setPrice();
                    suggestionList.setVisible(false);
                    suggestionList.getItems().clear();
                    productCodeInput.requestFocus();
                }
            } else if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.DOWN) {
                int selectedIndex = suggestionList.getSelectionModel().getSelectedIndex();
                if (selectedIndex < suggestionList.getItems().size() - 1) {
                    suggestionList.getSelectionModel().selectNext();
                    suggestionList.scrollTo(selectedIndex + 1);
                }
                event.consume();
                productCodeInput.requestFocus();
            } else if (event.getCode() == KeyCode.UP) {
                int selectedIndex = suggestionList.getSelectionModel().getSelectedIndex();
                if (selectedIndex > 0) {
                    suggestionList.getSelectionModel().selectPrevious();
                    suggestionList.scrollTo(selectedIndex - 1);
                }
                event.consume();
                productCodeInput.requestFocus();
            }
        });

        customerNameInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && deliverInput.isEditable() && !phoneNumberInput.getText().equals("")) {
                String query = String.format("SELECT addressLine FROM customers WHERE customerName = '%s' AND phone = '%s'", customerNameInput.getText(), phoneNumberInput.getText());
                ResultSet rs = sqlConnection.getDataQuery(query);
                try {
                    if (rs.next()) {
                        deliverInput.setText(rs.getString(1));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        phoneNumberInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && deliverInput.isEditable() && !customerNameInput.getText().equals("")) {
                String query = String.format("SELECT addressLine FROM customers WHERE customerName = '%s' AND phone = '%s'", customerNameInput.getText(), phoneNumberInput.getText());
                ResultSet rs = sqlConnection.getDataQuery(query);
                try {
                    if (rs.next()) {
                        deliverInput.setText(rs.getString(1));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        productLinePDetails.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // TextField has focus, show suggestion list
                if (!plSuggestionList.getItems().isEmpty()) {
                    plSuggestionList.setVisible(true);
                    plSuggestionList.setMouseTransparent(false);
                }
            } else {
                if (plSuggestionList.getItems().isEmpty()) {
                    plSuggestionList.setVisible(false);
                    plSuggestionList.setMouseTransparent(true);
                }
            }
        });

        plSuggestionList.setCellFactory(param -> new ListCell<>() {
            private final JFXButton removeButton = new JFXButton("X");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setGraphic(removeButton);
                    removeButton.setOnAction(event -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirm Delete");
                        alert.setHeaderText("Are you sure you want to delete this product line?");
                        alert.setContentText("This action will delete all products that are of this product line and cannot be undone.");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            // User clicked OK, so delete the item
                            String query = String.format("DELETE FROM productlines WHERE productLine = '%s'", item);
                            sqlConnection.updateQuery(query);
                            getListView().getItems().remove(item);
                            initProducts();
                        } else {
                            // User clicked Cancel or closed the dialog box, so do nothing
                            // ...
                        }
                    });
                }
            }
        });

        productLinePDetails.textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue.length() > 0 && newValue.length() > oldValue.length() && plSuggestionList.getItems().isEmpty()) {

            } else {
                // Update the suggestion list based on the current input
                List<String> suggestions = new ArrayList<>(); // Replace with your own function to retrieve suggestions
                String sql = "SELECT productLine FROM productlines WHERE upper(productLine) LIKE upper('" + newValue + "%');";
                ResultSet rs = sqlConnection.getDataQuery(sql);
                try {
                    // Add each suggestion to the list
                    while (rs.next()) {
                        suggestions.add(rs.getString("productLine"));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                if (suggestions.isEmpty() || (!suggestions.isEmpty() && suggestions.get(0).equals(newValue))) {
                    plSuggestionList.setMouseTransparent(true);
                    plSuggestionList.setVisible(false);
                    plSuggestionList.getItems().clear();
                } else {
                    plSuggestionList.getItems().setAll(suggestions);
                    plSuggestionList.getSelectionModel().selectFirst();
                    plSuggestionList.setMouseTransparent(false);
                    plSuggestionList.setVisible(true);
                }
            }
        });

        // Add an event handler to the suggestion list
        plSuggestionList.setOnMouseClicked(event -> {
            String selectedValue = plSuggestionList.getSelectionModel().getSelectedItem();
            if (selectedValue != null) {
                productLinePDetails.setText(selectedValue);
                plSuggestionList.setVisible(false);
                plSuggestionList.getItems().clear();
                plSuggestionList.setMouseTransparent(true);
            }
        });

        productLinePDetails.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String selectedValue = plSuggestionList.getSelectionModel().getSelectedItem();
                if (selectedValue != null) {
                    productLinePDetails.setText(selectedValue);
                    plSuggestionList.setVisible(false);
                    plSuggestionList.getItems().clear();
                    productLinePDetails.requestFocus();
                }
            } else if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.DOWN) {
                int selectedIndex = plSuggestionList.getSelectionModel().getSelectedIndex();
                if (selectedIndex < plSuggestionList.getItems().size() - 1) {
                    plSuggestionList.getSelectionModel().selectNext();
                    plSuggestionList.scrollTo(selectedIndex + 1);
                }
                event.consume();
                productLinePDetails.requestFocus();
            } else if (event.getCode() == KeyCode.UP) {
                int selectedIndex = plSuggestionList.getSelectionModel().getSelectedIndex();
                if (selectedIndex > 0) {
                    plSuggestionList.getSelectionModel().selectPrevious();
                    plSuggestionList.scrollTo(selectedIndex - 1);
                }
                event.consume();
                productLinePDetails.requestFocus();
            }
        });

        quantityInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                totalInput.setText("");
            } else if (!priceEachInput.getText().equals(""))
                totalInput.setText(String.format("%.2f", Integer.parseInt(newValue) * Double.parseDouble(priceEachInput.getText())));
        });

        priceEachInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                totalInput.setText("");
            } else if (!quantityInput.getText().equals(""))
                totalInput.setText(String.format("%.2f", Double.parseDouble(newValue) * Integer.parseInt(quantityInput.getText())));
        });

        totalAmountForOrder.textProperty().bind(totalAmount.asString("%.2f"));

        addOrderFilterButton.setOnAction(event -> {
            bgPaneOrders.setVisible(true);
            orderFilterPane.setVisible(true);
        });
        typeFilter.getItems().add("online");
        typeFilter.getItems().add("onsite");
        applyOrderFilterButton.setOnAction(event -> {
            updateOrderFilteredData();
            bgPaneOrders.setVisible(false);
            orderFilterPane.setVisible(false);
        });
        clearOrderFilterButton.setOnAction(event -> {
            customerNameFilter.clear();
            contactFilter.clear();
            createdFilter.clear();
            typeFilter.setValue(null);
            commentsFilter.clear();
            orderDateFilter.setValue(null);
            updateOrderFilteredData();
            bgPaneOrders.setVisible(false);
            orderFilterPane.setVisible(false);
        });
        closeOrderFilterButton.setOnAction(event -> {
            bgPaneOrders.setVisible(false);
            orderFilterPane.setVisible(false);
        });

        // Initialize columns
        orderNumberOrd.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        createdOrd.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        orderDateOrd.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        typeOrd.setCellValueFactory(new PropertyValueFactory<>("type"));
        commentsOrd.setCellValueFactory(new PropertyValueFactory<>("comments"));
        valueOrd.setCellValueFactory(new PropertyValueFactory<>("value"));
        customerNameOrd.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        contactOrd.setCellValueFactory(new PropertyValueFactory<>("contact"));

        ordersTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // check for double-click event
                Order selectedOrderRow = ordersTable.getSelectionModel().getSelectedItem();
                if (selectedOrderRow != null) {
                    initEditOrder(selectedOrderRow);
                    goToEditOrderTab();
                }
            }
        });

        editOrderButton.setOnAction(e -> {
            Order selectedRow = ordersTable.getSelectionModel().getSelectedItem();
            if (selectedRow != null) {
                initEditOrder(selectedRow);
                goToEditOrderTab();
            }
        });

        editProductButton.setOnAction(e -> {
            Product selected = productsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                initEditProductDetails(selected);
                bgPaneProducts.setVisible(true);
                productDetailsPane.setVisible(true);
            }
        });


        productsTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                Product selected = productsTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    initEditProductDetails(selected);
                    bgPaneProducts.setVisible(true);
                    productDetailsPane.setVisible(true);
                }
            }
        });

        closeProductDetailsButton.setOnAction(event -> {
            bgPaneProducts.setVisible(false);
            productDetailsPane.setVisible(false);
        });

        productCodeProd.setCellValueFactory(new PropertyValueFactory<>("productCode"));

        productNameProd.setCellValueFactory(new PropertyValueFactory<>("productName"));

        productLineProd.setCellValueFactory(new PropertyValueFactory<>("productLine"));

        productVendorProd.setCellValueFactory(new PropertyValueFactory<>("productVendor"));

        addProductFilterButton.setOnAction(event -> {
            bgPaneProducts.setVisible(true);
            productFilterPane.setVisible(true);
        });
        typeFilter.getItems().add("online");
        typeFilter.getItems().add("onsite");
        applyProductFilterButton.setOnAction(event -> {
            updateProductFilteredData();
            bgPaneProducts.setVisible(false);
            productFilterPane.setVisible(false);
        });
        clearProductFilterButton.setOnAction(event -> {
            productCodeFilter.clear();
            productNameFilter.clear();
            productLineFilter.clear();
            productVendorFilter.clear();
            updateProductFilteredData();
            bgPaneProducts.setVisible(false);
            productFilterPane.setVisible(false);
        });
        closeProductFilterButton.setOnAction(event -> {
            bgPaneProducts.setVisible(false);
            productFilterPane.setVisible(false);
        });

        removeItemButton.setOnAction(event -> {
            removeItemButtonClicked = true;
            NotificationSystem.throwNotification(NotificationCode.SUCCEED_DELETE_PRODUCT, stage);
            removeItems();
        });

        removeProductButton.setOnAction(event -> {
            removeProductButtonClicked = true;
            Product selected = productsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setHeaderText("Are you sure you want to delete this product?");
                alert.setContentText("This action cannot be undone.");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // User clicked OK, so delete the item
                    String query = String.format("DELETE FROM products WHERE productCode = '%s'", selected.getProductCode());
                    sqlConnection.updateQuery(query);
                    initProducts();
                } else {
                    // User clicked Cancel or closed the dialog box, so do nothing
                    // ...
                }
            }
        });

        createOrderButton.setOnAction(e -> {
            String type = "";
            // Create a ChoiceDialog to ask the user for the order type
            ChoiceDialog<String> dialog = new ChoiceDialog<>("onsite", "onsite", "online");
            dialog.setTitle("Order Type");
            dialog.setHeaderText("Select the order type:");
            dialog.setContentText("Order Type:");

            // Show the dialog and wait for the user's response
            dialog.showAndWait().ifPresent(orderType -> {
                // Handle the user's selection
                goToCreateOrderTab();
                initCreateOrder(orderType);
            });
        });

        removeOrderButton.setOnAction(event -> {
            removeOrderButtonClicked = true;
            Order selected = ordersTable.getSelectionModel().getSelectedItem();
            int selectedIndex = ordersTable.getSelectionModel().getSelectedIndex();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setHeaderText("Are you sure you want to delete this order?");
                alert.setContentText("This action cannot be undone.");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // User clicked OK, so delete the item
                    handleRemoveOrder();
                } else {
                    // User clicked Cancel or closed the dialog box, so do nothing
                    // ...
                }
            }
        });



    }

    private void initCreateOrder(String type) {
        if (type.equals("online")) {
            requiredDateInput.setVisible(true);
            requiredDateText.setVisible(true);
            shippedDateInput.setVisible(true);
            shippedDateText.setVisible(true);
            deliverInput.setVisible(true);
            deliverText.setVisible(true);
            deliverInput.setEditable(true);
            statusInput.setVisible(true);
            paymentMethodInput.getItems().clear();
            paymentMethodInput.getItems().addAll("Cash On Delivery", "Credit Card", "Debit Card", "E-Wallet", "Bank Transfer");
            paymentMethodInput.setValue(null);
        } else {
            requiredDateInput.setVisible(false);
            requiredDateText.setVisible(false);
            shippedDateInput.setVisible(false);
            shippedDateText.setVisible(false);
            deliverInput.setVisible(false);
            deliverText.setVisible(false);
            deliverInput.setEditable(false);
            statusInput.setVisible(false);
            paymentMethodInput.getItems().clear();
            paymentMethodInput.getItems().addAll("Cash", "Credit Card", "Debit Card", "E-Wallet", "Bank Transfer");
            paymentMethodInput.setValue(null);
        }
        clearCreateOrderTab();
        totalAmount.set(0);
        customerNameInput.setEditable(true);
        phoneNumberInput.setEditable(true);
        submitOrderButton.setText("Create Order");
        submitOrderButton.setOnAction(event -> {
            createOrder(type);
        });
        orderDetailsTable.setItems(getList());
    }

    private void initEditOrder(Order selectedOrderRow) {
        clearCreateOrderTab();
        submitOrderButton.setText("Save");
        customerNameInput.setEditable(false);
        phoneNumberInput.setEditable(false);
        totalAmount.set(0);
        if (selectedOrderRow != null) {

            // Extract the order number from the selected row
            int orderNumber = selectedOrderRow.getOrderNumber();

            printInvoiceButton.setOnAction(event -> {
                printInvoice(orderNumber);
            });

            // Query the orderdetails table to get the order details information for the selected order
            String query = String.format("SELECT productCode, quantityOrdered, priceEach FROM orderdetails WHERE orderNumber = %d", orderNumber);
            ResultSet rs = sqlConnection.getDataQuery(query);
            try {
                while (rs.next()) {
                    // Extract the relevant information from the result set
                    String productCode = rs.getString("productCode");
                    int quantity = rs.getInt("quantityOrdered");
                    double priceEach = rs.getDouble("priceEach");
                    OrderItem orderItem = new OrderItem(productCode, quantity, priceEach);
                    orderDetailsTable.getItems().add(orderItem);
                    totalAmount.set(totalAmount.get() + orderItem.getAmount());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


            if (selectedOrderRow.getType().equals("online")) {
                requiredDateInput.setVisible(true);
                requiredDateText.setVisible(true);
                shippedDateInput.setVisible(true);
                shippedDateText.setVisible(true);
                deliverInput.setVisible(true);
                deliverText.setVisible(true);
                deliverInput.setEditable(true);
                statusInput.setVisible(true);
                paymentMethodInput.getItems().clear();
                paymentMethodInput.getItems().addAll("Cash On Delivery", "Credit Card", "Debit Card", "E-Wallet", "Bank Transfer");

                requiredDateInput.setValue(selectedOrderRow.getRequiredDate());
                shippedDateInput.setValue(selectedOrderRow.getShippedDate());

                deliverInput.setText(selectedOrderRow.getDestination());

                // Set the status combo box to the value from the selected ord
                statusInput.setValue(selectedOrderRow.getStatus());
                paymentMethodInput.setValue(selectedOrderRow.getPayment_method());
            } else {
                requiredDateInput.setVisible(false);
                requiredDateText.setVisible(false);
                shippedDateInput.setVisible(false);
                shippedDateText.setVisible(false);
                deliverInput.setVisible(false);
                deliverText.setVisible(false);
                deliverInput.setEditable(false);
                statusInput.setVisible(false);
                paymentMethodInput.getItems().clear();
                paymentMethodInput.getItems().addAll("Cash", "Credit Card", "Debit Card", "E-Wallet", "Bank Transfer");

                paymentMethodInput.setValue(selectedOrderRow.getPayment_method());
            }


//            int customerNumber = -1;
//            query = String.format("SELECT customerNumber FROM customers WHERE customerName = '%s' AND phone = '%s'", selectedOrderRow.getCustomerName(), selectedOrderRow.getContact());
//            rs = sqlConnection.getDataQuery(query);
//            try {
//                if (rs.next()) {
//                    customerNumber = rs.getInt("customerNumber");
//                }
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }

            // Set the date pickers and comments text field with values from the selected order
            LocalDate orderDate = selectedOrderRow.getOrderDate();
            String comments = selectedOrderRow.getComments();
            orderDateInput.setValue(orderDate);
            commentsInput.setText(comments);

            customerNameInput.setText(selectedOrderRow.getCustomerName());
            phoneNumberInput.setText(selectedOrderRow.getContact());

            submitOrderButton.setOnAction(event -> {
                editOrder(orderNumber);
            });
        }
    }

    private void editOrder(int orderNumber) {
        runTask(() -> {
            String query = String.format("DELETE FROM orderdetails WHERE orderNumber = %d", orderNumber);
            sqlConnection.updateQuery(query);
            for (OrderItem orderItem : orderDetailsTable.getItems()) {
                String productCode = orderItem.getProductCode();
                int quantity = orderItem.getQuantityOrdered();
                double priceEach = orderItem.getPriceEach();
                query = String.format("INSERT INTO orderdetails (orderNumber, productCode, quantityOrdered, priceEach) VALUES (%d, '%s', %d, %f)", orderNumber, productCode, quantity, priceEach);
                sqlConnection.updateQuery(query);
            }

            // Get the values from the input fields
            LocalDate orderDate = orderDateInput.getValue();
            String requiredDate = requiredDateInput.getValue() != null ? "'" + requiredDateInput.getValue().toString() + "'" : null;
            String shippedDate = shippedDateInput.getValue() != null ? "'" + shippedDateInput.getValue().toString() + "'" : "null";
            String status = (String) statusInput.getValue();
            String comments = commentsInput.getText();

            query = String.format("UPDATE orders SET orderDate = '%s', requiredDate = %s, shippedDate = %s, status = '%s', comments = '%s', payment_method = '%s', deliver_to = '%s' WHERE orderNumber = %d", orderDate.toString(), requiredDate, shippedDate, status, comments, paymentMethodInput.getValue(), deliverInput.getText(), orderNumber);
            sqlConnection.updateQuery(query);

        }, null, progressIndicator, createOrderTab.getTabPane());
        customerNameInput.setEditable(true);
        phoneNumberInput.setEditable(true);

        NotificationSystem.throwNotification(NotificationCode.SUCCEED_EDIT_ORDER, stage);

        goToOrdersTab();
    }

    private void setPrice() {
        String productCode = productCodeInput.getText();

        // Query the database for the sellPrice of the product with the given code
        double sellPrice = -1;

        String sql = "SELECT sellPrice FROM products WHERE productCode = '" + productCode + "';";
        ResultSet result = sqlConnection.getDataQuery(sql);
        try {
            if (result.next()) {
                sellPrice = result.getDouble("sellPrice");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set the sellPrice as the text of the priceEachInput
        priceEachInput.setText(Double.toString(sellPrice));
    }

    public ObservableList<OrderItem> getList() {
        ObservableList<OrderItem> items = FXCollections.observableArrayList();
        return items;
    }

    public void addItem() {
        String productCode = productCodeInput.getText();
        int quantity = Integer.parseInt(quantityInput.getText());
        double priceEach = Double.parseDouble(priceEachInput.getText());

        // Check if an order with the same productCode already exists
        for (OrderItem orderItem : orderDetailsTable.getItems()) {
            if (orderItem.getProductCode().equals(productCode)) {
                // Update the existing order
                totalAmount.set(totalAmount.get() - orderItem.getAmount() + quantity * priceEach);
                orderItem.setQuantityOrdered(quantity);
                orderItem.setPriceEach(priceEach);
                orderDetailsTable.refresh();
                return;
            }
        }

        // If no existing order was found, create a new one and add it to the tableView
        OrderItem orderItem = new OrderItem(productCode, quantity, priceEach);
        orderDetailsTable.getItems().add(orderItem);

        totalAmount.set(totalAmount.get() + quantity * priceEach);

        productCodeInput.clear();
        quantityInput.clear();
        priceEachInput.clear();
    }

    public void removeItems() {
        ObservableList<OrderItem> selectedRows, allItems;
        allItems = orderDetailsTable.getItems();

        selectedRows = orderDetailsTable.getSelectionModel().getSelectedItems();

        for (OrderItem orderItem : selectedRows) {
            totalAmount.set(totalAmount.get() - orderItem.getAmount());
        }

        allItems.removeAll(selectedRows);
    }

    public void clearItems() {
        orderDetailsTable.getItems().clear();
        totalAmount.set(0);
    }

    @FXML
    TableView<OrderItem> orderDetailsTable;
    @FXML
    TableColumn<OrderItem, String> productCodeOD;
    @FXML
    TableColumn<OrderItem, Integer> quantityOD;
    @FXML
    TableColumn<OrderItem, Double> priceEachOD;
    @FXML
    TableColumn<OrderItem, String> totalOD;
    @FXML
    JFXTextField productCodeInput;
    @FXML
    ListView<String> suggestionList;
    @FXML
    JFXTextField quantityInput;
    @FXML
    JFXTextField priceEachInput;
    @FXML
    JFXTextField totalInput;
    @FXML
    JFXButton addButton;
    @FXML
    JFXButton removeItemButton;
    @FXML
    TextField totalAmountForOrder;
    private DoubleProperty totalAmount = new SimpleDoubleProperty(0);

    private boolean removeItemButtonClicked = false;
    @FXML
    DatePicker orderDateInput;
    @FXML
    DatePicker requiredDateInput;
    @FXML
    Text requiredDateText;
    @FXML
    DatePicker shippedDateInput;
    @FXML
    Text shippedDateText;
    @FXML
    JFXTextField deliverInput;
    @FXML
    Text deliverText;
    @FXML
    JFXTextField commentsInput;
    @FXML
    ComboBox<String> paymentMethodInput;
    @FXML
    JFXButton submitOrderButton;
    @FXML
    JFXButton printInvoiceButton;
    @FXML
    JFXTextField customerNameInput;
    @FXML
    JFXTextField phoneNumberInput;

    public void createOrder(String type) {
        runTask(() -> {
            String orderDate;
            if (orderDateInput.getValue() == null) {
                orderDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
            } else {
                orderDate = orderDateInput.getValue().format(DateTimeFormatter.ISO_DATE);
            }
            String shippedDate;
            String requiredDate;
            if (type.equals("online")) {
                if (shippedDateInput.getValue() != null) {
                    shippedDate = shippedDateInput.getValue().format(DateTimeFormatter.ISO_DATE);
                    shippedDate = "'" + shippedDate;
                    shippedDate += "'";
                } else {
                    shippedDate = "null";
                }
                requiredDate = "'" + requiredDateInput.getValue().format(DateTimeFormatter.ISO_DATE) + "'";
            } else {
                shippedDate = "null";
                requiredDate = "null";
            }
            String check = "SELECT customerNumber FROM customers WHERE customerName = '" + customerNameInput.getText() + "' AND phone = '" + phoneNumberInput.getText() + "';";
            ResultSet result = sqlConnection.getDataQuery(check);
            int customerNumber = -1;
            try {
                if (result.next()) {
                    customerNumber = result.getInt("customerNumber");
                } else {
                    check = "INSERT INTO customers (customerName, phone) VALUES ('" + customerNameInput.getText() + "', '" + phoneNumberInput.getText() + "')";
                    sqlConnection.updateQuery(check);
                    check = "SELECT LAST_INSERT_ID() FROM customers;";
                    result = sqlConnection.getDataQuery(check);
                    if (result.next()) {
                        customerNumber = result.getInt(1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            double value = 0;
            for (OrderItem item : orderDetailsTable.getItems()) {
                value += item.getAmount();
            }

            String customerRank = String.format("SELECT `rank` FROM customers WHERE customerNumber = %d", customerNumber);
            result = sqlConnection.getDataQuery(customerRank);
            try {
                if (result.next()) {
                    String rank = result.getString(1);
                    switch (rank) {
                        case "Emerald":
                            double discount_by = value * 0.25;
                            if (discount_by >= 200) {
                                discount_by = 200;
                            }
                            value -= discount_by;
                            break;
                        case "Diamond":
                            discount_by = value * 0.20;
                            if (discount_by >= 150) {
                                discount_by = 150;
                            }
                            value -= discount_by;
                            break;
                        case "Platinum":
                            discount_by = value * 0.15;
                            if (discount_by >= 100) {
                                discount_by = 100;
                            }
                            value -= discount_by;
                            break;
                        case "Gold":
                            discount_by = value * 0.1;
                            if (discount_by >= 50) {
                                discount_by = 50;
                            }
                            value -= discount_by;
                            break;
                        case "Silver":
                            discount_by = value * 0.05;
                            if (discount_by >= 20) {
                                discount_by = 20;
                            }
                            value -= discount_by;
                            break;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String order = "insert into orders(orderDate, requiredDate, shippedDate, status, comments, customerNumber, type, value, payment_method, created_by) values ('"
                    + orderDate + "',"
                    + requiredDate + ","
                    + shippedDate + ",'"
                    + statusInput.getValue() + "','"
                    + commentsInput.getText() + "',"
                    + customerNumber + ", '"
                    + type + "', "
                    + value + ", '"
                    + paymentMethodInput.getValue() + "'," +
                    loggerID + ");";
            sqlConnection.updateQuery(order);
            result = sqlConnection.getDataQuery("SELECT LAST_INSERT_ID() FROM orders;");

            int orderNumber = 0;
            try {
                if (result.next()) {
                    orderNumber = result.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            StringBuilder orderdetails = new StringBuilder("insert into orderdetails values");
            ObservableList<OrderItem> items = orderDetailsTable.getItems();

            for (OrderItem item : items) {
                orderdetails.append("(").append(orderNumber)
                        .append(", '")
                        .append(item.getProductCode())
                        .append("',")
                        .append(item.getQuantityOrdered())
                        .append(",")
                        .append(item.getPriceEach())
                        .append("),");
            }
            orderdetails.deleteCharAt(orderdetails.length() - 1);
            orderdetails.append(';');
            sqlConnection.updateQuery(orderdetails.toString());

            int finalOrderNumber = orderNumber;
            printInvoiceButton.setOnAction(event -> {
                printInvoice(finalOrderNumber);
            });

            int countOrd = -1;
            double totalValue = -1;
            if (customerNumber != 6) {
                String customerRankCheck = "SELECT COUNT(*) AS num, SUM(value) AS totalValue" +
                        "  FROM orders" +
                        "  WHERE customerNumber = " + customerNumber +
                        "    AND orderDate >= DATE_SUB(NOW(), INTERVAL 3 MONTH);";
                result = sqlConnection.getDataQuery(customerRankCheck);

                try {
                    if (result.next()) {
                        countOrd = result.getInt(1);
                        totalValue = result.getDouble(2);
                        String updateRank;
                        if (countOrd >= 100 || totalValue >= 10000) {
                            updateRank = String.format("UPDATE customers SET `rank` = 'Emerald' WHERE customerNumber = %d;", customerNumber);
                            sqlConnection.updateQuery(updateRank);
                        } else if (countOrd >= 40 || totalValue >= 5000) {
                            updateRank = String.format("UPDATE customers SET `rank` = 'Diamond' WHERE customerNumber = %d;", customerNumber);
                            sqlConnection.updateQuery(updateRank);
                        } else if (countOrd >= 20 || totalValue >= 1500) {
                            updateRank = String.format("UPDATE customers SET `rank` = 'Platinum' WHERE customerNumber = %d;", customerNumber);
                            sqlConnection.updateQuery(updateRank);
                        } else if (countOrd >= 10 || totalValue >= 1000) {
                            updateRank = String.format("UPDATE customers SET `rank` = 'Gold' WHERE customerNumber = %d;", customerNumber);
                            sqlConnection.updateQuery(updateRank);
                        } else if (countOrd >= 3 || totalValue >= 300) {
                            updateRank = String.format("UPDATE customers SET `rank` = 'Silver' WHERE customerNumber = %d;", customerNumber);
                            sqlConnection.updateQuery(updateRank);
                        } else {
                            updateRank = String.format("UPDATE customers SET `rank` = 'Membership' WHERE customerNumber = %d", customerNumber);
                            sqlConnection.updateQuery(updateRank);
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }, null, progressIndicator, createOrderTab.getTabPane());

        NotificationSystem.throwNotification(NotificationCode.SUCCEED_CREATE_ORDER, stage);

        goToOrdersTab();
    }


    @FXML
    void tabSelectingEffect(Event event) {
        HBox hbox = (HBox) currentTabButton.getGraphic();
        Label label = (Label) hbox.getChildren().get(1);
        label.setTextFill(Color.valueOf("#7c8db5"));
        ImageView buttonIcon = (ImageView) hbox.getChildren().get(0);
        if (currentTabButton.equals(dashBoardTabButton)) buttonIcon.setImage(ImageController.newsIcon);
        else if (currentTabButton.equals(ordersTabButton)) buttonIcon.setImage(ImageController.orderIcon);
        else if (currentTabButton.equals(productsTabButton)) buttonIcon.setImage(ImageController.productIcon);
        else if (currentTabButton.equals(employeesTabButton)) buttonIcon.setImage(ImageController.employeeIcon);
        else if (currentTabButton.equals(settingsTabButton)) buttonIcon.setImage(ImageController.settingsIcon);
        currentTabButton.setStyle("-fx-border-color: transparent;-fx-background-color :#ffffff; -fx-border-width: 0 0 0 4; -fx-border-radius: 0;");

        currentTabButton = (JFXButton) event.getSource();
        hbox = (HBox) currentTabButton.getGraphic();
        label = (Label) hbox.getChildren().get(1);
        label.setTextFill(Color.valueOf("#329cfe"));
        buttonIcon = (ImageView) hbox.getChildren().get(0);
        if (currentTabButton.equals(dashBoardTabButton)) buttonIcon.setImage(ImageController.blueNewsIcon);
        else if (currentTabButton.equals(ordersTabButton)) buttonIcon.setImage(ImageController.blueOrderIcon);
        else if (currentTabButton.equals(productsTabButton)) buttonIcon.setImage(ImageController.blueProductIcon);
        else if (currentTabButton.equals(employeesTabButton)) buttonIcon.setImage(ImageController.blueEmployeeIcon);
        else if (currentTabButton.equals(settingsTabButton)) buttonIcon.setImage(ImageController.blueSettingsIcon);

        currentTabButton.setStyle("-fx-border-color: #60b1fd; -fx-background-color : #fafafa;-fx-border-width: 0 0 0 4; -fx-border-radius: 0;");
    }

    /**
     * Handle ORDERS tab.
     */
    @FXML
    TableView<Order> ordersTable;
    @FXML
    TableColumn<Order, Integer> orderNumberOrd;
    @FXML
    TableColumn<Order, String> createdOrd;
    @FXML
    TableColumn<Order, LocalDate> orderDateOrd;
    @FXML
    TableColumn<Order, String> typeOrd;
    @FXML
    TableColumn<Order, String> commentsOrd;
    @FXML
    TableColumn<Order, String> valueOrd;
    @FXML
    TableColumn<Order, String> customerNameOrd;
    @FXML
    TableColumn<Order, String> contactOrd;
    @FXML
    JFXButton createOrderButton;
    @FXML
    JFXButton editOrderButton;
    @FXML
    JFXButton removeOrderButton;
    @FXML
    JFXButton addOrderFilterButton;
    @FXML
    Pane bgPaneOrders;
    @FXML
    AnchorPane orderFilterPane;
    @FXML
    JFXTextField customerNameFilter;
    @FXML
    JFXTextField contactFilter;
    @FXML
    ComboBox<String> typeFilter;
    @FXML
    JFXTextField createdFilter;
    @FXML
    DatePicker orderDateFilter;
    @FXML
    JFXTextField commentsFilter;
    @FXML
    JFXButton applyOrderFilterButton;
    @FXML
    JFXButton clearOrderFilterButton;
    @FXML
    JFXButton closeOrderFilterButton;
    FilteredList<Order> filteredOrders;
    SortedList<Order> sortedAndFilteredOrders;
    private boolean removeOrderButtonClicked = false;

    void initOrders() {
        runTask(() -> {
            ObservableList<Order> orders = FXCollections.observableArrayList();
            ordersTable.setItems(orders);

            try {
                String query = "SELECT orderNumber, CONCAT(lastName, ' ', firstName)  AS name, created_by, orderDate, requiredDate, shippedDate, orders.status, type, comments, value, payment_method, customerName, customers.phone, addressLine, deliver_to FROM orders INNER JOIN customers ON orders.customerNumber = customers.customerNumber INNER JOIN employees ON orders.created_by = employees.employeeNumber";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                while (resultSet.next()) {
                    Order order = new Order(
                            resultSet.getInt("orderNumber"),
                            resultSet.getInt("created_by"),
                            resultSet.getString("name"),
                            resultSet.getDate("orderDate").toLocalDate(),
                            resultSet.getDate("requiredDate") != null ? resultSet.getDate("requiredDate").toLocalDate() : null,
                            resultSet.getDate("shippedDate") != null ? resultSet.getDate("shippedDate").toLocalDate() : null,
                            resultSet.getString("status"),
                            resultSet.getString("comments"),
                            resultSet.getString("customerName"),
                            resultSet.getString("phone"),
                            resultSet.getString("type"),
                            resultSet.getDouble("value"),
                            resultSet.getString("payment_method"),
                            resultSet.getString("deliver_to")
                    );
                    ordersTable.getItems().add(order);
                }
                ordersTable.refresh();

                filteredOrders = new FilteredList<>(FXCollections.observableArrayList(ordersTable.getItems()), p -> true);
                sortedAndFilteredOrders = new SortedList<>(filteredOrders);
                ordersTable.setItems(sortedAndFilteredOrders);
                sortedAndFilteredOrders.comparatorProperty().bind(ordersTable.comparatorProperty());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, null, progressIndicator, ordersTab.getTabPane());

    }

    public void handleRemoveOrder() {
        // Get the selected row in the TableView
        Order selectedRow = ordersTable.getSelectionModel().getSelectedItem();
        int selectedIndex = ordersTable.getSelectionModel().getSelectedIndex();

        // If a row is selected, delete the corresponding order from the database and TableView
        if (selectedRow != null) {
            int orderNumber = selectedRow.getOrderNumber();

            String deleteQuery = "DELETE FROM orderdetails WHERE orderNumber = " + orderNumber;
            // Delete the order from the database
            sqlConnection.updateQuery(deleteQuery);

            deleteQuery = "DELETE FROM orders WHERE orderNumber = " + orderNumber;
            // Delete the order from the database
            sqlConnection.updateQuery(deleteQuery);

            NotificationSystem.throwNotification(NotificationCode.SUCCEED_DELETE_ORDER, stage);

            initOrders();
        }
    }

    @FXML
    TableView<Product> productsTable;
    @FXML
    TableColumn<Product, String> productCodeProd;
    @FXML
    TableColumn<Product, String> productNameProd;
    @FXML
    TableColumn<Product, String> productLineProd;
    @FXML
    TableColumn<Product, String> productVendorProd;
    @FXML
    JFXButton productDetailsButton;
    @FXML
    JFXButton editProductButton;
    @FXML
    JFXButton addProductButton;
    @FXML
    JFXButton removeProductButton;
    @FXML
    JFXTextField productCodeFilter;
    @FXML
    JFXTextField productNameFilter;
    @FXML
    JFXTextField productLineFilter;
    @FXML
    JFXTextField productVendorFilter;
    @FXML
    JFXButton applyProductFilterButton;
    @FXML
    JFXButton clearProductFilterButton;
    @FXML
    JFXButton closeProductFilterButton;
    @FXML
    JFXButton addProductFilterButton;
    FilteredList<Product> filteredProducts;
    SortedList<Product> sortedAndFilteredProducts;

    private boolean removeProductButtonClicked = false;
    @FXML
    Pane bgPaneProducts;
    @FXML
    AnchorPane productDetailsPane;
    @FXML
    AnchorPane productFilterPane;
    @FXML
    JFXButton closeProductDetailsButton;

    void initProducts() {
        runTask(() -> {
            productsInit = true;
            ObservableList<Product> products = FXCollections.observableArrayList();
            productsTable.setItems(products);

            try {
                String query = "SELECT productCode, productName, productLine, productVendor, productDescription, quantityInStock, buyPrice, sellPrice FROM products";
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                while (resultSet.next()) {
                    Product product = new Product(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getInt(6),
                            resultSet.getDouble(7),
                            resultSet.getDouble(8)
                    );
                    productsTable.getItems().add(product);
                }
                productsTable.refresh();

                filteredProducts = new FilteredList<>(FXCollections.observableArrayList(productsTable.getItems()), p -> true);
                sortedAndFilteredProducts = new SortedList<>(filteredProducts);
                productsTable.setItems(sortedAndFilteredProducts);
                sortedAndFilteredProducts.comparatorProperty().bind(productsTable.comparatorProperty());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, null, progressIndicator, productsOperationTab.getTabPane());

    }

    @FXML
    JFXTextField productCodePDetails;
    @FXML
    JFXTextField productNamePDetails;
    @FXML
    JFXTextField productLinePDetails;
    @FXML
    ListView<String> plSuggestionList;
    @FXML
    JFXTextField productVendorPDetails;
    @FXML
    JFXTextField inStockPDetails;
    @FXML
    JFXTextField buyPricePDetails;
    @FXML
    JFXTextField sellPricePDetails;
    @FXML
    JFXTextField productDescriptionPDetails;

    public void initAddProduct() {
        productDetailsButton.setText("Add");
        productCodePDetails.setText("");
        productNamePDetails.setText("");
        productVendorPDetails.setText("");
        productLinePDetails.setText("");
        inStockPDetails.setText("");
        buyPricePDetails.setText("");
        sellPricePDetails.setText("");
        productDescriptionPDetails.setText("");

        bgPaneProducts.setVisible(true);
        productDetailsPane.setVisible(true);

        productDetailsButton.setOnAction(event -> {
            String query = String.format("SELECT productLine FROM productlines WHERE productLine = '%s'", productLinePDetails.getText());
            ResultSet rs = sqlConnection.getDataQuery(query);
            try {
                if (!rs.next()) {
                    query = String.format("insert into productlines(productLine) values ('%s')", productLinePDetails.getText());
                    sqlConnection.updateQuery(query);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            query = String.format("insert into products(productCode, productName, productLine, productVendor, productDescription, quantityInStock, buyPrice, sellPrice) " +
                            "VALUES ('%s', '%s', '%s', '%s', '%s', %d, %f, %f);", productCodePDetails.getText(), productNamePDetails.getText(),

                    productLinePDetails.getText(), productVendorPDetails.getText(), productDescriptionPDetails.getText(),
                    Integer.parseInt(inStockPDetails.getText()), Double.parseDouble(buyPricePDetails.getText()), Double.parseDouble(sellPricePDetails.getText()));
            sqlConnection.updateQuery(query);

            NotificationSystem.throwNotification(NotificationCode.SUCCEED_CREATE_PRODUCT, stage);

            bgPaneProducts.setVisible(false);
            productDetailsPane.setVisible(false);

            initProducts();

        });
    }

    void initEditProductDetails(Product selected) {
        productDetailsButton.setText("Save");
        productCodePDetails.setText(selected.getProductCode());
        productNamePDetails.setText(selected.getProductName());
        productLinePDetails.setText(selected.getProductLine());
        productVendorPDetails.setText(selected.getProductVendor());
        productDescriptionPDetails.setText(selected.getProductDescription());
        inStockPDetails.setText(String.valueOf(selected.getQuantityInStock()));
        buyPricePDetails.setText(String.valueOf(selected.getBuyPrice()));
        sellPricePDetails.setText(String.valueOf(selected.getSellPrice()));


        productDetailsButton.setOnAction(e -> {
            String query = String.format("SELECT productLine FROM productlines WHERE productLine = '%s'", productLinePDetails.getText());
            ResultSet rs = sqlConnection.getDataQuery(query);
            try {
                if (!rs.next()) {
                    query = String.format("insert into productlines(productLine) values ('%s')", productLinePDetails.getText());
                    sqlConnection.updateQuery(query);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            query = String.format("UPDATE products SET productCode = '%s', productName = '%s', productLine = '%s', productVendor = '%s', productDescription = '%s', quantityInStock = %d, buyPrice = %f, sellPrice = %f WHERE productCode = '%s'",
                    productCodePDetails.getText(), productNamePDetails.getText(), productLinePDetails.getText(), productVendorPDetails.getText(),
                    productDescriptionPDetails.getText(), Integer.parseInt(inStockPDetails.getText()),
                    Double.parseDouble(buyPricePDetails.getText()), Double.parseDouble(sellPricePDetails.getText()), selected.getProductCode());
            sqlConnection.updateQuery(query);

            selected.setProductCode(productCodePDetails.getText());
            selected.setProductName(productNamePDetails.getText());
            selected.setProductLine(productLinePDetails.getText());

            NotificationSystem.throwNotification(NotificationCode.SUCCEED_EDIT_PRODUCT, stage);

            productsTable.refresh();
        });
    }

    @FXML
    TableView<Customer> customersTable;
    @FXML
    TableColumn<Customer, String> nameCustomer;
    @FXML
    TableColumn<Customer, String> contactCustomer;
    @FXML
    TableColumn<Customer, String> addressCustomer;
    @FXML
    TableColumn<Customer, String> rankCustomer;
    FilteredList<Customer> filteredCustomers;
    SortedList<Customer> sortedAndFilteredCustomers;

    @FXML
    JFXButton removeCustomerButton;
    @FXML
    JFXButton addCustomerButton;
    @FXML
    JFXButton editCustomerButton;
    @FXML
    JFXButton addCustomerFilterButton;
    @FXML
    AnchorPane customerDetailsPane;
    @FXML
    Pane bgPaneCustomers;
    @FXML
    JFXTextField nameCDetails;
    @FXML
    JFXTextField contactCDetails;
    @FXML
    JFXTextField addressCDetails;
    @FXML
    JFXButton customerDetailsButton;
    @FXML
    JFXButton closeCustomerDetailsButton;
    @FXML
    AnchorPane customerFilterPane;
    @FXML
    JFXTextField nameCustomerFilter;
    @FXML
    JFXTextField contactCustomerFilter;
    @FXML
    JFXTextField addressCustomerFilter;
    @FXML
    JFXTextField rankCustomerFilter;
    @FXML
    JFXButton closeCustomerFilterButton;
    @FXML
    JFXButton applyCustomerFilterButton;
    @FXML
    JFXButton clearCustomerFilterButton;

    public void printInvoice(int orderNumber) {
        String sourceFile = "src/main/resources/salesmanagement/salesmanagement/invoice.jrxml";
        try {
            JasperReport jr = JasperCompileManager.compileReport(sourceFile);
            HashMap<String, Object> para = new HashMap<>();
            para.put("invoiceNo", "INV" + String.format("%04d", orderNumber));
            para.put("customerName", customerNameInput.getText());
            para.put("contact", phoneNumberInput.getText());
            para.put("totalAmount", "totalAmount");
            para.put("otherAmount", "discount");
            para.put("paybleAmount", "toBePaid");
            para.put("paidAmount", "paid");
            para.put("dueAmount", "due");
            para.put("comments", commentsInput.getText());
            para.put("point1", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
            para.put("point2", "If you have any questions concerning this invoice, use the following contact information:");
            para.put("point3", "+243 999999999, purchase@example.com");
            LocalDate orderDate = orderDateInput.getValue() != null ? orderDateInput.getValue() : LocalDate.now();
            para.put("orderYear", orderDate.getYear() - 1900);
            para.put("orderMonth", orderDate.getMonthValue() - 1);
            para.put("orderDay", orderDate.getDayOfMonth());
            para.put("paymentMethod", paymentMethodInput.getValue());
            String query = String.format("SELECT value, type, CONCAT(lastName, ' ', firstName) AS name, `rank` FROM orders INNER JOIN employees ON orders.created_by = employees.employeeNumber INNER JOIN customers ON orders.customerNumber = customers.customerNumber WHERE orderNumber = %d", orderNumber);
            ResultSet rs = sqlConnection.getDataQuery(query);
            if (rs.next()) {
                String rank = rs.getString(4);
                double left = 1;
                switch (rank) {
                    case "Emerald":
                        para.put("discount", "25%");
                        left = 0.75;
                        break;
                    case "Diamond":
                        para.put("discount", "20%");
                        left = 0.8;
                        break;
                    case "Platinum":
                        para.put("discount", "15%");
                        left = 0.85;
                        break;
                    case "Gold":
                        para.put("discount", "10%");
                        left = 0.9;
                        break;
                    case "Silver":
                        para.put("discount", "5%");
                        left = 0.95;
                        break;
                    case "Membership":
                        para.put("discount", "0%");
                        left = 1;
                        break;
                }
                para.put("totalAmount", rs.getDouble(1) / left);
                para.put("leftAmount", rs.getDouble(1));
                para.put("type", rs.getString(2).substring(0, 1).toUpperCase() + rs.getString(2).substring(1));
                para.put("employee", rs.getString(3));
            }

            ArrayList<OrderItem> plist = new ArrayList<>();

            for (OrderItem item : orderDetailsTable.getItems()) {
                plist.add(new OrderItem(item.getProductCode(), item.getQuantityOrdered(), item.getPriceEach()));
            }

            JRBeanCollectionDataSource jcs = new JRBeanCollectionDataSource(plist);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, jcs);
            JasperViewer.viewReport(jp, false);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void updateOrderFilteredData() {
        filteredOrders.setPredicate(order -> {
            // Get the filter text from the text fields
            String customerNameFilterText = customerNameFilter.getText().toLowerCase();
            String contactFilterText = contactFilter.getText().toLowerCase();
            String createdFilterText = createdFilter.getText().toLowerCase();
            LocalDate orderDateFilterValue = orderDateFilter.getValue();
            String typeFilterText = typeFilter.getValue() != null ? typeFilter.getValue().toLowerCase() : "";
            String commentsFilterText = commentsFilter.getText().toLowerCase();
            // Check if the order matches the filter criteria
            boolean customerNameMatch = customerNameFilterText.isEmpty() || order.getCustomerName().toLowerCase().contains(customerNameFilterText);
            boolean contactMatch = contactFilterText.isEmpty() || order.getContact().toLowerCase().contains(contactFilterText);
            boolean createdMatch = createdFilterText.isEmpty() || order.getEmployeeName().toLowerCase().contains(createdFilterText);
            boolean orderDateMatch = orderDateFilterValue == null || order.getOrderDate().equals(orderDateFilterValue);
            boolean typeMatch = typeFilterText.isEmpty() || order.getType().toLowerCase().contains(typeFilterText);
            boolean commentsMatch = commentsFilterText.isEmpty() || order.getComments().toLowerCase().contains(commentsFilterText);
            return customerNameMatch && contactMatch && createdMatch && orderDateMatch && typeMatch && commentsMatch;
        });
    }

    private void updateProductFilteredData() {
        filteredProducts.setPredicate(product -> {
            // Check if the order matches the filter criteria
            boolean productCodeMatch = product.getProductCode().toLowerCase().contains(productCodeFilter.getText().toLowerCase());
            boolean productLineMatch = product.getProductLine().toLowerCase().contains(productLineFilter.getText().toLowerCase());
            boolean productNameMatch = product.getProductName().toLowerCase().contains(productNameFilter.getText().toLowerCase());
            boolean productVendorMatch = product.getProductVendor().toLowerCase().contains(productVendorFilter.getText().toLowerCase());
            return productCodeMatch && productLineMatch && productNameMatch && productVendorMatch;
        });
    }

    private void updateCustomerFilteredData() {
        filteredCustomers.setPredicate(customer -> {
            boolean nameMatch = customer.getName().toLowerCase().contains(nameCustomerFilter.getText().toLowerCase());
            boolean contactMatch = customer.getContact().toLowerCase().contains(contactCustomerFilter.getText().toLowerCase());
            boolean addressMatch = customer.getAddress() == null || customer.getAddress().toLowerCase().contains(addressCustomerFilter.getText().toLowerCase());
            boolean rankMatch = customer.getRank().toLowerCase().contains(rankCustomerFilter.getText().toLowerCase());
            return nameMatch && contactMatch && addressMatch && rankMatch;
        });
    }

    private void clearCreateOrderTab() {
        orderDetailsTable.getItems().clear();
        productCodeInput.clear();
        quantityInput.clear();
        priceEachInput.clear();
        totalInput.clear();
        customerNameInput.clear();
        phoneNumberInput.clear();
        deliverInput.clear();
        orderDateInput.setValue(null);
        requiredDateInput.setValue(null);
        shippedDateInput.setValue(null);
        statusInput.setValue(null);
        commentsInput.clear();
    }
}