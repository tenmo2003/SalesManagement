package salesmanagement.salesmanagement.ViewController.OrdersTab;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.textfield.TextFields;
import salesmanagement.salesmanagement.SalesComponent.Customer;
import salesmanagement.salesmanagement.SalesComponent.Order;
import salesmanagement.salesmanagement.SalesComponent.OrderItem;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.Utils.InputErrorCode;
import salesmanagement.salesmanagement.Utils.NotificationCode;
import salesmanagement.salesmanagement.Utils.NotificationSystem;
import salesmanagement.salesmanagement.ViewController.CustomersTab.CustomerSearchViewController;
import salesmanagement.salesmanagement.ViewController.EmployeesTab.EmployeeSearchViewController;
import salesmanagement.salesmanagement.ViewController.ViewController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;
import static salesmanagement.salesmanagement.Utils.InputErrorCode.getInputErrorLabel;
import static salesmanagement.salesmanagement.Utils.Utils.shake;

public class OrderInfoViewController extends ViewController implements OrdersTabController {

    @FXML
    private JFXButton add;

    @FXML
    private TextField commentsTextField;

    @FXML
    private TextField customerNumberTextField;

    @FXML
    private TextField deliverTo;

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private DatePicker orderedDatePicker;

    @FXML
    private FilteredTableView<OrderItem> orderTable;

    @FXML
    private ComboBox<String> paymentMethod;

    @FXML
    private TableColumn<?, ?> priceColumn;

    @FXML
    private TextField priceEachTextField;

    @FXML
    private JFXButton print;

    @FXML
    private TableColumn<?, ?> productCodeColumn;

    @FXML
    private TextField productCodeTextField;

    @FXML
    private TableColumn<?, ?> quantityColumn;

    @FXML
    private TextField quantityTextField;

    @FXML
    private DatePicker requiredDatePicker;

    @FXML
    private DatePicker shippedDatePicker;

    @FXML
    private ComboBox<String> status;
    @FXML
    private ComboBox<String> orderType;

    @FXML
    private TextField totalAmountTextField;

    @FXML
    private TableColumn<OrderItem, String> totalColumn;
    private final BooleanProperty tableUpdated = new SimpleBooleanProperty(true);

    @FXML
    private TextField totalTextField;
    private CustomerSearchViewController customerSearchViewController;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXButton updateButton;
    @FXML
    private JFXButton removeButton;
    @FXML
    private JFXButton clearButton;

    private List<Node> disabledNodesList;
    private Customer searchedCustomer;

    public void setSearchedCustomer(Customer searchedCustomer) {
        this.searchedCustomer = searchedCustomer;
        customerNumberTextField.setText(String.valueOf(searchedCustomer.getCustomerNumber()));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/customers-tab/customer-search-view.fxml"));
            loader.load();
            customerSearchViewController = loader.getController();
            root.getChildren().add(customerSearchViewController.getRoot());
            customerSearchViewController.setParentController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        status.setItems(FXCollections.observableList(Order.getorderStatusList()));
        orderType.setItems(FXCollections.observableList(Order.getorderTypeList()));
        paymentMethod.setItems(FXCollections.observableList(Order.getPaymentMethodList()));

        productCodeColumn.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityOrdered"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("priceEach"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        addCheckUserInput();

        orderType.setValue("onsite");
        status.setValue("In Process");
        paymentMethod.setValue("Cash");

        orderTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                addButton.setDisable(true);
                updateButton.setDisable(false);
                removeButton.setDisable(false);
                selectedOrderItem = orderTable.getSelectionModel().getSelectedItem();
                productCodeTextField.setText(selectedOrderItem.getProductCode());
                quantityTextField.setText(Integer.toString(selectedOrderItem.getQuantityOrdered()));
                priceEachTextField.setText(Double.toString(selectedOrderItem.getPriceEach()));
                totalTextField.setText(Double.toString(selectedOrderItem.getAmount()));
            }
        });
        tableUpdated.addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                orderTable.setItems(FXCollections.observableList(orderItems));
                tableUpdated.set(true);
                int orderTotalAmount = 0;
                for (OrderItem orderItem : orderItems) {
                    orderTotalAmount += orderItem.getAmount();
                }
                totalAmountTextField.setText(String.valueOf(orderTotalAmount));
            }
        });

        addOrderButton.setOnAction(event -> {
            addNewOrder(false);
        });
    }

    @FXML
    void searchCustomer() {
        customerSearchViewController.show();
    }

    List<OrderItem> orderItems = new ArrayList<>();
    OrderItem selectedOrderItem;

    @FXML
    void addOrderItem() {
        if (validInput()) {
            int itemIndex = -1;
            for (int i = 0; i < orderItems.size(); i++) {
                if (Objects.equals(orderItems.get(i).getProductCode(), productCodeTextField.getText())) {
                    itemIndex = i;
                    break;
                }
            }
            if (itemIndex == -1) {
                orderItems.add(new OrderItem(productCodeTextField.getText(), Integer.parseInt(quantityTextField.getText()), Double.parseDouble(priceEachTextField.getText().replaceAll(",", "."))));
            } else {
                int oldQuantity = orderItems.get(itemIndex).getQuantityOrdered();
                orderItems.get(itemIndex).setQuantityOrdered(oldQuantity + Integer.parseInt(quantityTextField.getText()));
            }
            tableUpdated.set(false);

            productCodeTextField.clear();
            quantityTextField.clear();
            priceEachTextField.clear();
            totalTextField.clear();
        }
    }

    @FXML
    void updateOrderItem() {
        if (validInput()) {
            int itemIndex = -1;
            for (int i = 0; i < orderItems.size(); i++) {
                if (Objects.equals(orderItems.get(i).getProductCode(), productCodeTextField.getText())) {
                    itemIndex = i;
                    break;
                }
            }
            if (itemIndex != -1) {
                orderItems.get(itemIndex).setQuantityOrdered(Integer.parseInt(quantityTextField.getText()));
            }
        }
        tableUpdated.set(false);

        addButton.setDisable(false);
        removeButton.setDisable(true);
        updateButton.setDisable(true);
    }

    @FXML
    void remove() {
        orderItems.remove(selectedOrderItem);
        tableUpdated.set(false);

        addButton.setDisable(false);
        removeButton.setDisable(true);
        updateButton.setDisable(true);
    }

    @FXML
    void clearAll() {
        productCodeTextField.setText("");
        quantityTextField.setText("");
        totalTextField.setText("");
        priceEachTextField.setText("");


        addButton.setDisable(false);
        updateButton.setDisable(true);
        removeButton.setDisable(true);
    }

    @FXML
    void addNewOrder(boolean print) {
        runTask(() -> {
            String orderDate;
            if (orderedDatePicker.getValue() == null) {
                orderDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
            } else {
                orderDate = orderedDatePicker.getValue().format(DateTimeFormatter.ISO_DATE);
            }
            String shippedDate;
            String requiredDate;
            if (orderType.getValue().equals("online")) {
                if (shippedDatePicker.getValue() != null) {
                    shippedDate = shippedDatePicker.getValue().format(DateTimeFormatter.ISO_DATE);
                    shippedDate = "'" + shippedDate;
                    shippedDate += "'";
                } else {
                    shippedDate = "null";
                }
                requiredDate = "'" + requiredDatePicker.getValue().format(DateTimeFormatter.ISO_DATE) + "'";
            } else {
                shippedDate = "null";
                requiredDate = "null";
            }

            int customerNumber = Integer.parseInt(customerNumberTextField.getText());

            double value = 0;
            for (OrderItem item : orderTable.getItems()) {
                value += item.getAmount();
            }

            String customerRank = String.format("SELECT `rank` FROM customers WHERE customerNumber = %d", Integer.parseInt(customerNumberTextField.getText()));
            ResultSet result = sqlConnection.getDataQuery(customerRank);
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
                    + status.getValue() + "','"
                    + commentsTextField.getText() + "',"
                    + customerNumberTextField.getText() + ", '"
                    + orderType.getValue() + "', "
                    + value + ", '"
                    + paymentMethod.getValue() + "'," +
                    sqlConnection.getUserID() + ");";
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
            ObservableList<OrderItem> items = orderTable.getItems();

            for (OrderItem item : items) {
                orderdetails.append("(").append(orderNumber)
                        .append(", '")
                        .append(item.getProductCode())
                        .append("',")
                        .append(item.getQuantityOrdered())
                        .append(",")
                        .append(item.getPriceEach())
                        .append("),");
                if (orderType.getValue().equals("onsite") || status.getValue().equals("Shipped")) {
                    String query = String.format("UPDATE products SET quantityInStock = quantityInStock - %d WHERE productCode = '%s';\n", item.getQuantityOrdered(), item.getProductCode());
                    sqlConnection.updateQuery(query);
                }
            }
            orderdetails.deleteCharAt(orderdetails.length() - 1);
            orderdetails.append(';');
            sqlConnection.updateQuery(orderdetails.toString());

//            int finalOrderNumber = orderNumber;
//            printInvoiceButton.setOnAction(event -> {
//                printInvoice(finalOrderNumber);
//            });

            if (print)
                printOrder(orderNumber);

            int countOrd = -1;
            double totalValue = -1;
            if (Integer.parseInt(customerNumberTextField.getText()) != 6) {
                String customerRankCheck = "SELECT COUNT(*) AS num, SUM(value) AS totalValue" +
                        "  FROM orders" +
                        "  WHERE customerNumber = " + customerNumberTextField.getText() +
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
        }, null, loadingIndicator, null);

        NotificationSystem.throwNotification(NotificationCode.SUCCEED_CREATE_ORDER, stage);
    }

    void saveOrder(int orderNumber) {
        if (orderType.getValue().equals("onsite") || status.getValue().equals("Shipped")) {
            String query = String.format("SELECT * FROM orderdetails WHERE orderNumber = %d", orderNumber);
            ResultSet rs = sqlConnection.getDataQuery(query);
            try {
                while (rs.next()) {
                    String tmp = String.format("UPDATE products SET quantityInStock = quantityInStock + %d WHERE productCode = '%s'", rs.getInt(3), rs.getString(2));
                    sqlConnection.updateQuery(tmp);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        String query = String.format("DELETE FROM orderdetails WHERE orderNumber = %d", orderNumber);
        sqlConnection.updateQuery(query);
        for (OrderItem orderItem : orderTable.getItems()) {
            String productCode = orderItem.getProductCode();
            int quantity = orderItem.getQuantityOrdered();
            double priceEach = orderItem.getPriceEach();
            query = String.format("INSERT INTO orderdetails (orderNumber, productCode, quantityOrdered, priceEach) VALUES (%d, '%s', %d, %f)", orderNumber, productCode, quantity, priceEach);
            sqlConnection.updateQuery(query);
            if (orderType.getValue().equals("onsite") || status.getValue().equals("Shipped")) {
                String tmp = String.format("UPDATE products SET quantityInStock = quantityInStock + %d WHERE productCode = '%s'", quantity, productCode);
                sqlConnection.updateQuery(tmp);
            }
        }
        // Get the values from the input fields
        LocalDate orderDate = orderedDatePicker.getValue();
        String requiredDate = requiredDatePicker.getValue() != null ? "'" + requiredDatePicker.getValue().toString() + "'" : null;
        String shippedDate = shippedDatePicker.getValue() != null ? "'" + shippedDatePicker.getValue().toString() + "'" : "null";
        String st = (String) status.getValue();
        String comments = commentsTextField.getText();
        query = String.format("UPDATE orders SET orderDate = '%s', requiredDate = %s, shippedDate = %s, status = '%s', comments = '%s', payment_method = '%s', deliver_to = '%s' WHERE orderNumber = %d", orderDate.toString(), requiredDate, shippedDate, st, comments, paymentMethod.getValue(), deliverTo.getText(), orderNumber);
        sqlConnection.updateQuery(query);
    }

    @FXML
    void printOrder(int orderNumber) {
        String sourceFile = "src/main/resources/salesmanagement/salesmanagement/invoice.jrxml";
        try {
            JasperReport jr = JasperCompileManager.compileReport(sourceFile);
            HashMap<String, Object> para = new HashMap<>();
            para.put("invoiceNo", "INV" + String.format("%04d", orderNumber));
            para.put("customerName", customerNumberTextField.getText());
            para.put("contact", "");
            para.put("totalAmount", "totalAmount");
            para.put("otherAmount", "discount");
            para.put("paybleAmount", "toBePaid");
            para.put("paidAmount", "paid");
            para.put("dueAmount", "due");
            para.put("comments", commentsTextField.getText());
            para.put("point1", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
            para.put("point2", "If you have any questions concerning this invoice, use the following contact information:");
            para.put("point3", "+243 999999999, purchase@example.com");
            LocalDate orderDate = orderedDatePicker.getValue() != null ? orderedDatePicker.getValue() : LocalDate.now();
            para.put("orderYear", orderDate.getYear() - 1900);
            para.put("orderMonth", orderDate.getMonthValue() - 1);
            para.put("orderDay", orderDate.getDayOfMonth());
            para.put("paymentMethod", paymentMethod.getValue());
            String query = String.format("SELECT value, type, CONCAT(lastName, ' ', firstName) AS name, `rank`, customerName, customers.phone FROM orders INNER JOIN employees ON orders.created_by = employees.employeeNumber INNER JOIN customers ON orders.customerNumber = customers.customerNumber WHERE orderNumber = %d", orderNumber);
            ResultSet rs = sqlConnection.getDataQuery(query);
            if (rs.next()) {
                String rank = rs.getString(4);
                double left = 1;
                switch (rank) {
                    case "Emerald" -> {
                        para.put("discount", "25%");
                        left = 0.75;
                    }
                    case "Diamond" -> {
                        para.put("discount", "20%");
                        left = 0.8;
                    }
                    case "Platinum" -> {
                        para.put("discount", "15%");
                        left = 0.85;
                    }
                    case "Gold" -> {
                        para.put("discount", "10%");
                        left = 0.9;
                    }
                    case "Silver" -> {
                        para.put("discount", "5%");
                        left = 0.95;
                    }
                    case "Membership" -> {
                        para.put("discount", "0%");
                        left = 1;
                    }
                }
                para.put("totalAmount", Double.parseDouble(String.format("%.2f", rs.getDouble(1) / left)));
                para.put("leftAmount", rs.getDouble(1));
                para.put("type", rs.getString(2).substring(0, 1).toUpperCase() + rs.getString(2).substring(1));
                para.put("employee", rs.getString(3));
                para.put("customerName", rs.getString(5));
                para.put("contact", rs.getString(6));
            }

            ArrayList<OrderItem> plist = new ArrayList<>();

            for (OrderItem item : orderTable.getItems()) {
                plist.add(new OrderItem(item.getProductCode(), item.getQuantityOrdered(), item.getPriceEach()));
            }

            JRBeanCollectionDataSource jcs = new JRBeanCollectionDataSource(plist);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, jcs);
            JasperViewer.viewReport(jp, false);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @FXML
    JFXButton saveOrderButton;
    @FXML
    JFXButton addOrderButton;

    @Override
    public void show() {
        if (disabledNodesList == null) {
            disabledNodesList = new ArrayList<>(Arrays.asList(customerNumberTextField, productCodeTextField,
                    quantityTextField, addButton, updateButton, removeButton, clearButton, orderType, orderedDatePicker,
                    deliverTo, requiredDatePicker, paymentMethod));
        }
        super.show();
        addOrderButton.setVisible(true);
        print.setOnAction(event -> {
            addNewOrder(true);
        });

        orderItems.clear();
        runTask(() -> {
            List<String> customerCodeList = new ArrayList<>();
            String query = "select customerNumber from customers";
            ResultSet customerInfo = sqlConnection.getDataQuery(query);
            try {
                while (customerInfo.next()) {
                    customerCodeList.add(customerInfo.getString("customerNumber"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            TextFields.bindAutoCompletion(customerNumberTextField, customerCodeList);

            List<String> productCodeList = new ArrayList<>();
            query = "select productCode from products";
            ResultSet productInfo = sqlConnection.getDataQuery(query);
            try {
                while (productInfo.next()) {
                    productCodeList.add(productInfo.getString("productCode"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            TextFields.bindAutoCompletion(productCodeTextField, productCodeList);
        }, null, null, null);
    }

    public void show(Order order) {
        show();
        for (Node node : disabledNodesList) {
            if (node instanceof TextField) {
                ((TextField) node).setEditable(false);
                break;
            }
            node.setDisable(true);
        }
        addOrderButton.setVisible(false);
        saveOrderButton.setVisible(true);
        saveOrderButton.setOnAction(event -> {
            saveOrder(order.getOrderNumber());
        });
        runTask(() -> {
            String query = "select * from orderdetails where orderNumber = " + order.getOrderNumber();
            ResultSet resultSet = sqlConnection.getDataQuery(query);
            try {
                while (resultSet.next()) {
                    orderItems.add(new OrderItem(resultSet.getString("productCode"), resultSet.getInt("quantityOrdered"), resultSet.getDouble("priceEach")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, () -> tableUpdated.set(false), loadingIndicator, null);

        customerNumberTextField.setText(String.valueOf(order.getCustomerNumber()));
        orderType.setValue(order.getType());
        orderedDatePicker.setValue(order.getOrderDate());
        requiredDatePicker.setValue(order.getRequiredDate());
        shippedDatePicker.setValue(order.getShippedDate());
        deliverTo.setText(order.getDestination());
        paymentMethod.setValue(order.getPayment_method());
        status.setValue(order.getStatus());
        commentsTextField.setText(order.getComments());

        print.setOnAction(event -> printOrder(order.getOrderNumber()));
    }

    @Override
    public void close() {
        super.close();
        for (Node node : disabledNodesList) {
            if (node instanceof TextField) {
                ((TextField) node).setEditable(true);
                break;
            }
            node.setDisable(false);
        }
        addOrderButton.setVisible(false);
        saveOrderButton.setVisible(false);
        orderedDatePicker.setValue(null);
        requiredDatePicker.setValue(null);
        shippedDatePicker.setValue(null);
        deliverTo.setText(null);
        paymentMethod.setValue(null);
        status.setValue(null);
        commentsTextField.setText(null);
        customerNumberTextField.setText(null);
        orderItems.clear();
        tableUpdated.set(false);
        clearAll();
    }

    private void addCheckUserInput() {
        customerNumberTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) customerNumberTextField.getParent().getParent();
                int customerNumber = 0;
                try {
                    customerNumber = Integer.parseInt(customerNumberTextField.getText());
                } catch (Exception e) {
                    customerNumber = -1;
                }
                String query = "SELECT customerNumber FROM customers WHERE customerNumber = " + customerNumber;
                ResultSet resultSet = sqlConnection.getDataQuery(query);
                try {
                    if (!resultSet.next()) {
                        customerNumberTextField.setStyle("-fx-border-color: #f35050");
                        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                            container.getChildren().remove(container.getChildren().size() - 1);
                        }
                        container.getChildren().add(getInputErrorLabel(InputErrorCode.INVALID_CUSTOMER_NUMBER));
                        shake(customerNumberTextField);
                    } else {
                        customerNumberTextField.setStyle("-fx-border-color: transparent");
                        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                            container.getChildren().remove(container.getChildren().size() - 1);
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        productCodeTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                VBox container = (VBox) productCodeTextField.getParent();
                String query = "select productCode from products where productCode = '" + productCodeTextField.getText() + "'";
                boolean productExist = false;
                try {
                    ResultSet resultSet = sqlConnection.getDataQuery(query);
                    if (resultSet.next()) productExist = true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (productExist) {
                    productCodeTextField.setStyle("-fx-border-color: transparent");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                } else {
                    productCodeTextField.setStyle("-fx-border-color: #f35050");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                    container.getChildren().add(getInputErrorLabel(InputErrorCode.PRODUCT_NOT_EXIST));

                    container = (VBox) quantityTextField.getParent();
                    quantityTextField.setStyle("-fx-border-color: transparent");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                    shake(productCodeTextField);
                }
            }
        });
        quantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("")) {
                VBox container = (VBox) quantityTextField.getParent();
                if (!quantityTextField.getText().matches("^\\d+$")) {
                    quantityTextField.setStyle("-fx-border-color: #f35050");
                    if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                        container.getChildren().remove(container.getChildren().size() - 1);
                    }
                    container.getChildren().add(getInputErrorLabel(InputErrorCode.INVALID_INPUT));
                    shake(quantityTextField);
                } else {
                    String query = "select quantityInStock, sellPrice from products where productCode = '" + productCodeTextField.getText() + "'";
                    int quantityInStock = -1;
                    double priceEach = -1;
                    ResultSet resultSet = sqlConnection.getDataQuery(query);
                    try {
                        if (resultSet.next()) {
                            quantityInStock = resultSet.getInt("quantityInStock");
                            priceEach = resultSet.getDouble("sellPrice");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (quantityInStock >= Integer.parseInt(quantityTextField.getText())) {
                        quantityTextField.setStyle("-fx-border-color: transparent");
                        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                            container.getChildren().remove(container.getChildren().size() - 1);
                        }
                        priceEachTextField.setText(Double.toString(priceEach));
                        totalTextField.setText(Double.toString(priceEach * Integer.parseInt(quantityTextField.getText())));
                    } else {
                        quantityTextField.setStyle("-fx-border-color: #f35050");
                        if ((container.getChildren().get(container.getChildren().size() - 1) instanceof Label)) {
                            container.getChildren().remove(container.getChildren().size() - 1);
                        }
                        container.getChildren().add(getInputErrorLabel(InputErrorCode.NOT_ENOUGH_QUANTITY));
                        shake(quantityTextField);
                    }
                }
            }
        });
    }

    boolean validInput() {
        VBox container = (VBox) productCodeTextField.getParent();
        if (container.getChildren().get(container.getChildren().size() - 1) instanceof Label) return false;
        container = (VBox) quantityTextField.getParent();
        if (container.getChildren().get(container.getChildren().size() - 1) instanceof Label) return false;
        return !Objects.equals(totalTextField.getText(), "");
    }
}
