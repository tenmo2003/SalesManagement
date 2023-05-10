package salesmanagement.salesmanagement.ViewController.SettingsTab;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.Utils.ImageController;
import salesmanagement.salesmanagement.Utils.NotificationCode;
import salesmanagement.salesmanagement.Utils.NotificationSystem;
import salesmanagement.salesmanagement.ViewController.TabView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class SettingsTabView extends TabView implements SettingsTab {
    AccountActivityLogView accountActivityLogView;
    Employee user;
    @FXML
    private ComboBox<String> accessibilityComboBox;

    @FXML
    private ImageView avatarImageView;

    private String avatarURI = "";

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
    JFXButton saveInfo;

    @FXML
    private ProgressIndicator loadingIndicator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        try {
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/settings-tab/account-activity-log-view.fxml"));
            loader.load();
            accountActivityLogView = loader.getController();
            root.getChildren().add(accountActivityLogView.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void uploadAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Avatar Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            avatarImageView.setImage(new Image(selectedFile.toURI().toString()));
            avatarURI = selectedFile.getAbsolutePath();
        }
    }

    @FXML
    void showAccountActivityLog() {
        accountActivityLogView.show();
    }

    @FXML
    void saveInfo() {
        runTask(() -> {
            String query = String.format("UPDATE employees SET username = '%s', password = '%s', email = '%s' WHERE employeeNumber = %d", usernameTextField.getText(), passwordTextField.getText(), emailTextField.getText(), user.getEmployeeNumber());
                    try {
                        sqlConnection.updateQuery(query);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    if (avatarImageView.getImage() != null) {
                ImageController.uploadImage(avatarURI, "avatar_employee_" + user.getEmployeeNumber() + ".png");
            }
        }, () -> NotificationSystem.throwNotification(NotificationCode.SUCCEED_SAVE_INFO, stage),
                loadingIndicator, null);
    }

    public void setUser(Employee user) {
        this.user = user;
        accountActivityLogView.setUser(user);
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
                        lastWorkingDatePicker.setValue(employeeInfo.getDate("lastWorkingDate") != null ? employeeInfo.getDate("lastWorkingDate").toLocalDate() : null);

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
