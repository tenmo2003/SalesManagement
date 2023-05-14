package salesmanagement.salesmanagement.SalesComponent;

import javafx.scene.control.Label;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Order implements SalesComponent {
    private int orderNumber;
    private int employeeNumber;
    private String employeeName;
    private LocalDate orderDate = LocalDate.of(1970, 1, 1);
    ;
    private LocalDate requiredDate = LocalDate.of(1970, 1, 1);
    ;
    private LocalDate shippedDate = LocalDate.of(1970, 1, 1);
    ;
    private String status;
    private Label statusLabel;
    private String comments;
    private int customerNumber;
    private String customerName;
    private String contact;
    private String type;
    private double value;
    private String payment_method;
    private String deliver_to;
    private static final List<String> orderStatusList = new ArrayList<>(Arrays.asList("Cancelled", "In Process", "Resolved", "Shipped"));

    public static List<String> getorderStatusList() {
        return orderStatusList;
    }
    private static final List<String> paymentMethodList = new ArrayList<>(Arrays.asList("Cash", "Credit Card", "Debit Card", "E-Wallet", "Bank Transfer"));

    public static List<String> getPaymentMethodList() {
        return paymentMethodList;
    }
    private static final List<String> orderTypeList = new ArrayList<>(Arrays.asList("onsite", "online"));

    public static List<String> getorderTypeList() {
        return orderTypeList;
    }

    public Order(int orderNumber, int employeeNumber, String employeeName, LocalDate orderDate, LocalDate requiredDate, LocalDate shippedDate, String status, String comments, String customerName, String contact, String type, double value, String payment_method, String deliver_to) {
        this.orderNumber = orderNumber;
        this.employeeNumber = employeeNumber;
        this.employeeName = employeeName;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.status = status;
        statusLabel = new Label(status);
        this.comments = comments;
        this.customerName = customerName;
        this.contact = contact;

        this.type = type;
        this.value = value;
        this.payment_method = payment_method;
        this.deliver_to = deliver_to;

        String statusBoxColor = "";
        if (Objects.equals(getStatus().toLowerCase(), "resolved") || Objects.equals(getStatus().toLowerCase(), "shipped")) statusBoxColor = "#19C37D";
        else if (Objects.equals(getStatus().toLowerCase(), "cancelled")) statusBoxColor = "#EF9589FF";
        else if (Objects.equals(getStatus().toLowerCase(), "in process")) statusBoxColor = "#FFD700";
        statusLabel.setStyle("-fx-background-color:" + statusBoxColor + ";-fx-pref-width: 100; -fx-pref-height: 20;-fx-text-fill: white;-fx-alignment: center;-fx-font-weight: bold;");
    }

    public Order(ResultSet resultSet) throws SQLException {
        this.orderNumber = resultSet.getInt("orderNumber");
        this.customerNumber = resultSet.getInt("customerNumber");
        this.employeeNumber = resultSet.getInt("orders.created_by");
        this.customerName = resultSet.getString("customerName");
        this.employeeName = resultSet.getString("lastName") + " " + resultSet.getString("firstName");
        this.contact = resultSet.getString("phone");
        Date orderDate = resultSet.getDate("orderDate");
        if (orderDate != null) this.orderDate = resultSet.getDate("orderDate").toLocalDate();

        Date requiredDate = resultSet.getDate("requiredDate");
        if (requiredDate != null) this.requiredDate = resultSet.getDate("requiredDate").toLocalDate();

        Date shippedDate = resultSet.getDate("shippedDate");
        if (shippedDate != null) this.shippedDate = shippedDate.toLocalDate();

        this.status = resultSet.getString("status");
        statusLabel = new Label(this.status);
        this.comments = resultSet.getString("comments");

        this.type = resultSet.getString("type");
        this.value = resultSet.getDouble("value");
        this.payment_method = resultSet.getString("payment_method");
        this.deliver_to = resultSet.getString("deliver_to");

        String statusBoxColor = "";
        if (Objects.equals(getStatus().toLowerCase(), "resolved") || Objects.equals(getStatus().toLowerCase(), "shipped")) statusBoxColor = "#19C37D";
        else if (Objects.equals(getStatus().toLowerCase(), "cancelled")) statusBoxColor = "#EF9589FF";
        else if (Objects.equals(getStatus().toLowerCase(), "in process")) statusBoxColor = "#FFD700";
        statusLabel.setStyle("-fx-background-color:" + statusBoxColor + ";-fx-pref-width: 100; -fx-pref-height: 20;-fx-text-fill: white;-fx-alignment: center;-fx-font-weight: bold;");
    }

    public Label getStatusLabel() {
        return statusLabel;
    }
    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public int getCustomerNumber() {
        return customerNumber;
    }

    public String getDestination() {
        return deliver_to == null ? "" : deliver_to;
    }

    public void setDestination(String deliver_to) {
        this.deliver_to = deliver_to;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getRequiredDate() {
        return requiredDate;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getEmployeeName() {
        return employeeName == null ? "" : employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setRequiredDate(LocalDate requiredDate) {
        this.requiredDate = requiredDate;
    }

    public LocalDate getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(LocalDate shippedDate) {
        this.shippedDate = shippedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments == null ? "" : comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCustomerName() {
        return customerName == null ? "" : customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContact() {
        return contact == null ? "" : contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }
}
