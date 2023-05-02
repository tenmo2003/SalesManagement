package salesmanagement.salesmanagement.SalesComponent;

import java.time.LocalDate;

public class Order implements SalesComponent{
    private int orderNumber;
    private int employeeNumber;
    private String employeeName;
    private LocalDate orderDate;
    private LocalDate requiredDate;
    private LocalDate shippedDate;
    private String status;
    private String comments;
    private String customerName;
    private String contact;
    private String type;
    private double value;
    private String payment_method;
    private String deliver_to;

    public Order(int orderNumber, int employeeNumber, String employeeName, LocalDate orderDate, LocalDate requiredDate, LocalDate shippedDate, String status, String comments, String customerName, String contact, String type, double value, String payment_method, String deliver_to) {
        this.orderNumber = orderNumber;
        this.employeeNumber = employeeNumber;
        this.employeeName = employeeName;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.status = status;
        this.comments = comments;
        this.customerName = customerName;
        this.contact = contact;

        this.type = type;
        this.value = value;
        this.payment_method = payment_method;
        this.deliver_to = deliver_to;
    }

    public String getDestination() {
        return deliver_to;
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
        return employeeName;
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
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContact() {
        return contact;
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
