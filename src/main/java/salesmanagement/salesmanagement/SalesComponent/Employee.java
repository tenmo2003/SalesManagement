package salesmanagement.salesmanagement.SalesComponent;

import com.jfoenix.controls.JFXButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import salesmanagement.salesmanagement.SQLConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @since 1.3
 */
public class Employee {
    private int employeeNumber;
    private String lastName;
    private String firstName;
    private String email;
    private String officeCode;
    private int reportsTo;
    private String jobTitle;
    private SQLConnection sqlConnection;
    private HBox operation = new HBox();

    public Employee(ResultSet employeeRecord) {
        try {
            employeeNumber = employeeRecord.getInt("employeeNumber");
            lastName = employeeRecord.getString("lastName");
            firstName = employeeRecord.getString("firstName");
            email = employeeRecord.getString("email");
            officeCode = employeeRecord.getString("officeCode");
            reportsTo = employeeRecord.getInt("reportsTo");
            jobTitle = employeeRecord.getString("jobTitle");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Operation is a hbox include
        JFXButton removeButton = new JFXButton();
        ImageView removeImg = new ImageView("remove.png");
        removeImg.setFitWidth(20);
        removeImg.setFitHeight(20);
        removeButton.setGraphic(removeImg);
        operation.getChildren().add(removeButton);
    }
    public Employee(SQLConnection sqlConnection, int employeeNumber) {
        this.sqlConnection = sqlConnection;
        this.employeeNumber = employeeNumber;

        String query = "select * from employees where employeeNumber = " + employeeNumber;
        ResultSet resultSet = sqlConnection.getDataQuery(query);

        try {
            if (resultSet.next()) {
                lastName = resultSet.getString("lastName");
                firstName = resultSet.getString("firstName");
                email = resultSet.getString("email");
                officeCode = resultSet.getString("officeCode");
                reportsTo = resultSet.getInt("reportsTo");
                jobTitle = resultSet.getString("jobTitle");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getFullName() {
        return lastName + " " + firstName;
    }

    void createOrder(Order order) {

    }

    @Override
    public String toString() {
        String employeeRecord = String.format("%d %s %s %s %s %d %s", employeeNumber, lastName, firstName, email, officeCode, reportsTo, jobTitle);
        return employeeRecord;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public int getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(int reportsTo) {
        this.reportsTo = reportsTo;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public HBox getOperation() {
        return operation;
    }

    public void setOperation(HBox operation) {
        this.operation = operation;
    }
}
