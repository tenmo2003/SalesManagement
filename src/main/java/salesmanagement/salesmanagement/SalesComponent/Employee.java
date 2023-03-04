package salesmanagement.salesmanagement.SalesComponent;

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
}
