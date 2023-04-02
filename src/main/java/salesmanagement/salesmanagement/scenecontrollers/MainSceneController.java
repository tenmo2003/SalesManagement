package salesmanagement.salesmanagement.scenecontrollers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import salesmanagement.salesmanagement.SQLConnection;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesComponent.Order;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainSceneController extends SceneController implements Initializable {
    @FXML
    Text usernameText;

    public void setUsernameText(String usernameText) {
        this.usernameText.setText(usernameText);
    }

    @FXML
    TabPane tabPane;
    @FXML
    private Tab employeesOperationTab;
    @FXML
    private Tab createOrderTab;
    @FXML
    private Tab homeTab;
    @FXML
    private Tab settingTab;
    @FXML
    private Tab productsOperationTab;

    @FXML
    void goToCreateOrderTab() {
        tabPane.getSelectionModel().select(createOrderTab);
        statusIcon.setImage(new Image("/create_order_icon.png"));
    }

    @FXML
    void goToEmployeesOperationTab() {
        tabPane.getSelectionModel().select(employeesOperationTab);
    }

    @FXML
    void goToHomeTab() {
        tabPane.getSelectionModel().select(homeTab);
        statusIcon.setImage(new Image("/home_icon.png"));
    }

    @FXML
    void goToProductsOperationTab() {
        tabPane.getSelectionModel().select(productsOperationTab);
    }

    @FXML
    void goToSettingTab() {
        tabPane.getSelectionModel().select(settingTab);
    }

    @FXML
    Text author;
    @FXML
    Text notificationTitle;
    @FXML
    Text publishedDate;
    @FXML
    Text content;
    @FXML
    HBox contentBox;

    private void uploadNotificationText() {
        runTask(() -> {
            String query = "SELECT * FROM notifications  " +
                    "WHERE notificationID = (SELECT MAX(notificationID) " +
                    "FROM notifications)";
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            try {
                if (resultSet.next()) {
                    content.wrappingWidthProperty().bind(contentBox.widthProperty().multiply(0.9));
                    content.setText(resultSet.getString("content"));
                    notificationTitle.setText(resultSet.getString("title"));
                    int authorID = Integer.parseInt(resultSet.getString("employeeNumber"));
                    Employee employee = new Employee(authorID);
                    author.setText("\t\tPosted by " + employee.getFullName() + ".");
                    publishedDate.setText("\t\tPublished " + resultSet.getString("publishedDate") + ".");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, progressIndicator, homeTab.getTabPane());

    }

    @FXML
    ImageView statusIcon;

    /**
     * Handle employee management tab.
     */
    @FXML
    TableView<Employee> employeeTable;
    @FXML
    private TableColumn<?, ?> email;
    @FXML
    private TableColumn<Employee, Integer> employeeNumber;
    @FXML
    private TableColumn<?, ?> firstName;
    @FXML
    private TableColumn<?, ?> jobTitle;
    @FXML
    private TableColumn<?, ?> lastName;
    @FXML
    private TableColumn<?, ?> officeCode;
    @FXML
    private TableColumn<?, ?> operation;
    @FXML
    private TableColumn<?, ?> reportsTo;
    @FXML
    AnchorPane employeeOperationPane;
    ArrayList<Employee> employees;

    @FXML
    void selectEmployeesOperationTab() {
        employeeTable.setSelectionModel(null);
        Task<Void> getEmployeeRecordsTask = new Task<>() {
            @Override
            protected Void call() {
                String query = "select * from employees";
                employees = new ArrayList<>();
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                try {
                    while (resultSet.next()) {
                        employees.add(new Employee(resultSet, MainSceneController.this));
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                employeeNumber.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
                lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                email.setCellValueFactory(new PropertyValueFactory<>("email"));
                officeCode.setCellValueFactory(new PropertyValueFactory<>("officeCode"));
                reportsTo.setCellValueFactory(new PropertyValueFactory<>("reportsTo"));
                jobTitle.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
                operation.setCellValueFactory(new PropertyValueFactory<>("operation"));

                ObservableList<Employee> employeeList = FXCollections.observableArrayList(employees);
                employeeTable.setItems(employeeList);
                return null;
            }
        };
        new Thread(getEmployeeRecordsTask).start();
        setProgressIndicatorStatus(getEmployeeRecordsTask, employeeOperationPane);
    }


    @FXML
    SplitPane firstSplitPane;
    @FXML
    SplitPane secondSplitPane;
    @FXML
    SplitPane thirdSplitPane;
    @FXML
    VBox employeeTabBox;
    @FXML
    HBox statusIconBox;

    public void initialSetup() {

        user = new Employee(sqlConnection, loggerID);

        usernameText.setText(user.getFullName());

        firstSplitPane.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
        ((AnchorPane) firstSplitPane.getItems().get(0)).setMinHeight(0.05 * Screen.getPrimary().getVisualBounds().getHeight());
        ((AnchorPane) firstSplitPane.getItems().get(1)).setMinHeight(0.95 * Screen.getPrimary().getVisualBounds().getHeight());
        secondSplitPane.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());
        ((AnchorPane) secondSplitPane.getItems().get(0)).setMinWidth(0.1667 * Screen.getPrimary().getVisualBounds().getWidth());
        ((AnchorPane) secondSplitPane.getItems().get(1)).setMinWidth(0.8333 * Screen.getPrimary().getVisualBounds().getWidth());
        thirdSplitPane.setMaxWidth(0.8333 * Screen.getPrimary().getVisualBounds().getWidth());
        ((VBox) thirdSplitPane.getItems().get(0)).setMinWidth(0.75 * thirdSplitPane.getMaxWidth());
        ((VBox) thirdSplitPane.getItems().get(1)).setMinWidth(0.25 * thirdSplitPane.getMaxWidth());

        statusIconBox.setPrefWidth(0.1667 * Screen.getPrimary().getVisualBounds().getWidth());

        double tableWidth = employeeTabBox.getWidth() * 0.95;
        System.out.println(employeeTabBox.getWidth());
        employeeTable.setMaxWidth(tableWidth);
        employeeNumber.setMinWidth(0.1 * tableWidth);
        firstName.setMinWidth(0.125 * tableWidth);
        lastName.setMinWidth(0.125 * tableWidth);
        email.setMinWidth(0.15 * tableWidth);
        officeCode.setMinWidth(0.125 * tableWidth);
        reportsTo.setMinWidth(0.125 * tableWidth);
        jobTitle.setMinWidth(0.125 * tableWidth);
        operation.setMinWidth(0.125 * tableWidth);
    }

    private Employee user;

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

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
                reloadFlagListener.start();
                stop();
            }
        }
    };
    private AnimationTimer reloadFlagListener = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if (haveJustOpened) {
                stage.setScene(MainSceneController.this.scene);
                stage.hide();
                initialSetup();
                stage.setX(0);
                stage.setY(0);
                stage.show();
                uploadNotificationText();
                haveJustOpened = false;
            }
            if (haveChangeInEmployeesTab) {
                selectEmployeesOperationTab();
                haveChangeInEmployeesTab = false;
            }
            if (haveChangeInHomeTab) {

            }
        }
    };

    @FXML
    JFXComboBox statusInput;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statusInput.getItems().add("Cancelled");
        statusInput.getItems().add("Disputed");
        statusInput.getItems().add("In Process");
        statusInput.getItems().add("On Hold");
        statusInput.getItems().add("Resolved");
        statusInput.getItems().add("Shipped");

//        tableView.setItems(getItems());
        productCode.setCellValueFactory(new PropertyValueFactory<Order, String>("productCode"));
        quantity.setCellValueFactory(new PropertyValueFactory<Order, Integer>("quantityOrdered"));
        priceEach.setCellValueFactory(new PropertyValueFactory<Order, Double>("priceEach"));
        total.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Order, Double> param) {
                Order order = param.getValue();
                int quantity = order.getQuantityOrdered();
                double price = order.getPriceEach();
                double total = quantity * price;
                return new SimpleDoubleProperty(total).asObject();
            }
        });

        tableView.setItems(getList());

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableView.setEditable(true);
        productCode.setCellFactory(TextFieldTableCell.forTableColumn());
        quantity.setCellFactory(TextFieldTableCell.forTableColumn((new IntegerStringConverter())));
        priceEach.setCellFactory(TextFieldTableCell.forTableColumn((new DoubleStringConverter())));
    }

    public ObservableList<Order> getList() {
        ObservableList<Order> items = FXCollections.observableArrayList();
        return items;
    }

    public void addItem() {
        String productCode = productCodeInput.getText();
        int quantity = Integer.parseInt(quantityInput.getText());
        double priceEach = Double.parseDouble(priceEachInput.getText());

        // Check if an order with the same productCode already exists
        for (Order order : tableView.getItems()) {
            if (order.getProductCode().equals(productCode)) {
                // Update the existing order
                order.setQuantityOrdered(quantity);
                order.setPriceEach(priceEach);
                tableView.refresh();
                return;
            }
        }

        // If no existing order was found, create a new one and add it to the tableView
        Order order = new Order(productCode, quantity, priceEach);
        tableView.getItems().add(order);
    }

    public void removeItems() {
        ObservableList<Order> selectedRows, allItems;
        allItems = tableView.getItems();

        selectedRows = tableView.getSelectionModel().getSelectedItems();

        allItems.removeAll(selectedRows);
    }

    public void changeProductCode(TableColumn.CellEditEvent edittedCell) {
        Order selected = tableView.getSelectionModel().getSelectedItem();
        selected.setProductCode(edittedCell.getNewValue().toString());
        tableView.refresh();
    }
    public void changeQuantity(TableColumn.CellEditEvent edittedCell) {
        Order selected = tableView.getSelectionModel().getSelectedItem();
        selected.setQuantityOrdered((int) edittedCell.getNewValue());
        tableView.refresh();
    }
    public void changePriceEach(TableColumn.CellEditEvent edittedCell) {
        Order selected = tableView.getSelectionModel().getSelectedItem();
        selected.setPriceEach((double) edittedCell.getNewValue());
        tableView.refresh();
    }

    @FXML
    TableView<Order> tableView;
    @FXML
    TableColumn<Order, String> productCode;
    @FXML
    TableColumn<Order, Integer> quantity;
    @FXML
    TableColumn<Order, Double> priceEach;
    @FXML
    TableColumn<Order, Double> total;
    @FXML
    JFXTextField customerNumberInput;
    @FXML
    JFXTextField productCodeInput;
    @FXML
    JFXTextField quantityInput;
    @FXML
    JFXTextField priceEachInput;
    @FXML
    JFXButton addButton;
    @FXML
    JFXButton removeButton;
    @FXML
    DatePicker orderDateInput;
    @FXML
    DatePicker requiredDateInput;
    @FXML
    DatePicker shippedDateInput;
    @FXML
    JFXTextField commentsInput;
    @FXML
    JFXButton createOrderButton;
    public void createOrder() throws SQLException {
        String orderDate;
        if (orderDateInput.getValue() == null) {
            orderDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        } else {
            orderDate = orderDateInput.getValue().format(DateTimeFormatter.ISO_DATE);
        }
        String shippedDate;
        if (shippedDateInput.getValue() != null) {
            shippedDate = shippedDateInput.getValue().format(DateTimeFormatter.ISO_DATE);
            shippedDate = "'" + shippedDate;
            shippedDate += "'";
        } else {
            shippedDate = "null";
        }
        String comment;
        if (commentsInput.getText().equals("")) {
            comment = "null";
        }
        String order = "insert into orders(orderDate, requiredDate, shippedDate, status, comments, customerNumber) values ('"
                + orderDate + "','"
                + requiredDateInput.getValue().format(DateTimeFormatter.ISO_DATE) + "',"
                + shippedDate + ",'"
                + statusInput.getValue() + "','"
                + commentsInput.getText() + "',"
                + Integer.parseInt(customerNumberInput.getText()) + ");";
        sqlConnection.updateQuery(order);
        ResultSet result = sqlConnection.getDataQuery("SELECT LAST_INSERT_ID() FROM orders;");

        int orderNumber = 0;
        if (result.next()) {
            orderNumber = result.getInt(1);
        }

        StringBuilder orderdetails = new StringBuilder("insert into orderdetails values");
        ObservableList<Order> items = tableView.getItems();

        for (Order item : items) {
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
    }
}
