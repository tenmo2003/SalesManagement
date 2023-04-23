package salesmanagement.salesmanagement.SalesComponent;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import salesmanagement.salesmanagement.ImageController;
import salesmanagement.salesmanagement.SQLConnection;
import salesmanagement.salesmanagement.scenecontrollers.MainSceneController;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * @since 1.3
 */
public class Employee {
    private int employeeNumber;
    private String lastName;
    private String firstName;
    private StackPane statusBox;
    private String phone;
    private String name;
    private String email;
    private String officeCode;
    private int reportsTo;
    private String jobTitle;
    private String username;
    private String password;
    private ImageView avatar = new ImageView();
    private static SQLConnection sqlConnection;
    private String gender;
    private LocalDate birthDate;
    private boolean mailVerified;
    private String phoneCode;
    private LocalDate lastWorkingDate;
    private LocalDate joiningDate;

    /**
     * This constructor is used to create an employee object
     * for content storage purposes only,
     * without affecting database connection variables.
     */
    public Employee(ResultSet employeeRecord, MainSceneController mainSceneController) {
        try {
            employeeNumber = employeeRecord.getInt("employeeNumber");
            lastName = employeeRecord.getString("lastName");
            firstName = employeeRecord.getString("firstName");
            email = employeeRecord.getString("email");
            officeCode = employeeRecord.getString("officeCode");
            reportsTo = employeeRecord.getInt("reportsTo");
            jobTitle = employeeRecord.getString("jobTitle");
            name = lastName + " " + firstName;
            statusBox = new StackPane(new Text(employeeRecord.getString("status")));
            phone = employeeRecord.getString("phone");
            username = employeeRecord.getString("username");
            password = employeeRecord.getString("password");
            gender = employeeRecord.getString("gender");
            mailVerified = employeeRecord.getBoolean("mailVerified");
            phoneCode = employeeRecord.getString("phoneCode");
            joiningDate = employeeRecord.getDate("joiningDate").toLocalDate();
            lastWorkingDate = employeeRecord.getDate("lastWorkingDate").toLocalDate();
                birthDate = employeeRecord.getDate("birthDate").toLocalDate();

            statusBox.setStyle("-fx-background-color: #43fc5c;-fx-font-weight: bold;-fx-pref-width: 100; -fx-pref-height: 20;");
            statusBox.getChildren().get(0).setStyle("-fx-text-fill: white;-fx-text-alignment: center");

            try {
                ResultSet rs = sqlConnection.getDataQuery("SELECT avatar FROM employees WHERE employeeNumber = " + employeeNumber);
                if (rs.next()) {
                    InputStream is = rs.getBinaryStream("avatar");
                    Image image;
                    if (is == null) image = ImageController.getImage("sample_avatar.jpg");
                    else image = new Image(is);
                    avatar.setImage(image);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        String employeeRecord = String.format("Employee: %d \nFull Name: %s \nEmail: %s \nOffice Code: %s", employeeNumber, getFullName(), email, officeCode);
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


    public ImageView getAvatar() {
        return avatar;
    }

    public void setAvatar(ImageView avatar) {
        this.avatar = avatar;
    }

    public StackPane getStatusBox() {
        return statusBox;
    }
    public String getStatus() {
        return ((Text)statusBox.getChildren().get(0)).getText();
    }

    public void setStatus(StackPane status) {
        this.statusBox = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
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
        return phoneCode;
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
