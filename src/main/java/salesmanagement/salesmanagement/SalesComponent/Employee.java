package salesmanagement.salesmanagement.SalesComponent;

import javafx.scene.control.*;
import salesmanagement.salesmanagement.SQLConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @since 1.3
 */
public class Employee {
    private boolean isNewUser = true;

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
    }

    private int employeeNumber = -1;
    private String lastName = "";
    private String firstName = "";
    private Label statusLabel = new Label("ACTIVE");
    private String phone = "";
    private String name = "";
    private String email = "";
    private String officeCode = "";
    private int reportsTo = -1;
    private String jobTitle = "Employee";
    private String username = "";
    private String password = "";
    private static SQLConnection sqlConnection;
    private String gender = "";
    private LocalDate birthDate = null;
    private boolean mailVerified = false;
    private String phoneCode = "+84 (VN)";
    private LocalDate lastWorkingDate = null;
    private LocalDate joiningDate = null;

    public Employee() {

    }

    /**
     * This constructor is used to create an employee object
     * for content storage purposes only,
     * without affecting database connection variables.
     */
    public Employee(ResultSet employeeRecord) {
        isNewUser = false;
        try {
            employeeNumber = employeeRecord.getInt("employeeNumber");
            lastName = employeeRecord.getString("lastName");
            firstName = employeeRecord.getString("firstName");
            email = employeeRecord.getString("email");
            officeCode = employeeRecord.getString("officeCode");
            reportsTo = employeeRecord.getInt("reportsTo");
            jobTitle = employeeRecord.getString("jobTitle");
            name = lastName + " " + firstName;
            statusLabel = new Label(employeeRecord.getString("status"));
            phone = employeeRecord.getString("phone");
            username = employeeRecord.getString("username");
            password = employeeRecord.getString("password");
            gender = employeeRecord.getString("gender");
            mailVerified = employeeRecord.getBoolean("mailVerified");
            phoneCode = employeeRecord.getString("phoneCode");
            joiningDate = employeeRecord.getDate("joiningDate").toLocalDate();
            if (employeeRecord.getDate("lastWorkingDate") != null)
                lastWorkingDate = employeeRecord.getDate("lastWorkingDate").toLocalDate();
            birthDate = employeeRecord.getDate("birthDate").toLocalDate();

            String statusBoxColor = "#E5E575FF";
            if (Objects.equals(getStatus().toLowerCase(), "active")) statusBoxColor = "#19C37D";
            else if (Objects.equals(getStatus().toLowerCase(), "inactive")) statusBoxColor = "#EF9589FF";
            statusLabel.setStyle("-fx-background-color:" + statusBoxColor + ";-fx-pref-width: 100; -fx-pref-height: 20;-fx-text-fill: white;-fx-alignment: center;-fx-font-weight: bold;");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    /**
     * This Employee's initializer allows initialization when the user logs into the application.
     * This also means that when entering the main screen of the application, this class is only
     * used once. It is reused when logging out to the login page. The connection to the data
     * storage server is saved as private static within this class to use the connection variable
     * for other employee objects. This means that employees created by other initialization methods
     * use this connection and do not create new connections.
     */
    public Employee(SQLConnection sqlConnection, int employeeNumber) {
        if (sqlConnection != null) Employee.sqlConnection = sqlConnection;
        this.employeeNumber = employeeNumber;
        String query = "select * from employees where employeeNumber = " + employeeNumber;
        ResultSet resultSet = Employee.sqlConnection.getDataQuery(query);
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

    public Employee(int employeeNumber) {
        this(null, employeeNumber);
    }

    public String getFullName() {
        if (isNewUser) return "";
        return lastName + " " + firstName;
    }

    /**
     * This code defines a toString() method to return a string
     * representation of the current Employee object. Specifically,
     * the method uses the String class to create a string that
     * combines the properties of the Employee object, including
     * the employee number, full name, email address, and office code.
     * Finally, the method returns this string as the result.
     * Defining a toString() method is very useful for displaying
     * object information in a readable and user-friendly way.
     */
    @Override
    public String toString() {
        return String.format("Employee: %d \nFull Name: %s \nEmail: %s \nOffice Code: %s", employeeNumber, getFullName(), email, officeCode);
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getLastName() {
        return (lastName == null) ? "" : lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return (firstName == null) ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return (email == null) ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOfficeCode() {
        return (officeCode == null) ? "" : officeCode;
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
        return (jobTitle == null) ? "" : jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }


    public String getStatus() {
        if (statusLabel == null) return "ACTIVE";
        return statusLabel.getText();
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getPhone() {
        return (phone == null) ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return (name == null) ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return (username == null) ? "" : username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return (password == null) ? "" : password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return (gender == null) ? "" : gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isMailVerified() {
        return mailVerified;
    }

    public void setMailVerified(boolean mailVerified) {
        this.mailVerified = mailVerified;
    }

    public String getPhoneCode() {
        return (phoneCode == null) ? "" : phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public LocalDate getLastWorkingDate() {
        return lastWorkingDate;
    }

    public void setLastWorkingDate(LocalDate lastWorkingDate) {
        this.lastWorkingDate = lastWorkingDate;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }
}
