package salesmanagement.salesmanagement.ViewController;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import salesmanagement.salesmanagement.ImageController;
import salesmanagement.salesmanagement.SalesComponent.Employee;
import salesmanagement.salesmanagement.SalesManagement;
import salesmanagement.salesmanagement.Utils;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static salesmanagement.salesmanagement.SceneController.SceneController.runTask;

public class SettingTabViewController extends ViewController {
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
    private VBox fixedInfoBox;

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
            FXMLLoader loader = new FXMLLoader(SalesManagement.class.getResource("fxml-view/account-activity-log-view.fxml"));
            loader.load();
            accountActivityLogController = loader.getController();
            root.getChildren().add(accountActivityLogController.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Lock all nodes editable in fixed info box.
        List<Class> disableNodeClasses = new ArrayList<>(Arrays.asList(TextField.class, PasswordField.class, DatePicker.class, ComboBox.class));
        for (Node node : Utils.getAllNodes(fixedInfoBox)) {
            for (Class disableNodeClass : disableNodeClasses) {
                if (disableNodeClass.isInstance(node)) {
                    node.setDisable(true);
                }
            }
        }
    }

    @FXML
    void changeAvatar(MouseEvent event) {

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
    public void show() {
        super.show();

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
            avatarImageView.setImage(ImageController.getImage("avatar_employee_" + user.getEmployeeNumber() + ".png", true));
        }, null, loadingIndicator, null);


    }
}
