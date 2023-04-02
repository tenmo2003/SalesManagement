package salesmanagement.salesmanagement.SalesComponent;

import com.jfoenix.controls.JFXButton;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import salesmanagement.salesmanagement.ImageController;
import salesmanagement.salesmanagement.SQLConnection;
import salesmanagement.salesmanagement.scenecontrollers.MainSceneController;
import salesmanagement.salesmanagement.scenecontrollers.SceneController;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @since 1.3
 */
public class Employee {
    private void convertGrayToGreen(ImageView imageView) {
        PixelReader pixelReader = imageView.getImage().getPixelReader();

        WritableImage writableImage = new WritableImage(pixelReader, (int) imageView.getFitWidth(), (int) imageView.getFitHeight());

        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int x = 0; x < (int) imageView.getFitWidth(); x++) {
            for (int y = 0; y < (int) imageView.getFitHeight(); y++) {
                Color color = pixelReader.getColor(x, y);

                if (color.getRed() == color.getGreen() && color.getGreen() == color.getBlue()) {
                    Color newColor = Color.GREEN;
                    pixelWriter.setColor(x, y, newColor);
                } else {
                    pixelWriter.setColor(x, y, color);
                }
            }
        }

        imageView.setImage(writableImage);
    }

    private int employeeNumber;
    private String lastName;
    private String firstName;
    private String email;
    private String officeCode;
    private int reportsTo;
    private String jobTitle;
    private ImageView avatar = new ImageView();
    private static SQLConnection sqlConnection;
    private MainSceneController mainSceneController;
    private HBox operation = new HBox();

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

            try {
                PreparedStatement ps = sqlConnection.getConnection().prepareStatement("SELECT avatar FROM employees WHERE employeeNumber = ?");
                ps.setInt(1, employeeNumber);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    InputStream is = rs.getBinaryStream("avatar");
                    Image image = new Image(is);
                    avatar.setImage(image);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.mainSceneController = mainSceneController;
        // Operation is a hbox include
        JFXButton removeButton = new JFXButton();
        ImageView removeImg = new ImageView(ImageController.getImage("remove.png"));
        removeImg.setFitWidth(20);
        removeImg.setFitHeight(20);
        removeButton.setGraphic(removeImg);

        JFXButton editButton = new JFXButton();
        ImageView editImg = new ImageView(ImageController.getImage("edit.png"));
        editImg.setFitWidth(20);
        editImg.setFitHeight(20);
        editButton.setGraphic(editImg);

        operation.getChildren().add(editButton);
        operation.getChildren().add(removeButton);

        removeButton.setOnMouseClicked(event -> {
            removeEmployee();
        });


        editButton.setOnMouseClicked(event -> {
            mainSceneController.editEmployees(this);
        });
    }

    public void editEmployeeInfo() {

    }

    private void removeEmployee() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Modifying Confirmation!");
        alert.setContentText(this.toString());
        alert.setHeaderText("You are deleting this employee permanently: ");
        alert.initOwner(mainSceneController.getStage());
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/myDialogs.css")).toExternalForm());
        dialogPane.getStyleClass().add("myDialog");

        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                mainSceneController.getEmployees().remove(this);
                MainSceneController.haveChangeInEmployeesTab = true;
                try {
                    String sql = "DELETE FROM employees WHERE employeeNumber = ?";
                    PreparedStatement pstmt = sqlConnection.getConnection().prepareStatement(sql);
                    pstmt.setInt(1, employeeNumber);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

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

    public HBox getOperation() {
        return operation;
    }

    public void setOperation(HBox operation) {
        this.operation = operation;
    }

    public ImageView getAvatar() {
        return avatar;
    }

    public void setAvatar(ImageView avatar) {
        this.avatar = avatar;
    }
}
