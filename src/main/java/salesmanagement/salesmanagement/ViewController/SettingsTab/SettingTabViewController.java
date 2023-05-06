package salesmanagement.salesmanagement.ViewController.SettingsTab;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.Utils.ImageController;
import salesmanagement.salesmanagement.ViewController.TabView;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class SettingTabViewController extends TabView implements SettingsTabController {
    AccountActivityLogController accountActivityLogController;
    Employee user;
    @FXML
    private ComboBox<String> accessibilityComboBox;

    @FXML
    private ImageView avatarImageView;

    @FXML
    private DatePicker birthDatePicker;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField fullNameTextField;

    @FXML
    private DatePicker joinngDatePicker;

    @FXML
    private DatePicker lastWorkingDatePicker;

    @FXML
    private TextField officeCodeTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private ComboBox<String> phoneCodeComboBox;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField supervisorCode;

    @FXML
    private TextField usernameTextField;

    @FXML
    private ProgressIndicator loadingIndicator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/settings-tab/account-activity-log-view.fxml"));
            loader.load();
            accountActivityLogController = loader.getController();
            root.getChildren().add(accountActivityLogController.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void changeAvatar() {

    }

    @FXML
    void showAccountActivityLog() {
        accountActivityLogController.show();
    }

    public void setUser(Employee user) {
        this.user = user;
        accountActivityLogController.setUser(user);
    }

    @Override
    protected void figureShow() {
        super.figureShow();
        runTask(() -> {
            String query = "SELECT * FROM employees WHERE employeeNumber = " + user.getEmployeeNumber();
            ResultSet employeeInfo = sqlConnection.getDataQuery(query);
            Platform.runLater(() -> {
                try {
                    if (employeeInfo.next()) {
                        fullNameTextField.setText(employeeInfo.getString("lastName") + " " + employeeInfo.getString("firstName"));
                        birthDatePicker.setValue(employeeInfo.getDate("birthDate").toLocalDate());
                        officeCodeTextField.setText(employeeInfo.getString("officeCode"));
                        supervisorCode.setText(employeeInfo.getString("reportsTo"));

                        accessibilityComboBox.setValue(employeeInfo.getString("jobTitle"));
                        phoneCodeComboBox.setValue(employeeInfo.getString("phoneCode"));

                        phoneNumberTextField.setText(employeeInfo.getString("phone"));
                        joinngDatePicker.setValue(employeeInfo.getDate("joiningDate").toLocalDate());
                        lastWorkingDatePicker.setValue(employeeInfo.getDate("lastWorkingDate").toLocalDate());

                        usernameTextField.setText(employeeInfo.getString("username"));
                        passwordTextField.setText(employeeInfo.getString("password"));
                        emailTextField.setText(employeeInfo.getString("email"));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }, () -> isShowing = false, loadingIndicator, null);
        runTask(() -> avatarImageView.setImage(ImageController.getImage("avatar_employee_" + user.getEmployeeNumber() + ".png", true)), null, null, null);
    }
}
